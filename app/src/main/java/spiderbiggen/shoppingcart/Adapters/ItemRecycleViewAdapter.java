package spiderbiggen.shoppingcart.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import spiderbiggen.shoppingcart.Data.Item;
import spiderbiggen.shoppingcart.Data.RealmManager;
import spiderbiggen.shoppingcart.MainActivity;
import spiderbiggen.shoppingcart.OnClickListeners;
import spiderbiggen.shoppingcart.R;

/**
 * Created by Stefan Breetveld on 8-3-2016.
 * Part of Shopping Cart.
 */
public class ItemRecycleViewAdapter extends RecyclerView.Adapter<ItemRecycleViewAdapter.ItemViewHolder> {

    RealmManager realmManager = RealmManager.getInstance();
    private List<Item> items;

    public ItemRecycleViewAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final CardView cv = holder.cv;
        final Item item = items.get(position);
        holder.tv.setText(String.format("%s %d", item.getItemName(), item.getAmount()));
        holder.cb.setChecked(!item.isNeededNow());
        if (item.isNeededNow()) {
            cv.setCardBackgroundColor(-1);
        } else {
            cv.setCardBackgroundColor(R.color.cardview_dark_background);
        }


        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                realmManager.setItemNeeded(items.get(position), !isChecked);
                if (isChecked) {
                    cv.setCardBackgroundColor(R.color.cardview_dark_background);
                } else {
                    cv.setCardBackgroundColor(-1);
                }
            }
        });

        holder.iv.setOnClickListener(OnClickListeners.openChangeItemDialog(item, MainActivity.activity));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        RelativeLayout rl;
        CheckBox cb;
        TextView tv;
        ImageView iv;

        public ItemViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.item_card_view);
            rl = (RelativeLayout) itemView.findViewById(R.id.item_container);
            cb = (CheckBox) itemView.findViewById(R.id.item_check_box);
            tv = (TextView) itemView.findViewById(R.id.item_text);
            iv = (ImageView) itemView.findViewById(R.id.item_edit_icon);
        }
    }
}
