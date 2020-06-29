package de.cas_ual_ty.vrobot.node;

import de.cas_ual_ty.visibilis.node.Node;
import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.visibilis.print.provider.DataProvider;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.provider.RobotDataKey;

public abstract class RobotNode extends Node
{
    public RobotNode(NodeType<?> type)
    {
        super(type);
    }
    
    @Override
    public boolean doCalculate(DataProvider context)
    {
        RobotEntity robot = context.getData(RobotDataKey.KEY_ROBOT);
        
        if(robot != null)
        {
            return this.doCalculate(robot);
        }
        
        return false;
    }
    
    public abstract boolean doCalculate(RobotEntity robot);
}
