package de.cas_ual_ty.vrobot.node;

import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.visibilis.node.field.Input;
import de.cas_ual_ty.visibilis.print.provider.DataProvider;
import de.cas_ual_ty.visibilis.registries.VDataTypes;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.action.RobotAction;
import de.cas_ual_ty.vrobot.action.RobotActionType;
import de.cas_ual_ty.vrobot.action.SetSlotRobotAction;

public class SetSlotNode extends RobotActionNode
{
    public final Input<Integer> in2;
    
    protected int slot;
    
    public SetSlotNode(NodeType<?> type, RobotActionType action)
    {
        super(type, action);
        this.addInput(this.in2 = new Input<>(this, VDataTypes.INTEGER, "in2").setValue(0));
    }
    
    @Override
    public boolean doCalculate(DataProvider context)
    {
        this.slot = this.in2.getValue();
        return super.doCalculate(context);
    }
    
    @Override
    public RobotAction createAction(RobotEntity robot)
    {
        return new SetSlotRobotAction(this.action, this.slot);
    }
}
