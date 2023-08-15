package com.chiranths.jobportal1.Activities.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chiranths.jobportal1.Activities.Dashboard.StartingActivity;
import com.chiranths.jobportal1.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminDashboard extends AppCompatActivity {

    private static final int GalleryPick = 1;
    private static final int GalleryPick2 = 2;
    private static final int GalleryPick3 = 3;
    private static final int GalleryPick4 = 4;
    private Uri ImageUri;
    private ProgressDialog loadingBar;
    private StorageReference ProductImagesRef, adsImageRef, hotImageRef, businessImageRef, layoutsImagesRef,loanImageRef;
    private DatabaseReference ProductsRef,adsRef, hotRef, businessRef, layoutRef, loanRef;
    private String productRandomKey, downloadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincoroselimages);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Corosel");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Corosels");
        adsImageRef = FirebaseStorage.getInstance().getReference().child("ads");
        adsRef = FirebaseDatabase.getInstance().getReference().child("adsforyou");
        hotImageRef = FirebaseStorage.getInstance().getReference().child("hot");
        hotRef = FirebaseDatabase.getInstance().getReference().child("hotforyou");
        businessImageRef = FirebaseStorage.getInstance().getReference().child("business");
        businessRef = FirebaseDatabase.getInstance().getReference().child("BusinessListing");
        layoutsImagesRef = FirebaseStorage.getInstance().getReference().child("layouts");
        layoutRef = FirebaseDatabase.getInstance().getReference().child("layoutsforyou");
        loanImageRef = FirebaseStorage.getInstance().getReference().child("loanoffers");
        loanRef = FirebaseDatabase.getInstance().getReference().child("loanoffersforyou");
        loadingBar = new ProgressDialog(this);

        Button btn_corosel = findViewById(R.id.btn_corosel);
        Button btn_ads = findViewById(R.id.btn_ads);
        Button btn_hot_deals = findViewById(R.id.btn_hot_deals);
        Button btn_business_listing = findViewById(R.id.btn_business_listing);
        Button btn_layouts = findViewById(R.id.btn_layouts);
        Button btn_loan_offers = findViewById(R.id.btn_loan_offers);
        Button btn_business_category = findViewById(R.id.btn_business_category);
        Button btn_propertys_approval = findViewById(R.id.propertys_for_approval);
        Button btn_business_approval = findViewById(R.id.business_for_approval);
        Button btn_livingplace_add = findViewById(R.id.btn_livingplace);
        Button btn_travels = findViewById(R.id.btn_travels);
        Button btn_construction = findViewById(R.id.btn_construction);
        Button btn_constCategory = findViewById(R.id.btn_construction_category);
        Button btn_hotels = findViewById(R.id.btn_Hotels);

        btn_hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, Admin_hotels.class);
                startActivity(intent);
            }
        });

        btn_constCategory.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, AdminConstructionCategorys.class);
            startActivity(intent);
        });

        btn_construction.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, Admin_Construction.class);
            startActivity(intent);
        });

        btn_travels.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, Admin_travels.class);
            startActivity(intent);
        });
        btn_livingplace_add.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, AdminLivingPlacess.class);
            startActivity(intent);
        });
        btn_propertys_approval.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, AdminPropertyApproval.class);
            startActivity(intent);
        });

        btn_business_approval.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, AdminBusinessCategorys.class);
            startActivity(intent);
        });

        btn_business_category.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, AdminBusinessCategorys.class);
            startActivity(intent);
        });

        btn_loan_offers.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboard.this, AdminloanOffers.class);
            startActivity(intent);
        });

        btn_hot_deals.setOnClickListener(view -> {

            Intent intent = new Intent(AdminDashboard.this, Admin_hotdeals_dashboard.class);
            startActivity(intent);
        });

        btn_layouts.setOnClickListener(view -> {

            Intent intent = new Intent(AdminDashboard.this, Admin_layouts_dashboard.class);
            startActivity(intent);
        });


        btn_corosel.setOnClickListener(view -> {

            Intent intent = new Intent(AdminDashboard.this, Admin_corosel_dashboard.class);
            startActivity(intent);
        });

        btn_ads.setOnClickListener(view -> {

            Intent intent = new Intent(AdminDashboard.this, Admin_ads_dashboard.class);
            intent.putExtra("page","1");
            startActivity(intent);
        });

        btn_business_listing.setOnClickListener(view -> {

            Intent intent = new Intent(AdminDashboard.this, AdminBusinessListings.class);
            startActivity(intent);
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            //InputProductImage.setImageURI(ImageUri);
            StoreProductInformation();
        }else  if (requestCode==GalleryPick2  &&  resultCode==RESULT_OK  &&  data!=null) {
            ImageUri = data.getData();
            //InputProductImage.setImageURI(ImageUri);
            Storeadsinfo();
        }else  if (requestCode==GalleryPick3  &&  resultCode==RESULT_OK  &&  data!=null) {
            ImageUri = data.getData();
            //InputProductImage.setImageURI(ImageUri);
            Storehotinfo();
        }else if (requestCode==GalleryPick4  &&  resultCode==RESULT_OK  &&  data!=null) {
            ImageUri = data.getData();
            //InputProductImage.setImageURI(ImageUri);
            StoreBusiness();
        }
    }

    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

         productRandomKey = saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminDashboard.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminDashboard.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("image", downloadImageUrl);


        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminDashboard.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminDashboard.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void Storeadsinfo()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentTime;

        final StorageReference filePath = adsImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminDashboard.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminDashboard.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            SaveadsInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveadsInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();


        productMap.put("pid", productRandomKey);
        productMap.put("date", "today");
        productMap.put("time", "time");
        productMap.put("description", "better places in haven");
        productMap.put("image", downloadImageUrl);
        productMap.put("category", "flats");
        productMap.put("price", "50000");
        productMap.put("pname", "flats");
        productMap.put("type","flat");
        productMap.put("propertysize",4000*500);
        productMap.put("location","location");
        productMap.put("number","12345567");
        productMap.put("Status", 1);


        adsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminDashboard.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminDashboard.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void Storehotinfo()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentTime;

        final StorageReference filePath = hotImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminDashboard.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminDashboard.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            SavehotInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SavehotInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid", productRandomKey);
        productMap.put("date", "today");
        productMap.put("time", "time");
        productMap.put("description", "better places in haven");
        productMap.put("image", downloadImageUrl);
        productMap.put("category", "flats");
        productMap.put("price", "50000");
        productMap.put("pname", "flats");
        productMap.put("type","flat");
        productMap.put("propertysize",4000*500);
        productMap.put("location","location");
        productMap.put("number","12345567");
        productMap.put("Status", 1);


        hotRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminDashboard.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminDashboard.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void StoreBusiness()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentTime;

        final StorageReference filePath = businessImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminDashboard.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminDashboard.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
                            SaveBusinessToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveBusinessToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("type", "Construction");
        productMap.put("name", "BR builders");
        productMap.put("description", "20 yeras of experiece in construction fields");
        productMap.put("image", downloadImageUrl);
        productMap.put("location", "Gouribidanur, nr.reddy circle");
        productMap.put("servicess", "construction,material supplay");
        productMap.put("number","12345567");
        productMap.put("Status", 1);


        businessRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminDashboard.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminDashboard.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminDashboard.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}