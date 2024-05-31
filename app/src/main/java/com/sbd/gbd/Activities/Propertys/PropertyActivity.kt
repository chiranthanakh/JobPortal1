package com.sbd.gbd.Activities.Propertys

import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.Admin.Admin_ads_dashboard
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Adapters.PropertyAdaptor
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import java.util.Collections

class PropertyActivity : AppCompatActivity(), View.OnClickListener {
    private var ProductsRef: DatabaseReference? = null
    var adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder>? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var btn_add: Button? = null
    var search: LinearLayout? = null
    var iv_back_toolbar: ImageView? = null
    var iv_sites: ImageView? = null
    var iv_green_land: ImageView? = null
    var iv_home: ImageView? = null
    var iv_commercial: ImageView? = null
    var ll_field : LinearLayout? = null
    var buttonToggleGroup : MaterialButtonToggleGroup? = null
    var ll_assetRecyclrView : LinearLayout? = null
    var ll_assetText : LinearLayout? = null
    var mHandler = Handler()
    var propertylist: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var siteslist: ArrayList<String> = ArrayList<String>()
    var Homeslist: ArrayList<String> = ArrayList<String>()
    var Rentallist: ArrayList<String> = ArrayList<String>()
    var adslist: ArrayList<AdsModel> = ArrayList<AdsModel>()
    var propertyAdaptor: PropertyAdaptor? = null
    var adsAdaptor: AdsAdaptor? = null
    var recyclarviewads: RecyclerView? = null
    var bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
           // setTheme(R.style.darkTheme)
        } else {
            setTheme(R.style.JobPortaltheam)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.app_blue)
        }
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        initilize()
        AsyncTask.execute { fetchads() }
        intent.getStringExtra("type")?.let { fetchproducts(it) }
    }

    private fun initilize() {
        btn_add = findViewById(R.id.btn_add_property)
        btn_add?.setOnClickListener(this)
        iv_sites = findViewById(R.id.iv_sites)
        iv_commercial = findViewById(R.id.iv_commercial)
        iv_green_land = findViewById(R.id.iv_green_land)
        ll_assetText = findViewById(R.id.ll_assetText)
        ll_assetRecyclrView = findViewById(R.id.ll_assetRecyclrView)
        buttonToggleGroup = findViewById(R.id.buttonToggleGroup)
        ll_assetText?.visibility = View.GONE
        buttonToggleGroup?.visibility = View.GONE
        ll_assetRecyclrView?.visibility = View.GONE
        iv_home = findViewById(R.id.iv_home)
        search = findViewById(R.id.llsearch_property)
        iv_sites?.setOnClickListener(this)
        iv_green_land?.setOnClickListener(this)
        iv_commercial?.setOnClickListener(this)
        iv_home?.setOnClickListener(this)
        iv_back_toolbar = findViewById(R.id.back_toolbar_property)
        iv_back_toolbar?.setOnClickListener(this)
        recyclarviewads = findViewById(R.id.rv_adds_layots2)
        recyclerView = findViewById(R.id.recycler_menu)
        recyclerView?.setHasFixedSize(true)
        val mgrid = GridLayoutManager(this, 1)
        recyclerView?.setLayoutManager(mgrid)
        search?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@PropertyActivity, SearchActivity::class.java)
            bundle.putString("searchtype", "property")
            intent.putExtras(bundle)
            startActivity(intent)
        })
    }

    override fun onResume() {
        super.onResume()
    }

    private fun fetchads() {
        val adsimage = FirebaseDatabase.getInstance().reference.child("adsforyou")
        adsimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    adslist.clear()
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            adslist.add(
                                AdsModel(
                                    userData!![AppConstants.image].toString(),
                                    userData[AppConstants.image2].toString(),
                                    userData[AppConstants.pid].toString(),
                                    userData[AppConstants.description].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData[AppConstants.price].toString(),
                                    userData[AppConstants.pname].toString(),
                                    userData[AppConstants.propertysize].toString(),
                                    userData[AppConstants.location].toString(),
                                    userData[AppConstants.number].toString(),
                                    userData[AppConstants.Status].toString(),
                                    userData[AppConstants.postedBy].toString(),
                                    userData[AppConstants.approvedBy].toString(),
                                    userData[AppConstants.facing].toString(),
                                    userData[AppConstants.ownership].toString(),
                                    userData[AppConstants.postedOn].toString(),
                                    userData[AppConstants.postedOn].toString(),
                                    userData[AppConstants.katha].toString(),
                                    userData[AppConstants.text1].toString(),
                                    userData[AppConstants.text2].toString(),
                                    userData[AppConstants.text3].toString(),
                                    userData[AppConstants.text4].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {
                        }
                    }
                    Collections.shuffle(adslist)
                    adsAdaptor = AdsAdaptor(adslist, this@PropertyActivity)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@PropertyActivity, RecyclerView.HORIZONTAL, false)
                    recyclarviewads!!.layoutManager = n1layoutManager
                    recyclarviewads!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { /*if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }*/
                        recyclarviewads!!.adapter = adsAdaptor
                        adsAdaptor!!.notifyItemRangeInserted(0, adslist.size)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchproducts(type : String) {
        var coroselimage = if(type == "" || type == null ) {
            FirebaseDatabase.getInstance().reference.child("Products").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        } else {
            FirebaseDatabase.getInstance().reference.child("Products").orderByChild(AppConstants.type).equalTo(type)
        }
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    propertylist.clear()
                    siteslist.clear()
                    Homeslist.clear()
                    Rentallist.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get(AppConstants.Status)
                                    ?.equals(AppConstants.user) == true
                            ) {
                                propertylist.add(
                                    FilterModel(
                                        userData[AppConstants.pname].toString(),
                                        userData[AppConstants.description].toString(),
                                        userData[AppConstants.price].toString(),
                                        userData[AppConstants.image].toString(),
                                        userData[AppConstants.category].toString(),
                                        userData[AppConstants.pid].toString(),
                                        AppConstants.date,
                                        AppConstants.time,
                                        userData[AppConstants.type].toString(),
                                        userData[AppConstants.propertysize].toString(),
                                        userData[AppConstants.location].toString(),
                                        userData[AppConstants.number].toString(),
                                        userData[AppConstants.Status].toString()
                                    )
                                )
                            }

                        } catch (cce: ClassCastException) {

                        }
                    }
                    propertyAdaptor = PropertyAdaptor(propertylist, this@PropertyActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@PropertyActivity, RecyclerView.VERTICAL, false)
                    recyclerView!!.layoutManager = nlayoutManager
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                    propertyAdaptor!!.notifyItemRangeInserted(0, propertylist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
       /* when (view.id) {
            R.id.btn_add_property -> {
                val intent = Intent(this, Admin_ads_dashboard::class.java)
                intent.putExtra("page", "2")
                startActivity(intent)
            }

            R.id.iv_sites -> {
                propertyAdaptor = PropertyAdaptor(siteslist, this@PropertyActivity)
                val nlayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@PropertyActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, siteslist.size)
            }

            R.id.iv_home -> {
                propertyAdaptor = PropertyAdaptor(Homeslist, this@PropertyActivity)
                val nlayoutManager1: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@PropertyActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager1
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Homeslist.size)
            }

            R.id.iv_commercial -> {
                propertyAdaptor = PropertyAdaptor(Rentallist, this@PropertyActivity)
                val nlayoutManager2: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@PropertyActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager2
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Rentallist.size)
            }

            R.id.iv_green_land -> {
                propertyAdaptor = PropertyAdaptor(greenlandlist, this@PropertyActivity)
                val nlayoutManager3: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@PropertyActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager3
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, greenlandlist.size)
            }

            R.id.back_toolbar_property -> finish()
        }*/
    }
}