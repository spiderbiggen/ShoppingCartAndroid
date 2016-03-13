package spiderbiggen.shoppingcart.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.JsonReader;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spiderbiggen.shoppingcart.Data.Item;

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
public class JsonParser {

    @NotNull
    public static HashMap<String, List<Item>> readJsonStream(Context in) throws IOException {
        InputStream storeFile = in.getAssets().open("stores.json");
        InputStream itemFile = in.getAssets().open("items.json");

        JsonReader reader = new JsonReader(new InputStreamReader(storeFile, "UTF-8"));
        HashMap<Integer, String> stores = readStores(reader);reader.close();
        reader = new JsonReader(new InputStreamReader(itemFile, "UTF-8"));
        HashMap<String, List<Item>> items = readItemArray(reader, stores);
        reader.close();
        return items;
    }

    @NotNull
    private static HashMap<Integer, String> readStores(JsonReader reader) throws IOException {
        HashMap<Integer, String> stores = new HashMap<>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            String storeName = "";
            int key = -1;
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("name")) {
                    storeName = reader.nextString();
                } else if (name.equals("id")) {
                    key = reader.nextInt();
                }
            }
            stores.put(key, storeName);
            reader.endObject();
        }
        reader.endArray();
        return stores;
    }

    @NotNull
    private static HashMap<String, List<Item>> readItemArray(JsonReader reader, HashMap<Integer, String> stores) throws IOException {
        HashMap<String, List<Item>> items = new HashMap<>();
        for(int key : stores.keySet()){
            items.put(stores.get(key), new ArrayList<Item>());
        }
        reader.beginArray();
        while (reader.hasNext()) {
            String itemName = "";
            int area = 0;
            int amount = 0;
            boolean needed = false;
            String storeName = "";
            reader.beginObject();
            while(reader.hasNext()){
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        itemName = reader.nextString();
                        break;
                    case "amount":
                        amount = reader.nextInt();
                        break;
                    case "needed":
                        needed = reader.nextBoolean();
                        break;
                    case "area":
                        area = reader.nextInt();
                        break;
                    case "store id":
                        storeName = stores.get(reader.nextInt());
                        break;
                }
            }
            reader.endObject();
            items.get(storeName).add(new Item(itemName, amount, needed, area, storeName));
        }
        reader.endArray();
        return items;
    }

    private static Item readItem(JsonReader reader, HashMap<Integer, String> stores) throws IOException {
        String itemName = "";
        int area = 0;
        int amount = 0;
        boolean needed = false;
        String storeName = "";
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            switch (name) {
                case "name":
                    itemName = reader.nextString();
                    break;
                case "amount":
                    amount = reader.nextInt();
                    break;
                case "needed":
                    needed = reader.nextBoolean();
                    break;
                case "area":
                    area = reader.nextInt();
                    break;
                case "store id":
                    storeName = stores.get(reader.nextInt());
                    break;
            }
        }
        reader.endObject();
        return new Item(itemName, amount, needed, area, storeName);
    }
}
