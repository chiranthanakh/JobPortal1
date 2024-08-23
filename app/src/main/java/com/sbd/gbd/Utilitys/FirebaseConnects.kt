package com.sbd.gbd.Utilitys

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.AdsAdaptor
import com.sbd.gbd.Model.AdsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseConnects {

     fun fetchDataWithStatus(tabelName: String, callback: (HashMap<*, *>?) -> Unit) {
        val adsimage = FirebaseDatabase.getInstance().reference.child(tabelName)
            .orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        adsimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataMap = snapshot.value
                        as? HashMap<*, *>
                callback(dataMap)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    fun fetchDataWithLayout(tabelName: String,type: String, callback: (HashMap<*, *>?) -> Unit) {
        val adsimage = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
            .orderByChild(AppConstants.type).equalTo(type)
           // .orderByChild(AppConstants.Status).equalTo(AppConstants.user)

        adsimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataMap = snapshot.value
                        as? HashMap<*, *>
                callback(dataMap)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    fun fetchCarosel(tabelName: String, callback: (HashMap<*, *>?) -> Unit) {
        val carosel = FirebaseDatabase.getInstance().reference.child(tabelName)
        carosel.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataMap = snapshot.value
                        as? HashMap<*, *>
                callback(dataMap)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

}