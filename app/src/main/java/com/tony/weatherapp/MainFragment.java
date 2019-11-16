package com.tony.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button showWeather;
    private EditText getCity;
    private TextView weatherDescription;

    private static String TAG = "MAIN_FRAGMENT";
    private static String url = "http://api.openweathermap.org/data/2.5/weather?q=minneapolis&appid=9db10307657b0ff8224b0da642ac57f7";

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

        showWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = getCity.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainFragment.this.getContext(), "Enter a city", Toast.LENGTH_SHORT).show();
                }else {
                    getWeather();

                }
            }
        });


        return view;
    }

    private void getWeather() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());


        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("weather");
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String description = jsonObject.getString("description");
                            Log.d(TAG, "description is " + description);
                            weatherDescription.setText(description);

                        } catch (JSONException e) {
                            Log.e(TAG, "Error processing JSON resposne", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching data from compliment server", error);
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
