package spiderbiggen.shoppingcart.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import spiderbiggen.shoppingcart.datamanagement.Store;

/**
 * Created by Stefan Breetveld on 12-3-2016.
 * Part of Shopping Cart.
 */
public class StoreSpinnerAdapter extends RealmBaseAdapter<Store> implements ThemedSpinnerAdapter {
    private final ThemedSpinnerAdapter.Helper mDropDownHelper;



    public StoreSpinnerAdapter(Context context, RealmResults<Store> objects) {
        super(context, objects, true);
        mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView itemView;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_spinner_item, parent, false);

            itemView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(itemView);

        } else {
            itemView = (TextView) convertView.getTag();
        }

        Store store = getItem(position);
        if (store != null) {
            itemView.setText(store.getStoreName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            // Inflate the drop down using the helper's LayoutInflater
            LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = convertView;
        }

        Store store = getItem(position);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(store.getStoreName());

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Resources.Theme getDropDownViewTheme() {
        return mDropDownHelper.getDropDownViewTheme();
    }

    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
        mDropDownHelper.setDropDownViewTheme(theme);
    }
}
