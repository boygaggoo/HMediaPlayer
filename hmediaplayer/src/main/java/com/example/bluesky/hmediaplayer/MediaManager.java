package com.example.bluesky.hmediaplayer;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

/**
 * Created by blue_sky on 2017/12/18.
 * @author blue_sky
 */

public class MediaManager implements TextureView.SurfaceTextureListener {
    public static final String TAG = "HVideoPlayer";
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;

    public static ResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;
    public static MediaManager jzMediaManager;
    public int positionInList = -1;
    public BaseMediaInterface hMediaInterface;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    public HandlerThread mMediaHandlerThread;
    public MediaHandler mMediaHandler;
    public Handler mainThreadHandler;

    public MediaManager() {
        mMediaHandlerThread = new HandlerThread(TAG);
        mMediaHandlerThread.start();
        mMediaHandler = new MediaHandler(mMediaHandlerThread.getLooper());
        mainThreadHandler = new Handler();
        if (hMediaInterface == null) {
            hMediaInterface = new MediaSystem();
        }

    }

    public static MediaManager instance() {
        if (jzMediaManager == null) {
            jzMediaManager = new MediaManager();
        }
        return jzMediaManager;
    }

    public static Object[] getDataSource() {
        return instance().hMediaInterface.dataSourceObjects;
    }

    /**
     * 这几个方法是不是多余了，为了不让其他地方动MediaInterface的方法
     */
    public static void setDataSource(Object[] dataSourceObjects) {
        instance().hMediaInterface.dataSourceObjects = dataSourceObjects;
    }

    /**
     * 正在播放的url或者uri
     */
    public static Object getCurrentDataSource() {
        return instance().hMediaInterface.currentDataSource;
    }

    public static void setCurrentDataSource(Object currentDataSource) {
        instance().hMediaInterface.currentDataSource = currentDataSource;
    }

    public static long getCurrentPosition() {
        return instance().hMediaInterface.getCurrentPosition();
    }

    public static long getDuration() {
        return instance().hMediaInterface.getDuration();
    }

    public static void seekTo(long time) {
        instance().hMediaInterface.seekTo(time);
    }

    public static void pause() {
        instance().hMediaInterface.pause();
    }

    public static void start() {
        instance().hMediaInterface.start();
    }

    public static boolean isPlaying() {
        return instance().hMediaInterface.isPlaying();
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
    }

    public void prepare() {
        releaseMediaPlayer();
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        mMediaHandler.sendMessage(msg);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + VideoPlayerManager.getCurrentJzvd().hashCode() + "] ");
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
        } else {
            textureView.setSurfaceTexture(savedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    currentVideoWidth = 0;
                    currentVideoHeight = 0;
                    hMediaInterface.prepare();
                    if (surface != null) {
                        surface.release();
                    }
                    surface = new Surface(savedSurfaceTexture);
                    hMediaInterface.setSurface(surface);
                    break;
                case HANDLER_RELEASE:
                    hMediaInterface.release();
                    break;
                default:
                    break;
            }
        }
    }
}
