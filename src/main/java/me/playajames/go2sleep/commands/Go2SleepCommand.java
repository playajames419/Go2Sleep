package me.playajames.go2sleep.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.playajames.go2sleep.Go2Sleep;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.playajames.go2sleep.Go2Sleep.BEDTIME_ENCHANTMENT;
import static me.playajames.go2sleep.Go2Sleep.PREFIX;

public class Go2SleepCommand extends CommandAPICommand {

    public Go2SleepCommand(String commandName) {
        super(commandName);
        withAliases("g2s");
        withPermission("go2sleep.command.execute");
        withSubcommand(new Go2SleepGiveSubCommand("give"));
        register();
    }

}

class Go2SleepGiveSubCommand extends CommandAPICommand {

    public Go2SleepGiveSubCommand(String commandName) {
        super(commandName);
        populateArguments();

        executes(this::execute);

        register();
    }

    private void populateArguments() {
        List<Argument> arguments = getArguments();
        arguments.add(new PlayerArgument("player"));
        arguments.add(new IntegerArgument("amount"));
        setArguments(arguments);
    }

    private void execute(CommandSender sender, Object[] args) {
        ConfigurationSection config = Go2Sleep.getProvidingPlugin(Go2Sleep.class).getConfig();
        Player player = (Player) args[0];
        int amount = (Integer) args[1];
        ItemStack item = new ItemStack(Material.BOW);
        item.setAmount(amount);
        item.addEnchantment(BEDTIME_ENCHANTMENT, 1);
        item.addUnsafeEnchantment(Enchantment.LURE, 1);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(config.getString("item-name"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + BEDTIME_ENCHANTMENT.getName());
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);

        player.sendMessage(PREFIX + config.getString("give-message"));

    }
}
