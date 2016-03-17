package spiderbiggen.shoppingcart.datamanagement;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.exceptions.RealmMigrationNeededException;
import spiderbiggen.shoppingcart.R;
import spiderbiggen.shoppingcart.dialogcreators.ItemDialog;

import static spiderbiggen.shoppingcart.datamanagement.JsonWriteHandler.writeItems;
import static spiderbiggen.shoppingcart.datamanagement.JsonWriteHandler.writeStores;

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
public class RealmManager {

    private static final String TAG = ItemDialog.class.getSimpleName();

    private static RealmManager instance = new RealmManager();
    private Context context;
    private Realm realm;

    private RealmManager() {
    }

    public static RealmManager getInstance() {
        return instance;
    }

    public static RealmManager getInstance(Context context) {
        instance.setContext(context);
        return instance;
    }

    public void setContext(final Context context) {
        this.context = context;
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        //Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        Realm.setDefaultConfiguration(realmConfig);
        try {
            realm = Realm.getInstance(realmConfig);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfig);
                //Realm file has been deleted.
                realm = Realm.getInstance(realmConfig);
            } catch (Exception ex) {
                Log.e(TAG, "setContext: Failed to remove Realm", ex);
                //No Realm file to remove.
            }
        }

        if (realm.isEmpty()) reloadRealm();
    }


    public List<Item> getItems(int key) {
        if (key == -1) {
            return realm.where(Item.class).equalTo("neededNow", true).notEqualTo(Item.ITEM_ID, -1).findAll();
        }
        return realm.where(Item.class).equalTo("storeId", key).findAllSorted("area");
    }

    public Realm getRealm() {
        return realm;
    }

    public void remove(final RealmObject rObj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                rObj.removeFromRealm();
            }
        });
    }

    public void add(final RealmObject rObj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(rObj);
            }
        });
    }

    public void reloadRealm() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    if (isExternalStorageReadable() && hasExternalStoragePrivateFile("stores.json") && hasExternalStoragePrivateFile("items.json")) {
                        realm.deleteAll();
                        File[] extFiles = ContextCompat.getExternalFilesDirs(context, null);

                        FileInputStream stores = new FileInputStream(new File(extFiles[0], "stores.json"));
                        FileInputStream items = new FileInputStream(new File(extFiles[0], "items.json"));

                        realm.createAllFromJson(Store.class, stores);
                        realm.createAllFromJson(Item.class, items);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "setContext: failed to initialize realm objects from JSON", e);
                }

                Store store = new Store(-1, context.getString(R.string.leftovers));
                realm.copyToRealm(store);
                Item item = new Item(-1, "", -1, false, -1, -1);
                realm.copyToRealm(item);
            }
        });
    }

    public Store getStore(int key) {
        return realm.where(Store.class).equalTo(Store.STORE_ID, key).findFirst();
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

    public boolean exportToFile() {
        List<Store> stores = realm.where(Store.class).notEqualTo(Store.STORE_ID, -1).findAll();
        List<Item> items = realm.where(Item.class).notEqualTo(Item.ITEM_ID, -1).findAll();
        return writeItems(items, context) && writeStores(stores, context);
    }

    public void clearRealm() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Item.class).notEqualTo(Item.ITEM_ID, -1).findAll().clear();
                realm.where(Store.class).notEqualTo(Store.STORE_ID, -1).findAll().clear();
            }
        });
    }

    boolean hasExternalStoragePrivateFile(String s) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(ContextCompat.getExternalFilesDirs(context, null)[0], s);
        return file.exists();
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public Item getItem(long key) {
        return realm.where(Item.class).equalTo(Item.ITEM_ID, key).findFirst();
    }
}
