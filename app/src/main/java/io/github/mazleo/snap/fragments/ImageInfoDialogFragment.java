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

    public ImageInfoDialogFragment() {
        super();
    }

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

        initializeFields(infoView);
        populateFields();

        return infoView;
    }

    private void populateFields() {
        if (pexelsImage != null) {
            photoIdTextView.setText(getString(R.string.label_photo_id, pexelsImage.getId()));
            photographerNameTextView.setText(getString(R.string.label_photographer_name, pexelsImage.getPhotographerName()));
            photographerIdTextView.setText(getString(R.string.label_photographer_id, pexelsImage.getPhotographerId()));
            resolutionTextView.setText(getString(R.string.label_resolution, pexelsImage.getWidth(), pexelsImage.getHeight()));
        }
    }

    private void initializeFields(View infoView) {
        photoIdTextView = infoView.findViewById(R.id.dialog_photo_id);
        photographerNameTextView = infoView.findViewById(R.id.dialog_photographer_name);
        photographerIdTextView = infoView.findViewById(R.id.dialog_photographer_id);
        resolutionTextView = infoView.findViewById(R.id.dialog_image_resolution);
    }
}
