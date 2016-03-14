package spiderbiggen.shoppingcart.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Stefan Breetveld on 12-3-2016.
 * Part of Shopping Cart.
 */
public class StoreSpinnerAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
    private final ThemedSpinnerAdapter.Helper mDropDownHelper;

    public StoreSpinnerAdapter(Context context, List<String> mArrayList) {
        super(context, android.R.layout.simple_list_item_1, mArrayList);
        mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
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

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getItem(position));

        return view;
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
