package com.chiranths.jobportal1.Activities.Propertys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.chiranths.jobportal1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PropertyActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference ProductsRef;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

          if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
            //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.JobPortaltheam); //default app theme
        }


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.app_blue));
        }

        initilize();

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclarviewset();

    }

    private void initilize() {

        btn_add = findViewById(R.id.btn_add_property);
        btn_add.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mgrid = new GridLayoutManager(this,1);

        recyclerView.setLayoutManager(mgrid);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void recyclarviewset() {

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                       /* if(model.getType()==4){
                            Picasso.get().load(model.getImage()).into(holder.imageview2);

                            holder.imageview2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //Intent intent =new Intent(HomeActivity.this,ItemsCartActivity.class);
                                    // intent.putExtra("pid",model.getPid());
                                    //intent.putExtra("ctype", model.getCategory());
                                    //startActivity(intent);
                                }
                            });

                        }else {*/
                            holder.txtProductName.setText(model.getPname());
                            // holder.txtProductDescription.setText(model.getDescription());
                        holder.type.setText(model.getType());
                        holder.size.setText(model.getSize());
                        holder.location.setText(model.getLocation());
                            holder.txtProductPrice.setText("₹ " + model.getPrice());
                            Picasso.get().load(model.getImage()).into(holder.imageView);

                           // progressBar.setVisibility(View.GONE);

                            //add button function
                            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  //  String num = holder.numberButton1.getNumber();
                                   /* boolean isInserted = myDB.insertData(model.getPid(),model.getPname(),model.getPrice(),holder.numberButton1.getNumber());

                                    if (isInserted == true) {
                                        Toast.makeText(HomeActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(HomeActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                                    }*/
                                }
                            });


                            holder.imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    /*Intent intent =new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);*/
                                }
                            });

                       // }
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view;

                        /*if(viewType==1){
                            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_cart_items, parent, false);
                        } else if(viewType==2){
                            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rec_category, parent, false);
                        } else {
                            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        }*/

                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);

                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                    @Override
                    public int getItemViewType(int position) {
                     /* Products user = getItem(position);
                        if(user.getType()==1){
                            return 1;
                        }else if(user.getType()==2) {
                            return 2;
                        }else{
                            return 0;
                        }*/

                        return 1;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_add_property:
                Intent intent = new Intent(PropertyActivity.this, AdminAddNewProductActivity.class);
                startActivity(intent);
                break;

        }

    }
}