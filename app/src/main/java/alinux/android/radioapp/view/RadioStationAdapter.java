package alinux.android.radioapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import alinux.android.radioapp.R;
import alinux.android.radioapp.model.RadioStation;

/**
 * Created by mihai on 7/23/16.
 */
public class RadioStationAdapter extends ArrayAdapter<RadioStation> {
    public RadioStationAdapter(Context context, ArrayList<RadioStation> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RadioStation radioStation = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.radio_station, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.stationName);
        // Populate the data into the template view using the data object
        tvName.setText(radioStation.getName());

        // Return the completed view to render on screen
        return convertView;
    }
}