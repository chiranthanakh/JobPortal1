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
import com.google.firebase.database.FirebaseDatabase
import com.sbd.gbd.Activities.Dashboard.AdsDetailsActivity
import com.sbd.gbd.Activities.Dashboard.LayoutDetailsActivity
import com.sbd.gbd.Model.FilterModel
import com.sbd.gbd.Utilitys.CalldetailsRecords
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys

class PropertyAdaptor(private val productInfos: List<FilterModel>, private var context: Context) :
    RecyclerView.Adapter<PropertyAdaptor.ViewHolder>() {
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.product_items_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val propertyinfo = productInfos[position]
        val imagesdata = propertyinfo.image
        val imageurl = propertyinfo.image
        Glide.with(context)
            .load(imageurl)
            .into(holder.iv_image)
        holder.product_type1.text = propertyinfo.type
        holder.product_location1.text = propertyinfo.location
        holder.product_price.text = propertyinfo.price
        holder.product_size1.text = propertyinfo.size
        holder.product_name.text = propertyinfo.pname
        holder.cv_layout.setOnClickListener {
            if(propertyinfo.type == AppConstants.layoutsname ) {
                val intent = Intent(context, LayoutDetailsActivity::class.java)
                intent.putExtra(AppConstants.pid, propertyinfo.pid)
                intent.putExtra("page", "2")
                context.startActivity(intent)
            } else {
                val intent = Intent(context, AdsDetailsActivity::class.java)
                intent.putExtra(AppConstants.pid, propertyinfo.pid)
                intent.putExtra("page", "2")
                context.startActivity(intent)
            }
        }

        if (!propertyinfo.status.isNullOrEmpty()) {
            if (propertyinfo.status.equals("1")) {
                holder.ll_approve.visibility = View.VISIBLE
                holder.ll_call?.visibility = View.GONE
            } else if (propertyinfo.status.equals("3")) {
                holder.ll_approve.visibility = View.VISIBLE
                holder.ll_call?.visibility = View.GONE
            } else {
                holder.ll_approve.visibility = View.GONE
                holder.ll_call?.visibility = View.VISIBLE
            }
        }

        holder.ll_approve.setOnClickListener {

            propertyinfo.pid?.let { it1 ->
                FirebaseDatabase.getInstance().reference.child(AppConstants.products).child(it1)
                    .child(AppConstants.Status).setValue("2")
            }

        }

        //calling function
        holder.property_btn_call.setOnClickListener { view: View? ->
            utilitys.navigateCall(
                context, propertyinfo.number, propertyinfo.pid
            )
        }

        //whatsapp function
        holder.property_btn_whatsapp.setOnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                context, propertyinfo.number, propertyinfo.pid
            )
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_image: ImageView
        var property_btn_whatsapp: ImageView
        var property_btn_call: ImageView
        var cv_layout: CardView
        var product_name: TextView
        var product_price: TextView
        var product_size1: TextView
        var product_location1: TextView
        var product_type1: TextView
        var ll_approve: LinearLayout
        var ll_call: LinearLayout

        init {
            property_btn_whatsapp = itemView.findViewById(R.id.property_btn_whatsapp)
            property_btn_call = itemView.findViewById(R.id.property_btn_call)
            iv_image = itemView.findViewById(R.id.product_image)
            product_name = itemView.findViewById(R.id.product_name1)
            product_price = itemView.findViewById(R.id.product_price)
            product_size1 = itemView.findViewById(R.id.product_size1)
            product_location1 = itemView.findViewById(R.id.product_location1)
            product_type1 = itemView.findViewById(R.id.product_type1)
            cv_layout = itemView.findViewById(R.id.cv_layout)
            ll_approve = itemView.findViewById(R.id.ll_approve)
            ll_call = itemView.findViewById(R.id.ll_call)
        }
    }
}
