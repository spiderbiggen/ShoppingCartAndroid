package spiderbiggen.shoppingcart.data

import spiderbiggen.shoppingcart.data.interfaces.IItem

/**
 * Created by Stefan Breetveld on 2-4-2016.
 * Part of ShoppingBasket.
 */
class Item(override val id: Int, override var name: String, override var amount: String, override var required: Boolean) : IItem {

    companion object {
        var maxId: Int = 0 //Highest currently assigned ID.
    }

    init {
        if(id > maxId) maxId = id
    }

    constructor(name: String, amount: String) : this(++maxId, name, amount, true)
    constructor(name: String, amount: String, needed: Boolean) : this(++maxId, name, amount, needed)

}