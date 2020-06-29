package de.cas_ual_ty.vrobot.registries;

import de.cas_ual_ty.vrobot.RobotInventoryContainer;
import de.cas_ual_ty.vrobot.VRobot;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = VRobot.MOD_ID, bus = Bus.MOD)
@ObjectHolder(VRobot.MOD_ID)
public class VRobotContainerTypes
{
    public static final ContainerType<RobotInventoryContainer> ROBOT_INVENTORY = null;
    
    @SubscribeEvent
    public static void register(Register<ContainerType<?>> event)
    {
        IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
        
        registry.register(new ContainerType<>((IContainerFactory<RobotInventoryContainer>)RobotInventoryContainer::new).setRegistryName(VRobot.MOD_ID, "robot_inventory"));
    }
}