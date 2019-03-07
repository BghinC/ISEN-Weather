package com.example.isenweather.ui.fragments;


import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isenweather.R;
import com.example.isenweather.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastDetailsFragment extends Fragment {


    public ForecastDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.forecast_details, container, false);

        final String dateTime = getArguments().getString(Constants.Information.DATETIME);
        ((TextView) view.findViewById(R.id.textViewDateTime)).setText(dateTime);

        final String image = getArguments().getString(Constants.Information.IMAGENAME);
        int imageRessource = getContext().getResources().getIdentifier("ic_"+image, "drawable", getContext().getPackageName());
        ((ImageView) view.findViewById(R.id.imageWeather)).setImageResource(imageRessource);
        ((ImageView) view.findViewById(R.id.imageWeather)).setTag("ic_"+image);

        final String description = getArguments().getString(Constants.Information.DESCRIPTION);
        ((TextView) view.findViewById(R.id.textViewDescription)).setText(description);

        final String temperature = getArguments().getString(Constants.Information.TEMPERATURE);
        ((TextView) view.findViewById(R.id.textViewTemperature)).setText(temperature);

        final String humidity = getArguments().getString(Constants.Information.HUMIDITY);
        ((TextView) view.findViewById(R.id.textViewHumidity)).setText(humidity);

        final String pressure = getArguments().getString(Constants.Information.PRESSURE);
        ((TextView) view.findViewById(R.id.textViewPressure)).setText(pressure);

        final String cloudinees = getArguments().getString(Constants.Information.CLOUDINESS);
        ((TextView) view.findViewById(R.id.textViewCloudiness)).setText(cloudinees);

        final String wind = getArguments().getString(Constants.Information.WIND);
        ((TextView) view.findViewById(R.id.textViewWind)).setText(wind);

        return view;
    }

}
