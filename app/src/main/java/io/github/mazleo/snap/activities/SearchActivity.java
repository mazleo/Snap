package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;

import io.github.mazleo.snap.R;

public class SearchActivity extends AppCompatActivity {

    private ImageView appLogo;
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        appLogo = findViewById(R.id.search_activity_logo);
        searchBar = findViewById(R.id.search_activity_search_bar);
    }
}
