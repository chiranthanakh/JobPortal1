package com.sbd.gbd.Activities.Profile

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.Dashboard.StartingActivity
import com.sbd.gbd.Activities.OtpLoginActivity
import com.sbd.gbd.Adapters.*
import com.sbd.gbd.Interface.OnItemClick
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.Model.ConstructionModel
import com.sbd.gbd.Model.ProfileListModel
import com.sbd.gbd.Model.TravelsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragments : Fragment(),OnItemClick {
    private var ProductsRef: DatabaseReference? = null
    private var iv_profile_image: CircleImageView? = null
    private var tv_name: TextView? = null
    private var tv_email: TextView? = null
    private var tv_number: TextView? = null
    private var iv_edit: ImageView? = null
    private var rv_my_postings : RecyclerView? = null
    private var rv_my_business : RecyclerView? = null
    private var rv_my_construction : RecyclerView? = null
    private var rv_my_travels : RecyclerView? = null
    var businesslist = java.util.ArrayList<BusinessModel>()
    var businessAdaptor: ProfileBusinessAdaptor? = null
    private var travelsAdaptor: ProfileTravelsAdaptor? = null
    var constructioninfo: java.util.ArrayList<ConstructionModel?> =
        java.util.ArrayList<ConstructionModel?>()
    var vehicleinfo: java.util.ArrayList<TravelsModel?> = java.util.ArrayList<TravelsModel?>()

    private var constructionAdaptor: ProfileConstructorAdaptor? = null

    private lateinit var nameofuser : String
    private lateinit var userNumber : String
    private lateinit var useremail : String
    var mHandler = Handler()


    var productinfolist: ArrayList<ProfileListModel?> = ArrayList<ProfileListModel?>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        nameofuser = sh?.getString("name", "")!!
        userNumber = sh?.getString(AppConstants.number, "")!!
        useremail = sh?.getString("email", "")!!
        initilize(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Profile")
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initilize(view: View) {
        iv_profile_image = view.findViewById(R.id.iv_profile_image)
        tv_name = view.findViewById(R.id.tv_profile_name)
        tv_email = view.findViewById(R.id.tv_profile_email)
        tv_number = view.findViewById(R.id.tv_profile_number)
        val ll_logout = view.findViewById<LinearLayout>(R.id.ll_logout)
        val ll_login = view.findViewById<LinearLayout>(R.id.ll_login)

        rv_my_postings = view.findViewById(R.id.rv_my_postings)
        rv_my_business = view.findViewById(R.id.rv_my_business);
        rv_my_construction = view.findViewById(R.id.rv_my_construction)
        rv_my_travels = view.findViewById(R.id.rv_my_travels)
        fetchProfile()
        AsyncTask.execute { fetchdata() }
        fetchbusiness()
        if(userNumber == null || userNumber.equals("")){
            ll_logout.visibility = View.GONE
            ll_login.visibility = View.VISIBLE
        }else{
            ll_logout.visibility = View.VISIBLE
            ll_login.visibility = View.GONE
        }

        ll_login.setOnClickListener{
            val intent = Intent(context, OtpLoginActivity::class.java)
            context?.startActivity(intent)
        }

        ll_logout.setOnClickListener {
            val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sh?.edit()
            editor?.clear()
            editor?.apply()
            val ft = fragmentManager?.beginTransaction()
            ft?.detach(this)?.attach(this)?.commit()

            val intent= Intent(activity,StartingActivity::class.java)
            startActivity(intent)

        }
    }



    private fun fetchProfile() {

        if (userNumber !== "") {
            val profile = FirebaseDatabase.getInstance().reference.child("Profile").child(userNumber)
            profile.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataMap = snapshot.value as HashMap<String, Any>?
                        for (key in dataMap!!.keys) {
                            tv_name?.text = dataMap["name"] as CharSequence?
                            tv_email?.text = dataMap["Email"] as CharSequence?
                            tv_number?.text = dataMap["number"] as CharSequence?
                            Glide.with(context!!)
                                .load(dataMap[AppConstants.image])
                                .apply(RequestOptions().override(500, 500))
                                .into(iv_profile_image!!)
                            try {
                            } catch (cce: ClassCastException) {
                                try {

                                } catch (cce2: ClassCastException) {
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun fetchbusiness() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("BusinessListing")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as java.util.HashMap<String, Any>?
                    businesslist.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as java.util.HashMap<String, Any>?
                            if (userData?.get(AppConstants.number) == userNumber) {
                                businesslist.add(
                                    BusinessModel(
                                        userData[AppConstants.pid].toString(),
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
                            }

                        } catch (cce: java.lang.ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: java.lang.ClassCastException) {
                            }
                        }
                    }
                    businessAdaptor = context?.let { ProfileBusinessAdaptor(businesslist, it) }
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    rv_my_business?.setLayoutManager(nlayoutManager)
                    rv_my_business?.setItemAnimator(DefaultItemAnimator())
                    rv_my_business?.setNestedScrollingEnabled(false);

                    mHandler.post { rv_my_business?.setAdapter(businessAdaptor) }

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

            val productsinfo = FirebaseDatabase.getInstance().reference.child("constructionforyou")
            productsinfo.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val dataMap = dataSnapshot.value as java.util.HashMap<String, Any>?
                        constructioninfo.clear()
                        for (key in dataMap!!.keys) {
                            val data = dataMap[key]
                            try {
                                val userData = data as java.util.HashMap<String, Any>?
                                if (userData?.get("number1") == userNumber) {
                                constructioninfo.add(
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
                            }
                            } catch (cce: java.lang.ClassCastException) {
                                try {

                                } catch (cce2: java.lang.ClassCastException) {
                                }
                            }
                        }

                        // Upcoming Event
                        constructionAdaptor =
                            context?.let { ProfileConstructorAdaptor(constructioninfo, it) }
                        val elayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        rv_my_construction?.layoutManager = GridLayoutManager(context, 1)
                        rv_my_construction?.itemAnimator = DefaultItemAnimator()
                        rv_my_construction?.itemAnimator = DefaultItemAnimator()
                        mHandler.post { rv_my_construction?.adapter = constructionAdaptor }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

            val travelsinfo = FirebaseDatabase.getInstance().reference.child("travelsforyou")
        travelsinfo.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val dataMap = dataSnapshot.value as java.util.HashMap<String, Any>?
                        vehicleinfo.clear()
                        for (key in dataMap!!.keys) {
                            val data = dataMap!![key]
                            try {
                                val userData = data as java.util.HashMap<String, Any>?
                                if (userData?.get("contactnumber") == userNumber) {
                                    vehicleinfo.add(
                                        TravelsModel(
                                            userData!![AppConstants.pid].toString(),
                                            userData!![AppConstants.date].toString(),
                                            userData!![AppConstants.time].toString(),
                                            userData!!["vehiclename"].toString(),
                                            userData!![AppConstants.category].toString(),
                                            userData!!["vehiclenumber"].toString(),
                                            userData!!["costperkm"].toString(),
                                            userData!!["contactnumber"].toString(),
                                            userData!!["ownerNmae"].toString(),
                                            userData!![AppConstants.verified].toString(),
                                            userData!![AppConstants.description].toString(),
                                            userData!![AppConstants.image].toString(),
                                            userData!![AppConstants.image2].toString(),
                                            userData!!["model"].toString(),
                                            userData!![AppConstants.Status].toString()
                                        )
                                    )
                                }
                            } catch (cce: java.lang.ClassCastException) {
                                try {

                                } catch (cce2: java.lang.ClassCastException) {
                                }
                            }
                        }

                        // Upcoming Event
                        travelsAdaptor = context?.let { ProfileTravelsAdaptor(vehicleinfo, it) }
                        val elayoutManager: RecyclerView.LayoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        rv_my_travels?.setLayoutManager(GridLayoutManager(context, 1))
                        rv_my_travels?.setItemAnimator(DefaultItemAnimator())
                        mHandler.post { rv_my_travels?.setAdapter(travelsAdaptor) }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

    }


    private fun fetchdata() {
        productinfolist.clear()
        val productsinfo = FirebaseDatabase.getInstance().reference.child("hotforyou").orderByChild(AppConstants.number).equalTo(userNumber)
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get(AppConstants.number)?.equals(userNumber) == true) {
                                Log.d("dataparm5",userData?.get(AppConstants.number).toString()+"--"+userData?.get(AppConstants.Status))
                                productinfolist.add(
                                    ProfileListModel(
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
                                        userData[AppConstants.postedBy].toString(),
                                        "hotforyou",
                                        userData[AppConstants.Status].toString()
                                    )
                                )
                                Log.d("dataparm61",productinfolist.size.toString())
                                //}
                        }
                        } catch (cce: ClassCastException) {
                            try {

                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }

                    // Upcoming Event
                    /*val bottomhomeRecyclarviewAdaptor = context?.let {
                        ProfilePostingListings(
                            productinfolist,
                            it, userNumber, nameofuser
                        )
                    }
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                        context, RecyclerView.VERTICAL, false
                    )
                    rv_my_postings?.setLayoutManager(GridLayoutManager(context, 1))
                    rv_my_postings?.setItemAnimator(DefaultItemAnimator())
                    rv_my_postings?.setNestedScrollingEnabled(false);
                    mHandler.post(Runnable { rv_my_postings?.setAdapter(
                        bottomhomeRecyclarviewAdaptor
                    ) })*/
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })


        //layouts
        val productsinfoLayouts = FirebaseDatabase.getInstance().reference.child("layoutsforyou").orderByChild(AppConstants.number).equalTo(userNumber)
        productsinfoLayouts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get(AppConstants.number)?.equals(userNumber) == true) {
                                    productinfolist.add(
                                        ProfileListModel(
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
                                            userData[AppConstants.postedBy].toString(),
                                            "layoutsforyou",
                                            userData[AppConstants.Status].toString()
                                        )
                                    )
                            }
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }

                    // Upcoming Event
                    /*val bottomhomeRecyclarviewAdaptor = context?.let {
                        ProfilePostingListings(
                            productinfolist,
                            it, userNumber, nameofuser
                        )
                    }
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                        context, RecyclerView.VERTICAL, false
                    )
                    rv_my_postings?.setLayoutManager(GridLayoutManager(context, 1))
                    rv_my_postings?.setItemAnimator(DefaultItemAnimator())
                    mHandler.post(Runnable { rv_my_postings?.setAdapter(
                        bottomhomeRecyclarviewAdaptor
                    ) })*/
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val productsinfogeneral = FirebaseDatabase.getInstance().reference.child("adsforyou").orderByChild(AppConstants.number).equalTo(userNumber)
        productsinfogeneral.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            Log.d("dataparm1",productinfolist.toString())
                             if (userData?.get(AppConstants.number)?.equals(userNumber) == true) {
                                     productinfolist.add(
                                        ProfileListModel(
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
                                            userData[AppConstants.postedBy].toString(),
                                            "adsforyou",
                                            userData[AppConstants.Status].toString()

                                        )
                                    )
                                }
                        } catch (cce: ClassCastException) {
                            try {
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }

                    // Upcoming Event
                    val bottomhomeRecyclarviewAdaptor = context?.let {
                        ProfilePostingListings(
                            productinfolist,
                            it, userNumber, nameofuser
                        )
                    }
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                        context, RecyclerView.VERTICAL, false
                    )
                    rv_my_postings?.setLayoutManager(GridLayoutManager(context, 1))
                    rv_my_postings?.setItemAnimator(DefaultItemAnimator())
                    rv_my_postings?.setNestedScrollingEnabled(false);
                    mHandler.post(Runnable { rv_my_postings?.setAdapter(
                        bottomhomeRecyclarviewAdaptor
                    ) })
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onClick1() {
        TODO("Not yet implemented")
    }

}