package de.cas_ual_ty.vrobot.node;

import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.visibilis.node.field.Input;
import de.cas_ual_ty.visibilis.node.field.Output;
import de.cas_ual_ty.visibilis.registries.VDataTypes;
import de.cas_ual_ty.visibilis.util.VUtility;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.action.RobotAction;
import de.cas_ual_ty.vrobot.action.RobotActionType;

public class RobotActionNode extends RobotNode
{
    public static final float[] COLOR = { 0.2F, 0.8F, 0.8F };
    public static final float[] TEXT_COLOR = VUtility.COLOR_DEFAULT_BLACK;
    
    public final Output<Object> out1;
    public final Input<Object> in1;
    
    protected RobotActionType action;
    
    public RobotActionNode(NodeType<?> type, RobotActionType action)
    {
        super(type);
        this.action = action;
        this.addOutput(this.out1 = new Output<>(this, VDataTypes.EXEC, "out1"));
        this.addInput(this.in1 = new Input<>(this, VDataTypes.EXEC, "in1"));
    }
    
    @Override
    public boolean doCalculate(RobotEntity robot)
    {
        robot.addAction(this.createAction(robot));
        return true;
    }
    
    public RobotAction createAction(RobotEntity robot)
    {
        return new RobotAction(this.action);
    }
    
    @Override
    public <O> O getOutputValue(Output<O> out)
    {
        return null;
    }
    
    @Override
    public Output<Object> getOutExec(int index)
    {
        return index == 0 ? this.out1 : null;
    }
    
    @Override
    public float[] getColor()
    {
        return RobotActionNode.COLOR;
    }
    
    @Override
    public float[] getTextColor()
    {
        return RobotActionNode.TEXT_COLOR;
    }
}
