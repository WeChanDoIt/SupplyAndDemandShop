package net.spectralskyblock.src.config;

import net.spectralskyblock.src.Main;
import net.spectralskyblock.src.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {
    private static File file = new File("plugins/SupplyDemandShop/", "config.yml");
    private static FileConfiguration data = YamlConfiguration.loadConfiguration(file);

    public static void setDefaults() {
        ConfigData.BuyPriceChanged = "&3&l[!] &bBuy Price of " + "%item%" + " &bchanged from " + "%oldbuyprice%" + " &bto " + "%newbuyprice%";
        ConfigData.SellPriceChanged = "&3&l[!] &bSell Price of " + "%item%" + " &bchanged from " + "%oldsellprice%" + " &bto " + "%newsellprice%";
        ConfigData.EconomyReset = "&4&l[!] &cThe Economy has been RESET!";
        ConfigData.UseCommand = "&4&l[!] &cUsage: /shopdebug reseteco";
        ConfigData.NoPermission = "&4&l[!] &cYou do not have permission to use this command!";
        ConfigData.amountSoldForChange = 5000;
        ConfigData.amountToChangeBy = 0.1;

        data.set("messages.buypricechanged", ConfigData.BuyPriceChanged);
        data.set("messages.sellpricechanged", ConfigData.SellPriceChanged);
        data.set("messages.ecoreset", ConfigData.EconomyReset);
        data.set("messages.commandusage", ConfigData.UseCommand);
        data.set("messages.noperms", ConfigData.NoPermission);
        data.set("defaultData.amountSoldForChange", ConfigData.amountSoldForChange);
        data.set("defaultData.amountToChangeBy", ConfigData.amountToChangeBy);

        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void loadConfig() {

        if (data.getConfigurationSection("messages") == null && data.getConfigurationSection("defaultData") == null) {
            setDefaults();
        } else {
            ConfigData.BuyPriceChanged = data.getString("messages.buypricechanged");
            ConfigData.SellPriceChanged = data.getString("messages.sellpricechanged");
            ConfigData.EconomyReset = Utils.chat(data.getString("messages.ecoreset"));
            ConfigData.UseCommand = Utils.chat(data.getString("messages.commandusage"));
            ConfigData.NoPermission = Utils.chat(data.getString("messages.noperms"));
            ConfigData.amountSoldForChange = data.getInt("defaultData.amountSoldForChange");
            ConfigData.amountToChangeBy = data.getDouble("defaultData.amountToChangeBy");
        }
    }
}
