package spiderbiggen.shoppingcart.data

import android.content.Context
import android.util.Log
import spiderbiggen.shoppingcart.BuildConfig
import spiderbiggen.shoppingcart.data.interfaces.IStore
import java.util.*

/**
 * Created by Stefan Breetveld on 31-3-2016.
 * Part of ShoppingBasket.
 */
object StoreManager : Observable() {

    val leftoverStore = Store(0, "\u2063Leftovers")
    val storeMap: MutableMap<Int, IStore> = mutableMapOf(kotlin.Pair(leftoverStore.id, leftoverStore))

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

    init {
        if (BuildConfig.DEBUG) {
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

    fun readData(context: Context) {
        JsonParser.readJsonStream(context)
    }

}