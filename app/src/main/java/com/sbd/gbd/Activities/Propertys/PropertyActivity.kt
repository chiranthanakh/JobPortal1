package com.sbd.gbd.Activities.Propertys

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Adapters.PropertyAdaptor
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.databinding.ActivityPropertyBinding
import com.sbd.gbd.databinding.ActivityPropertyMainBinding
import kotlinx.coroutines.launch

class PropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyMainBinding
    private var ProductsRef: DatabaseReference? = null
    var adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var search: LinearLayout? = null
    var ll_field : LinearLayout? = null
    var mHandler = Handler()
    var propertylist: ArrayList<FilterModel> = ArrayList()
    var siteslist: ArrayList<String> = ArrayList()
    var Homeslist: ArrayList<String> = ArrayList()
    var Rentallist: ArrayList<String> = ArrayList()
    var adslist: ArrayList<AdsModel> = ArrayList()
    var propertyAdaptor: PropertyAdaptor? = null
    var adsAdaptor: AdsAdaptor? = null
    var bundle = Bundle()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPropertyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager= PreferenceManager(this)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.app_blue)
        }
        ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        initilize()
        lifecycleScope.launch {
            fetchads()
            intent.getStringExtra("type")?.let { fetchproducts(it) }
        }
    }

    private fun initilize() {
        binding.llAssetText.visibility = View.GONE
        binding.buttonToggleGroup.visibility = View.GONE
        binding.llAssetRecyclrView.visibility = View.GONE
        search = findViewById(R.id.llsearch_property)
        binding.recyclerMenu.setHasFixedSize(true)
        val mgrid = GridLayoutManager(this, 1)
        binding.recyclerMenu.setLayoutManager(mgrid)
        search?.setOnClickListener{
            val intent = Intent(this@PropertyActivity, SearchActivity::class.java)
            bundle.putString("searchtype", "property")
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun fetchads() {
        val adsimage = FirebaseDatabase.getInstance().reference.child(AppConstants.ads)
        adsimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    adslist.clear()
                    val dataMap = snapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
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
                        } catch (_: ClassCastException) {
                        }
                    }
                    adslist.shuffle()
                    adsAdaptor = AdsAdaptor(adslist, this@PropertyActivity)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@PropertyActivity, RecyclerView.HORIZONTAL, false)
                    binding.rvAddsLayots2.layoutManager = n1layoutManager
                    binding.rvAddsLayots2.itemAnimator = DefaultItemAnimator()
                    mHandler.post {
                        binding.rvAddsLayots2.adapter = adsAdaptor
                        adsAdaptor?.notifyItemRangeInserted(0, adslist.size)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchproducts(type : String) {
        Log.d("typeprint",type)
        var coroselimage = if(type == "" || type == null ) {
            FirebaseDatabase.getInstance().reference.child(AppConstants.products).orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        } else {
            FirebaseDatabase.getInstance().reference.child(AppConstants.products).orderByChild(AppConstants.type).equalTo(type)
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
                            Log.d("checkdataofdist",  userData?.get(AppConstants.district).toString()+"--"+preferenceManager.getDistrict())
                            if (userData?.get(AppConstants.Status)
                                    ?.equals(AppConstants.user) == true &&
                                userData?.get(AppConstants.district).toString() == preferenceManager.getDistrict())
                             {
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
                    binding.recyclerMenu.layoutManager = nlayoutManager
                    binding.recyclerMenu.itemAnimator = DefaultItemAnimator()
                    mHandler.post { binding.recyclerMenu.adapter = propertyAdaptor }
                    propertyAdaptor?.notifyItemRangeInserted(0, propertylist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}