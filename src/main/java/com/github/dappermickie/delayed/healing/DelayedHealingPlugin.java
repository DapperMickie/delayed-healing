package com.github.dappermickie.delayed.healing;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Delayed Healing"
)
public class DelayedHealingPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private DelayedHealingConfig config;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private DelayedHealingOverlay delayedHealingOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ItemManager itemManager;

	private DelayedHealingInfoBox activeInfobox = null;

	private final Map<Integer, Integer> previousInventory = new HashMap<>();
	private boolean isEating = false;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(delayedHealingOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(delayedHealingOverlay);
		if (activeInfobox != null)
		{
			infoBoxManager.removeInfoBox(activeInfobox);
			activeInfobox = null;
		}
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState().equals(GameState.LOGGED_IN))
		{
			isEating = false;
			updateInventoryState();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("DelayedHealing"))
		{
			return;
		}
		if (!config.infobox())
		{
			infoBoxManager.removeInfoBox(activeInfobox);
			activeInfobox = null;
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		String menuOption = Text.removeTags(event.getMenuOption());
		if (menuOption.equals("Eat") && isApplicableConsumable(event.getItemId()))
		{
			isEating = true;
		}
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.INVENTORY.getId() && event.getContainerId() != InventoryID.BANK.getId())
		{
			return;
		}
		if (isEating)
		{
			detectConsumableUsage();
			isEating = false;
		}
		updateInventoryState();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (activeInfobox != null)
		{
			activeInfobox.tickTimer();
			if (activeInfobox.ticksLeft() <= 0)
			{
				infoBoxManager.removeInfoBox(activeInfobox);
				activeInfobox = null;
			}
		}
		isEating = false;
	}

	private void updateInventoryState()
	{
		previousInventory.clear();
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory != null)
		{
			for (Item item : inventory.getItems())
			{
				previousInventory.merge(item.getId(), item.getQuantity(), Integer::sum);
			}
		}
	}

	private void detectConsumableUsage()
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null)
		{
			return;
		}

		Map<Integer, Integer> currentInventory = new HashMap<>();
		for (Item item : inventory.getItems())
		{
			currentInventory.merge(item.getId(), item.getQuantity(), Integer::sum);
		}

		for (Map.Entry<Integer, Integer> entry : previousInventory.entrySet())
		{
			int itemID = entry.getKey();
			int previousItemQuantity = entry.getValue();
			if (previousItemQuantity > currentInventory.getOrDefault(itemID, 0) && isApplicableConsumable(itemID))
			{
				handleConsumable(itemID);
			}
		}
	}

	private boolean isApplicableConsumable(int itemId)
	{
		DelayedHeals item = DelayedHeals.getDelayedHealByItemId(itemId);
		return item != null && isItemEnabled(item);
	}

	private void handleConsumable(int itemId)
	{
		DelayedHeals item = DelayedHeals.getDelayedHealByItemId(itemId);
		delayedHealingOverlay.setActiveHeal(item);
		if (config.infobox())
		{
			BufferedImage itemImage = itemManager.getImage(itemId);
			DelayedHealingInfoBox infobox = new DelayedHealingInfoBox(itemImage, this, item.getTickDelay());
			if (activeInfobox != null)
			{
				infoBoxManager.removeInfoBox(activeInfobox);
			}
			infoBoxManager.addInfoBox(infobox);
			activeInfobox = infobox;
		}
	}

	private boolean isItemEnabled(DelayedHeals item)
	{
		switch (item)
		{
			case COOKED_WILD_KEBBIT:
				return config.wildKebbit();
			case COOKED_LARUPIA:
				return config.larupia();
			case COOKED_BARBTAILED_KEBBIT:
				return config.barbTailedKebbit();
			case COOKED_GRAAHK:
				return config.cookedGraahk();
			case COOKED_KYATT:
				return config.cookedKyatt();
			case COOKED_PYRE_FOX:
				return config.cookedPyreFox();
			case COOKED_DASHING_KEBBIT:
				return config.cookedDashingKebbit();
			case COOKED_SUNLIGHT_ANTELOPE:
				return config.cookedSunlightAntelope();
			case COOKED_MOONLIGHT_ANTELOPE:
				return config.cookedMoonlightAntelope();
			default:
				return false;
		}
	}

	@Provides
	DelayedHealingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DelayedHealingConfig.class);
	}
}
