package net.spectralskyblock.src.events;

import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.ShopItem;
import net.brcdev.shopgui.shop.ShopManager;
import net.spectralskyblock.src.shop.DataHandler;
import net.spectralskyblock.src.shop.Item;
import net.spectralskyblock.src.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.rmi.CORBA.Util;
import java.text.DecimalFormat;

public class ShopEvents implements Listener {

    private DataHandler dataHandler;

    public ShopEvents(DataHandler dataHandler)
    {
        this.dataHandler = dataHandler;
    }

    @EventHandler
    public void onShopTransaction(ShopPreTransactionEvent event)
    {
        Player player = event.getPlayer();
        Item item = dataHandler.getItemFromShopItem(event.getShopItem());
        if (item != null)
        {

            boolean hasChanged = false;
            double originalSell = item.getOriginalSellPrice();
            double originalBuy = item.getOriginalBuyPrice();
            ShopItem shopItem = item.getShopItem();
            double buyPrice = shopItem.getBuyPrice();
            double sellPrice = shopItem.getSellPrice();
            DecimalFormat df = new DecimalFormat("$#,###.0");

            if (event.getShopAction().equals(ShopManager.ShopAction.BUY))
            {
                double origAmtBought = item.getAmountBought();
                item.setAmountBought(item.getAmountBought() + event.getAmount());
                int multiplier = Math.abs(((int) origAmtBought / 5000) - ((int) item.getAmountBought() / 5000));
                if (multiplier >= 1 && item.isAllowedToChangePrice() && buyPrice - 0.1 >= 1)
                {
                    hasChanged = true;
                    if (sellPrice != -1) shopItem.setSellPrice( sellPrice + 0.1);
                    if (buyPrice != -1) shopItem.setBuyPrice( buyPrice - 0.1);
                }
            } else if (event.getShopAction().equals(ShopManager.ShopAction.SELL) || event.getShopAction().equals(ShopManager.ShopAction.SELL_ALL))
            {
                double origAmtSold = item.getAmountSold();
                item.setAmountSold(item.getAmountSold() + event.getAmount());
                int multiplier = Math.abs(((int) origAmtSold / 5000) - ((int) item.getAmountSold() / 5000));
                if (multiplier >= 1 && item.isAllowedToChangePrice() && sellPrice - 0.1 >= 1)
                {
                    hasChanged = true;
                    if (sellPrice != -1) shopItem.setSellPrice( sellPrice - 0.1);
                    if (buyPrice != -1) shopItem.setBuyPrice( buyPrice + 0.1);
                }
            }

            if (hasChanged) {
                if (sellPrice != -1 && sellPrice - 0.1 >= 1) player.sendMessage(Utils.chat("&3&l[!] &bBuy Price of " + Utils.getFriendlyName(shopItem.getItem(), true) + " &bchanged from " + Utils.getChangeColor(originalBuy, buyPrice) + df.format(buyPrice) + " &bto " + Utils.getChangeColor(originalBuy, shopItem.getBuyPrice()) + df.format(shopItem.getBuyPrice())));
                if (buyPrice != -1 && buyPrice - 0.1 >= 1) player.sendMessage(Utils.chat("&3&l[!] &bSell Price of " + Utils.getFriendlyName(shopItem.getItem(), true) + " &bchanged from " + Utils.getChangeColor(originalSell, sellPrice) + df.format(sellPrice) + " &bto " + Utils.getChangeColor(originalSell, shopItem.getSellPrice()) + df.format(shopItem.getSellPrice())));
            }

        }
    }

}
