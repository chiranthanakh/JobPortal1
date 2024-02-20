package com.sbd.gbd.Activities.BasicActivitys

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class CoroselDetailsActivity : AppCompatActivity() {

    private var productID: String? = ""
    private lateinit var url: Array<String>
    var carouselView: CarouselView? = null
    var imageuls: String? = ""
    var pname: String? = ""
    var discription: String? = ""
    var tv_heading: TextView? = null
    var tv_discription: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corosel_details)

        initilize()
        productID = intent.getStringExtra(AppConstants.pid)
        imageuls = intent.getStringExtra(AppConstants.image)
        url = imageuls!!.split("---".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        pname = intent.getStringExtra(AppConstants.pname)
        discription = intent.getStringExtra(AppConstants.description)

        tv_heading?.setText(pname)
        tv_discription?.setText(discription)
        carouselView?.setImageListener(imageListener)
        carouselView?.pageCount = url.size
    }

    private fun initilize() {
        carouselView = findViewById(R.id.details_carouselView)
        tv_heading = findViewById(R.id.tv_carosel_heading)
        tv_discription = findViewById(R.id.tv_carosel_details)
    }

    var imageListener = ImageListener { position, imageView ->
        Glide.with(this@CoroselDetailsActivity)
            .load(url[position])
            .into(imageView)
    }
}