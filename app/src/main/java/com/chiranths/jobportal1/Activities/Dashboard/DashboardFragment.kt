package com.chiranths.jobportal1.Activities.Dashboard

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Activities.Admin.AdminDashboard
import com.chiranths.jobportal1.Activities.BasicActivitys.CenterHomeActivity
import com.chiranths.jobportal1.Activities.BasicActivitys.LivingPlaceActivity
import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity
import com.chiranths.jobportal1.Activities.BasicActivitys.SeeAllLayoutActivity
import com.chiranths.jobportal1.Activities.BasicActivitys.Travelsactivity
import com.chiranths.jobportal1.Activities.BasicActivitys.UpcommingProjects
import com.chiranths.jobportal1.Activities.Businesthings.BusinessFragment
import com.chiranths.jobportal1.Activities.Construction.ConstructionActivity
import com.chiranths.jobportal1.Activities.LoanActivity.LoanActivity
import com.chiranths.jobportal1.Activities.Sell.SellActivity
import com.chiranths.jobportal1.Adapters.AdsAdaptor
import com.chiranths.jobportal1.Adapters.BottomhomeRecyclarviewAdaptor
import com.chiranths.jobportal1.Adapters.CoroselListAdaptor
import com.chiranths.jobportal1.Adapters.LayoutsAdaptor
import com.chiranths.jobportal1.Interface.FragmentInteractionListener
import com.chiranths.jobportal1.Model.AdsModel
import com.chiranths.jobportal1.Model.Corosolmodel
import com.chiranths.jobportal1.Model.LayoutModel
import com.chiranths.jobportal1.Model.ProductInfo
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.chiranths.jobportal1.databinding.DashboardFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synnapps.carouselview.CarouselView
import java.io.IOException
import java.util.Collections
import java.util.Locale

class DashboardFragment : Fragment(), View.OnClickListener, FragmentInteractionListener {
    private lateinit var binding: DashboardFragmentBinding
    var id: String? = null
    var name: String? = ""
    var mail: String? = null
    var pic: String? = null
    var search: EditText? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var bottomhomeRecyclarviewAdaptor: BottomhomeRecyclarviewAdaptor? = null
    var addresses: List<Address>? = null
    val handler = Handler()
    var recyclerView: RecyclerView? = null
    private var coroselListAdaptor: CoroselListAdaptor? = null
    var adsAdaptor: AdsAdaptor? = null
    var layoutsAdaptor: LayoutsAdaptor? = null
    private var reload = false
    var coroselimagelist = ArrayList<Corosolmodel>()
    var adslist: ArrayList<Any?> = ArrayList<Any?>()
    var layoutslists: ArrayList<Any?> = ArrayList<Any?>()
    var productinfolist: ArrayList<ProductInfo> = ArrayList<ProductInfo>()
    var carouselView: CarouselView? = null
    var mHandler = Handler()
    var tv_location: TextView? = null
    var tv_pincode: TextView? = null
    var progressDialog: ProgressDialog? = null
    var admin_btn: TextView? = null
    var bundle = Bundle()

