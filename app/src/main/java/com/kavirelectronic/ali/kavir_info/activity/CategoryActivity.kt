package com.kavirelectronic.ali.kavir_info.activity

import android.app.Fragment
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.kavirelectronic.ali.kavir_info.R
import com.kavirelectronic.ali.kavir_info.activity.CategoryActivity
import com.kavirelectronic.ali.kavir_info.fragments.*
import com.kavirelectronic.ali.kavir_info.utility.CustomTypeFaceSpan
import com.kavirelectronic.ali.kavir_info.utility.FormatHelper
import com.kavirelectronic.ali.kavir_info.utility.SaveItem
import com.kavirelectronic.ali.kavir_info.utility.Utility

class CategoryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mTopToolbar: Toolbar? = null
    private var transaction: FragmentTransaction? = null
    private var drawer: DrawerLayout? = null
    private var toolbarTitle: TextView? = null
    private var displayNameTv: TextView? = null
    private var navigationView: NavigationView? = null
    private var searchBtn: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        context = this
        mTopToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        toolbarTitle = findViewById<View>(R.id.category_title) as TextView
        searchBtn = findViewById<View>(R.id.search_btn) as ImageButton
        searchBtn!!.setOnClickListener { startActivity(Intent(this@CategoryActivity, SearchActivity::class.java)) }
        setSupportActionBar(mTopToolbar)
        //// check new version
        if (SaveItem.getItem(this, SaveItem.COME_NEW_VERSION, "").equals("1", ignoreCase = true)) {
            Utility.showAlertNewVersion(this
                    , SaveItem.getItem(this, SaveItem.NEW_VERSION_NAME, "")
                    , SaveItem.getItem(this, SaveItem.NEW_VERSION_URL, ""))
            SaveItem.setItem(this, SaveItem.COME_NEW_VERSION, "0")
        }

        //***************************
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView!!.getHeaderView(0)
        displayNameTv = headerView.findViewById<View>(R.id.display_name_header) as TextView
        var displayName = SaveItem.getItem(this, SaveItem.USER_NAME, "")
        if (displayName == "") {
            displayName = getString(R.string.guest)
        }
        displayNameTv!!.text = FormatHelper.toPersianNumber(displayName)
        navigationView!!.setNavigationItemSelectedListener(this)
        setNameLoginItem()
        toggle.isDrawerIndicatorEnabled = false
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        //////***************************************
        findViewById<View>(R.id.drawer_button).setOnClickListener { // open right drawer
            if (drawer!!.isDrawerOpen(GravityCompat.END)) {
                drawer!!.closeDrawer(GravityCompat.END)
            } else drawer!!.openDrawer(GravityCompat.END)
        }
        setFontNavig()
    }

    override fun onResume() {
        try {
            val fragment = fragmentManager.findFragmentById(R.id.fragment) as CategoryFragment
            fragment.dataFromServer()
        } catch (e: Exception) {
            Log.e("onResume:", e.message)
        }
        super.onResume()
    }

    private fun setFontNavig() {
        val m = navigationView!!.menu
        for (i in 0 until m.size()) {
            val mi = m.getItem(i)

            //for aapplying a font to subMenu ...
            val subMenu = mi.subMenu
            if (subMenu != null && subMenu.size() > 0) {
                for (j in 0 until subMenu.size()) {
                    val subMenuItem = subMenu.getItem(j)
                    applyFontToMenuItem(subMenuItem)
                }
            }
            applyFontToMenuItem(mi)
        }
    }

    private fun loadFragment(fragmentNew: Fragment?) {
        transaction = fragmentManager!!.beginTransaction()
        val fragment = fragmentManager!!.findFragmentById(R.id.fragment)
        if (fragment != null) {
            fragmentManager!!.beginTransaction().remove(fragment).commit()
        }
        // load fragment
        transaction!!.replace(R.id.fragment, fragmentNew)
        transaction!!.addToBackStack(null)
        transaction!!.commit()
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.END)) {
            drawer!!.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = menuItem.itemId
        if (id == R.id.login) {
            loginProcess()
        } else if (id == R.id.home_page) {
            loadFragment(CategoryFragment.newInstance())
            toolbarTitle!!.text = getString(R.string.home)
            statusSearchBtn(View.VISIBLE)
        } else if (id == R.id.ready_read_page) {
            loadFragment(ReadyReadFragment.newInstance())
            toolbarTitle!!.text = getString(R.string.redy_read)
            statusSearchBtn(View.VISIBLE)
        } else if (id == R.id.about_we) {
            loadFragment(AboutFragment.newInstance())
            toolbarTitle!!.text = getString(R.string.about_we)
            statusSearchBtn(View.VISIBLE)
        } else if (id == R.id.register) {
            loadFragment(RegisterFragment.newInstance())
            toolbarTitle!!.text = getString(R.string.register)
            statusSearchBtn(View.VISIBLE)
        } else if (id == R.id.active) {
            loadFragment(ActiveFragment.newInstance())
            toolbarTitle!!.text = getString(R.string.active)
            statusSearchBtn(View.VISIBLE)
        } else if (id == R.id.access) {
            loadFragment(AccessFragment.newInstance())
            toolbarTitle!!.text = getString(R.string.access_list)
            statusSearchBtn(View.GONE)
        } else if (id == R.id.help) {
            loadFragment(HelpFragment.newInstance())
            toolbarTitle!!.text = getString(R.string.help)
            statusSearchBtn(View.VISIBLE)
        }
        drawer!!.closeDrawer(GravityCompat.END)
        return true
    }

    private fun statusSearchBtn(visible: Int) {
        searchBtn!!.visibility = visible
    }

    private fun loginProcess() {
        val menu = navigationView!!.menu
        val loginItem = menu.findItem(R.id.login)
        if (loginItem.title.toString().equals(getString(R.string.login), ignoreCase = true)) {
            startActivity(Intent(this@CategoryActivity, LoginActivity::class.java))
        } else {
            logout()
        }
    }

    private fun logout() {
        clearSeesion()
        val intent = intent
        finishAffinity()
        startActivity(intent)
    }

    private fun clearSeesion() {
        SaveItem.setItem(this, SaveItem.USER_FIRST_NAME, "")
        SaveItem.setItem(this, SaveItem.USER_LAST_NAME, "")
        SaveItem.setItem(this, SaveItem.USER_EMAIL, "")
        SaveItem.setItem(this, SaveItem.USER_NAME, "")
        SaveItem.setItem(this, SaveItem.USER_MOBILE, "")
        SaveItem.setItem(this, SaveItem.USER_ID, "")
        SaveItem.setItem(this, SaveItem.USER_COOKIE, "")
    }


    private fun setNameLoginItem() {
        val menu = navigationView!!.menu
        val loginItem = menu.findItem(R.id.login)
        val activeItem = menu.findItem(R.id.active)
        val registerItem = menu.findItem(R.id.register)
        val accessItem = menu.findItem(R.id.access)
        if (SaveItem.getItem(this, SaveItem.USER_COOKIE, "") == "") {
            loginItem.title = getString(R.string.login)
            activeItem.isVisible = false
            registerItem.isVisible = true
            accessItem.isVisible = false
        } else {
            loginItem.title = getString(R.string.logout)
            activeItem.isVisible = true
            registerItem.isVisible = false
            accessItem.isVisible = true
        }
    }

    private fun applyFontToMenuItem(mi: MenuItem) {
        val font = Typeface.createFromAsset(assets, "fonts/Vazir.ttf")
        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(CustomTypeFaceSpan("", font, Color.WHITE), 0, mNewTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        mi.title = mNewTitle
    }

    companion object {
        @JvmField
        var context: Context? = null
    }
}