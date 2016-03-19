package spiderbiggen.shoppingcart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.RealmResults;
import spiderbiggen.shoppingcart.adapters.StoreNavDrawerAdapter;
import spiderbiggen.shoppingcart.datamanagement.RealmManager;
import spiderbiggen.shoppingcart.datamanagement.Store;
import spiderbiggen.shoppingcart.dialogcreators.ItemDialog;
import spiderbiggen.shoppingcart.dialogcreators.StoreDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static AppCompatActivity activity;
    private RealmManager realmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        realmManager = RealmManager.getInstance(activity);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDialog.openNewItemDialog(activity);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Store store = realmManager.getStores().get(0);
        ItemListFragment itemListFragment = ItemListFragment.newInstance(store);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, itemListFragment, "item_list").commit();

        ListView listView = (ListView) findViewById(R.id.nav_list);
        RealmResults<Store> stores = realmManager.getStores();
        StoreNavDrawerAdapter adapter = new StoreNavDrawerAdapter(activity, stores);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmManager.getInstance().getRealm().close();
    }
}
