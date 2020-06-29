package de.cas_ual_ty.vrobot.registries;

import de.cas_ual_ty.visibilis.node.NodeEvent;
import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.vrobot.VRobot;
import de.cas_ual_ty.vrobot.node.ExportItemsNode;
import de.cas_ual_ty.vrobot.node.ItemHandlerBlockNode;
import de.cas_ual_ty.vrobot.node.RobotActionNode;
import de.cas_ual_ty.vrobot.node.SimpleRobotNode;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = VRobot.MOD_ID, bus = Bus.MOD)
@ObjectHolder(VRobot.MOD_ID)
public class VRobotNodeTypes
{
    public static final NodeType<?> INITIALIZE = null;
    public static final NodeType<?> TICK = null;
    
    public static final NodeType<?> START = null;
    public static final NodeType<?> CONTINUE = null;
    
    public static final NodeType<?> PAUSE = null;
    public static final NodeType<?> END = null;
    public static final NodeType<?> FORWARD = null;
    public static final NodeType<?> UP = null;
    public static final NodeType<?> DOWN = null;
    public static final NodeType<?> TURN_LEFT = null;
    public static final NodeType<?> TURN_RIGHT = null;
    public static final NodeType<?> PAUSE_1_SEC = null;
    public static final NodeType<?> NEXT_SLOT = null;
    public static final NodeType<?> PREV_SLOT = null;
    public static final NodeType<?> RESET_SLOT = null;
    public static final NodeType<?> HARVEST = null;
    public static final NodeType<?> HARVEST_BELOW = null;
    public static final NodeType<?> HARVEST_ABOVE = null;
    
    public static final NodeType<?> EXPORT_ITEMS = null;
    public static final NodeType<?> GET_ITEM_HANDLER_BLOCK = null;
    
    public static final NodeType<?> DEBUG = null; //TODO
    
    @SubscribeEvent
    public static void register(Register<NodeType<?>> event)
    {
        IForgeRegistry<NodeType<?>> registry = event.getRegistry();
        
        registry.register(new NodeType<>((type) -> new NodeEvent(type, type.getRegistryName().getNamespace(), type.getRegistryName().getPath())).setRegistryName(VRobot.MOD_ID, "initialize"));
        registry.register(new NodeType<>((type) -> new NodeEvent(type, type.getRegistryName().getNamespace(), type.getRegistryName().getPath())).setRegistryName(VRobot.MOD_ID, "tick"));
        
        registry.register(new NodeType<>((type) -> new SimpleRobotNode(type, (r) -> r.buttonStart(), true)).setRegistryName(VRobot.MOD_ID, "start"));
        registry.register(new NodeType<>((type) -> new SimpleRobotNode(type, (r) -> r.continuePaused(), true)).setRegistryName(VRobot.MOD_ID, "continue"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.PAUSE)).setRegistryName(VRobot.MOD_ID, "pause"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.END)).setRegistryName(VRobot.MOD_ID, "end"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.FORWARD)).setRegistryName(VRobot.MOD_ID, "forward"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.UP)).setRegistryName(VRobot.MOD_ID, "up"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.DOWN)).setRegistryName(VRobot.MOD_ID, "down"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.TURN_LEFT)).setRegistryName(VRobot.MOD_ID, "turn_left"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.TURN_RIGHT)).setRegistryName(VRobot.MOD_ID, "turn_right"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.PAUSE_1_SEC)).setRegistryName(VRobot.MOD_ID, "pause_1_sec"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.NEXT_SLOT)).setRegistryName(VRobot.MOD_ID, "next_slot"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.PREV_SLOT)).setRegistryName(VRobot.MOD_ID, "prev_slot"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.RESET_SLOT)).setRegistryName(VRobot.MOD_ID, "reset_slot"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.HARVEST)).setRegistryName(VRobot.MOD_ID, "harvest"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.HARVEST_BELOW)).setRegistryName(VRobot.MOD_ID, "harvest_below"));
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.HARVEST_ABOVE)).setRegistryName(VRobot.MOD_ID, "harvest_above"));
        
        registry.register(new NodeType<>((type) -> new ExportItemsNode(type)).setRegistryName(VRobot.MOD_ID, "export_items"));
        registry.register(new NodeType<>((type) -> new ItemHandlerBlockNode(type)).setRegistryName(VRobot.MOD_ID, "get_item_handler_block"));
        
        registry.register(new NodeType<>((type) -> new RobotActionNode(type, VRobotActionTypes.DEBUG)).setRegistryName(VRobot.MOD_ID, "debug"));
    }
}
