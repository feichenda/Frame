package com.lenovo.frame.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Author: chenhao
 * Date: 2021/12/23-0023 下午 02:12:40
 * Describe:
 */
public class MediaPlayerUtil {
    private static Context mContext;
    private MediaPlayer mMediaPlayer;
    private MediaPlayerListener mMediaPlayerListener;
    private String mFileName;
    private volatile Boolean isPrepared;

    private MediaPlayerUtil() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            isPrepared = false;
            mMediaPlayer.setOnErrorListener((mp, what, extra) -> {
                LogUtil.e("Audio playback error." + what);
                isPrepared = false;
                if (mMediaPlayerListener != null) {
                    mMediaPlayerListener.onError();
                }
                return true;
            });
            mMediaPlayer.setOnPreparedListener(mp -> {
                LogUtil.i("Audio is ready.");
                isPrepared = true;
                if (mMediaPlayerListener != null) {
                    mMediaPlayerListener.onPrepared();
                }
            });
            mMediaPlayer.setOnCompletionListener(mp -> {
                LogUtil.i("Audio has finished playing.");
                isPrepared = false;
                if (mMediaPlayerListener != null) {
                    mMediaPlayerListener.onCompltion();
                }
            });
        }
    }

    public static MediaPlayerUtil getInstance(Context context) {
        if (context != null) {
            mContext = context;
        } else {
            throw new NullPointerException("context is null");
        }
        return MediaPlayerUtilHolder.sInstance;
    }

    private static class MediaPlayerUtilHolder {
        private static final MediaPlayerUtil sInstance = new MediaPlayerUtil();
    }

    public MediaPlayerUtil setDataSource(String fileName) {
        if (mMediaPlayer == null) {
            LogUtil.i("MediaPlayer is null");
        } else {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                if (TextUtils.isEmpty(fileName)) {
                    LogUtil.e("Datasource is null");
                }else {
                    mFileName = fileName;
                    if (fileName.contains("/")) {
                        mMediaPlayer.setDataSource(fileName);
                    } else {
                        AssetFileDescriptor assetFileDescriptor = mContext.getResources().getAssets().openFd(fileName);
                        mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    }
                    mMediaPlayer.prepare();
                }
            } catch (IOException e) {
                LogUtil.e("Resource loading failed.");
                LogUtil.e(e.getMessage());
                e.printStackTrace();
            }
        }
        return this;
    }

    public MediaPlayerUtil play() {
        if (mMediaPlayer == null) {
            LogUtil.i("MediaPlayer is null");
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            if (!isPrepared){
                setDataSource(mFileName);
            }
            mMediaPlayer.start();
        }
        return this;
    }

    public MediaPlayerUtil stop() {
        if (mMediaPlayer == null) {
            LogUtil.i("MediaPlayer is null");
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                isPrepared = false;
            }
        }
        return this;
    }

    public MediaPlayerUtil pause() {
        if (mMediaPlayer == null) {
            LogUtil.i("MediaPlayer is null");
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
        return this;
    }

    public void setLoop(Boolean value) {
        if (mMediaPlayer == null) {
            LogUtil.i("MediaPlayer is null");
        } else {
            mMediaPlayer.setLooping(value);
        }
    }

    public void release() {
        if (mMediaPlayer == null) {
            LogUtil.i("MediaPlayer is null");
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public MediaPlayerUtil setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        if (mediaPlayerListener == null) {
            throw new NullPointerException("MediaPlayerListener is null");
        }
        mMediaPlayerListener = mediaPlayerListener;
        return this;
    }

    public interface MediaPlayerListener {
        void onCompltion();

        default void onPrepared() {
        }

        void onError();
    }

}
