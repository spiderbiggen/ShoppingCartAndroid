package spiderbiggen.shoppingcart.main

import android.content.Context

/**
 * Created by Stefan Breetveld on 17-4-2016.
 * Part of ShoppingBasket.
 */
interface IMainPresenter {
    fun onResume()
    fun onDestroy()
    fun saveData(context: Context)
    fun loadData(context: Context)
    fun showToast(resID: Int)
    fun showToast(text: String)
}