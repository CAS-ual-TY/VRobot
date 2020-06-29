package de.cas_ual_ty.vrobot.action;

import net.minecraft.nbt.CompoundNBT;

public class SetSlotRobotAction extends RobotAction
{
    public static final String KEY_SLOT = "slot";
    
    public final int slot;
    
    public SetSlotRobotAction(RobotActionType action, int slot)
    {
        super(action);
        this.slot = slot;
    }
    
    public SetSlotRobotAction(RobotActionType action, CompoundNBT nbt)
    {
        super(action, nbt);
        this.slot = nbt.getInt(SetSlotRobotAction.KEY_SLOT);
    }
    
    @Override
    public CompoundNBT toNBT()
    {
        CompoundNBT nbt = super.toNBT();
        nbt.putInt(SetSlotRobotAction.KEY_SLOT, this.slot);
        return nbt;
    }
}
