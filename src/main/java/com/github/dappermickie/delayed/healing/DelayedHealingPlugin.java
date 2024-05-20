package com.github.dappermickie.delayed.healing;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

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
	private ItemManager itemManager;

	private DelayedHealingInfoBox activeInfobox = null;

	private final Map<Integer, Item> previousInventory = new HashMap<>();
	private boolean isEating = false;

	@Override
	protected void startUp() throws Exception
	{
		updateInventoryState();
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		if (event.getActor() == client.getLocalPlayer())
		{
			int animationId = client.getLocalPlayer().getAnimation();
			if (isEatingAnimation(animationId))
			{
				isEating = true;
			}
		}
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
		if (isEating)
		{
			detectConsumableUsage();
			isEating = false;
		}
		updateInventoryState();
	}

	private void updateInventoryState()
	{
		previousInventory.clear();
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory != null)
		{
			Item[] items = inventory.getItems();
			for (int i = 0; i < items.length; i++)
			{
				Item item = items[i];
				previousInventory.put(i, item);
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

		Item[] items = inventory.getItems();
		for (int i = 0; i < items.length; i++)
		{
			Item item = items[i];
			Item previousItem = previousInventory.getOrDefault(i, null);
			int currentQuantity = item.getQuantity();

			if (previousItem.getQuantity() > currentQuantity && isConsumable(previousItem.getId()))
			{
				handleConsumable(previousItem.getId());
			}
		}
	}

	private boolean isConsumable(int itemId)
	{
		return DelayedHeals.getDelayedHealByItemId(itemId) != null;
	}

	private void handleConsumable(int itemId)
	{
		BufferedImage itemImage = itemManager.getImage(itemId);
		DelayedHeals item = DelayedHeals.getDelayedHealByItemId(itemId);
		if (shouldShowInfoBoxForItem(item))
		{
			DelayedHealingInfoBox infobox = new DelayedHealingInfoBox(itemImage, this, item.getTickDelay());
			infoBoxManager.addInfoBox(infobox);
			if (activeInfobox != null)
			{
				infoBoxManager.removeInfoBox(activeInfobox);
			}
			activeInfobox = infobox;
		}
	}

	private boolean shouldShowInfoBoxForItem(DelayedHeals item)
	{
		switch (item)
		{
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

	private boolean isEatingAnimation(int animationId)
	{
		return animationId == 829;
	}

	@Provides
	DelayedHealingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DelayedHealingConfig.class);
	}
}
