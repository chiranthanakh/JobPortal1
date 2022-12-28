package com.chiranths.jobportal1.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AdminLivingPlacess extends AppCompatActivity {

    private static final int GalleryPick = 1;
    String  saveCurrentDate, saveCurrentTime,title,category,rent_lease,floore,rentamount,location,contactNumber,verified,nuBHK,sqft,water,parking,postedBY,discription;
    private EditText livingplace_name,livingplace_category,livingplace_rent_lease,livingplace_flore,livingplace_rent_advance,livingplace_rent_amount,
    livingplace_location,livingplace_contact_number,leavingplace_verify_or_nt,livingplace_number_of_bhk,livingplace_sqft,livingplace_water_facility,
    livingplace_vehicle_parking,livingplace_posted_by,livingplace_discription;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl,MainimageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    ArrayList fileNameList = new ArrayList<>();
    ArrayList fileDoneList = new ArrayList<>();
    EditText ads_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_living_placess);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("livingplace");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("livingplaceforyou");

        initilize();
    }

    private void initilize() {
        ImageView add_images = findViewById(R.id.select_livingplace_image);
        Button btn_add_livingplace = findViewById(R.id.livingplace_submit);

        livingplace_name = findViewById(R.id.livingplace_name);
        livingplace_category = findViewById(R.id.livingplace_type);
        livingplace_rent_lease = findViewById(R.id.livingplace_rent_lease);
        livingplace_flore = findViewById(R.id.livingplace_flore);
        livingplace_rent_amount = findViewById(R.id.livingplace_rent_amount);
        livingplace_location = findViewById(R.id.livingplace_location);
        livingplace_contact_number = findViewById(R.id.livingplace_contact_number);
        leavingplace_verify_or_nt = findViewById(R.id.leavingplace_verify_or_nt);
        livingplace_number_of_bhk = findViewById(R.id.livingplace_number_of_bhk);
        livingplace_sqft = findViewById(R.id.livingplace_sqft);
        livingplace_water_facility = findViewById(R.id.livingplace_water_facility);
        livingplace_vehicle_parking = findViewById(R.id.livingplace_vehicle_parking);
        livingplace_posted_by = findViewById(R.id.livingplace_posted_by);
        livingplace_discription = findViewById(R.id.livingplace_discription);

        add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();

            }
        });

        btn_add_livingplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
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
            StoreProductInformation(data);
        }
    }

    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, GalleryPick);
    }

    private void ValidateProductData() {
        title = livingplace_name.getText().toString();
        category = livingplace_category.getText().toString();
        rent_lease = livingplace_rent_lease.getText().toString();
        floore = livingplace_flore.getText().toString();
        location = livingplace_location.getText().toString();
        rentamount = livingplace_rent_amount.getText().toString();
        contactNumber = livingplace_contact_number.getText().toString();
        nuBHK = livingplace_number_of_bhk.getText().toString();
        sqft = livingplace_sqft.getText().toString();
        water = livingplace_water_facility.getText().toString();
        parking = livingplace_vehicle_parking.getText().toString();
        postedBY = livingplace_posted_by.getText().toString();
        discription = livingplace_discription.getText().toString();

        if (TextUtils.isEmpty(downloadImageUrl))
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(title))
        {
            Toast.makeText(this, "Please write product title...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(rent_lease))
        {
            Toast.makeText(this, "Please write product rent_lease...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(location))
        {
            Toast.makeText(this, "Please write location...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(rentamount))
        {
            Toast.makeText(this, "Please enter rentamount", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SaveProductInfoToDatabase();
        }
    }

    private void StoreProductInformation(Intent data) {

        downloadImageUrl ="";
        System.out.println("image5---"+downloadImageUrl);
        int totalItems = data.getClipData().getItemCount();
        for (int i = 0; i < totalItems; i++) {
            Uri fileUri = data.getClipData().getItemAt(i).getUri();
            String fileName = getFileName(fileUri);
            fileNameList.add(fileName);
            fileDoneList.add("Uploading");

            System.out.println("image1---"+downloadImageUrl);
            System.out.println("count---"+totalItems);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            productRandomKey = saveCurrentDate + saveCurrentTime;

            final StorageReference filePath = ProductImagesRef.child(fileUri.getLastPathSegment() + productRandomKey + ".jpg");

            final UploadTask uploadTask = filePath.putFile(fileUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(AdminLivingPlacess.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminLivingPlacess.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();

                            }

                            System.out.println("url3---"+downloadImageUrl);
                            // downloadImageUrl = downloadImageUrl+"---"+ filePath.getDownloadUrl().toString();
                            System.out.println("url1---"+downloadImageUrl);

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
                                Toast.makeText(AdminLivingPlacess.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }
            });

            if(i==totalItems-1){
                System.out.println("downloadurl--"+downloadImageUrl);
                // SaveProductInfoToDatabase();
            }
        }
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("title", title);
        productMap.put("category", category);
        productMap.put("rent_lease", rent_lease);
        productMap.put("floore", floore);
        productMap.put("image2", downloadImageUrl);
        productMap.put("image", MainimageUrl);
        productMap.put("location", location);
        productMap.put("rentamount", rentamount);
        productMap.put("contactNumber", contactNumber);
        productMap.put("nuBHK",nuBHK);
        productMap.put("sqft",sqft);
        productMap.put("water",water);
        productMap.put("postedOn", saveCurrentDate);
        productMap.put("Approval",1);
        productMap.put("parking",parking);
        productMap.put("postedBY", postedBY);
        productMap.put("discription", discription);
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
                            Toast.makeText(AdminLivingPlacess.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(AdminLivingPlacess.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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