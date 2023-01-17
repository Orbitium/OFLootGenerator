package me.orbitium.oflootgenerator.database;

import me.orbitium.oflootgenerator.OFLootGenerator;
import me.orbitium.oflootgenerator.customitem.CustomItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileWriter;

public class FileManager {

    public static void checkFiles() {
        /*
            plugins/OFLootGenerator/LootTables/<table>
            plugins/OFLootGenerator/CustomItems/<itemName>
            plugins/OFLootGenerator/config.yml
         */

        try {
            File tablePath = new File(OFLootGenerator.getInstance().getDataFolder().getPath() + "/LootTables/");
            File customItemPath = new File(OFLootGenerator.getInstance().getDataFolder().getPath() + "/CustomItems/");
            if (!tablePath.exists()) {
                tablePath.mkdirs();
                File defaultTable = new File(OFLootGenerator.getInstance().getDataFolder().getPath() + "/LootTables/DEFAULT.yml");
                defaultTable.createNewFile();
                FileWriter writer = new FileWriter(defaultTable);
                writer.write("# Config for DEFAULT table\n\n");
                writer.write("# How much item can generate The table for per trigger\n");
                writer.write("maxDifferentItem: 10\n\n");
                writer.write("# The all possible loots\n");
                writer.write("lootTable:\n");
                writer.write("    - \"ExampleCustomItem\":\n");
                writer.write("        spawnChange: 10\n");
                writer.write("        spawnAmount: \"1-1\"\n");
                writer.write("    # Vanilla Minecraft items have to start with 'minecraft:'\n");
                writer.write("    - \"minecraft:STONE\":\n");
                writer.write("        spawnChange: 100\n");
                writer.write("        spawnAmount: \"30-60\"");
                writer.close();
            }
            if (!customItemPath.exists()) {
                customItemPath.mkdirs();
                ItemStack itemStack = new ItemStack(Material.TRIDENT);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.GOLD + "Example custom item");
                itemMeta.addEnchant(Enchantment.LOYALTY, 1, false);
                itemStack.setItemMeta(itemMeta);
                writeCustomItemToFile("ExampleCustomItem", itemStack);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void writeCustomItemToFile(String name, ItemStack itemStack) {
        try {
            File customItemFile = new File(OFLootGenerator.getInstance().getDataFolder().getPath() + "/CustomItems/" + name + ".data");
            customItemFile.createNewFile();
            FileWriter writer = new FileWriter(customItemFile);
            writer.write(CustomItemManager.itemStackToBase64(itemStack));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteCustomItemFile(String name) {
        try {
            File customItemFile = new File(OFLootGenerator.getInstance().getDataFolder().getPath() + "/CustomItems/" + name + ".data");
            return customItemFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
