package com.example.mytask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    private ImageView btnFirstImageCapture,btnSecondImageCapture;
    private Button btnBack;
    private static final int first_pic_id = 123;
    private static final int second_pic_id = 456;
    boolean isFirstImage,isSecondImage=false;
    private AlertDialog.Builder builder;
    private Bitmap firstPhoto,secondPhoto;
    private int i;
    byte[] firstImageBytes,secondImageBytes ;


    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;
    private String firstImageString,secondImageString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnFirstImageCapture=findViewById(R.id.btnFirstImageCapture);
        btnSecondImageCapture=findViewById(R.id.btnSecondImageCapture);
        btnBack=findViewById(R.id.btnBack);

        Intent intent = getIntent();
        String first = intent.getStringExtra("first");
        String second = intent.getStringExtra("second");

        if (first!=null) {
            firstImageBytes = Base64.decode(first, Base64.DEFAULT);
            Bitmap firstDecodedImage = BitmapFactory.decodeByteArray(firstImageBytes, 0, firstImageBytes.length);
            btnFirstImageCapture.setImageBitmap(firstDecodedImage);

        }
        if (second!=null) {
            secondImageBytes = Base64.decode(second, Base64.DEFAULT);
            Bitmap secondDecodedImage = BitmapFactory.decodeByteArray(secondImageBytes, 0, secondImageBytes.length);
            btnSecondImageCapture.setImageBitmap(secondDecodedImage);

        }
     //   image.setImageBitmap(decodedImage);

      //  ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 =new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}