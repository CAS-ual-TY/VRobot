package de.cas_ual_ty.vrobot.provider;

import de.cas_ual_ty.visibilis.print.provider.DataKey;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.VRobot;

public class RobotDataKey<I> extends DataKey<I>
{
    public static final DataKey<RobotEntity> KEY_ROBOT = new RobotDataKey<>("robot");
    
    public RobotDataKey(String type)
    {
        super(VRobot.MOD_ID, type);
    }
}
