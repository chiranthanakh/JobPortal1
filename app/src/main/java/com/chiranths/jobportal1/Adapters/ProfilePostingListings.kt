package com.chiranths.jobportal1.Adapters

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
import com.chiranths.jobportal1.Activities.HotDealsactivity.HotDealsDetailsActivity
import com.chiranths.jobportal1.CalldetailsRecords
import com.chiranths.jobportal1.Interface.MyInterface
import com.chiranths.jobportal1.Interface.OnItemClick
import com.chiranths.jobportal1.Model.ProfileListModel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants
import com.chiranths.jobportal1.Utilitys.Utilitys
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class ProfilePostingListings(
    private val productInfos: ArrayList<ProfileListModel?>,
    private var context: Context,
    private val number: String,
    private val name: String
) : RecyclerView.Adapter<ProfilePostingListings.ViewHolder>() {
    var calldetails = CalldetailsRecords()
    var utilitys = Utilitys()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.profile_listings, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val productInfo = productInfos[position]
        Glide.with(context)
            .load(productInfo?.image)
            .into(holder.iv_image)
        holder.tv_name_hot.text = productInfo?.pname
        holder.tv_final_price_hot.text = productInfo?.price
        holder.tv_hot_sqft.text = productInfo?.propertysize
        holder.tv_loaction_hot.text = productInfo?.location


        if(productInfo?.status == "2"){
            holder.ll_remove.visibility = View.VISIBLE
            holder.iv_delete_btn.visibility = View.GONE
            holder.btn_removed.visibility = View.VISIBLE
            holder.ll_text_msg.visibility = View.GONE
        } else if(productInfo?.status == "1") {
            holder.ll_text_msg.visibility = View.VISIBLE
            holder.iv_delete_btn.visibility = View.VISIBLE
            holder.btn_removed.visibility = View.GONE
            holder.ll_remove.visibility = View.GONE
        } else {
            holder.ll_text_msg.visibility = View.VISIBLE
            holder.tv_msg.text = "You deleted this post"
            holder.btn_removed.visibility = View.GONE
            holder.ll_remove.visibility = View.GONE
            holder.ll_remove.visibility = View.GONE
        }
        //holder.tv_btn_call_hot.setText("");
        /*holder.cv_deals.setOnClickListener {
            val intent = Intent(context, HotDealsDetailsActivity::class.java)
            intent.putExtra(AppConstants.pid, productInfo?.pid)
            context.startActivity(intent)
        }*/
        holder.iv_delete_btn.setOnClickListener { view: View? ->
             FirebaseDatabase.getInstance().reference.child(productInfo?.listedFrom.toString()).child(
                 productInfo?.pid.toString()
             ).child(AppConstants.Status).setValue("3")
        }
        holder.ll_remove.setOnClickListener { view: View? ->
            FirebaseDatabase.getInstance().reference.child(productInfo?.listedFrom.toString()).child(
                productInfo?.pid.toString()
            ).child(AppConstants.Status).setValue("3")
        }
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_image: ImageView
        var iv_delete_btn: MaterialButton
        var cv_deals: CardView
        var tv_name_hot: TextView
        var tv_final_price_hot: TextView
        var tv_hot_sqft: TextView
        var tv_loaction_hot: TextView
        var btn_removed : TextView
        var tv_msg : TextView
        var ll_remove : LinearLayout
        var ll_text_msg: LinearLayout

        init {
            iv_image = itemView.findViewById(R.id.iv_location_image)
            cv_deals = itemView.findViewById(R.id.cv_card_deals)
            tv_name_hot = itemView.findViewById(R.id.tv_name_hot)
            tv_final_price_hot = itemView.findViewById(R.id.tv_final_price_hot)
            tv_hot_sqft = itemView.findViewById(R.id.tv_hot_sqft)
            tv_loaction_hot = itemView.findViewById(R.id.tv_loaction_hot)
            iv_delete_btn = itemView.findViewById(R.id.iv_delete_btn)
            btn_removed = itemView.findViewById(R.id.iv_btn_removed)
            ll_remove = itemView.findViewById(R.id.ll_remove)
            ll_text_msg = itemView.findViewById(R.id.ll_text_msg)
            tv_msg = itemView.findViewById(R.id.tv_msg)
        }
    }
}