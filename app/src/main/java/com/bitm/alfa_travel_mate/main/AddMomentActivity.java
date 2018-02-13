package com.bitm.alfa_travel_mate.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitm.alfa_travel_mate.R;
import com.bitm.alfa_travel_mate.model.TravelMoment;
import com.bitm.alfa_travel_mate.model.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddMomentActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private TravelMoment objMoment;
    Button takePhotoBtn, chosePhotoBtn;
    ImageView imageView;
    TextView momentTV;
    EditText descriptionET;
    Button submitBtn;
    String eventid;
    String eventName;
    String date,time;
    Bitmap rotatedBMP;
    java.util.Calendar calendar;
    java.text.SimpleDateFormat timeFormat,dateFormat;
    private Uri imageUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    String mCurrentPhotoPath;
    String photoName;
    String momentId ;
    String description;
    private static final String TAG = "Moment";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addm);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Moment");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.FIRE_MOMENTS);
        mProgress = new ProgressDialog(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        descriptionET = (EditText) findViewById(R.id.descriptionET);
        submitBtn = (Button)findViewById(R.id.submitBtn);
        takePhotoBtn = (Button) findViewById(R.id.take_photo_btn);
        chosePhotoBtn = (Button) findViewById(R.id.chose_photo_btn);
        submitBtn.setOnClickListener(onClick);
        chosePhotoBtn.setOnClickListener(onClick);
        takePhotoBtn.setOnClickListener(onClick);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMoment();
            }
        });

        Intent intent = getIntent();
        eventid=intent.getStringExtra("event_id");

        calendar = java.util.Calendar.getInstance(Locale.getDefault());

        timeFormat = new java.text.SimpleDateFormat("HH:mm a");
        dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");

        date= dateFormat.format(calendar.getTime());

        time = timeFormat.format(calendar.getTime());

    }
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.take_photo_btn){
                dispatchTakePictureIntent();
            }
            else if(view.getId() == R.id.chose_photo_btn){
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
            else if(view.getId() ==  R.id.submitBtn){

                description = descriptionET.getText().toString().trim();
                if(momentId != "" && momentId != null)
                {
                    //updateMoment();
                }
                else
                {
                    saveMoment();
                }
            }
        }
    };

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        imageUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/picimage";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        imageUri = Uri.fromFile(image);
        photoName=timeStamp;
        Log.i(TAG, "photo path = " + mCurrentPhotoPath);
        return image;
    }

    private void saveMoment() {
        mProgress.setMessage("Posting moment...");
        mProgress.show();
        final String description = descriptionET.getText().toString().trim();
        if(imageUri == null){
            Toast.makeText(AddMomentActivity.this, "Please capture or choose an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(description)){
            Toast.makeText(AddMomentActivity.this, "description is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(eventid)&& imageUri != null){

            StorageReference filePath = mStorage.child("Moment_Images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    String momentId = mDatabase.push().getKey();
                    TravelMoment moment = new TravelMoment(momentId,eventid,description,downloadUri.toString(),date,time);
                    mDatabase.child(momentId).setValue(moment);
                    mProgress.dismiss();
                    finish();
                    startActivity(new Intent(AddMomentActivity.this,EventDescription.class).putExtra("event_id",eventid));

                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            setPic();

        }

    }

    private void setPic() {
        int targetW = 500;
        int targetH = 600;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        Matrix mtx = new Matrix();
        mtx.postRotate(0);
        // Rotating Bitmap
        rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);

        if (rotatedBMP != bitmap)
            bitmap.recycle();

        imageView.setImageBitmap(rotatedBMP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}
