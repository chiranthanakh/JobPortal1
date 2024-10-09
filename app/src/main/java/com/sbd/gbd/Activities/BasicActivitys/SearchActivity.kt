package com.sbd.gbd.Activities.BasicActivitys

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener
import com.sbd.gbd.Activities.Admin.Admin_ads_dashboard
import com.sbd.gbd.Activities.Dashboard.StartingActivity
import com.sbd.gbd.Activities.Propertys.PropertyLayoutFragment
import com.sbd.gbd.Activities.Propertys.PropertySitesFragment
import com.sbd.gbd.Utilitys.UtilityMethods.districtList
import com.sbd.gbd.Utilitys.UtilityMethods.getDistricts
import com.sbd.gbd.Utilitys.UtilityMethods.locationMap
import com.sbd.gbd.databinding.ActivityLocationSelectBinding
import com.sbd.gbd.databinding.ActivitySearchBinding
import java.util.Collections
import java.util.Locale


class SearchActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySearchBinding
    var adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder>? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var back_toolbar_search: ImageView? = null
    var mHandler = Handler()
    var propertylist: ArrayList<String> = ArrayList()
    var Propertyfilterlist: ArrayList<FilterModel> = ArrayList()
    var greenlandlist: ArrayList<String> = ArrayList()
    var adslist: ArrayList<AdsModel> = ArrayList()
    var constructionList: ArrayList<ConstructionModel> = ArrayList()
    var businesslist = ArrayList<BusinessModel>()
    var businesslistFiltered = ArrayList<BusinessModel>()
    var constlistFiltered = ArrayList<ConstructionModel>()
    private var constructionAdaptor: ConstructorAdaptor? = null
    var propertyAdaptor: PropertyAdaptor? = null
    var businessAdaptor: BusinessAdaptor? = null
    var adsAdaptor: AdsAdaptor? = null
    var recyclarviewads: RecyclerView? = null
    var P_Type = ""
    var min_price = 0
    var max_price = 0
    var plot_type = ""
    var noBedroom = ""
    var place = ""
    var bundle = Bundle()
    private var districtList = mutableSetOf<String>()
    private var districtAdapter: ArrayAdapter<*>? = null
    var buttonToggleGroup: MaterialButtonToggleGroup? = null
    var filterarraylist: ArrayList<FilterModel> = ArrayList()
    var type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            setTheme(R.style.JobPortaltheam)
