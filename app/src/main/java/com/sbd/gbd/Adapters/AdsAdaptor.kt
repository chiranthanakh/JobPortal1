package com.sbd.gbd.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sbd.gbd.Activities.Dashboard.AdsDetailsActivity
import com.sbd.gbd.Model.AdsModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants

class AdsAdaptor(noticeBoardList: ArrayList<AdsModel>, context: Context?) :
    RecyclerView.Adapter<AdsAdaptor.ViewHolder>() {
    private val noticeBoardList: ArrayList<AdsModel>
    private var context: Context? = null

    init {
        this.noticeBoardList = noticeBoardList
        if (context != null) {
            this.context = context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem = layoutInflater.inflate(R.layout.ads_recyclarview_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val noticeimage = noticeBoardList[position].toString()

        val adsModel = noticeBoardList[position]
        val data = noticeimage.split("---".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        context?.let {
            Glide.with(it)
                .load(adsModel.image)
                .apply(RequestOptions().override(500, 500))
                .into(holder.iv_corosel_image)
        }

        holder.tv_ads_category.text = adsModel.category
        holder.tv_amount.text = adsModel.price
        holder.tv_space.text = adsModel.propertysize
        holder.ads_location_adaptor.text = adsModel.location
        holder.tv_ads_name.text = adsModel.pname

        if (adsModel.verified == "1") {
            holder.tv_ads_verification.text = "Not Verified"
            holder.tv_ads_verification.setTextColor(Color.parseColor("#FDDA0D"))
        } else if (adsModel.verified == "2") {
            holder.tv_ads_verification.text = "Verified Property"
            holder.tv_ads_verification.setTextColor(Color.parseColor("#228B22"))
        }

        holder.cv_card.setOnClickListener {
            val intent = Intent(context, AdsDetailsActivity::class.java)
            intent.putExtra(AppConstants.pid, adsModel.pid)
            intent.putExtra("page", "1")
            context?.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return noticeBoardList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_corosel_image: ImageView = itemView.findViewById(R.id.iv_ads_image)
        var tv_ads_name: TextView = itemView.findViewById(R.id.tv_layout_name)
        var tv_ads_category: TextView = itemView.findViewById(R.id.tv_ads_category)
        var tv_amount: TextView = itemView.findViewById(R.id.tv_ads_amount)
        var tv_space: TextView = itemView.findViewById(R.id.tv_ads_spaces)
        var ads_location_adaptor: TextView = itemView.findViewById(R.id.ads_location_adaptor)
        var tv_ads_verification: TextView = itemView.findViewById(R.id.tv_layout_name)
        var cv_card: CardView = itemView.findViewById(R.id.cv_card)
        var ll_enquiry: LinearLayout? = null

        init {
            //this.ll_enquiry = itemView.findViewById(R.id.ll_enquiry);
        }
    }
}
