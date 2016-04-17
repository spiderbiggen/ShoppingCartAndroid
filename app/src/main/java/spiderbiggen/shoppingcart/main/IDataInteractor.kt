package spiderbiggen.shoppingcart.main

import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore

/**
 * Created by Stefan Breetveld on 17-4-2016.
 * Part of ShoppingBasket.
 */
interface IDataInteractor {
    interface OnFinishedListener {
        fun onFinishedItemLoad(items: MutableList<IItem>)
        fun onFinishedStoreLoad(stores: MutableList<IStore>)
    }

    fun findItems(listener: OnFinishedListener, storeId: Int)
    fun findStores(listener: OnFinishedListener)
}