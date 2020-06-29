package de.cas_ual_ty.vrobot.action;

import java.util.function.BiConsumer;

import de.cas_ual_ty.vrobot.RobotEntity;

public class SingleRobotActionType extends RobotActionType
{
    public final BiConsumer<RobotEntity, RobotAction> consumer;
    
    public SingleRobotActionType(BiConsumer<RobotEntity, RobotAction> consumer)
    {
        this.consumer = consumer;
    }
    
    @Override
    public boolean isDone(RobotEntity robot, RobotAction instance)
    {
        this.consumer.accept(robot, instance);
        return true;
    }
}
