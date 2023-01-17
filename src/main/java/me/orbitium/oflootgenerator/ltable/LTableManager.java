package me.orbitium.oflootgenerator.ltable;

import me.orbitium.oflootgenerator.OFLootGenerator;
import me.orbitium.oflootgenerator.customitem.CustomItemManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class LTableManager {

    private static Map<NamespacedKey, OFLootTable> registeredTables = new HashMap<>();

    public static void loadTables() {
        registeredTables = new HashMap<>();

        File file = new File(OFLootGenerator.getInstance().getDataFolder().getPath() + "/LootTables/");
        for (File tableFile : file.listFiles()) {
            String name = tableFile.getName().split("\\.")[0].toUpperCase(Locale.US);
            LootTables table = null;
            if (name.equals("DEFAULT"))
                table = LootTables.VEX;
            else
                for (LootTables value : LootTables.values()) {
                    if (value.name().equals(name)) {
                        table = value;
                        break;
                    }
                }
            if (table == null) {
                for (int i = 0; i < 5; i++)
                    OFLootGenerator.getInstance().getLogger().log(Level.INFO, "Couldn't found loot table type! Searched table name: " + name);
                break;
            } else {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(tableFile);
                int maxItem = yamlConfiguration.getInt("maxDifferentItem");
                OFLootTable lootTable = new OFLootTable(maxItem);

                for (Object tableData : yamlConfiguration.getList("lootTable")) {
                    // ItemName, key, val
                    Map<String, Map<String, Object>> itemData = (Map<String, Map<String, Object>>) tableData;
                    for (Map.Entry<String, Map<String, Object>> entry : itemData.entrySet()) {
                        ItemStack targetItem = null;

                        // If target item isn't custom item
                        String key = entry.getKey();
                        if (key.startsWith("minecraft:")) {
                            String stringMaterial = key.split(":")[1].toUpperCase(Locale.US);
                            Material material = Material.getMaterial(stringMaterial);
                            if (material == null) {
                                OFLootGenerator.getInstance().getLogger().log(Level.SEVERE, "Couldn't found item! Searched item type: " + stringMaterial);
                                break;
                            }
                            targetItem = new ItemStack(material);
                        } else {
                            // If target item is custom item
                            ItemStack itemStack = CustomItemManager.getItem(key);
                            if (itemStack == null) {
                                OFLootGenerator.getInstance().getLogger().log(Level.SEVERE, "Couldn't found custom item! Searched custom item name: " + key);
                                break;
                            }
                            targetItem = itemStack;
                        }

                        lootTable.addItem(targetItem, (int) entry.getValue().get("spawnChange"), entry.getValue().get("spawnAmount") + "");

                    }
                }
                registeredTables.put(table.getKey(), lootTable);
            }
        }

    }

    public static List<ItemStack> generateLoot(LootGenerateEvent event) {
        List<ItemStack> loot = new ArrayList<>();

        // Try to add vanilla loot
        if (!OFLootGenerator.deleteVanillaLoot)
            loot.addAll(event.getLoot());


        NamespacedKey key = event.getLootTable().getKey();
        OFLootTable table = registeredTables.get(key);

        // If loot table isn't DEFAULT
        if (table != null)
            loot.addAll(table.generate());

        // Add default custom loot
        loot.addAll(registeredTables.get(LootTables.VEX.getKey()).generate());

        return loot;
    }
}
