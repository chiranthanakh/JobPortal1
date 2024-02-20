package com.sbd.gbd.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sbd.gbd.Activities.Businesthings.BusinessDetails
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys
import com.google.firebase.database.FirebaseDatabase

class BusinessAdaptor(private val productInfos: List<BusinessModel>, private var context: Context) :
    RecyclerView.Adapter<BusinessAdaptor.ViewHolder>() {
    var utilitys = Utilitys()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.business_items_layout, parent, false)
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
        holder.cv_layout.setOnClickListener {
            val intent = Intent(context, BusinessDetails::class.java)
            intent.putExtra(AppConstants.pid, propertyinfo.pid)
            context.startActivity(intent)
        }

        holder.iv_call_business.setOnClickListener { view: View? ->
            utilitys.navigateCall(
                    context, propertyinfo.number, propertyinfo.owner
            )
        }

        holder.iv_whatsup_business.setOnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                    context, propertyinfo.number, propertyinfo.owner
            )
        }
        if(propertyinfo.status.equals("2")) {
            holder.ll_approve.visibility = View.GONE
        } else if(propertyinfo.status.equals("1")) {
            holder.ll_approve.visibility = View.VISIBLE
        }

        holder.ll_approve.setOnClickListener { view: View? ->
            propertyinfo.pid?.let {
                FirebaseDatabase.getInstance().reference.child("BusinessListing").child(it)
                    .child(AppConstants.Status).setValue("2")
            }
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var business_image: ImageView
        var iv_call_business: ImageView
        var iv_whatsup_business: ImageView
        var cv_layout: CardView
        var tv_business_locatin: TextView
        var tv_business_name: TextView
        var tv_business_type: TextView
        var tv_business_servicess: TextView
        var ll_approve : LinearLayout

        init {
            tv_business_locatin = itemView.findViewById(R.id.tv_business_locatin)
            tv_business_name = itemView.findViewById(R.id.tv_business_name)
            tv_business_type = itemView.findViewById(R.id.tv_business_type)
            cv_layout = itemView.findViewById(R.id.cv_layout2)
            business_image = itemView.findViewById(R.id.business_image)
            tv_business_servicess = itemView.findViewById(R.id.business_servicess)
            iv_call_business = itemView.findViewById(R.id.iv_call_bottom_business)
            iv_whatsup_business = itemView.findViewById(R.id.iv_whatsapp_bottom_business)
            ll_approve = itemView.findViewById(R.id.ll_approve)
        }
    }
}
