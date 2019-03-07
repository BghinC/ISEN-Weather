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

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private final List<ForecastInformation> mInfo;
    private final OnItemClickListener mlistener;
    private Context context;

    public WeatherAdapter(List<ForecastInformation> mInfo, Context context, OnItemClickListener mlistener) {
        this.mInfo = mInfo;
        this.context = context;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.single_forecast, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the current item
        final ForecastInformation infos = (ForecastInformation) mInfo.get(position);
        //Set the date
        holder.dateTime.setText(infos.getDateTime());
        //Set the temperature
        holder.temperature.setText(infos.getTemperature());
        //Set the pressure
        holder.pressure.setText(infos.getPressure());
        //Set the humidity
        holder.humidity.setText(infos.getHumidity());
        //Set the description
        holder.description.setText(infos.getDescription());

        int imageRessource = context.getResources().getIdentifier("ic_"+infos.getImage_name(), "drawable", context.getPackageName());
        holder.image.setImageResource(imageRessource);
        holder.image.setTag("ic_"+infos.getImage_name());

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
        public ImageView image;
        public TextView dateTime;
        public TextView temperature;
        public TextView pressure;
        public TextView humidity;
        public TextView description;

        public ViewHolder(View view){
            super(view);

            dateTime = (TextView) view.findViewById(R.id.textViewDateTime);
            image = (ImageView)  view.findViewById(R.id.imageWeather);
            temperature = (TextView) view.findViewById(R.id.textViewTemperature);
            pressure = (TextView) view.findViewById(R.id.textViewPressure);
            humidity = (TextView) view.findViewById(R.id.textViewHumidity);
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
