package de.cas_ual_ty.vrobot.provider;

import java.util.ArrayList;

import de.cas_ual_ty.visibilis.node.Node;
import de.cas_ual_ty.visibilis.print.provider.NodeListProviderBase;
import de.cas_ual_ty.vrobot.registries.VRobotNodeTypes;

public class RobotNodeListProvider extends NodeListProviderBase
{
    public RobotNodeListProvider()
    {
        super();
        this.list.clear();
        RobotNodeListProvider.robot(this.list);
        NodeListProviderBase.exec(this.list);
        NodeListProviderBase.base(this.list);
    }
    
    public static void robot(ArrayList<Node> list)
    {
        list.add(VRobotNodeTypes.INITIALIZE.instantiate());
        list.add(VRobotNodeTypes.START.instantiate());
        list.add(VRobotNodeTypes.CONTINUE.instantiate());
        list.add(VRobotNodeTypes.TICK.instantiate());
        list.add(VRobotNodeTypes.PAUSE.instantiate());
        list.add(VRobotNodeTypes.END.instantiate());
        list.add(VRobotNodeTypes.FORWARD.instantiate());
        list.add(VRobotNodeTypes.UP.instantiate());
        list.add(VRobotNodeTypes.DOWN.instantiate());
        list.add(VRobotNodeTypes.TURN_LEFT.instantiate());
        list.add(VRobotNodeTypes.TURN_RIGHT.instantiate());
        list.add(VRobotNodeTypes.PAUSE_1_SEC.instantiate());
        list.add(VRobotNodeTypes.NEXT_SLOT.instantiate());
        list.add(VRobotNodeTypes.PREV_SLOT.instantiate());
        list.add(VRobotNodeTypes.RESET_SLOT.instantiate());
        list.add(VRobotNodeTypes.HARVEST.instantiate());
        list.add(VRobotNodeTypes.EXPORT_ITEMS.instantiate());
        list.add(VRobotNodeTypes.GET_ITEM_HANDLER_BLOCK.instantiate());
        list.add(VRobotNodeTypes.DEBUG.instantiate()); //TODO
    }
}
