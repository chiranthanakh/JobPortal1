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
import androidx.appcompat.widget.AppCompatButton
import com.sbd.gbd.Activities.Construction.ConstructionFilter

class BusinessCategoryAdaptor(
    private val businessType : Int,
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

        holder.busbtn_iness_cat.text = businessInfo.subcategory

        holder.busbtn_iness_cat.setOnClickListener{
            if(businessType == 1) {
                val intent = Intent(context, ConstructionFilter::class.java)
                bundle.putString("center", holder.busbtn_iness_cat.text.toString())
                intent.putExtras(bundle)
                context.startActivity(intent)
            }else {
                val intent = Intent(context, BusinessFilter::class.java)
                bundle.putString("center", holder.busbtn_iness_cat.text.toString())
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var busbtn_iness_cat: AppCompatButton

        init {
            busbtn_iness_cat = itemView.findViewById(R.id.btn_category)
        }
    }
}