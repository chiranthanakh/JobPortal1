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
import com.sbd.gbd.Activities.Construction.ConstructionDetailsActivity
import com.sbd.gbd.Model.ConstructionModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys

class ConstructorAdaptor(
    private val productInfos: ArrayList<ConstructionModel>,
    private var context: Context
) : RecyclerView.Adapter<ConstructorAdaptor.ViewHolder>() {
    var utilitys = Utilitys()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.activity_construction_adaptor, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val productInfo = productInfos[position]
        Glide.with(context)
            .load(productInfo.image)
            .into(holder.iv_vehicle_image)
        holder.travel_vehicle_name.text = productInfo.name
        holder.tv_travel_category.text = productInfo.category
        holder.tv_cost_km.text = productInfo.cost
        holder.tv_vehicle_number.text = productInfo.experience
        holder.iv_const_call_bottom.setOnClickListener { view: View? ->
            utilitys.navigateCall(
                context, productInfo.contactDetails, productInfo.name
            )
        }

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
            productInfo.pid.let { it1 ->
                if (it1 != null) {
                    FirebaseDatabase.getInstance().reference.child(AppConstants.construction).child(it1)
                        .child(AppConstants.Status).setValue("2")
                }
            }
        }

        holder.iv_const_whatsapp_bottom.setOnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                context, productInfo.contactDetails, productInfo.name
            )
        }
        holder.cv_card_const.setOnClickListener { view: View? ->
            val intent = Intent(context, ConstructionDetailsActivity::class.java)
            intent.putExtra(AppConstants.pid, productInfo.pid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_vehicle_image: ImageView
        var iv_const_whatsapp_bottom: ImageView
        var iv_const_call_bottom: ImageView
        var travel_vehicle_name: TextView
        var tv_travel_category: TextView
        var tv_cost_km: TextView
        var tv_vehicle_number: TextView
        var tv_discription: TextView? = null
        var cv_card_const: CardView
        var ll_approve: LinearLayout
        var ll_call: LinearLayout

        init {
            travel_vehicle_name = itemView.findViewById(R.id.travel_vehicle_name)
            tv_travel_category = itemView.findViewById(R.id.tv_travel_category)
            tv_cost_km = itemView.findViewById(R.id.tv_cost_km)
            tv_vehicle_number = itemView.findViewById(R.id.tv_vehicle_number)
            iv_vehicle_image = itemView.findViewById(R.id.iv_vehicle_image)
            iv_const_whatsapp_bottom = itemView.findViewById(R.id.iv_const_whatsapp_bottom)
            iv_const_call_bottom = itemView.findViewById(R.id.iv_const_call_bottom)
            cv_card_const = itemView.findViewById(R.id.cv_card_const)
            ll_approve = itemView.findViewById(R.id.ll_approve)
            ll_call = itemView.findViewById(R.id.ll_call)
        }
    }
}
