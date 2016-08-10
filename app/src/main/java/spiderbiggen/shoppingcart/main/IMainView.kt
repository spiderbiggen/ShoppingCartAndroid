package spiderbiggen.shoppingcart.main

import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore

/**
 * Defines all basic interactions between applied to the view.
 *
 * @author Stefan Breetveld
 */
interface IMainView {

    /**
     * Show progressbar in the view.
     */
    fun showProgress()

    /**
     * Hide progressbar in the view.
     */
    fun hideProgress()

    /**
     * Load a new [MutableList] of items.
     *
     * @param items The new [Items][IItem] to be shown
     */
    fun setItems(items: MutableList<IItem>)

    /**
     * Load a new [MutableList] of stores.
     *
     * @param stores The new [Stores][IStore] to be shown.
     */
    fun setStores(stores: MutableList<IStore>)

    /**
     * Show a toast.
     *
     * @param resID A resource ID eg. R.String.text
     */
    fun showToast(resID: Int)

    /**
     * Show a toast.
     *
     * @param text A [String] Containing the text to be displayed.
     */
    fun showToast(text: String)
}