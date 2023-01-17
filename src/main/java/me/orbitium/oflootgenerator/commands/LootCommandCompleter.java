package me.orbitium.oflootgenerator.commands;

import me.orbitium.oflootgenerator.customitem.CustomItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LootCommandCompleter implements TabCompleter {

    List<String> emptyList = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp())
            return emptyList;
        if (args.length == 1)
            return Arrays.asList("save", "delete", "preview", "reload");
        else if (args.length == 2) {
            if (!args[0].equals("save") && !args[0].equals("reload"))
                return new ArrayList<>(CustomItemManager.getAllNames());
            else if (args[0].equals("save"))
                return Collections.singletonList("<name>");
        }
        return emptyList;
    }
}
