package de.cas_ual_ty.vrobot.action;

import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.VRobot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class RobotAction
{
    public static final String KEY_ACTION = "action";
    public static final String KEY_TIME = "time";
    public static final String KEY_DEAD = "isDead";
    
    public final RobotActionType actionType;
    protected int time;
    protected boolean isDead;
    
    public RobotAction(RobotActionType action)
    {
        this.actionType = action;
        this.time = 0;
        this.isDead = false;
    }
    
    public RobotAction(RobotActionType action, CompoundNBT nbt)
    {
        this.actionType = action;
        this.time = nbt.getInt(RobotAction.KEY_TIME);
        this.isDead = nbt.getBoolean(RobotAction.KEY_DEAD);
    }
    
    public int getTime()
    {
        return this.time;
    }
    
    public void setDead(RobotEntity robot)
    {
        this.actionType.onSetDead(robot, this);
        this.isDead = true;
    }
    
    public boolean isDead()
    {
        return this.isDead;
    }
    
    public boolean hasBegun()
    {
        return this.time > 0;
    }
    
    public void tick(RobotEntity robot)
    {
        if(this.time == 0)
        {
            this.actionType.onStart(robot, this);
        }
        
        this.actionType.tick(robot, this);
        
        if(this.actionType.isDone(robot, this))
        {
            this.setDead(robot);
        }
        
        ++this.time;
    }
    
    public CompoundNBT toNBT()
    {
        return RobotAction.toNBT(this);
    }
    
    public static CompoundNBT toNBT(RobotAction action)
    {
        CompoundNBT nbt = new CompoundNBT();
        
        nbt.putString(RobotAction.KEY_ACTION, action.actionType.getRegistryName().toString());
        nbt.putInt(RobotAction.KEY_TIME, action.time);
        nbt.putBoolean(RobotAction.KEY_DEAD, action.isDead);
        
        return nbt;
    }
    
    public static RobotAction fromNBT(CompoundNBT nbt)
    {
        RobotActionType actionType = VRobot.ACTION_TYPES_REGISTRY.getValue(new ResourceLocation(nbt.getString(RobotAction.KEY_ACTION)));
        return actionType.deserialize(nbt);
    }
}
