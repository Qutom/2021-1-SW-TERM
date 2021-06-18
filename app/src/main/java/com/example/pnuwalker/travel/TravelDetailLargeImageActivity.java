package com.example.pnuwalker.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pnuwalker.R;
import com.github.chrisbanes.photoview.PhotoView;

public class TravelDetailLargeImageActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private PhotoView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail_large_image);

        backBtn = findViewById(R.id.detail_large_image_back_btn);
        imageView =findViewById(R.id.detail_large_image);

        backBtn.setOnClickListener((v) -> finish());

        Intent intent = getIntent();
        int imageId = intent.getIntExtra("image_id", -1);
        imageView.setImageResource(imageId);
    }

}
