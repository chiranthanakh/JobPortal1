package com.chiranths.jobportal1.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chiranths.jobportal1.Activities.BasicActivitys.LayoutDetailsActivity
import com.chiranths.jobportal1.Activities.LoanActivity.LoanForm
import com.chiranths.jobportal1.Model.Corosolmodel
import com.chiranths.jobportal1.R
import com.chiranths.jobportal1.Utilitys.AppConstants

class CoroselListAdaptor(
    private val noticeBoardList: ArrayList<Corosolmodel>,
    private var context: Context
) : RecyclerView.Adapter<CoroselListAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val listItem =
            layoutInflater.inflate(R.layout.home_colorsel_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val noticeimage = noticeBoardList[position]
        Glide.with(context)
            .load(noticeimage.imageurl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.iv_corosel_image)
        holder.iv_corosel_image.setOnClickListener {
            if (noticeimage.type == "2") {
                val uri = Uri.parse("http://www.google.com")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            } else {
                Log.d("printCaroselDetails", noticeimage.category + "--" + noticeimage.pid)
                if (noticeimage.category == "Layout") {
                    val intent = Intent(context, LayoutDetailsActivity::class.java)
                    intent.putExtra(AppConstants.pid, noticeimage.pid)
                    intent.putExtra("page", "3")
                    context.startActivity(intent)
                } else if(noticeimage.category == "Loan") {
                    val intent = Intent(context, LoanForm::class.java)
                    intent.putExtra(AppConstants.pid, noticeimage.pid)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return noticeBoardList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_corosel_image: ImageView

        init {
            iv_corosel_image = itemView.findViewById(R.id.iv_corosel_image)
        }
    }
}
