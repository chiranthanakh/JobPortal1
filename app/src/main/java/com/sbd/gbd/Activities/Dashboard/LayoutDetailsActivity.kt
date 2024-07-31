package com.sbd.gbd.Activities.Dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Utilitys.CalldetailsRecords
import com.sbd.gbd.Model.LayoutModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sbd.gbd.databinding.ActivityLayoutsDetailsBinding
import com.synnapps.carouselview.ImageListener

class LayoutDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLayoutsDetailsBinding
    private var productID: String? = ""
    private var number: String? = null
    private var page: String? = null
    private lateinit var url: Array<String>
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLayoutsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productID = intent.getStringExtra(AppConstants.pid)
        page = intent.getStringExtra("page")
        getProductDetails(productID)
        binding.adsClBtn.setOnClickListener {
            utilitys.navigateCall(
                this@LayoutDetailsActivity,
                number,
                binding.productNameDetails.text.toString()
            )
        }
        binding.adsWhtsappBtn.setOnClickListener {
            utilitys.navigateWhatsapp(
                this@LayoutDetailsActivity,
                number,
                binding.productNameDetails.text.toString()
            )
        }
        binding.ivBackAds.setOnClickListener { finish() }
    }

    private fun getProductDetails(productID: String?) {
        val productsRef: DatabaseReference = if (page == "3") {
            FirebaseDatabase.getInstance().reference.child("Corosels")
        } else {
            FirebaseDatabase.getInstance().reference.child(AppConstants.layouts)
        }
        productsRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(LayoutModel::class.java) != null) {
                    val products = dataSnapshot.getValue(LayoutModel::class.java)
                    binding.productNameDetails.text = products!!.pname
                    binding.productPriceDetails.text = "Rs." + products.price
                    binding.productDescriptionDetails.text = products.description
                    binding.tvTopbarProductName.text = products.pname
                    binding.adsPlaceLocation.text = products.location
                    binding.adsSizeDetails.text = products.propertysize
                    binding.tvPropType.text = products.category
                    binding.tvLayoutFacing.text = products.facing
                    binding.tvSitesAvailable.text = products.sitesAvailable
                    binding.tvLayoutArea.text = products.layoutarea
                    binding.adsSizeKatha.text = products.katha
                    if (products.date == null) {
                        binding.tvAdsPostedOn.visibility = View.GONE
                    } else {
                        binding.tvAdsPostedOn.text = "Posted on " + products.date
                    }
                    url = products.image2!!.split("---".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    binding.adsDetailsCarouselView.setImageListener(imageListener)
                    binding.adsDetailsCarouselView.pageCount = url.size
                    val test = 1 //Integer.parseInt(products.getStatus());
                    if (test == 1) {
                        binding.adsDetailsVerifyed.visibility = View.GONE
                    } else {
                        //ads_details_not_verified.setVisibility(View.GONE);
                    }
                    number = products.number
                    binding.tvContactWho.text = products.postedBy
                    if (products.point1 != null) {
                        binding.tvFutures1.text = products.point1
                        binding.tvFutures2.text = products.point2
                        binding.tvFutures3.text = products.point3
                        binding.tvFutures4.text = products.point4
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@LayoutDetailsActivity)
            .load(url[position])
            .into(imageView)
    }
}