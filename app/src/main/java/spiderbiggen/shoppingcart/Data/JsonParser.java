package spiderbiggen.shoppingcart.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.JsonReader;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spiderbiggen.shoppingcart.Data.ItemReaderHelper.ItemEntry;
import spiderbiggen.shoppingcart.Data.ItemReaderHelper.StoreEntry;

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
public class JsonParser {

    private static final String TAG = "JsonParser";

    public static void readJsonStream(Context in, ItemReaderHelper dbHelper) throws IOException {
        long start = System.currentTimeMillis();

        InputStream storeFile = in.getAssets().open("stores.json");
        InputStream itemFile = in.getAssets().open("items.json");

        JsonReader storeReader = new JsonReader(new InputStreamReader(storeFile, "UTF-8"));
        readStores(storeReader, dbHelper);
        storeReader.close();
        JsonReader itemReader = new JsonReader(new InputStreamReader(itemFile, "UTF-8"));
        readItemArray(itemReader, dbHelper);
        itemReader.close();

        long elapsed = System.currentTimeMillis() - start;
        Log.d(TAG, "readJsonStream: time elapsed: " + elapsed);
    }

    @NotNull
    private static HashMap<Integer, String> readStores(JsonReader reader, ItemReaderHelper dbHelper) throws IOException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO " + StoreEntry.TABLE_NAME + " VALUES ( ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
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

//            ContentValues values = new ContentValues();
//            values.put(StoreEntry._ID, key);
//            values.put(StoreEntry.COLUMN_NAME_STORE_NAME, storeName);
//            db.insert(StoreEntry.TABLE_NAME, null, values);

            statement.clearBindings();
            statement.bindLong(1, key);
            statement.bindString(2, storeName);
            statement.execute();

            reader.endObject();
        }
        reader.endArray();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return stores;
    }

    private static void readItemArray(JsonReader reader, ItemReaderHelper dbHelper) throws IOException {
        int i = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO " + ItemEntry.TABLE_NAME + " VALUES ( ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        reader.beginArray();
        while (reader.hasNext()) {
            String itemName = "";
            int area = 0;
            int amount = 0;
            boolean needed = false;
            int store = -1;
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
                        store = reader.nextInt();
                        break;
                }
            }
            statement.clearBindings();
            statement.bindLong(1, i);
            statement.bindString(2, itemName);
            statement.bindLong(3, amount);
            statement.bindLong(4, (needed) ? 1 : 0);
            statement.bindLong(5, area);
            statement.bindLong(6, store);
            statement.execute();
//            ContentValues values = new ContentValues();
//            values.put(ItemEntry.COLUMN_NAME_ITEM_NAME, itemName);
//            values.put(ItemEntry.COLUMN_NAME_ITEM_AMOUNT, amount);
//            values.put(ItemEntry.COLUMN_NAME_ITEM_NEEDED, (needed) ? 1 : 0);
//            values.put(ItemEntry.COLUMN_NAME_ITEM_AREA, area);
//            values.put(ItemEntry.COLUMN_NAME_ITEM_STORE_ID, store);
            reader.endObject();
            i++;
        }
        reader.endArray();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
}
