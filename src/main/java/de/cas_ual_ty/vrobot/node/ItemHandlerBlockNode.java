package de.cas_ual_ty.vrobot.node;

import de.cas_ual_ty.visibilis.node.NodeType;
import de.cas_ual_ty.visibilis.node.field.Input;
import de.cas_ual_ty.visibilis.node.field.Output;
import de.cas_ual_ty.visibilis.registries.VDataTypes;
import de.cas_ual_ty.visibilis.util.VUtility;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.registries.VRobotDataTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerBlockNode extends RobotNode
{
    public Output<Object> out1Exec;
    public Output<IItemHandler> out2ItemHandler;
    public Output<Object> out3Exec;
    public Input<Object> in1Exec;
    
    protected IItemHandler value;
    
    public ItemHandlerBlockNode(NodeType<?> type)
    {
        super(type);
        this.addOutput(this.out1Exec = new Output<>(this, VDataTypes.EXEC, "out1"));
        this.addOutput(this.out2ItemHandler = new Output<>(this, VRobotDataTypes.ITEM_HANDLER, "out2"));
        this.addOutput(this.out3Exec = new Output<>(this, VDataTypes.EXEC, "out3"));
        this.addInput(this.in1Exec = new Input<>(this, VDataTypes.EXEC, "in1"));
    }
    
    @Override
    public boolean doCalculate(RobotEntity robot)
    {
        this.value = null;
        
        BlockPos pos = robot.getPosition().down();
        TileEntity te = robot.world.getTileEntity(pos);
        
        if(te != null)
        {
            this.value = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
        }
        
        return true;
    }
    
    @Override
    public <O> O getOutputValue(Output<O> out)
    {
        return out == this.out2ItemHandler ? VUtility.cast(this.value) : null;
    }
    
    @Override
    public Output<Object> getOutExec(int index)
    {
        return index == 0 ? (this.value != null ? this.out1Exec : this.out3Exec) : null;
    }
    
    @Override
    public float[] getColor()
    {
        return SimpleRobotInteractionNode.COLOR;
    }
    
    @Override
    public float[] getTextColor()
    {
        return SimpleRobotInteractionNode.TEXT_COLOR;
    }
}
