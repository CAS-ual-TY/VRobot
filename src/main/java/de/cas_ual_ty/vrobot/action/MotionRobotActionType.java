package de.cas_ual_ty.vrobot.action;

import java.util.function.Function;

import de.cas_ual_ty.vrobot.RobotEntity;
import net.minecraft.util.math.Vec3d;

public class MotionRobotActionType extends TimedRobotActionType
{
    public final Function<RobotEntity, Vec3d> motion;
    
    public MotionRobotActionType(int maxTime, Function<RobotEntity, Vec3d> motion)
    {
        super(maxTime);
        this.motion = motion;
    }
    
    @Override
    public void onStart(RobotEntity robot, RobotAction instance)
    {
        Vec3d motion = this.motion.apply(robot);
        robot.addVelocity(motion.x, motion.y, motion.z);
    }
    
    @Override
    public void onSetDead(RobotEntity robot, RobotAction instance)
    {
        robot.setMotion(0, 0, 0);
    }
}
