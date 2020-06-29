package de.cas_ual_ty.vrobot.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.cas_ual_ty.visibilis.print.Print;
import de.cas_ual_ty.visibilis.util.VNBTUtility;
import de.cas_ual_ty.visibilis.util.VUtility;
import de.cas_ual_ty.vrobot.RobotEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SynchRobotToClientMessage
{
    public final int entityId;
    public final CompoundNBT nbt;
    public final boolean working;
    public final boolean paused;
    public final boolean openGuiForClient;
    public final List<ItemStack> inventory;
    
    public SynchRobotToClientMessage(RobotEntity entity)
    {
        this(entity, false);
    }
    
    public SynchRobotToClientMessage(RobotEntity entity, boolean openForClient)
    {
        this(entity.getEntityId(), VNBTUtility.savePrintToNBT(entity.getPrint(), false), entity.isWorking(), entity.isPaused(), openForClient, entity.getItemStackList());
    }
    
    public SynchRobotToClientMessage(int entityId, CompoundNBT nbt, boolean working, boolean paused, boolean openForClient, List<ItemStack> inventory)
    {
        this.nbt = nbt;
        this.entityId = entityId;
        this.working = working;
        this.paused = paused;
        this.openGuiForClient = openForClient;
        this.inventory = inventory;
    }
    
    public static void encode(SynchRobotToClientMessage msg, PacketBuffer buf)
    {
        buf.writeInt(msg.entityId);
        buf.writeCompoundTag(msg.nbt);
        buf.writeBoolean(msg.working);
        buf.writeBoolean(msg.paused);
        buf.writeBoolean(msg.openGuiForClient);
        
        buf.writeInt(msg.inventory.size());
        for(ItemStack stack : msg.inventory)
        {
            buf.writeItemStack(stack, false);
        }
    }
    
    public static SynchRobotToClientMessage decode(PacketBuffer buf)
    {
        int entityId = buf.readInt();
        CompoundNBT nbt = buf.readCompoundTag();
        boolean working = buf.readBoolean();
        boolean paused = buf.readBoolean();
        boolean openForClient = buf.readBoolean();
        
        int size = buf.readInt();
        List<ItemStack> list = new ArrayList<>(size);
        for(int i = 0; i < size; ++i)
        {
            list.add(buf.readItemStack());
        }
        
        return new SynchRobotToClientMessage(entityId, nbt, working, paused, openForClient, list);
    }
    
    public static void handle(SynchRobotToClientMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        Context context = ctx.get();
        
        context.enqueueWork(() ->
        {
            World world = VUtility.getWorld(context);
            Entity entity = world.getEntityByID(msg.entityId);
            
            if(entity instanceof RobotEntity)
            {
                RobotEntity robot = (RobotEntity)entity;
                
                Print print = robot.getPrint();
                print.overrideFromNBT(msg.nbt);
                
                robot.setWorking(msg.working);
                robot.setPaused(msg.paused);
                robot.setItemStackList(msg.inventory);
                
                if(msg.openGuiForClient)
                {
                    robot.openGui(VUtility.getPlayer(context));
                }
            }
        });
        
        ctx.get().setPacketHandled(true);
    }
}
