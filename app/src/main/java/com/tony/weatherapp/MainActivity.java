package com.tony.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, ForecastFragment.OnFragmentInteractionListener {

    private String WEATHER_KEY = "9db10307657b0ff8224b0da642ac57f7";
    private String TAG = "MAIN_ACTIVITY";

    private Button mShowWeather;
    private EditText mGetCity;




    MainFragment mainFragment;

    public MainActivity() {
        //empty constructer
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = MainFragment.newInstance();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, mainFragment);
        ft.commit();
        //displaying main fragment initially











    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
