package net.spectralskyblock.src;

import net.brcdev.shopgui.ShopGuiPlusApi;
import net.spectralskyblock.src.commands.ShopDebug;
import net.spectralskyblock.src.config.ConfigHandler;
import net.spectralskyblock.src.events.ShopEvents;
import net.spectralskyblock.src.shop.DataHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private DataHandler dataHandler = new DataHandler();
    public static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;
        ConfigHandler.loadConfig();

        if (hasShopGUIPlus()) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    dataHandler.getData(getCategoryNames());
                    loadEvents();
                    loadCommands();
                    System.out.println("Supply/Demand Shop Plugin Loaded!");
                }
            }.runTaskLater(this, 20); // 1 second delay or the plugin freaks out
        }
    }

    public void loadEvents()
    {
        Bukkit.getPluginManager().registerEvents(new ShopEvents(dataHandler), this);
    }

    public void loadCommands() {
        getServer().getPluginCommand("shopdebug").setExecutor(new ShopDebug(dataHandler));
    }

    @Override
    public void onDisable() {
        dataHandler.saveData(getCategoryNames());
        dataHandler = new DataHandler();
    }

    public static List<String> getCategoryNames()
    {
        List<String> categories = new ArrayList<String>();
        FileConfiguration shopConfig = YamlConfiguration.loadConfiguration(new File("plugins/ShopGUIPlus/", "config.yml"));
        if (shopConfig.getConfigurationSection("shopMenuItems") != null) {
            for (String string : shopConfig.getConfigurationSection("shopMenuItems").getKeys(false)) {
                categories.add(shopConfig.getString("shopMenuItems." + string + ".shop"));
            }
        }

        return categories;
    }

    public boolean hasShopGUIPlus() {
        return (getServer().getPluginManager().getPlugin("ShopGUIPlus") != null);
    }
}
