package com.sbd.gbd.Activities.Dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sbd.gbd.Activities.Admin.AdminDashboard
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Activities.BasicActivitys.SeeAllLayoutActivity
import com.sbd.gbd.Activities.BasicActivitys.UpcommingProjects
import com.sbd.gbd.Activities.Businesthings.BusinessActivity
import com.sbd.gbd.Activities.Businesthings.BusinessFragment
import com.sbd.gbd.Activities.Construction.ConstructionActivity
import com.sbd.gbd.Activities.HomeRentels.LivingPlaceActivity
import com.sbd.gbd.Activities.HotDealsactivity.HotDealsDetailsActivity
import com.sbd.gbd.Activities.LoanActivity.LoanActivity
import com.sbd.gbd.Activities.LoanActivity.LoanForm
import com.sbd.gbd.Activities.Propertys.PropertyActivity
import com.sbd.gbd.Activities.Sell.SellActivity
import com.sbd.gbd.Activities.Travels.Travelsactivity
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Adapters.BottomhomeRecyclarviewAdaptor
import com.sbd.gbd.Adapters.LayoutsAdaptor
import com.sbd.gbd.BuildConfig
import com.sbd.gbd.Interface.FragmentInteractionListener
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.Model.Corosolmodel
import com.sbd.gbd.Model.LayoutModel
import com.sbd.gbd.Model.ProductInfo
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.FirebaseConnects
import com.sbd.gbd.Utilitys.UtilityMethods
import com.sbd.gbd.databinding.DashboardFragmentBinding
import com.synnapps.carouselview.ImageListener
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

class DashboardFragment : Fragment(), View.OnClickListener, FragmentInteractionListener {
    private lateinit var binding: DashboardFragmentBinding
    var search: EditText? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var bottomhomeRecyclarviewAdaptor: BottomhomeRecyclarviewAdaptor? = null
    var addresses: List<Address>? = null
    var layoutsAdaptor: LayoutsAdaptor? = null
    private var reload = false
    var coroselimagelist = ArrayList<Corosolmodel>()
    var adslist: ArrayList<AdsModel> = ArrayList()
    var layoutslists: ArrayList<LayoutModel> = ArrayList()
    var productinfolist: ArrayList<ProductInfo> = ArrayList()
    var mHandler = Handler()
    var tv_location: TextView? = null
    var bundle = Bundle()
    private var url: ArrayList<String> = ArrayList()


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
        if (UtilityMethods.isNetworkAvailable(requireContext())) {
            initilize(view)
        } else {
            binding.ivNoInternet.visibility = View.VISIBLE
            binding.mainItemsLayout.visibility = View.GONE
            UtilityMethods.showToast(requireContext(),"Please check your internet connection")
        }
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
        binding.llHouses.setOnClickListener(this)
        binding.llSites.setOnClickListener(this)
        binding.llGreenLand.setOnClickListener(this)
        binding.progressLayout.visibility= View.VISIBLE

        lifecycleScope.launch {
            fetchcorosel()
            fetchdata()
            fetchads()
            fetchlayouts()
        }

        if (BuildConfig.BUILD_TYPE == "release" && BuildConfig.FLAVOR == "prod") {
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

        binding.tvBuy.setOnClickListener{
            val intent = Intent(context, LocationSelectActivity::class.java)
            intent.putExtra("page", "1")
            startActivity(intent)
        }
    }

    private fun fetchcorosel() {
        FirebaseConnects.fetchCarosel("Corosels") { dataMap ->
            if (dataMap != null) {
                url.clear()
                for (data in dataMap) {
                    try {
                        val userData = data.value as HashMap<*, *>?
                        coroselimagelist.add(Corosolmodel(
                                userData!![AppConstants.image].toString(),
                                userData[AppConstants.type].toString(),
                                userData[AppConstants.category].toString(),
                                userData[AppConstants.pid].toString(),
                                userData[AppConstants.pname].toString(),
                                userData[AppConstants.description].toString()
                            )
                        )
                        url.add(userData[AppConstants.image].toString())
                    } catch (cce: ClassCastException) { }
                }
                if (coroselimagelist.size != 0 && !coroselimagelist.isNullOrEmpty()) {
                    binding.caroselDashboard.setImageListener(imageListener)
                    binding.caroselDashboard.pageCount = url.size
                    binding.caroselDashboard.setImageClickListener {
                        carouselClick(coroselimagelist.get(it))
                    }
                }
            }
        }
    }