    //var view: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sh = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        id = sh.getString("id", null)
        name = sh.getString("name", "")
        mail = sh.getString("mail", null)
        pic = sh.getString("pic", null)
        progressDialog = ProgressDialog(context)
        //if (!reload) {
        initilize(view)
        //}
    }

    private fun initilize(view: View) {
        reload = true
        tv_location = view.findViewById(R.id.tv_location)
        binding.mainEdtSearch2.setInputType(InputType.TYPE_NULL) // disable soft input
        binding.mainEdtSearch2.setOnClickListener(this)
        binding.tvSeeallLayouts.setOnClickListener(this)
        binding.tvSeeallUpcomming.setOnClickListener(this)
        search?.setOnClickListener(this)
        binding.searchLayout.setOnClickListener(this)
        binding.ivSell.setOnClickListener(this)
        binding.cvLoans.setOnClickListener(this)
        binding.cvServicess1.setOnClickListener(this)
        binding.cvJobs.setOnClickListener(this)
        binding.llConstructions.setOnClickListener(this)
        binding.llHomeRent.setOnClickListener(this)
        binding.llTravels.setOnClickListener(this)
        binding.llCommercialRent.setOnClickListener(this)

        if (progressDialog != null) {
            if (!progressDialog!!.isShowing) {
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.show()
                AsyncTask.execute {
                    fetchcorosel()
                    fetchdata()
                    fetchads()
                    fetchlayouts()
                }
            }
        }
        binding.ivBell.setOnClickListener {
            val intent = Intent(context, AdminDashboard::class.java)
            startActivity(intent)
        }
    }

    private fun fetchcorosel() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("Corosels")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            coroselimagelist.add(
                                Corosolmodel(
                                    userData!![AppConstants.image].toString(),
                                    userData[AppConstants.type].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData[AppConstants.pid].toString(),
                                    userData[AppConstants.pname].toString(),
                                    userData[AppConstants.description].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {
                            //through exception
                        }
                    }
                    if (coroselimagelist.size != 0) {
                        coroselListAdaptor = CoroselListAdaptor(coroselimagelist, context!!)
                        val nlayoutManager: RecyclerView.LayoutManager =
                            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        binding.rvHomeEvent.layoutManager = nlayoutManager
                        binding.rvHomeEvent.itemAnimator = DefaultItemAnimator()
                        binding.rvHomeEvent.onFlingListener = null
                        mHandler.post {
                            binding.rvHomeEvent.adapter = coroselListAdaptor
                        }
                        coroselListAdaptor!!.notifyItemRangeInserted(0, coroselimagelist.size)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
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
                    adsAdaptor = AdsAdaptor(adslist, context)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    binding.rvAddsLayots1.layoutManager = n1layoutManager
                    binding.rvAddsLayots1.itemAnimator = DefaultItemAnimator()
                    mHandler.post {
                        if (progressDialog != null) {
                            if (progressDialog!!.isShowing) {
                                progressDialog!!.dismiss()
                            }
                        }
                        binding.rvAddsLayots1.adapter = adsAdaptor
                        adsAdaptor?.notifyItemRangeInserted(0, adslist.size)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchlayouts() {
        val layouts = FirebaseDatabase.getInstance().reference.child("layoutsforyou")
        layouts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layoutslists.clear()
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            layoutslists.add(
                                LayoutModel(
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
                                    userData[AppConstants.sitesAvailable].toString(),
                                    userData[AppConstants.postedBy].toString(),
                                    userData[AppConstants.facing].toString(),
                                    userData[AppConstants.layoutarea].toString(),
                                    userData[AppConstants.point1].toString(),
                                    userData[AppConstants.point2].toString(),
                                    userData[AppConstants.point3].toString(),
                                    userData[AppConstants.point4].toString(),
                                )
                            )
                        } catch (cce: ClassCastException) {

                        }
                    }
                    Collections.shuffle(layoutslists)
                    layoutsAdaptor = LayoutsAdaptor(layoutslists, context)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    binding.rvLayouts.layoutManager = n1layoutManager
                    binding.rvLayouts.itemAnimator = DefaultItemAnimator()
                    mHandler.post {
                        if (progressDialog != null) {
                            if (progressDialog!!.isShowing) {
                                progressDialog!!.dismiss()
                            }
                        }
                        binding.rvLayouts.adapter = layoutsAdaptor
                        layoutsAdaptor!!.notifyItemRangeInserted(0, adslist.size)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("hotforyou")
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    productinfolist.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            productinfolist.add(
                                ProductInfo(
                                    userData!![AppConstants.category].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.description].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.location].toString(),
                                    userData[AppConstants.number].toString(),
                                    userData[AppConstants.pid].toString(),
                                    userData[AppConstants.pname].toString(),
                                    userData[AppConstants.price].toString(),
                                    userData[AppConstants.propertysize].toString(),
                                    userData[AppConstants.time].toString(),
                                    userData[AppConstants.type].toString(),
                                    userData[AppConstants.postedBy].toString()
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

                    // Upcoming Event
                    bottomhomeRecyclarviewAdaptor =
                        BottomhomeRecyclarviewAdaptor(productinfolist, context!!)
                    val elayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.rvDashProp.layoutManager = GridLayoutManager(context, 1)
                    binding.rvDashProp.itemAnimator = DefaultItemAnimator()
                    mHandler.post { binding.rvDashProp.adapter = bottomhomeRecyclarviewAdaptor }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.cv_jobs -> {
                val intent: Intent
                if (name == "") {

                } else {

                }
            }

            R.id.cv_loans -> {
                val intent2 = Intent(context, LoanActivity::class.java)
                startActivity(intent2)
            }

            R.id.cv_servicess1 -> {
                binding.bottomNavShift.visibility = View.GONE
                val businessFragment = BusinessFragment()
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, businessFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            R.id.iv_sell -> {
                val intent4 = Intent(context, SellActivity::class.java)
                startActivity(intent4)
            }

            R.id.main_edt_search2 -> {
                val intent5 = Intent(context, SearchActivity::class.java)
                bundle.putString("searchtype", "property")
                intent5.putExtras(bundle)
                startActivity(intent5)
            }

            R.id.search_layout -> {
                val intent6 = Intent(context, SearchActivity::class.java)
                bundle.putString("searchtype", "property")
                intent6.putExtras(bundle)
                startActivity(intent6)
            }

            R.id.ll_home_rent -> {
                val intent7 = Intent(context, LivingPlaceActivity::class.java)
                bundle.putString("center", "hotel")
                intent7.putExtras(bundle)
                startActivity(intent7)
            }

            R.id.ll_commercial_rent -> {
                val intent8 = Intent(context, CenterHomeActivity::class.java)
                bundle.putString("center", "commercial")
                intent8.putExtras(bundle)
                startActivity(intent8)
            }

            R.id.ll_travels -> {
                val intent9 = Intent(context, Travelsactivity::class.java)
                bundle.putString("center", "hotel")
                intent9.putExtras(bundle)
                startActivity(intent9)
            }

            R.id.tv_seeall_upcomming -> {
                val intent10 = Intent(context, UpcommingProjects::class.java)
                intent10.putExtras(bundle)
                startActivity(intent10)
            }

            R.id.tv_seeall_layouts -> {
                val intent11 = Intent(context, SeeAllLayoutActivity::class.java)
                intent11.putExtras(bundle)
                startActivity(intent11)
            }

            R.id.ll_constructions -> {
                val intent12 = Intent(context, ConstructionActivity::class.java)
                intent12.putExtras(bundle)
                startActivity(intent12)
            }
        }
    }

    //location fetch
    private fun displayLocationSettingsRequest(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        } else {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    try {
                        addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        // textView.setText(addresses.get(0).getLocality() + addresses.get(0).getPostalCode() + addresses.get(0).getThoroughfare() + addresses.get(0).getSubLocality() + addresses.get(0).getSubLocality());
                        //  tv_location.setText(addresses.get(0).getLocality());
                        //  tv_pincode.setText(addresses.get(0).getSubLocality()+" - "+addresses.get(0).getPostalCode());
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onFragmentInteraction() {
        val businessFragment = BusinessFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, businessFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}