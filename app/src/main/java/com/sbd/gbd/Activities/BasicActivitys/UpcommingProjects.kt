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
import com.sbd.gbd.Adapters.AllUpcommingadaptor
import com.sbd.gbd.Model.PropertytModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants

class UpcommingProjects : AppCompatActivity(), View.OnClickListener {
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var layoutlist: MutableList<PropertytModel> = ArrayList()
    private val mHandler = Handler()
    var layoutAdaptor: AllUpcommingadaptor? = null
    var back_toolbar_up: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcomming_projects)
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
        AsyncTask.execute { fetchads() }
    }

    private fun initilize() {
        recyclerView = findViewById(R.id.recyclarview_allupcomming)
        back_toolbar_up = findViewById(R.id.back_toolbar_up)
        //  recyclerView.setHasFixedSize(true);
        val mgrid = GridLayoutManager(this, 1)
        recyclerView?.setLayoutManager(mgrid)
        back_toolbar_up?.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun fetchads() {
        val adsimage = FirebaseDatabase.getInstance().reference.child("adsforyou")
        adsimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<String, Any>?
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<String, Any>?
                            layoutlist.add(
                                PropertytModel(
                                    userData!![AppConstants.image].toString(),
                                    userData[AppConstants.pid].toString(),
                                    userData[AppConstants.description].toString(),
                                    userData[AppConstants.category].toString(),
                                    userData[AppConstants.price].toString(),
                                    userData[AppConstants.pname].toString(),
                                    userData[AppConstants.propertysize].toString(),
                                    userData[AppConstants.location].toString(),
                                    userData[AppConstants.number].toString(),
                                    userData[AppConstants.type].toString()
                                )
                            )
                        } catch (cce: ClassCastException) {

                        }
                    }
                    layoutAdaptor = AllUpcommingadaptor(layoutlist, this@UpcommingProjects)
                    val n1layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@UpcommingProjects, RecyclerView.VERTICAL, false)
                    recyclerView!!.layoutManager = n1layoutManager
                    recyclerView!!.itemAnimator = DefaultItemAnimator()
                    mHandler.post { recyclerView!!.adapter = layoutAdaptor }
                    layoutAdaptor!!.notifyItemRangeInserted(0, layoutlist.size)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onClick(view: View) {}
}