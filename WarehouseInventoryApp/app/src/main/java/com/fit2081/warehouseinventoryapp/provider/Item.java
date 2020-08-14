package com.fit2081.warehouseinventoryapp.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {

    public static final String TABLE_NAME = "items";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "itemId")
    private int itemID;

    @ColumnInfo(name = "itemName")
    private String itemNameText;

    @ColumnInfo(name = "itemQuantity")
    private int quantityNum;

    @ColumnInfo(name = "itemCost")
    private float costNum;

    @ColumnInfo(name = "itemDescription")
    private String descriptionText;

    @ColumnInfo(name = "itemFrozen")
    private boolean toggleValue;

    public Item(String itemNameText, int quantityNum, float costNum, String descriptionText,
                boolean toggleValue) {
        this.itemNameText = itemNameText;
        this.quantityNum = quantityNum;
        this.costNum = costNum;
        this.descriptionText = descriptionText;
        this.toggleValue = toggleValue;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemNameText() {
        return itemNameText;
    }

    public int getQuantityNum() {
        return quantityNum;
    }

    public float getCostNum() {
        return costNum;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public boolean isToggleValue() {
        return toggleValue;
    }

    public void setItemID(@NonNull int itemID) {
        this.itemID = itemID;
    }

    public void setItemNameText(String itemNameText) {
        this.itemNameText = itemNameText;
    }

    public void setQuantityNum(int quantityNum) {
        this.quantityNum = quantityNum;
    }

    public void setCostNum(float costNum) {
        this.costNum = costNum;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public void setToggleValue(boolean toggleValue) {
        this.toggleValue = toggleValue;
    }
}
