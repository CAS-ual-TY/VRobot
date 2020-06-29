package de.cas_ual_ty.vrobot.registries;

import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.VRobot;
import de.cas_ual_ty.vrobot.action.MotionRobotActionType;
import de.cas_ual_ty.vrobot.action.PredicateRobotActionType;
import de.cas_ual_ty.vrobot.action.RobotAction;
import de.cas_ual_ty.vrobot.action.RobotActionType;
import de.cas_ual_ty.vrobot.action.SingleRobotActionType;
import de.cas_ual_ty.vrobot.action.TimedRobotActionType;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = VRobot.MOD_ID, bus = Bus.MOD)
@ObjectHolder(VRobot.MOD_ID)
public class VRobotActionTypes
{
    public static final RobotActionType IDLE = null;
    public static final RobotActionType IDLE_FAILURE = null;
    public static final RobotActionType PAUSE = null;
    public static final RobotActionType END = null;
    public static final RobotActionType FORWARD = null;
    public static final RobotActionType UP = null;
    public static final RobotActionType DOWN = null;
    public static final RobotActionType TURN_LEFT = null;
    public static final RobotActionType TURN_RIGHT = null;
    public static final RobotActionType PAUSE_1_SEC = null;
    public static final RobotActionType NEXT_SLOT = null;
    public static final RobotActionType PREV_SLOT = null;
    public static final RobotActionType RESET_SLOT = null;
    public static final RobotActionType HARVEST = null;
    public static final RobotActionType HARVEST_BELOW = null;
    public static final RobotActionType HARVEST_ABOVE = null;
    
    public static final RobotActionType DEBUG = null; //TODO
    
