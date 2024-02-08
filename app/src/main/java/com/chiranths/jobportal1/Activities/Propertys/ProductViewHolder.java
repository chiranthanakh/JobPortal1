package com.chiranths.jobportal1.Activities.Propertys;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chiranths.jobportal1.Interface.ItemClickListner;
import com.chiranths.jobportal1.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductDescription, txtProductPrice,location,number,size,type;
    public ImageView imageView, imageview1, imageview2;
    public ItemClickListner listner;
    CardView cv_layout;
    public Button btn_add;

    public ProductViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        cv_layout = (CardView) itemView.findViewById(R.id.cv_layout);
        //imageview1 = (ImageView)itemView.findViewById(R.id.imgBanner);
       // imageview2 = (ImageView)itemView.findViewById(R.id.image_cat);
        txtProductName = (TextView) itemView.findViewById(R.id.project_name);
        location = itemView.findViewById(R.id.product_location1);
        size = itemView.findViewById(R.id.product_size1);
        type = itemView.findViewById(R.id.product_type1);
       // txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        btn_add=(Button)itemView.findViewById(R.id.add_btn);

    }



    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}

