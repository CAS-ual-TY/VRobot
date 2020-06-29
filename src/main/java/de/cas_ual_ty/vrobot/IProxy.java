package de.cas_ual_ty.vrobot;

import de.cas_ual_ty.visibilis.print.provider.PrintProvider;

public interface IProxy
{
    public default void registerRenderer()
    {
    }
    
    public default void openRobotGui(PrintProvider helper, RobotEntity entity)
    {
    }
}
