package io.github.mazleo.snap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.github.mazleo.snap.R;
import io.github.mazleo.snap.model.PexelsImage;

public class ImageInfoDialogFragment extends DialogFragment {
    private PexelsImage pexelsImage;
    private TextView photoIdTextView;
    private TextView photographerNameTextView;
    private TextView photographerIdTextView;
    private TextView resolutionTextView;

    public ImageInfoDialogFragment(PexelsImage pexelsImage) {
        super();
        this.pexelsImage = pexelsImage;
    }

    public static ImageInfoDialogFragment newInstance(PexelsImage pexelsImage) {
        Bundle args = new Bundle();
        ImageInfoDialogFragment fragment = new ImageInfoDialogFragment(pexelsImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View infoView = inflater.inflate(R.layout.image_info_dialog, container, false);

        photoIdTextView = infoView.findViewById(R.id.dialog_photo_id);
        photographerNameTextView = infoView.findViewById(R.id.dialog_photographer_name);
        photographerIdTextView = infoView.findViewById(R.id.dialog_photographer_id);
        resolutionTextView = infoView.findViewById(R.id.dialog_image_resolution);

        photoIdTextView.setText("Photo ID: " + pexelsImage.getId());
        photographerNameTextView.setText("Photographer: " + pexelsImage.getPhotographerName());
        photographerIdTextView.setText("Photographer ID: " + pexelsImage.getPhotographerId());
        resolutionTextView.setText("Resolution: " + pexelsImage.getWidth() + " x " + pexelsImage.getHeight());

        return infoView;
    }
}
