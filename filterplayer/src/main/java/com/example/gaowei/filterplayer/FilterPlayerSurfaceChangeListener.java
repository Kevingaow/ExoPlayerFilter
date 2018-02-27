package com.example.gaowei.filterplayer;

/**
 * Created by gaowei on 2/23/18.
 */

public interface FilterPlayerSurfaceChangeListener {
    void onSurfaceSizeChanged(int width, int height, int unappliedRotationDegrees,
                              float pixelWidthHeightRatio);
    void onRenderedFirstFrame();
}
