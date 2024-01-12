package com.chiranths.jobportal1.Activities.BasicActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chiranths.jobportal1.Activities.Admin.Admin_corosel_dashboard;
import com.chiranths.jobportal1.R;
import com.chiranths.jobportal1.Utilitys.AppConstants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserDetailsActivity extends AppCompatActivity {

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    private static final int GalleryPick = 1;
    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP,edtEmail,edtName;
    private String saveCurrentDate, saveCurrentTime;

    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;

    private DatabaseReference Profiles;
    private StorageReference ProfileImagesRef;
    private String productRandomKey, downloadImageUrl,MainProfileUrl;
    private ProgressDialog loadingBar;
    ArrayList fileNameList = new ArrayList<>();
    ArrayList fileDoneList = new ArrayList<>();
    ImageView profileImage;
    private Uri ImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        Profiles = FirebaseDatabase.getInstance().getReference().child("Profile");
        ProfileImagesRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");

        initilize();

    }

    private void initilize() {
        edtPhone = findViewById(R.id.edt_phone_login);
        edtName = findViewById(R.id.edt_name_login);
        edtEmail = findViewById(R.id.edt_email_login);
        profileImage = findViewById(R.id.select_profile_image);
       // verifyOTPBtn = findViewById(R.id.btn_submit_login);
        generateOTPBtn = findViewById(R.id.btn_submit_login);
        loadingBar = new ProgressDialog(this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = edtName.getText().toString();
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();

                if(number != null && name != null && email != null) {
                    SaveProductInfoToDatabase();
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("name", edtName.getText().toString());
                myEdit.putString(AppConstants.number, edtPhone.getText().toString());
                myEdit.commit();
                finish();
            }else {
                    Toast.makeText(UserDetailsActivity.this, "Please Enter all details...", Toast.LENGTH_SHORT).show();
                }
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
            StoreProductInformation(data);
        }
    }

    private void StoreProductInformation(Intent data) {
        downloadImageUrl ="";
        System.out.println("image5---"+downloadImageUrl);

            Uri fileUri = data.getData();
            String fileName = getFileName(fileUri);
            profileImage.setImageURI(fileUri);
            fileNameList.add(fileName);
            fileDoneList.add("Uploading");
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
            saveCurrentTime = currentTime.format(calendar.getTime());
            productRandomKey = saveCurrentDate + saveCurrentTime;

            final StorageReference filePath = ProfileImagesRef.child(fileUri.getLastPathSegment() + productRandomKey + ".jpg");

            final UploadTask uploadTask = filePath.putFile(fileUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(UserDetailsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserDetailsActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
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
                                    MainProfileUrl = task.getResult().toString();
                                }else {
                                    downloadImageUrl = downloadImageUrl+"---"+task.getResult().toString();
                                }

                                System.out.println("url2---"+downloadImageUrl);
                                Toast.makeText(UserDetailsActivity.this, "got the Profile image Url Successfully...", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }
            });
    }


    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("name", edtName.getText().toString());
        productMap.put("Email", edtEmail.getText().toString());
        productMap.put(AppConstants.image, MainProfileUrl);
        productMap.put(AppConstants.number,edtPhone.getText().toString());


        Profiles.child(edtPhone.getText().toString()).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Intent intent = new Intent(AdminAddNewProductActivity.this, .class);
                            //startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(UserDetailsActivity.this, "Profile successfull", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(UserDetailsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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