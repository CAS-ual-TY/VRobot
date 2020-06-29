package de.cas_ual_ty.vrobot.registries;

import de.cas_ual_ty.vrobot.RobotItem;
import de.cas_ual_ty.vrobot.VRobot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = VRobot.MOD_ID, bus = Bus.MOD)
@ObjectHolder(VRobot.MOD_ID)
public class VRobotItems
{
    public static final RobotItem ROBOT = null;
    
    @SubscribeEvent
    public static void register(Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        
        registry.register(new RobotItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)).setRegistryName(VRobot.MOD_ID, "robot"));
    }
}
