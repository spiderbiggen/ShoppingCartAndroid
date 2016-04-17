package spiderbiggen.shoppingcart.main

import spiderbiggen.shoppingcart.data.ItemManager
import spiderbiggen.shoppingcart.data.StoreManager
import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore
import java.util.*

/**
 * Created by Stefan Breetveld on 17-4-2016.
 * Part of ShoppingBasket.
 */
class MainPresenter(var mainView: IMainView?) : IMainPresenter, Observer, IDataInteractor.OnFinishedListener {

    override fun onFinishedStoreLoad(stores: MutableList<IStore>) {
        mainView?.setStores(stores)
    }

    override fun onFinishedItemLoad(items: MutableList<IItem>) {
            mainView?.setItems(items)
            mainView?.hideProgress()
    }

    override fun onResume() {
        StoreManager.addObserver(this)
        ItemManager.addObserver(this)
    }

    override fun onDestroy() {
        StoreManager.deleteObserver(this)
        ItemManager.deleteObserver(this)
        mainView = null
    }

    override fun update(observable: Observable?, data: Any?) {
        when (observable) {
            StoreManager -> {
                mainView?.setStores(StoreManager.getStoreList())
            }
            ItemManager -> {
                mainView?.setItems(ItemManager.getItemList(data as Int))
            }
        }
    }
}