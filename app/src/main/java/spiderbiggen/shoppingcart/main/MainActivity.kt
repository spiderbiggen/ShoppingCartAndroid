package spiderbiggen.shoppingcart.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import spiderbiggen.shoppingcart.R
import spiderbiggen.shoppingcart.data.ItemAdapter
import spiderbiggen.shoppingcart.data.Store
import spiderbiggen.shoppingcart.data.StoreManager
import spiderbiggen.shoppingcart.data.interfaces.IItem
import spiderbiggen.shoppingcart.data.interfaces.IStore

/**
 * Main Activity Responsible for most user interaction.
 *
 * @author Stefan Breetveld
 */
class MainActivity : IMainView, AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var presenter: MainPresenter = MainPresenter(this)
    var dataInteractor = DataInteractor()

    private val menuFirst = Menu.FIRST
    private val menuGroupId = menuFirst + 1
    private val menuStoresStart = menuFirst + 10
    private var selectedStoreId = 0;

    private var toolbar: Toolbar? = null
    private var fab: FloatingActionButton? = null
    private var drawer: DrawerLayout? = null
    private var toggle: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null
    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        fab = findViewById(R.id.fab) as FloatingActionButton?
        fab?.setOnClickListener { Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        drawer = findViewById(R.id.drawer_layout) as DrawerLayout?

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer?.addDrawerListener(toggle as ActionBarDrawerToggle)
        toggle?.syncState()

        navigationView = findViewById(R.id.nav_view) as NavigationView?
        navigationView?.setNavigationItemSelectedListener(this)

        progressBar = findViewById(R.id.progressBar) as ProgressBar?

        recyclerView = findViewById(R.id.main_content) as RecyclerView?
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        dataInteractor.findStores(presenter)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        if (drawer?.isDrawerOpen(GravityCompat.START) as Boolean) {
            drawer?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        when (id) {
            R.id.action_settings -> {
                return true
            }
            R.id.action_save -> {
                presenter.saveData(this)
                return true
            }
            R.id.action_load -> {
                presenter.loadData(this)
                return true
            }
            R.id.action_add_store -> {
                val builder = AlertDialog.Builder(this)
                val view = this.layoutInflater.inflate(R.layout.store_dialog, null)
                builder.setTitle(R.string.action_add_store)
                builder.setView(view)
                builder.setPositiveButton(R.string.save, { dialog, id ->
                    val editStoreName = view.findViewById(R.id.edit_store_name) as EditText
                    StoreManager.add(Store(editStoreName.text.toString()))
                    dialog.dismiss()
                })
                builder.setNegativeButton(R.string.cancel, { dialog, id ->
                    dialog.cancel()
                })
                builder.create().show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun setItems(items: MutableList<IItem>) {
        recyclerView?.adapter = ItemAdapter(items, this)
    }

    override fun setStores(stores: MutableList<IStore>) {
        val navMenu =  navigationView?.menu
        var selectedItem: MenuItem? = null
        navMenu?.clear()
        for(store in stores) {
            val v = navMenu?.add(menuGroupId, store.id + menuStoresStart, 3, store.name)?.setIcon(R.drawable.ic_store_24dp)
            if(store.id == selectedStoreId) {
                selectedItem = v
            }
        }
        navMenu?.setGroupCheckable(menuGroupId, true, true)
        if (selectedItem != null) {
            selectedItem.isChecked = true
            onNavigationItemSelected(selectedItem)
        }
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
        recyclerView?.visibility = View.INVISIBLE
    }

    override fun hideProgress() {
        progressBar?.visibility = View.INVISIBLE
        recyclerView?.visibility = View.VISIBLE
    }

    override fun showToast(resID: Int) {
        Toast.makeText(this, resID, Toast.LENGTH_LONG).show()
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val storeId = id - menuStoresStart

        if (storeId in StoreManager.getIdList()) {
            showProgress()
            dataInteractor.findItems(presenter, storeId)
            title = item.title
            selectedStoreId = storeId
            return true
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        drawer?.closeDrawer(GravityCompat.START)
        return true
    }



}


