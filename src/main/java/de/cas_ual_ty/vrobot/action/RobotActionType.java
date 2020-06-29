package de.cas_ual_ty.vrobot.action;

import de.cas_ual_ty.vrobot.RobotEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class RobotActionType extends ForgeRegistryEntry<RobotActionType>
{
    public RobotActionType()
    {
    }
    
    public void onStart(RobotEntity robot, RobotAction instance)
    {
        
    }
    
    public void tick(RobotEntity robot, RobotAction instance)
    {
        
    }
    
    public void onSetDead(RobotEntity robot, RobotAction instance)
    {
        
    }
    
    public abstract boolean isDone(RobotEntity robot, RobotAction instance);
    
    public RobotAction deserialize(CompoundNBT nbt)
    {
        return new RobotAction(this, nbt);
    }
    
    public CompoundNBT serialize(RobotAction action)
    {
        return action.toNBT();
    }
}