    private fun fetchads() {
        lifecycleScope.launch {
            FirebaseConnects.fetchDataWithStatus(AppConstants.ads) { dataMap ->
                if (dataMap != null) {
                    adslist.clear()
                    var index = 0
                    for (data in dataMap) {
                        if (index < 10) {
                            index++
                            try {
                                val userData = data.value as HashMap<*, *>?
                                adslist.add(
                                    AdsModel(
                                        userData?.get(AppConstants.image).toString(),
                                        userData?.get(AppConstants.image2).toString(),
                                        userData?.get(AppConstants.pid).toString(),
                                        userData?.get(AppConstants.description).toString(),
                                        userData?.get(AppConstants.date).toString(),
                                        userData?.get(AppConstants.category).toString(),
                                        userData?.get(AppConstants.price).toString(),
                                        userData?.get(AppConstants.pname).toString(),
                                        userData?.get(AppConstants.propertysize).toString(),
                                        userData?.get(AppConstants.location).toString(),
                                        userData?.get(AppConstants.number).toString(),
                                        userData?.get(AppConstants.Status).toString(),
                                        userData?.get(AppConstants.postedBy).toString(),
                                        userData?.get(AppConstants.approvedBy).toString(),
                                        userData?.get(AppConstants.facing).toString(),
                                        userData?.get(AppConstants.ownership).toString(),
                                        userData?.get(AppConstants.postedOn).toString(),
                                        userData?.get(AppConstants.verified).toString(),
                                        userData?.get(AppConstants.katha).toString(),
                                        userData?.get(AppConstants.text1).toString(),
                                        userData?.get(AppConstants.text2).toString(),
                                        userData?.get(AppConstants.text3).toString(),
                                        userData?.get(AppConstants.text4).toString(),
                                        userData?.get(AppConstants.city).toString()
                                    )
                                )
                            } catch (_: ClassCastException) {
                            }
                    }
                    }
                    adslist.shuffle()
                    val adsAdaptor = AdsAdaptor(adslist, context)
                    val n1layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    binding.rvAddsLayots1 .layoutManager = n1layoutManager
                    binding.rvAddsLayots1.adapter = adsAdaptor.apply { notifyDataSetChanged() }
                } else {
                }
            }
        }
    }

    private fun fetchlayouts() {
        FirebaseConnects.fetchDataWithLayout(AppConstants.layouts,AppConstants.layoutsname) { dataMap ->
            if (dataMap != null) {
                    layoutslists.clear()
                var index = 0
                    for (data in dataMap) {
                        if (index < 10) {
                            index++
                            try {
                                val userData = data.value as HashMap<*, *>?
                                if (userData?.get(AppConstants.Status).toString()
                                        .equals(AppConstants.user)
                                ) {
                                    layoutslists.add(
                                        LayoutModel(
                                            userData?.get(AppConstants.image).toString(),
                                            userData?.get(AppConstants.image2).toString(),
                                            userData?.get(AppConstants.pid).toString(),
                                            userData?.get(AppConstants.description).toString(),
                                            userData?.get(AppConstants.date).toString(),
                                            userData?.get(AppConstants.type).toString(),
                                            userData?.get(AppConstants.price).toString(),
                                            userData?.get(AppConstants.pname).toString(),
                                            userData?.get(AppConstants.propertysize).toString(),
                                            userData?.get(AppConstants.location).toString(),
                                            userData?.get(AppConstants.number).toString(),
                                            userData?.get(AppConstants.Status).toString(),
                                            userData?.get(AppConstants.sitesAvailable).toString(),
                                            userData?.get(AppConstants.postedBy).toString(),
                                            userData?.get(AppConstants.facing).toString(),
                                            userData?.get(AppConstants.layoutarea).toString(),
                                            userData?.get(AppConstants.point1).toString(),
                                            userData?.get(AppConstants.point2).toString(),
                                            userData?.get(AppConstants.point3).toString(),
                                            userData?.get(AppConstants.point4).toString(),
                                        )
                                    )
                                }
                            } catch (_: ClassCastException) {
                            }
                        }
                    }
                    layoutslists.shuffle()
                   if( isAdded) {
                       val layoutsAdaptor = LayoutsAdaptor(layoutslists, requireContext())
                       val n1layoutManager =
                           LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                       binding.rvLayouts.layoutManager = n1layoutManager
                       binding.rvLayouts.adapter = layoutsAdaptor.apply { notifyDataSetChanged() }
                       binding.progressLayout.visibility = View.GONE
                   }
                }
                binding.progressLayout.visibility= View.GONE
            }
    }

