package io.github.mazleo.snap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

import io.github.mazleo.snap.R;

public class MainActivity extends AppCompatActivity {

    private ImageView appLogo;
    private AppCompatActivity activity;
    private boolean activitySwitched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();
    }

    private void initializeFields() {
        appLogo = findViewById(R.id.launch_activity_logo);
        activity = this;
        activitySwitched = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        startNewActivityOnTimerEnd();
    }

    private void startNewActivityOnTimerEnd() {
        new CountDownTimer(1000, 1000) {
            @Override
            public void onFinish() {
                startNewActivityWithTransitionAnimation();
                activitySwitched = true;
            }
            @Override
            public void onTick(long l) {}
        }.start();
    }

    private void startNewActivityWithTransitionAnimation() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, appLogo, "animated_logo");
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (activitySwitched) {
            activity.finish();
        }
    }
}
