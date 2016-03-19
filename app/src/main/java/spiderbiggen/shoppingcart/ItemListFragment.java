package spiderbiggen.shoppingcart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import io.realm.RealmResults;
import spiderbiggen.shoppingcart.adapters.ItemRealmAdapter;
import spiderbiggen.shoppingcart.datamanagement.Item;
import spiderbiggen.shoppingcart.datamanagement.RealmManager;
import spiderbiggen.shoppingcart.datamanagement.Store;

/**
 * Created by Stefan Breetveld on 17-3-2016.
 * Part of ShoppingCart.
 */
public class ItemListFragment extends Fragment {

    private static final String TAG = ItemListFragment.class.getSimpleName();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_STORE_ID = "store_id";

    public ItemListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given {@link Store}
     *
     * @param store the store to display in this fragment
     */
    public static ItemListFragment newInstance(Store store) {
        ItemListFragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_STORE_ID, store.getStoreId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.rv);

        long key = getArguments().getLong(ARG_STORE_ID);
        Store store = RealmManager.getInstance().getStore(key);
        RealmResults<Item> items = RealmManager.getInstance().getItems(key);

        ItemRealmAdapter adapter = new ItemRealmAdapter(container.getContext(), items);
        lv.setAdapter(adapter);
        ActionBar actionBar = MainActivity.activity.getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(store.getStoreName());
        return rootView;
    }
}
