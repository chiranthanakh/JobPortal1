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
import com.sbd.gbd.Model.HotelsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.Utilitys

class CenterHomeadaptor(private val productInfos: ArrayList<HotelsModel>, private var context: Context) :
    RecyclerView.Adapter<CenterHomeadaptor.ViewHolder>() {
    var utilitys = Utilitys()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.recyclar_center_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val productInfo = productInfos[position]
        Glide.with(context)
            .load(productInfo.image)
            .into(holder.iv_image)
        holder.tv_name_hot.text = productInfo.name
        holder.tv_final_price_hot.text = productInfo.price
       // holder.tv_hotel_website.text = productInfo.website
        holder.tv_advance.text = productInfo.category
        holder.tv_loaction_hot.text = productInfo.address

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
                FirebaseDatabase.getInstance().reference.child("hotelsforyou").child(it1)
                    .child(AppConstants.Status).setValue("2")
            }
        }
        holder.iv_const_call_bottom.setOnClickListener { view: View? ->
            utilitys.navigateCall(
                context,
                productInfo.Number,
                productInfo.owner
            )
        }
        holder.iv_const_whatsapp_bottom.setOnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                context,
                productInfo.Number,
                productInfo.owner
            )
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_image: ImageView
        var iv_call_bottom: ImageView? = null
        var iv_whatsapp_bottom: ImageView? = null
        var cv_deals: CardView
        var tv_name_hot: TextView
        var tv_final_price_hot: TextView
        var tv_hotel_website: TextView
        var tv_loaction_hot: TextView
        var tv_advance: TextView
        var ll_approve: LinearLayout
        var ll_call: LinearLayout
        var iv_const_whatsapp_bottom: ImageView
        var iv_const_call_bottom: ImageView

        init {
            iv_image = itemView.findViewById(R.id.iv_location_image_center)
            cv_deals = itemView.findViewById(R.id.cv_card_deals_center)
            tv_name_hot = itemView.findViewById(R.id.tv_name_hot_center)
            tv_final_price_hot = itemView.findViewById(R.id.tv_final_price_hot_center)
            tv_hotel_website = itemView.findViewById(R.id.tv_hotel_website)
            tv_loaction_hot = itemView.findViewById(R.id.tv_loaction_hot_center)
            tv_advance = itemView.findViewById(R.id.tv_home_advance)
            ll_approve = itemView.findViewById(R.id.ll_approve)
            ll_call = itemView.findViewById(R.id.ll_call)
            iv_const_whatsapp_bottom = itemView.findViewById(R.id.iv_whatsapp_bottom)
            iv_const_call_bottom = itemView.findViewById(R.id.iv_call_bottom)
        }
    }
}
