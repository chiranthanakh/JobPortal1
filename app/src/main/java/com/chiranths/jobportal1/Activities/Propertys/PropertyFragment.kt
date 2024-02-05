package com.chiranths.jobportal1.Activities.Propertys

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Activities.Admin.Admin_ads_dashboard
import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity
import com.chiranths.jobportal1.Activities.Dashboard.DashboardFragment
import com.chiranths.jobportal1.Adapters.AdsAdaptor
import com.chiranths.jobportal1.Adapters.PropertyAdaptor
import com.chiranths.jobportal1.Model.AdsModel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    var propertylist: ArrayList<String> = ArrayList<String>()
    var greenlandlist: ArrayList<*> = ArrayList<Any?>()
    var siteslist: ArrayList<*> = ArrayList<Any?>()
    var Homeslist: ArrayList<*> = ArrayList<Any?>()
    var Rentallist: ArrayList<*> = ArrayList<Any?>()
    var adslist: ArrayList<AdsModel?> = ArrayList<AdsModel?>()
    var propertyAdaptor: PropertyAdaptor? = null
    var adsAdaptor: AdsAdaptor? = null
    var recyclarviewads: RecyclerView? = null
    var bundle = Bundle()
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
        initilize(view)
        AsyncTask.execute { fetchads() }
    }

    private fun initilize(view: View) {
        btn_add = view.findViewById(R.id.btn_add_property)
        btn_add?.setOnClickListener(this)
        iv_sites = view.findViewById(R.id.iv_sites)
        iv_commercial = view.findViewById(R.id.iv_commercial)
        iv_green_land = view.findViewById(R.id.iv_green_land)
        iv_home = view.findViewById(R.id.iv_home)
        search = view.findViewById(R.id.iv_search)
        iv_sites?.setOnClickListener(this)
        iv_green_land?.setOnClickListener(this)
        iv_commercial?.setOnClickListener(this)
        iv_home?.setOnClickListener(this)
        iv_back_toolbar = view.findViewById(R.id.back_toolbar_property)
        iv_back_toolbar?.setOnClickListener(this)
        recyclarviewads = view.findViewById(R.id.rv_adds_layots2)
        recyclerView = view.findViewById(R.id.recycler_menu)
        recyclerView?.setHasFixedSize(true)
        val mgrid = GridLayoutManager(context, 1)
        recyclerView?.setLayoutManager(mgrid)
        fetchcorosel()
        search?.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            bundle.putString("searchtype", "property")
            intent.putExtras(bundle)
            startActivity(intent)
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
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
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

    private fun fetchcorosel() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("Products")
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
                            propertylist.add(
                                userData!![AppConstants.image].toString() + "!!" + userData[AppConstants.pid] + "---" + userData[AppConstants.description] + "---" +
                                        userData[AppConstants.category] + "---" + userData[AppConstants.price] + "---" + userData[AppConstants.pname]
                                        + "---" + userData[AppConstants.propertysize] + "---" + userData[AppConstants.location] + "---" + userData[AppConstants.number] + "---" + userData[AppConstants.type]
                            )

                            /*if(userData.get(AppConstants.type).equals("sites")){
                                siteslist.add(userData.get(AppConstants.type)+"!!"+userData.get(AppConstants.pid)+"---"+userData.get(AppConstants.description)+"---"+
                                        userData.get(AppConstants.category)+"---"+userData.get(AppConstants.price)+"---"+userData.get(AppConstants.pname)
                                        +"---"+userData.get(AppConstants.propertysize)+"---"+userData.get(AppConstants.location)+"---"+userData.get(AppConstants.number)+"---"+userData.get(AppConstants.type));

                            }else if(userData.get(AppConstants.type).equals("homes")){
                                Homeslist.add(userData.get(AppConstants.image)+"!!"+userData.get(AppConstants.pid)+"---"+userData.get(AppConstants.description)+"---"+
                                        userData.get(AppConstants.category)+"---"+userData.get(AppConstants.price)+"---"+userData.get(AppConstants.pname)
                                        +"---"+userData.get(AppConstants.propertysize)+"---"+userData.get(AppConstants.location)+"---"+userData.get(AppConstants.number)+"---"+userData.get(AppConstants.type));

                            }else if(userData.get(AppConstants.type).equals("greenland")){
                                greenlandlist.add(userData.get(AppConstants.image)+"!!"+userData.get(AppConstants.pid)+"---"+userData.get(AppConstants.description)+"---"+
                                        userData.get(AppConstants.category)+"---"+userData.get(AppConstants.price)+"---"+userData.get(AppConstants.pname)
                                        +"---"+userData.get(AppConstants.propertysize)+"---"+userData.get(AppConstants.location)+"---"+userData.get(AppConstants.number)+"---"+userData.get(AppConstants.type));

                            }else if(userData.get(AppConstants.type).equals("rental")){
                                Rentallist.add(userData.get(AppConstants.image)+"!!"+userData.get(AppConstants.pid)+"---"+userData.get(AppConstants.description)+"---"+
                                        userData.get(AppConstants.category)+"---"+userData.get(AppConstants.price)+"---"+userData.get(AppConstants.pname)
                                        +"---"+userData.get(AppConstants.propertysize)+"---"+userData.get(AppConstants.location)+"---"+userData.get(AppConstants.number)+"---"+userData.get(AppConstants.type));

                            };*/
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    propertyAdaptor = PropertyAdaptor(propertylist, context)
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
                val intent = Intent(context, Admin_ads_dashboard::class.java)
                intent.putExtra("page", "2")
                startActivity(intent)
            }

            R.id.iv_sites -> {
                propertyAdaptor = PropertyAdaptor(siteslist, context)
                val nlayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, siteslist.size)
            }

            R.id.iv_home -> {
                propertyAdaptor = PropertyAdaptor(Homeslist, context)
                val nlayoutManager1: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager1
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Homeslist.size)
            }

            R.id.iv_commercial -> {
                propertyAdaptor = PropertyAdaptor(Rentallist, context)
                val nlayoutManager2: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager2
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, Rentallist.size)
            }

            R.id.iv_green_land -> {
                propertyAdaptor = PropertyAdaptor(greenlandlist, context)
                val nlayoutManager3: RecyclerView.LayoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = nlayoutManager3
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = propertyAdaptor }
                propertyAdaptor!!.notifyItemRangeInserted(0, greenlandlist.size)
            }

            R.id.back_toolbar_property -> {
                    val fragmentManager = requireActivity().supportFragmentManager
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, startingFragment).commit()
            }
        }
    }
}
