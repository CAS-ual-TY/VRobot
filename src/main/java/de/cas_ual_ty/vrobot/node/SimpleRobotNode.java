package de.cas_ual_ty.vrobot.node;

import java.util.function.Consumer;
import java.util.function.Function;

import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.visibilis.node.field.Input;
import de.cas_ual_ty.visibilis.node.field.Output;
import de.cas_ual_ty.visibilis.registries.VDataTypes;
import de.cas_ual_ty.vrobot.RobotEntity;

public class SimpleRobotNode extends RobotNode
{
    public final Output<Object> out1;
    public final Input<Object> in1;
    
    public final Function<RobotEntity, Boolean> function;
    
    public SimpleRobotNode(NodeType<?> type, Function<RobotEntity, Boolean> function)
    {
        super(type);
        this.function = function;
        this.addOutput(this.out1 = new Output<>(this, VDataTypes.EXEC, "out1"));
        this.addInput(this.in1 = new Input<>(this, VDataTypes.EXEC, "in1"));
    }
    
    public SimpleRobotNode(NodeType<?> type, Consumer<RobotEntity> function, boolean ret)
    {
        this(type, (robot) ->
        {
            function.accept(robot);
            return ret;
        });
    }
    
    @Override
    public boolean doCalculate(RobotEntity robot)
    {
        return this.function.apply(robot);
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
        return SimpleRobotInteractionNode.COLOR;
    }
    
    @Override
    public float[] getTextColor()
    {
        return SimpleRobotInteractionNode.TEXT_COLOR;
    }
}
