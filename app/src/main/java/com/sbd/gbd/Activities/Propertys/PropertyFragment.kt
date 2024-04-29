package com.sbd.gbd.Activities.Propertys

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Activities.Admin.Admin_ads_dashboard
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Activities.Dashboard.DashboardFragment
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Adapters.PropertyAdaptor
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.BasicActivitys.OtpLoginActivity
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.Utilitys.UtilityMethods
import java.util.Collections

class PropertyFragment : Fragment(), View.OnClickListener {
    private var ProductsRef: DatabaseReference? = null
    var adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder>? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var btn_add: Button? = null
    var search: ImageView? = null
    var iv_back_toolbar: ImageView? = null
    var iv_sites: ImageView? = null
    var iv_green_land: ImageView? = null
    var iv_home: ImageView? = null
    var iv_commercial: ImageView? = null
    var mHandler = Handler()
    var iv_no_internet: ImageView? = null
    var business_layout : LinearLayout? = null
    var propertylist: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var greenlandlist: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var siteslist: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var Homeslist: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var Rentallist: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var adslist: ArrayList<AdsModel?> = ArrayList<AdsModel?>()
    var buttonToggleGroup: MaterialButtonToggleGroup? = null
    var propertyAdaptor: PropertyAdaptor? = null
    var ll_assetRecyclrView : LinearLayout? = null
    var ll_asset : LinearLayout? = null
    var adsAdaptor: AdsAdaptor? = null
    var recyclarviewads: RecyclerView? = null
    var bundle = Bundle()
    lateinit var preferenceManager:PreferenceManager
    var startingFragment = DashboardFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_property, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        if (UtilityMethods.isNetworkAvailable(requireContext())){
            initilize(view)
            AsyncTask.execute { fetchads() }
        }else{
            iv_no_internet = view.findViewById(R.id.iv_no_internet)
            business_layout = view.findViewById(R.id.business_layout)
            iv_no_internet?.visibility = View.VISIBLE
            business_layout?.visibility = View.GONE
            UtilityMethods.showToast(requireContext(),"Please check your internet connection")
        }
    }

    private fun initilize(view: View) {
        preferenceManager= PreferenceManager(requireContext());
        btn_add = view.findViewById(R.id.btn_add_property)
        btn_add?.setOnClickListener(this)
        iv_sites = view.findViewById(R.id.iv_sites)
        iv_commercial = view.findViewById(R.id.iv_commercial)
        iv_green_land = view.findViewById(R.id.iv_green_land)
        iv_home = view.findViewById(R.id.iv_home)
        ll_asset = view.findViewById(R.id.ll_assetText)
        ll_assetRecyclrView = view.findViewById(R.id.ll_assetRecyclrView)
        search = view.findViewById(R.id.iv_search)
        iv_sites?.setOnClickListener(this)
        iv_green_land?.setOnClickListener(this)
        iv_commercial?.setOnClickListener(this)
        iv_home?.setOnClickListener(this)
        iv_back_toolbar = view.findViewById(R.id.back_toolbar_property)
        iv_back_toolbar?.setOnClickListener(this)
        recyclarviewads = view.findViewById(R.id.rv_adds_layots2)
        recyclerView = view.findViewById(R.id.recycler_menu)
        buttonToggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.buttonToggleGroup)
        recyclerView?.setHasFixedSize(true)
        val mgrid = GridLayoutManager(context, 1)
        recyclerView?.setLayoutManager(mgrid)
        fetchproducts("")
        search?.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            bundle.putString("searchtype", "property")
            intent.putExtras(bundle)
            startActivity(intent)
        })

        buttonToggleGroup?.addOnButtonCheckedListener { _, checkedId, isChecked ->
            val selectedButton = view.findViewById<MaterialButton>(checkedId)
            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        fetchproducts("Site")
                        ll_asset?.visibility = View.GONE
                        ll_assetRecyclrView?.visibility = View.GONE
                    }

                    R.id.button2 -> {
                        fetchproducts("Green Land")
                        ll_asset?.visibility = View.GONE
                        ll_assetRecyclrView?.visibility = View.GONE
                    }

                    R.id.button3 -> {
                        fetchproducts("House")
                        ll_asset?.visibility = View.GONE
                        ll_assetRecyclrView?.visibility = View.GONE
                    }

                    R.id.button -> {
                        fetchproducts("Layout")
                        ll_asset?.visibility = View.GONE
                        ll_assetRecyclrView?.visibility = View.GONE
                    }
                }
            } else {

            }
        }

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
                    adsAdaptor = AdsAdaptor(adslist, context)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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
                                    userData[AppConstants.number].toString(),
                                    userData[AppConstants.Status].toString()
                                )                            )
                        }

                        } catch (cce: ClassCastException) {

                        }
                    }
                    propertyAdaptor = PropertyAdaptor(propertylist, requireContext())
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
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
        when (view.id) {
            R.id.btn_add_property -> {
                if (preferenceManager.getLoginState()) {
                    val intent = Intent(requireContext(), Admin_ads_dashboard::class.java)
                    intent.putExtra("page", "2")
                    startActivity(intent)
                } else {
                    UtilityMethods.showToast(requireContext(),"Please Login to process")
                    val intent = Intent(context, OtpLoginActivity::class.java)
                    startActivity(intent)
                }
            }

            R.id.iv_sites -> {
                propertyAdaptor = PropertyAdaptor(siteslist, requireContext())
                val nlayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, siteslist.size)
            }

            R.id.iv_home -> {
                propertyAdaptor = PropertyAdaptor(Homeslist, requireContext())
                val nlayoutManager1: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager1
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Homeslist.size)
            }

            R.id.iv_commercial -> {
                propertyAdaptor = PropertyAdaptor(Rentallist, requireContext())
                val nlayoutManager2: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager2
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Rentallist.size)
            }

            R.id.iv_green_land -> {
                propertyAdaptor = PropertyAdaptor(greenlandlist, requireContext())
                val nlayoutManager3: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager3
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, greenlandlist.size)
            }

            R.id.back_toolbar_property -> {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
                   // fragmentManager.beginTransaction().replace(R.id.fragment_container, startingFragment).commit()
            }
        }
    }
}
