package ru.permasha.resourcepackcompiler.util;

import java.util.logging.Logger;

import com.sun.security.auth.login.ConfigFile;
import org.bukkit.Bukkit;


public class Debug {

    private static final Logger log = Bukkit.getLogger();

    public static void say(String s) {
        log.info(s);
    }

    public static void sayTrue(String s) {
        log.info(s);
    }

    public static void saySuper(String s) {
        log.info(s);
    }

    public static void sayError(String s) {
        log.severe(s);
    }

    public static void sayTrueError(String s) {
        log.severe(s);
    }

    public static void saySuperError(String s) {
        log.severe(s);
    }

}
