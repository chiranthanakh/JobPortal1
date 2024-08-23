package com.sbd.gbd.Activities.Propertys

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.Adapters.PropertyAdaptor
import com.sbd.gbd.Adapters.PropertyFragmentPagerAdapter
import com.sbd.gbd.Adapters.SeeallLayouts
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.UtilityMethods
import com.sbd.gbd.databinding.ActivityPropertyBinding
import com.sbd.gbd.databinding.FragmentPropertySitesBinding
import com.sbd.gbd.databinding.FragmentSitesBinding
import kotlinx.coroutines.launch

class PropertyLayoutFragment : Fragment() {
    private lateinit var binding: FragmentPropertySitesBinding
    private var ProductsRef: DatabaseReference? = null
    var adapter: FirebaseRecyclerAdapter<Products, ProductViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var propertyAdaptor: PropertyAdaptor? = null
    var propertylist: ArrayList<FilterModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertySitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        binding.recyclerMenu.setHasFixedSize(true)
        binding.recyclerMenu.setLayoutManager(GridLayoutManager(context, 1))
        lifecycleScope.launch { fetchproducts(AppConstants.layoutsname) }
    }


    private fun fetchproducts(type : String) {
        var coroselimage = if(type == "" || type == null ) {
            FirebaseDatabase.getInstance().reference.child(AppConstants.products).orderByChild(AppConstants.Status).equalTo(AppConstants.user)
        } else {
            FirebaseDatabase.getInstance().reference.child(AppConstants.products).orderByChild(AppConstants.type).equalTo(type)
        }
        coroselimage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dataMap = snapshot.value as HashMap<*, *>?
                    propertylist.clear()
                    for (key in dataMap!!.keys) {
                        val data = dataMap[key]
                        try {
                            val userData = data as HashMap<*, *>?
                            if (userData?.get(AppConstants.Status)
                                    ?.equals(AppConstants.user) == true
                            ) {
                                propertylist.add(
                                    FilterModel(
                                        userData[AppConstants.pname].toString(),
                                        userData[AppConstants.description].toString(),
                                        userData[AppConstants.price].toString(),
                                        userData[AppConstants.image].toString(),
                                        userData[AppConstants.category].toString(),
                                        userData[AppConstants.pid].toString(),
                                        AppConstants.date,
                                        AppConstants.time,
                                        userData[AppConstants.type].toString(),
                                        userData[AppConstants.propertysize].toString(),
                                        userData[AppConstants.location].toString(),
                                        userData[AppConstants.number].toString(),
                                        userData[AppConstants.Status].toString()
                                    )
                                )
                            }

                        } catch (_: ClassCastException) {

                        }
                    }
                    propertyAdaptor = PropertyAdaptor(propertylist, requireContext())
                    val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.recyclerMenu.apply {
                        this.layoutManager = layoutManager
                        itemAnimator = DefaultItemAnimator()
                        adapter = propertyAdaptor
                        adapter?.notifyItemRangeInserted(0, propertylist.size)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}