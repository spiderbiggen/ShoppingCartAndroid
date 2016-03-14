package spiderbiggen.shoppingcart.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import spiderbiggen.shoppingcart.Data.ItemReaderHelper.ItemEntry;
import spiderbiggen.shoppingcart.Data.ItemReaderHelper.StoreEntry;

/**
 * Created by Stefan Breetveld on 14-3-2016.
 * Part of Shopping Cart.
 */
public class ItemProvider extends ContentProvider {

    private ItemReaderHelper dbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI("spiderbiggen.shoppingcart", "store_table", 1);
        sUriMatcher.addURI("spiderbiggen.shoppingcart", "store_table/#", 2);
        sUriMatcher.addURI("spiderbiggen.shoppingcart", "item_table", 3);
        sUriMatcher.addURI("spiderbiggen.shoppingcart", "item_table/#", 4);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ItemReaderHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        Cursor c;
        switch(sUriMatcher.match(uri)){
            case 1:
                c = db.query(StoreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case 2:
                c = db.query(StoreEntry.TABLE_NAME, projection, selection + " AND " + StoreEntry._ID + " = " + uri.getLastPathSegment() , selectionArgs, null, null, sortOrder);
                break;
            case 3:
                c = db.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case 4:
                c = db.query(ItemEntry.TABLE_NAME, projection, selection + " AND " + ItemEntry._ID + " = " + uri.getLastPathSegment() , selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        String tableName;
        switch(sUriMatcher.match(uri)){
            case 1 | 2:
                tableName = StoreEntry.TABLE_NAME;
                break;
            case 3 | 4:
                tableName = ItemEntry.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException();
        }
        long row = dbHelper.getWritableDatabase().insert(tableName, null, values);
        return Uri.withAppendedPath(uri, "/" + row);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case 1:
                return db.delete(StoreEntry.TABLE_NAME, selection, selectionArgs);
            case 2:
                return db.delete(StoreEntry.TABLE_NAME, selection + " AND " + StoreEntry._ID + " = " + uri.getLastPathSegment() , selectionArgs);
            case 3:
                return db.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
            case 4:
                return db.delete(ItemEntry.TABLE_NAME, selection + " AND " + ItemEntry._ID + " = " + uri.getLastPathSegment() , selectionArgs);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db =  dbHelper.getWritableDatabase();
        switch(sUriMatcher.match(uri)){
            case 1:
                return db.update(StoreEntry.TABLE_NAME, values, selection, selectionArgs);
            case 2:
                return db.update(StoreEntry.TABLE_NAME, values, selection + " AND " + StoreEntry._ID + " = " + uri.getLastPathSegment(), selectionArgs);
            case 3:
                return db.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
            case 4:
                return db.update(ItemEntry.TABLE_NAME, values, selection + " AND " + ItemEntry._ID + " = " + uri.getLastPathSegment(), selectionArgs);
            default:
                throw new IllegalArgumentException();
        }
    }
}
