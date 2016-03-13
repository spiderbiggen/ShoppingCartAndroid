package spiderbiggen.shoppingcart.Data;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import spiderbiggen.shoppingcart.R;

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
public class StoreHolder {

    private static final String LOG_TAG = "StoreHolder";

    private static StoreHolder instance = new StoreHolder();
    private Context context;
    private HashMap<String, List<Item>> stores;

    private List<String> keys;

    private StoreHolder() {
    }

    public static StoreHolder getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
        this.setStores(this.getStores());
    }

    @NotNull
    public HashMap<String, List<Item>> getStores() {
        if (stores == null || stores.isEmpty()) {
            stores = readStores();
        }
        stores.put(context.getString(R.string.leftovers), new ArrayList<Item>());
        return stores;
    }

    public void setStores(HashMap<String, List<Item>> map) {
        this.stores = map;
    }

    public List<Item> getItems(String key) {
        if (stores == null || stores.isEmpty() || !stores.containsKey(key)) stores = getStores();
        if (key.equals(context.getString(R.string.leftovers))) {
            List<Item> leftovers = new ArrayList<>();
            for (String s : stores.keySet()) {
                leftovers.addAll(stores.get(s));
                for (Item item : stores.get(s)) {
                    if (!item.isNeeded()) leftovers.remove(item);
                }
            }
            stores.put(context.getString(R.string.leftovers), leftovers);
        }
        if (!stores.containsKey(key)) return new ArrayList<>();
        return stores.get(key);
    }

    public List<String> getKeys() {
        if (this.keys != null) return this.keys;
        if (stores == null || stores.isEmpty()) getStores();
        assert stores != null;
        List<String> keys = new ArrayList<>(stores.keySet());
        Collections.sort(keys);
        this.keys = keys;
        return keys;
    }

    public HashMap<String, List<Item>> readStores(){
        try {
            return JsonParser.readJsonStream(context);
        } catch (Exception e) {
            Log.e(LOG_TAG, "readStores: failed to read json", e);
        }
        return new HashMap<>();
    }
}
