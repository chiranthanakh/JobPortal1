package com.sbd.gbd.Adapters

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
import com.sbd.gbd.Activities.BasicActivitys.RentHomeDetails
import com.sbd.gbd.Model.LivingPlaceModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys

class LivingPlaceAdaptor(
    private val productInfos: List<LivingPlaceModel>,
    private var context: Context
) : RecyclerView.Adapter<LivingPlaceAdaptor.ViewHolder>() {
    var utilitys = Utilitys()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.recyclar_livingplace_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productInfo = productInfos[position]
        Glide.with(context)
            .load(productInfo.image)
            .into(holder.iv_living_image)
        holder.tv_living_title.text = productInfo.title
        holder.tv_living_rent_amount.text =  productInfo.rentamount
        holder.tv_living_sqft.text = productInfo.sqft
        holder.tv_number_bhk.text = productInfo.nuBHK + "BHK"
        holder.tv_loaction_living.text = productInfo.location
        holder.cv_card_living.setOnClickListener {
            val intent = Intent(context, RentHomeDetails::class.java)
            intent.putExtra(AppConstants.pid, productInfo.pid)
            context.startActivity(intent)
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
            productInfo.pid?.let { it1 ->
                FirebaseDatabase.getInstance().reference.child("livingplaceforyou").child(it1)
                    .child(AppConstants.Status).setValue("2")
            }
        }
        holder.iv_const_call_bottom.setOnClickListener { view: View? ->
            utilitys.navigateCall(
                context,
                productInfo.contactNumber,
                productInfo.postedBy
            )
        }
        holder.iv_const_whatsapp_bottom.setOnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                context,
                productInfo.contactNumber,
                productInfo.postedBy
            )
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_living_image: ImageView
        var iv_call_bottom: ImageView? = null
        var iv_whatsapp_bottom: ImageView? = null
        var cv_card_living: CardView
        var ll_approve: LinearLayout
        var ll_call: LinearLayout
        var tv_living_title: TextView
        var tv_living_rent_amount: TextView
        var tv_living_sqft: TextView
        var tv_loaction_living: TextView
        var tv_number_bhk: TextView
        var iv_const_whatsapp_bottom: ImageView
        var iv_const_call_bottom: ImageView

        init {
            iv_living_image = itemView.findViewById(R.id.iv_living_image)
            cv_card_living = itemView.findViewById(R.id.cv_card_living)
            tv_living_title = itemView.findViewById(R.id.tv_living_title)
            tv_living_rent_amount = itemView.findViewById(R.id.tv_living_rent_amount)
            tv_living_sqft = itemView.findViewById(R.id.tv_living_sqft)
            tv_loaction_living = itemView.findViewById(R.id.tv_loaction_living)
            tv_number_bhk = itemView.findViewById(R.id.tv_number_bhk)
            ll_approve = itemView.findViewById(R.id.ll_approve)
            ll_call = itemView.findViewById(R.id.ll_call)
            iv_const_whatsapp_bottom = itemView.findViewById(R.id.iv_whatsapp_bottom)
            iv_const_call_bottom = itemView.findViewById(R.id.iv_call_bottom)
        }
    }
}
