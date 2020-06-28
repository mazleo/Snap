package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Do nothing on search button clicked
        searchBar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Do nothing on query submission
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            // Switch activities on searchview query change
            @Override
            public boolean onQueryTextChange(String s) {
                Intent intent = new Intent(getApplicationContext(), ImageSearchActivity.class);
                intent.putExtra("SEARCH_ACTIVITY_QUERY", s);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
