package com.sbd.gbd.Activities.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.CallsAdaptor
import com.sbd.gbd.Adapters.LoanCoroselListAdaptor
import com.sbd.gbd.Adapters.LoanoffersAdaptor
import com.sbd.gbd.Model.CallerDetailsModel
import com.sbd.gbd.Model.LoanOffersModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants

class CallsListActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var callslist = ArrayList<CallerDetailsModel>()
    private var callsAdaptor: CallsAdaptor? = null
    var mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calls_list)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_calls)
        fetchcalls()
    }


    private fun fetchcalls() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("callDetails")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            callslist.add(
                                CallerDetailsModel(
                                    userData?.get("called_number").toString(),
                                    userData?.get("caller_name").toString(),
                                    userData?.get("caller_number").toString(),
                                    userData?.get("date").toString(),
                                    userData?.get("time").toString(),

                                )
                            )
                        } catch (cce: ClassCastException) {

                        }
                    }
                    callsAdaptor = CallsAdaptor(callslist, this@CallsListActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@CallsListActivity, RecyclerView.VERTICAL, false)
                    recyclerView?.layoutManager = nlayoutManager
                    recyclerView?.itemAnimator = DefaultItemAnimator()
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(recyclerView)
                    snapHelper.onFling(20, 20)
                    mHandler.post { recyclerView!!.adapter = callsAdaptor }
                    callsAdaptor?.notifyItemRangeInserted(0, callslist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}