    @SubscribeEvent
    public static void register(Register<RobotActionType> event)
    {
        IForgeRegistry<RobotActionType> registry = event.getRegistry();
        
        registry.register(new PredicateRobotActionType((r, i) -> false).setRegistryName(VRobot.MOD_ID, "idle"));
        registry.register(new PredicateRobotActionType((r, i) -> false).setRegistryName(VRobot.MOD_ID, "idle_failure"));
        registry.register(new SingleRobotActionType((r, i) -> r.pause()).setRegistryName(VRobot.MOD_ID, "pause"));
        registry.register(new SingleRobotActionType((r, i) -> r.continuePaused()).setRegistryName(VRobot.MOD_ID, "continue"));
        registry.register(new SingleRobotActionType((r, i) -> r.nodeEnd()).setRegistryName(VRobot.MOD_ID, "end"));
        registry.register(new MotionRobotActionType(20, (r) -> new Vec3d(r.getDirectionFromYaw().getDirectionVec())).setRegistryName(VRobot.MOD_ID, "forward"));
        registry.register(new MotionRobotActionType(20, (r) -> new Vec3d(0, 1, 0)).setRegistryName(VRobot.MOD_ID, "up"));
        registry.register(new MotionRobotActionType(20, (r) -> new Vec3d(0, -1, 0)).setRegistryName(VRobot.MOD_ID, "down"));
        registry.register(new TimedRobotActionType(20)
        {
            @Override
            public void tick(RobotEntity robot, RobotAction instance)
            {
                float degree = 90F * 0.05F;
                robot.setPositionAndRotation(robot.getPosX(), robot.getPosY(), robot.getPosZ(), robot.rotationYaw - degree, robot.rotationPitch);
            }
            
            @Override
            public void onSetDead(RobotEntity robot, RobotAction instance)
            {
                robot.fixPositionRotation();
            }
        }.setRegistryName(VRobot.MOD_ID, "turn_left"));
        registry.register(new TimedRobotActionType(20)
        {
            @Override
            public void tick(RobotEntity robot, RobotAction instance)
            {
                float degree = 90F * 0.05F;
                robot.setPositionAndRotation(robot.getPosX(), robot.getPosY(), robot.getPosZ(), robot.rotationYaw + degree, robot.rotationPitch);
            }
            
            @Override
            public void onSetDead(RobotEntity robot, RobotAction instance)
            {
                robot.fixPositionRotation();
            }
        }.setRegistryName(VRobot.MOD_ID, "turn_right"));
        registry.register(new TimedRobotActionType(20).setRegistryName(VRobot.MOD_ID, "pause_1_sec"));
        registry.register(new SingleRobotActionType((r, i) -> r.nextActiveSlot()).setRegistryName(VRobot.MOD_ID, "next_slot"));
        registry.register(new SingleRobotActionType((r, i) -> r.prevActiveSlot()).setRegistryName(VRobot.MOD_ID, "prev_slot"));
        registry.register(new SingleRobotActionType((r, i) -> r.setActiveSlot(0)).setRegistryName(VRobot.MOD_ID, "reset_slot"));
        registry.register(new RobotActionType()
        {
            @Override
            public void onStart(RobotEntity robot, RobotAction instance)
            {
                super.onStart(robot, instance);
                
                BlockPos pos = robot.getForwardPos();
                
                if(!robot.world.isAirBlock(pos))
                {
                    robot.getActiveItemStack().onBlockStartBreak(pos, robot.getFakePlayer());
                }
            }
            
            @Override
            public void tick(RobotEntity robot, RobotAction instance)
            {
                super.tick(robot, instance);
                
                BlockPos pos = robot.getForwardPos();
                
                if(!robot.world.isAirBlock(pos))
                {
                    robot.world.sendBlockBreakProgress(robot.getEntityId(), pos, ((instance.getTime()) * 10) / robot.getCurrentDestroyTime(pos));
                }
            }
            
            @Override
            public boolean isDone(RobotEntity robot, RobotAction instance)
            {
                BlockPos pos = robot.getForwardPos();
                
                if(robot.world.isAirBlock(pos))
                {
                    return true;
                }
                
                if(instance.getTime() >= robot.getCurrentDestroyTime(pos))
                {
                    BlockState state = robot.world.getBlockState(pos);
                    ItemStack itemStack = robot.getActiveItemStack();
                    
                    itemStack.onBlockDestroyed(robot.world, state, pos, robot.getFakePlayer());
                    robot.insertItems(robot.getDrops(itemStack, pos, state));
                    robot.world.removeBlock(pos, false);
                    
                    return true;
                }
                
                return false;
            }
        }.setRegistryName(VRobot.MOD_ID, "harvest"));
        
        registry.register(new RobotActionType()
        {
            @Override
            public void onStart(RobotEntity robot, RobotAction instance)
            {
                super.onStart(robot, instance);
                
                BlockPos pos = robot.getPosition().down();
                
                if(!robot.world.isAirBlock(pos))
                {
                    robot.getActiveItemStack().onBlockStartBreak(pos, robot.getFakePlayer());
                }
            }
            
            @Override
            public void tick(RobotEntity robot, RobotAction instance)
            {
                super.tick(robot, instance);
                
                BlockPos pos = robot.getPosition().down();
                
                if(!robot.world.isAirBlock(pos))
                {
                    robot.world.sendBlockBreakProgress(robot.getEntityId(), pos, ((instance.getTime()) * 10) / robot.getCurrentDestroyTime(pos));
                }
            }
            
            @Override
            public boolean isDone(RobotEntity robot, RobotAction instance)
            {
                BlockPos pos = robot.getPosition().down();
                
                if(robot.world.isAirBlock(pos))
                {
                    return true;
                }
                
                if(instance.getTime() >= robot.getCurrentDestroyTime(pos))
                {
                    BlockState state = robot.world.getBlockState(pos);
                    ItemStack itemStack = robot.getActiveItemStack();
                    
                    itemStack.onBlockDestroyed(robot.world, state, pos, robot.getFakePlayer());
                    robot.insertItems(robot.getDrops(itemStack, pos, state));
                    robot.world.removeBlock(pos, false);
                    
                    return true;
                }
                
                return false;
            }
        }.setRegistryName(VRobot.MOD_ID, "harvest_below"));
        
        registry.register(new RobotActionType()
        {
            @Override
            public void onStart(RobotEntity robot, RobotAction instance)
            {
                super.onStart(robot, instance);
                
                BlockPos pos = robot.getPosition().up();
                
                if(!robot.world.isAirBlock(pos))
                {
                    robot.getActiveItemStack().onBlockStartBreak(pos, robot.getFakePlayer());
                }
            }
            
            @Override
            public void tick(RobotEntity robot, RobotAction instance)
            {
                super.tick(robot, instance);
                
                BlockPos pos = robot.getPosition().up();
                
                if(!robot.world.isAirBlock(pos))
                {
                    robot.world.sendBlockBreakProgress(robot.getEntityId(), pos, ((instance.getTime()) * 10) / robot.getCurrentDestroyTime(pos));
                }
            }
            
            @Override
            public boolean isDone(RobotEntity robot, RobotAction instance)
            {
                BlockPos pos = robot.getPosition().up();
                
                if(robot.world.isAirBlock(pos))
                {
                    return true;
                }
                
                if(instance.getTime() >= robot.getCurrentDestroyTime(pos))
                {
                    BlockState state = robot.world.getBlockState(pos);
                    ItemStack itemStack = robot.getActiveItemStack();
                    
                    itemStack.onBlockDestroyed(robot.world, state, pos, robot.getFakePlayer());
                    robot.insertItems(robot.getDrops(itemStack, pos, state));
                    robot.world.removeBlock(pos, false);
                    
                    return true;
                }
                
                return false;
            }
        }.setRegistryName(VRobot.MOD_ID, "harvest_above"));
        
        registry.register(new PredicateRobotActionType((r, i) ->
        {
            BlockPos pos = r.getPosition().add(r.getDirectionFromYaw().getDirectionVec());
            BlockState s = r.world.getBlockState(pos);
            
            if(!s.isAir(r.world, pos))
            {
                ItemStack itemStack;
                
                itemStack = ItemStack.EMPTY;
                System.out.println(r.getDestroyTime(itemStack, s, pos) + " " + r.getDrops(itemStack, pos, s));
                
                itemStack = new ItemStack(Items.IRON_SHOVEL);
                System.out.println(r.getDestroyTime(itemStack, s, pos) + " " + r.getDrops(itemStack, pos, s));
                
                itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                itemStack.addEnchantment(Enchantments.SILK_TOUCH, 1);
                System.out.println(r.getDestroyTime(itemStack, s, pos) + " " + r.getDrops(itemStack, pos, s));
            }
            
            return true;
        }).setRegistryName(VRobot.MOD_ID, "debug"));
    }
}