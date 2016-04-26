package spiderbiggen.shoppingcart.data

import android.content.Context
import android.util.JsonReader
import android.util.Log
import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
object JsonParser {

    private val TAG = "JsonParser"

    @Throws(IOException::class)
    fun readJsonStream(context: Context): HashMap<Int, IStore> {
        val start = System.currentTimeMillis()
        val storeFile = context.assets.open("stores.json")
        val reader = JsonReader(InputStreamReader(storeFile, "UTF-8"))
        val stores = readStores(reader)
        reader.close()
        reader.close()
        val elapsed = System.currentTimeMillis() - start
        Log.d(TAG, "readJsonStream: time elapsed: " + elapsed)
        return stores
    }

    @Throws(IOException::class)
    private fun readStores(reader: JsonReader): HashMap<Int, IStore> {
        val stores = HashMap<Int, IStore>()
        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()
            var storeName = ""
            var key = -1
            var itemList: MutableList<IItem>? = null
            while (reader.hasNext()) {
                val name = reader.nextName()
                when (name) {
                    "name" -> storeName = reader.nextString()
                    "id" -> key = reader.nextInt()
                    "items" -> itemList = readItemArray(reader)
                }
            }
            if (itemList == null) {
                stores.put(key, Store(key, storeName))
            } else {
                stores.put(key, Store(key, storeName, itemList))
            }
            reader.endObject()
        }
        reader.endArray()
        return stores
    }

    @Throws(IOException::class)
    private fun readItemArray(reader: JsonReader): MutableList<IItem> {
        val items = ArrayList<IItem>()
        reader.beginArray()
        while (reader.hasNext()) {
            var itemName = ""
            var amount = ""
            var needed = false
            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                when (name) {
                    "name" -> itemName = reader.nextString()
                    "amount" -> amount = reader.nextString()
                    "needed" -> needed = reader.nextBoolean()
                    else -> reader.nextString()
                }
            }
            reader.endObject()
            items.add(Item(itemName, amount, needed))
        }
        reader.endArray()
        return items
    }

}
