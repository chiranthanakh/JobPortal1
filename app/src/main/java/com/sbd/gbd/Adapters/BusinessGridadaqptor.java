package com.sbd.gbd.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.sbd.gbd.Activities.Businesthings.BusinessActivity;
import com.sbd.gbd.Model.Categorymmodel;
import com.sbd.gbd.R;

import java.util.ArrayList;

public class BusinessGridadaqptor extends ArrayAdapter<Categorymmodel>{

    BusinessActivity businessActivity = new BusinessActivity();

    public BusinessGridadaqptor(@NonNull Context context, ArrayList<Categorymmodel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.business_gridlayout_adaptor, parent, false);
        }

        Categorymmodel categorymmodel = getItem(position);
        TextView text = listitemView.findViewById(R.id.tv_business_category);
        ImageView image = listitemView.findViewById(R.id.iv_grid_image);

        text.setText(categorymmodel.getCategory());
        Glide.with(getContext())
                .load(categorymmodel.getImage())
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessActivity.filter(text.getText().toString());
            }
        });

        return listitemView;
    }
}