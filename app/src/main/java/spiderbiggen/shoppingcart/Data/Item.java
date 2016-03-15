package spiderbiggen.shoppingcart.Data;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Stefan Breetveld on 8-3-2016 as Shopping Cart.
 * Part of Shopping Cart.
 */
public class Item extends RealmObject {

    private static RealmManager realmManager = RealmManager.getInstance();

    @PrimaryKey
    private long itemId;

    private String itemName;
    private int amount;
    private boolean neededNow;
    private int area;
    private int storeId;

    public Item() {
    }

    public Item(boolean bool) {

    }

    public Item(long id, String itemName, int amount, boolean neededNow, int area, int storeId) {
        itemId = id;
        this.itemName = itemName;
        this.amount = amount;
        this.neededNow = neededNow;
        this.area = area;
        this.storeId = storeId;
    }

    public Item(String itemName, int amount, boolean neededNow, int area, int storeId) {
        this(realmManager.getRealm().where(Item.class).count() + 1, itemName, amount, neededNow, area, storeId);
    }

    public Item(String itemName, int amount, boolean neededNow, int area, String storeName) {
        this(realmManager.getRealm().where(Item.class).count() + 1, itemName, amount, neededNow, area, realmManager.getRealm().where(Store.class).equalTo("storeName", storeName).findFirst().getStoreId());
    }

    public static void copyFrom(final Item from, final Item to) {
        realmManager.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                from.itemId = to.itemId;
                from.itemName = to.itemName;
                from.amount = to.amount;
                from.neededNow = to.neededNow;
                from.area = to.area;
                from.storeId = to.storeId;
            }
        });
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isNeededNow() {
        return neededNow;
    }

    public void setNeededNow(boolean neededNow) {
        this.neededNow = neededNow;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

}