//        } else {
//            setTheme(R.style.JobPortaltheam)
//        }
//        val bundle = intent.extras
//        if (bundle != null) {
//            type = bundle.getString("searchtype", "")
//        }
        getlocations()
        initilize()
    }

    private fun initilize() {

        binding.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    val list = districtList.toList()
                    place = list[p2]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.propertyType.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let {
                P_Type = it.text.toString()
                if (it.text.toString() == "Flat") {
                    binding.tvPlot.visibility = View.GONE
                    binding.cgPlotType.visibility = View.GONE
                    binding.tvBedroom.visibility = View.VISIBLE
                    binding.cgBedroom.visibility = View.VISIBLE
                } else if (it.text.toString() == "Plots") {
                    binding.tvPlot.visibility = View.VISIBLE
                    binding.cgPlotType.visibility = View.VISIBLE
                    binding.tvBedroom.visibility = View.GONE
                    binding.cgBedroom.visibility = View.GONE
                }else if (it.text.toString() == "House / Villa") {
                    binding.tvPlot.visibility = View.GONE
                    binding.cgPlotType.visibility = View.GONE
                    binding.tvBedroom.visibility = View.VISIBLE
                    binding.cgBedroom.visibility = View.VISIBLE
                }else if (it.text.toString() == "Commercial") {
                    binding.tvPlot.visibility = View.GONE
                    binding.cgPlotType.visibility = View.GONE
                    binding.tvBedroom.visibility = View.GONE
                    binding.cgBedroom.visibility = View.GONE
                } else {

                }
            }
        }

        binding.cgPlotType.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let {
                plot_type = it.text.toString()
            }
        }

        binding.cgBedroom.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let {
                noBedroom = it.text.toString()
            }
        }

        binding.llSearch.setOnClickListener{
            val intent = Intent(this@SearchActivity, SearchResultActivity::class.java)
            bundle.putString("pType",P_Type)
            bundle.putString("plotType",plot_type)
            bundle.putInt("minPrice",min_price)
            bundle.putInt("maxPrice",max_price)
            bundle.putString("bedroom", noBedroom)
            bundle.putString("place", place)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        binding.doubleRangeSeekbar.setOnRangeSeekBarViewChangeListener(object : OnDoubleValueSeekBarChangeListener{
            override fun onStartTrackingTouch(
                seekBar: DoubleValueSeekBarView?, min: Int, max: Int
            ) {}

            override fun onStopTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {
                binding.tvPriceRange.text = "Price Range : ₹${min} to ₹${max} /Sqft"
                min_price = min

            }

            override fun onValueChanged(
                seekBar: DoubleValueSeekBarView?, min: Int, max: Int, fromUser: Boolean
            ) {}

        })

        recyclarviewads = findViewById(R.id.rv_adds_layots2);
        recyclerView = findViewById(R.id.recycler_search_results)
        back_toolbar_search = findViewById(R.id.back_toolbar_search)
        back_toolbar_search?.setOnClickListener(this)      //  edt_filter = findViewById(R.id.edt_filter)

        //edt_filter?.requestFocus()
        //edt_filter?.setFocusable(true)
        recyclerView?.setHasFixedSize(true)
        val mgrid = GridLayoutManager(this, 1)
        recyclerView?.setLayoutManager(mgrid)



    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_add_property -> {
                val intent = Intent(this, Admin_ads_dashboard::class.java)
                intent.putExtra("page", "2")
                startActivity(intent)
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
                    if (businesslist[i].Businessname!!.lowercase().contains(text.lowercase(Locale.getDefault())) ||
                        businesslist[i].Business_category!!.lowercase().contains(text.lowercase(Locale.getDefault())) ||
                        businesslist[i].location!!.lowercase().contains(text.lowercase(Locale.getDefault()))
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
                    if (constructionList[i].name!!.lowercase().contains(text.lowercase(Locale.getDefault())) ||
                        constructionList[i].category!!.lowercase().contains(text.lowercase(Locale.getDefault())) ||
                        constructionList[i].address!!.lowercase().contains(text.lowercase(Locale.getDefault()))
                    ) {
                        constlistFiltered.add(
                            ConstructionModel(
                                constructionList[i].pid,
                                constructionList[i].saveCurrentDate,
                                constructionList[i].saveCurrentTime,
                                constructionList[i].name,
                                constructionList[i].category,
                                constructionList[i].cost,
                                constructionList[i].contactDetails,
                                constructionList[i].product_services,
                                constructionList[i].experience,
                                constructionList[i].service1,
                                constructionList[i].service2,
                                constructionList[i].service3,
                                constructionList[i].service4,
                                constructionList[i].description,
                                constructionList[i].verified,
                                constructionList[i].image,
                                constructionList[i].image2,
                                constructionList[i].owner,
                                constructionList[i].address,
                                constructionList[i].status,
                                constructionList[i].gst,
                                constructionList[i].workingHrs
                            )
                        )
                    }
                }
                constructionAdaptor = ConstructorAdaptor(constlistFiltered, this@SearchActivity)
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
                if (Propertyfilterlist[i].location?.lowercase(Locale.getDefault())!!.contains(text.lowercase(Locale.getDefault())) ||
                    Propertyfilterlist[i].pname?.lowercase(Locale.getDefault())
                        !!.contains(text.lowercase(Locale.getDefault())) ||
                    Propertyfilterlist[i].price?.lowercase(Locale.getDefault())
                        !!.contains(text.lowercase(Locale.getDefault())) ||
                    Propertyfilterlist[i].type?.lowercase(Locale.getDefault())
                        !!.contains(text.lowercase(Locale.getDefault()))
                ) {

                    filterarraylist.add(FilterModel( Propertyfilterlist[i].pname,Propertyfilterlist[i].description,Propertyfilterlist[i].price,
                        Propertyfilterlist[i].image,Propertyfilterlist[i].category,Propertyfilterlist[i].pid,AppConstants.date, AppConstants.time,
                        Propertyfilterlist[i].type,Propertyfilterlist[i].size,Propertyfilterlist[i].location,Propertyfilterlist[i].number,Propertyfilterlist[i].status.toString()
                    ))
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

    private fun getlocations() {
        val myDataRef = FirebaseDatabase.getInstance().reference.child(AppConstants.locations)
        myDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = dataSnapshot.value as HashMap<*, *>?
                districtList.clear()
                districtList.add("Select Place")
                for (key in dataMap?.keys!!) {
                    val data = dataMap[key]
                    try {
                        val userData = data as HashMap<*, *>?
                        districtList.add(userData?.get(AppConstants.district)?.toString() ?: "select District")
                        userData?.get("id")?.let {
                            locationMap.put(userData.get(AppConstants.district).toString(), it.toString())
                        }
                    } catch (_: Exception) {
                    }
                }
                districtAdapter = ArrayAdapter(
                    this@SearchActivity,
                    android.R.layout.simple_spinner_item,
                    districtList.toList()
                )
                districtAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spDistrict.adapter = districtAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value: ${error.message}")
            }
        })
    }

}