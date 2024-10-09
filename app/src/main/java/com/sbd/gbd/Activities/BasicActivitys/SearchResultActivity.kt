package com.sbd.gbd.Activities.BasicActivitys

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.PropertyAdaptor
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    var Propertyfilterlist: ArrayList<FilterModel> = ArrayList()
    var propertyAdaptor: PropertyAdaptor? = null
    var propertylist: ArrayList<String> = ArrayList()
    var mHandler = Handler()
    var P_Type = ""
    var min_price = 0
    var max_price = 10000
    var plot_type = ""
    var noBedroom = ""
    var place = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToolbarSearch.setOnClickListener{
            finish()
        }

        val bundle = intent.extras
        if (bundle != null) {
            P_Type = bundle.getString("pType", "")
            plot_type = bundle.getString("plotType", "")
            min_price = bundle.getInt("minPrice", 0)
            max_price = bundle.getInt("maxPrice", 10000)
            noBedroom = bundle.getString("bedroom", "")
            place = bundle.getString("place","")
            if (max_price == 0){
                max_price = 10000
            }
            if(P_Type == "Layout/Plot") {
                P_Type = "Layout"
            }
        }
        fetchproducts()
    }

    private fun fetchproducts() {
        var coroselimage = FirebaseDatabase.getInstance().reference.
        child(AppConstants.products).orderByChild(AppConstants.type).equalTo(P_Type)
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<*, *>?
                    propertylist.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
                            if ( userData?.get(AppConstants.Status)?.
                                equals(AppConstants.user) == true  &&
                                numberMin(userData[AppConstants.price].toString())
                                && locationFind(userData[AppConstants.location].toString(),
                                    userData[AppConstants.district].toString(),
                                    userData[AppConstants.taluk].toString() )
                                ) {

                                Propertyfilterlist.add(
                                    FilterModel(userData[AppConstants.pname].toString(),
                                        userData[AppConstants.description].toString(),
                                        userData[AppConstants.price].toString(),
                                        userData[AppConstants.image].toString(),
                                        userData[AppConstants.category].toString(),
                                        userData[AppConstants.pid].toString(),
                                        AppConstants.date, AppConstants.time,
                                        userData[AppConstants.type].toString(),
                                        userData[AppConstants.propertysize].toString(),
                                        userData[AppConstants.location].toString(),
                                        userData[AppConstants.number].toString(),
                                        userData[AppConstants.Status].toString() )
                                )
                            }

                        } catch (_: ClassCastException) {

                        }
                    }
                    propertyAdaptor = PropertyAdaptor(Propertyfilterlist, this@SearchResultActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@SearchResultActivity, RecyclerView.VERTICAL, false)
                    binding.recyclerSearchResults.layoutManager = nlayoutManager
                    binding.recyclerSearchResults.itemAnimator = DefaultItemAnimator()
                    binding.recyclerSearchResults.adapter = propertyAdaptor
                    propertyAdaptor!!.notifyItemRangeInserted(0, propertylist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun numberMin( number : String ) : Boolean {
        val regex = "\\d+".toRegex()
        val numbers = regex.find(number)?.value ?: "0"
        return if(numbers != "") {
            numbers.toInt() >= min_price  && numbers.toInt() <= max_price
        } else {
            false
        }
    }
    fun locationFind(location: String, district: String, taluk: String): Boolean {
        if(place == ""){
            return true
        }
        if(location.lowercase().contains(place.lowercase()) ||
            district.lowercase().contains(place.lowercase()) ||
            taluk.lowercase().contains(place.lowercase())){
            return true
        } else {
            return false
        }
    }
}