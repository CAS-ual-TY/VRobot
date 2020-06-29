package de.cas_ual_ty.vrobot;

import de.cas_ual_ty.vrobot.action.RobotActionType;
import de.cas_ual_ty.vrobot.network.ContinueRobotMessage;
import de.cas_ual_ty.vrobot.network.DropRobotMessage;
import de.cas_ual_ty.vrobot.network.OpenRobotInventoryMessage;
import de.cas_ual_ty.vrobot.network.StartRobotMessage;
import de.cas_ual_ty.vrobot.network.StopRobotMessage;
import de.cas_ual_ty.vrobot.network.SynchRobotToClientMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.NewRegistry;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(VRobot.MOD_ID)
public class VRobot
{
    /*
     * List<ItemStack> getDrops
     * ForgeHooks::canToolHarvestBlock
     * Block::getDrops
     */
    
    public static final String MOD_ID = "vrobot";
    public static final String PROTOCOL_VERSION = "1";
    
    public static IProxy proxy;
    public static SimpleChannel channel;
    
    public static IForgeRegistry<RobotActionType> ACTION_TYPES_REGISTRY;
    
    public VRobot()
    {
        VRobot.proxy = DistExecutor.runForDist(() -> de.cas_ual_ty.vrobot.client.ClientProxy::new, () -> (() -> new IProxy()
        {
        }));
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::newRegistry);
        
        bus = MinecraftForge.EVENT_BUS;
        bus.addListener(this::playerInteract);
    }
    
    private void commonSetup(FMLCommonSetupEvent event)
    {
        VRobot.channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(VRobot.MOD_ID, "main"),
            () -> VRobot.PROTOCOL_VERSION,
            VRobot.PROTOCOL_VERSION::equals,
            VRobot.PROTOCOL_VERSION::equals);
        VRobot.channel.registerMessage(0, SynchRobotToClientMessage.class, SynchRobotToClientMessage::encode, SynchRobotToClientMessage::decode, SynchRobotToClientMessage::handle);
        VRobot.channel.registerMessage(1, StartRobotMessage.class, StartRobotMessage::encode, StartRobotMessage::decode, StartRobotMessage::handle);
        VRobot.channel.registerMessage(2, ContinueRobotMessage.class, ContinueRobotMessage::encode, ContinueRobotMessage::decode, ContinueRobotMessage::handle);
        VRobot.channel.registerMessage(3, StopRobotMessage.class, StopRobotMessage::encode, StopRobotMessage::decode, StopRobotMessage::handle);
        VRobot.channel.registerMessage(4, DropRobotMessage.class, DropRobotMessage::encode, DropRobotMessage::decode, DropRobotMessage::handle);
        VRobot.channel.registerMessage(5, OpenRobotInventoryMessage.class, OpenRobotInventoryMessage::encode, OpenRobotInventoryMessage::decode, OpenRobotInventoryMessage::handle);
        
        VRobot.proxy.registerRenderer();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void newRegistry(NewRegistry event)
    {
        VRobot.ACTION_TYPES_REGISTRY = new RegistryBuilder().setName(new ResourceLocation(VRobot.MOD_ID, "robot_action_types")).setType(RobotActionType.class).setMaxID(127).create();
    }
    
    private void playerInteract(EntityInteractSpecific event)
    {
        if(event.getPlayer() != null && event.getTarget() instanceof RobotEntity)
        {
            RobotEntity robot = (RobotEntity)event.getTarget();
            robot.playerInteract(event.getPlayer());
        }
    }
}
