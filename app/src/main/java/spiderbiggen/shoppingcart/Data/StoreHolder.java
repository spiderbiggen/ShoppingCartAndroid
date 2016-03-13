package spiderbiggen.shoppingcart.Data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import spiderbiggen.shoppingcart.R;
import spiderbiggen.shoppingcart.Util.JsonParser;

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

    public static StoreHolder getInstance() {
        return instance;
    }

    private StoreHolder(){}

    public void setContext(Context context) {
        this.context = context;
        this.setStores(this.readStores());
    }

    @NotNull
    public HashMap<String, List<Item>> getStores() {
        HashMap<String, List<Item>> tempStores;
        if (stores == null || stores.isEmpty()) {
            tempStores = readStores();
            stores = tempStores;
        }
        else tempStores = stores;

        List<Item> leftovers = new ArrayList<>();
        for(String s : stores.keySet()) {
            leftovers.addAll(stores.get(s));
            for (Item item : stores.get(s)) {
                if (!item.isNeeded()) leftovers.remove(item);
            }
        }
        tempStores.put(context.getResources().getString(R.string.leftovers), leftovers);

        return tempStores;
    }

    public List<Item> getItems(String key) {
        if (stores == null || stores.isEmpty() || key.equals(context.getString(R.string.leftovers)) || !stores.containsKey(key)) stores = getStores();
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

    public void setStores(HashMap<String, List<Item>> map) {
        this.stores = map;
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
