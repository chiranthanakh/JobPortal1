package com.sbd.gbd.Activities.Propertys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Activities.Admin.Admin_ads_dashboard
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Adapters.PropertyAdaptor
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.BasicActivitys.OtpLoginActivity
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.Utilitys.UtilityMethods
import com.sbd.gbd.databinding.ActivityPropertyBinding
import kotlinx.coroutines.launch
import java.util.Collections

class PropertyFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: ActivityPropertyBinding
    private var ProductsRef: DatabaseReference? = null
    var adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var search: ImageView? = null
    var mHandler = Handler()
    var iv_no_internet: ImageView? = null
    var business_layout : LinearLayout? = null
    var propertylist: ArrayList<FilterModel> = ArrayList()
    var siteslist: ArrayList<FilterModel> = ArrayList()
    var Homeslist: ArrayList<FilterModel> = ArrayList()
    var Rentallist: ArrayList<FilterModel> = ArrayList()
    var adslist: ArrayList<AdsModel> = ArrayList()
    var propertyAdaptor: PropertyAdaptor? = null
    var adsAdaptor: AdsAdaptor? = null
    var bundle = Bundle()
    lateinit var preferenceManager:PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityPropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        if (UtilityMethods.isNetworkAvailable(requireContext())){
            initilize(view)
            lifecycleScope.launch { fetchads() }
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
        binding.btnAddProperty.setOnClickListener(this)
        search = view.findViewById(R.id.iv_search)
        binding.backToolbarProperty.setOnClickListener(this)
        binding.recyclerMenu.setHasFixedSize(true)
        binding.recyclerMenu.setLayoutManager(GridLayoutManager(context, 1))
        lifecycleScope.launch { fetchproducts("") }
        search?.setOnClickListener{
            val intent = Intent(context, SearchActivity::class.java)
            bundle.putString("searchtype", "property")
            intent.putExtras(bundle)
            startActivity(intent)
        }

        binding.buttonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            view.findViewById<MaterialButton>(checkedId)
            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        lifecycleScope.launch { fetchproducts("Site") }
                        binding.llAssetText.visibility = View.GONE
                        binding.llAssetRecyclrView.visibility = View.GONE
                    }

                    R.id.button2 -> {
                        lifecycleScope.launch { fetchproducts("Green Land") }
                        binding.llAssetText.visibility = View.GONE
                        binding.llAssetRecyclrView.visibility = View.GONE
                    }

                    R.id.button3 -> {
                        lifecycleScope.launch { fetchproducts("House") }
                        binding.llAssetText.visibility = View.GONE
                        binding.llAssetRecyclrView.visibility = View.GONE
                    }

                    R.id.button -> {
                        lifecycleScope.launch { fetchproducts("Layout") }
                        binding.llAssetText.visibility = View.GONE
                        binding.llAssetRecyclrView.visibility = View.GONE
                    }
                }
            }
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
                                    userData[AppConstants.text1].toString(),
                                    userData[AppConstants.text2].toString(),
                                    userData[AppConstants.text3].toString(),
                                    userData[AppConstants.text4].toString()
                                )
                            )
                        } catch (_: ClassCastException) {

                        }
                    }
                    Collections.shuffle(adslist)
                    adsAdaptor = AdsAdaptor(adslist, context)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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
        var coroselimage = if(type == "" || type == null ) {
            FirebaseDatabase.getInstance().reference.child(AppConstants.products).orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        } else {
            FirebaseDatabase.getInstance().reference.child(AppConstants.products).orderByChild(AppConstants.type).equalTo(type)
        }
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<*, *>?
                    propertylist.clear()
                    siteslist.clear()
                    Homeslist.clear()
                    Rentallist.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
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

                        } catch (_: ClassCastException) {

                        }
                    }
                    propertyAdaptor = PropertyAdaptor(propertylist, requireContext())
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.recyclerMenu.layoutManager = nlayoutManager
                    binding.recyclerMenu.itemAnimator = DefaultItemAnimator()
                    mHandler.post { binding.recyclerMenu.adapter = propertyAdaptor }
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

            R.id.back_toolbar_property -> {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            }
        }
    }
}
