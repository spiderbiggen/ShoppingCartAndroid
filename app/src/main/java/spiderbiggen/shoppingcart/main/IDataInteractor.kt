package spiderbiggen.shoppingcart.main

import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore

/**
 * Defines all interactions that load data.
 *
 * @author Stefan Breetveld
 */
interface IDataInteractor {

    /**
     * Defines a listener finished loading of data.
     */
    interface OnFinishedListener {

        /**
         * Called when a [findItems] is done.
         *
         * @param items New list of items
         */
        fun onFinishedItemLoad(items: MutableList<IItem>)

        /**
         * Called when a [findStores] is done.
         *
         * @param stores New list of stores
         */
        fun onFinishedStoreLoad(stores: MutableList<IStore>)
    }

    /**
     * Find all items that correspond with the given [storeId]
     *
     * @param listener Instance of [OnFinishedListener] used to send the result.
     * @param storeId [Int] defining the given store.
     */
    fun findItems(listener: OnFinishedListener, storeId: Int)

    /**
     * Find all [Stores][IStore]
     *
     * @param listener Instance of [OnFinishedListener] used to send the result.
     */
    fun findStores(listener: OnFinishedListener)
}