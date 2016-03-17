package spiderbiggen.shoppingcart;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import io.realm.RealmResults;
import spiderbiggen.shoppingcart.adapters.ItemRealmAdapter;
import spiderbiggen.shoppingcart.adapters.StoreSpinnerAdapter;
import spiderbiggen.shoppingcart.datamanagement.Item;
import spiderbiggen.shoppingcart.datamanagement.RealmManager;
import spiderbiggen.shoppingcart.datamanagement.Store;
import spiderbiggen.shoppingcart.dialogcreators.ItemDialog;
import spiderbiggen.shoppingcart.dialogcreators.StoreDialog;

public class MainActivity extends AppCompatActivity {

    public static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        RealmManager.getInstance(activity);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new StoreSpinnerAdapter(toolbar.getContext(), (RealmResults<Store>) RealmManager.getInstance().getStores()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance((Store) spinner.getItemAtPosition(position)))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDialog.openNewItemDialog(activity);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        RealmManager realmManager = RealmManager.getInstance();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_reload:
                realmManager.reloadRealm();
                return true;
            case R.id.action_create_store:
                StoreDialog.openNewStoreDialog(activity);
                return true;
            case R.id.action_save:
                realmManager.exportToFile();
                return true;
            case R.id.action_clear:
                realmManager.clearRealm();
//                final Spinner spinner = (Spinner) findViewById(R.id.spinner);
//                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//                spinner.setAdapter(new StoreSpinnerAdapter(toolbar.getContext(), RealmManager.getInstance().getStores()));
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance((Store) spinner.getSelectedItem()))
//                        .commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmManager.getInstance().getRealm().close();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_STORE_NAME = "store_name";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given {@link Store}
         * @param store the store to display in this fragment
         */
        public static PlaceholderFragment newInstance(Store store) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_STORE_NAME, store.getStoreId());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ListView lv = (ListView) rootView.findViewById(R.id.rv);

            int key = getArguments().getInt(ARG_STORE_NAME);
            RealmResults<Item> items = (RealmResults) RealmManager.getInstance().getItems(key);

            ItemRealmAdapter adapter = new ItemRealmAdapter(activity, items);
            lv.setAdapter(adapter);
            return rootView;
        }
    }
}
