package com.chiranths.jobportal1.Activities.Admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chiranths.jobportal1.Activities.Businesthings.BusinessActivity;
import com.chiranths.jobportal1.Adapters.BusinessAdaptor;
import com.chiranths.jobportal1.Adapters.BusinessCategoryAdaptor;
import com.chiranths.jobportal1.Model.BusinessModel;
import com.chiranths.jobportal1.Model.Categorymmodel;
import com.chiranths.jobportal1.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AdminBusinessListings extends AppCompatActivity {
    private static final int GalleryPick = 1;
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime,propertysize,location,number,owner,rating,email;
    private EditText InputProductName,Inputtype,InputProductDescription,InputProductPrice,et_sell_name,et_location,
            et_number,et_ownername,et_email,et_rating;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl,MainimageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private AutoCompleteTextView business_category;
    ArrayList fileNameList = new ArrayList<>();
    ArrayList fileDoneList = new ArrayList<>();
    ArrayList categoryList = new ArrayList();
    ArrayAdapter arrayAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_business_listings);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("business");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("BusinessListing");

        ImageView btn_corosel = findViewById(R.id.select_business_image);
        Button add_new_corosel = findViewById(R.id.add_new_Business);
        InputProductName = (EditText) findViewById(R.id.Business_name);
        business_category = findViewById(R.id.Business_type_admin);
        InputProductDescription = (EditText) findViewById(R.id.Business_description);
        InputProductPrice = (EditText) findViewById(R.id.Business_price_admin);
        et_sell_name = findViewById(R.id.Business_size);
        et_location = findViewById(R.id.Business_location_admin);
        et_number = findViewById(R.id.Business_number1);
        et_ownername = findViewById(R.id.Business_owner_name);
        et_email = findViewById(R.id.Business_email);
        et_rating = findViewById(R.id.Business_rating);
        loadingBar = new ProgressDialog(this);
        fetchbusinessCategorys();

        btn_corosel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();

            }
        });

        add_new_corosel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });


        business_category.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    business_category.showDropDown();
            }
        });
         arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList);

    }

    private void fetchbusinessCategorys() {
        DatabaseReference categorylist = FirebaseDatabase.getInstance().getReference().child("BusinessListing_category");
        categorylist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            categoryList.add(userData.get("category"));

                        } catch (ClassCastException cce) {
                            try {
                                String mString = String.valueOf(dataMap.get(key));
                                //addTextToView(mString);
                            } catch (ClassCastException cce2) {

                            }
                        }
                    }
                    business_category.setAdapter(arrayAdapter);
                    business_category.setInputType(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            //InputProductImage.setImageURI(ImageUri);
            StoreProductInformation(data);
        }
    }

    private void StoreProductInformation(Intent data) {

        /*loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();*/
        downloadImageUrl ="";

        // If the user selected only one image
        if (data.getData() != null) {
            Uri uri = data.getData();
            uploadTostorage(data,uri);
        }else if (data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                Uri uri = clipData.getItemAt(i).getUri();
                uploadTostorage(data,uri);
            }
        }
    }

    private void uploadTostorage(Intent data,Uri uri) {
        String fileName = getFileName(uri);
        fileNameList.add(fileName);
        fileDoneList.add("Uploading");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(uri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminBusinessListings.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminBusinessListings.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            if(downloadImageUrl.equals("")){
                                downloadImageUrl =task.getResult().toString();
                                MainimageUrl = task.getResult().toString();
                            }else {
                                downloadImageUrl = downloadImageUrl+"---"+task.getResult().toString();
                            }

                            System.out.println("url2---"+downloadImageUrl);
                            Toast.makeText(AdminBusinessListings.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }

    private void ValidateProductData() {

        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        propertysize = et_sell_name.getText().toString();
        location = et_location.getText().toString();
        number = et_number.getText().toString();
        owner = et_ownername.getText().toString();
        email = et_email.getText().toString();
        rating = et_rating.getText().toString();
        CategoryName = business_category.getText().toString();

        if (TextUtils.isEmpty(downloadImageUrl))
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SaveProductInfoToDatabase();
        }
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image2", downloadImageUrl);
        productMap.put("image", MainimageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("Businessname", Pname);
        productMap.put("Business_category",propertysize);
        productMap.put("location",location);
        productMap.put("number",number);
        productMap.put("owner", owner);
        productMap.put("email",email);
        productMap.put("rating", rating);
        productMap.put("Status", 1);


        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Intent intent = new Intent(AdminAddNewProductActivity.this, .class);
                            //startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminBusinessListings.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminBusinessListings.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        } if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}