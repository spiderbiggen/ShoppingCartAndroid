package spiderbiggen.shoppingcart.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import spiderbiggen.shoppingcart.MainActivity;
import spiderbiggen.shoppingcart.R;
import spiderbiggen.shoppingcart.datamanagement.Item;
import spiderbiggen.shoppingcart.dialogcreators.ItemDialog;

/**
 * Created by Stefan Breetveld on 17-3-2016.
 * Part of ShoppingCart.
 */
public class ItemRealmAdapter extends RealmBaseAdapter<Item> implements ListAdapter {

    private static final String TAG = ItemRealmAdapter.class.getSimpleName();

    private static final int darkCardBg = R.color.cardview_dark_background;
    private static final int lightCardBg = -1;

    public ItemRealmAdapter(Context context, RealmResults<Item> realmResults) {
        super(context, realmResults, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_item,
                    parent, false);
            viewHolder = new ItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        final Item item = realmResults.get(position);
        viewHolder.tv.setText(String.format("%s %d", item.getItemName(), item.getAmount()));
        viewHolder.cb.setChecked(!item.isNeededNow());


        viewHolder.cv.setCardBackgroundColor(item.isNeededNow() ? lightCardBg : darkCardBg);
        viewHolder.cv.setOnClickListener(itemClicked(viewHolder, item));
        viewHolder.tv.setOnClickListener(itemClicked(viewHolder, item));
        viewHolder.cb.setOnClickListener(itemClicked(viewHolder, item));

        viewHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDialog.openChangeItemDialog(item, MainActivity.activity);
                Log.d(TAG, "onClick: Opened edit item for: " + item.getItemName() + " " + item.getItemId());
            }
        });

        return convertView;
    }

    private View.OnClickListener itemClicked(final ItemViewHolder holder, final Item item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newNeeded = !item.isNeededNow();
                Item.setNeededNow(item, newNeeded);
                holder.cb.setChecked(!newNeeded);
                if (newNeeded) {
                    holder.cv.setCardBackgroundColor(lightCardBg);
                    holder.cb.setChecked(false);
                } else {
                    holder.cv.setCardBackgroundColor(darkCardBg);
                    holder.cb.setChecked(true);
                }
                Log.d(TAG, "onClick: Toggled needed value of: " + item.getItemName() + " " + item.getItemId());
            }
        };
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
