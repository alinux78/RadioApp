package alinux.android.radioapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import alinux.android.radioapp.R;
import alinux.android.radioapp.model.RadioStation;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<RadioStation> radioStations = new ArrayList();
        radioStations.add(new RadioStation("Rock FM", "http://80.86.106.143:9128/rockfm.aacp"));
        radioStations.add(new RadioStation("Radio Cafe", "http://live.radiocafe.ro:8064"));

        ListView stationsList = (ListView) findViewById(R.id.stations_list);
        stationsList.setAdapter(new RadioStationAdapter(this, radioStations));

        stationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                RadioStation station = radioStations.get(position);
                playStation(station);
            }
        });

    }

    private void playStation(RadioStation station) {
        Intent intent = new Intent(getApplicationContext(), VideoVLCActivity.class);
        intent.putExtra(VideoVLCActivity.RADIO_STATION_KEY, station);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
