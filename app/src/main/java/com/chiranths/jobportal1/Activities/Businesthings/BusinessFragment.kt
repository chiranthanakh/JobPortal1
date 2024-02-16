package com.chiranths.jobportal1.Activities.Businesthings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Activities.Admin.AdminBusinessListings
import com.chiranths.jobportal1.Activities.BasicActivitys.SearchActivity
import com.chiranths.jobportal1.Activities.Dashboard.StartingActivity
import com.chiranths.jobportal1.Adapters.BusinessAdaptor
import com.chiranths.jobportal1.Adapters.BusinessCategoryAdaptor
import com.chiranths.jobportal1.Interface.FragmentInteractionListener
import com.chiranths.jobportal1.Model.BusinessModel
import com.chiranths.jobportal1.Model.Categorymmodel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BusinessFragment : Fragment() {

    private var ProductsRef: DatabaseReference? = null
    var recyclerView: RecyclerView? = null
    var main_edt_search2: EditText? = null
    private var gridView: RecyclerView? = null
    var back: ImageView? = null
    var btnListbusiness: AppCompatButton? = null
    var llsearch: LinearLayout? = null
    var businesslist = ArrayList<BusinessModel>()
    var filterbusinesslist = ArrayList<BusinessModel>()
    var categorylists = ArrayList<Categorymmodel>()
    var businesscatAdaptor: BusinessCategoryAdaptor? = null
    var businessAdaptor: BusinessAdaptor? = null
    var add_button: AppCompatButton? = null
    var mHandler = Handler()
    var bundle = Bundle()
    var iv_back : ImageView?= null
    var iv_search : ImageView?= null
    var fragmentInt : FragmentInteractionListener?= null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
            //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.JobPortaltheam) //default app theme
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.app_blue)
        }*/

        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        initilize(view)
    }

    private fun initilize(view: View) {
        recyclerView = view.findViewById(R.id.recycler_business)
        llsearch = view.findViewById(R.id.ll_search_business)
        gridView = view.findViewById(R.id.id_gridview)
        main_edt_search2 = view.findViewById(R.id.main_edt_search2)
        add_button = view.findViewById(R.id.btn_add_business)
        iv_back = view.findViewById(R.id.iv_business_back)
        iv_search = view.findViewById(R.id.iv_business_search)

        //val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavShift)
        //bottomNav?.visibility = View.GONE

        fetchbusiness("")
        fetchbusinessCategorys()
        main_edt_search2?.setOnClickListener { view: View? ->
            val intent = Intent(context, SearchActivity::class.java)
            bundle.putString("searchtype", "business")
            intent.putExtras(bundle)
            startActivity(intent)
        }

        iv_back?.setOnClickListener {
            onBackButtonClicked()
        }
        iv_search?.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            bundle.putString("searchtype", "business")
            intent.putExtras(bundle)
            startActivity(intent)
        }

        llsearch?.setOnClickListener { view: View? ->
            val intent = Intent(context, SearchActivity::class.java)
            bundle.putString("searchtype", "business")
            intent.putExtras(bundle)
            startActivity(intent)
        }
        add_button?.setOnClickListener { view: View? ->
            val intent = Intent(
                context,
                AdminBusinessListings::class.java
            )
            startActivity(intent)
        }
    }

    fun onBackButtonClicked() {
        fragmentInt?.onFragmentInteraction()
    }

    private fun fetchbusinessCategorys() {
        val categorylist =
            FirebaseDatabase.getInstance().reference.child("BusinessListing_category")
        categorylist.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    categorylists.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            categorylists.add(
                                Categorymmodel(
                                    userData!![AppConstants.pid].toString(),
                                    userData[AppConstants.image].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData["subcategory"].toString()
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
                    //RecyclerView.LayoutManager nlayoutManager1 = new LinearLayoutManager(BusinessActivity.this, RecyclerView.HORIZONTAL, false);
                    val nlayoutManager1 = GridLayoutManager(context, 4)
                    gridView?.layoutManager = nlayoutManager1
                    gridView?.itemAnimator = DefaultItemAnimator()
                    businesscatAdaptor =
                        context?.let { BusinessCategoryAdaptor(categorylists, it) }
                    mHandler.post { gridView!!.adapter = businesscatAdaptor }
                    businesscatAdaptor!!.notifyItemRangeInserted(0, businesslist.size)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchbusiness(cat: String) {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("BusinessListing").orderByChild(AppConstants.Status).equalTo("2")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as java.util.HashMap<String, Any>?
                    businesslist.clear()
                    filterbusinesslist.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as java.util.HashMap<String, Any>?
                            if (userData!!["products"].toString() == cat) {
                                businesslist.add(
                                    BusinessModel(
                                        userData[AppConstants.pid].toString(),
                                        userData[AppConstants.date].toString(),
                                        userData[AppConstants.time].toString(),
                                        userData["Businessname"].toString(),
                                        userData["Business_category"].toString(),
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
                            filterbusinesslist.add(
                                BusinessModel(
                                    userData[AppConstants.pid].toString(),
                                    userData[AppConstants.date].toString(),
                                    userData[AppConstants.time].toString(),
                                    userData["Businessname"].toString(),
                                    userData["Business_category"].toString(),
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
                        } catch (cce: java.lang.ClassCastException) {
                        }
                    }
                    businessAdaptor = BusinessAdaptor(filterbusinesslist, context!!)
                    if (cat == "") {
                        recyadaptor(filterbusinesslist)
                    } else {
                        recyadaptor(filterbusinesslist)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun recyadaptor(businesslist1: ArrayList<BusinessModel>) {
        val nlayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView?.layoutManager = nlayoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        mHandler.post { recyclerView?.adapter = businessAdaptor }
        businessAdaptor?.notifyItemRangeInserted(0, businesslist1.size)
    }
}