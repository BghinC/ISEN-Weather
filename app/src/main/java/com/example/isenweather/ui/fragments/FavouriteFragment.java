package com.example.isenweather.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isenweather.MainActivity;
import com.example.isenweather.R;
import com.example.isenweather.WeatherApplication;
import com.example.isenweather.adapters.FavouriteAdapter;
import com.example.isenweather.database.DatabaseHelper;
import com.example.isenweather.model.City;
import com.example.isenweather.utils.API_request;
import com.example.isenweather.utils.ForecastInformation;
import com.example.isenweather.utils.GridSpacingItemDecoration;
import com.example.isenweather.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    private View rootView;
    //private LinearLayout layout_noInternet;
    private RecyclerView mRecyclerView;
    private ConstraintLayout mEmptyRecyclerView;
    private ProgressBar mProgressBar;
    final Executor executor = Executors.newSingleThreadExecutor();
    DatabaseHelper dbh = DatabaseHelper.getInstance();


    private FavouriteAdapter adapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_forecast_favourite, container, false);


        mEmptyRecyclerView = (ConstraintLayout) rootView.findViewById(R.id.layoutProgressBar_favourite);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarForecast_favourite);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listViewForecast_favourite);

        /*
        //If not, set an image button to refresh
        layout_noInternet = new LinearLayout(getContext());
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
                    onResume();
                }
        });
        layout_noInternet.addView(imageButton);
        root.addView(layout_noInternet);
        */

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshFavouritesCities();
    }

    private void refreshFavouritesCities() {
        if(isOnline()){
            mEmptyRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final List<City> cities = dbh.getWeatherDao().getAll();
                    final List<ForecastInformation> infos = new ArrayList<ForecastInformation>();
                    if(cities.size() > 0){
                        //If we have favourite city(ies)
                        mProgressBar.setMax(cities.size());
                        API_request api = new API_request();

                        int counter = 0;
                        for(City city : cities){
                            infos.add(api.getFavourite(city).get(0));
                            infos.get(counter).setCity_name(city.getName());
                            counter++;
                            mProgressBar.setProgress(counter);
                        }
                        if(getActivity() != null){
                            displayFavourites(infos);
                        }
                    }else if(getActivity() != null){
                        displayNoFavourites();
                    }
                }
            });
        }else{
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final List<City> cities = dbh.getWeatherDao().getAll();
                    final List<ForecastInformation> infos = new ArrayList<ForecastInformation>();
                    if(cities.size() > 0){
                        //If we have favourite city(ies)
                        mProgressBar.setMax(cities.size());

                        int counter = 0;
                        for(City city : cities){

                            infos.add(new ForecastInformation(
                                    cities.get(counter).getName(),
                                    cities.get(counter).getDateTime(),
                                    cities.get(counter).getTemperature(),
                                    cities.get(counter).getHumidity(),
                                    cities.get(counter).getPressure(),
                                    cities.get(counter).getDescription(),
                                    cities.get(counter).getImage_name().substring(3)
                            ));
                            counter++;
                            mProgressBar.setProgress(counter);
                        }
                        if(getActivity() != null){
                            displayFavourites(infos);
                        }
                    }else if(getActivity() != null){
                        displayNoFavourites();
                    }
                }
            });
        }
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) WeatherApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(WeatherApplication.getContext(), "No Internet connection!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void displayFavourites(final List<ForecastInformation> infos){
        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.layoutFragmentForecast);
        final TextView textViewFavouriteTitle = root.findViewById(R.id.textViewFavouriteTitle);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewFavouriteTitle.setText("Favourites");
                adapter = new FavouriteAdapter(infos, getContext(), new OnItemClickListener() {
                    @Override
                    public void onItemClick(ForecastInformation item) {
                        //When click on a favourite city, go to the CityWeather activity
                        ((MainActivity)getActivity()).goToCityWeatherActivity(item.getCity_name());
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                int spanCount = 2;
                int spacing = 50;
                boolean includeEdge = true;
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
                //Add spaces between cards
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                //Add animation
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_fall_down);
                mRecyclerView.setLayoutAnimation(controller);
                mRecyclerView.scheduleLayoutAnimation();

                mRecyclerView.setAdapter(adapter);

                mEmptyRecyclerView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void displayNoFavourites(){

        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.layoutFragmentForecast);
        final TextView textViewFavouriteTitle = root.findViewById(R.id.textViewFavouriteTitle);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewFavouriteTitle.setText("No Favourites");
                ViewGroup root = (ViewGroup) rootView.findViewById(R.id.layoutFragmentForecast);
                root.removeView(root.findViewById(R.id.listViewForecast_favourite));
                root.removeView(root.findViewById(R.id.layoutProgressBar_favourite));
            }
        });
    }
}
