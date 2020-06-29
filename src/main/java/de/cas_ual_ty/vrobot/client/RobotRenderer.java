package de.cas_ual_ty.vrobot.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.VRobot;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

@SuppressWarnings("deprecation")
public class RobotRenderer extends EntityRenderer<RobotEntity>
{
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(VRobot.MOD_ID, "textures/entity/robot.png");
    
    public RobotRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
    }
    
    @Override
    public void render(RobotEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        matrixStackIn.push();
        Vec3d motion = entityIn.getMotion().scale(partialTicks / 20F);
        matrixStackIn.translate(motion.x, motion.y, motion.z);
        
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.YN, entityYaw + 180F, true));
        matrixStackIn.translate(+0.5D, 0, 0);
        Minecraft.getInstance().getItemRenderer().renderItem(entityIn.getItemHandler().getStackInSlot(entityIn.getActiveSlotFromDataManager()), TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.YN, entityYaw, true));
        matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(Blocks.CHEST.getDefaultState(), matrixStackIn, bufferIn, packedLightIn, 1);
        matrixStackIn.pop();
        
        matrixStackIn.pop();
        
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
    
    @Override
    public ResourceLocation getEntityTexture(RobotEntity entity)
    {
        return RobotRenderer.TEXTURE_LOCATION;
    }
}
