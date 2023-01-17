package me.orbitium.oflootgenerator.message;

import me.orbitium.oflootgenerator.OFLootGenerator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {

    public static void sendMessage(Player player, String path) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                OFLootGenerator.getInstance().getConfig().getString(path)));
    }

}
