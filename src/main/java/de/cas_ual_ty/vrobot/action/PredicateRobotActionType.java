package de.cas_ual_ty.vrobot.action;

import java.util.function.BiFunction;

import de.cas_ual_ty.vrobot.RobotEntity;

public class PredicateRobotActionType extends RobotActionType
{
    public final BiFunction<RobotEntity, RobotAction, Boolean> isDone;
    
    public PredicateRobotActionType(BiFunction<RobotEntity, RobotAction, Boolean> isDone)
    {
        this.isDone = isDone;
    }
    
    @Override
    public boolean isDone(RobotEntity robot, RobotAction instance)
    {
        return this.isDone.apply(robot, instance);
    }
}
