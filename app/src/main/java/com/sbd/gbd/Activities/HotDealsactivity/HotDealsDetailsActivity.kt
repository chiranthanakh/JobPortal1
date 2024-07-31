package com.sbd.gbd.Activities.HotDealsactivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Model.HotDealsModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.databinding.ActivityHotdealsDetailsBinding
import com.synnapps.carouselview.ImageListener

class HotDealsDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHotdealsDetailsBinding
    private var page: String? = null
    private var productID: String? = ""
    private var number: String? = null
    private var products: HotDealsModel? = null
    lateinit var url: Array<String>
    var utilitys = Utilitys()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHotdealsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productID = intent.getStringExtra(AppConstants.pid)
        page = intent.getStringExtra("page")
        getProductDetails(productID)
        binding.hdClBtn.setOnClickListener {
            utilitys.navigateCall(
                this,
                products!!.number,
                products!!.pname
            )
        }
        binding.hdWhtsappBtn.setOnClickListener {
            utilitys.navigateWhatsapp(
                this,
                products!!.number,
                products!!.pname
            )
        }
        binding.ivHotBack.setOnClickListener{ finish() }
    }

    private fun getProductDetails(productID: String?) {
        val productsRef: DatabaseReference = if (page == "3") {
            FirebaseDatabase.getInstance().reference.child("Corosels")
        } else {
            FirebaseDatabase.getInstance().reference.child(AppConstants.hotdeals)
        }
        productsRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    products = dataSnapshot.getValue(HotDealsModel::class.java)
                    binding.hotNameDetails.text = products?.pname
                    binding.hotPriceDetails.text = products?.price
                    binding.hotDescriptionDetails.text = products?.description
                    binding.tvTopbarProductName.text = products?.pname
                    binding.hotSizeDetails.text = products?.propertysize
                    binding.tvHotOwner.text = products?.ownerName
                    binding.tvHotLocation.text = products?.location
                    binding.tvHotTimings.text = products?.timings
                    url = products?.image2?.split("---".toRegex())!!.dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    binding.hotDetailsCarouselView.setImageListener(imageListener)
                    binding.hotDetailsCarouselView.pageCount = url.size
                    number = products!!.number
                    binding.tvOwnerBroker.text = products!!.postedby
                    binding.tvTypeofPost.text = products!!.type
                    if (products!!.text1 == null || products!!.text1 == "") {
                        binding.tvSpecialOption1.visibility = View.GONE
                    } else {
                        binding.tvSpecialOption1.text = products!!.text1
                    }
                    if (products!!.text2 == null || products!!.text2 == "") {
                        binding.tvSpecialOption2.visibility = View.GONE
                    } else {
                        binding.tvSpecialOption2.text = products!!.text1
                    }
                    binding.tvHotPostedDate.text = "Posted on " + products!!.date
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@HotDealsDetailsActivity)
            .load(url[position])
            .into(imageView)
    }
}