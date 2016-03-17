package spiderbiggen.shoppingcart.datamanagement;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Stefan Breetveld on 14-3-2016.
 * Part of ShoppingCart.
 */
public class Store extends RealmObject {

    public static final String STORE_ID = "storeId";
    public static final String STORE_NAME = "storeName";

    private static RealmManager realmManager = RealmManager.getInstance();

    @PrimaryKey
    private int storeId;

    private String storeName;

    public Store() {
    }

    public Store(int id, String string) {
        storeId = id;
        storeName = string;
    }

    public Store(String string) {
        storeId = realmManager.getRealm().where(Store.class).max(STORE_ID).intValue() + 1;
        storeName = string;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
