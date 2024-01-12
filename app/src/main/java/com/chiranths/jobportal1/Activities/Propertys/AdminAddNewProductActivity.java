package com.chiranths.jobportal1.Activities.Propertys;

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
import com.chiranths.jobportal1.Utilitys.AppConstants;
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

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime,propertysize,location,number;
    private String type;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice, Inputtype,et_size,et_location,et_number;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    ArrayList fileNameList = new ArrayList<>();
    ArrayList fileDoneList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        //CategoryName = getIntent().getExtras().get(AppConstants.category).toString();
        CategoryName = "cqat";
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        Inputtype = (EditText)findViewById(R.id.product_type_admin);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price_admin);
        et_size = findViewById(R.id.product_size);
        et_location = findViewById(R.id.product_location_admin);
        et_number = findViewById(R.id.contact_number);
        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }


    private void OpenGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            /*ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);*/
            StoreProductInformation(data);
        }

        /*if (requestCode == 1 && resultCode == RESULT_OK){
            if (data.getClipData() != null){
                //Toast.makeText(this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();
                int totalItems = data.getClipData().getItemCount();
                for (int i = 0;i < totalItems; i++){
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    fileNameList.add(fileName);
                    fileDoneList.add("Uploading");

                    StoreProductInformation(data);
                }
            } else if (data.getData() != null){
                Toast.makeText(this, "Selected Single File", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void ValidateProductData() {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        propertysize = et_size.getText().toString();
        location = et_location.getText().toString();
        type = Inputtype.getText().toString();
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
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                            }else {
                                downloadImageUrl = downloadImageUrl+"---"+task.getResult().toString();
                            }

                            System.out.println("url2---"+downloadImageUrl);
                            Toast.makeText(AdminAddNewProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();


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
        productMap.put(AppConstants.pid, productRandomKey);
        productMap.put(AppConstants.date, saveCurrentDate);
        productMap.put(AppConstants.time, saveCurrentTime);
        productMap.put(AppConstants.description, Description);
        productMap.put(AppConstants.image, downloadImageUrl);
        productMap.put(AppConstants.category, CategoryName);
        productMap.put(AppConstants.price, Price);
        productMap.put(AppConstants.pname, Pname);
        productMap.put("type",type);
        productMap.put("Approval",1);
        productMap.put(AppConstants.propertysize,propertysize);
        productMap.put(AppConstants.location,location);
        productMap.put(AppConstants.number,number);

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
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
