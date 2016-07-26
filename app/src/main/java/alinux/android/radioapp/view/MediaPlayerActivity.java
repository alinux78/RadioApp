package alinux.android.radioapp.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.util.ArrayList;

import alinux.android.radioapp.R;
import alinux.android.radioapp.model.RadioStation;

public class MediaPlayerActivity extends Activity {
    private static final String TAG = MediaPlayerActivity.class.getSimpleName();

    public static final String RADIO_STATION_KEY = "mRadioStation";

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;


    private RadioStation mRadioStation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.d(TAG, "VideoVLC -- onCreate -- START ------------");
        setContentView(R.layout.media_player_activity);

        Button b = (Button) findViewById(R.id.stop_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.stop();
                MediaPlayerActivity.this.finish();
            }
        });

        mRadioStation = (RadioStation) getIntent().getExtras().get(RADIO_STATION_KEY);
        TextView stationName = (TextView) findViewById(R.id.stationName);
        stationName.setText(mRadioStation.getName());

        /*
        TextView trackDetails = (TextView) findViewById(R.id.trackDetails);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        TranslateAnimation m_ta = new TranslateAnimation(0f, dm.widthPixels, 0f, 0f);
        m_ta.setDuration(30000);
        m_ta.setInterpolator(new CycleInterpolator(2f));
        m_ta.setRepeatCount(Animation.INFINITE);
        trackDetails.startAnimation(m_ta);
        */


        createPlayer(mRadioStation.getUrl());
    }

    private void createPlayer(String mediaUrl) {
        releasePlayer();
        try {
            if (mediaUrl.length() > 0) {
                Toast toast = Toast.makeText(this, mediaUrl, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
                toast.show();
            }

            ArrayList<String> options = new ArrayList<String>();
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            mLibVLC = new LibVLC(options);

            // Create media player
            mMediaPlayer = new MediaPlayer(mLibVLC);


            mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
                @Override
                public void onEvent(MediaPlayer.Event event) {
                    if ( mMediaPlayer.getMedia().getTrackCount() > 0) {

                        //TODO - find a better way for getting the description
                        final int magicMetaIndexForTrackDescription = 12;
                        String trackDescription = mMediaPlayer.getMedia().getMeta(magicMetaIndexForTrackDescription);

                        TextView trackDetails = (TextView) findViewById(R.id.trackDetails);
                        trackDetails.setText(trackDescription);



                    }
                }
            });


            Media media = new Media(mLibVLC, Uri.parse(mediaUrl));
            mMediaPlayer.setMedia(media);
            mMediaPlayer.play();
        } catch (Exception e) {
            Toast.makeText(this, "Error creating player!", Toast.LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        if (mLibVLC == null)
            return;
        mMediaPlayer.stop();
        mLibVLC = null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        createPlayer(mRadioStation.getUrl());
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

}
