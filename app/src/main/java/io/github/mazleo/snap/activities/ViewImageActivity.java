package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

        initializeFields();
        updateImageUIOnChange();
        updateProgressUIOnChange();
        indicateErrorOnError();
        cleanUpOnBackButtonClick();
        showInfoOnInfoButtonClick();

        this.imageViewModel.downloadImage(pexelsImage, activity);
    }

    private void showInfoOnInfoButtonClick() {
        this.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.show(getSupportFragmentManager(), "info-dialog");
            }
        });
    }

    private void cleanUpOnBackButtonClick() {
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
    }

    private void indicateErrorOnError() {
        this.imageViewModel.getErrorLiveData().observe(this,
                error -> {
                    if (error) {
                        Toast.makeText(activity, "An error has occured while downloading the image", Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                }
        );
    }

    private void updateProgressUIOnChange() {
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
    }

    private void updateImageUIOnChange() {
        this.imageViewModel.getImageLiveData().observe(this,
                pImage -> {
                    if (pImage != null) {
                        setImageInUI(pImage);
                    }
                }
        );
    }

    private void setImageInUI(PexelsImage pImage) {
        pexelsImage = pImage;
        Bitmap image = pImage.getImageBitmap();
        if (image != null) {
            imageView.setImageBitmap(image);
        }
    }

    private void initializeFields() {
        pexelsImage = getIntent().getParcelableExtra("PEXELS_IMAGE");
        imageView = findViewById(R.id.view_image_activity_imageview);
        backButton = findViewById(R.id.view_image_activity_back_button);
        infoButton = findViewById(R.id.view_image_activity_information_button);
        progressBar = findViewById(R.id.view_image_activity_progress_bar);
        activity = this;
        infoDialog = ImageInfoDialogFragment.newInstance(pexelsImage);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
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
