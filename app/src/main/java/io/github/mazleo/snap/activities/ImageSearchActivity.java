package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import io.github.mazleo.snap.R;
import io.github.mazleo.snap.controllers.ImageGridAdapter;
import io.github.mazleo.snap.controllers.QueryViewModel;
import io.github.mazleo.snap.utils.DisplayUtility;
import io.github.mazleo.snap.utils.SearchInfo;

public class ImageSearchActivity extends AppCompatActivity {
    private ImageView appLogo;
    private SearchView searchBar;
    private RecyclerView imageGrid;
    private QueryViewModel queryViewModel;
    private AppCompatActivity activity;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        appLogo = findViewById(R.id.image_search_activity_logo);
        searchBar = findViewById(R.id.image_search_activity_search_bar);
        imageGrid = findViewById(R.id.image_search_activity_recycler_view);
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        progressBar = findViewById(R.id.image_search_progress);
        activity = this;

        // Get search bar ready
        Intent prevIntent = getIntent();
        searchBar.setQuery(prevIntent.getStringExtra("SEARCH_ACTIVITY_QUERY"), false);
        searchBar.requestFocus();

        // TODO: Temporarily use submit to search, then make it to query change
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryViewModel.cancelSearchResultRetrieval();

                queryViewModel.setSearchProgress(SearchInfo.SEARCH_IN_PROGRESS);
                queryViewModel.fetchSearchResult(1, SearchInfo.RESULTS_PER_PAGE, SearchInfo.SEARCH_TYPE_IMAGE, s, activity);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // TODO: Search process
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = DisplayUtility.getScreenOrientation(this) == DisplayUtility.ORIENTATION_PORTRAIT
                ? new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
                : new GridLayoutManager(this, 6, LinearLayoutManager.VERTICAL, false);
        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this.queryViewModel.getSearchResultLiveData());
        imageGrid.setLayoutManager(gridLayoutManager);
        imageGrid.setAdapter(imageGridAdapter);

        this.queryViewModel.getSearchResultLiveData().observe(this,
                searchResult -> {
                    imageGridAdapter.notifyDataSetChanged();
                }
        );

        this.queryViewModel.getSearchProgressLiveData().observe(this,
                searchProgress -> {
                    if (searchProgress == SearchInfo.SEARCH_IN_PROGRESS) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
