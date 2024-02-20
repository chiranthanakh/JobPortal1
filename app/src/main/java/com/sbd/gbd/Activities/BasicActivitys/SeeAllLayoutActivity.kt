package com.sbd.gbd.Activities.BasicActivitys

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.SeeallLayouts
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import java.util.Collections

class SeeAllLayoutActivity : AppCompatActivity(), View.OnClickListener {
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var layoutlist: MutableList<String> = ArrayList<String>()
    private val mHandler = Handler()
    var layoutAdaptor: SeeallLayouts? = null
    var layoutslists: ArrayList<*> = ArrayList<Any?>()
    var layoutsAdaptor: SeeallLayouts? = null
    var back_toolbar_layouts: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_layout)
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
        initilize()
        AsyncTask.execute {
            fetchLayouts()
            //fetchlayouts();
        }
    }

    private fun initilize() {
        recyclerView = findViewById(R.id.recyclarview_alllayouts)
        //  recyclerView.setHasFixedSize(true);
        val mgrid = GridLayoutManager(this, 1)
        recyclerView?.setLayoutManager(mgrid)
        back_toolbar_layouts = findViewById(R.id.back_toolbar_layouts)
        back_toolbar_layouts?.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun fetchLayouts() {
        val adsimage = FirebaseDatabase.getInstance().reference.child("layoutsforyou").orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        adsimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            layoutlist.add(
                                userData!![AppConstants.image].toString() + "!!" + userData[AppConstants.pid] + "---" + userData[AppConstants.description] + "---" +
                                        userData[AppConstants.category] + "---" + userData[AppConstants.price] + "---" + userData[AppConstants.pname]
                                        + "---" + userData[AppConstants.propertysize] + "---" + userData[AppConstants.location] + "---" + userData[AppConstants.number] + "---" + userData[AppConstants.type]
                            )
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    layoutAdaptor = SeeallLayouts(layoutlist, this@SeeAllLayoutActivity)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@SeeAllLayoutActivity, RecyclerView.VERTICAL, false)
                    recyclerView!!.layoutManager = n1layoutManager
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { recyclerView!!.adapter = layoutAdaptor }
                    layoutAdaptor!!.notifyItemRangeInserted(0, layoutlist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchlayouts() {
        val adsimage = FirebaseDatabase.getInstance().reference.child("layoutsforyou")
        adsimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layoutslists.clear()
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
                            layoutlist.add(
                                userData!![AppConstants.image].toString() + "!!" + userData[AppConstants.pid] + "---" + userData[AppConstants.description] + "---" +
                                        userData[AppConstants.category] + "---" + userData[AppConstants.price] + "---" + userData[AppConstants.pname]
                                        + "---" + userData[AppConstants.propertysize] + "---" + userData[AppConstants.location] + "---" + userData[AppConstants.number] + "---" + userData[AppConstants.type]
                            )
                            layoutlist.add(
                                userData[AppConstants.image].toString() + "!!" + userData[AppConstants.pid] + "---" + userData[AppConstants.description] + "---" +
                                        userData[AppConstants.category] + "---" + userData[AppConstants.price] + "---" + userData[AppConstants.pname]
                                        + "---" + userData[AppConstants.propertysize] + "---" + userData[AppConstants.location] + "---" + userData[AppConstants.number]
                            )
                        } catch (cce: ClassCastException) {
                            try {
                                val mString = dataMap[key].toString()
                                //addTextToView(mString);
                            } catch (cce2: ClassCastException) {
                            }
                        }
                    }
                    Collections.shuffle(layoutslists)
                    layoutsAdaptor = SeeallLayouts(layoutslists, this@SeeAllLayoutActivity)
                    val n1layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                        this@SeeAllLayoutActivity,
                        RecyclerView.HORIZONTAL,
                        false
                    )
                    recyclerView!!.layoutManager = n1layoutManager
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post {
                        /*  if (progressDialog != null) {
                                                if (progressDialog.isShowing()) {
                                                    progressDialog.dismiss();
                                                }
                                            }*/
                        recyclerView!!.adapter = layoutsAdaptor
                        //  layoutsAdaptor.notifyItemRangeInserted(0, adslist.size());
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {}
}
