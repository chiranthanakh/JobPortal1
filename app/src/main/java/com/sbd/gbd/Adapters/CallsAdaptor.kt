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
import com.sbd.gbd.Activities.LoanActivity.LoanForm
import com.sbd.gbd.Model.CallerDetailsModel
import com.sbd.gbd.Model.LoanOffersModel
import com.sbd.gbd.R

class CallsAdaptor(private val productInfos: List<CallerDetailsModel>, private var context: Context) :
    RecyclerView.Adapter<CallsAdaptor.ViewHolder>() {
    private val number: String? = ""
    private val name: String? = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.calls_adaptor, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val productInfo = productInfos[position]
        holder.tv_caller.text = productInfo.called_number
        holder.tv_called.text = productInfo.caller_number
        holder.tv_product.text = productInfo.caller_name
        holder.tv_time.text = productInfo.time+"-"+productInfo.date
    }

    override fun getItemCount(): Int {
        return productInfos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_caller: TextView
        var tv_called: TextView
        var tv_product: TextView
        var tv_time: TextView

        init {
            tv_caller = itemView.findViewById(R.id.tv_caller)
            tv_called = itemView.findViewById(R.id.tv_called)
            tv_product = itemView.findViewById(R.id.tv_product)
            tv_time = itemView.findViewById(R.id.tv_time)
        }
    }
}
