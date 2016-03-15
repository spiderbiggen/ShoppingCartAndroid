package spiderbiggen.shoppingcart.Data;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import spiderbiggen.shoppingcart.R;

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
public class RealmManager {

    private static final String TAG = "RealmManager";

    private static RealmManager instance = new RealmManager();
    private Context context;
    private RealmConfiguration realmConfig;
    private Realm realm;

    private RealmManager() {
    }

    public static RealmManager getInstance() {
        return instance;
    }

    public void setContext(final Context context) {
        this.context = context;
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.

        realmConfig = new RealmConfiguration.Builder(context).build();
        //Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        try {
            realm = Realm.getInstance(realmConfig);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfig);
                //Realm file has been deleted.
                realm = Realm.getInstance(realmConfig);
            } catch (Exception ex) {
                throw ex;
                //No Realm file to remove.
            }
        }

        if (realm.isEmpty()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        realm.createAllFromJson(Store.class, context.getAssets().open("stores.json"));
                        realm.createAllFromJson(Item.class, context.getAssets().open("items.json"));
                    } catch (IOException e) {
                        Log.e(TAG, "setContext: failed to initialize realm objects from JSON", e);
                    }

                    Store store = new Store(-1, context.getString(R.string.leftovers));
                    realm.copyToRealm(store);
                }
            });

        }
    }


    public List<Item> getItems(int key) {
        if (key == -1) {
            return realm.where(Item.class).equalTo("neededNow", true).findAll();
        }
        return realm.where(Item.class).equalTo("storeId", key).findAllSorted("area");
    }

    public Realm getRealm() {
        return realm;
    }

    public void remove(final Item item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.removeFromRealm();
            }
        });
    }

    public void add(final Item item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(item);
            }
        });
    }

    public void reloadRealm() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.deleteAll();
                    realm.createAllFromJson(Store.class, context.getAssets().open("stores.json"));
                    realm.createAllFromJson(Item.class, context.getAssets().open("items.json"));
                } catch (IOException e) {
                    Log.e(TAG, "setContext: failed to initialize realm objects from JSON", e);
                }

                Store store = new Store(-1, context.getString(R.string.leftovers));
                realm.copyToRealm(store);
            }
        });
    }

    public List<Store> getStores() {
        return realm.where(Store.class).findAllSorted("storeName");
    }

    public List<Store> getStoresDialog() {
        return realm.where(Store.class).notEqualTo(Store.STORE_ID, -1).findAllSorted("storeName");
    }

    public Item getCopy(Item item) {
        return realm.copyFromRealm(item);
    }

    public void setItemNeeded(final Item item, final boolean bool) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setNeededNow(bool);
            }
        });
    }
}
