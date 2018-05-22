package org.alexdev.kepler.game.catalogue;

import org.alexdev.kepler.game.item.ItemDefinition;
import org.alexdev.kepler.game.item.ItemManager;

public class CatalogueItem {
    private String saleCode;
    private int pageId;
    private int orderId;
    private int price;
    private int definitionId;
    private int itemSpecialId;
    private String packageName;
    private String packageDescription;
    private boolean isPackage;

    public CatalogueItem(String saleCode, int pageId, int orderId, int price, int definitionId, int itemSpecialId, String packageName, String packageDescription, boolean isPackage) {
        this.saleCode = saleCode;
        this.pageId = pageId;
        this.orderId = orderId;
        this.price = price;
        this.definitionId = definitionId;
        this.itemSpecialId = itemSpecialId;
        this.packageName = packageName;
        this.packageDescription = packageDescription;
        this.isPackage = isPackage;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public int getPageId() {
        return pageId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getPrice() {
        return price;
    }

    public ItemDefinition getDefinition() {
        return ItemManager.getInstance().getDefinition(this.definitionId);
    }

    public int getItemSpecialId() {
        return itemSpecialId;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public boolean isPackage() {
        return isPackage;
    }
}
