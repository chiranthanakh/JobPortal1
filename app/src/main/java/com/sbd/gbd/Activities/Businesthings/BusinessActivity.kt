package com.sbd.gbd.Activities.Businesthings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Activities.Admin.AdminBusinessListings
import com.sbd.gbd.Activities.BasicActivitys.SearchActivity
import com.sbd.gbd.Adapters.BusinessAdaptor
import com.sbd.gbd.Adapters.BusinessCategoryAdaptor
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.Model.Categorymmodel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BusinessActivity : AppCompatActivity(), View.OnClickListener, FilterCategory {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_business)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
            //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.JobPortaltheam) //default app theme
        }
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.app_blue)
        }
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        initilize()
    }

    private fun initilize() {
        recyclerView = findViewById(R.id.recycler_business)
        llsearch = findViewById(R.id.ll_search_business)
        back = findViewById(R.id.back_toolbar_business)
        gridView = findViewById(R.id.id_gridview)
        main_edt_search2 = findViewById(R.id.main_edt_search2)
        add_button = findViewById(R.id.btn_add_business)
        fetchbusiness("")
        fetchbusinessCategorys()
        main_edt_search2?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@BusinessActivity, SearchActivity::class.java)
            bundle.putString("searchtype", "business")
            intent.putExtras(bundle)
            startActivity(intent)
        })
        back?.setOnClickListener(View.OnClickListener { finish() })
        llsearch?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@BusinessActivity, SearchActivity::class.java)
            bundle.putString("searchtype", "business")
            intent.putExtras(bundle)
            startActivity(intent)
        })
        add_button?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@BusinessActivity, AdminBusinessListings::class.java)
            startActivity(intent)
        })
    }

    private fun fetchbusinessCategorys() {
        val categorylist =
            FirebaseDatabase.getInstance().reference.child("BusinessListing_category")
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
                    //RecyclerView.LayoutManager nlayoutManager1 = new LinearLayoutManager(BusinessActivity.this, RecyclerView.HORIZONTAL, false);
                    val nlayoutManager1 = GridLayoutManager(this@BusinessActivity, 4)
                    gridView!!.layoutManager = nlayoutManager1
                    gridView!!.itemAnimator = DefaultItemAnimator()
                    businesscatAdaptor =
                        BusinessCategoryAdaptor(categorylists, this@BusinessActivity)
                    mHandler.post { gridView!!.adapter = businesscatAdaptor }
                    businesscatAdaptor!!.notifyItemRangeInserted(0, businesslist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchbusiness(cat: String) {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("BusinessListing").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            if (userData!!["products"].toString() == cat) {
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
                            filterbusinesslist.add(
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
                        } catch (cce: ClassCastException) {

                        }
                    }
                    //bottomhomeRecyclarviewAdaptor = new BottomhomeRecyclarviewAdaptor(filterbusinesslist, getContext(), userNumber, nameofuser);
                    businessAdaptor = BusinessAdaptor(filterbusinesslist, this@BusinessActivity)
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
            LinearLayoutManager(this@BusinessActivity, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = nlayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        mHandler.post { recyclerView!!.adapter = businessAdaptor }
        businessAdaptor!!.notifyItemRangeInserted(0, businesslist1.size)
    }

    override fun onClick(view: View) {}
    override fun filter(cat: String) {

        //Intent intent = new Intent(BusinessActivity.this, BusinessFilter.class);
        //bundle.putString("CatFilter",cat);
        //intent.putExtras(bundle);
        //BusinessActivity.this.startActivity(intent);
        //fetchbusiness(cat);
        //recyadaptor(businesslist);

        /* if(cat!=null){
            filterbusinesslist.clear();
            for (int i=0;i<businesslist.size();i++){

                if(businesslist.get(i).getBusiness_category().equals(cat)){
                    filterbusinesslist.add(new BusinessModel(businesslist.get(i).getPid(),businesslist.get(i).getDate(),businesslist.get(i).getTime(),
                            businesslist.get(i).getBusinessname(),businesslist.get(i).getBusiness_category(),businesslist.get(i).getDescription(),businesslist.get(i).getPrice(),
                            businesslist.get(i).getLocation(),businesslist.get(i).getNumber(),businesslist.get(i).getOwner(),businesslist.get(i).getEmail(),
                            businesslist.get(i).getRating(),businesslist.get(i).getImage(),businesslist.get(i).getImage2()));
                }
            }

            businessAdaptor.notifyDataSetChanged();

        }*/
    }
}