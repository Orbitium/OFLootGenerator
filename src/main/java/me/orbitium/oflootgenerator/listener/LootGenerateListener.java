package me.orbitium.oflootgenerator.listener;

import me.orbitium.oflootgenerator.OFLootGenerator;
import me.orbitium.oflootgenerator.ltable.LTableManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.logging.Level;

public class LootGenerateListener implements Listener {

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        List<ItemStack> newLoot = LTableManager.generateLoot(event);
        event.setLoot(newLoot);
        if (OFLootGenerator.debugMode)
            OFLootGenerator.getInstance().getLogger().log(Level.INFO, "Loot generated with loot table key: " + event.getLootTable().getKey());
    }

}
