package com.github.liamdev06.mc.nmsadvanced;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import com.github.liamdev06.mc.nmsadvanced.models.ServerMenuInfoModifier;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.logging.Logger;

/**
 * Main plugin class
 *
 * @author Liam, not from course.
 */
public class NMSPlugin extends JavaPlugin {

    private static NMSPlugin INSTANCE;
    private final String NAME = this.getDescription().getName();
    private final String VERSION = this.getDescription().getVersion();
    private final Logger log = this.getLogger();
    private @Nullable ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        final long time = System.currentTimeMillis();
        log.info(NAME + " version " + VERSION + " is starting up...");
        INSTANCE = this;

        // Setup ProtocolLib
        if (this.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            this.protocolManager = ProtocolLibrary.getProtocolManager();
        } else {
            log.warning("ProtocolLib is not enabled! This will decrease the amount of features enabled.");
        }

        // Register commands
        this.registerCommands();

        // Models
        this.loadModels();

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
        for (Class<?> clazz : AutoRegistry.getClassesWithRegisterType(AutoRegistry.Type.COMMAND)) {
            if (clazz.getTypeParameters().length == 0) {
                AutoRegistry.register(clazz, null, null);
            } else {
                AutoRegistry.register(clazz, NMSPlugin.class, this);
            }
        }
    }

    private void loadModels() {
        // Load in specific models that requires ProtocolLib
        if (this.protocolManager != null) {
            new ServerMenuInfoModifier(this, this.protocolManager);
        }
    }

    /**
     * @return the singleton plugin instance
     */
    public static NMSPlugin getInstance() {
        return INSTANCE;
    }

    /**
     * @return the protocol manager from the spigot plugin <a href="https://www.spigotmc.org/resources/protocollib.1997/">ProtocolLib</a>
     */
    public @Nullable ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }
}
