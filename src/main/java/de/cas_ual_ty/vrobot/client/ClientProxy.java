package de.cas_ual_ty.vrobot.client;

import de.cas_ual_ty.visibilis.print.provider.PrintProvider;
import de.cas_ual_ty.vrobot.IProxy;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.registries.VRobotContainerTypes;
import de.cas_ual_ty.vrobot.registries.VRobotEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
    @Override
    public void registerRenderer()
    {
        RenderingRegistry.registerEntityRenderingHandler(VRobotEntityTypes.ROBOT, RobotRenderer::new);
        ScreenManager.registerFactory(VRobotContainerTypes.ROBOT_INVENTORY, RobotInventoryContainerGui::new);
    }
    
    @Override
    public void openRobotGui(PrintProvider helper, RobotEntity entity)
    {
        Minecraft.getInstance().displayGuiScreen(new RobotGui(new StringTextComponent("Print"), entity, helper));
    }
}
