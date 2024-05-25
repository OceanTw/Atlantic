package lol.oce.atlantis.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class StringUtils {


    public static String handleString(String message, String... params) {

        if (params == null) {
            return message;
        }

        for (int i = 0; i < params.length; i += 2) {
            message = message.replace(params[i], params[i + 1]);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
