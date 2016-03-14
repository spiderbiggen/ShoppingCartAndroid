package spiderbiggen.shoppingcart;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import spiderbiggen.shoppingcart.Adapters.ItemRecycleViewAdapter;
import spiderbiggen.shoppingcart.Adapters.StoreSpinnerAdapter;
import spiderbiggen.shoppingcart.Data.DataBaseManager;
import spiderbiggen.shoppingcart.Data.ItemReaderHelper;

public class MainActivity extends AppCompatActivity {

    public static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        DataBaseManager.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new StoreSpinnerAdapter(toolbar.getContext(), DataBaseManager.getInstance().getKeys()));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance((String) spinner.getItemAtPosition(position)))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(OnClickListeners.openAddDialog(this));

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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_recreate) {
            DataBaseManager.getInstance().updateStores();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_STORE_NAME = "store_name";
        private static final int URL_LOADER = 0;
        private View rootView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String store) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_STORE_NAME, store);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getLoaderManager().initLoader(URL_LOADER, getArguments(), this);
            getActivity().requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

            getActivity().setProgressBarIndeterminateVisibility(true);
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case URL_LOADER:
                    String key = args.getString(ARG_STORE_NAME);
                    assert key != null;
                    if(key.equals(getContext().getString(R.string.leftovers))) {
                        return new CursorLoader(getActivity(), Uri.parse("spiderbiggen.shoppingcart/item_table"), null, null, null, null);
                    }
                    String selection = ItemReaderHelper.ItemEntry.COLUMN_NAME_ITEM_STORE_ID + " =  SELECT " + ItemReaderHelper.StoreEntry._ID + " FROM " + ItemReaderHelper.StoreEntry.TABLE_NAME + " WHERE " + ItemReaderHelper.StoreEntry.COLUMN_NAME_STORE_NAME + " = ?";
                    String[] selectionargs = { key };
                    return new CursorLoader(getActivity(), Uri.parse("spiderbiggen.shoppingcart/item_table"), null, selection, selectionargs, null);
                default:
                    return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            getActivity().setProgressBarIndeterminateVisibility(false);
            RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);
            rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(llm);

            String key = getArguments().getString(ARG_STORE_NAME);

            ItemRecycleViewAdapter adapter = new ItemRecycleViewAdapter(DataBaseManager.getInstance().getItemsAsList(key));
            rv.setAdapter(adapter);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
