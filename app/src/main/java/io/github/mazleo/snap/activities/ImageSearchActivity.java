package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import io.github.mazleo.snap.R;

public class ImageSearchActivity extends AppCompatActivity {
    private ImageView appLogo;
    private SearchView searchBar;
    private RecyclerView imageGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        appLogo = findViewById(R.id.image_search_activity_logo);
        searchBar = findViewById(R.id.image_search_activity_search_bar);
        imageGrid = findViewById(R.id.image_search_activity_recycler_view);

        // Get search bar ready
        Intent prevIntent = getIntent();
        searchBar.setQuery(prevIntent.getStringExtra("SEARCH_ACTIVITY_QUERY"), false);
        searchBar.requestFocus();
    }
}
