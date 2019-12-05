package com.tony.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tony.weatherapp.model.Day;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ForecastFragment extends Fragment {

    private static final String TAG = "FORECAST_FRAGMENT";


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;

    private List<Day> mDays;

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

        mDays = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.forecast_list);
        mRecyclerView.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mlayoutManager);

        mAdapter = new ForecastListAdapter(mDays);
        mRecyclerView.setAdapter(mAdapter);



        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+city+",usa&appid=" + key;


        Log.d(TAG, url);
        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        try {
                            JSONArray jsonArray = response.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length() ; i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                int unixStamp = obj.getInt("dt");
                                java.util.Date date =new java.util.Date((long)unixStamp*1000);


                                JSONObject obj2 = obj.getJSONObject("main");

                                double tempKMin = obj2.getDouble("temp_min");
                                double tempFMin = (tempKMin - 273.15) * 9/5 + 32;

                                double tempKMax = obj2.getDouble("temp_max");
                                double tempFMax = (tempKMax - 273.15) * 9/5 + 32;

                                JSONArray weather = obj.getJSONArray("weather");
                                JSONObject obj3 = weather.getJSONObject(0);
                                String description = obj3.getString("description");

                                mDays.add(new Day(description, tempFMin, tempFMax, date));
                                mAdapter.notifyItemInserted(mDays.size() - 1);


                                    }
                                }
                        catch (JSONException e) {
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


