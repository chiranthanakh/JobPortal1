package com.sbd.gbd.Activities.LoanActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.sbd.gbd.Adapters.LoanoffersAdaptor
import com.sbd.gbd.Model.LoanOffersModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.CoroselListAdaptor
import com.sbd.gbd.Model.Corosolmodel
import com.sbd.gbd.databinding.ActivityLoanBinding
import kotlinx.coroutines.launch

class LoanActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoanBinding
    var coroselimagelist = ArrayList<Corosolmodel>()
    var bankadslist = ArrayList<LoanOffersModel>()
    private var coroselListAdaptor: CoroselListAdaptor? = null
    private var loanoffersAdaptor: LoanoffersAdaptor? = null
    var mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initilize()
    }

    private fun initilize() {
        binding.cvPersonalLoan.setOnClickListener(this)
        binding.cvBusinessLoan.setOnClickListener(this)
        binding.cvHomeLoan.setOnClickListener(this)
        binding.cvLap.setOnClickListener(this)
        binding.cvMartgageLoan.setOnClickListener(this)
        binding.cvVehicle.setOnClickListener(this)
        lifecycleScope.launch {
            fetchcorosel()
            fetchbankads()
        }

        binding.ivNavView.setOnClickListener{
            finish()
        }
    }

    private fun fetchcorosel() {
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
                    coroselListAdaptor = CoroselListAdaptor(coroselimagelist, this@LoanActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@LoanActivity, RecyclerView.HORIZONTAL, false)
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

    private fun fetchbankads() {
        val coroselimage = FirebaseDatabase.getInstance().reference.child("banksadsforyou")
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<*, *>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
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
                        } catch (_: ClassCastException) {

                        }
                    }
                    loanoffersAdaptor = LoanoffersAdaptor(bankadslist, this@LoanActivity)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@LoanActivity, RecyclerView.VERTICAL, false)
                    binding.recyclerLoanoffers.layoutManager = nlayoutManager
                    binding.recyclerLoanoffers.itemAnimator = DefaultItemAnimator()
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(binding.recyclerLoanoffers)
                    snapHelper.onFling(20, 20)
                    mHandler.post { binding.recyclerLoanoffers.adapter = loanoffersAdaptor }
                    loanoffersAdaptor?.notifyItemRangeInserted(0, bankadslist.size)
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