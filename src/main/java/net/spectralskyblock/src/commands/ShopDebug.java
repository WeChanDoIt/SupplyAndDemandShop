package net.spectralskyblock.src.commands;

import net.spectralskyblock.src.Main;
import net.spectralskyblock.src.config.ConfigData;
import net.spectralskyblock.src.shop.DataHandler;
import net.spectralskyblock.src.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShopDebug implements CommandExecutor {

    private DataHandler dataHandler;

    public ShopDebug(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() || sender.hasPermission("supplydemandshop.admin")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reseteco")) {
                dataHandler.resetData(Main.getCategoryNames());
                Utils.broadcast(Utils.chat(ConfigData.EconomyReset));
            } else
            {
                sender.sendMessage(Utils.chat(ConfigData.UseCommand));
            }
        }else
        {
            sender.sendMessage(Utils.chat(ConfigData.NoPermission));
        }

        return true;
    }
}
