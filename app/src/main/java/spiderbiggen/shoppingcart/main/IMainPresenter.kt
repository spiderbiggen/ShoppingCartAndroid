package spiderbiggen.shoppingcart.main

import android.content.Context

/**
 * Defines all basic interactions applied to the data from the view.
 *
 * @author Stefan Breetveld
 */
interface IMainPresenter {

    /**
     * Call when activity is resumed.
     */
    fun onResume()

    /**
     * Call when activity is destroyed.
     */
    fun onDestroy()

    /**
     * Save all data.
     *
     * @param context The [Context] being called from.
     */
    fun saveData(context: Context)

    /**
     * Load all data.
     *
     * @param context The [Context] being called from.
     */
    fun loadData(context: Context)

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