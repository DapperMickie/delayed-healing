package com.github.dappermickie.delayed.healing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

public class DelayedHealingInfoBox extends InfoBox
{
	private final int tickDelay;
	private int ticksPast;

	@Inject
	public DelayedHealingInfoBox(BufferedImage image, Plugin plugin, int tickDelay)
	{
		super(image, plugin);
		this.tickDelay = tickDelay;
		this.ticksPast = 0;
	}

	@Override
	public String getText()
	{
		return String.valueOf(ticksLeft());
	}

	@Override
	public Color getTextColor()
	{
		return Color.WHITE;
	}

	public void tickTimer()
	{
		ticksPast++;
	}

	public int ticksLeft()
	{
		return tickDelay - ticksPast;
	}
}