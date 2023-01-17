package me.orbitium.oflootgenerator.commands;

import me.orbitium.oflootgenerator.OFLootGenerator;
import me.orbitium.oflootgenerator.customitem.CustomItemManager;
import me.orbitium.oflootgenerator.ltable.LTableManager;
import me.orbitium.oflootgenerator.message.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;

public class LootCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Loot commands ONLY can be used by PLAYERS");
            return true;
        }

        if (!sender.isOp())
            return true;

        if (args.length == 2) {
            switch (args[0]) {
                case "save" -> {
                    ItemStack targetItem = player.getInventory().getItemInMainHand();
                    if (targetItem.getType() == Material.AIR) {
                        MessageUtil.sendMessage(player, "messages.errors.youHaveToHoldItem");
                        return true;
                    }

                    String name = args[1];
                    int length = name.length();
                    if (length < 3 || length > 16) {
                        MessageUtil.sendMessage(player, "messages.errors.invalidNameLength");
                        return true;
                    }

                    if (!CustomItemManager.isNameAvailable(name)) {
                        MessageUtil.sendMessage(player, "messages.errors.nameMustBeUnique");
                        return true;
                    }
                    ItemStack itemStack = targetItem.clone();
                    itemStack.setAmount(1);
                    CustomItemManager.addItem(name, itemStack);
                    MessageUtil.sendMessage(player, "messages.itemSaved");
                }

                case "delete" -> {
                    String name = args[1];
                    int length = name.length();
                    if (length < 3 || length > 16) {
                        MessageUtil.sendMessage(player, "messages.errors.invalidNameLength");
                        return true;
                    }

                    if (CustomItemManager.isNameAvailable(name)) {
                        MessageUtil.sendMessage(player, "messages.errors.invalidName");
                        return true;
                    }

                    CustomItemManager.deleteItem(name);
                    MessageUtil.sendMessage(player, "messages.itemDeleted");
                }

                case "preview" -> {
                    String name = args[1];
                    int length = name.length();
                    if (length < 3 || length > 16) {
                        MessageUtil.sendMessage(player, "messages.errors.invalidNameLength");
                        return true;
                    }

                    if (CustomItemManager.isNameAvailable(name)) {
                        MessageUtil.sendMessage(player, "messages.errors.invalidName");
                        return true;
                    }

                    player.getInventory().addItem(CustomItemManager.getItem(name));
                    MessageUtil.sendMessage(player, "messages.itemGave");
                }
            }
        } else if (args.length == 1) {
            if (args[0].equals("reload")) {
                Bukkit.getScheduler().runTaskAsynchronously(OFLootGenerator.getInstance(), () -> {
                    MessageUtil.sendMessage(player, "messages.reloadStarted");
                    CustomItemManager.loadCustomItems();
                    LTableManager.loadTables();
                    OFLootGenerator.getInstance().reloadConfig();
                    OFLootGenerator.deleteVanillaLoot = OFLootGenerator.getInstance().getConfig().getBoolean("removeVanillaLoot");
                    OFLootGenerator.debugMode = OFLootGenerator.getInstance().getConfig().getBoolean("debugMode");
                    MessageUtil.sendMessage(player, "messages.reloadFinished");
                });
            }
        }
        return false;
    }
}
