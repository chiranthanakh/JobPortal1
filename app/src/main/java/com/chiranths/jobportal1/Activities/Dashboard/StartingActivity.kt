package com.chiranths.jobportal1.Activities.Dashboard

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chiranths.jobportal1.Activities.BasicActivitys.LivingPlaceActivity
import com.chiranths.jobportal1.Activities.BasicActivitys.Travelsactivity
import com.chiranths.jobportal1.Activities.Businesthings.BusinessActivity
import com.chiranths.jobportal1.Activities.Businesthings.BusinessFragment
import com.chiranths.jobportal1.Activities.Construction.ConstructionActivity
import com.chiranths.jobportal1.Activities.LoanActivity.LoanFragment
import com.chiranths.jobportal1.Activities.Profile.ProfileFragments
import com.chiranths.jobportal1.Activities.Propertys.PropertyFragment
import com.chiranths.jobportal1.Interface.FragmentInteractionListener
import com.chiranths.jobportal1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class StartingActivity : AppCompatActivity() {
    var bottomNavShift: BottomNavigationView? = null
    var profileFragment = ProfileFragments()
    var startingFragment = DashboardFragment()
    var businessFragment = BusinessFragment()
    var propertyFragment = PropertyFragment()
    var loanFragment = LoanFragment()
    var frameLayout: FrameLayout? = null
    var drawer_layout: DrawerLayout? = null
    var progressDialog: ProgressDialog? = null
    var iv_drawer_nav: ImageView? = null
    var btn_nav_logout : Button? = null
    var iv_nav_image : ImageView? = null
    var tv_nav_username : TextView? = null
    var tv_nav_email : TextView? = null
    var dashboard_property : TextView? = null
    var dashboard_loans : TextView? = null
    var dashboard_Business : TextView? = null
    var dashboard_constructions : TextView? = null
    var dashboard_travels : TextView? = null
    var dashboard_rented : TextView? = null
    var dashboard_hotels : TextView? = null
    var dashboard_profile : TextView? = null
    var navigation_view : NavigationView? = null


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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    private fun initilize() {
        drawer_layout = findViewById(R.id.drawer_layout_main_d)
        navigation_view = findViewById(R.id.nav_view_mainA)
        iv_drawer_nav = findViewById<ImageView>(R.id.iv_drawer_nav)
        btn_nav_logout = findViewById(R.id.btn_nav_logout)
        iv_nav_image = findViewById(R.id.iv_nav_image)
        tv_nav_username = findViewById(R.id.tv_nav_username)
        tv_nav_email = findViewById(R.id.tv_nav_email)
        dashboard_property = findViewById(R.id.dashboard_property)
        dashboard_loans = findViewById(R.id.dashboard_loans)
        dashboard_Business = findViewById(R.id.dashboard_Business)
        dashboard_constructions = findViewById(R.id.dashboard_constructions)
        dashboard_travels = findViewById(R.id.dashboard_travels)
        dashboard_rented = findViewById(R.id.dashboard_rented)
        dashboard_hotels = findViewById(R.id.dashboard_hotels)
        dashboard_profile = findViewById(R.id.dashboard_profile)


        iv_drawer_nav?.setOnClickListener{
            drawer_layout?.openDrawer(GravityCompat.START)
        }

        dashboard_property?.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, propertyFragment).commit()
            drawer_layout?.closeDrawer(GravityCompat.START)
        }

        dashboard_loans?.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loanFragment).commit()
            drawer_layout?.closeDrawer(GravityCompat.START)

        }

        dashboard_Business?.setOnClickListener{
            val intent = Intent(this, BusinessActivity::class.java)
            startActivity(intent)
        }

        dashboard_constructions?.setOnClickListener{
            val intent = Intent(this, ConstructionActivity::class.java)
            startActivity(intent)

        }

        dashboard_travels?.setOnClickListener{
            val intent = Intent(this, Travelsactivity::class.java)
            startActivity(intent)
        }

        dashboard_rented?.setOnClickListener{
            val intent = Intent(this, LivingPlaceActivity::class.java)
            startActivity(intent)
        }

        dashboard_hotels?.setOnClickListener{
            val intent = Intent(this, LivingPlaceActivity::class.java)
            startActivity(intent)
        }

        bottomNavShift = findViewById(R.id.bottomNavShift)
        frameLayout = findViewById(R.id.fragment_container)
        fetchProfile()
        bottomNavShift?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.profile -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit()
                R.id.Home -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, startingFragment).addToBackStack(null).commit()
                R.id.it_business -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, businessFragment).addToBackStack(null).commit()
                R.id.it_property -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, propertyFragment).addToBackStack(null).commit()
            }
            true
        })
    }

    override fun onBackPressed() {
        if(navigation_view?.isVisible == true){
            drawer_layout?.closeDrawer(GravityCompat.START)
        }else {
            val homeItem: MenuItem? = bottomNavShift?.getMenu()?.getItem(0)
            var count = supportFragmentManager.backStackEntryCount
            if (count == 0) {
                super.onBackPressed()
            } else {
                supportFragmentManager.popBackStack()
                bottomNavShift?.setSelectedItemId(homeItem?.getItemId()!!)
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    private fun fetchProfile() {
        val sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val nameofuser = sh?.getString("name", "")
        val userNumber = sh?.getString("number", "")
        val useremail = sh?.getString("email", "")
        if (userNumber !== "") {
            val profile = FirebaseDatabase.getInstance().reference.child("Profile").child(
                userNumber!!
            )
            profile.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataMap = snapshot.value as HashMap<String, Any>?
                        for (key in dataMap!!.keys) {
                            tv_nav_username?.text = dataMap["name"] as CharSequence?
                            tv_nav_email?.text = dataMap["Email"] as CharSequence?
                            Glide.with(this@StartingActivity)
                                .load(dataMap["image"])
                                .apply(RequestOptions().override(500, 500))
                                .into(iv_nav_image!!)
                            try {
                            } catch (cce: ClassCastException) {
                                try {
                                    val mString = dataMap[key].toString()
                                    //addTextToView(mString);
                                } catch (cce2: ClassCastException) {
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

}