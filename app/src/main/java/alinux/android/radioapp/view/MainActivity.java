package alinux.android.radioapp.view;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import alinux.android.radioapp.R;
import alinux.android.radioapp.model.RadioStation;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final ArrayList<RadioStation> radioStations = new ArrayList();
        //radioStations.add(new RadioStation("Rock FM", "http://80.86.106.143:9128/rockfm.aacp"));
        radioStations.add(new RadioStation("Rock FM", "http://www.rockfm.ro/rockfm.pls"));
        radioStations.add(new RadioStation("Radio Cafe", "http://live.radiocafe.ro:8048/listen.pls"));
        radioStations.add(new RadioStation("Magic FM", "http://www.magicfm.ro/listen.pls"));


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

    private void playStation(final RadioStation station) {
        AsyncTask checkContentType = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String urlToPlay = station.getUrl();
                try {
                    URL url = new URL(station.getUrl());
                    URLConnection httpConnection = url.openConnection();
                    Map<String, List<String>> headers = httpConnection.getHeaderFields();
                    if (headers != null && headers.get("Content-Type") != null) {

                        String contentType = headers.get("Content-Type").get(0);
                        if (contentType.equals("audio/x-scpls")) {

                            Properties prop = new Properties();
                            prop.load(httpConnection.getInputStream());
                            urlToPlay = prop.get("File1").toString();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    urlToPlay = null;
                }

                return urlToPlay;
            }

            @Override
            protected void onPostExecute(Object o) {
                try {
                    String stationUrl = (String) this.get();
                    if (stationUrl == null) {
                        Toast toast = Toast.makeText(MainActivity.this, R.string.could_not_get_content, Toast.LENGTH_LONG);
                        toast.show();

                    }
                    station.setUrl(stationUrl);
                    Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
                    intent.putExtra(MediaPlayerActivity.RADIO_STATION_KEY, station);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        checkContentType.execute();
    }


}
