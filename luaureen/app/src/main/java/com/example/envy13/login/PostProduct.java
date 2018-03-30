package com.example.envy13.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class PostProduct extends AppCompatActivity {

    EditText contact, location, productName, productAmount, productDescription;
    Button productbtn, submitbtn;
    ImageView productimage;
    TextView email;

    FirebaseUser firebaseUser; //get current user
    FirebaseAuth firebaseAuth; //authenticstion of users
    StorageReference storageReference; //storing photos
    DatabaseReference mReference; //store rest of data

    String userChoosenTask, TAG;
    static final int SELECT_FILE = 1; //for gallery choice
    static final int REQUEST_CAMERA = 1; //for camera choice
    private static final String IMAGE_DIRECTORY = "/mahitajipics"; //name of location where the pics will be stored
    String getImgUrl; // for getting the photo url in string form for the dbrefence

    UserProduct userProduct = new UserProduct();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product);

        email = findViewById(R.id.emailtv);
        contact = findViewById(R.id.contact);
        location = findViewById(R.id.mylocation);
        productName = findViewById(R.id.productname);
        productAmount = findViewById(R.id.productAmount);
        productDescription = findViewById(R.id.description);
        productbtn = findViewById(R.id.productbtn);
        productimage = findViewById(R.id.productimage);
        submitbtn = findViewById(R.id.submit);

//        authenticate firebase user
        firebaseAuth = FirebaseAuth.getInstance();

//        initializing the storagereference
        storageReference = FirebaseStorage.getInstance().getReference().child("farmerposts");

//        get current user's email
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String useremail = firebaseUser.getEmail();
            email.setText(useremail);
        }

        productbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                method to help choose if to pick image from gallery or take a pic
                selectImage();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                method for saving the product created
                saveNewProduct();
            }
        });

    }

    private void selectImage() {
//        list of choices on the dialog created
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
//        alert dialog getting created
        final AlertDialog.Builder builder = new AlertDialog.Builder(PostProduct.this);
//        title of dialog created
        builder.setTitle("Add Product Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
//                first choice on dialog
                if (items[item].equals("Take Photo")){
                    userChoosenTask = "Take Photo";
//                    take a photo method
                        cameraIntent();
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask="Choose from Gallery";
//                    choose from gallery method
                        galleryIntent();
                } else if (items[item].equals("Cancel")) { //cancels the process
                    finish();
                }
            }
        });
        builder.show();
    }
//    select an image from the phone's gallery
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

//    take a photo using phone's camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_CANCELED){
//            return;
//        }
        if (requestCode == SELECT_FILE && resultCode == this.RESULT_OK) {
            if (data != null) {
//                Uri contentUri = data.getData();
                //                    for transferring data in the form of bundles
                Bundle extras = data.getExtras();
//                    bitmap is the form which data is transferred
//                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                Bitmap bitmap;
                bitmap = (Bitmap) extras.get("data");
                saveImage(bitmap);
                Toast.makeText(PostProduct.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    picture taken is picked and saved in the product image view
                productimage.setImageBitmap(bitmap);

//                    method for saving the image taken or picked and save to firebase db
                encodeImageandSavetoFirebase(bitmap);

            }

        } else if (requestCode == REQUEST_CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data"); //change to thumbnail baadaye
            productimage.setImageBitmap(bitmap);
            saveImage(bitmap);
            Toast.makeText(PostProduct.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            encodeImageandSavetoFirebase(bitmap);
        }
    }

    private String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File mahitajiDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY); //save images to specified location called mahitajipics
        // have the object build the directory structure, if needed.
        if (!mahitajiDirectory.exists()) {
            mahitajiDirectory.mkdirs();
        }

        try {
            File f = new File(mahitajiDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void encodeImageandSavetoFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imgEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("imgUrl"); //.child(sth.getPushId())
        Log.d(TAG, "encodeImageandSavetoFirebase: image saved to firebase db");
        getImgUrl = imgEncoded;
        ref.setValue(imgEncoded);

//transfer image to other activity
        Intent intent = new Intent(this, ViewProduct.class);
        intent.putExtra("BitmapImage", imgEncoded);
    }

    private void saveNewProduct() {

        String emailtext = email.getText().toString().trim();
        String contacttext = contact.getText().toString().trim();
        String locationtext = location.getText().toString().trim();
        String prodnametext = productName.getText().toString().trim();
        String prodamounttext = productAmount.getText().toString().trim();
        String proddescrtext = productDescription.getText().toString().trim();
        String imgUrl = productimage.toString().trim();
        getImgUrl = imgUrl;

        userProduct.setEmail(emailtext);
        userProduct.setContact(contacttext);
        userProduct.setLocation(locationtext);
        userProduct.setProductName(prodnametext);
        userProduct.setProductAmount(prodamounttext);
        userProduct.setProductDescription(proddescrtext);
//        userProduct.setprodImage(imgUrl);

//        if current user is present, data above is saved to db
        if (firebaseAuth.getCurrentUser() != null) {
            mReference = FirebaseDatabase.getInstance().getReference();
            mReference.child("farmer").child(firebaseAuth.getCurrentUser().getUid()).setValue(userProduct, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference mdatabaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(PostProduct.this, "Data is saved successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PostProduct.this, "Unable to save data. Retry!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


//            save to firebase storage
            productimage.setDrawingCacheEnabled(true);
            productimage.buildDrawingCache();
            Bitmap bitmap = productimage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(PostProduct.this, "uploading data", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostProduct.this, "unable to upload image to database. please retry.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
