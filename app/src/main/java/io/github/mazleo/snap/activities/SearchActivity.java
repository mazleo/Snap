package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import io.github.mazleo.snap.R;

public class SearchActivity extends AppCompatActivity {

    private ImageView appLogo;
    private SearchView searchBar;
    private AppCompatActivity activity;
    private boolean activitySwitched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeFields();
        passOnSearchSubmit();
        startNewActivityOnQueryChange();
    }

    private void startNewActivityOnQueryChange() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                activitySwitched = true;
                startActivityWithTransitionAnimation(s);

                return true;
            }
        });
    }

    private void startActivityWithTransitionAnimation(String s) {
        Intent intent = new Intent(getApplicationContext(), ImageSearchActivity.class);
        intent.putExtra("SEARCH_ACTIVITY_QUERY", s);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, Pair.create((View) appLogo, "animated_logo"), Pair.create((View) searchBar, "animated_search_bar"));
        startActivity(intent, options.toBundle());
    }

    private void passOnSearchSubmit() {
        searchBar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
    }

    private void initializeFields() {
        appLogo = findViewById(R.id.search_activity_logo);
        searchBar = findViewById(R.id.search_activity_search_bar);
        activity = this;
        activitySwitched = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (activitySwitched) {
            this.finish();
        }
    }
}
