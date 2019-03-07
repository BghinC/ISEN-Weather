package com.example.isenweather.ui.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isenweather.R;
import com.example.isenweather.WeatherApplication;
import com.example.isenweather.adapters.WeatherAdapter;
import com.example.isenweather.model.City;
import com.example.isenweather.utils.API_request;
import com.example.isenweather.utils.Constants;
import com.example.isenweather.utils.ForecastInformation;
import com.example.isenweather.utils.OnItemClickListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ConstraintLayout mEmptyRecyclerView;
    private TextView textViewCityName;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        mEmptyRecyclerView = (ConstraintLayout) rootView.findViewById(R.id.layoutEmptyView);
        mRecyclerView = rootView.findViewById(R.id.listViewForecast);

        Bundle bundle = this.getArguments();
        String city_name = bundle.getString("city_name", "null");
        final City city = new City();
        city.setName(city_name);

        textViewCityName = getActivity().findViewById(R.id.textViewCityName);

        List<ForecastInformation> forecastInformationList = getListForecast(city);

        if(forecastInformationList == null){

            textViewCityName.setText(city.getName());
            ImageButton imageButtonStar = getActivity().findViewById(R.id.imageButtonStar);
            mEmptyRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            imageButtonStar.setVisibility(View.GONE);

            LinearLayout layout_noInternet = new LinearLayout(getContext());
            layout_noInternet.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout_noInternet.setGravity(Gravity.CENTER);
            final ImageButton imageButton = new ImageButton(getActivity());
            imageButton.setImageResource(R.drawable.refresh);
            imageButton.setForegroundGravity(Gravity.CENTER);
            imageButton.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final RotateAnimation rotateAnim = new RotateAnimation(0.0f, 360f*2,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnim.setDuration(1500);
                    rotateAnim.setFillAfter(true);
                    imageButton.startAnimation(rotateAnim);
                    getListForecast(city);
                }
            });
            layout_noInternet.addView(imageButton);
            mEmptyRecyclerView.addView(layout_noInternet);


        }else if(forecastInformationList.get(0).getDescription().equals("404")){
            Toast.makeText(WeatherApplication.getContext(), "City not found !", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        else{
            mEmptyRecyclerView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            textViewCityName.setText(city.getName());
            setAdapter(forecastInformationList);
        }

        return rootView;
    }

    private List<ForecastInformation> getListForecast(City city){
        API_request api = new API_request();
        List<ForecastInformation> forecastInformationList = api.get(city);

        if(forecastInformationList != null){
            mEmptyRecyclerView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            ImageButton imageButtonStar = getActivity().findViewById(R.id.imageButtonStar);
            imageButtonStar.setVisibility(View.VISIBLE);
            setAdapter(forecastInformationList);
        }else{
            Toast.makeText(WeatherApplication.getContext(), "No internet connection !", Toast.LENGTH_SHORT).show();
        }

        return forecastInformationList;
    }

    private void setAdapter(List<ForecastInformation> forecastInformationList){
        final WeatherAdapter adapter = new WeatherAdapter(forecastInformationList, this.getContext(), new OnItemClickListener() {
            @Override
            public void onItemClick(ForecastInformation info) {
                //When click on a favourite city, go to the CityWeather activity
                final FragmentTransaction transaction = getFragmentManager().beginTransaction();

                final ForecastDetailsFragment fragment = new ForecastDetailsFragment();
                final Bundle bundle = new Bundle();
                bundle.putString(Constants.Information.TEMPERATURE, info.getTemperature());
                bundle.putString(Constants.Information.HUMIDITY, info.getHumidity());
                bundle.putString(Constants.Information.PRESSURE, info.getPressure());
                bundle.putString(Constants.Information.CLOUDINESS, info.getCloudiness());
                bundle.putString(Constants.Information.WIND, info.getWind());
                bundle.putString(Constants.Information.IMAGENAME, info.getImage_name());
                bundle.putString(Constants.Information.DATETIME, info.getDateTime());
                bundle.putString(Constants.Information.DESCRIPTION, info.getDescription());
                fragment.setArguments(bundle);

                transaction.setCustomAnimations(R.animator.slide_in_right,0,0,R.animator.slide_out_left);
                transaction.replace(R.id.containerFragment,fragment);
                transaction.addToBackStack(null).commit();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }
}