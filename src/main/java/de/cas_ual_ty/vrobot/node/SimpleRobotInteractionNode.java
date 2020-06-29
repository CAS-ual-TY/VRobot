package de.cas_ual_ty.vrobot.node;

import java.util.function.BiFunction;

import de.cas_ual_ty.visibilis.datatype.DataType;
import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.visibilis.node.field.Input;
import de.cas_ual_ty.visibilis.node.field.Output;
import de.cas_ual_ty.visibilis.registries.VDataTypes;
import de.cas_ual_ty.visibilis.util.VUtility;
import de.cas_ual_ty.vrobot.RobotEntity;

public abstract class SimpleRobotInteractionNode<A> extends RobotNode
{
    public static final float[] COLOR = { 0.949F, 0.667F, 0.667F };
    public static final float[] TEXT_COLOR = VUtility.COLOR_DEFAULT_BLACK;
    
    public final Output<Object> out1;
    public final Input<Object> in1;
    public final Input<A> in2;
    
    public SimpleRobotInteractionNode(NodeType<?> type)
    {
        super(type);
        this.addOutput(this.out1 = new Output<>(this, VDataTypes.EXEC, "out1"));
        this.addInput(this.in1 = new Input<>(this, VDataTypes.EXEC, "in1"));
        this.addInput(this.in2 = new Input<>(this, this.getSecondDataType(), "in2"));
    }
    
    @Override
    public boolean doCalculate(RobotEntity robot)
    {
        A value = this.in2.getValue();
        return this.doCalculate(robot, value);
    }
    
    public abstract DataType<A> getSecondDataType();
    
    public abstract boolean doCalculate(RobotEntity robot, A value);
    
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
    
    public static <I> NodeType.IFactory<SimpleRobotInteractionNode<I>> createRobotInteractionNode(DataType<I> secondDataType, BiFunction<RobotEntity, I, Boolean> function)
    {
        return (type) ->
        {
            return new SimpleRobotInteractionNode<I>(type)
            {
                
                @Override
                public DataType<I> getSecondDataType()
                {
                    return secondDataType;
                }
                
                @Override
                public boolean doCalculate(RobotEntity robot, I value)
                {
                    return function.apply(robot, value);
                }
            };
        };
    }
}
