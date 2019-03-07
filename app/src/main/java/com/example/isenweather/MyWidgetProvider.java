package com.example.isenweather;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isenweather.adapters.FavouriteAdapter;
import com.example.isenweather.database.DatabaseHelper;
import com.example.isenweather.model.City;
import com.example.isenweather.utils.API_request;
import com.example.isenweather.utils.Constants;
import com.example.isenweather.utils.ForecastInformation;
import com.example.isenweather.utils.OnItemClickListener;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Intent.getIntent;
import static com.example.isenweather.WeatherApplication.getContext;

public class MyWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLIC = "ACTION_CLIC";

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Executor executor2 = Executors.newSingleThreadExecutor();
    private DatabaseHelper dbh = DatabaseHelper.getInstance();

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {

        // Retrouver tous les id
        final ComponentName thisWidget = new ComponentName(context,
                MyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (final int widgetId : allWidgetIds) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                            R.layout.widget_layout);
                    City cityToDisplay = new City();
                    int number = (new Random().nextInt(100));

                    List<City> cities = dbh.getWeatherDao().getAll();
                    // Si des la base de données n'est pas vide
                    if(cities.size() > 0) {
                        int randCity = 0;
                        if(cities.size() > 1) randCity = (new Random().nextInt(cities.size()));
                        cityToDisplay.setName(Constants.Weather.ERROR);

                        if(isOnline()){
                            API_request api = new API_request();
                            List<ForecastInformation> forecastInformationList = api.get(cities.get(randCity));

                            if(forecastInformationList != null){
                                Log.i("Debug","1" + forecastInformationList.get(0).getCity_name());
                                // .get(0) --> Current meteo, .get(1) --> Tomorrow, .get(2) --> After-Tomorrow
                                cityToDisplay.setName(forecastInformationList.get(0).getCity_name());
                                cityToDisplay.setTemperature(forecastInformationList.get(0).getTemperature());
                                cityToDisplay.setHumidity(forecastInformationList.get(0).getHumidity());
                                cityToDisplay.setDescription(forecastInformationList.get(0).getDescription());
                                cityToDisplay.setPressure(forecastInformationList.get(0).getPressure());
                                cityToDisplay.setDateTime(forecastInformationList.get(0).getDateTime());
                                cityToDisplay.setImage_name(forecastInformationList.get(0).getImage_name());
                            }
                        }
                        else{
                            Log.i("Debug","2" + cities.get(randCity).getName());
                            cityToDisplay.setName(cities.get(randCity).getName());
                            cityToDisplay.setTemperature(cities.get(randCity).getTemperature());
                            cityToDisplay.setHumidity(cities.get(randCity).getHumidity());
                            cityToDisplay.setDescription(cities.get(randCity).getDescription());
                            cityToDisplay.setPressure(cities.get(randCity).getPressure());
                            cityToDisplay.setDateTime(cities.get(randCity).getDateTime());
                            cityToDisplay.setImage_name(cities.get(randCity).getImage_name().substring(3));
                        }
                    }
                    else{
                        cityToDisplay.setName("\n\nAdd a city in your favorites");
                        Log.i("Debug","3" + cityToDisplay.getName());
                    }

                    // Définir le texte
                    //remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName()+"\n"+cityToDisplay.getTemperature()+"\n"+cityToDisplay.getHumidity()+"\n"+cityToDisplay.getDescription()+"\n"+cityToDisplay.getDateTime()+"\n"+String.valueOf(number));
                    //remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName()+"\n"+cityToDisplay.getTemperature()+"\n"+cityToDisplay.getHumidity()+"\n"+cityToDisplay.getDescription()+"\n"+cityToDisplay.getDateTime());
                    /*int imageRessource = getContext().getResources().getIdentifier("ic_"+cityToDisplay.getImage_name(), "drawable", getContext().getPackageName());
                    if(imageRessource != 0) {
                        remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName()+"\n"+cityToDisplay.getTemperature()+"\n"+cityToDisplay.getHumidity()+"\n"+cityToDisplay.getDescription()+"\n"+cityToDisplay.getDateTime());
                        remoteViews.setImageViewResource(R.id.widget_image, imageRessource);
                    }
                    else {
                        remoteViews.setImageViewResource(R.id.widget_image, R.drawable.ic_02d);
                        remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName());
                    }*/
                    int imageRessource = getContext().getResources().getIdentifier("ic_"+cityToDisplay.getImage_name(), "drawable", getContext().getPackageName());
                    Log.i("Debug",Integer.toString(imageRessource));
                    if(imageRessource != 0) remoteViews.setImageViewResource(R.id.widget_image, imageRessource);
                    else remoteViews.setImageViewResource(R.id.widget_image, R.drawable.ic_02d);

                    if(cityToDisplay.getName().equals("\n\nAdd a city in your favorites")) remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName());
                    else remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName()+"\n"+cityToDisplay.getTemperature()+"\n"+cityToDisplay.getHumidity()+"\n"+cityToDisplay.getDescription()+"\n"+cityToDisplay.getDateTime());

                    // Enregistrer un onClickListener
                    Intent intent = new Intent(context, MyWidgetProvider.class);

                    intent.setAction(ACTION_CLIC);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);


                    PendingIntent pi = PendingIntent.getBroadcast(context,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.widget_button, pi);
