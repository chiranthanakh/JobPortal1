package com.chiranths.jobportal1.Activities.Profile

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
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
import com.chiranths.jobportal1.Activities.Dashboard.StartingActivity
import com.chiranths.jobportal1.Activities.OtpLoginActivity
import com.chiranths.jobportal1.Adapters.*
import com.chiranths.jobportal1.Model.BusinessModel
import com.chiranths.jobportal1.Model.ConstructionModel
import com.chiranths.jobportal1.Model.ProfileListModel
import com.chiranths.jobportal1.Model.TravelsModel
import com.chiranths.jobportal1.R
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragments : Fragment() {
    private var ProductsRef: DatabaseReference? = null
    private var iv_profile_image: CircleImageView? = null
    private var tv_name: TextView? = null
    private var tv_email: TextView? = null
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
        userNumber = sh?.getString("number", "")!!
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
        iv_edit = view.findViewById(R.id.iv_edit)
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
            val profile = FirebaseDatabase.getInstance().reference.child("Profile").child(
                userNumber!!
            )
            profile.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataMap = snapshot.value as HashMap<String, Any>?
                        for (key in dataMap!!.keys) {
                            tv_name?.text = dataMap["name"] as CharSequence?
                            tv_email?.text = dataMap["Email"] as CharSequence?
                            Glide.with(context!!)
                                .load(dataMap["image"])
                                .apply(RequestOptions().override(500, 500))
                                .into(iv_profile_image!!)
                            try {
                            } catch (cce: ClassCastException) {
                                try {
                                    val mString = dataMap[key].toString()
                                    //addTextToView(mString);
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
                            if (userData?.get("number") == userNumber) {
                                businesslist.add(
                                    BusinessModel(
                                        userData["pid"].toString(),
                                        userData["date"].toString(),
                                        userData["time"].toString(),
                                        userData["Businessname"].toString(),
                                        userData["Business_category"].toString(),
                                        userData["description"].toString(),
                                        userData["price"].toString(),
                                        userData["location"].toString(),
                                        userData["number"].toString(),
                                        userData["owner"].toString(),
                                        userData["email"].toString(),
                                        userData["rating"].toString(),
                                        userData["image"].toString(),
                                        userData["image2"].toString(),
                                        userData["status"].toString()
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
                                        userData!!["pid"].toString(),
                                        userData["date"].toString(),
                                        userData["time"].toString(),
                                        userData["name"].toString(),
                                        userData["category"].toString(),
                                        userData["cost"].toString(),
                                        userData["number1"].toString(),
                                        userData["number2"].toString(),
                                        userData["experience"].toString(),
                                        userData["servicess1"].toString(),
                                        userData["servicess2"].toString(),
                                        userData["servicess3"].toString(),
                                        userData["servicess4"].toString(),
                                        userData["discription"].toString(),
                                        userData["verified"].toString(),
                                        userData["image"].toString(),
                                        userData["image2"].toString()
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
                                            userData!!["pid"].toString(),
                                            userData!!["date"].toString(),
                                            userData!!["time"].toString(),
                                            userData!!["vehiclename"].toString(),
                                            userData!!["category"].toString(),
                                            userData!!["vehiclenumber"].toString(),
                                            userData!!["costperkm"].toString(),
                                            userData!!["contactnumber"].toString(),
                                            userData!!["ownerNmae"].toString(),
                                            userData!!["verified"].toString(),
                                            userData!!["description"].toString(),
                                            userData!!["image"].toString(),
                                            userData!!["image2"].toString(),
                                            userData!!["model"].toString(),
                                            userData!!["status"].toString()
                                        )
                                    )
                                }
                            } catch (cce: java.lang.ClassCastException) {
                                try {
                                    val mString = dataMap!![key].toString()
                                    //addTextToView(mString);
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
                            if (userData?.get("number") == userNumber) {
                            productinfolist.add(
                                ProfileListModel(
                                    userData!!["category"].toString(),
                                    userData["date"].toString(),
                                    userData["description"].toString(),
                                    userData["image"].toString(),
                                    userData["location"].toString(),
                                    userData["number"].toString(),
                                    userData["pid"].toString(),
                                    userData["pname"].toString(),
                                    userData["price"].toString(),
                                    userData["propertysize"].toString(),
                                    userData["time"].toString(),
                                    userData["type"].toString(),
                                    userData["postedby"].toString(),
                                    "hotforyou",
                                    userData["Status"].toString()
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


        //layouts
        val productsinfoLayouts = FirebaseDatabase.getInstance().reference.child("layoutsforyou")
        productsinfoLayouts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get("number") == userNumber) {
                                productinfolist.add(
                                    ProfileListModel(
                                        userData!!["category"].toString(),
                                        userData["date"].toString(),
                                        userData["description"].toString(),
                                        userData["image"].toString(),
                                        userData["location"].toString(),
                                        userData["number"].toString(),
                                        userData["pid"].toString(),
                                        userData["pname"].toString(),
                                        userData["price"].toString(),
                                        userData["propertysize"].toString(),
                                        userData["time"].toString(),
                                        userData["type"].toString(),
                                        userData["postedby"].toString(),
                                        "layoutsforyou",
                                        userData["Status"].toString()
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
                    mHandler.post(Runnable { rv_my_postings?.setAdapter(
                        bottomhomeRecyclarviewAdaptor
                    ) })
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val productsinfogeneral = FirebaseDatabase.getInstance().reference.child("adsforyou")
        productsinfogeneral.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get("number") == userNumber) {
                                productinfolist.add(
                                    ProfileListModel(
                                        userData!!["category"].toString(),
                                        userData["date"].toString(),
                                        userData["description"].toString(),
                                        userData["image"].toString(),
                                        userData["location"].toString(),
                                        userData["number"].toString(),
                                        userData["pid"].toString(),
                                        userData["pname"].toString(),
                                        userData["price"].toString(),
                                        userData["propertysize"].toString(),
                                        userData["time"].toString(),
                                        userData["type"].toString(),
                                        userData["postedby"].toString(),
                                        "adsforyou",
                                        userData["Status"].toString()

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

}