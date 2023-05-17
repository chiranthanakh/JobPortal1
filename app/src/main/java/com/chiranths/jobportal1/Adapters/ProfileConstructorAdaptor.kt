package com.chiranths.jobportal1.Adapters

import com.chiranths.jobportal1.Model.ConstructionModel
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Utilitys
import android.view.ViewGroup
import android.view.LayoutInflater
import com.chiranths.jobportal1.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.widget.TextView
import android.widget.LinearLayout
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class ProfileConstructorAdaptor(
    private val productInfos: ArrayList<ConstructionModel?>,
    private var context: Context
) : RecyclerView.Adapter<ProfileConstructorAdaptor.ViewHolder>() {
    var utilitys = Utilitys()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.profile_construction_adaptor, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val productInfo = productInfos[position]
        Glide.with(context)
            .load(productInfo?.image)
            .into(holder.iv_vehicle_image)
        holder.travel_vehicle_name.text = productInfo?.category
        holder.tv_travel_category.text = productInfo?.experience
        holder.tv_cost_km.text = productInfo?.contactDetails
        holder.tv_vehicle_number.text = productInfo?.cost

        holder.ll_construction_delete.setOnClickListener{
            FirebaseDatabase.getInstance().reference.child("constructionforyou").child(
                productInfo?.pid.toString()
            ).child("Status").setValue("2")
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_vehicle_image: ImageView
        var iv_const_whatsapp_bottom: ImageView? = null
        var iv_const_call_bottom: ImageView? = null
        var travel_vehicle_name: TextView
        var tv_travel_category: TextView
        var tv_cost_km: TextView
        var tv_vehicle_number: TextView
        var tv_discription: TextView? = null
        var ll_construction_delete: LinearLayout

        init {
            travel_vehicle_name = itemView.findViewById(R.id.travel_vehicle_name)
            tv_travel_category = itemView.findViewById(R.id.tv_travel_category)
            tv_cost_km = itemView.findViewById(R.id.tv_cost_km)
            tv_vehicle_number = itemView.findViewById(R.id.tv_vehicle_number)
            iv_vehicle_image = itemView.findViewById(R.id.iv_vehicle_image)
            ll_construction_delete = itemView.findViewById(R.id.ll_construction_delete)
        }
    }

}