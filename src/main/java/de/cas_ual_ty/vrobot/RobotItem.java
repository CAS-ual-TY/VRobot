package de.cas_ual_ty.vrobot;

import de.cas_ual_ty.visibilis.print.item.ItemPrint;
import de.cas_ual_ty.vrobot.registries.VRobotEntityTypes;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class RobotItem extends ItemPrint
{
    public RobotItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = Item.rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        if(raytraceresult.getType() != RayTraceResult.Type.BLOCK)
        {
            return ActionResult.resultPass(itemstack);
        }
        else if(worldIn.isRemote)
        {
            return ActionResult.resultSuccess(itemstack);
        }
        else
        {
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getPos();
            BlockPos blockpos2 = blockpos.add(blockraytraceresult.getFace().getDirectionVec());
            if(/*!*/(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock))
            {
                return ActionResult.resultPass(itemstack);
            }
            else if(worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack))
            {
                RobotEntity e = VRobotEntityTypes.ROBOT.create(worldIn);
                
                e.rotationYaw = playerIn.rotationYaw;
                e.fixPositionRotation();
                e.setPrint(this.getPrint(itemstack));
                
                double d = 0.5D;
                e.setLocationAndAngles(blockpos2.getX() + d, blockpos2.getY() + d, blockpos2.getZ() + d, e.rotationYaw, e.rotationPitch);
                
                if(!worldIn.addEntity(e))
                {
                    return ActionResult.resultPass(itemstack);
                }
                else
                {
                    if(!playerIn.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }
                    
                    playerIn.addStat(Stats.ITEM_USED.get(this));
                    return ActionResult.resultSuccess(itemstack);
                }
            }
            else
            {
                return ActionResult.resultFail(itemstack);
            }
        }
    }
}
