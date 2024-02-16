package com.chiranths.jobportal1.Activities.Propertys

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.chiranths.jobportal1.Interface.ItemClickListner
import com.chiranths.jobportal1.R

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var txtProductName: TextView
    var txtProductDescription: TextView? = null
    var txtProductPrice: TextView
    var location: TextView
    var number: TextView? = null
    var size: TextView
    var type: TextView
    var imageView: ImageView
    var imageview1: ImageView? = null
    var imageview2: ImageView? = null
    var listner: ItemClickListner? = null
    var cv_layout: CardView
    var btn_add: Button

    init {
        imageView = itemView.findViewById<View>(R.id.product_image) as ImageView
        cv_layout = itemView.findViewById<View>(R.id.cv_layout) as CardView
        //imageview1 = (ImageView)itemView.findViewById(R.id.imgBanner);
        // imageview2 = (ImageView)itemView.findViewById(R.id.image_cat);
        txtProductName = itemView.findViewById<View>(R.id.project_name) as TextView
        location = itemView.findViewById(R.id.product_location1)
        size = itemView.findViewById(R.id.product_size1)
        type = itemView.findViewById(R.id.product_type1)
        // txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById<View>(R.id.product_price) as TextView
        btn_add = itemView.findViewById<View>(R.id.add_btn) as Button
    }

    override fun onClick(view: View) {
        listner!!.onClick(view, adapterPosition, false)
    }
}
