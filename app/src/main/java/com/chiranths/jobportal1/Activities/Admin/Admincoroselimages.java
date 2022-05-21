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

import com.chiranths.jobportal1.Activities.BasicActivitys.StartingActivity;
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

public class Admincoroselimages extends AppCompatActivity {

    private static final int GalleryPick = 1;
    private static final int GalleryPick2 = 2;
    private static final int GalleryPick3 = 3;
    private static final int GalleryPick4 = 4;
    private Uri ImageUri;
    private ProgressDialog loadingBar;
    private StorageReference ProductImagesRef, adsImageRef, hotImageRef, businessImageRef;
    private DatabaseReference ProductsRef,adsRef, hotRef, businessRef;
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

        loadingBar = new ProgressDialog(this);

        Button btn_corosel = findViewById(R.id.btn_corosel);
        Button btn_ads = findViewById(R.id.btn_ads);
        Button btn_hot_deals = findViewById(R.id.btn_hot_deals);
        Button btn_business_listing = findViewById(R.id.btn_business_listing);

        btn_hot_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Admincoroselimages.this,Admin_hotdeals.class);
                startActivity(intent);
            }
        });

        btn_corosel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Admincoroselimages.this,Admin_corosel.class);
                startActivity(intent);
            }
        });

        btn_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Admincoroselimages.this,Admin_ads.class);
                startActivity(intent);
            }
        });

        btn_business_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Admincoroselimages.this,Admin_hotdeals.class);
                startActivity(intent);
            }
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
                Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admincoroselimages.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Admincoroselimages.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(Admincoroselimages.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(Admincoroselimages.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admincoroselimages.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Admincoroselimages.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
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

        adsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(Admincoroselimages.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(Admincoroselimages.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admincoroselimages.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Admincoroselimages.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
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

        hotRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(Admincoroselimages.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(Admincoroselimages.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admincoroselimages.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Admincoroselimages.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();
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

        businessRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(Admincoroselimages.this, StartingActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(Admincoroselimages.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Admincoroselimages.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}