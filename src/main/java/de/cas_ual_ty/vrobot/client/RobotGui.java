package de.cas_ual_ty.vrobot.client;

import de.cas_ual_ty.visibilis.print.GuiPrint;
import de.cas_ual_ty.visibilis.print.provider.PrintProvider;
import de.cas_ual_ty.vrobot.RobotEntity;
import de.cas_ual_ty.vrobot.VRobot;
import de.cas_ual_ty.vrobot.network.ContinueRobotMessage;
import de.cas_ual_ty.vrobot.network.DropRobotMessage;
import de.cas_ual_ty.vrobot.network.OpenRobotInventoryMessage;
import de.cas_ual_ty.vrobot.network.StartRobotMessage;
import de.cas_ual_ty.vrobot.network.StopRobotMessage;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class RobotGui extends GuiPrint
{
    public final RobotEntity robot;
    protected Button start;
    protected Button cont;
    protected Button drop;
    protected Button stop;
    protected Button clear;
    protected Button inventory;
    
    public RobotGui(ITextComponent title, RobotEntity robot, PrintProvider provider)
    {
        super(title, provider);
        this.robot = robot;
    }
    
    @Override
    protected void init()
    {
        super.init();
        
        if(this.robot.isPaused())
        {
            this.addButton(this.cont = new Button(114, 0, 60, 20, "Continue", this::buttonPressed));
            this.addButton(this.stop = new Button(174, 0, 60, 20, "Stop", this::buttonPressed));
            this.start = null;
            this.clear = null;
        }
        else
        {
            this.addButton(this.start = new Button(114, 0, 60, 20, "Start", this::buttonPressed));
            this.addButton(this.clear = new Button(174, 0, 60, 20, "Clear", this::buttonPressed));
            //            this.clear.active = false; //TODO
            this.cont = null;
            this.stop = null;
        }
        
        this.addButton(this.drop = new Button(234, 0, 60, 20, "Drop", this::buttonPressed));
        this.addButton(this.inventory = new Button(294, 0, 60, 20, "Inventory", this::buttonPressed));
    }
    
    @Override
    public void tick()
    {
        if(this.robot.isWorking())
        {
            this.onClose();
        }
        
        super.tick();
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int modifiers)
    {
        boolean b = super.mouseClicked(mouseX, mouseY, modifiers);
        this.robot.setPrint(this.getProvider().getPrint());
        return b;
    }
    
    protected void buttonPressed(Button b)
    {
        if(b == this.start)
        {
            this.onClose();
            VRobot.channel.sendToServer(new StartRobotMessage(this.robot.getEntityId()));
        }
        else if(b == this.cont)
        {
            this.onClose();
            VRobot.channel.sendToServer(new ContinueRobotMessage(this.robot.getEntityId()));
        }
        else if(b == this.stop)
        {
            this.onClose();
            VRobot.channel.sendToServer(new StopRobotMessage(this.robot.getEntityId()));
        }
        else if(b == this.drop)
        {
            this.onClose();
            VRobot.channel.sendToServer(new DropRobotMessage(this.robot.getEntityId()));
        }
        else if(b == this.clear)
        {
            this.getProvider().saveChange();
            this.getProvider().getPrint().getNodes().clear();
        }
        else if(b == this.inventory)
        {
            VRobot.channel.sendToServer(new OpenRobotInventoryMessage(this.robot.getEntityId()));
        }
    }
}
