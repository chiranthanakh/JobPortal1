package com.sbd.gbd.Activities.BasicActivitys

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Activities.Propertys.ProductViewHolder
import com.sbd.gbd.Activities.Propertys.Products
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Adapters.BusinessAdaptor
import com.sbd.gbd.Adapters.ConstructorAdaptor
import com.sbd.gbd.Adapters.PropertyAdaptor
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.Model.ConstructionModel
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.Admin.AdminNewPropurtyActivity
import java.util.Collections
import java.util.Locale


class SearchActivity : AppCompatActivity(), View.OnClickListener {
    var adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder>? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var back_toolbar_search: ImageView? = null
    var mHandler = Handler()
    var propertylist: ArrayList<String> = ArrayList<String>()
    var Propertyfilterlist: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var greenlandlist: ArrayList<String> = ArrayList<String>()
    var siteslist: ArrayList<String> = ArrayList<String>()
    var Homeslist: ArrayList<String> = ArrayList<String>()
    var Rentallist: ArrayList<String> = ArrayList<String>()
    var adslist: ArrayList<AdsModel> = ArrayList<AdsModel>()
    var constructionList: ArrayList<ConstructionModel> = ArrayList<ConstructionModel>()
    var businesslist = ArrayList<BusinessModel>()
    var businesslistFiltered = ArrayList<BusinessModel>()
    var constlistFiltered = ArrayList<ConstructionModel>()
    private var constructionAdaptor: ConstructorAdaptor? = null
    var propertyAdaptor: PropertyAdaptor? = null
    var businessAdaptor: BusinessAdaptor? = null
    var adsAdaptor: AdsAdaptor? = null
    var recyclarviewads: RecyclerView? = null
    var edt_filter: SearchView? = null
    var buttonToggleGroup: MaterialButtonToggleGroup? = null
    var filterarraylist: ArrayList<String> = ArrayList<String>()
    var type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.JobPortaltheam) //default app theme
            //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.JobPortaltheam) //default app theme
        }
       /* if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.app_blue)
        }*/
        val bundle = intent.extras
        if (bundle != null) {
            type = bundle.getString("searchtype", "")
        }
        initilize()
    }

    private fun initilize() {
        recyclarviewads = findViewById(R.id.rv_adds_layots2);
        edt_filter = findViewById(R.id.edt_filter)
        recyclerView = findViewById(R.id.recycler_search_results)
        back_toolbar_search = findViewById(R.id.back_toolbar_search)
        buttonToggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.buttonToggleGroup)
        back_toolbar_search?.setOnClickListener(this)
        edt_filter?.requestFocus()
        edt_filter?.setFocusable(true)
        recyclerView?.setHasFixedSize(true)
        val mgrid = GridLayoutManager(this, 1)
        recyclerView?.setLayoutManager(mgrid)
        if (type == "business") {
            AsyncTask.execute { fetchbusiness() }
        } else if (type == "const") {
            AsyncTask.execute { fetchConstruction() }
        } else {
            AsyncTask.execute { fetchpropertys() }
        }

        buttonToggleGroup?.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        type = "property"
                        propertylist.clear()
                        businesslist.clear()
                        AsyncTask.execute { fetchpropertys() }
                    }

                    R.id.button2 -> {
                        type = "business"
                        propertylist.clear()
                        businesslist.clear()
                        AsyncTask.execute { fetchbusiness() }
                    }

                    R.id.button3 -> {
                        type = "const"
                        propertylist.clear()
                        businesslist.clear()
                        AsyncTask.execute { fetchConstruction() }
                    }
                }
            } else {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        edt_filter!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                filter(s)
                return false
            }
        })
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
                    adsAdaptor = AdsAdaptor(adslist, this@SearchActivity)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@SearchActivity, RecyclerView.HORIZONTAL, false)
                    recyclarviewads!!.layoutManager = n1layoutManager
                    recyclarviewads!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post {
                        recyclarviewads!!.adapter = adsAdaptor
                        adsAdaptor!!.notifyItemRangeInserted(0, adslist.size)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchpropertys() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("Products").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    propertylist.clear()
                    Propertyfilterlist.clear()
                    for (key in dataMap!!.keys) {
                        Log.d("testflight",key)
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            Propertyfilterlist.add(
                                FilterModel(
                                    userData!![AppConstants.pname].toString(),
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
                                    userData[AppConstants.number].toString()
                                )
                            )
                            propertylist.add(
                                userData[AppConstants.image].toString() + "!!" + userData[AppConstants.pid] + "---" + userData[AppConstants.description] + "---" +
                                        userData[AppConstants.category] + "---" + userData[AppConstants.price] + "---" + userData[AppConstants.pname]
                                        + "---" + userData[AppConstants.propertysize] + "---" + userData[AppConstants.location] + "---" + userData[AppConstants.number] + "---" + userData[AppConstants.type] + "---" + userData[AppConstants.Status]
                            )

                        } catch (cce: ClassCastException) {
                            try {

                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    propertyAdaptor = PropertyAdaptor(propertylist, this@SearchActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                    recyclerView!!.layoutManager = nlayoutManager
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                    propertyAdaptor!!.notifyItemRangeInserted(0, propertylist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchbusiness() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("BusinessListing").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            businesslist.add(
                                BusinessModel(
                                    userData!![AppConstants.pid].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.time].toString(),
                                    userData["Businessname"].toString(),
                                    userData["products"].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData[AppConstants.description].toString(),
                                    userData[AppConstants.price].toString(),
                                    userData[AppConstants.location].toString(),
                                    userData[AppConstants.number].toString(),
                                    userData["owner"].toString(),
                                    userData["email"].toString(),
                                    userData["rating"].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.image2].toString(),
                                    userData[AppConstants.Status].toString(),
                                    userData["gst"].toString(),
                                    userData["from"].toString(),
                                    userData["productServicess"].toString(),
                                    userData["workingHrs"].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {

                        }
                    }
                    businessAdaptor = BusinessAdaptor(businesslist, this@SearchActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                    recyclerView!!.layoutManager = nlayoutManager
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { recyclerView!!.adapter = businessAdaptor }
                    businessAdaptor!!.notifyItemRangeInserted(0, businesslist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchConstruction() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("constructionforyou").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            constructionList.add(
                                ConstructionModel(
                                    userData!![AppConstants.pid].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.time].toString(),
                                    userData["name"].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData["cost"].toString(),
                                    userData["number1"].toString(),
                                    userData["product_services"].toString(),
                                    userData["experience"].toString(),
                                    userData["servicess1"].toString(),
                                    userData["servicess2"].toString(),
                                    userData["servicess3"].toString(),
                                    userData["servicess4"].toString(),
                                    userData["discription"].toString(),
                                    userData[AppConstants.verified].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.image2].toString(),
                                    userData["owner"].toString(),
                                    userData["address"].toString(),
                                    userData[AppConstants.Status].toString(),
                                    userData["gst"].toString(),
                                    userData["workingHrs"].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {
                            try {

                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    // Upcoming Event
                    constructionAdaptor = ConstructorAdaptor(constructionList, this@SearchActivity)
                    val elayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                    recyclerView?.layoutManager = GridLayoutManager(this@SearchActivity, 1)
                    recyclerView?.itemAnimator = DefaultItemAnimator()
                    recyclerView?.itemAnimator = DefaultItemAnimator()
                    mHandler.post { recyclerView?.adapter = constructionAdaptor }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_add_property -> {
                val intent = Intent(this@SearchActivity, AdminNewPropurtyActivity::class.java)
                startActivity(intent)
            }

            R.id.iv_sites -> {
                propertyAdaptor = PropertyAdaptor(siteslist, this@SearchActivity)
                val nlayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, siteslist.size)
            }

            R.id.iv_home -> {
                propertyAdaptor = PropertyAdaptor(Homeslist, this@SearchActivity)
                val nlayoutManager1: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager1
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Homeslist.size)
            }

            R.id.iv_commercial -> {
                propertyAdaptor = PropertyAdaptor(Rentallist, this@SearchActivity)
                val nlayoutManager2: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager2
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Rentallist.size)
            }

            R.id.iv_green_land -> {
                propertyAdaptor = PropertyAdaptor(greenlandlist, this@SearchActivity)
                val nlayoutManager3: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager3
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, greenlandlist.size)
            }

            R.id.back_toolbar_search -> finish()
        }
    }

    private fun filter(text: String) {
        if (type.equals("business")) {
            if (text == "") {
            } else {
                businesslistFiltered.clear()
                for (i in businesslist.indices) {
                    if (businesslist[i].Businessname!!.contains(text.lowercase(Locale.getDefault())) ||
                        businesslist[i].Business_category!!.contains(text.lowercase(Locale.getDefault())) ||
                        businesslist[i].location!!.contains(text.lowercase(Locale.getDefault()))
                    ) {
                        businesslistFiltered.add(
                            BusinessModel(
                                businesslist[i].pid,
                                businesslist[i].date,
                                businesslist[i].time,
                                businesslist[i].Businessname,
                                businesslist[i].Business_category,
                                businesslist[i].category,
                                businesslist[i].description,
                                businesslist[i].price,
                                businesslist[i].location,
                                businesslist[i].number,
                                businesslist[i].owner,
                                businesslist[i].email,
                                businesslist[i].rating,
                                businesslist[i].image,
                                businesslist[i].image2,
                                businesslist[i].status,
                                businesslist[i].gst,
                                businesslist[i].from,
                                businesslist[i].productServicess,
                                businesslist[i].workingHrs
                            )
                        )
                    }
                }
                businessAdaptor = BusinessAdaptor(businesslistFiltered, this@SearchActivity)
                val nlayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                recyclerView?.layoutManager = nlayoutManager
                recyclerView?.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = businessAdaptor }
                businessAdaptor!!.notifyItemRangeInserted(0, businesslistFiltered.size)
            }
        } else if (type.equals("const")) {
            if (text == "") {
            } else {
                constlistFiltered.clear()
                for (i in constructionList.indices) {
                    if (constructionList[i]?.name!!.contains(text.lowercase(Locale.getDefault())) ||
                        constructionList[i]?.category!!.contains(text.lowercase(Locale.getDefault())) ||
                        constructionList[i]?.address!!.contains(text.lowercase(Locale.getDefault()))
                    ) {
                        constlistFiltered.add(
                            ConstructionModel(
                                constructionList[i]?.pid,
                                constructionList[i]?.saveCurrentDate,
                                constructionList[i]?.saveCurrentTime,
                                constructionList[i]?.name,
                                constructionList[i]?.category,
                                constructionList[i]?.cost,
                                constructionList[i]?.contactDetails,
                                constructionList[i]?.product_services,
                                constructionList[i]?.experience,
                                constructionList[i]?.service1,
                                constructionList[i]?.service2,
                                constructionList[i]?.service3,
                                constructionList[i]?.service4,
                                constructionList[i]?.description,
                                constructionList[i]?.verified,
                                constructionList[i]?.image,
                                constructionList[i]?.image2,
                                constructionList[i]?.owner,
                                constructionList[i]?.address,
                                constructionList[i]?.status,
                                constructionList[i]?.gst,
                                constructionList[i]?.workingHrs
                            )
                        )
                    }
                }
                constructionAdaptor = ConstructorAdaptor(constlistFiltered, this@SearchActivity)
                val elayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                recyclerView?.layoutManager = GridLayoutManager(this@SearchActivity, 1)
                recyclerView?.itemAnimator = DefaultItemAnimator()
                recyclerView?.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView?.adapter = constructionAdaptor }
            }
        } else {
            Log.d("insidebbusiness2", "property")
            filterarraylist.clear()
            for (i in Propertyfilterlist.indices) {
                if (Propertyfilterlist[i].location.lowercase(Locale.getDefault())
                        .contains(text.lowercase(Locale.getDefault())) ||
                    Propertyfilterlist[i].pname.lowercase(Locale.getDefault())
                        .contains(text.lowercase(Locale.getDefault())) ||
                    Propertyfilterlist[i].price.lowercase(Locale.getDefault())
                        .contains(text.lowercase(Locale.getDefault())) ||
                    Propertyfilterlist[i].type.lowercase(Locale.getDefault())
                        .contains(text.lowercase(Locale.getDefault()))
                ) {
                    filterarraylist.add(
                        Propertyfilterlist[i]
                            .image + "!!" + Propertyfilterlist[i]
                            .pid + "---" + Propertyfilterlist[i].description + "---" +
                                Propertyfilterlist[i].category + "---" + Propertyfilterlist[i]!!
                            .price + "---" + Propertyfilterlist[i].pname
                                + "---" + Propertyfilterlist[i]
                            .size + "---" + Propertyfilterlist[i]
                            .location + "---" + Propertyfilterlist[i]
                            .number + "---" + Propertyfilterlist[i].type
                    )
                }
                propertyAdaptor = if (filterarraylist.size == 0) {
                    PropertyAdaptor(Propertyfilterlist, this@SearchActivity)
                } else {
                    PropertyAdaptor(filterarraylist, this@SearchActivity)
                }
            }
            propertyAdaptor = PropertyAdaptor(filterarraylist, this@SearchActivity)
            val nlayoutManager3: RecyclerView.LayoutManager =
                LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
            recyclerView!!.layoutManager = nlayoutManager3
            recyclerView!!.itemAnimator = DefaultItemAnimator()
            mHandler.post { recyclerView!!.adapter = propertyAdaptor }
            propertyAdaptor!!.notifyItemRangeInserted(0, greenlandlist.size)
        }
    }
}