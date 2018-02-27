package com.example.gaowei.filterplayer;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer.VideoListener;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by gaowei on 2/23/18.
 */

public class ExoPlayerAdapter implements FilterPlayer {

    SimpleExoPlayer mPlayer;
    FilterPlayerSurfaceChangeListener mSurfaceChangeListener;

    SimpleExoPlayer.VideoListener mPlayerListener = new VideoListener() {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            if (mSurfaceChangeListener == null) {
                return;
            }
            mSurfaceChangeListener.onSurfaceSizeChanged(
                    width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }

        @Override
        public void onRenderedFirstFrame() {
            if (mSurfaceChangeListener == null) {
                return;
            }
            mSurfaceChangeListener.onRenderedFirstFrame();
        }
    };

    @Override
    public void initialize(Context context, String filePath) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "yourApplicationName"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(filePath), dataSourceFactory, extractorsFactory, null, null);

        // SimpleExoPlayer
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        mPlayer.addVideoListener(mPlayerListener);
        // Prepare the player with the source.
        mPlayer.prepare(videoSource);
        mPlayer.setPlayWhenReady(true);
    }

    @Override
    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public void start() {
        mPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        mPlayer.setPlayWhenReady(false);
    }

    @Override
    public void seekTo(long positionMs) {
        mPlayer.seekTo(positionMs);
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void release() {
        mPlayer.release();
    }

    @Override
    public void setVideoSurface(Surface surface) {
        mPlayer.setVideoSurface(surface);
    }

    @Override
    public void registerSurfaceChangeListener(FilterPlayerSurfaceChangeListener l) {
        mSurfaceChangeListener = l;
        mPlayer.addVideoListener(mPlayerListener);
    }
}
