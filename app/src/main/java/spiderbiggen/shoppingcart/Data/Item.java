package spiderbiggen.shoppingcart.Data;

import android.support.annotation.NonNull;

import java.text.MessageFormat;

/**
 * Created by Stefan Breetveld on 8-3-2016 as Shopping Cart.
 * Part of Shopping Cart.
 */
public class Item implements Comparable<Item> {

    private int area;
    private String itemName;
    private int amount;
    private boolean neededNow;
    private String storeName;

    private boolean empty;

    public Item(Item item){
        copyFrom(item);
    }

    public Item() {
        empty = true;
    }

    public Item(String itemName, int amount, boolean neededNow, int area, String storeName){
        this.itemName = itemName;
        this.amount = amount;
        this.neededNow = neededNow;
        this.area = area;
        this.storeName = storeName;
        empty = false;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void copyFrom(Item item) {
        this.itemName = item.itemName;
        this.amount = item.amount;
        this.neededNow = item.neededNow;
        this.area = item.area;
        this.storeName = item.storeName;
        this.empty = item.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (area != item.area) return false;
        if (amount != item.amount) return false;
        if (neededNow != item.neededNow) return false;
        if (empty != item.empty) return false;
        if (!itemName.equals(item.itemName)) return false;
        return storeName.equals(item.storeName);

    }

    @Override
    public int hashCode() {
        int result = area;
        result = 31 * result + itemName.hashCode();
        result = 31 * result + amount;
        result = 31 * result + (neededNow ? 1 : 0);
        result = 31 * result + storeName.hashCode();
        result = 31 * result + (empty ? 1 : 0);
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
}
