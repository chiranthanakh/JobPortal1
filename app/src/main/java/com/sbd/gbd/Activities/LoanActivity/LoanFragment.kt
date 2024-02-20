package com.sbd.gbd.Activities.LoanActivity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.sbd.gbd.Activities.Dashboard.DashboardFragment
import com.sbd.gbd.Adapters.LoanCoroselListAdaptor
import com.sbd.gbd.Adapters.LoanoffersAdaptor
import com.sbd.gbd.Model.LoanOffersModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoanFragment : Fragment(), View.OnClickListener {
    var cv_PL: CardView? = null
    var cv_BL: CardView? = null
    var cv_HL: CardView? = null
    var cv_ML: CardView? = null
    var cv_LAP: CardView? = null
    var cv_VL: CardView? = null
    var btn_next: Button? = null
    var iv_nav_view :ImageView? = null
    var coroselimagelist: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var bankadslist = ArrayList<LoanOffersModel>()
    var recyclerView: RecyclerView? = null
    var recyclarviewloanads: RecyclerView? = null
    private var coroselListAdaptor: LoanCoroselListAdaptor? = null
    private var loanoffersAdaptor: LoanoffersAdaptor? = null
    var startingFragment = DashboardFragment()
    var mHandler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_loan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initilize(view)
    }

    private fun initilize(view: View) {
        cv_PL = view.findViewById(R.id.cv_personal_loan)
        cv_BL = view.findViewById(R.id.cv_business_loan)
        cv_HL = view.findViewById(R.id.cv_home_loan)
        cv_ML = view.findViewById(R.id.cv_martgage_loan)
        cv_LAP = view.findViewById(R.id.cv_lap)
        cv_VL = view.findViewById(R.id.cv_vehicle)
        iv_nav_view = view.findViewById(R.id.iv_nav_view)
        recyclerView = view.findViewById<View>(R.id.rv_loan_event) as RecyclerView
        recyclarviewloanads = view.findViewById(R.id.recycler_loanoffers)
        iv_nav_view?.setOnClickListener{
            removeCurrentFragment()
        }
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
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    coroselListAdaptor = LoanCoroselListAdaptor(coroselimagelist, context)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    recyclerView?.layoutManager = nlayoutManager
                    recyclerView?.itemAnimator = DefaultItemAnimator()
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(recyclerView)
                    snapHelper.onFling(20, 20)
                    mHandler.post { recyclerView!!.adapter = coroselListAdaptor }
                    coroselListAdaptor?.notifyItemRangeInserted(0, coroselimagelist.size)

                    /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                                mHandler2.removeCallbacks(SCROLLING_RUNNABLE);
                                Handler postHandler = new Handler();
                                postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(coroselListAdaptor);
                                        recyclerView.smoothScrollBy(pixelsToMove, 0);

                                        mHandler2.postDelayed(SCROLLING_RUNNABLE, 200);
                                    }
                                }, 2000);

                        }
                    });

                    mHandler.postDelayed(SCROLLING_RUNNABLE, 200);*/
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
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    loanoffersAdaptor = LoanoffersAdaptor(bankadslist, context)
                    val nlayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    recyclarviewloanads?.layoutManager = nlayoutManager
                    recyclarviewloanads?.itemAnimator = DefaultItemAnimator()
                    val snapHelper: SnapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(recyclarviewloanads)
                    snapHelper.onFling(20, 20)
                    mHandler.post { recyclarviewloanads!!.adapter = loanoffersAdaptor }
                    loanoffersAdaptor?.notifyItemRangeInserted(0, bankadslist.size)

                    /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                                mHandler2.removeCallbacks(SCROLLING_RUNNABLE);
                                Handler postHandler = new Handler();
                                postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(coroselListAdaptor);
                                        recyclerView.smoothScrollBy(pixelsToMove, 0);

                                        mHandler2.postDelayed(SCROLLING_RUNNABLE, 200);
                                    }
                                }, 2000);

                        }
                    });

                    mHandler.postDelayed(SCROLLING_RUNNABLE, 200);*/
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.cv_personal_loan -> {
                val intent = Intent(context, LoanFormfirst::class.java)
                startActivity(intent)
            }

            R.id.cv_business_loan -> {
                val intent1 = Intent(context, LoanFormGeneral::class.java)
                startActivity(intent1)
            }

            R.id.cv_home_loan -> {
                val intent2 = Intent(context, LoanFormfirst::class.java)
                startActivity(intent2)
            }

            R.id.cv_martgage_loan -> {
                val intent3 = Intent(context, LoanFormfirst::class.java)
                startActivity(intent3)
            }

            R.id.cv_lap -> {
                val intent4 = Intent(context, LoanFormfirst::class.java)
                startActivity(intent4)
            }

            R.id.cv_vehicle -> {
                val intent5 = Intent(context, LoanFormfirst::class.java)
                startActivity(intent5)
            }
        }
    }

    private fun removeCurrentFragment() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container, startingFragment).commit()
    }
}