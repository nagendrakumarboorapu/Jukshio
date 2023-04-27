package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   private ImageView btnFirstImageCapture,btnSecondImageCapture;
   private Button btnSubmit;
    private static final int first_pic_id = 123;
    private static final int second_pic_id = 456;
    boolean isFirstImage,isSecondImage=false;
   private AlertDialog.Builder builder;
   private Bitmap firstPhoto,secondPhoto;
   private int i;


   private FirebaseDatabase firebaseDatabase;

   private DatabaseReference databaseReference;
   private String firstImageString,secondImageString;
    // Images firebaseImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnFirstImageCapture = findViewById(R.id.btnFirstImageCapture);
        btnSecondImageCapture = findViewById(R.id.btnSecondImageCapture);
        btnSubmit = findViewById(R.id.btnSubmit);






        FirebaseApp.initializeApp(MainActivity.this);

        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Images");

        Intent intent = getIntent();
        if (intent!=null) {
            String first = intent.getStringExtra("first");
            String second = intent.getStringExtra("second");
        }else {
            btnFirstImageCapture.setImageBitmap(null);
            btnSecondImageCapture.setImageBitmap(null);
            databaseReference = firebaseDatabase.getReference("");

        }
        //  firebaseImages =new Images();
        builder  = new AlertDialog.Builder(MainActivity.this);
        btnFirstImageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=view.getId();
                if (!isFirstImage) {
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, first_pic_id);
                }else {

                    displayImage();


                }
            }
        });


        btnSecondImageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i =view.getId();
                if (!isSecondImage) {
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(camera_intent, second_pic_id);
                }else {
                    displayImage();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{


                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // inside the method of on Data change we are setting
                            // our object class to our database reference.
                            // data base reference will sends data to firebase.

                            Log.d("DEBUG", "Success");

                            Images images =new Images();
                            images.setFirstImage(firstImageString);
                            images.setSecondImage(secondImageString);
                            databaseReference.setValue(images);

                          Images images1 = snapshot.getValue(Images.class);
                          //  String firstImage = object.
                            try {


                                if (images1 != null) {
                                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                    intent.putExtra("first", images1.getFirstImage());
                                    intent.putExtra("second", images1.getSecondImage());
                                    startActivity(intent);

                                    Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Please capture Image and Preview", Toast.LENGTH_SHORT).show();

                                }
                            }catch (Exception E){
                                E.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled( DatabaseError error) {
                            // if the data is not added or it is cancelled then
                            // we are displaying a failure toast message.
                            Toast.makeText(MainActivity.this, "Fail to add data " , Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });





    }

    public void   displayImage(){
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Yes button Clicked", Toast.LENGTH_LONG).show();
                Log.i("Code2care ", "Yes button Clicked!");
                dialog.dismiss();
            }
        });




        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);

        ImageView imageView1,imageView2;
        imageView1=dialoglayout.findViewById(R.id.imageView1);
        imageView2=dialoglayout.findViewById(R.id.imageView2);
        if (isFirstImage && i==2131230820) {
            imageView1.setImageBitmap(firstPhoto);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yourimage);
            firstPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            firstImageString  = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //  firebaseImages.setFirstImage(firstImageString);

            builder.setView(dialoglayout);
            builder.show();
            imageView2.setVisibility(View.GONE);

        }else {
            imageView2.setImageBitmap(secondPhoto);

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yourimage);
          secondPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
          byte[] imageBytes = baos.toByteArray();
          secondImageString  = Base64.encodeToString(imageBytes, Base64.DEFAULT);


            builder.setView(dialoglayout);
            builder.show();
            imageView1.setVisibility(View.GONE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == first_pic_id && resultCode == -1) {
            // BitMap is data structure of image file which store the image in memory

                firstPhoto = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                btnFirstImageCapture.setImageBitmap(firstPhoto);
                isFirstImage = true;

        }

        if (requestCode == second_pic_id && resultCode == -1) {
            // BitMap is data structure of image file which store the image in memory


                secondPhoto = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
                btnSecondImageCapture.setImageBitmap(secondPhoto);
                isSecondImage = true;

        }
    }

}