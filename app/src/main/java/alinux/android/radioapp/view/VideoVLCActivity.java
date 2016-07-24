package alinux.android.radioapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.w3c.dom.Text;

import alinux.android.radioapp.R;
import alinux.android.radioapp.model.RadioStation;

public class VideoVLCActivity extends Activity  {
    private static final String TAG = VideoVLCActivity.class.getSimpleName();

    public static final String RADIO_STATION_KEY = "radioStation";

    private LibVLC mLibVLC;

    private RadioStation radioStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "VideoVLC -- onCreate -- START ------------");
        setContentView(R.layout.activity_video_vlc);

        Button b = (Button) findViewById(R.id.stop_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLibVLC.stop();
                VideoVLCActivity.this.finish();
            }
        });
        radioStation = (RadioStation) getIntent().getExtras().get(RADIO_STATION_KEY);


        TextView stationName = (TextView) findViewById(R.id.stationName);
        stationName.setText(radioStation.getName());

        try {
            mLibVLC = new LibVLC();
            mLibVLC.setAout(mLibVLC.AOUT_AUDIOTRACK);
            mLibVLC.setVout(mLibVLC.VOUT_ANDROID_SURFACE);
            mLibVLC.setHardwareAcceleration(LibVLC.HW_ACCELERATION_FULL);

            mLibVLC.init(getApplicationContext());
        } catch (LibVlcException e){
            Log.e(TAG, e.toString());
        }

        mLibVLC.playMRL(radioStation.getUrl());

        //TODO - add track info
        //mLibVLC.getAudioTrackDescription().get(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaCodec opaque direct rendering should not be used anymore since there is no surface to attach.
        mLibVLC.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_vlc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
