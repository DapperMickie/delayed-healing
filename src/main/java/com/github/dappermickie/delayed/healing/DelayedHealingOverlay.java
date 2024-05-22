package com.github.dappermickie.delayed.healing;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.Point;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.FontType;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class DelayedHealingOverlay extends Overlay
{
	private final Client client;
	private final DelayedHealingConfig config;
	private DelayedHeals activeHeal;
	private int ticksLeft;

	@Inject
	public DelayedHealingOverlay(Client client, DelayedHealingConfig config)
	{
		this.client = client;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(Overlay.PRIORITY_HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final Widget inventoryWidget = client.getWidget(ComponentID.INVENTORY_CONTAINER);
		if (config.overlay() && activeHeal != null && !inventoryWidget.isHidden())
		{
			Item[] items = client.getItemContainer(InventoryID.INVENTORY).getItems();
			for (int i = 0; i < items.length; i++)
			{
				Item item = items[i];
				if (DelayedHeals.getDelayedHealByItemId(item.getId()) != null)
				{
					drawTicksOverlay(graphics, inventoryWidget, ticksLeft, i);
				}
			}
		}
		return null;
	}

	public void setActiveHeal(DelayedHeals heal)
	{
		this.activeHeal = heal;
		this.ticksLeft = heal.getTickDelay();
	}

	public void clearActiveHeal()
	{
		this.activeHeal = null;
	}

	public void tickTimer()
	{
		if (activeHeal != null)
		{
			ticksLeft--;
			if (ticksLeft <= 0)
			{
				clearActiveHeal();
			}
		}
	}

	private void drawTicksOverlay(Graphics2D graphics, Widget inventoryWidget, int ticksLeft, int index)
	{
		Point location = inventoryWidget
			.getChild(index)
			.getCanvasLocation();
		String text = String.valueOf(ticksLeft);
		graphics.setColor(config.overlayColor());
		graphics.setFont(config.overlayFont().getFont());
		graphics.drawString(text, location.getX() + config.overlayXOffset(), location.getY() + config.overlayYOffset());
	}
}
