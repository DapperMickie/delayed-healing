package com.github.dappermickie.delayed.healing;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("DelayedHealing")
public interface DelayedHealingConfig extends Config
{
	@ConfigItem(
		keyName = "cookedGraahk",
		name = "Graahk",
		description = "Toggles the delayed timer infobox for cooked Graahks."
	)
	default boolean cookedGraahk()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedKyatt",
		name = "Kyatt",
		description = "Toggles the delayed timer infobox for cooked Kyatts."
	)
	default boolean cookedKyatt()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedPyreFox",
		name = "Pyre Fox",
		description = "Toggles the delayed timer infobox for cooked Pyre Foxes."
	)
	default boolean cookedPyreFox()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedDashingKebbit",
		name = "Dashing Kebbit",
		description = "Toggles the delayed timer infobox for cooked Dashing Kebbits."
	)
	default boolean cookedDashingKebbit()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedSunlightAntelope",
		name = "Sunlight Antelope",
		description = "Toggles the delayed timer infobox for cooked Sunlight Antelopes."
	)
	default boolean cookedSunlightAntelope()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedMoonlightAntelope",
		name = "Moonlight Antelope",
		description = "Toggles the delayed timer infobox for cooked Moonlight Antelopes."
	)
	default boolean cookedMoonlightAntelope()
	{
		return true;
	}
}
