package net.spectralskyblock.src.commands;

import net.spectralskyblock.src.Main;
import net.spectralskyblock.src.shop.DataHandler;
import net.spectralskyblock.src.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopDebug implements CommandExecutor {

    private DataHandler dataHandler;

    public ShopDebug(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() || sender.hasPermission("supplydemandshop.admin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reseteco")) {
                dataHandler.resetData(Main.getCategoryNames());
                System.out.println(Utils.chat("&4&l[!] &cSupply/Demand Shop Plugin Cleaned! Eco has been reset!"));
                System.out.println(Utils.chat("&4&l[!] &cBackup of Economy stored in backup.yml"));
                Utils.broadcast(Utils.chat("&8--------------------------"));
                Utils.broadcast(Utils.chat("&4&l[!] &cThe Economy has been RESET!"));
                Utils.broadcast(Utils.chat("&8--------------------------"));
            } else
            {
                sender.sendMessage(Utils.chat("&4&l[!] &cUsage: /shopdebug reseteco"));
            }
        }else
        {
            sender.sendMessage(Utils.chat("&4&l[!] &cYou do not have permission to use this command!"));
        }

        return true;
    }
}
