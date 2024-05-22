package lol.oce.atlantis.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class StringUtils {


    public static String handleString(String message, String... params) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (params == null) {
            return message;
        }

        for (int i = 0; i < params.length; i += 2) {
            message = message.replaceAll(params[i], params[i + 1]);
        }

        return message;
    }
}
