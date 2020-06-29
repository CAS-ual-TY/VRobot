package de.cas_ual_ty.vrobot.node;

import de.cas_ual_ty.visibilis.datatype.DataType;
import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.registries.VRobotDataTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ExportItemsNode extends SimpleRobotInteractionNode<IItemHandler>
{
    public ExportItemsNode(NodeType<?> type)
    {
        super(type);
    }
    
    @Override
    public boolean doCalculate(RobotEntity robot, IItemHandler value)
    {
        ItemStack itemStack = robot.getItemHandler().getStackInSlot(robot.getActiveSlot());
        
        for(int i = 0; i < value.getSlots() && !itemStack.isEmpty(); ++i)
        {
            itemStack = value.insertItem(i, itemStack, false);
        }
        
        return true;
    }
    
    @Override
    public DataType<IItemHandler> getSecondDataType()
    {
        return VRobotDataTypes.ITEM_HANDLER;
    }
}
