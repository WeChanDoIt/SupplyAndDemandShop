package net.spectralskyblock.src.shop;

import net.brcdev.shopgui.shop.ShopItem;

public class Item {
    private ShopItem shopItem;
    private double amountSold;
    private double amountBought;
    private double originalBuyPrice;
    private double originalSellPrice;
    private boolean allowedToChangePrice;

    public Item(ShopItem shopItem, double amountBought, double amountSold, boolean allowedToChangePrice, double buyPrice, double sellPrice)
    {
        this.shopItem = shopItem;
        this.amountBought = amountBought;
        this.amountSold = amountSold;
        this.originalBuyPrice = shopItem.getBuyPrice();
        this.originalSellPrice = shopItem.getSellPrice();
        this.allowedToChangePrice = allowedToChangePrice;
        shopItem.setBuyPrice(buyPrice);
        shopItem.setSellPrice(sellPrice);
    }

    public boolean isAllowedToChangePrice() {
        return allowedToChangePrice;
    }

    public void setAllowedToChangePrice(boolean allowedToChangePrice) {
        this.allowedToChangePrice = allowedToChangePrice;
    }

    public double getOriginalBuyPrice() {
        return originalBuyPrice;
    }

    public void setOriginalBuyPrice(double originalBuyPrice) {
        this.originalBuyPrice = originalBuyPrice;
    }

    public double getOriginalSellPrice() {
        return originalSellPrice;
    }

    public void setOriginalSellPrice(double originalSellPrice) {
        this.originalSellPrice = originalSellPrice;
    }

    public ShopItem getShopItem() {
        return shopItem;
    }

    public void setShopItem(ShopItem shopItem) {
        this.shopItem = shopItem;
    }

    public double getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(double amountSold) {
        this.amountSold = amountSold;
    }

    public double getAmountBought() {
        return amountBought;
    }

    public void setAmountBought(double amountBought) {
        this.amountBought = amountBought;
    }
}
