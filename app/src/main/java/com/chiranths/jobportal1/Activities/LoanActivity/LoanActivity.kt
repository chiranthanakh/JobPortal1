package com.chiranths.jobportal1.Activities.LoanActivity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.chiranths.jobportal1.Adapters.LoanCoroselListAdaptor
import com.chiranths.jobportal1.Adapters.LoanoffersAdaptor
import com.chiranths.jobportal1.Model.LoanOffersModel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoanActivity : AppCompatActivity(), View.OnClickListener {
    var cv_PL: CardView? = null
    var cv_BL: CardView? = null
    var cv_HL: CardView? = null
    var cv_ML: CardView? = null
    var cv_LAP: CardView? = null
    var cv_VL: CardView? = null
    var btn_next: Button? = null
    var coroselimagelist: ArrayList<String> = ArrayList<String>()
    var bankadslist = ArrayList<LoanOffersModel>()
    var recyclerView: RecyclerView? = null
    var recyclarviewloanads: RecyclerView? = null
    var iv_nav_view : ImageView? = null
    private var coroselListAdaptor: LoanCoroselListAdaptor? = null
    private var loanoffersAdaptor: LoanoffersAdaptor? = null
    var mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)
        initilize()
    }

    private fun initilize() {
        cv_PL = findViewById(R.id.cv_personal_loan)
        cv_BL = findViewById(R.id.cv_business_loan)
        cv_HL = findViewById(R.id.cv_home_loan)
        cv_ML = findViewById(R.id.cv_martgage_loan)
        cv_LAP = findViewById(R.id.cv_lap)
        cv_VL = findViewById(R.id.cv_vehicle)
        iv_nav_view = findViewById(R.id.iv_nav_view)
        recyclerView = findViewById<View>(R.id.rv_loan_event) as RecyclerView
        recyclarviewloanads = findViewById(R.id.recycler_loanoffers)
        cv_PL?.setOnClickListener(this)
        cv_BL?.setOnClickListener(this)
        cv_HL?.setOnClickListener(this)
        cv_LAP?.setOnClickListener(this)
        cv_ML?.setOnClickListener(this)
        cv_VL?.setOnClickListener(this)
        AsyncTask.execute {
            fetchcorosel()
            fetchbankads()
        }

        iv_nav_view?.setOnClickListener{
            finish()
        }
    }

    private fun fetchcorosel() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("Corosels")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            coroselimagelist.add((userData?.get(AppConstants.image) ?: "") as String)
                        } catch (cce: ClassCastException) {

                        }
                    }
                    coroselListAdaptor = LoanCoroselListAdaptor(coroselimagelist, this@LoanActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@LoanActivity, RecyclerView.HORIZONTAL, false)
                    recyclerView?.layoutManager = nlayoutManager
                    recyclerView?.itemAnimator = DefaultItemAnimator()
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(recyclerView)
                    snapHelper.onFling(20, 20)
                    mHandler.post { recyclerView!!.adapter = coroselListAdaptor }
                    coroselListAdaptor!!.notifyItemRangeInserted(0, coroselimagelist.size)

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchbankads() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("banksadsforyou")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            bankadslist.add(
                                LoanOffersModel(
                                    userData!![AppConstants.pid].toString(),
                                    userData["bankName"].toString(),
                                    userData["intrestrate"].toString(),
                                    userData["loanamount"].toString(),
                                    userData["loantype"].toString(),
                                    userData[AppConstants.description].toString(),
                                    userData[AppConstants.image].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {

                        }
                    }
                    loanoffersAdaptor = LoanoffersAdaptor(bankadslist, this@LoanActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@LoanActivity, RecyclerView.VERTICAL, false)
                    recyclarviewloanads!!.layoutManager = nlayoutManager
                    recyclarviewloanads!!.itemAnimator = DefaultItemAnimator()
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(recyclarviewloanads)
                    snapHelper.onFling(20, 20)
                    mHandler.post { recyclarviewloanads!!.adapter = loanoffersAdaptor }
                    loanoffersAdaptor!!.notifyItemRangeInserted(0, bankadslist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.cv_personal_loan -> {
                val intent = Intent(this@LoanActivity, LoanFormfirst::class.java)
                intent.putExtra("type","1")
                startActivity(intent)
            }

            R.id.cv_business_loan -> {
                val intent1 = Intent(this@LoanActivity, LoanBusiness::class.java)
                intent.putExtra("type","2")
                startActivity(intent1)
            }

            R.id.cv_home_loan -> {
                val intent2 = Intent(this@LoanActivity, LoanFormfirst::class.java)
                intent2.putExtra("type","3")
                startActivity(intent2)
            }

            R.id.cv_martgage_loan -> {
                val intent3 = Intent(this@LoanActivity, LoanFormfirst::class.java)
                intent3.putExtra("type","4")
                startActivity(intent3)
            }

            R.id.cv_lap -> {
                val intent4 = Intent(this@LoanActivity, LoanFormfirst::class.java)
                intent4.putExtra("type","5")
                startActivity(intent4)
            }

            R.id.cv_vehicle -> {
                val intent5 = Intent(this@LoanActivity, LoanVehicle::class.java)
                intent5.putExtra("type","6")
                startActivity(intent5)
            }
        }
    }
}