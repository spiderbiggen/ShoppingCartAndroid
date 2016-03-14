package spiderbiggen.shoppingcart.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Stefan Breetveld on 13-3-2016.
 * Part of Shopping Cart.
 */
public class ItemReaderHelper extends SQLiteOpenHelper {

    private static final String TAG = "ItemReaderHelper";

    private static final String TEXT_TYPE = " TEXT";
    private static final String NUM_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_STORE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + StoreEntry.TABLE_NAME + " (" +
                    StoreEntry._ID + " INTEGER PRIMARY KEY," +
                    StoreEntry.COLUMN_NAME_STORE_NAME + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_ITEM_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry._ID + " INTEGER PRIMARY KEY," +
                    ItemEntry.COLUMN_NAME_ITEM_NAME + TEXT_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ITEM_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ITEM_NEEDED + NUM_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ITEM_AREA + NUM_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ITEM_STORE_ID + NUM_TYPE +
                    " )";


    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;
    private static final String SQL_DELETE_STORES = "DROP TABLE IF EXISTS " + StoreEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "items.db";
    public boolean exists = false;

    public ItemReaderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ITEM_ENTRIES);
        db.execSQL(SQL_CREATE_STORE_ENTRIES);
        exists = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: " + oldVersion + " " + newVersion);
        if(newVersion > oldVersion) {
            clear(db);
        }
    }


    public void clear(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_STORES);
        onCreate(db);
    }

    public static abstract class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_ITEM_NAME = "itemname";
        public static final String COLUMN_NAME_ITEM_AMOUNT = "amount";
        public static final String COLUMN_NAME_ITEM_NEEDED = "isneeded";
        public static final String COLUMN_NAME_ITEM_STORE_ID = "storeid";
        public static final String COLUMN_NAME_ITEM_AREA = "area";
    }

    public static abstract class StoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "stores";
        public static final String COLUMN_NAME_STORE_NAME = "storename";
    }
}
