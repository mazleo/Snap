package io.github.mazleo.snap.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import io.github.mazleo.snap.R;
import io.github.mazleo.snap.controllers.ImageGridAdapter;
import io.github.mazleo.snap.controllers.QueryViewModel;
import io.github.mazleo.snap.utils.DisplayUtility;
import io.github.mazleo.snap.utils.SearchInfo;
import io.github.mazleo.snap.utils.WindowUtility;

public class ImageSearchActivity extends AppCompatActivity {
    private ImageView appLogo;
    private SearchView searchBar;
    private RecyclerView imageGrid;
    private QueryViewModel queryViewModel;
    private AppCompatActivity activity;
    private ProgressBar progressBar;
    private ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        appLogo = findViewById(R.id.image_search_activity_logo);
        searchBar = findViewById(R.id.image_search_activity_search_bar);
        imageGrid = findViewById(R.id.image_search_activity_recycler_view);
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        progressBar = findViewById(R.id.image_search_progress);
        rootLayout = findViewById(R.id.image_search_activity_root_layout);
        activity = this;

        // Get search bar ready
        Intent prevIntent = getIntent();
        searchBar.setQuery(prevIntent.getStringExtra("SEARCH_ACTIVITY_QUERY"), false);
        searchBar.requestFocus();

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Do nothing
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                queryViewModel.cancelSearchResultRetrieval();

                queryViewModel.setSearchProgress(SearchInfo.SEARCH_IN_PROGRESS);
                queryViewModel.fetchSearchResult(1, SearchInfo.RESULTS_PER_PAGE, SearchInfo.SEARCH_TYPE_IMAGE, s, activity);

                int dpi = DisplayUtility.getScreenDensityDPI(activity);
                int paddingPX = (int) WindowUtility.convertDPToPX(80, dpi);
                imageGrid.setPadding(0, 0, 0, paddingPX);

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
                    if (searchResult != null) {
                        int listSize = searchResult.getListPexelsElement().size();
                        int totalSize = searchResult.getSearchState().getTotalResults();
                        if (listSize < totalSize) {
                            int dpi = DisplayUtility.getScreenDensityDPI(activity);
                            int paddingPX = (int) WindowUtility.convertDPToPX(80, dpi);

                            imageGrid.setPadding(0, 0, 0, paddingPX);
                        }
                        else {
                            imageGrid.setPadding(0, 0, 0, 0);
                        }
                    }
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
                }
        );

        this.imageGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (queryViewModel.getSearchResultObject() != null) {
                    int listLength = queryViewModel.getSearchResultObject().getListPexelsElement().size();
                    int lastVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItem == listLength - 1) {
                        if (listLength < queryViewModel.getSearchResultObject().getSearchState().getTotalResults()) {
                            progressBar.setVisibility(View.VISIBLE);

                            if (queryViewModel.getSearchProgress() == SearchInfo.SEARCH_NO_PROGRESS) {
                                String query = searchBar.getQuery().toString();
                                int nextPage = queryViewModel.getSearchResultObject().getSearchState().getOpenPages() + 1;

                                queryViewModel.setSearchProgress(SearchInfo.SEARCH_IN_PROGRESS);
                                queryViewModel.fetchSearchResult(nextPage, SearchInfo.RESULTS_PER_PAGE, SearchInfo.SEARCH_TYPE_IMAGE, query, activity);
                            }
                        }
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
