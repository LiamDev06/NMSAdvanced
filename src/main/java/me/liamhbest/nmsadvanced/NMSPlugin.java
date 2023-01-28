package me.liamhbest.nmsadvanced;

import me.liamhbest.nmsadvanced.commands.DirectPingCommand;
import me.liamhbest.nmsadvanced.commands.ReflectionPingCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class NMSPlugin extends JavaPlugin {

    private static NMSPlugin INSTANCE;
    private final String NAME = getDescription().getName();
    private final String VERSION = getDescription().getVersion();
    private final Logger log = getLogger();

    @Override
    public void onEnable() {
        final long time = System.currentTimeMillis();
        log.info(NAME + " version " + VERSION + " is starting up...");
        INSTANCE = this;

        // Register commands
        this.registerCommands();

        // Done
        log.info(NAME + " version " + VERSION + " has loaded successfully in " + (System.currentTimeMillis() - time) + "ms!");
    }

    @Override
    public void onDisable() {
        long time = System.currentTimeMillis();

        // Shutdown code
        // TODO

        INSTANCE = null;
        log.info(NAME + " version " + VERSION + " disabled in " + (System.currentTimeMillis() - time) + "ms!");
    }

    private void registerCommands() {
        new DirectPingCommand();
        new ReflectionPingCommand();
    }

    public static NMSPlugin getInstance() {
        return INSTANCE;
    }
}
