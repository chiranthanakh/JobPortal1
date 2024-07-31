package com.sbd.gbd.Activities.Construction

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.Utilitys.CalldetailsRecords
import com.sbd.gbd.Model.ConstructionModel
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import com.sbd.gbd.R
import com.sbd.gbd.databinding.ActivityConstDetailsBinding
import com.sbd.gbd.databinding.ActivityConstructionBinding
import com.sbd.gbd.databinding.ActivityStartingBinding
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class ConstructionDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConstDetailsBinding
    private var productID: String? = ""
    private var number: String? = null
    private var page: String? = null
    private lateinit var url: Array<String>
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productID = intent.getStringExtra(AppConstants.pid)
        page = intent.getStringExtra("page")
        getProductDetails(productID)
        binding.adsClBtn.setOnClickListener {
            utilitys.navigateCall(
                this@ConstructionDetailsActivity,
                number,
                binding.productNameDetails.text.toString()
            )
        }
        binding.adsWhtsappBtn.setOnClickListener {
            utilitys.navigateWhatsapp(
                this@ConstructionDetailsActivity,
                number,
                binding.productNameDetails.text.toString()
            )
        }
        binding.ivBackAds.setOnClickListener { finish() }
    }

    private fun getProductDetails(productID: String?) {
        val productsRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(AppConstants.construction)
        productsRef.child(productID.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products = dataSnapshot.getValue(ConstructionModel::class.java)
                binding.productNameDetails.text = products?.name
                binding.constPrice.text = "Rs." + products?.cost
                binding.productDescriptionDetails.text = products?.description
                binding.tvTopbarProductName.text = products?.name
                binding.constLocation.text = products?.address
                binding.constCategory.text = products?.category
                binding.constGst.text = products?.gst
                binding.tvProductService.text = products?.product_services
                binding.tvOpenHrs.text = products?.workingHrs
                binding.tvExperience.text = products?.experience
                binding.tvOwner.text = products?.owner
                url = products?.image2?.split("---")!!.toTypedArray()
                binding.adsDetailsCarouselView.setImageListener(imageListener)
                binding.adsDetailsCarouselView.pageCount = url.size
                number = products.contactDetails
                if (products.service1 != null && products.service1 !== "") {
                    binding.tvFutures1.text = products.service1
                    binding.tvFutures2.text = products.service2
                    binding.tvFutures3.text = products.service3
                    binding.tvFutures4.text = products.service4
                } else {
                    binding.tvFutures1.visibility = View.GONE
                    binding.tvFutures2.visibility = View.GONE
                    binding.tvFutures3.visibility = View.GONE
                    binding.tvFutures4.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@ConstructionDetailsActivity)
            .load(url[position])
            .into(imageView)
    }
}