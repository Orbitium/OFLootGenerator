package me.orbitium.oflootgenerator.ltable;

import me.orbitium.oflootgenerator.OFLootGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;

public class LootTableItem {

    ItemStack itemStack;
    int minRange;
    int maxRange;
    int spawnChange;

    public LootTableItem(ItemStack itemStack, int spawnChange, int minRange, int maxRange) {
        this.itemStack = itemStack;
        this.spawnChange = spawnChange;
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public ItemStack tryToGenerateItem() {
        int r = OFLootGenerator.random.nextInt(100);
        if (spawnChange > r) {
            ItemStack returnedItem = itemStack.clone();
            int difference = Math.abs(maxRange - minRange);
            if (difference > 0)
                returnedItem.setAmount(minRange + OFLootGenerator.random.nextInt(difference));
            else
                returnedItem.setAmount(minRange);
            return returnedItem;
        }
        return null;
    }
}
