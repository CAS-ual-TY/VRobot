package de.cas_ual_ty.vrobot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.mojang.authlib.GameProfile;

import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.visibilis.print.Print;
import de.cas_ual_ty.visibilis.print.entity.EntityPrint;
import de.cas_ual_ty.visibilis.print.provider.DataProvider;
import de.cas_ual_ty.vrobot.action.RobotAction;
import de.cas_ual_ty.vrobot.network.SynchRobotToClientMessage;
import de.cas_ual_ty.vrobot.provider.RobotDataKey;
import de.cas_ual_ty.vrobot.registries.VRobotActionTypes;
import de.cas_ual_ty.vrobot.registries.VRobotItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class RobotEntity extends EntityPrint implements INamedContainerProvider, IEntityAdditionalSpawnData
{
    public static final String KEY_WORKING = "working";
    public static final String KEY_INITIALIZED = "initialized";
    public static final String KEY_PAUSED = "paused";
    public static final String KEY_ITEM_HANDLER = "item_handler";
    public static final String KEY_ENERGY_STORAGE = "energy_storage";
    public static final String KEY_ACTIONS = "actions";
    public static final String KEY_ACTIVE_ACTION = "activeAction";
    public static final String KEY_ACTIVE_SLOT = "activeSlot";
    
    public static final String EVENT_INITIALIZE = "initialize";
    public static final String EVENT_TICK = "tick";
    
    protected LinkedList<RobotAction> actions;
    protected AxisAlignedBB collisionBB;
    
    private static final DataParameter<String> ACTIVE_ACTION = EntityDataManager.createKey(RobotEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> ACTIVE_SLOT = EntityDataManager.createKey(RobotEntity.class, DataSerializers.VARINT);
    
    protected final IItemHandler itemHandler;
    protected final IEnergyStorage energyStorage;
    
    protected final LazyOptional<IItemHandler> itemHandlerOptional;
    protected final LazyOptional<IEnergyStorage> energyStorageOptional;
    
    protected boolean working;
    protected boolean initialized;
    protected boolean paused;
    protected int activeSlot;
    
    protected RobotAction activeAction;
    
    protected FakePlayer fakePlayer;
    
    public RobotEntity(EntityType<?> entityTypeIn, World worldIn)
    {
        super(entityTypeIn, worldIn);
        this.actions = new LinkedList<>();
        double d1 = -0.4D;
        double d2 = -d1;
        this.collisionBB = new AxisAlignedBB(d1, d1, d1, d2, d2, d2);
        Direction d = Direction.NORTH;
        this.rotationYaw = d.getHorizontalAngle();
        this.rotationPitch = 0;
        this.itemHandler = new ItemStackHandler(27);
        this.energyStorage = new EnergyStorage(100);
        this.itemHandlerOptional = LazyOptional.of(() -> this.itemHandler);
        this.energyStorageOptional = LazyOptional.of(() -> this.energyStorage);
        this.working = false;
        this.initialized = false;
        this.paused = false;
        this.activeSlot = 0;
        this.activeAction = null;
    }
    
    public void fixPositionRotation()
    {
        this.rotationYaw = Math.round(this.rotationYaw / 90) * 90;
        Vec3i pos = this.getPosition();
        double d = 0.5D;
        this.setPosition(pos.getX() + d, pos.getY() + d, pos.getZ() + d);
        this.setMotion(0, 0, 0);
    }
    
    @Override
    protected void registerData()
    {
        this.dataManager.register(RobotEntity.ACTIVE_ACTION, this.activeAction != null ? this.activeAction.actionType.getRegistryName().toString() : VRobotActionTypes.IDLE.getRegistryName().toString());
        this.dataManager.register(RobotEntity.ACTIVE_SLOT, this.activeSlot);
    }
    
    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    public Function<Print, DataProvider> createDataProvider()
    {
        return (print) -> new DataProvider(print, this).addData(RobotDataKey.KEY_ROBOT, this);
    }
    
    @Override
    public List<NodeType<?>> getNodeTypeList()
    {
        return VRobot.ROBOT_NODE_TYPES;
    }
    
    @Override
    public void doOpenGui()
    {
        VRobot.proxy.openRobotGui(this.getPrintProvider(), this);
    }
    
    public Direction getDirectionFromYaw()
    {
        Vec3d vec = this.getLookVec();
        return Direction.getFacingFromVector(vec.x, vec.y, vec.z);
    }
    
    @Override
    public void baseTick()
    {
        super.baseTick();
        
        if(this.world.isRemote)
        {
            return;
        }
        
        if(this.tickHandleActions() && this.working)
        {
            if(!this.initialized)
            {
                this.executeEvent(VRobot.MOD_ID, RobotEntity.EVENT_INITIALIZE);
                this.initialized = true;
            }
            
            this.executeEvent(VRobot.MOD_ID, RobotEntity.EVENT_TICK);
        }
        
        Vec3d pos = this.getPositionVector().add(this.getMotion().scale(0.05D));
        
        this.setPosition(pos.x, pos.y, pos.z);
    }
    
    public boolean tickHandleActions()
    {
        if(this.world.isRemote)
        {
            return false;
        }
        
        if(this.activeAction == null)
        {
            RobotAction next = this.getNextAction();
            
            if(next != null)
            {
                this.setActiveAction(next);
            }
            else
            {
                return true;
            }
        }
        
        RobotAction action = this.activeAction;
        
        if(!action.hasBegun() && !this.working)
        {
            return true;
        }
        
        action.tick(this);
        
        if(action.isDead())
        {
            this.activeAction = null;
        }
        
        return false;
    }
    
    protected RobotAction getNextAction()
    {
        if(this.world.isRemote)
        {
            return new RobotAction(VRobotActionTypes.IDLE);
        }
        else
        {
            return this.actions.poll();
        }
    }
    
    public LinkedList<RobotAction> getActions()
    {
        return this.actions;
    }
    
    public void addAction(RobotAction action)
    {
        this.actions.add(action);
    }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }
    
    @Override
    protected AxisAlignedBB getBoundingBox(Pose p_213321_1_)
    {
        return this.getCollisionBoundingBox();
    }
    
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return this.getCollisionBoundingBox();
    }
    
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return this.getCollisionBoundingBox();
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.collisionBB.offset(this.getPositionVector());
    }
    
    public void playerInteract(PlayerEntity player)
    {
        if(!this.world.isRemote)
        {
            if(this.isWorking())
            {
                this.pause();
            }
            //            this.synchToTrackers();
            this.openGui(player);
        }
    }
    
    public ItemStack getActiveItemStack()
    {
        return this.getItemHandler().getStackInSlot(this.getActiveSlot());
    }
    
    public int getActiveSlot()
    {
        return this.activeSlot;
    }
    
    public int getActiveSlotFromDataManager()
    {
        return this.dataManager.get(RobotEntity.ACTIVE_SLOT);
    }
    
    public void setActiveSlot(int slot)
    {
        if(slot >= 0 && slot < this.getItemHandler().getSlots())
        {
            this.activeSlot = slot;
            this.dataManager.set(RobotEntity.ACTIVE_SLOT, this.activeSlot);
            this.synchToTrackers();
        }
    }
    
    public void nextActiveSlot()
    {
        this.setActiveSlot(++this.activeSlot % this.getItemHandler().getSlots());
    }
    
    public void prevActiveSlot()
    {
        this.setActiveSlot(--this.activeSlot % this.getItemHandler().getSlots());
    }
    
    protected void setActiveAction(RobotAction action)
    {
        this.activeAction = action;
        this.dataManager.set(RobotEntity.ACTIVE_ACTION, action.actionType.getRegistryName().toString());
    }
    
    protected void reset()
    {
        this.initialized = false;
        this.working = false;
        this.paused = false;
        this.actions.clear();
        this.activeSlot = 0;
        this.fixPositionRotation();
        this.getPrint().getVariablesMap().clear();
    }
    
    protected void stop()
    {
        this.reset();
        this.fixPositionRotation();
    }
    
    public void errorStop()
    {
        this.stop();
        this.setActiveAction(new RobotAction(VRobotActionTypes.IDLE_FAILURE));
    }
    
    public void buttonStart()
    {
        this.reset();
        this.working = true;
        this.activeAction = null;
        this.synchToTrackers();
    }
    
    public void buttonStop()
    {
        this.nodeEnd();
    }
    
    public void nodeEnd()
    {
        this.stop();
        this.setActiveAction(new RobotAction(VRobotActionTypes.IDLE));
    }
    
    public boolean isWorking()
    {
        return this.working;
    }
    
    public boolean isPaused()
    {
        return this.paused;
    }
    
    public RobotEntity setWorking(boolean working)
    {
        this.working = working;
        return this;
    }
    
    public RobotEntity setPaused(boolean paused)
    {
        this.paused = paused;
        return this;
    }
    
    public void continuePaused()
    {
        if(this.isPaused())
        {
            this.paused = false;
            this.working = true;
            this.synchToTrackers();
        }
    }
    
    public void pause()
    {
        this.paused = true;
        this.working = false;
    }
    
    @Override
    public boolean openGui(PlayerEntity player)
    {
        World world = this.world;
        
        if(player.world == world)
        {
            if(world.isRemote)
            {
                this.doOpenGui();
                return true;
            }
            else if(player instanceof ServerPlayerEntity)
            {
                VRobot.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new SynchRobotToClientMessage(this, true));
                return true;
            }
        }
        
        return false;
    }
    
    public void openInventory(ServerPlayerEntity player)
    {
        NetworkHooks.openGui(player, this, buf -> buf.writeInt(RobotEntity.this.getEntityId()));
    }
    
    @Override
    public void synchToTrackers()
    {
        VRobot.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> (Entity)this), new SynchRobotToClientMessage(this, false));
    }
    
    public ItemStack createItemStack()
    {
        ItemStack itemStack = new ItemStack(VRobotItems.ROBOT);
        VRobotItems.ROBOT.setPrint(itemStack, this.getPrint().clone());
        return itemStack;
    }
    
    public void dropAsItem()
    {
        this.entityDropItem(this.createItemStack());
        
        List<ItemStack> list = this.getItemStackList();
        for(ItemStack itemStack : list)
        {
            this.entityDropItem(itemStack);
        }
        
        this.remove();
    }
    
    @Override
    protected void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        this.working = compound.getBoolean(RobotEntity.KEY_WORKING);
        this.initialized = compound.getBoolean(RobotEntity.KEY_INITIALIZED);
        this.paused = compound.getBoolean(RobotEntity.KEY_PAUSED);
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getItemHandler(), null, compound.get(RobotEntity.KEY_ITEM_HANDLER));
        CapabilityEnergy.ENERGY.getStorage().readNBT(CapabilityEnergy.ENERGY, this.getEnergyStorage(), null, compound.get(RobotEntity.KEY_ENERGY_STORAGE));
        
        if(compound.hasUniqueId(RobotEntity.KEY_ACTIVE_ACTION))
        {
            this.activeAction = RobotAction.fromNBT(compound.getCompound(RobotEntity.KEY_ACTIVE_ACTION));
        }
        
        ListNBT list = compound.getList(RobotEntity.KEY_ACTIONS, 10);
        for(INBT nbt : list)
        {
            this.actions.add(RobotAction.fromNBT((CompoundNBT)nbt));
        }
        
        this.activeSlot = compound.getInt(RobotEntity.KEY_ACTIVE_SLOT);
    }
    
    @Override
    protected void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean(RobotEntity.KEY_WORKING, this.working);
        compound.putBoolean(RobotEntity.KEY_INITIALIZED, this.initialized);
        compound.putBoolean(RobotEntity.KEY_PAUSED, this.paused);
        compound.put(RobotEntity.KEY_ITEM_HANDLER, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getItemHandler(), null));
        compound.put(RobotEntity.KEY_ENERGY_STORAGE, CapabilityEnergy.ENERGY.getStorage().writeNBT(CapabilityEnergy.ENERGY, this.getEnergyStorage(), null));
        
        compound.remove(RobotEntity.KEY_ACTIVE_ACTION);
        if(this.activeAction != null)
        {
            compound.put(RobotEntity.KEY_ACTIVE_ACTION, this.activeAction.toNBT());
        }
        
        ListNBT list = new ListNBT();
        for(RobotAction i : this.actions)
        {
            list.add(i.toNBT());
        }
        compound.put(RobotEntity.KEY_ACTIONS, list);
        
        compound.putInt(RobotEntity.KEY_ACTIVE_SLOT, this.activeSlot);
    }
    
    @Override
    public void addVelocity(double x, double y, double z)
    {
        super.addVelocity(x, y, z);
        this.markVelocityChanged();
    }
    
    @Override
    public void setMotion(double x, double y, double z)
    {
        super.setMotion(x, y, z);
        this.markVelocityChanged();
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, this.itemHandlerOptional);
        }
        else if(cap == CapabilityEnergy.ENERGY)
        {
            return CapabilityEnergy.ENERGY.orEmpty(cap, this.energyStorageOptional);
        }
        
        return super.getCapability(cap, side);
    }
    
    public List<ItemStack> getItemStackList()
    {
        List<ItemStack> list = new ArrayList<>(this.getItemHandler().getSlots());
        
        for(int i = 0; i < this.getItemHandler().getSlots(); ++i)
        {
            list.add(this.getItemHandler().getStackInSlot(i));
        }
        
        return list;
    }
    
    public void setItemStackList(List<ItemStack> list) // Only for Server->Client synch
    {
        if(this.world.isRemote)
        {
            for(int i = 0; i < this.getItemHandler().getSlots(); ++i)
            {
                this.getItemHandler().extractItem(i, this.getItemHandler().getSlotLimit(i), false);
                this.getItemHandler().insertItem(i, list.get(i), false);
            }
        }
    }
    
    public IItemHandler getItemHandler()
    {
        return this.itemHandler;
    }
    
    public IEnergyStorage getEnergyStorage()
    {
        return this.energyStorage;
    }
    
    public BlockPos getForwardPos()
    {
        return this.getPosition().add(this.getDirectionFromYaw().getDirectionVec());
    }
    
    public int getCurrentDestroyTime(BlockPos pos)
    {
        return Math.max(1, this.getDestroyTime(this.getActiveItemStack(), this.world.getBlockState(pos), pos));
    }
    
    public int getDestroyTime(ItemStack itemStack, BlockState block, BlockPos pos) //From Block#getPlayerRelativeBlockHardness
    {
        float f = itemStack.getDestroySpeed(block);
        if(f > 1.0F && !itemStack.isEmpty())
        {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, itemStack);
            if(i > 0)
            {
                f += (float)(i * i + 1);
            }
        }
        
        float speed = f / block.getBlockHardness(this.world, pos) / this.canHarvestBlockTime(itemStack, block, pos);
        return Math.round(1F / speed);
    }
    
    public float canHarvestBlockTime(ItemStack itemStack, BlockState block, BlockPos pos) //From Block#getPlayerRelativeBlockHardness
    {
        return this.canHarvestBlock(itemStack, block, pos) ? 30F : 100F;
    }
    
    public boolean canHarvestBlock(ItemStack stack, BlockState state, BlockPos pos) //From ForgeHooks#canHarvestBlock
    {
        if(state.getMaterial().isToolNotRequired())
        {
            return true;
        }
        
        ToolType tool = state.getHarvestTool();
        if(tool == null)
        {
            return true;
        }
        else if(stack.isEmpty())
        {
            return false;
        }
        
        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, null, state);
        if(toolLevel < 0)
        {
            return false;
        }
        
        return toolLevel >= state.getHarvestLevel();
    }
    
    public List<ItemStack> getDrops(ItemStack itemStack, BlockPos pos, BlockState block)
    {
        if(this.world.isRemote || !this.canHarvestBlock(itemStack, block, pos))
        {
            return new ArrayList<>(0);
        }
        else
        {
            return Block.getDrops(block, (ServerWorld)this.world, pos, null, this, itemStack);
        }
    }
    
    public void insertItems(List<ItemStack> list)
    {
        ItemStack active;
        
        for(ItemStack itemStack : list)
        {
            active = itemStack;
            
            for(int i = 0; i < this.getItemHandler().getSlots(); ++i)
            {
                active = this.getItemHandler().insertItem(i, active, false);
                
                if(active.isEmpty())
                {
                    break;
                }
            }
            
            if(!active.isEmpty())
            {
                this.entityDropItem(active);
            }
        }
    }
    
    @Override
    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player)
    {
        return new RobotInventoryContainer(id, inv, this);
    }
    
    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.vrobot.robot_inventory");
    }
    
    public FakePlayer getFakePlayer()
    {
        if(!this.world.isRemote && this.fakePlayer == null)
        {
            this.fakePlayer = new FakePlayer((ServerWorld)this.world, new GameProfile(this.getUniqueID(), "robot"));
        }
        this.fakePlayer.setPosition(this.getPosX(), this.getPosY(), this.getPosY());
        this.fakePlayer.rotationYaw = this.rotationYaw;
        this.fakePlayer.rotationPitch = this.rotationPitch;
        return this.fakePlayer;
    }
    
    @Override
    public boolean isGlowing()
    {
        return !this.isWorking();
    }
    
    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        SynchRobotToClientMessage.encode(new SynchRobotToClientMessage(this), buffer);
    }
    
    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        SynchRobotToClientMessage msg = SynchRobotToClientMessage.decode(buffer);
        
        Print print = this.getPrint();
        print.overrideFromNBT(msg.nbt);
        this.setWorking(msg.working);
        this.setPaused(msg.paused);
        this.setItemStackList(msg.inventory);
    }
}
