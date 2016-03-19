package spiderbiggen.shoppingcart.adapters;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import spiderbiggen.shoppingcart.ItemListFragment;
import spiderbiggen.shoppingcart.R;
import spiderbiggen.shoppingcart.datamanagement.Store;

/**
 * Created by Stefan Breetveld on 12-3-2016.
 * Part of Shopping Cart.
 */
public class StoreNavDrawerAdapter extends RealmBaseAdapter<Store> implements ListAdapter {

    private static final String TAG = StoreNavDrawerAdapter.class.getSimpleName();

    private AppCompatActivity activity;

    public StoreNavDrawerAdapter(AppCompatActivity context, RealmResults<Store> objects) {
        super(context, objects, true);
        activity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView itemView;
        RelativeLayout relativeLayout;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nav_list_item, parent, false);

            relativeLayout = (RelativeLayout) convertView.findViewById(R.id.nav_item_container);
            itemView = (TextView) relativeLayout.findViewById(R.id.text1);
            convertView.setTag(relativeLayout);

        } else {
            relativeLayout = (RelativeLayout) convertView.getTag();
            itemView = (TextView) relativeLayout.findViewById(R.id.text1);
        }

        final Store store = getItem(position);
        if (store != null) {
            itemView.setText(store.getStoreName());
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ItemListFragment itemListFragment = ItemListFragment.newInstance(store);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, itemListFragment, "item_list")
                            .commit();
                    DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                    drawer.closeDrawers();
                }
            });
        }

        return convertView;
    }
}
