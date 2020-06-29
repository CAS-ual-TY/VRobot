package de.cas_ual_ty.vrobot;

import de.cas_ual_ty.vrobot.registries.VRobotContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RobotInventoryContainer extends Container
{
    public final RobotEntity robot;
    public final IItemHandler itemHandler;
    
    public RobotInventoryContainer(int id, PlayerInventory inv, RobotEntity robot)
    {
        super(VRobotContainerTypes.ROBOT_INVENTORY, id);
        this.robot = robot;
        this.itemHandler = this.robot.getItemHandler();
        
        for(int j = 0; j < 3; ++j)
        {
            for(int k = 0; k < 9; ++k)
            {
                this.addSlot(new SlotItemHandler(this.itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        
        for(int l = 0; l < 3; ++l)
        {
            for(int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlot(new Slot(inv, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 - 18));
            }
        }
        
        for(int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlot(new Slot(inv, i1, 8 + i1 * 18, 161 - 18));
        }
    }
    
    public RobotInventoryContainer(int id, PlayerInventory inv, PacketBuffer buf)
    {
        this(id, inv, (RobotEntity)inv.player.world.getEntityByID(buf.readInt()));
    }
    
    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.robot == null || (this.robot.isAlive() && !this.robot.isWorking());
    }
    
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < this.itemHandler.getSlots())
            {
                if(!this.mergeItemStack(itemstack1, this.itemHandler.getSlots(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, this.itemHandler.getSlots(), false))
            {
                return ItemStack.EMPTY;
            }
            
            if(itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        
        return itemstack;
    }
}
