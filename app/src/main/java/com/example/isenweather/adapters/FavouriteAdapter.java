package com.example.isenweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isenweather.R;
import com.example.isenweather.utils.ForecastInformation;
import com.example.isenweather.utils.OnItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>  {

    private List<ForecastInformation> mInfo;
    private final OnItemClickListener mlistener;
    private Context context;

    public FavouriteAdapter(List<ForecastInformation> mInfo, Context context, OnItemClickListener mlistener) {
        this.mInfo = mInfo;
        this.context = context;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.favourite_forecast, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    public void updateData(List<ForecastInformation> mInfo){
        mInfo.clear();
        this.mInfo = mInfo;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        final ForecastInformation infos = (ForecastInformation) mInfo.get(position);

        //Set the city name
        holder.city_name.setText(infos.getCity_name());
        //Set the date
        holder.dateTime.setText(infos.getDateTime());
        //Set the temperature
        holder.temperature.setText(infos.getTemperature());
        //Set the pressure
        //holder.pressure.setText(infos.getPressure());
        //Set the humidity
        //holder.humidity.setText(infos.getHumidity());
        //Set the description
        holder.description.setText(infos.getDescription());

        int imageRessource = context.getResources().getIdentifier("ic_"+infos.getImage_name(), "drawable", context.getPackageName());
        holder.image.setImageResource(imageRessource);

        holder.bind(infos, mlistener);
    }

    @Override
    public long getItemId(int position) {
        return null != mInfo ? position : null;
    }

    @Override
    public int getItemCount() {
        return null != mInfo ? mInfo.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView city_name;
        public TextView dateTime;
        public ImageView image;
        public TextView temperature;
        //public TextView pressure;
        //public TextView humidity;
        public TextView description;

        public ViewHolder(View view){
            super(view);

            city_name = (TextView) view.findViewById(R.id.textViewCityNameFavourite);
            dateTime = (TextView) view.findViewById(R.id.textViewDateTime);
            image = (ImageView)  view.findViewById(R.id.imageWeather);
            temperature = (TextView) view.findViewById(R.id.textViewTemperature);
            //pressure = (TextView) view.findViewById(R.id.textViewPressure);
            //humidity = (TextView) view.findViewById(R.id.textViewHumidity);
            description = (TextView) view.findViewById(R.id.textViewDescription);
        }

        public void bind(final ForecastInformation item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }
}
