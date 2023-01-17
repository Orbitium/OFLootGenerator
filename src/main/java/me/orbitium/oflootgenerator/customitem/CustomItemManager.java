package me.orbitium.oflootgenerator.customitem;

import me.orbitium.oflootgenerator.OFLootGenerator;
import me.orbitium.oflootgenerator.database.FileManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class CustomItemManager {

    private static Map<String, ItemStack> registeredItems = new HashMap<>();

    public static void loadCustomItems() {
        try {
            registeredItems = new HashMap<>();
            File file = new File(OFLootGenerator.getInstance().getDataFolder().getPath() + "/CustomItems/");
            StringBuilder sb = new StringBuilder();
            for (File customItem : file.listFiles()) {
                Scanner scanner = new Scanner(customItem);
                while (scanner.hasNextLine())
                    sb.append(scanner.nextLine());

                registeredItems.put(customItem.getName().split("\\.")[0], itemStackFromBase64(sb.toString()));
                sb.setLength(0);
                scanner.close();
            }
            OFLootGenerator.getInstance().getLogger().log(Level.INFO, "Custom items loaded!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static boolean addItem(String name, ItemStack itemStack) {
        if (!registeredItems.containsKey(name)) {
            registeredItems.put(name, itemStack);
            FileManager.writeCustomItemToFile(name, itemStack);
            return true;
        }

        return false;
    }

    public static boolean deleteItem(String name) {
        registeredItems.remove(name);
        return FileManager.deleteCustomItemFile(name);
    }

    public static ItemStack getItem(String name) {
        ItemStack itemStack = registeredItems.get(name);
        if (itemStack != null)
            return registeredItems.get(name);

        for (int i = 0; i < 5; i++)
            OFLootGenerator.getInstance().getLogger().log(Level.SEVERE, "Unregistered custom item access! Undefined item name: " + name);
        return null;
    }

    public static boolean isNameAvailable(String name) {
        return !registeredItems.containsKey(name);
    }

    public static Set<String> getAllNames() {
        return registeredItems.keySet();
    }

    public static String itemStackToBase64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack itemStackFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack itemStack = (ItemStack) dataInput.readObject();

            dataInput.close();
            return itemStack;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
