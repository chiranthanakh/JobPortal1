package com.chiranths.jobportal1.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity
import com.chiranths.jobportal1.CalldetailsRecords
import com.chiranths.jobportal1.Model.ProductInfo
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.chiranths.jobportal1.Utilitys.Utilitys

class BottomhomeRecyclarviewAdaptor(
    private val productInfos: ArrayList<ProductInfo>,
    private var context: Context,
) : RecyclerView.Adapter<BottomhomeRecyclarviewAdaptor.ViewHolder?>() {
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.home_event_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val productInfo = productInfos[position]
        Glide.with(context)
            .load(productInfo.image)
            .into(holder.iv_image)
        holder.tv_name_hot.text = productInfo.pname
        holder.tv_final_price_hot.text = productInfo.price
        holder.tv_hot_sqft.text = productInfo.propertysize
        holder.tv_loaction_hot.text = productInfo.location
        holder.tv_contect_whome.text = "Contact: "

        //holder.tv_btn_call_hot.setText("");
        holder.cv_deals.setOnClickListener { view: View? ->
            val intent = Intent(context, HotDealsDetailsActivity::class.java)
            intent.putExtra(AppConstants.pid, productInfo.pid)
            context.startActivity(intent)
        }

        //calling function
        holder.iv_call_bottom.setOnClickListener { view: View? ->
            utilitys.navigateCall(
                context, productInfo.number, productInfo.pname
            )
        }

        //whatsapp function
        holder.iv_whatsapp_bottom.setOnClickListener { view: View? ->
            utilitys.navigateWhatsapp(
                context, productInfo.number, productInfo.pname
            )
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_image: ImageView
        var iv_call_bottom: ImageView
        var iv_whatsapp_bottom: ImageView
        var cv_deals: CardView
        var tv_name_hot: TextView
        var tv_final_price_hot: TextView
        var tv_hot_sqft: TextView
        var tv_loaction_hot: TextView
        var tv_btn_call_hot: TextView? = null
        var tv_contect_whome: TextView

        init {
            iv_image = itemView.findViewById(R.id.iv_location_image)
            cv_deals = itemView.findViewById(R.id.cv_card_deals)
            tv_name_hot = itemView.findViewById(R.id.tv_name_hot)
            tv_final_price_hot = itemView.findViewById(R.id.tv_final_price_hot)
            tv_hot_sqft = itemView.findViewById(R.id.tv_hot_sqft)
            tv_loaction_hot = itemView.findViewById(R.id.tv_loaction_hot)
            iv_call_bottom = itemView.findViewById(R.id.iv_call_bottom)
            iv_whatsapp_bottom = itemView.findViewById(R.id.iv_whatsapp_bottom)
            tv_contect_whome = itemView.findViewById(R.id.tv_contect_whome)
            //tv_btn_call_hot = itemView.findViewById(R.id.tv_btn_call_hot);
        }
    }
}