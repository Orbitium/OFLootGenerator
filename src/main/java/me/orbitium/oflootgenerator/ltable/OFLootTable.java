package me.orbitium.oflootgenerator.ltable;

import me.orbitium.oflootgenerator.OFLootGenerator;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class OFLootTable {

    int maxDifferentItem;
    List<LootTableItem> items = new ArrayList<>();

    public OFLootTable(int maxDifferentItem) {
        this.maxDifferentItem = maxDifferentItem;
    }

    public void addItem(ItemStack itemStack, int spawnChange, String spawnRange) {
        String[] range = spawnRange.split("-");

        int minSpawn = Integer.parseInt(range[0].replaceAll(" ", ""));
        int maxSpawn = Integer.parseInt(range[1].replaceAll(" ", ""));

        LootTableItem item = new LootTableItem(itemStack, spawnChange, minSpawn, maxSpawn);
        items.add(item);
    }


    public List<ItemStack> generate() {
        List<ItemStack> loot = new ArrayList<>();
        List<LootTableItem> possibleItems = new ArrayList<>(items);

        for (int i = 0; i < Math.min(maxDifferentItem, items.size()); i++) {
            LootTableItem lootTableItem = possibleItems.get(OFLootGenerator.random.nextInt(possibleItems.size()));
            possibleItems.remove(lootTableItem);
            ItemStack itemStack = lootTableItem.tryToGenerateItem();
            if (itemStack != null)
                loot.add(itemStack);
        }
        return loot;
    }

}
