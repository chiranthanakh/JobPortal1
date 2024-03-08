package com.sbd.gbd.Activities.Dashboard

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.bumptech.glide.Glide
import com.sbd.gbd.Activities.Admin.AdminDashboard
import com.sbd.gbd.Activities.HotelsPG.CenterHomeActivity
import com.sbd.gbd.Activities.HomeRentels.LivingPlaceActivity
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Activities.BasicActivitys.SeeAllLayoutActivity
import com.sbd.gbd.Activities.Travels.Travelsactivity
import com.sbd.gbd.Activities.BasicActivitys.UpcommingProjects
import com.sbd.gbd.Activities.Businesthings.BusinessActivity
import com.sbd.gbd.Activities.Businesthings.BusinessFragment
import com.sbd.gbd.Activities.Construction.ConstructionActivity
import com.sbd.gbd.Activities.LoanActivity.LoanActivity
import com.sbd.gbd.Activities.Sell.SellActivity
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Adapters.BottomhomeRecyclarviewAdaptor
import com.sbd.gbd.Adapters.CoroselListAdaptor
import com.sbd.gbd.Adapters.LayoutsAdaptor
import com.sbd.gbd.Interface.FragmentInteractionListener
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.Model.Corosolmodel
import com.sbd.gbd.Model.LayoutModel
import com.sbd.gbd.Model.ProductInfo
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.databinding.DashboardFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.HotDealsactivity.HotDealsDetailsActivity
import com.sbd.gbd.Activities.LoanActivity.LoanForm
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
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
    private var count: Int? = 0
    private var totalCarousel: Int? = 0
    var coroselimagelist = ArrayList<Corosolmodel>()
    var adslist: ArrayList<Any?> = ArrayList<Any?>()
    var layoutslists: ArrayList<Any?> = ArrayList<Any?>()
    var productinfolist: ArrayList<ProductInfo> = ArrayList<ProductInfo>()
    var carouselView: CarouselView? = null
    var mHandler = Handler()
    var tv_location: TextView? = null
    var bundle = Bundle()
    private var url: ArrayList<String> = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        initilize(view)
    }

    private fun initilize(view: View) {
        reload = true
        tv_location = view.findViewById(R.id.tv_location)
        binding.mainEdtSearch2.setInputType(InputType.TYPE_NULL)
        binding.mainEdtSearch2.setOnClickListener(this)
        binding.tvSeeallLayouts.setOnClickListener(this)
        binding.tvSeeallUpcomming.setOnClickListener(this)
        search?.setOnClickListener(this)
        binding.searchLayout.setOnClickListener(this)
        binding.btnPost.setOnClickListener(this)
        binding.cvLoans.setOnClickListener(this)
        binding.llConstructions.setOnClickListener(this)
        binding.llConstructions.setOnClickListener(this)
        binding.llHomeRent.setOnClickListener(this)
        binding.llTravels.setOnClickListener(this)
        binding.llHotels.setOnClickListener(this)

        binding.progressLayout.visibility= View.VISIBLE
        AsyncTask.execute {
            fetchcorosel()
            fetchdata()
            fetchads()
            fetchlayouts()
        }

        if (AppConstants.user.equals("2")) {
            binding.ivBell.visibility = View.GONE
        } else {
            binding.ivBell.setOnClickListener {
                val intent = Intent(context, AdminDashboard::class.java)
                startActivity(intent)
            }
        }

        binding.llBusiness.setOnClickListener {
            val intent = Intent(context, BusinessActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchcorosel() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("Corosels")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    url.clear()
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
                            url.add(userData[AppConstants.image].toString())
                        } catch (cce: ClassCastException) {
                            //through exception
                        }
                    }
                    if (coroselimagelist.size != 0) {
                        binding.caroselDashboard.setImageListener(imageListener)
                        binding.caroselDashboard.pageCount = url.size

                        binding.caroselDashboard.setImageClickListener {
                            carouselClick(coroselimagelist.get(it))
                        }

                        coroselListAdaptor = CoroselListAdaptor(coroselimagelist, context!!)
                        val nlayoutManager: RecyclerView.LayoutManager =
                            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchads() {
        val adsimage = FirebaseDatabase.getInstance().reference.child("adsforyou")
            .orderByChild(AppConstants.Status).equalTo(AppConstants.user)
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
            .orderByChild(AppConstants.Status).equalTo(AppConstants.user)
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
                        binding.rvLayouts.adapter = layoutsAdaptor
                        layoutsAdaptor!!.notifyItemRangeInserted(0, adslist.size)
                        binding.progressLayout.visibility= View.GONE

                    }
                }
                binding.progressLayout.visibility= View.GONE
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchdata() {
        val productsinfo = FirebaseDatabase.getInstance().reference.child("hotforyou")
            .orderByChild(AppConstants.Status).equalTo(AppConstants.user)
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

                        }
                    }
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

            R.id.cv_loans -> {
                val intent2 = Intent(context, LoanActivity::class.java)
                startActivity(intent2)
            }

            R.id.cv_servicess1 -> {
                /*binding.bottomNavShift.visibility = View.GONE
                val businessFragment = BusinessFragment()
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, businessFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()*/
                val intent2 = Intent(context, BusinessActivity::class.java)
                startActivity(intent2)
            }

            R.id.btn_post -> {
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

            R.id.ll_hotels -> {
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

    private fun carouselClick( itemClick : Corosolmodel) {
        if (itemClick.type == "2") {
            val uri = Uri.parse("http://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(intent)
        } else {
            if (itemClick.category == "Layout") {
                val intent = Intent(context, LayoutDetailsActivity::class.java)
                intent.putExtra(AppConstants.pid, itemClick.pid)
                intent.putExtra("page", "3")
                context?.startActivity(intent)
            } else if (itemClick.category == "Loan") {
                val intent = Intent(context, LoanForm::class.java)
                intent.putExtra(AppConstants.pid, itemClick.pid)
                intent.putExtra("page", "3")
                context?.startActivity(intent)
            } else if (itemClick.category == "hotdeals") {
                val intent = Intent(context, HotDealsDetailsActivity::class.java)
                intent.putExtra(AppConstants.pid, itemClick.pid)
                intent.putExtra("page", "3")
                context?.startActivity(intent)
            } else {
                val intent = Intent(context, CoroselDetailsActivity::class.java)
                intent.putExtra(AppConstants.pid, itemClick.pid)
                intent.putExtra(AppConstants.image, itemClick.imageurl)
                intent.putExtra(AppConstants.category, itemClick.category)
                intent.putExtra(AppConstants.pname, itemClick.pname)
                intent.putExtra(AppConstants.description, itemClick.discription)
                intent.putExtra("page", "3")
                context?.startActivity(intent)
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

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@DashboardFragment)
            .load(url[position])
            .into(imageView)
    }
}