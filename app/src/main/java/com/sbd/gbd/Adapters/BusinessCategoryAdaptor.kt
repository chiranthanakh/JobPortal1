package com.sbd.gbd.Adapters

import com.sbd.gbd.Model.Categorymmodel
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Activities.Businesthings.BusinessActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import com.sbd.gbd.R
import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sbd.gbd.Activities.Businesthings.BusinessFilter
import android.widget.TextView

class BusinessCategoryAdaptor(
    private val productInfos: List<Categorymmodel>,
    private var context: Context
) : RecyclerView.Adapter<BusinessCategoryAdaptor.ViewHolder>() {
    var businessActivity = BusinessActivity()
    var bundle = Bundle()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.business_gridlayout_adaptor, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val businessInfo = productInfos[position]
        Glide.with(context)
            .load(businessInfo.image)
            .into(holder.business_image)

        holder.tv_business_cat.text = businessInfo.category

        holder.ll_bus_category.setOnClickListener{
            val intent = Intent(context, BusinessFilter::class.java)
            bundle.putString("center", holder.tv_business_cat.text.toString())
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var business_image: ImageView
        var tv_business_cat: TextView
        var ll_bus_category : LinearLayout

        init {
            tv_business_cat = itemView.findViewById(R.id.tv_business_category)
            business_image = itemView.findViewById(R.id.iv_grid_image)
            ll_bus_category = itemView.findViewById(R.id.ll_bus_category)
        }
    }
}