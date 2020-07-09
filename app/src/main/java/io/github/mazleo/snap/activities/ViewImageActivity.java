package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.github.mazleo.snap.R;
import io.github.mazleo.snap.controllers.ImageViewModel;
import io.github.mazleo.snap.fragments.ImageInfoDialogFragment;
import io.github.mazleo.snap.model.PexelsImage;

public class ViewImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button backButton;
    private Button infoButton;
    private PexelsImage pexelsImage;
    private ImageViewModel imageViewModel;
    private Activity activity;
    private ProgressBar progressBar;
    private DialogFragment infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        pexelsImage = getIntent().getParcelableExtra("PEXELS_IMAGE");
        imageView = findViewById(R.id.view_image_activity_imageview);
        backButton = findViewById(R.id.view_image_activity_back_button);
        infoButton = findViewById(R.id.view_image_activity_information_button);
        progressBar = findViewById(R.id.view_image_activity_progress_bar);
        activity = this;
        infoDialog = ImageInfoDialogFragment.newInstance(pexelsImage);

        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        this.imageViewModel.getImageLiveData().observe(this,
                pImage -> {
                    if (pImage != null) {
                        pexelsImage = pImage;
                        Bitmap image = pImage.getImageBitmap();
                        if (image != null) {
                            imageView.setImageBitmap(image);
                        }
//                        Log.i("APPDEBUG", "ID: " + pImage.getId());
//                        Log.i("APPDEBUG", "NAME: " + pImage.getPhotographerName());
                    }
                }
        );

        this.imageViewModel.getProgressLiveData().observe(this,
                progress -> {
                    if (progress) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
        );

        this.imageViewModel.getErrorLiveData().observe(this,
                error -> {
                    if (error) {
                        Toast.makeText(activity, "An error has occured while downloading the image", Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                }
        );

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageViewModel != null) {
                    imageViewModel.cleanUpWebService();
                    imageViewModel = null;
                }
                activity.finish();
            }
        });

        this.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.show(getSupportFragmentManager(), "info-dialog");
            }
        });

        this.imageViewModel.downloadImage(pexelsImage, activity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.imageViewModel != null) {
            this.imageViewModel.cleanUpWebService();
            this.imageViewModel = null;
        }
        activity.finish();
    }
}
