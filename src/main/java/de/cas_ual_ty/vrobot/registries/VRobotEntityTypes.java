package de.cas_ual_ty.vrobot.registries;

import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.VRobot;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = VRobot.MOD_ID, bus = Bus.MOD)
@ObjectHolder(VRobot.MOD_ID)
public class VRobotEntityTypes
{
    public static final EntityType<RobotEntity> ROBOT = null;
    
    @SubscribeEvent
    public static void register(Register<EntityType<?>> event)
    {
        IForgeRegistry<EntityType<?>> registry = event.getRegistry();
        
        registry.register(EntityType.Builder.<RobotEntity>create(RobotEntity::new, EntityClassification.MISC).setShouldReceiveVelocityUpdates(true).size(1, 1).build("robot").setRegistryName(VRobot.MOD_ID, "robot"));
    }
}
