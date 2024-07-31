package com.sbd.gbd.Activities.Profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.Dashboard.StartingActivity
import com.sbd.gbd.Activities.BasicActivitys.OtpLoginActivity
import com.sbd.gbd.Adapters.*
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.Model.ConstructionModel
import com.sbd.gbd.Model.Corosolmodel
import com.sbd.gbd.Model.ProfileListModel
import com.sbd.gbd.Model.TravelsModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragments : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var ProductsRef: DatabaseReference? = null
    var businessList = ArrayList<BusinessModel>()
    var businessAdaptor: ProfileBusinessAdaptor? = null
    var coroselimagelist = ArrayList<Corosolmodel>()
    private var travelsAdaptor: ProfileTravelsAdaptor? = null
    var constructionInfo: ArrayList<ConstructionModel?> = ArrayList()
    var vehicleinfo: ArrayList<TravelsModel?> = ArrayList()
    private var coroselListAdaptor: CoroselListAdaptor? = null
    val TAG = "ProfileActivity"
    private var constructionAdaptor: ProfileConstructorAdaptor? = null
    private lateinit var nameofuser: String
    private lateinit var userNumber: String
    private lateinit var useremail: String
    var mHandler = Handler()
    var productinfolist: ArrayList<ProfileListModel?> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.profile)
        val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        nameofuser = sh?.getString("name", "")!!
        userNumber = sh.getString(AppConstants.number, "")!!
        useremail = sh.getString("email", "")!!
        initilize()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        nameofuser = sh?.getString("name", "")!!
        userNumber = sh.getString(AppConstants.number, "")!!
        useremail = sh.getString("email", "")!!
        fetchProfile()
    }

    private fun initilize() {
        lifecycleScope.launch {
            fetchdata()
            fetchbusiness()
            fetchCarosel()
        }
        if (userNumber == null || userNumber.equals("")) {
            binding.llLogout.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE
        } else {
            binding.llLogout.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE
        }

        binding.llLogin.setOnClickListener {
            val intent = Intent(context, OtpLoginActivity::class.java)
            context?.startActivity(intent)
        }

        binding.llLogout.setOnClickListener {
            binding.progressLayout.visibility = View.VISIBLE
            val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sh?.edit()
            editor?.clear()
            editor?.apply()
            val ft = fragmentManager?.beginTransaction()
            ft?.detach(this)?.attach(this)?.commit()
            val intent = Intent(activity, StartingActivity::class.java)
            startActivity(intent)
        }
    }


    private fun fetchProfile() {
        if (userNumber !== "") {
            val profile =
                FirebaseDatabase.getInstance().reference.child(AppConstants.Profiles).child(userNumber)
            profile.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataMap = snapshot.value as HashMap<*, *>?
                        for (key in dataMap!!.keys) {
                            binding.tvProfileName.text = dataMap["name"] as CharSequence?
                            binding.tvProfileEmail.text = dataMap["Email"] as CharSequence?
                            binding.tvProfileNumber.text = dataMap["number"] as CharSequence?
                            Glide.with(context!!)
                                .load(dataMap[AppConstants.image])
                                .apply(RequestOptions().override(500, 500))
                                .into(binding.ivProfileImage)
                            try {
                            } catch (_: ClassCastException) {

                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun fetchCarosel() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("Corosels").orderByChild(AppConstants.category).equalTo("Loan")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
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
                        } catch (_: ClassCastException) {

                        }
                    }
                    coroselListAdaptor = CoroselListAdaptor(coroselimagelist, requireContext())
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    binding.rvLoanEvent.layoutManager = nlayoutManager
                    binding.rvLoanEvent.itemAnimator = DefaultItemAnimator()
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(binding.rvLoanEvent)
                    snapHelper.onFling(20, 20)
                    mHandler.post { binding.rvLoanEvent.adapter = coroselListAdaptor }
                    coroselListAdaptor!!.notifyItemRangeInserted(0, coroselimagelist.size)

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun fetchbusiness() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("BusinessListing")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as java.util.HashMap<*, *>?
                    businessList.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as java.util.HashMap<*, *>?
                            if (userData?.get(AppConstants.number) == userNumber) {
                                binding.rvLoanEvent.visibility = View.GONE
                                binding.ivEmpty.visibility = View.GONE
                                businessList.add(
                                    BusinessModel(
                                        userData[AppConstants.pid].toString(),
                                        userData[AppConstants.date].toString(),
                                        userData[AppConstants.time].toString(),
                                        userData["Businessname"].toString(),
                                        userData[AppConstants.products].toString(),
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
                            }

                        } catch (_: java.lang.ClassCastException) {

                        }
                    }
                    businessAdaptor = context?.let { ProfileBusinessAdaptor(businessList, it) }
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.rvMyBusiness.setLayoutManager(nlayoutManager)
                    binding.rvMyBusiness.setItemAnimator(DefaultItemAnimator())
                    binding.rvMyBusiness.isNestedScrollingEnabled = false;

                    mHandler.post { binding.rvMyBusiness.setAdapter(businessAdaptor) }

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val productsinfo = FirebaseDatabase.getInstance().reference.child(AppConstants.construction)
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as java.util.HashMap<*, *>?
                    constructionInfo.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as java.util.HashMap<*, *>?
                            if (userData?.get("number1") == userNumber) {
                                binding.rvLoanEvent.visibility = View.GONE
                                binding.ivEmpty.visibility = View.GONE
                                constructionInfo.add(
                                    ConstructionModel(
                                        userData[AppConstants.pid].toString(),
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
                            }
                        } catch (_: java.lang.ClassCastException) {

                        }
                    }
                    constructionAdaptor =
                        context?.let { ProfileConstructorAdaptor(constructionInfo, it) }
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.rvMyConstruction.layoutManager = GridLayoutManager(context, 1)
                    binding.rvMyConstruction.itemAnimator = DefaultItemAnimator()
                    mHandler.post { binding.rvMyConstruction.adapter = constructionAdaptor }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val travelsinfo = FirebaseDatabase.getInstance().reference.child("travelsforyou")
        travelsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as java.util.HashMap<*, *>?
                    vehicleinfo.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as java.util.HashMap<*, *>?
                            if (userData?.get("contactnumber") == userNumber) {
                                binding.rvLoanEvent.visibility = View.GONE
                                binding.ivEmpty.visibility = View.GONE
                                vehicleinfo.add(
                                    TravelsModel(
                                        userData[AppConstants.pid].toString(),
                                        userData[AppConstants.date].toString(),
                                        userData[AppConstants.time].toString(),
                                        userData["vehiclename"].toString(),
                                        userData[AppConstants.category].toString(),
                                        userData["vehiclenumber"].toString(),
                                        userData["costperkm"].toString(),
                                        userData["contactnumber"].toString(),
                                        userData["ownerNmae"].toString(),
                                        userData[AppConstants.verified].toString(),
                                        userData[AppConstants.description].toString(),
                                        userData[AppConstants.image].toString(),
                                        userData[AppConstants.image2].toString(),
                                        userData["model"].toString(),
                                        userData[AppConstants.Status].toString()
                                    )
                                )
                            }
                        } catch (_: java.lang.ClassCastException) {
                        }
                    }
                    travelsAdaptor = context?.let { ProfileTravelsAdaptor(vehicleinfo, it) }
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.rvMyTravels.setLayoutManager(GridLayoutManager(context, 1))
                    binding.rvMyTravels.setItemAnimator(DefaultItemAnimator())
                    mHandler.post { binding.rvMyTravels.setAdapter(travelsAdaptor) }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }


    private fun fetchdata() {
        productinfolist.clear()
        val productsinfo = FirebaseDatabase.getInstance().reference.child(AppConstants.hotdeals)
            .orderByChild(AppConstants.number).equalTo(userNumber)
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
                            if (userData?.get(AppConstants.number)?.equals(userNumber) == true) {
                                binding.rvLoanEvent.visibility = View.GONE
                                binding.ivEmpty.visibility = View.GONE
                                productinfolist.add(
                                    ProfileListModel(
                                        userData[AppConstants.category].toString(),
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
                                        userData[AppConstants.postedBy].toString(),
                                        "hotforyou",
                                        userData[AppConstants.Status].toString()
                                    )
                                )
                                Log.d(TAG, productinfolist.size.toString())
                            }
                        } catch (_: ClassCastException) {

                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //layouts
        val productsinfoLayouts = FirebaseDatabase.getInstance().reference.child(AppConstants.layouts)
            .orderByChild(AppConstants.number).equalTo(userNumber)
        productsinfoLayouts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
                            if (userData?.get(AppConstants.number)?.equals(userNumber) == true) {
                                binding.rvLoanEvent.visibility = View.GONE
                                binding.ivEmpty.visibility = View.GONE
                                productinfolist.add(
                                    ProfileListModel(
                                        userData[AppConstants.category].toString(),
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
                                        userData[AppConstants.postedBy].toString(),
                                        "layoutsforyou",
                                        userData[AppConstants.Status].toString()
                                    )
                                )
                            }
                        } catch (_: ClassCastException) {

                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val productsinfogeneral = FirebaseDatabase.getInstance().reference.child(AppConstants.ads)
            .orderByChild(AppConstants.number).equalTo(userNumber)
        productsinfogeneral.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
                            Log.d(TAG, productinfolist.toString())
                            if (userData?.get(AppConstants.number)?.equals(userNumber) == true) {
                                binding.rvLoanEvent.visibility = View.GONE
                                binding.ivEmpty.visibility = View.GONE
                                productinfolist.add(
                                    ProfileListModel(
                                        userData[AppConstants.category].toString(),
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
                                        userData[AppConstants.postedBy].toString(),
                                        "adsforyou",
                                        userData[AppConstants.Status].toString()

                                    )
                                )
                            }
                        } catch (_: ClassCastException) {
                        }
                    }
                    val bottomhomeRecyclarviewAdaptor = context?.let {
                        ProfilePostingListings(
                            productinfolist,
                            it, userNumber, nameofuser
                        )
                    }
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    binding.rvMyPostings.setLayoutManager(GridLayoutManager(context, 1))
                    binding.rvMyPostings.setItemAnimator(DefaultItemAnimator())
                    binding.rvMyPostings.isNestedScrollingEnabled = false;
                    mHandler.post {
                        binding.rvMyPostings.setAdapter(
                            bottomhomeRecyclarviewAdaptor
                        )
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}