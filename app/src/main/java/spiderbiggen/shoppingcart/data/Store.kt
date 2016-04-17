package spiderbiggen.shoppingcart.data

import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore

/**
 * Created by Stefan Breetveld on 31-3-2016.
 * Part of ShoppingBasket.
 */
data class Store(override val id: Int, override var name: String, override val itemList: MutableList<IItem>) : IStore {

    companion object {
        var maxId: Int = 0
    }


    init {
        if(id > maxId) maxId = id
    }
    constructor(id: Int, storeName: String): this(id, storeName, mutableListOf())
    constructor(storeName: String) : this(++maxId, storeName)

    override fun compareTo(other: IStore): Int {
        return this.name.compareTo(other.name)
    }

    override fun toString() :String {
        return this.name
    }
}