package com.bartovapps.simplewebserv;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {

    ImageView ivDetailedImage;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        initUiComponents();
        imageUri = getIntent().getData();
    }

    private void initUiComponents() {
        ivDetailedImage = (ImageView)findViewById(R.id.ivDetailedImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Picasso.with(this).load(imageUri).fit().centerInside().into(ivDetailedImage);
    }
}
