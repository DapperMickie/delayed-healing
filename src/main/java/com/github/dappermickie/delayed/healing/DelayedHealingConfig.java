package com.github.dappermickie.delayed.healing;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.FontType;
import net.runelite.client.config.Range;

@ConfigGroup("DelayedHealing")
public interface DelayedHealingConfig extends Config
{
	@ConfigItem(
		keyName = "overlay",
		name = "Overlay",
		description = "Configures whether or not the overlay is enabled",
		position = 1
	)
	default boolean overlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "overlayColor",
		name = "Overlay Color",
		description = "Configures the color of the text in the overlay",
		position = 2
	)
	default Color overlayColor()
	{
		return Color.BLUE.darker();
	}

	@ConfigItem(
		keyName = "overlayXOffset",
		name = "Overlay X Offset",
		description = "Configures the x offset of the text in the overlay",
		position = 3
	)
	@Range(max = 20)
	default int overlayXOffset()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "overlayYOffset",
		name = "Overlay Y Offset",
		description = "Configures the Y offset of the text in the overlay",
		position = 4
	)
	@Range(max = 20)
	default int overlayYOffset()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "overlayFont",
		name = "Overlay Font",
		description = "Configures the font of the text in the overlay",
		position = 5
	)
	default FontType overlayFont()
	{
		return FontType.BOLD;
	}

	@ConfigItem(
		keyName = "infobox",
		name = "Infobox",
		description = "Configures whether or not the infobox is enabled",
		position = 6
	)
	default boolean infobox()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedWildKebbit",
		name = "Wild Kebbit",
		description = "Toggles the delayed timer infobox for cooked Wild Kebbits.",
		position = 7
	)
	default boolean wildKebbit()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedLarupia",
		name = "Larupia",
		description = "Toggles the delayed timer infobox for cooked Larupia.",
		position = 7
	)
	default boolean larupia()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedBarbTailedKebbit",
		name = "Barb-Tailed Kebbit",
		description = "Toggles the delayed timer infobox for cooked Barb-Tailed Kebbit.",
		position = 7
	)
	default boolean barbTailedKebbit()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedGraahk",
		name = "Graahk",
		description = "Toggles the delayed timer infobox for cooked Graahks.",
		position = 7
	)
	default boolean cookedGraahk()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedKyatt",
		name = "Kyatt",
		description = "Toggles the delayed timer infobox for cooked Kyatts.",
		position = 7
	)
	default boolean cookedKyatt()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedPyreFox",
		name = "Pyre Fox",
		description = "Toggles the delayed timer infobox for cooked Pyre Foxes.",
		position = 7
	)
	default boolean cookedPyreFox()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedDashingKebbit",
		name = "Dashing Kebbit",
		description = "Toggles the delayed timer infobox for cooked Dashing Kebbits.",
		position = 7
	)
	default boolean cookedDashingKebbit()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedSunlightAntelope",
		name = "Sunlight Antelope",
		description = "Toggles the delayed timer infobox for cooked Sunlight Antelopes.",
		position = 7
	)
	default boolean cookedSunlightAntelope()
	{
		return true;
	}

	@ConfigItem(
		keyName = "cookedMoonlightAntelope",
		name = "Moonlight Antelope",
		description = "Toggles the delayed timer infobox for cooked Moonlight Antelopes.",
		position = 7
	)
	default boolean cookedMoonlightAntelope()
	{
		return true;
	}
}
