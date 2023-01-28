package me.liamhbest.nmsadvanced.utility;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class PlayerCommand extends Command {

    public PlayerCommand(String command) {
        super(command);
        init();
    }

    public PlayerCommand(String command, String... aliases) {
        super(command, "", "/" + command, Arrays.asList(aliases));
        init();
    }

    private void init() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap)bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("command", this);
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
    }

    public abstract void onPlayerCommand(Player player, String[] args);

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            this.onPlayerCommand((Player) sender, args);
        }

        return false;
    }
}
