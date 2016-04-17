package spiderbiggen.shoppingcart.data.interfaces

/**
 * Created by Stefan Breetveld on 2-4-2016.
 * Part of ShoppingBasket.
 */
interface IItem {
    val id: Int
    var name: String
    var amount: String
    var required: Boolean
}