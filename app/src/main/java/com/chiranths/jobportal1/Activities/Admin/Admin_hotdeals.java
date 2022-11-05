package com.chiranths.jobportal1.Activities.Admin;

import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Admin_hotdeals extends AppCompatActivity {

    private static final int GalleryPick = 1;
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime,propertysize,location,number;
    private EditText InputProductName,Inputtype,InputProductDescription,InputProductPrice,et_size,et_location,et_number;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl,MainimageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    ArrayList fileNameList = new ArrayList<>();
    ArrayList fileDoneList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hotdeals);

        CategoryName = "cqat";
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("hot");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("hotforyou");

        ImageView btn_corosel = findViewById(R.id.select_hot_image);
        Button add_new_corosel = findViewById(R.id.add_new_hot);

        InputProductName = (EditText) findViewById(R.id.hot_name);
        Inputtype = (EditText)findViewById(R.id.hot_type_admin);
        InputProductDescription = (EditText) findViewById(R.id.hot_description);
        InputProductPrice = (EditText) findViewById(R.id.hot_price_admin);
        et_size = findViewById(R.id.hot_size);
        et_location = findViewById(R.id.hot_location_admin);
        et_number = findViewById(R.id.contact_number3);
        loadingBar = new ProgressDialog(this);

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

    private void ValidateProductData() {

        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        propertysize = et_size.getText().toString();
        location = et_location.getText().toString();
        number = et_number.getText().toString();

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

    private void StoreProductInformation(Intent data) {

        /*loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();*/

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
                    Toast.makeText(Admin_hotdeals.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Admin_hotdeals.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Admin_hotdeals.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();


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

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image2", downloadImageUrl);
        productMap.put("image", MainimageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("Approval",1);
        productMap.put("propertysize",propertysize);
        productMap.put("location",location);
        productMap.put("number",number);

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
                            Toast.makeText(Admin_hotdeals.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Admin_hotdeals.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



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