    private fun fetchdata() {
        FirebaseConnects.fetchDataWithStatus(AppConstants.hotdeals) { dataMap ->
            if (dataMap != null) {
                    productinfolist.clear()
                    for (data in dataMap) {
                        try {
                            val userData = data.value as HashMap<*, *>?
                            productinfolist.add(
                                ProductInfo(
                                    userData?.get(AppConstants.category).toString(),
                                    userData?.get(AppConstants.date).toString(),
                                    userData?.get(AppConstants.description).toString(),
                                    userData?.get(AppConstants.image).toString(),
                                    userData?.get(AppConstants.location).toString(),
                                    userData?.get(AppConstants.number).toString(),
                                    userData?.get(AppConstants.pid).toString(),
                                    userData?.get(AppConstants.pname).toString(),
                                    userData?.get(AppConstants.price).toString(),
                                    userData?.get(AppConstants.propertysize).toString(),
                                    userData?.get(AppConstants.time).toString(),
                                    userData?.get(AppConstants.type).toString(),
                                    userData?.get(AppConstants.postedBy).toString()
                                )
                            )
                        } catch (_: ClassCastException) { }
                    }
                    bottomhomeRecyclarviewAdaptor =
                        BottomhomeRecyclarviewAdaptor(productinfolist, context)
                    binding.rvDashProp.layoutManager = GridLayoutManager(context, 1)
                    mHandler.post { binding.rvDashProp.adapter = bottomhomeRecyclarviewAdaptor }
                }
            }
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.cv_loans -> {
                val intent2 = Intent(context, LoanActivity::class.java)
                startActivity(intent2)
            }

            R.id.cv_servicess1 -> {
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

            /*R.id.ll_Houses -> {
                val intent8 = Intent(context, CenterHomeActivity::class.java)
                bundle.putString("center", "commercial")
                intent8.putExtras(bundle)
                startActivity(intent8)
            }*/

            R.id.ll_travels -> {
                val intent9 = Intent(context, Travelsactivity::class.java)
                bundle.putString("center", "hotel")
                intent9.putExtras(bundle)
                startActivity(intent9)
            }
            R.id.ll_green_land -> {
                val intent14 = Intent(context, PropertyActivity::class.java)
                bundle.putString("type", "Green Land")
                intent14.putExtras(bundle)
                startActivity(intent14)
            }

            R.id.ll_sites -> {
                val intent15 = Intent(context, PropertyActivity::class.java)
                bundle.putString("type", "Site")
                intent15.putExtras(bundle)
                startActivity(intent15)
            }

            R.id.ll_Houses ->{
                val intent10 = Intent(context, PropertyActivity::class.java)
                bundle.putString("type", "House")
                intent10.putExtras(bundle)
                startActivity(intent10)
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
            if (itemClick.category == AppConstants.layoutsname) {
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