package com.daasuu.exoplayerfilter;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.daasuu.epf.EPlayerView;
import com.example.gaowei.filterplayer.ExoPlayerAdapter;
import com.example.gaowei.filterplayer.FilterPlayer;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;
    private SeekBar seekBar;
    private PlayerTimer playerTimer;
    private EPlayerView mFilterPlayerView;

    private FilterPlayer mFilterPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        checkPermissionIfNecessary();
        mFilterPlayer = new ExoPlayerAdapter();
    }

    private void checkPermissionIfNecessary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> grantList = new ArrayList<>();
            if (!PermissionUtils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                grantList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            String[] grantArray = grantList.toArray(new String[grantList.size()]);
            if (grantArray != null && grantArray.length > 0) {
                ActivityCompat.requestPermissions(this, grantArray, 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFilterPlayer.initialize(this, Constant.STREAM_URL_MP4_VOD_LONG);
//        setUpSimpleExoPlayer();
        setUoGlPlayerView();
        setUpTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
        if (playerTimer != null) {
            playerTimer.stop();
            playerTimer.removeMessages(0);
        }
    }

    private void setUpViews() {
        // play pause
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilterPlayer == null) return;

                if (button.getText().toString().equals(MainActivity.this.getString(R.string.pause))) {
                    mFilterPlayer.pause();
                    button.setText(R.string.play);
                } else {
                    mFilterPlayer.start();
                    button.setText(R.string.pause);
                }
            }
        });

        // seek
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mFilterPlayer == null) return;

                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                mFilterPlayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

        // list
        ListView listView = (ListView) findViewById(R.id.list);
        final List<FilterType> filterTypes = FilterType.createFilterList();
        listView.setAdapter(new FilterAdapter(this, R.layout.row_text, filterTypes));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFilterPlayerView.setGlFilter(FilterType.createGlFilter(filterTypes.get(position), getApplicationContext()));
            }
        });
    }

    private void setUoGlPlayerView() {
        mFilterPlayerView = new EPlayerView(this);
        mFilterPlayerView.setFilterPlayer(mFilterPlayer);
        mFilterPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).addView(mFilterPlayerView);
        mFilterPlayerView.onResume();
    }


    private void setUpTimer() {
        playerTimer = new PlayerTimer();
        playerTimer.setCallback(new PlayerTimer.Callback() {
            @Override
            public void onTick(long timeMillis) {
                long position = mFilterPlayer.getCurrentPosition();
                long duration = mFilterPlayer.getDuration();

                if (duration <= 0) return;

                seekBar.setMax((int) duration / 1000);
                seekBar.setProgress((int) position / 1000);
            }
        });
        playerTimer.start();
    }


    private void releasePlayer() {
        mFilterPlayerView.onPause();
        ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).removeAllViews();
        mFilterPlayerView = null;
        mFilterPlayer.stop();
        mFilterPlayer.release();
        mFilterPlayer = null;
    }


}