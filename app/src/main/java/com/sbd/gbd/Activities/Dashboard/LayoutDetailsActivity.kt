package com.sbd.gbd.Activities.Dashboard

import android.os.Bundle
import android.util.Log
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
        binding.llWhatsapp.setOnClickListener {
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
            FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        }
        Log.d("checkProduction","123")
        productsRef.child(productID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(LayoutModel::class.java) != null) {
                    val products = dataSnapshot.getValue(LayoutModel::class.java)
                    Log.d("checkProduction",products.toString())
                    binding.productNameDetails.text = products!!.pname
                    binding.productPriceDetails.text = "Rs." + products.price
                    binding.productDescriptionDetails.text = products.description
                    binding.tvTopbarProductName.text = products.pname
                    binding.adsPlaceLocation.text = products.location
                    binding.adsSizeDetails.text = products.propertysize
                    binding.tvPropType.text = products.type
                    binding.tvLayoutFacing.text = products.facing
                    binding.tvSitesAvailable.text = products.availableSites
                    binding.tvLayoutArea.text = products.layoutarea
                    binding.adsSizeKatha.text = products.katha
                    binding.tvLayoutFeatures.text = products.additionalPoints
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
                       // binding.adsDetailsVerifyed.visibility = View.GONE
                    } else {
                        //ads_details_not_verified.setVisibility(View.GONE);
                    }
                    number = products.number
                    binding.tvContactWho.text = products.postedBy
                    if(products.pool.equals("true")) {
                        binding.tvFutures1.text = "Swiming Pool available"
                    } else {
                        binding.tvFutures1.text = "Swiming Pool not available"
                    }
                    if(products.garden.equals("true")) {
                        binding.tvFutures2.text = "garden / play area available"
                    } else {
                        binding.tvFutures2.text = "garden / play area  Not available"
                    }
                    if(products.clubhouse.equals("true")) {
                        binding.tvFutures3.text = "clubhouse available"
                    } else {
                        binding.tvFutures3.text = "clubhouse Not available"
                    }
                    if(products.gym.equals("true")) {
                        binding.tvFutures4.text = "gym available"
                    } else {
                        binding.tvFutures4.text = "gym Not available"
                    }
                   // binding.tvFutures4.text = "Ready for registration"

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@LayoutDetailsActivity)
            .load(url[position])
            .fitCenter()
            .into(imageView)
    }
}