package spiderbiggen.shoppingcart.datamanagement;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import spiderbiggen.shoppingcart.dialogcreators.ItemDialog;

/**
 * Created by Stefan Breetveld on 8-3-2016 as Shopping Cart.
 * Part of Shopping Cart.
 */
public class Item extends RealmObject {

    public static final String ITEM_ID = "itemId";
    public static final String ITEM_NAME = "itemName";
    public static final String AMOUNT = "amount";
    public static final String NEEDED_NOW = "neededNow";
    public static final String AREA = "area";
    public static final String STORE_ID = "storeId";
    private static final String TAG = ItemDialog.class.getSimpleName();
    private static RealmManager realmManager = RealmManager.getInstance();

    @PrimaryKey
    private long itemId;

    private String itemName;
    private int amount;
    private boolean neededNow;
    private int area;
    private long storeId;

    public Item() {
    }

    public Item(long id, String itemName, int amount, boolean neededNow, int area, long storeId) {
        this.itemId = id;
        this.itemName = itemName;
        this.amount = amount;
        this.neededNow = neededNow;
        this.area = area;
        this.storeId = storeId;
    }

    public Item(String itemName, int amount, boolean neededNow, int area, long storeId) {
        this.itemId = realmManager.getRealm().where(Item.class).count();
        this.itemName = itemName;
        this.amount = amount;
        this.neededNow = neededNow;
        this.area = area;
        this.storeId = storeId;
    }

    public Item(String itemName, int amount, boolean neededNow, int area, String storeName) {
        this(itemName, amount, neededNow, area, realmManager.getRealm().where(Store.class).equalTo("storeName", storeName).findFirst().getStoreId());
    }

    public static void copyFrom(final Item from, final Item to) {
        realmManager.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                to.setItemId(from.getItemId());
                to.setItemName(from.getItemName());
                to.setAmount(from.getAmount());
                to.setNeededNow(from.isNeededNow());
                to.setArea(from.getArea());
                to.setStoreId(from.getStoreId());
            }
        });
    }

    public static void setNeededNow(final Item item, final boolean neededNow) {
        realmManager.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setNeededNow(neededNow);
            }
        });
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
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

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

}
