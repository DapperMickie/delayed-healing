package com.github.dappermickie.delayed.healing;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DelayedHealingPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DelayedHealingPlugin.class);
		RuneLite.main(args);
	}
}