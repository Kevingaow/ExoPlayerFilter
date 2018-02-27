package com.example.gaowei.mediacodecplayer;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Surface;


import java.io.IOException;

/**
 * Created by gaowei on 2/23/18.
 */

public class MediaCodecPlayer {
    MediaCodec mCodec;
    String mFilePath;
    int mWidth;
    int mHeight;
    MediaFormat mVideoMediaFormat;
    MediaExtractor mExtractor;
    Surface mSurface;

    public MediaCodecPlayer() {

    }

    public void initialize(String filePath){
        mFilePath = filePath;
    }


    private void parseFile() throws IOException {
        mExtractor = new MediaExtractor();
        mExtractor.setDataSource(mFilePath);
        int countTrack = mExtractor.getTrackCount();
        MediaFormat format;
        String mime;
        for (int i = 0; i < countTrack; i++) {
            format = mExtractor.getTrackFormat(i);
            mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video")) {
                mWidth = format.getInteger(MediaFormat.KEY_WIDTH);
                mHeight = format.getInteger(MediaFormat.KEY_HEIGHT);
                mExtractor.selectTrack(i);
                mVideoMediaFormat = format;
            }
        }
    }

    public void setSurface(Surface surface) {
        mSurface = surface;
    }
}