/*
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.widget_city_name, pendingIntent);
*/
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            });

            /*
            // créer des données aléatoires
            int number = (new Random().nextInt(100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            Log.w("WidgetExample", String.valueOf(number));
            // Définir le texte
            remoteViews.setTextViewText(R.id.update, String.valueOf(number));

            // Enregistrer un onClickListener
            Intent intent = new Intent(context, MyWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
            */
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Toast.makeText(context, "Button Clicked.....!!!", Toast.LENGTH_SHORT).show();

        if (ACTION_CLIC.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            int[] widgetIds = bundle.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            changeFavOnWidget(context,widgetIds);
        }
    };

    private void changeFavOnWidget(final Context context, final int[] widgetIds) {

        for (final int widgetId : widgetIds) {
            executor2.execute(new Runnable() {
                @Override
                public void run() {
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                            R.layout.widget_layout);
                    City cityToDisplay = new City();
                    int number = (new Random().nextInt(100));

                    List<City> cities = dbh.getWeatherDao().getAll();
                    // Si des la base de données n'est pas vide
                    if(cities.size() > 0) {
                        int randCity = 0;
                        if(cities.size() > 1) randCity = (new Random().nextInt(cities.size()));
                        cityToDisplay.setName(Constants.Weather.ERROR);

                        if(isOnline()){
                            API_request api = new API_request();
                            List<ForecastInformation> forecastInformationList = api.get(cities.get(randCity));

                            if(forecastInformationList != null){
                                Log.i("Debug","1" + forecastInformationList.get(0).getCity_name());
                                // .get(0) --> Current meteo, .get(1) --> Tomorrow, .get(2) --> After-Tomorrow
                                cityToDisplay.setName(forecastInformationList.get(0).getCity_name());
                                cityToDisplay.setTemperature(forecastInformationList.get(0).getTemperature());
                                cityToDisplay.setHumidity(forecastInformationList.get(0).getHumidity());
                                cityToDisplay.setDescription(forecastInformationList.get(0).getDescription());
                                cityToDisplay.setPressure(forecastInformationList.get(0).getPressure());
                                cityToDisplay.setDateTime(forecastInformationList.get(0).getDateTime());
                                cityToDisplay.setImage_name(forecastInformationList.get(0).getImage_name());
                            }
                        }
                        else{
                            Log.i("Debug","2" + cities.get(randCity).getName());
                            cityToDisplay.setName(cities.get(randCity).getName());
                            cityToDisplay.setTemperature(cities.get(randCity).getTemperature());
                            cityToDisplay.setHumidity(cities.get(randCity).getHumidity());
                            cityToDisplay.setDescription(cities.get(randCity).getDescription());
                            cityToDisplay.setPressure(cities.get(randCity).getPressure());
                            cityToDisplay.setDateTime(cities.get(randCity).getDateTime());
                            cityToDisplay.setImage_name(cities.get(randCity).getImage_name().substring(3));
                        }
                    }
                    else{
                        cityToDisplay.setName("\n\nAdd a city in your favorites");
                        Log.i("Debug","3" + cityToDisplay.getName());
                    }

                    int imageRessource = getContext().getResources().getIdentifier("ic_"+cityToDisplay.getImage_name(), "drawable", getContext().getPackageName());
                    Log.i("Debug","Ressource: " + cityToDisplay.getImage_name() +" /// "+Integer.toString(imageRessource));
                    if(imageRessource != 0) remoteViews.setImageViewResource(R.id.widget_image, imageRessource);
                    else remoteViews.setImageViewResource(R.id.widget_image, R.drawable.ic_02d);

                    if(cityToDisplay.getName().equals("\n\nAdd a city in your favorites")) remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName());
                    else remoteViews.setTextViewText(R.id.widget_city_name, cityToDisplay.getName()+"\n"+cityToDisplay.getTemperature()+"\n"+cityToDisplay.getHumidity()+"\n"+cityToDisplay.getDescription()+"\n"+cityToDisplay.getDateTime());

                    Context context = getContext();
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            });
        }
    }


    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            //Toast.makeText(WeatherApplication.getContext(), "No Internet connection!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}