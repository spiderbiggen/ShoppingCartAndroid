package spiderbiggen.shoppingcart.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import spiderbiggen.shoppingcart.R;

import static spiderbiggen.shoppingcart.Data.ItemReaderHelper.*;

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
public class DataBaseManager {

    private static final String TAG = "DataBaseManager";

    private static DataBaseManager instance = new DataBaseManager();
    private Context context;
    private ItemReaderHelper dbHelper;


    private DataBaseManager() {
        //Empty
    }

    public static DataBaseManager getInstance(Context context) {
        instance.setContext(context);
        return getInstance();
    }

    public static DataBaseManager getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
        dbHelper = new ItemReaderHelper(context);
        if(!context.getDatabasePath(dbHelper.getDatabaseName()).exists())
            updateStores();
    }

    public Cursor getItems(String key) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { StoreEntry._ID };
        String[] whereArgs = { key };
        Cursor c = db.query(StoreEntry.TABLE_NAME, projection, StoreEntry.COLUMN_NAME_STORE_NAME + " = ?", whereArgs, null, null, null);
        c.moveToFirst();
        int intKey = c.getInt(c.getColumnIndex(StoreEntry._ID));
        c.close();
        return getItems(intKey);
    }

    public Cursor getItems(int key) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(ItemEntry.TABLE_NAME, null, ItemEntry.COLUMN_NAME_ITEM_STORE_ID + " = ?", new String[]{String.valueOf(key)}, null, null, null);
    }

    public List<Item> getItemsAsList(String key) {
        Cursor c;
        if (key.equals(context.getString(R.string.leftovers))) c = createLeftoversCursor();
        else c = getItems(key);

        c.moveToFirst();
        int colItemId = c.getColumnIndex(ItemEntry._ID);
        int colItemName = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_NAME);
        int colItemAmount = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_AMOUNT);
        int colItemNeeded = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_NEEDED);
        int colItemArea = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_AREA);
        int colItemStoreId = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_AREA);
        ArrayList<Item> mArrayList = new ArrayList<>();
        while(c.moveToNext()) {
            mArrayList.add(new Item(c.getInt(colItemId), c.getString(colItemName), c.getInt(colItemAmount), c.getInt(colItemNeeded) == 1, c.getInt(colItemArea), c.getInt(colItemStoreId) )); //add the item
        }
        c.close();
        return mArrayList;
    }

    private Cursor createLeftoversCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(ItemEntry.TABLE_NAME, null, ItemEntry.COLUMN_NAME_ITEM_NEEDED + " = ?", new String[]{String.valueOf(1)}, null, null, null);
    }

    public Item getItem(int key) {
        if (key < 0) return new Item();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(ItemEntry.TABLE_NAME, null, ItemEntry._ID + " = ?", new String[]{String.valueOf(key)}, null, null, null);
        c.moveToFirst();
        int colItemName = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_NAME);
        int colItemAmount = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_AMOUNT);
        int colItemNeeded = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_NEEDED);
        int colItemArea = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_AREA);
        int colItemStoreId = c.getColumnIndex(ItemEntry.COLUMN_NAME_ITEM_AREA);
        Item item = new Item(key, c.getString(colItemName), c.getInt(colItemAmount), c.getInt(colItemNeeded) == 1, c.getInt(colItemArea), c.getInt(colItemStoreId) );
        c.close();

        return item;
    }

    public String getStore(int key) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(StoreEntry.TABLE_NAME, new String[]{StoreEntry.COLUMN_NAME_STORE_NAME}, StoreEntry._ID + " = ?", new String[]{String.valueOf(key)}, null, null, null);
        c.moveToFirst();
        int colStoreName = c.getColumnIndex(StoreEntry.COLUMN_NAME_STORE_NAME);
        String store = c.getString(colStoreName);
        c.close();

        return store;
    }

    public int getStoreId(String store) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(StoreEntry.TABLE_NAME, new String[]{StoreEntry._ID}, StoreEntry.COLUMN_NAME_STORE_NAME + " = ?", new String[]{store}, null, null, null);
        c.moveToFirst();
        int colStoreId = c.getColumnIndex(StoreEntry._ID);
        int key = c.getInt(colStoreId);
        c.close();

        return key;
    }

    public ArrayList<String> getKeys() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                StoreEntry.COLUMN_NAME_STORE_NAME
        };

        Cursor cursor = db.query(true, StoreEntry.TABLE_NAME, projection, null, null, null, null, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<>();
        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            mArrayList.add(cursor.getString(cursor.getColumnIndex(ItemReaderHelper.StoreEntry.COLUMN_NAME_STORE_NAME))); //add the item
        }
        cursor.close();
        mArrayList.add(context.getString(R.string.leftovers));
        Collections.sort(mArrayList);
        return mArrayList;
    }

    public void updateStores(){
        try {
            dbHelper.clear(dbHelper.getWritableDatabase());
            JsonParser.readJsonStream(context, dbHelper);
        } catch (Exception e) {
            Log.e(TAG, "readStores: failed to read json", e);
        }
    }

    public void addItem(Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = itemToContentValues(item);
        db.insert(ItemEntry.TABLE_NAME, null, values);
    }

    public void removeItem(int itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = ItemEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };
        db.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void changeItem(int itemId, Item item) {
        if (itemId < 0) addItem(item);
        else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String selection = ItemEntry._ID + " = ?";
            String[] selectionArgs = { String.valueOf(itemId) };
            ContentValues values = itemToContentValues(item);
            db.update(ItemEntry.TABLE_NAME, values, selection,selectionArgs);
        }
    }

    private ContentValues itemToContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_NAME_ITEM_NAME, item.getItemName());
        values.put(ItemEntry.COLUMN_NAME_ITEM_AMOUNT, item.getAmount());
        values.put(ItemEntry.COLUMN_NAME_ITEM_NEEDED, (item.isNeeded()) ? 1 : 0);
        values.put(ItemEntry.COLUMN_NAME_ITEM_AREA, item.getArea());
        values.put(ItemEntry.COLUMN_NAME_ITEM_STORE_ID, item.getStoreId());
        return values;
    }
}
