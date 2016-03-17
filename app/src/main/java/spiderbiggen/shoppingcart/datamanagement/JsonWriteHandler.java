package spiderbiggen.shoppingcart.datamanagement;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.JsonWriter;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;

import spiderbiggen.shoppingcart.dialogcreators.ItemDialog;

/**
 * Created by Stefan Breetveld on 15-3-2016.
 * Part of ShoppingCart.
 */
public class JsonWriteHandler {

    private static final String TAG = ItemDialog.class.getSimpleName();

    public static boolean writeStores(List<Store> stores, Context context) {
        long start = Calendar.getInstance().getTimeInMillis();
        File[] extFiles = ContextCompat.getExternalFilesDirs(context, null);
        try {
            if (!isExternalStorageWritable())
                throw new IOException("External Storage not accessible");
            FileOutputStream storeStream = new FileOutputStream(new File(extFiles[0], "stores.json"));
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(storeStream));
            writer.beginArray();
            for (Store store : stores) {
                writer.beginObject();
                writer.name(Store.STORE_ID).value(store.getStoreId());
                writer.name(Store.STORE_NAME).value(store.getStoreName());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
            long elapsed = Calendar.getInstance().getTimeInMillis() - start;
            Log.d(TAG, "writeItems: wrote store array in " + elapsed + "ms");
        } catch (IOException e) {
            Log.e(TAG, "writeItems: Failed to write stores", e);
            return false;
        }
        return true;
    }

    public static boolean writeItems(List<Item> items, Context context) {
        long start = Calendar.getInstance().getTimeInMillis();
        File[] extFiles = ContextCompat.getExternalFilesDirs(context, null);
        try {
            if (!isExternalStorageWritable())
                throw new IOException("External Storage not accessible");
            FileOutputStream itemStream = new FileOutputStream(new File(extFiles[0], "items.json"));
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(itemStream));
            writer.beginArray();
            for (Item item : items) {
                writer.beginObject();
                writer.name(Item.ITEM_ID).value(item.getItemId());
                writer.name(Item.ITEM_NAME).value(item.getItemName());
                writer.name(Item.AMOUNT).value(item.getAmount());
                writer.name(Item.AREA).value(item.getArea());
                writer.name(Item.NEEDED_NOW).value(item.isNeededNow());
                writer.name(Item.STORE_ID).value(item.getStoreId());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
            long elapsed = Calendar.getInstance().getTimeInMillis() - start;
            Log.d(TAG, "writeItems: wrote item array in " + elapsed + "ms");
        } catch (IOException e) {
            Log.e(TAG, "writeItems: Failed to write items", e);
            return false;
        }
        return true;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


}
