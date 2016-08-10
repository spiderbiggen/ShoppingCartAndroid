package spiderbiggen.shoppingcart.main

import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore

/**
 * Created by Stefan Breetveld on 17-4-2016.
 * Part of ShoppingBasket.
 */
interface IMainView {
    fun showProgress()
    fun hideProgress()
    fun setItems(items: MutableList<IItem>)
    fun setStores(stores: MutableList<IStore>)
    fun showToast(resID: Int)
    fun showToast(text: String)
}