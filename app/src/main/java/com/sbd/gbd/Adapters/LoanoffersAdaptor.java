package com.sbd.gbd.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sbd.gbd.Activities.LoanActivity.LoanForm;
import com.sbd.gbd.Model.LoanOffersModel;
import com.sbd.gbd.R;

import java.util.List;

public class LoanoffersAdaptor extends RecyclerView.Adapter<LoanoffersAdaptor.ViewHolder> {


    private List<LoanOffersModel> productInfos;
    private Context context;
    private String number, name;


    public LoanoffersAdaptor(List<LoanOffersModel> productInfos, Context context) {
        this.productInfos = productInfos;
        this.context = context;
        this.number = number;
        this.name = name;
    }

    @NonNull
    @Override
    public LoanoffersAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View listItem= layoutInflater.inflate(R.layout.loan_offer_adaptor, parent, false);
        LoanoffersAdaptor.ViewHolder viewHolder = new LoanoffersAdaptor.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LoanOffersModel productInfo = productInfos.get(position);

        Glide.with(context)
                .load(productInfo.imageurl)
                .into(holder.iv_image);

        holder.tv_bankname.setText(productInfo.bankname);
        holder.tv_loantype.setText(productInfo.bankloantype);
        holder.tv_intrestrate.setText(productInfo.bankintrest);
        holder.tv_amountprovid.setText(productInfo.bankamountprovid);
        //holder.tv_btn_call_hot.setText("");

        holder.btn_applayLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoanForm.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        CardView cv_deals;
        private LinearLayout btn_applayLoan;
        TextView tv_bankname,tv_loantype,tv_amountprovid,tv_intrestrate,tv_discription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_bankname = itemView.findViewById(R.id.tv_bankname);
            tv_loantype = itemView.findViewById(R.id.tv_bankloantype);
            tv_amountprovid = itemView.findViewById(R.id.tv_amount_upto);
            tv_intrestrate = itemView.findViewById(R.id.tv_intrestRate);
            iv_image = itemView.findViewById(R.id.iv_bank_image);
            btn_applayLoan = itemView.findViewById(R.id.btn_loanapplay);

            //tv_btn_call_hot = itemView.findViewById(R.id.tv_btn_call_hot);


        }
    }

}
