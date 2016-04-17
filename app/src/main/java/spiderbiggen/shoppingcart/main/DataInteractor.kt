package spiderbiggen.shoppingcart.main

import android.os.Handler
import spiderbiggen.shoppingcart.data.ItemManager
import spiderbiggen.shoppingcart.data.StoreManager

/**
 * Created by Stefan Breetveld on 17-4-2016.
 * Part of ShoppingBasket.
 */
class DataInteractor : IDataInteractor {
    override fun findStores(listener: IDataInteractor.OnFinishedListener) {
        Handler().post({ listener.onFinishedStoreLoad(StoreManager.getStoreList()) })
    }

    override fun findItems(listener: IDataInteractor.OnFinishedListener, storeId: Int) {
        Handler().post({ listener.onFinishedItemLoad(ItemManager.getItemList(storeId)) })
    }

}
