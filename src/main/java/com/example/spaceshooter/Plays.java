package com.example.spaceshooter;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

public class Plays implements Runnable{
    private Thread mSoundThread;
    private volatile boolean mIsPlaying;
    private final SoundPool pool;
    private final int mByeId;
    private final int mLaserId;
    private final int mTabrakId;
    private boolean mIsLaserPlaying, mIsExplodePlaying, mIsCrashPlaying;

    public Plays(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            pool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attributes)
                    .build(); }
        else {
            pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1); }
        mByeId = pool.load(context, R.raw.rock_explode_1, 1);
        mLaserId = pool.load(context, R.raw.laser_1, 1);
        mTabrakId = pool.load(context, R.raw.rock_explode_2, 1);
    }

    @Override
    public void run() {
        while (mIsPlaying){
            if (mIsLaserPlaying){
                pool.play(mLaserId, 1, 1, 1, 0, 1f);
                mIsLaserPlaying = false; }
            if (mIsExplodePlaying){
                pool.play(mByeId, 1, 1, 1, 0, 1);
                mIsExplodePlaying = false; }
            if (mIsCrashPlaying){
                pool.play(mTabrakId, 1, 1, 1, 0, 1);
                mIsCrashPlaying = false; }
        }
    }

    public void playTabrak(){
        mIsCrashPlaying = true;
    }
    public void playLaser(){
        mIsLaserPlaying = true;
    }
    public void playSFX(){
        mIsExplodePlaying = true;
    }

    public void resume(){
        mIsPlaying = true;
        mSoundThread = new Thread(this);
        mSoundThread.start();
    }

    public void pause() throws InterruptedException {
        Log.d("GameThread", "Sound");
        mIsPlaying = false;
        mSoundThread.join();
    }
}
