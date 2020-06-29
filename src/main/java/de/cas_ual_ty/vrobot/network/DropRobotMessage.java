package de.cas_ual_ty.vrobot.network;

import java.util.function.Supplier;

import de.cas_ual_ty.vrobot.RobotEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class DropRobotMessage
{
    public int entityId;
    
    public DropRobotMessage(int entityId)
    {
        this.entityId = entityId;
    }
    
    public static void encode(DropRobotMessage msg, PacketBuffer buf)
    {
        buf.writeInt(msg.entityId);
    }
    
    public static DropRobotMessage decode(PacketBuffer buf)
    {
        return new DropRobotMessage(buf.readInt());
    }
    
    public static void handle(DropRobotMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        Context context = ctx.get();
        
        context.enqueueWork(() ->
        {
            if(context.getSender().getEntityWorld().getEntityByID(msg.entityId) != null)
            {
                Entity e = context.getSender().getEntityWorld().getEntityByID(msg.entityId);
                
                if(e instanceof RobotEntity)
                {
                    ((RobotEntity)e).dropAsItem();
                }
            }
        });
        
        ctx.get().setPacketHandled(true);
    }
}
