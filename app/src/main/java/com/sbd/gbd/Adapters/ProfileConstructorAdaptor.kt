package com.sbd.gbd.Adapters

import com.sbd.gbd.Model.ConstructionModel
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Utilitys.Utilitys
import android.view.ViewGroup
import android.view.LayoutInflater
import com.sbd.gbd.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.widget.TextView
import android.widget.LinearLayout
import com.sbd.gbd.Utilitys.AppConstants
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
        holder.tv_cost_km.text = productInfo?.product_services
        holder.tv_vehicle_number.text = productInfo?.address

        if(productInfo?.status == "2"){
            holder.ll_remove.visibility = View.VISIBLE
            holder.ll_text_msg.visibility = View.GONE
        } else if(productInfo?.status == "1") {
            holder.ll_text_msg.visibility = View.VISIBLE
            holder.ll_remove.visibility = View.GONE
        } else {
            holder.ll_text_msg.visibility = View.VISIBLE
            holder.tv_msg.text = "You deleted this post"
            holder.ll_remove.visibility = View.GONE
            holder.ll_remove.visibility = View.GONE
        }

        holder.ll_remove.setOnClickListener{
            FirebaseDatabase.getInstance().reference.child(AppConstants.construction).child(
                productInfo?.pid.toString()
            ).child(AppConstants.Status).setValue("3")
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_vehicle_image: ImageView
        var travel_vehicle_name: TextView
        var tv_travel_category: TextView
        var tv_cost_km: TextView
        var tv_vehicle_number: TextView
        var tv_discription: TextView? = null
        var tv_msg : TextView
        var ll_remove : LinearLayout
        var ll_text_msg: LinearLayout

        init {
            travel_vehicle_name = itemView.findViewById(R.id.travel_vehicle_name)
            tv_travel_category = itemView.findViewById(R.id.tv_travel_category)
            tv_cost_km = itemView.findViewById(R.id.tv_cost_km)
            tv_vehicle_number = itemView.findViewById(R.id.tv_vehicle_number)
            iv_vehicle_image = itemView.findViewById(R.id.iv_vehicle_image)
            ll_remove = itemView.findViewById(R.id.ll_remove)
            ll_text_msg = itemView.findViewById(R.id.ll_text_msg)
            tv_msg = itemView.findViewById(R.id.tv_msg)
        }
    }

}