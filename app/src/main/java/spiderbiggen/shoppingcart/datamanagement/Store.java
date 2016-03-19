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
    private long storeId;

    private String storeName;

    public Store() {
    }

    public Store(long id, String string) {
        storeId = id;
        storeName = string;
    }

    public Store(String string) {
        storeId = realmManager.getRealm().where(Store.class).count();
        storeName = string;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
