package me.orbitium.oflootgenerator;

import me.orbitium.oflootgenerator.commands.LootCommandCompleter;
import me.orbitium.oflootgenerator.commands.LootCommandExecutor;
import me.orbitium.oflootgenerator.customitem.CustomItemManager;
import me.orbitium.oflootgenerator.database.FileManager;
import me.orbitium.oflootgenerator.listener.LootGenerateListener;
import me.orbitium.oflootgenerator.ltable.LTableManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class OFLootGenerator extends JavaPlugin {

    private static OFLootGenerator instance;
    public static Random random;
    public static boolean deleteVanillaLoot;
    public static boolean debugMode;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        random = new Random();
        saveDefaultConfig();
        getCommand("loot").setExecutor(new LootCommandExecutor());
        getCommand("loot").setTabCompleter(new LootCommandCompleter());
        deleteVanillaLoot = getInstance().getConfig().getBoolean("removeVanillaLoot");
        debugMode = getInstance().getConfig().getBoolean("debugMode");
        FileManager.checkFiles();
        CustomItemManager.loadCustomItems();
        LTableManager.loadTables();

        getServer().getPluginManager().registerEvents(new LootGenerateListener(), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static OFLootGenerator getInstance() {
        return instance;
    }
}
