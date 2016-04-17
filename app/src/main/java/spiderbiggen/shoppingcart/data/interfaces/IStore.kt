package spiderbiggen.shoppingcart.data.interfaces

/**
 * Created by Stefan Breetveld on 31-3-2016.
 * Part of ShoppingBasket.
 */
interface IStore : Comparable<IStore> {

    val id: Int
    var name: String
    val itemList:MutableList<IItem>

}