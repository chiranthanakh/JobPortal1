package com.sbd.gbd.Activities.Construction

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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Activities.Admin.AdminBusinessListings
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Adapters.BusinessAdaptor
import com.sbd.gbd.Adapters.BusinessCategoryAdaptor
import com.sbd.gbd.Interface.FragmentInteractionListener
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.Model.Categorymmodel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Activities.Admin.Admin_Construction
import com.sbd.gbd.Activities.BasicActivitys.OtpLoginActivity
import com.sbd.gbd.Activities.BasicActivitys.UserDetailsActivity
import com.sbd.gbd.Adapters.ConstructorAdaptor
import com.sbd.gbd.Model.ConstructionModel
import com.sbd.gbd.Utilitys.PreferenceManager
import com.sbd.gbd.Utilitys.UtilityMethods


class ConstructionFragment : Fragment() {


    var backarrow : ImageView? = null
    var cv_plumbers: CardView? = null
    var Name: String? = null
    var category: String? = null
    var saveCurrentDate: String? = null
    var saveCurrentTime: String? = null
    var discription: String? = null
    var vehicleNumber: String? = null
    var llsearch: LinearLayout? = null
    var edt_const_search : ImageView? = null
    var rv_construction: RecyclerView? = null
    private var btn_add_business: AppCompatButton? = null
    private var rv_category: RecyclerView? = null
    var businesscatAdaptor: BusinessCategoryAdaptor? = null
    var bundle = Bundle()


    private var ProductsRef: DatabaseReference? = null
    var recyclerView: RecyclerView? = null
    var iv_no_internet: ImageView? = null
    var business_layout : LinearLayout? = null
    var mHandler = Handler()
    var iv_back : ImageView?= null
    var iv_search : ImageView?= null
    var fragmentInt : FragmentInteractionListener?= null
    lateinit var preferenceManager: PreferenceManager
    var constructioninfo: ArrayList<ConstructionModel> = ArrayList<ConstructionModel>()
    var categorylists = java.util.ArrayList<Categorymmodel>()
    private var constructionAdaptor: ConstructorAdaptor? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_construction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        if (UtilityMethods.isNetworkAvailable(requireContext())){
            initilize(view)
        }else{
            iv_no_internet = view.findViewById(R.id.iv_no_internet)
            business_layout = view.findViewById(R.id.business_layout)
            iv_no_internet?.visibility = View.VISIBLE
            business_layout?.visibility = View.GONE
            UtilityMethods.showToast(requireContext(),"Please check your internet connection")
        }
    }

    private fun initilize(view: View) {
        rv_construction = view.findViewById(R.id.rv_constructions_relates)
        rv_category = view.findViewById(R.id.id_gridview_construction)
        backarrow = view.findViewById(R.id.iv_const_backarrow)
        llsearch = view.findViewById(R.id.ll_search_business)
        edt_const_search = view.findViewById(R.id.iv_business_search)
        btn_add_business = view.findViewById(R.id.btn_add_business)
        preferenceManager= PreferenceManager(requireContext());

        fetchbusinessCategorys()
        fetchdata()

        llsearch?.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            bundle.putString("searchtype", "const")
            intent.putExtras(bundle)
            startActivity(intent)
        }
        edt_const_search?.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            bundle.putString("searchtype", "const")
            intent.putExtras(bundle)
            startActivity(intent)
        }
        backarrow?.visibility = View.GONE

        btn_add_business?.setOnClickListener{
            if (preferenceManager.getLoginState()) {
                val intent = Intent(requireContext(), Admin_Construction::class.java)
                startActivity(intent)
            } else {
                UtilityMethods.showToast(requireContext(),"Please Login to process")
                val intent = Intent(requireContext(), OtpLoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    fun onBackButtonClicked() {
        fragmentInt?.onFragmentInteraction()
    }

    private fun fetchbusinessCategorys() {
        categorylists.clear()
        var categorylist = FirebaseDatabase.getInstance().reference.child("BusinessListing_category").orderByChild(AppConstants.category).equalTo("Construction")

        categorylist.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
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

                        }
                    }
                    val nlayoutManager1 = GridLayoutManager(requireContext(), 4)
                    rv_category?.setLayoutManager(nlayoutManager1)
                    rv_category?.setItemAnimator(DefaultItemAnimator())
                    businesscatAdaptor =
                        BusinessCategoryAdaptor(1,categorylists, requireContext())
                    mHandler.post { rv_category?.setAdapter(businesscatAdaptor) }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchdata() {

        val productsinfo = FirebaseDatabase.getInstance().reference.child("constructionforyou").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        productsinfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap = dataSnapshot.value as HashMap<String, Any>?
                    constructioninfo.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData?.get(AppConstants.Status).toString().equals(AppConstants.user)) {
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
                        } catch (cce: ClassCastException) {

                        }
                    }

                    // Upcoming Event
                    constructionAdaptor = ConstructorAdaptor(constructioninfo, requireContext())
                    val elayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                    rv_construction!!.layoutManager = GridLayoutManager(requireContext(), 1)
                    rv_construction!!.itemAnimator = DefaultItemAnimator()
                    rv_construction!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { rv_construction!!.adapter = constructionAdaptor }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}