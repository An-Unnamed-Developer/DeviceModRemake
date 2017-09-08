package com.mrcrayfish.device.api.app.component;

import java.util.Arrays;

import com.mrcrayfish.device.api.app.Component;
import com.mrcrayfish.device.api.app.listener.ClickListener;
import com.mrcrayfish.device.api.utils.RenderUtil;
import com.mrcrayfish.device.core.Laptop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class Button extends Component
{
	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
	
	protected String text, toolTip, toolTipTitle;
	protected boolean hovered;
	protected int width, height;
	
	protected boolean hasIcon = false;
	protected ResourceLocation icon = null;
	protected int iconU, iconV;
	protected int iconWidth, iconHeight;
	
	protected ClickListener clickListener = null;
	
	/**
	 * Default button constructor
	 * 
	 * @param text text to be displayed in the button
	 * @param left how many pixels from the left
	 * @param top how many pixels from the top
	 * @param width width of the button
	 * @param height height of the button
	 */
	public Button(String text, int left, int top, int width, int height) 
	{
		super(left, top);
		
		if(width < 5 || height < 5)
			throw new IllegalArgumentException("Width and height must be more than or equal to 5");
		
		this.text = text;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Creates a button with an image inside. The size of the button is based
	 * on the size of the image with 3 pixels of padding.
	 * 
	 * @param left how many pixels from the left
	 * @param top how many pixels from the top
	 * @param icon the icon resource location
	 * @param iconU the u position on the resource
	 * @param iconV the v position on the resource
	 * @param iconWidth width of the icon
	 * @param iconHeight height of the icon
	 */
	public Button(int left, int top, ResourceLocation icon, int iconU, int iconV, int iconWidth, int iconHeight)
	{
		this("", left, top, iconWidth + 6, iconHeight + 6);
		this.hasIcon = true;
		this.icon = icon;
		this.iconU = iconU;
		this.iconV = iconV;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
	}
	
	/**
	 * Creates a button with an image inside. The size of the button is based
	 * on the size of the image with 3 pixels of padding.
	 * 
	 * @param left how many pixels from the left
	 * @param top how many pixels from the top
	 * @param width width of the button
	 * @param height height of the button
	 * @param icon the icon resource location
	 * @param iconU the u position on the resource
	 * @param iconV the v position on the resource
	 * @param iconWidth width of the icon
	 * @param iconHeight height of the icon
	 */
	public Button(int left, int top, int width, int height, ResourceLocation icon, int iconU, int iconV, int iconWidth, int iconHeight)
	{
		this("", left, top, Math.max(width, iconWidth + 6), Math.max(height, iconHeight + 6));
		this.hasIcon = true;
		this.icon = icon;
		this.iconU = iconU;
		this.iconV = iconV;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) 
	{
		if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(Component.COMPONENTS_GUI);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = isInside(mouseX, mouseY) && windowActive;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            
            /* Corners */
            RenderUtil.drawRectWithTexture(xPosition, yPosition, 96 + i * 5, 12, 2, 2, 2, 2);
            RenderUtil.drawRectWithTexture(xPosition + width - 2, yPosition, 99 + i * 5, 12, 2, 2, 2, 2);
            RenderUtil.drawRectWithTexture(xPosition + width - 2, yPosition + height - 2, 99 + i * 5, 15, 2, 2, 2, 2);
            RenderUtil.drawRectWithTexture(xPosition, yPosition + height - 2, 96 + i * 5, 15, 2, 2, 2, 2);

            /* Middles */
            RenderUtil.drawRectWithTexture(xPosition + 2, yPosition, 98 + i * 5, 12, width - 4, 2, 1, 2);
            RenderUtil.drawRectWithTexture(xPosition + width - 2, yPosition + 2, 99 + i * 5, 14, 2, height - 4, 2, 1);
            RenderUtil.drawRectWithTexture(xPosition + 2, yPosition + height - 2, 98 + i * 5, 15, width - 4, 2, 1, 2);
            RenderUtil.drawRectWithTexture(xPosition, yPosition + 2, 96 + i * 5, 14, 2, height - 4, 2, 1);
            
            /* Center */
            RenderUtil.drawRectWithTexture(xPosition + 2, yPosition + 2, 98 + i * 5, 14, width - 4, height - 4, 1, 1);
            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            
            int j = 14737632;

            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }
            
            if(hasIcon)
            {
            	mc.getTextureManager().bindTexture(icon);
            	this.drawTexturedModalRect(xPosition + 3 + (width - iconWidth - 6) / 2, yPosition + 3 + (height - iconHeight - 6) / 2, iconU, iconV, iconWidth, iconHeight);
            }
            else
            {
            	this.drawCenteredString(fontrenderer, this.text, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
            }
        }
	}
	
	@Override
	public void renderOverlay(Laptop laptop, Minecraft mc, int mouseX, int mouseY, boolean windowActive) 
	{
        if(this.hovered && this.toolTip != null)
        {
        	laptop.drawHoveringText(Arrays.asList(new String[] { TextFormatting.GOLD + this.toolTipTitle, this.toolTip }), mouseX, mouseY);
        }
	}

	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) 
	{
		if(!this.visible || !this.enabled)
			return;
		
		if(this.hovered) 
		{
			if(clickListener != null)
			{
				clickListener.onClick(this, mouseButton);
			}
			playClickSound(Minecraft.getMinecraft().getSoundHandler());
		}
	}
	
	/**
	 * Sets the click listener. Use this to handle custom actions
	 * when you press the button.
	 * 
	 * @param clickListener
	 */
	public final void setClickListener(ClickListener clickListener) 
	{
		this.clickListener = clickListener;
	}
	
	protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }
	
	public void playClickSound(SoundHandler handler) 
	{
		handler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	protected boolean isInside(int mouseX, int mouseY)
	{
		return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	}
	
	/**
	 * Sets the text to display in the button
	 * 
	 * @param text the text
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	
	/**
	 * Gets the text currently displayed in the button
	 * 
	 * @return the button text
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Displays a message when hovering the button.
	 * 
	 * @param toolTipTitle title of the tool tip
	 * @param toolTip description of the tool tip
	 */
	public void setToolTip(String toolTipTitle, String toolTip)
	{
		this.toolTipTitle = toolTipTitle;
		this.toolTip = toolTip;
	}
	
	//TODO add button text colour and button colour
}
