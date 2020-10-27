package net.spectralskyblock.src.events;

import net.brcdev.shopgui.event.ShopPreTransactionEvent;
import net.brcdev.shopgui.shop.ShopItem;
import net.brcdev.shopgui.shop.ShopManager;
import net.spectralskyblock.src.config.ConfigData;
import net.spectralskyblock.src.shop.DataHandler;
import net.spectralskyblock.src.shop.Item;
import net.spectralskyblock.src.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
            int amountSoldForChange = ConfigData.amountSoldForChange;
            double amountToChangeBy = ConfigData.amountToChangeBy;

            DecimalFormat df = new DecimalFormat("$#,###.0");

            if (event.getShopAction().equals(ShopManager.ShopAction.BUY))
            {
                double origAmtBought = item.getAmountBought();
                item.setAmountBought(item.getAmountBought() + event.getAmount());
                int multiplier = Math.abs(((int) origAmtBought / amountSoldForChange) - ((int) item.getAmountBought() / amountSoldForChange));
                if (multiplier >= 1 && item.isAllowedToChangePrice() && buyPrice - amountToChangeBy >= 1)
                {
                    hasChanged = true;
                    amountToChangeBy *= multiplier;
                    if (sellPrice != -1) shopItem.setSellPrice( sellPrice + amountToChangeBy);
                    if (buyPrice != -1) shopItem.setBuyPrice( buyPrice - amountToChangeBy);
                }
            } else if (event.getShopAction().equals(ShopManager.ShopAction.SELL) || event.getShopAction().equals(ShopManager.ShopAction.SELL_ALL))
            {
                double origAmtSold = item.getAmountSold();
                item.setAmountSold(item.getAmountSold() + event.getAmount());
                int multiplier = Math.abs(((int) origAmtSold / amountSoldForChange) - ((int) item.getAmountSold() / amountSoldForChange));
                if (multiplier >= 1 && item.isAllowedToChangePrice() && sellPrice - amountToChangeBy >= 1)
                {
                    hasChanged = true;
                    amountToChangeBy *= multiplier;
                    if (sellPrice != -1) shopItem.setSellPrice( sellPrice - amountToChangeBy);
                    if (buyPrice != -1) shopItem.setBuyPrice( buyPrice + amountToChangeBy);
                }
            }

            if (hasChanged) {
                if (sellPrice != -1 && sellPrice - amountToChangeBy >= 1) player.sendMessage(Utils.chat(ConfigData.BuyPriceChanged.replace("%item%", Utils.getFriendlyName(shopItem.getItem(), true)).replace("%oldbuyprice%", Utils.getChangeColor(originalBuy, buyPrice) + df.format(buyPrice)).replace("%newbuyprice%", Utils.getChangeColor(originalBuy, shopItem.getBuyPrice()) + df.format(shopItem.getBuyPrice()))));
                if (buyPrice != -1 && buyPrice - amountToChangeBy >= 1) player.sendMessage(Utils.chat(ConfigData.SellPriceChanged.replace("%item%", Utils.getFriendlyName(shopItem.getItem(), true)).replace("%oldsellprice%", Utils.getChangeColor(originalSell, sellPrice) + df.format(sellPrice)).replace("%newsellprice%", Utils.getChangeColor(originalSell, shopItem.getSellPrice()) + df.format(shopItem.getSellPrice()))));
            }

        }
    }

}
