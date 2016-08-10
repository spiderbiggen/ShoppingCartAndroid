package spiderbiggen.shoppingcart.data

import spiderbiggen.shoppingcart.data.interfaces.IItem
import java.util.*

/**
 * Created by Stefan Breetveld on 17-4-2016.
 * Part of ShoppingBasket.
 */
object ItemManager : Observable() {

    var cachedItemList: Pair<Int, MutableList<IItem>> = Pair(-1, mutableListOf())
        private set

    fun getItemList(storeId: Int): MutableList<IItem> {
        if(cachedItemList.first != storeId) {
            if (storeId == StoreManager.leftoverStore.id) {
                val list: MutableList<IItem> = mutableListOf()
                for (store in StoreManager.getStoreList()) {
                    list.addAll(store.itemList.filter(fun(item) = item.required))
                }
                cachedItemList = Pair(storeId, list)
            } else {
                cachedItemList = Pair(storeId, StoreManager.storeMap[storeId]?.itemList as MutableList<IItem>)
            }
            setChanged()
            notifyObservers(cachedItemList.first)
        }
        return cachedItemList.second
    }
}