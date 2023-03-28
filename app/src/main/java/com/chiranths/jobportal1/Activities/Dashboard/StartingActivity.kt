package com.chiranths.jobportal1.Activities.Dashboard

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.chiranths.jobportal1.Activities.LoanActivity.LoanFragment
import com.chiranths.jobportal1.Activities.Profile.ProfileFragments
import com.chiranths.jobportal1.Activities.Propertys.PropertyFragment
import com.chiranths.jobportal1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class StartingActivity : AppCompatActivity() {
    var bottomNavShift: BottomNavigationView? = null
    var profileFragment = ProfileFragments()
    var startingFragment = DashboardFragment()
    var propertyFragment = PropertyFragment()
    var loanFragment = LoanFragment()
    var frameLayout: FrameLayout? = null
    var drawer_layout: DrawerLayout? = null
    var progressDialog: ProgressDialog? = null
    var iv_drawer_nav: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        progressDialog = ProgressDialog(this)
        initilize()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, startingFragment)
            .commit()

        val packageManager: PackageManager = applicationContext.getPackageManager()
        val lastUpdatedTime =
            packageManager.getPackageInfo(applicationContext.packageName, 0).lastUpdateTime

        val test = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastUpdatedTime)
        val test2 = System.currentTimeMillis()

        val lastUpdateTime1 = packageManager.getPackageInfo(applicationContext.packageName, 0).firstInstallTime
        val date = Date(lastUpdateTime1)
        val dateFormat: DateFormat = DateFormat.getDateTimeInstance()
        val lastUpdateDate: String = dateFormat.format(date)
        // Initialize the AppUpdateManager instance
        val appUpdateManager = AppUpdateManagerFactory.create(this)



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onResume() {
        super.onResume()

        // initilize();
    }

    private fun initilize() {
        drawer_layout = findViewById(R.id.drawer_layout_main_d)

        iv_drawer_nav = findViewById<ImageView>(R.id.iv_drawer_nav)

        iv_drawer_nav?.setOnClickListener{
            drawer_layout!!.openDrawer(GravityCompat.START)
        }

        bottomNavShift = findViewById(R.id.bottomNavShift)
        frameLayout = findViewById(R.id.fragment_container)
        bottomNavShift?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.profile -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, profileFragment).commit()
                R.id.Home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, startingFragment).commit()
                R.id.it_loan -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, loanFragment).commit()
                R.id.it_property -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, propertyFragment).commit()
            }
            true
        })
    }
}