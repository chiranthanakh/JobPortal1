package com.sbd.gbd.Adapters

import android.annotation.SuppressLint
import android.content.Context
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
import com.sbd.gbd.Model.TravelsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys

class TravelsAdaptor(productInfos: ArrayList<TravelsModel>, context: Context) :
    RecyclerView.Adapter<TravelsAdaptor.ViewHolder>() {
    private val productInfos: List<TravelsModel>
    private var context: Context
    var utilitys = Utilitys()

    init {
        this.productInfos = productInfos
        this.context = context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.activity_travels_adaptor, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val productInfo = productInfos[position]
        Glide.with(context)
            .load(productInfo.image)
            .into(holder.iv_vehicle_image)
        holder.travel_vehicle_name.text = productInfo.vehicle
        holder.tv_travel_category.text = productInfo.category
        holder.tv_cost_km.text = productInfo.costperKM
        holder.tv_vehicle_number.text = productInfo.vehicleNumber
        if (productInfo.status.equals("1")) {
            holder.ll_approve.visibility = View.VISIBLE
            holder.ll_call?.visibility = View.GONE
        } else if (productInfo.status.equals("3")) {
            holder.ll_approve.visibility = View.VISIBLE
            holder.ll_call?.visibility = View.GONE
        } else {
            holder.ll_approve.visibility = View.GONE
            holder.ll_call?.visibility = View.VISIBLE
        }

        holder.ll_approve.setOnClickListener {
            productInfo.pid?.let { it1 ->
                FirebaseDatabase.getInstance().reference.child("travelsforyou").child(it1)
                    .child(AppConstants.Status).setValue("2")
            }
        }
        holder.iv_const_call_bottom.setOnClickListener { view: View? ->
            utilitys.navigateCall(
                context,
                productInfo.contactDetails,
                productInfo.ownerName
            )
        }
        holder.iv_const_whatsapp_bottom.setOnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                context,
                productInfo.contactDetails,
                productInfo.ownerName
            )
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_vehicle_image: ImageView
        var iv_const_whatsapp_bottom: ImageView
        var iv_const_call_bottom: ImageView
        var cv_deals: CardView? = null
        private val btn_applayLoan: LinearLayout? = null
        var btn_vehicle_contact: LinearLayout? = null
        var travel_vehicle_name: TextView
        var tv_travel_category: TextView
        var tv_cost_km: TextView
        var tv_vehicle_number: TextView
        var tv_discription: TextView? = null
        var ll_approve : LinearLayout
        var ll_call : LinearLayout? = null

        init {
            ll_call = itemView.findViewById(R.id.ll_call)
            travel_vehicle_name = itemView.findViewById(R.id.travel_vehicle_name)
            tv_travel_category = itemView.findViewById(R.id.tv_travel_category)
            tv_cost_km = itemView.findViewById(R.id.tv_cost_km)
            tv_vehicle_number = itemView.findViewById(R.id.tv_vehicle_number)
            iv_vehicle_image = itemView.findViewById(R.id.iv_vehicle_image)
            iv_const_whatsapp_bottom = itemView.findViewById(R.id.iv_const_whatsapp_bottom)
            iv_const_call_bottom = itemView.findViewById(R.id.iv_const_call_bottom)
            ll_approve = itemView.findViewById(R.id.ll_approve)
        }
    }
}
