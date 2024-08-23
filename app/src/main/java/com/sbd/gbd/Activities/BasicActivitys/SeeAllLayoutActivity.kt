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
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.LayoutsAdaptor
import com.sbd.gbd.Adapters.SeeallLayouts
import com.sbd.gbd.Model.LayoutModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.FirebaseConnects
import kotlinx.coroutines.launch
import java.util.Collections

class SeeAllLayoutActivity : AppCompatActivity(), View.OnClickListener {
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var layoutlist: ArrayList<String> = ArrayList<String>()
    private val mHandler = Handler()
    var layoutAdaptor: SeeallLayouts? = null
    var layoutslists: ArrayList<LayoutModel> = ArrayList<LayoutModel>()
    var layoutsAdaptor: SeeallLayouts? = null
    var back_toolbar_layouts: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_layout)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
          //  setTheme(R.style.darkTheme)
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
        lifecycleScope.launch {fetchlayouts()}
    }

    private fun initilize() {
        recyclerView = findViewById(R.id.recyclarview_alllayouts)
        //  recyclerView.setHasFixedSize(true);
        val mgrid = GridLayoutManager(this, 1)
        recyclerView?.setLayoutManager(mgrid)
        back_toolbar_layouts = findViewById(R.id.back_toolbar_layouts)
        back_toolbar_layouts?.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun fetchlayouts() {
        FirebaseConnects.fetchDataWithLayout(AppConstants.layouts,AppConstants.layoutsname) { dataMap ->
            if (dataMap != null) {
                layoutslists.clear()
                for (data in dataMap) {
                    try {
                        val userData = data.value as HashMap<*, *>?
                        if (userData?.get(AppConstants.Status).toString()
                                .equals(AppConstants.user)
                        ){
                            layoutslists.add(
                                LayoutModel(
                                    userData?.get(AppConstants.image).toString(),
                                    userData?.get(AppConstants.image2).toString(),
                                    userData?.get(AppConstants.pid).toString(),
                                    userData?.get(AppConstants.description).toString(),
                                    userData?.get(AppConstants.date).toString(),
                                    userData?.get(AppConstants.type).toString(),
                                    userData?.get(AppConstants.price).toString(),
                                    userData?.get(AppConstants.pname).toString(),
                                    userData?.get(AppConstants.propertysize).toString(),
                                    userData?.get(AppConstants.location).toString(),
                                    userData?.get(AppConstants.number).toString(),
                                    userData?.get(AppConstants.Status).toString(),
                                    userData?.get(AppConstants.sitesAvailable).toString(),
                                    userData?.get(AppConstants.postedBy).toString(),
                                    userData?.get(AppConstants.facing).toString(),
                                    userData?.get(AppConstants.layoutarea).toString(),
                                    userData?.get(AppConstants.point1).toString(),
                                    userData?.get(AppConstants.point2).toString(),
                                    userData?.get(AppConstants.point3).toString(),
                                    userData?.get(AppConstants.point4).toString(),
                                )
                            )
                        }
                    } catch (_: ClassCastException) { }
                }
                layoutAdaptor = SeeallLayouts(layoutslists, this@SeeAllLayoutActivity)
                val n1layoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(this@SeeAllLayoutActivity, RecyclerView.VERTICAL, false)
                recyclerView!!.layoutManager = n1layoutManager
                recyclerView!!.itemAnimator = DefaultItemAnimator()
                mHandler.post { recyclerView!!.adapter = layoutAdaptor }
                layoutAdaptor!!.notifyItemRangeInserted(0, layoutlist.size)
            }
        }
    }

    override fun onClick(view: View) {}
}
