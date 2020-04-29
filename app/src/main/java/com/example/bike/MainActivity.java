package com.example.bike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.bike.Data.WeatherRepository;
import com.example.bike.Model.Comparison;
import com.example.bike.Model.NetworkHandler;
import com.example.bike.Model.WeatherHandler;
import com.example.bike.Util.Pref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private WeatherHandler weatherHandler;
    private final int LAUNCH_FIRST_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate Classes
        Pref pref = new Pref(this);
        Comparison comparison = new Comparison();
        WeatherRepository weatherRepository = new WeatherRepository(this);
        weatherHandler = new WeatherHandler(this, comparison, pref, weatherRepository);
        NetworkHandler networkHandler = new NetworkHandler(this, pref, weatherHandler);

        // Find UI Views
        ImageButton settings = findViewById(R.id.settings_imagebutton);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton previousButton = findViewById(R.id.prevButton);

        // Set Button listeners
        settings.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        // Check network status
        if (networkHandler.haveNetworkConnection()) {
            System.out.println("CONNECTED!");
            networkHandler.ConnectedToNetwork();
        }
        else {
            System.out.println("DISCONNECTED!");
            networkHandler.NotConnectedToNetwork();
        }
    }

    // Monitor button clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_imagebutton:
                redirectToNewActivity();
                break;
            case R.id.prevButton:
                weatherHandler.reduceIndex();
                weatherHandler.handleUpdateUI();
                break;
            case R.id.nextButton:
                weatherHandler.increaseIndex();
                weatherHandler.handleUpdateUI();
                break;
        }
    }

    // Redirect to PreferencesActivity class
    private void redirectToNewActivity() {
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivityForResult(intent, LAUNCH_FIRST_ACTIVITY);
    }

    // Recreate MainActivity class when control is returned from PreferenceActivity class
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_FIRST_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                recreate();
            }
        }
    }
}
