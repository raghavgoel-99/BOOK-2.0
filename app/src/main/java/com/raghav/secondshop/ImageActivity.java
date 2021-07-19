package com.raghav.secondshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    private String Image;
    private ImageView pi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Product Image");
        setSupportActionBar(toolbar);

        pi=findViewById(R.id.productimage);
        Image = getIntent().getExtras().get("IMAGE").toString();
        Picasso.get().load(Image).into(pi);

    }
}