package com.github.liamdev06.mc.nmsadvanced;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.liamdev06.mc.nmsadvanced.utility.NMSUtil;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import com.github.liamdev06.mc.nmsadvanced.models.ServerMenuInfoModifier;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.logging.Logger;

/**
 * Main plugin class
 *
 * @author Liam
 */
public class NMSPlugin extends JavaPlugin {

    private static NMSPlugin INSTANCE;
    private final String NAME = this.getDescription().getName();
    private final String VERSION = this.getDescription().getVersion();
    private final @NonNull Logger log = this.getLogger();
    private @Nullable ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        final long time = System.currentTimeMillis();
        log.info(NAME + " version " + VERSION + " is starting up...");
        INSTANCE = this;
        final PluginManager pluginManager = this.getServer().getPluginManager();

        // Hook into ProtocolLib plugin
        if (pluginManager.isPluginEnabled("ProtocolLib")) {
            this.protocolManager = ProtocolLibrary.getProtocolManager();
        } else {
            log.warning("ProtocolLib is not enabled! This will decrease the amount of features enabled.");
        }

        // Register commands
        this.registerCommands();

        // Register listeners
        this.registerListeners(pluginManager);

        // Models
        this.loadModels();

        // Register entities
        this.registerEntities();

        // Done
        log.info(NAME + " version " + VERSION + " has loaded successfully in " + (System.currentTimeMillis() - time) + "ms!");
    }

    @Override
    public void onDisable() {
        long time = System.currentTimeMillis();

        // Shutdown code

        INSTANCE = null;
        log.info(NAME + " version " + VERSION + " disabled in " + (System.currentTimeMillis() - time) + "ms!");
    }

    /**
     * Register all commands by initializing the constructor of all classes with annotation {@link AutoRegister} with element type {@link AutoRegistry.Type#COMMAND}
     */
    private void registerCommands() {
        for (Class<?> clazz : AutoRegistry.getClassesWithRegisterType(AutoRegistry.Type.COMMAND)) {
            // If the class has no parameters, register without it
            if (clazz.getTypeParameters().length == 0) {
                AutoRegistry.register(clazz, null, null);
            } else {
                // The class has parameters, register it with the default value which is this class
                AutoRegistry.register(clazz, NMSPlugin.class, this);
            }
        }
    }

    /**
     * Register all listeners by looping through all classes with annotation {@link AutoRegister} with element type {@link AutoRegistry.Type#LISTENER}
     * and register using {@link PluginManager#registerEvents(Listener, Plugin)}
     */
    private void registerListeners(PluginManager pluginManager) {
        for (Class<?> clazz : AutoRegistry.getClassesWithRegisterType(AutoRegistry.Type.LISTENER)) {
            // The target class contains no interfaces (which means no listener implemented), continue
            if (clazz.getInterfaces().length == 0) {
                continue;
            }

            try {
                // Initialize new constructor and cast to bukkit listener
                Listener listener = (Listener) clazz.getConstructor().newInstance();

                // Register the event using the regular method of PluginManager#registerEvents
                pluginManager.registerEvents(listener, this);
            } catch (Exception exception) {
                exception.printStackTrace();
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
     * Register all custom entities by looping through all classes with annotation {@link AutoRegister} with element type {@link AutoRegistry.Type#CUSTOM_ENTITY}
     * and register them using {@link NMSUtil#registerEntity(String, int, Class)}
     */
    private void registerEntities() {
        for (Class<?> clazz : AutoRegistry.getClassesWithRegisterType(AutoRegistry.Type.CUSTOM_ENTITY)) {
            // Register the entity with the NMSUtil
            NMSUtil.registerEntity(clazz.getSimpleName(), clazz.getAnnotation(AutoRegister.class).entityId(), clazz);
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
