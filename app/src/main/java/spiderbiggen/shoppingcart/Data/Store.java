package spiderbiggen.shoppingcart.Data;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Stefan Breetveld on 14-3-2016.
 * Part of ShoppingCart.
 */
public class Store extends RealmObject {

    public static final String STORE_ID = "storeId";
    public static final String STORE_NAME = "storeName";


    @PrimaryKey
    private int storeId;

    private String storeName;

    public Store() {
    }

    public Store(int i, String string) {
        storeId = i;
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
