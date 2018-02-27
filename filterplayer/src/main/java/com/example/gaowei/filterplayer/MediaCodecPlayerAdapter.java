package com.example.gaowei.filterplayer;

import android.content.Context;
import android.view.Surface;

import com.example.gaowei.mediacodecplayer.MediaCodecPlayer;

/**
 * Created by gaowei on 2/23/18.
 */

public class MediaCodecPlayerAdapter implements FilterPlayer {

    MediaCodecPlayer mPlayer;

    @Override
    public void initialize(Context context, String filePath) {

    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seekTo(long positionMs) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void release() {

    }

    @Override
    public void setVideoSurface(Surface surface) {

    }

    @Override
    public void registerSurfaceChangeListener(FilterPlayerSurfaceChangeListener l) {

    }
}
