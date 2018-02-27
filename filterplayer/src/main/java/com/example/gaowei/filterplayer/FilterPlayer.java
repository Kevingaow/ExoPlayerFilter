package com.example.gaowei.filterplayer;

import android.content.Context;
import android.view.Surface;

/**
 * Created by gaowei on 2/23/18.
 */

public interface FilterPlayer {
    void initialize(Context context, String filePath);
    long getCurrentPosition();
    long getDuration();
    void start();
    void pause();
    void seekTo(long positionMs);
    void stop();
    void release();
    void setVideoSurface(Surface surface);
    void registerSurfaceChangeListener(FilterPlayerSurfaceChangeListener l);
}
