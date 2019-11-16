package com.tony.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ForecastFragment extends Fragment {

    private static final String TAG = "FORECAST_FRAGMENT";
    private TextView forecastCity;
    private TextView temp1;
    private TextView desc1;
    private TextView temp2;
    private TextView desc2;
    private TextView temp3;
    private TextView desc3;
    private TextView temp4;
    private TextView desc4;
    private TextView temp5;
    private TextView desc5;


    private static String key = BuildConfig.OPENWEATHER_KEY;
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    private static final String ARG_PARAM1 = "param1";
    private static final String HEADER = "5 Day Forecast For ";




    private String city;


    private OnFragmentInteractionListener mListener;

    public ForecastFragment() {
        // Required empty public constructor
    }


    public static ForecastFragment newInstance(String param1) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        forecastCity = view.findViewById(R.id.display_forecast_city);
        temp1 = view.findViewById(R.id.temp1);
        desc1 = view.findViewById(R.id.desc1);
        temp2 = view.findViewById(R.id.temp2);
        desc2 = view.findViewById(R.id.desc2);
        temp3 = view.findViewById(R.id.temp3);
        desc3 = view.findViewById(R.id.desc3);
        temp4 = view.findViewById(R.id.temp4);
        desc4 = view.findViewById(R.id.desc4);
        temp5 = view.findViewById(R.id.temp5);
        desc5 = view.findViewById(R.id.desc5);

        forecastCity.setText(HEADER+city);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+city+",usa&appid=" + key;

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        double avgDay1 = 0;
                        double avgDay2 = 0;
                        double avgDay3 = 0;
                        double avgDay4 = 0;
                        double avgDay5 = 0;

                        int hourCount = 0;
                        int dayCount = 0;

                        double sum = 0;

                        try {
                            JSONArray jsonArray = response.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length() ; i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                JSONObject obj2 = obj.getJSONObject("main");
                                double tempK = obj2.getDouble("temp");
                                double tempF = (tempK - 273.15) * 9/5 + 32;
                                sum = sum + tempF;
                                JSONArray weather = obj.getJSONArray("weather");
                                JSONObject obj3 = weather.getJSONObject(0);
                                String description = obj3.getString("description");
                                hourCount++;
                                if (hourCount == 7) {
                                    dayCount++;
                                    hourCount = 0;
                                    if (dayCount == 1) {
                                        avgDay1 = sum/8;
                                        temp1.setText(df2.format(avgDay1) + "f");
                                        desc1.setText(description);
                                        sum = 0;
                                    }
                                    if (dayCount == 2) {
                                        avgDay2 = sum/8;
                                        temp2.setText(df2.format(avgDay2) + "f");
                                        desc2.setText(description);
                                        sum = 0;
                                    }
                                    if (dayCount == 3) {
                                        avgDay3 = sum/8;
                                        temp3.setText(df2.format(avgDay3) + "f");
                                        desc3.setText(description);
                                        sum = 0;
                                    }
                                    if (dayCount == 4) {
                                        avgDay4 = sum/8;
                                        temp4.setText(df2.format(avgDay4) + "f");
                                        desc4.setText(description);
                                        sum = 0;
                                    }
                                    if (dayCount == 5) {
                                        avgDay5 = sum/8;
                                        temp5.setText(df2.format(avgDay5) + "f");
                                        desc5.setText(description);
                                        sum = 0;
                                    }
                                }
                            }




                        } catch (JSONException e) {
                            Log.e(TAG, "Error processing JSON response", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Not found",error);


                    }
                }
        );
        queue.add(weatherRequest);






        Log.d(TAG, city);


        return view;
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
        void onFragmentInteraction(Uri uri);
    }
}
