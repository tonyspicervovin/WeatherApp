package com.tony.weatherapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static androidx.core.content.ContextCompat.getSystemService;


public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button showWeather;
    private EditText getCity;
    private TextView weatherDescription;
    private TextView tempDisplay;
    private TextView showWeatherFor;
    private Button showForecastButton;
    private ImageView weatherView;

    private Drawable cloudyDrawable;
    private Drawable snowyDrawable;
    private Drawable sunnyDrawable;
    private Drawable rainyDrawable;


    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private static String TAG = "MAIN_FRAGMENT";
    private static String wrongCity = "Not a valid city";

    private static String key = BuildConfig.OPENWEATHER_KEY;
    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        showWeather = view.findViewById(R.id.show_weather_button);
        getCity = view.findViewById(R.id.get_city);
        weatherDescription = view.findViewById(R.id.weather_description);
        tempDisplay = view.findViewById(R.id.temp_display);
        showWeatherFor = view.findViewById(R.id.showing_weather_for);
        showForecastButton = view.findViewById(R.id.show_forecast_button);
        weatherView = view.findViewById(R.id.weatherView);

        //finding widgets

        cloudyDrawable =getResources().getDrawable(R.drawable.cloudy);
        snowyDrawable = getResources().getDrawable(R.drawable.snowy);
        sunnyDrawable = getResources().getDrawable(R.drawable.sunny);
        rainyDrawable = getResources().getDrawable(R.drawable.rain);

        //finding drawables for weather icon

        showForecastButton.setVisibility(View.GONE);
        weatherView.setVisibility(View.GONE);

        //setting forecast button and weather icon invisible initially



        showWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = getCity.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainFragment.this.getContext(), "Enter a city", Toast.LENGTH_SHORT).show();
                }else {
                    getWeather(city);
                }
                //listener for showing weather, if it's empty it shows a toast msg
            }
        });


        return view;
    }

    private void getWeather(final String city) {

        showForecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ForecastFragment forecastFragment = ForecastFragment.newInstance(city);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, forecastFragment);
                ft.addToBackStack(null);
                ft.commit();

                //switching to forecast fragment if show forecast button is pressed
            }
        });

        String showWeatherForText = "Showing weather for " + city;
        Log.d(TAG, showWeatherForText);

        showWeatherFor.setText(showWeatherForText);


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=" + key;


        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("weather");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String description = jsonObject.getString("description");
                            Log.d(TAG, "description for " + city + " is " + description);
                            weatherDescription.setText(description);

                            //pulling weather description from api

                            JSONObject main = response.getJSONObject("main");
                            double tempK = main.getDouble("temp");
                            double tempF = (tempK - 273.15) * 9/5 + 32;
                            Log.d(TAG, "temp for "+city + " is " + df2.format(tempF));
                            tempDisplay.setText(df2.format(tempF) + "f");

                            //pulling temp from api and converting to f

                            showForecastButton.setVisibility(View.VISIBLE);

                            //showing the forecast button if the api hit successfully

                            if (description.contains("clear")) {
                                Log.d(TAG, "It's clear");
                                weatherView.setImageDrawable(sunnyDrawable);
                            }
                            else if (description.contains("rain")) {
                                Log.d(TAG, "It's rainy");
                                weatherView.setImageDrawable(rainyDrawable);
                            }else if (description.contains("snow")) {
                                Log.d(TAG, "It's snowy");
                                weatherView.setImageDrawable(snowyDrawable);
                            }else if (description.contains("cloud")) {
                                Log.d(TAG, "It's cloudy");
                                weatherView.setImageDrawable(cloudyDrawable);
                            }

                            // changing the icon based on what weather description is returned

                            weatherView.setVisibility(View.VISIBLE);
                            //showing weather icon

                            if (description.isEmpty()){
                                weatherView.setVisibility(View.GONE);
                                //hiding icon if nothing comes back
                            }


                        } catch (JSONException e) {
                            Log.e(TAG, "Error processing JSON resposne", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainFragment.this.getContext(), "Enter a valid city", Toast.LENGTH_SHORT).show();
                        showWeatherFor.setText(wrongCity);
                        weatherDescription.setText("");
                        tempDisplay.setText("");
                        showForecastButton.setVisibility(View.GONE);
                        Log.e(TAG, "Error fetching data from compliment server", error);

                        //if no response clearing weather info and showing a msg

                    }
                }
        );
        queue.add(weatherRequest);

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
