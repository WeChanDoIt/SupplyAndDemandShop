package net.spectralskyblock.src.shop;

import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.ShopItem;
import net.spectralskyblock.src.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private File file = new File("plugins/SupplyDemandShop/", "data.yml");
    private File backup = new File("plugins/SupplyDemandShop/", "backup.yml");
    private FileConfiguration data = YamlConfiguration.loadConfiguration(file);
    private FileConfiguration backupData = YamlConfiguration.loadConfiguration(backup);
    private List<Item> itemList = new ArrayList<Item>();

    public List<Item> getItemList() {
        return itemList;
    }

    public int amountOfPages = itemList.size() / 45;

    public void resetData(List<String> categories)
    {

        backupData.set("shops", new ArrayList<String>());
        try {
            backupData.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (itemList == null || itemList.isEmpty()) {
            return;
        }
        for (String category : categories) {
            List<ShopItem> shopItems = ShopGuiPlusApi.getShop(category).getShopItems();
            for (ShopItem item : shopItems) {
                Item i = getItemFromShopItem(item);
                backupData.set("shops." + category + "." + item.getId() + ".amountBought", i.getAmountBought());
                backupData.set("shops." + category + "." + item.getId() + ".amountSold", i.getAmountSold());
                backupData.set("shops." + category + "." + item.getId() + ".canChangePrice", i.isAllowedToChangePrice());
            }
        }
        try {
            backupData.save(backup);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Item item : itemList) {
            if (item.getShopItem().getBuyPrice() != item.getOriginalBuyPrice()) {
                item.getShopItem().setBuyPrice(item.getOriginalBuyPrice());
            }

            if (item.getShopItem().getSellPrice() != item.getOriginalSellPrice()) {
                item.getShopItem().setSellPrice(item.getOriginalSellPrice());
            }
        }

        loadData(categories);
        saveData(Main.getCategoryNames());
    }

    public void createNewData(List<String> categories) {
        for (String category : categories) {
            List<ShopItem> shopItems = ShopGuiPlusApi.getShop(category).getShopItems();
            for (ShopItem item : shopItems) {
                Item i = new Item(item, 0, 0, true, item.getBuyPrice(), item.getSellPrice());
                itemList.add(i);
            }
        }
    }

    public void loadData(List<String> categories) {
        if (data.getConfigurationSection("shops") == null) {
            return;
        }
        for (String category : categories) {
            List<ShopItem> shopItems = ShopGuiPlusApi.getShop(category).getShopItems();
            for (ShopItem item : shopItems) {

                double amountBought = 0;
                double amountSold = 0;
                double sellPrice = item.getSellPrice();
                double buyPrice = item.getBuyPrice();
                boolean canChangePrice = true;
                String id = item.getId();

                if (data.get("shops." + category + "." + id) != null) {
                    amountBought = data.getDouble("shops." + category + "." + id + ".amountBought");
                    amountSold = data.getDouble("shops." + category + "." + id + ".amountSold");
                    buyPrice = data.getDouble("shops." + category + "." + id + ".finalBuy");
                    sellPrice = data.getDouble("shops." + category + "." + id + ".finalSell");
                    canChangePrice = data.getBoolean("shops." + category + "." + id + ".canChangePrice");
                }
                Item i = new Item(item, amountBought, amountSold, canChangePrice, buyPrice, sellPrice);
                itemList.add(i);
            }
        }
    }

    public void saveData(List<String> categories) {

        // save important and not-redundant info
        data.set("shops", new ArrayList<String>());
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (itemList == null || itemList.isEmpty()) {
            return;
        }
        for (String category : categories) {
            List<ShopItem> shopItems = ShopGuiPlusApi.getShop(category).getShopItems();
            for (ShopItem item : shopItems) {
                Item i = getItemFromShopItem(item);
                data.set("shops." + category + "." + item.getId() + ".amountBought", i.getAmountBought());
                data.set("shops." + category + "." + item.getId() + ".amountSold", i.getAmountSold());
                data.set("shops." + category + "." + item.getId() + ".finalBuy", i.getShopItem().getBuyPrice());
                data.set("shops." + category + "." + item.getId() + ".finalSell", i.getShopItem().getSellPrice());
                data.set("shops." + category + "." + item.getId() + ".canChangePrice", i.isAllowedToChangePrice());
            }
        }
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // revert item prices
        for (Item item : itemList) {
            if (item.getShopItem().getBuyPrice() != item.getOriginalBuyPrice()) {
                item.getShopItem().setBuyPrice(item.getOriginalBuyPrice());
            }

            if (item.getShopItem().getSellPrice() != item.getOriginalSellPrice()) {
                item.getShopItem().setSellPrice(item.getOriginalSellPrice());
            }
        }
    }

    public void getData(List<String> categories) {
        if (!file.exists()) {
            createNewData(categories);
        } else {
            loadData(categories);
            editPrices();
        }
    }

    public void editPrices()
    {
        for (Item item : itemList)
        {
            ShopItem shopItem = item.getShopItem();
            int buyMultiplier = ((int) item.getAmountBought() / 5000);
            int sellMultiplier = ((int) item.getAmountSold() / 5000);
            if (item.isAllowedToChangePrice()) {
                if (buyMultiplier >= 1 && shopItem.getBuyPrice() > 1) {
                    if (shopItem.getSellPrice() != -1) shopItem.setSellPrice(shopItem.getSellPrice() + (buyMultiplier * 0.1));
                    if (shopItem.getBuyPrice() != -1) shopItem.setBuyPrice(shopItem.getBuyPrice() - (buyMultiplier * 0.1));
                }
                if (sellMultiplier >= 1 && shopItem.getSellPrice() > 1) {
                    if (shopItem.getSellPrice() != -1) shopItem.setSellPrice(shopItem.getSellPrice() - (sellMultiplier * 0.1));
                    if (shopItem.getBuyPrice() != -1)  shopItem.setBuyPrice(shopItem.getBuyPrice() + (sellMultiplier * 0.1));
                }
            }
        }
    }

    public Item getItemFromShopItem(ShopItem item) {
        for (Item i : itemList) {
            if (i.getShopItem() == item) return i;
        }
        return null;
    }

}
