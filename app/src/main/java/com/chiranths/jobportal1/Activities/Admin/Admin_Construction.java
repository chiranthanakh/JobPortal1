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

public class Admin_Construction extends AppCompatActivity {

    private static final int GalleryPick = 1;
    String Name,category,cost,contactDetails,contactDetails2,experience,service1,service2,open,close,service3,service4,saveCurrentDate,saveCurrentTime,discription,vehicleNumber,address,owner,product_services,gst;
    private EditText edt_construction_name,edt_construction_number,edt_construction_cost,etd_opening_time,edt_closing_time,
            edt_construction_experience,edt_construction_model,edt_construction_servicessoffer_1,edt_construction_servicessoffer_2,edt_construction_servicessoffer_3,edt_construction_servicessoffer_4,
            edt_construction_verified_not,edt_construction_discription,edt_product_services,edt_construction_address,edt_construction_gst,edt_construction_owner;
    private Uri ImageUri;
    private AutoCompleteTextView edt_construction_category;
    private String productRandomKey, downloadImageUrl,MainimageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    ArrayList fileNameList = new ArrayList<>();
    ArrayList fileDoneList = new ArrayList<>();
    EditText ads_name;
    ImageView back_btn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_construction);

        //CategoryName = "cqat";
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("construction");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("constructionforyou");
        ImageView btn_add_image = findViewById(R.id.select_construction_images);
        Button add_new_construction = findViewById(R.id.add_new_construction);
        edt_construction_name = (EditText) findViewById(R.id.edt_construction_name);
        edt_construction_category = findViewById(R.id.edt_construction_category);
        edt_construction_number = (EditText) findViewById(R.id.edt_construction_contact_number);
        edt_product_services = (EditText) findViewById(R.id.edt_product_services);
        edt_construction_cost = (EditText) findViewById(R.id.edt_construction_cost);
        edt_construction_experience = findViewById(R.id.edt_construction_experience);
        edt_construction_model = findViewById(R.id.edt_construction_model);
        etd_opening_time = findViewById(R.id.etd_opening_time);
        edt_closing_time = findViewById(R.id.etd_closing_time);
        edt_construction_servicessoffer_1 = findViewById(R.id.edt_construction_servicessoffer_1);
        edt_construction_servicessoffer_2 = findViewById(R.id.edt_construction_servicessoffer_2);
        edt_construction_servicessoffer_3 = findViewById(R.id.edt_construction_servicessoffer_3);
        edt_construction_servicessoffer_4 = findViewById(R.id.edt_construction_servicessoffer_4);
        edt_construction_verified_not = findViewById(R.id.edt_construction_verified_not);
        edt_construction_discription = findViewById(R.id.edt_construction_discription);
        edt_construction_address = findViewById(R.id.edt_construction_address);
        edt_construction_gst = findViewById(R.id.edt_construction_gst);
        edt_construction_owner = findViewById(R.id.edt_construction_owner);
        back_btn = findViewById(R.id.iv_nav_view);
        loadingBar = new ProgressDialog(this);
        btn_add_image.setOnClickListener(view -> OpenGallery());
        add_new_construction.setOnClickListener(view -> ValidateProductData());

        ArrayList list = new ArrayList();
        list.add("Contractors");
        list.add("Architect");
        list.add("Interior Designer");
        list.add("Construction Meterials");
        list.add("Hardwares");
        list.add("Painters");
        list.add("Carpenters");
        list.add("Electrician");
        list.add("Plumber");

        ArrayAdapter arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        edt_construction_category.setAdapter(arrayAdapter);
        edt_construction_category.setInputType(0);

        edt_construction_category.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                edt_construction_category.showDropDown();
        });

        back_btn.setOnClickListener(view -> finish());
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
                Toast.makeText(Admin_Construction.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admin_Construction.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Admin_Construction.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

    }

    private void ValidateProductData() {
        Name = edt_construction_name.getText().toString();
        category = edt_construction_category.getText().toString();
        experience = edt_construction_experience.getText().toString();
        cost = edt_construction_cost.getText().toString();
        contactDetails = edt_construction_number.getText().toString();
        discription = edt_construction_verified_not.getText().toString();
        vehicleNumber = edt_construction_discription.getText().toString();
        owner = edt_construction_owner.getText().toString();
        address = edt_construction_address.getText().toString();
        gst = edt_construction_gst.getText().toString();
        open = etd_opening_time.getText().toString();
        close = edt_closing_time.getText().toString();
        product_services = edt_product_services.getText().toString();

        if (TextUtils.isEmpty(downloadImageUrl))
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Please enter vehicle name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cost)) {
            Toast.makeText(this, "Please write Price/KM...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(contactDetails)) {
            Toast.makeText(this, "Please enter contact details...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(open)) {
            Toast.makeText(this, "Please enter opening timings...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(close)) {
            Toast.makeText(this, "Please enter closing timings...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(product_services)) {
            Toast.makeText(this, "please enter product or services", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SaveProductInfoToDatabase();
        }
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", discription);
        productMap.put("image2", downloadImageUrl);
        productMap.put("image", MainimageUrl);
        productMap.put("category", category);
        productMap.put("workingHrs",open+" to "+ close);
        productMap.put("cost", cost);
        productMap.put("name", Name);
        productMap.put("number1",contactDetails);
        productMap.put("product_services",product_services);
        productMap.put("servicess1",service1);
        productMap.put("servicess2",service2);
        productMap.put("servicess3",service3);
        productMap.put("servicess4",service4);
        productMap.put("verified", "1");
        productMap.put("experience", experience);
        productMap.put("owner",owner);
        productMap.put("address",address);
        productMap.put("gst",gst);
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
                            Toast.makeText(Admin_Construction.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Admin_Construction.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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