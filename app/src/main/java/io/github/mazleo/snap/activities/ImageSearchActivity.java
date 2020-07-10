package io.github.mazleo.snap.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.mazleo.snap.R;
import io.github.mazleo.snap.controllers.IdItemDetailsLookup;
import io.github.mazleo.snap.controllers.IdKeyProvider;
import io.github.mazleo.snap.controllers.ImageGridAdapter;
import io.github.mazleo.snap.controllers.QueryViewModel;
import io.github.mazleo.snap.model.PexelsImage;
import io.github.mazleo.snap.model.SearchResult;
import io.github.mazleo.snap.utils.DisplayUtility;
import io.github.mazleo.snap.utils.SearchInfo;
import io.github.mazleo.snap.utils.WindowUtility;

public class ImageSearchActivity extends AppCompatActivity {
    private SearchView searchBar;
    private RecyclerView imageGrid;
    private QueryViewModel queryViewModel;
    private AppCompatActivity activity;
    private ProgressBar progressBar;
    private TextView noResultTextView;
    private SelectionTracker selectionTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);

        initializeFields();
        prepareSearchBar();
        addQueryTextListenerToSearchBar();

        GridLayoutManager gridLayoutManager = DisplayUtility.getScreenOrientation(this) == DisplayUtility.ORIENTATION_PORTRAIT
                ? new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
                : new GridLayoutManager(this, 6, LinearLayoutManager.VERTICAL, false);
        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(this.queryViewModel.getSearchResultLiveData());
        imageGrid.setLayoutManager(gridLayoutManager);
        imageGrid.setAdapter(imageGridAdapter);

        updateUIOnSearchResultChange(imageGridAdapter);
        updateProgressBarUIOnChange();
        indicateErrorOnChange();
        indicateNoResultOnChange();
        fetchNextSearchResultPageOnScrollToBottom(gridLayoutManager);
    }

    private void fetchNextSearchResultPageOnScrollToBottom(GridLayoutManager gridLayoutManager) {
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
                                fetchNextSearchResultPage();
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

    private void fetchNextSearchResultPage() {
        String query = searchBar.getQuery().toString();
        int nextPage = queryViewModel.getSearchResultObject().getSearchState().getOpenPages() + 1;

        queryViewModel.setSearchProgress(SearchInfo.SEARCH_IN_PROGRESS);
        queryViewModel.fetchSearchResult(nextPage, SearchInfo.RESULTS_PER_PAGE, SearchInfo.SEARCH_TYPE_IMAGE, query, activity);
    }

    private void indicateNoResultOnChange() {
        this.queryViewModel.getNoResultLiveData().observe(this,
                noResult -> {
                    if (noResult) {
                        noResultTextView.setVisibility(View.VISIBLE);
                    }
                    else {
                        noResultTextView.setVisibility(View.INVISIBLE);
                    }
                }
        );
    }

    private void indicateErrorOnChange() {
        this.queryViewModel.getErrorLiveData().observe(this,
                error -> {
                    if (error) {
                        Toast.makeText(activity, "An error has occured during the search process. The search process has been cancelled.", Toast.LENGTH_LONG).show();
                        setQueryViewModelError(false);
                    }
                }
        );
    }

    private void updateProgressBarUIOnChange() {
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
    }

    private void updateUIOnSearchResultChange(ImageGridAdapter imageGridAdapter) {
        this.queryViewModel.getSearchResultLiveData().observe(this,
                searchResult -> {
                    clearSelectionsInSelectionTracker();
                    imageGridAdapter.notifyDataSetChanged();
                    if (searchResult != null) {
                        setPaddingToImageGridIfNotAllResultsFetched(searchResult);

                        if (this.selectionTracker == null) {
                            buildSelectionTracker();
                        }
                        imageGridAdapter.setSelectionTracker(this.selectionTracker);
                    }
                }
        );
    }

    private void buildSelectionTracker() {
        this.selectionTracker = new SelectionTracker.Builder<>(
                "image-selection",
                this.imageGrid,
                new IdKeyProvider(1, this.imageGrid),
                new IdItemDetailsLookup(this.imageGrid),
                StorageStrategy.createLongStorage()
        )
                .withOnItemActivatedListener(
                        (item, e) -> {
                            selectSelectionKey(item);
                            return true;
                        }
                )
                .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
                .build();
        this.selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                super.onItemStateChanged(key, selected);
                if (selected) {
                    startActivityWithSelectedPexelsImage((long) key);
                }
            }
        });
    }

    private void startActivityWithSelectedPexelsImage(@NonNull long key) {
        int pos = Math.toIntExact(key);
        PexelsImage pexelsImage = (PexelsImage) ((ImageSearchActivity) activity).getQueryViewModel().getSearchResultObject().getListPexelsElement().get(pos);
//                                        PexelsImage pexelsImage = (PexelsImage) ac.getListPexelsElement().get(pos);
        Intent intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtra("PEXELS_IMAGE", pexelsImage);

        startActivity(intent);
    }

    private void selectSelectionKey(ItemDetailsLookup.ItemDetails item) {
        if (!this.selectionTracker.isSelected(item.getSelectionKey())) {
            this.selectionTracker.select(item.getSelectionKey());
        }
    }

    private void setPaddingToImageGridIfNotAllResultsFetched(SearchResult searchResult) {
        int listSize = searchResult.getListPexelsElement().size();
        int totalSize = searchResult.getSearchState().getTotalResults();
        if (listSize < totalSize) {
            addSpaceToBottomOfImageGridForProgressBar();
        }
        else {
            imageGrid.setPadding(0, 0, 0, 0);
        }
    }

    private void addQueryTextListenerToSearchBar() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 0) {
                    clearSelectionsInSelectionTracker();
                    hideSoftInput();
                    fetchSearchResults(s);
                    addSpaceToBottomOfImageGridForProgressBar();
                }

                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() > 0) {
                    clearSelectionsInSelectionTracker();
                    fetchSearchResults(s);

                    addSpaceToBottomOfImageGridForProgressBar();
                }

                return true;
            }
        });
    }

    private void addSpaceToBottomOfImageGridForProgressBar() {
        int dpi = DisplayUtility.getScreenDensityDPI(activity);
        int paddingPX = (int) WindowUtility.convertDPToPX(80, dpi);
        imageGrid.setPadding(0, 0, 0, paddingPX);
    }

    private void hideSoftInput() {
        View focusedView = getCurrentFocus();
        if (focusedView == null) {
            focusedView = new View(activity);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        searchBar.clearFocus();
    }

    private void fetchSearchResults(String s) {
        queryViewModel.cancelSearchResultRetrieval();
        queryViewModel.setSearchProgress(SearchInfo.SEARCH_IN_PROGRESS);
        queryViewModel.setNoResult(false);
        queryViewModel.fetchSearchResult(1, SearchInfo.RESULTS_PER_PAGE, SearchInfo.SEARCH_TYPE_IMAGE, s, activity);
    }

    private void clearSelectionsInSelectionTracker() {
        if (selectionTracker != null) {
            if (selectionTracker.hasSelection()) {
                selectionTracker.clearSelection();
            }
        }
    }

    private void prepareSearchBar() {
        Intent prevIntent = getIntent();
        searchBar.setQuery(prevIntent.getStringExtra("SEARCH_ACTIVITY_QUERY"), false);
        searchBar.requestFocus();
    }

    private void initializeFields() {
        searchBar = findViewById(R.id.image_search_activity_search_bar);
        imageGrid = findViewById(R.id.image_search_activity_recycler_view);
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        progressBar = findViewById(R.id.image_search_progress);
        noResultTextView = findViewById(R.id.image_search_activity_no_result);
        activity = this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void setQueryViewModelError(boolean error) {
        this.queryViewModel.setError(error);
    }

    public QueryViewModel getQueryViewModel() {
        return this.queryViewModel;
    }
}
