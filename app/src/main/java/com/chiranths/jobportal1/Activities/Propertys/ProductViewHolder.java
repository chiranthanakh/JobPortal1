package com.chiranths.jobportal1.Activities.Propertys;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chiranths.jobportal1.Interface.ItemClickListner;
import com.chiranths.jobportal1.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView, imageview1, imageview2;
    public ItemClickListner listner;
    public Button btn_add;

    public ProductViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        //imageview1 = (ImageView)itemView.findViewById(R.id.imgBanner);
       // imageview2 = (ImageView)itemView.findViewById(R.id.image_cat);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        btn_add=(Button)itemView.findViewById(R.id.add_btn);

    }



    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}

