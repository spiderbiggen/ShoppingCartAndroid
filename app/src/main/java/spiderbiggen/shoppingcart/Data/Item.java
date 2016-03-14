package spiderbiggen.shoppingcart.Data;

import android.support.annotation.NonNull;

import java.text.MessageFormat;

/**
 * Created by Stefan Breetveld on 8-3-2016 as Shopping Cart.
 * Part of Shopping Cart.
 */
public class Item implements Comparable<Item> {

    private int id = -1;
    private int area = 0;
    private String itemName = "";
    private int amount = 0;
    private boolean neededNow = false;
    private int storeId = -1;

    /**
     * Create a copy of the instance
     * @param item item to copy from
     */
    private Item(Item item){
        copyFrom(item);
    }

    public Item() {
    }

    /**
     * Create a new instance of an item
     * @param id the id of this object in the database
     * @param itemName name of the article
     * @param amount the amount needed
     * @param neededNow if it's needed this time
     * @param area priority
     * @param storeId the name of the store
     */
    public Item(int id, String itemName, int amount, boolean neededNow, int area, int storeId){
        this.id = id;
        this.itemName = itemName;
        this.amount = amount;
        this.neededNow = neededNow;
        this.area = area;
        this.storeId = storeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int newAmount) {
        this.amount = newAmount;
    }

    public void setNeededNow(boolean neededNow) {
        this.neededNow = neededNow;
    }

    public boolean isNeeded(){
        return neededNow;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }


    public void copyFrom(Item item) {
        this.itemName = item.itemName;
        this.amount = item.amount;
        this.neededNow = item.neededNow;
        this.area = item.area;
        this.storeId = item.storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (area != item.area) return false;
        if (amount != item.amount) return false;
        if (neededNow != item.neededNow) return false;
        if (!itemName.equals(item.itemName)) return false;
        return storeId == item.storeId;

    }

    @Override
    public int hashCode() {
        int result = area;
        result = 31 * result + itemName.hashCode();
        result = 31 * result + amount;
        result = 31 * result + (neededNow ? 1 : 0);
        result = 31 * result + storeId;
        return result;
    }

    @Override
    public int compareTo(@NonNull Item another) {
        int res = area - another.area;
        return res != 0 ? res : getItemName().compareTo(another.getItemName());
    }

    @Override
    public String toString() {
        return MessageFormat.format("Item{name='{0}', amount={1}, area={2}}", itemName, amount, area);
    }

    public Item copy() {
        Item item = new Item(this);
        return null;
    }
}
