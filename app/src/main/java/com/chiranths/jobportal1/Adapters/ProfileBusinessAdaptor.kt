package com.chiranths.jobportal1.Adapters

import com.chiranths.jobportal1.Model.BusinessModel
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.chiranths.jobportal1.R
import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.chiranths.jobportal1.Activities.Businesthings.BusinessDetailsActivity
import androidx.cardview.widget.CardView
import android.widget.TextView
import android.widget.LinearLayout
import com.google.firebase.database.FirebaseDatabase

class ProfileBusinessAdaptor(
    private val productInfos: List<BusinessModel>,
    private var context: Context
) : RecyclerView.Adapter<ProfileBusinessAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.profile_business_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val propertyinfo = productInfos[position]
        Glide.with(context)
            .load(propertyinfo.image)
            .into(holder.business_image)
        holder.tv_business_type.text = propertyinfo.Business_category
        holder.tv_business_locatin.text = propertyinfo.location
        holder.tv_business_name.text = propertyinfo.Businessname
        holder.tv_business_servicess.text = propertyinfo.description
        if (propertyinfo.status.equals("2")) {
            holder.ll_business_delete.visibility = View.GONE
        }
        holder.ll_business_delete.setOnClickListener {
            propertyinfo.pid?.let { it1 ->
                FirebaseDatabase.getInstance().reference.child("BusinessListing").child(
                    it1
                ).child("status").setValue("2")
            }
        }

    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var business_image: ImageView
        var iv_call_business: ImageView? = null
        var iv_whatsup_business: ImageView? = null
        var cv_layout: CardView
        var tv_business_locatin: TextView
        var tv_business_name: TextView
        var tv_business_type: TextView
        var tv_business_servicess: TextView
        var ll_business_delete: LinearLayout

        init {
            tv_business_locatin = itemView.findViewById(R.id.tv_business_locatin)
            tv_business_name = itemView.findViewById(R.id.tv_business_name)
            tv_business_type = itemView.findViewById(R.id.tv_business_type)
            cv_layout = itemView.findViewById(R.id.cv_layout2)
            business_image = itemView.findViewById(R.id.business_image)
            tv_business_servicess = itemView.findViewById(R.id.business_servicess)
            ll_business_delete = itemView.findViewById(R.id.ll_business_delete)
            //iv_call_business = itemView.findViewById(R.id.iv_call_bottom_business);
            //iv_whatsup_business = itemView.findViewById(R.id.iv_whatsapp_bottom_business);
        }
    }
}