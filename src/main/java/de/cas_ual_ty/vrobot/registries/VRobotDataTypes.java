package de.cas_ual_ty.vrobot.registries;

import de.cas_ual_ty.visibilis.datatype.DataType;
import de.cas_ual_ty.vrobot.VRobot;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = VRobot.MOD_ID, bus = Bus.MOD)
@ObjectHolder(VRobot.MOD_ID)
public class VRobotDataTypes
{
    public static final DataType<IItemHandler> ITEM_HANDLER = null;
    
    @SubscribeEvent
    public static void register(Register<DataType<?>> event)
    {
        IForgeRegistry<DataType<?>> registry = event.getRegistry();
        
        registry.register(new DataType<>((size) -> new IItemHandler[size]).setColor(new float[] { 1F, 0.663F, 0.192F }).setRegistryName(VRobot.MOD_ID, "item_handler"));
    }
}
