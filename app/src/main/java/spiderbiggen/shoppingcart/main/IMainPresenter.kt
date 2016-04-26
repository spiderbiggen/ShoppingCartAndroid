package spiderbiggen.shoppingcart.main

import android.content.Context

/**
 * Created by Stefan Breetveld on 17-4-2016.
 * Part of ShoppingBasket.
 */
interface IMainPresenter {
    fun onResume()
    fun onDestroy()
    open fun saveData(context: Context)
    open fun loadData(context: Context)
}