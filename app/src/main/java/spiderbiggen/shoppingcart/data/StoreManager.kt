package spiderbiggen.shoppingcart.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import spiderbiggen.shoppingcart.BuildConfig
import spiderbiggen.shoppingcart.data.interfaces.IStore
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import java.util.concurrent.locks.ReentrantLock


/**
 * Created by Stefan Breetveld on 31-3-2016.
 * Part of ShoppingBasket.
 */
object StoreManager : Observable() {

    val leftoverStore = Store(0, "\u2063Leftovers")
    val storeMap: MutableMap<Int, IStore> = mutableMapOf(kotlin.Pair(leftoverStore.id, leftoverStore))
    val fileName = "stores.json"
    val lock = ReentrantLock()
    val fillList = false

    private var storeListCached: MutableList<IStore> = arrayListOf()

    fun getStoreList(): MutableList<IStore> {
        if (storeListCached.count() != storeMap.count()) {
            val list: MutableList<IStore> = storeMap.values.toMutableList()
            list.sort()
            storeListCached = list
        }
        return storeListCached
    }

    fun getIdList(): List<Int> = storeMap.keys.toList()

    fun setStores(stores: List<Store>) {
        storeMap.clear()
        stores.forEach{store -> storeMap.put(store.id, store)}
        setChanged()
        notifyObservers()2
    }

    init {
        if (BuildConfig.DEBUG && fillList) {
            Log.i("StoreManager", "Constructor: add demo stores")
            val products = listOf("Kaas", "Oreo", "Pizza", "Appel", "Pudding", "Chips", "Salami", "Banaan", "Kroket", "Biefstuk", "Aardappels")
            val rand = Random()

            for(i in 0..99) {
                val st = Store(String.format("Aldi %02d", i))
                for(j in 1..99) {
                    st.itemList.add(Item(String.format("%s %02d", products[rand.nextInt(products.size)], j), st.toString()))
                }
                this.add(st) //TODO Remove this filler shit
            }

        }
    }

    fun add(st: IStore) {
        storeMap.put(st.id, st)
        setChanged()
        notifyObservers()
    }

    fun remove(st: Store) {
        storeMap.remove(st.id)
        setChanged()
        notifyObservers()
    }

    fun saveData(context: Context) {
        val storeFile = context.openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE)
        val writer = OutputStreamWriter(storeFile, "UTF-8")
        Log.d("StoreManager", "saveData: ${Gson().toJson(getStoreList())}" )
        Gson().toJson(getStoreList(), writer)
        writer.close()
    }

    fun readData(context: Context) {
        val storeFile = context.openFileInput(fileName)
        val reader = InputStreamReader(storeFile, "UTF-8")
        val type = object : TypeToken<MutableList<Store>>() {}.type
        val gson : MutableList<Store> = Gson().fromJson(reader, type)
        reader.close()
        Log.d("StoreManager", "readData: $gson" )
        setStores(gson)

    }

}