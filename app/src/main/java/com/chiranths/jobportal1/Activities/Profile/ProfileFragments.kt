package com.chiranths.jobportal1.Activities.Profile

import android.content.Context
import android.os.Bundle
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
import com.chiranths.jobportal1.Adapters.BottomhomeRecyclarviewAdaptor
import com.chiranths.jobportal1.Adapters.ProfilePostingListings
import com.chiranths.jobportal1.Model.ProductInfo
import com.chiranths.jobportal1.R
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragments : Fragment(), View.OnClickListener {
    private var ProductsRef: DatabaseReference? = null
    private var iv_profile_image: CircleImageView? = null
    private var tv_name: TextView? = null
    private var tv_email: TextView? = null
    private var iv_edit: ImageView? = null
    private var rv_my_postings : RecyclerView? = null
    private var ll_logout: LinearLayout? = null
    private lateinit var nameofuser : String
    private lateinit var userNumber : String
    private lateinit var useremail : String

    var productinfolist: ArrayList<ProductInfo?> = ArrayList<ProductInfo?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Profile")
        val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
         nameofuser = sh?.getString("name", "")!!
         userNumber = sh?.getString("number", "")!!
         useremail = sh?.getString("email", "")!!
        initilize(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initilize(view: View) {
        iv_profile_image = view.findViewById(R.id.iv_profile_image)
        tv_name = view.findViewById(R.id.tv_profile_name)
        tv_email = view.findViewById(R.id.tv_profile_email)
        iv_edit = view.findViewById(R.id.iv_edit)
        ll_logout = view.findViewById(R.id.ll_logout)
        rv_my_postings = view.findViewById(R.id.rv_my_postings)
        fetchProfile()
        fetchdata()
        ll_logout?.setOnClickListener(View.OnClickListener { view1: View? ->
            val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sh?.edit()
            editor?.clear()
            editor?.apply()
            val ft = fragmentManager?.beginTransaction()
            ft?.detach(this)?.attach(this)?.commit()
        })
    }

    private fun fetchProfile() {
        val sh = context?.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val nameofuser = sh?.getString("name", "")
        val userNumber = sh?.getString("number", "")
        val useremail = sh?.getString("email", "")
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
                                ProductInfo(
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
                                    userData["postedby"].toString()
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
                    val bottomhomeRecyclarviewAdaptor = ProfilePostingListings(
                        productinfolist,
                        context, "7338010153", "test"
                    )
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                        context, RecyclerView.VERTICAL, false
                    )
                    rv_my_postings?.setLayoutManager(GridLayoutManager(context, 1))
                    rv_my_postings?.setItemAnimator(DefaultItemAnimator())
                        rv_my_postings?.setAdapter(
                            bottomhomeRecyclarviewAdaptor
                        )
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    override fun onClick(view: View) {}
}