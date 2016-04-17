package spiderbiggen.shoppingcart.data

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import spiderbiggen.shoppingcart.R
import spiderbiggen.shoppingcart.data.interfaces.IItem


/**
 * Created by Stefan Breetveld on 15-4-2016.
 * Part of ShoppingBasket.
 */
class ItemAdapter(val items : MutableList<IItem>, val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tv.text = String.format("%s %s", item.name, item.amount)
        holder.cb.isChecked = !item.required
        val colorTo: Int
        if (item.required) {
            colorTo = ContextCompat.getColor(context, R.color.cardview_light_background)
            holder.cv.setCardBackgroundColor(colorTo)
            holder.tv.setTextColor(ContextCompat.getColor(context, R.color.primary_text_default_material_light))
            holder.tv.paintFlags = (holder.tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv());
        } else {
            colorTo = ContextCompat.getColor(context, R.color.cardview_dark_background)
            holder.cv.setCardBackgroundColor(colorTo)
            holder.tv.setTextColor(ContextCompat.getColor(context, R.color.primary_text_default_material_dark ))
            holder.tv.paintFlags = (holder.tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.rl.setOnClickListener { view ->
            item.required = !item.required
            holder.cb.isChecked = !item.required
            setCardAppearance(holder, item)
        }

        //holder.iv.setOnClickListener(OnClickListeners.openAddDialog(item, MainActivity.activity))
    }

    fun setCardAppearance(holder: ViewHolder, item: IItem) {
        val complete = ContextCompat.getColor(context, R.color.cardview_light_background)
        val incomplete = ContextCompat.getColor(context, R.color.cardview_dark_background)
        val colorAnimation: ValueAnimator
        if (item.required) {
            colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), incomplete, complete);
            holder.tv.setTextColor(ContextCompat.getColor(context, R.color.primary_text_default_material_light))
            holder.tv.paintFlags = (holder.tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv());
        } else {
            colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), complete, incomplete);
            holder.tv.setTextColor(ContextCompat.getColor(context, R.color.primary_text_default_material_dark ))
            holder.tv.paintFlags = (holder.tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG);
        }
        colorAnimation.duration = 250; // milliseconds
        colorAnimation.addUpdateListener {
            animator -> holder.cv.setCardBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start();
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        internal var cv: CardView
        internal var rl: RelativeLayout
        internal var cb: CheckBox
        internal var tv: TextView
        internal var iv: ImageView

        init {
            cv = itemView.findViewById(R.id.item_card_view) as CardView
            rl = itemView.findViewById(R.id.item_container) as RelativeLayout
            cb = itemView.findViewById(R.id.item_check_box) as CheckBox
            tv = itemView.findViewById(R.id.item_text) as TextView
            iv = itemView.findViewById(R.id.item_edit_icon) as ImageView
        }
    }
}