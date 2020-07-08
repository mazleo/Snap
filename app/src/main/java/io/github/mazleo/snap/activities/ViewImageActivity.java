package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import io.github.mazleo.snap.R;
import io.github.mazleo.snap.model.PexelsImage;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        PexelsImage pexelsImage = getIntent().getParcelableExtra("PEXELS_IMAGE");
    }
}
