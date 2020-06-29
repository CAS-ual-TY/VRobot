package de.cas_ual_ty.vrobot.action;

import de.cas_ual_ty.vrobot.RobotEntity;

public class TimedRobotActionType extends RobotActionType
{
    public final int maxTime;
    
    public TimedRobotActionType(int maxTime)
    {
        this.maxTime = maxTime;
    }
    
    @Override
    public boolean isDone(RobotEntity robot, RobotAction instance)
    {
        return instance.getTime() >= this.maxTime;
    }
}
