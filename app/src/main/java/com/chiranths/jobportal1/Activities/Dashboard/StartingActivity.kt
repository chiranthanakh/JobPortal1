package com.chiranths.jobportal1.Activities.Dashboard

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chiranths.jobportal1.Activities.BasicActivitys.AboutUsActivity
import com.chiranths.jobportal1.Activities.BasicActivitys.LivingPlaceActivity
import com.chiranths.jobportal1.Activities.BasicActivitys.Travelsactivity
import com.chiranths.jobportal1.Activities.Businesthings.BusinessActivity
import com.chiranths.jobportal1.Activities.Businesthings.BusinessFragment
import com.chiranths.jobportal1.Activities.Construction.ConstructionActivity
import com.chiranths.jobportal1.Activities.LoanActivity.LoanActivity
import com.chiranths.jobportal1.Activities.LoanActivity.LoanFragment
import com.chiranths.jobportal1.Activities.OtpLoginActivity
import com.chiranths.jobportal1.Activities.Profile.ProfileFragments
import com.chiranths.jobportal1.Activities.Propertys.PropertyFragment
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.chiranths.jobportal1.Utilitys.PreferenceManager
import com.chiranths.jobportal1.databinding.ActivityStartingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
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
    private var permissionListener: PermissionListener? = null
    private lateinit var binding: ActivityStartingBinding
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var preferenceManager:PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStartingBinding.inflate(layoutInflater)
        preferenceManager= PreferenceManager(this);
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        analytics = Firebase.analytics
        progressDialog = ProgressDialog(this)
        initilize()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, startingFragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, startingFragment).commit()
    }

    private fun initilize() {
        iv_drawer_nav = findViewById<ImageView>(R.id.iv_drawer_nav)

        iv_drawer_nav?.setOnClickListener{
            if (preferenceManager.getLoginState()) {
                fetchProfile()
            } else {
                binding.btnNavLogout.setText("LogIn")
            }
            binding.drawerLayoutMainD?.openDrawer(GravityCompat.START)
        }

        binding.dashboardProperty?.setOnClickListener {
            replaceFragment(propertyFragment)
            binding.drawerLayoutMainD?.closeDrawer(GravityCompat.START)
        }

        binding.dashboardLoans.setOnClickListener {
            binding.drawerLayoutMainD.closeDrawer(GravityCompat.START)
            val intent = Intent(this, LoanActivity::class.java)
            startActivity(intent)
        }

        binding.dashboardBusiness.setOnClickListener {
            replaceFragment(businessFragment)
        }

        binding.dashboardConstructions.setOnClickListener{
            binding.drawerLayoutMainD.closeDrawer(GravityCompat.START)
            val intent = Intent(this, ConstructionActivity::class.java)
            startActivity(intent)
        }

        binding.dashboardTravels.setOnClickListener{
            binding.drawerLayoutMainD.closeDrawer(GravityCompat.START)
            val intent = Intent(this, Travelsactivity::class.java)
            startActivity(intent)
        }

        binding.dashboardAboutUs.setOnClickListener {
            binding.drawerLayoutMainD.closeDrawer(GravityCompat.START)
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }

        binding.dashboardRented.setOnClickListener{
            binding.drawerLayoutMainD.closeDrawer(GravityCompat.START)
            val intent = Intent(this, LivingPlaceActivity::class.java)
            startActivity(intent)
        }

        binding.dashboardHotels.setOnClickListener{
            binding.drawerLayoutMainD.closeDrawer(GravityCompat.START)
            val intent = Intent(this, LivingPlaceActivity::class.java)
            startActivity(intent)
        }

        binding.btnNavLogout.setOnClickListener{
            if (preferenceManager.getLoginState()) {
                val sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                val editor = sh?.edit()
                editor?.clear()
                editor?.apply()
                binding.drawerLayoutMainD?.closeDrawer(GravityCompat.START)
            } else {
                val intent = Intent(this, OtpLoginActivity::class.java)
                startActivity(intent)
            }
        }

        bottomNavShift = findViewById(R.id.bottomNavShift)
        frameLayout = findViewById(R.id.fragment_container)
        fetchProfile()
        bottomNavShift?.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.profile -> replaceFragment(profileFragment)
                R.id.Home -> replaceFragment(startingFragment)
                R.id.it_business -> replaceFragment(businessFragment)
                R.id.it_property -> replaceFragment(propertyFragment)
            }
            true
        })
    }

    override fun onBackPressed() {
        if(binding.navViewMainA?.isVisible == true){
            drawer_layout?.closeDrawer(GravityCompat.START)
        }else {
           /* val homeItem: MenuItem? = bottomNavShift?.getMenu()?.getItem(0)
            var count = supportFragmentManager.backStackEntryCount
            Log.d("countofFragment",count.toString())*/

            val homeItem: MenuItem? = bottomNavShift?.getMenu()?.getItem(0)
            Log.d("checkCountfdn", homeItem.toString())
            if (supportFragmentManager.backStackEntryCount > 0) {
               // supportFragmentManager.popBackStack()
                bottomNavShift?.setSelectedItemId(homeItem?.getItemId()!!)
            } else {
                // If the back stack is empty, handle the back press as needed
                super.onBackPressed()
            }
        }
    }

    private fun fetchProfile() {
        val sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val nameofuser = sh?.getString("name", "")
        val userNumber = sh?.getString(AppConstants.number, "")
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
                            binding.tvNavUsername?.text = dataMap["name"] as CharSequence?
                            binding.tvNavEmail?.text = dataMap["Email"] as CharSequence?
                            Glide.with(this@StartingActivity)
                                .load(dataMap[AppConstants.image])
                                .apply(RequestOptions().override(500, 500))
                                .into(binding.ivNavImage)
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

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_in_right,
            R.anim.slide_in_left,
            R.anim.slide_in_right
        )
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null).commit()
    }

    private fun checkPermissions() {
        permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                //openCamera()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@StartingActivity,
                    "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Please Provide permission ")
                .setPermissions(
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.CAMERA
                )
                .check()
        } else {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Please provide Permission")
                .setPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .check()
        }
    }
}