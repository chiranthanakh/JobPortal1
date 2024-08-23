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
import com.bumptech.glide.request.RequestOptions
import com.sbd.gbd.Activities.BasicActivitys.SeeAllLayoutActivity
import com.sbd.gbd.Activities.Dashboard.LayoutDetailsActivity
import com.sbd.gbd.Model.LayoutModel
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants

class LayoutsAdaptor(layoutsList: ArrayList<LayoutModel>, context: Context) :
    RecyclerView.Adapter<LayoutsAdaptor.ViewHolder>() {
    private val layoutsList: ArrayList<LayoutModel>
    private var context: Context

    init {
        this.layoutsList = layoutsList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem = layoutInflater.inflate(R.layout.ads_recyclarview_layout, parent, false)
        val viewHolder: ViewHolder = ViewHolder(listItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val layoutinfo = layoutsList[position]
        if (position == getItemCount() - 1 ) {
            holder.cv_card_next.visibility = View.VISIBLE
            holder.cv_card.visibility = View.GONE
            holder.cv_card_next.setOnClickListener {
                val intent11 = Intent(context, SeeAllLayoutActivity::class.java)
                context.startActivity(intent11)
            }
        } else {
        val options = RequestOptions()
        options.fitCenter()

        Glide.with(context)
            .load(layoutinfo.image)
            .apply(RequestOptions().override(1000, 500))
            .into(holder.iv_corosel_image)

        holder.tv_ads_category.text = layoutinfo.type
        holder.tv_amount.text = layoutinfo.price
        holder.tv_space.text = layoutinfo.propertysize
        holder.ads_location_adaptor.text = layoutinfo.location
        holder.tv_layout_name.text = layoutinfo.pname

        holder.cv_card.setOnClickListener {
            val intent = Intent(context, LayoutDetailsActivity::class.java)
            intent.putExtra(AppConstants.pid, layoutinfo.pid)
            intent.putExtra("page", "2")
            context.startActivity(intent)
        }
    }
    }


    override fun getItemCount(): Int {
        return layoutsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_corosel_image: ImageView = itemView.findViewById(R.id.iv_ads_image)
        var tv_ads_category: TextView = itemView.findViewById(R.id.tv_ads_category)
        var tv_amount: TextView = itemView.findViewById(R.id.tv_ads_amount)
        var tv_space: TextView = itemView.findViewById(R.id.tv_ads_spaces)
        var ads_location_adaptor: TextView = itemView.findViewById(R.id.ads_location_adaptor)
        var tv_layout_name: TextView = itemView.findViewById(R.id.tv_layout_name)
        var cv_card: CardView = itemView.findViewById(R.id.cv_card)
        var cv_card_next: CardView = itemView.findViewById(R.id.cv_card_next)

        var ll_enquiry: LinearLayout? = null

        init {
            /// this.ll_enquiry = itemView.findViewById(R.id.ll_enquiry);
        }
    }
}
