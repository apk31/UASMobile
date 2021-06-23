package com.example.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{
    private Thread mGameThread;
    private volatile boolean mIsPlaying;
    private Pemain mPlayer;
    private Paint mPaint;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private ArrayList<Laser> mLasers;
    private ArrayList<Meteor> mMeteors;
    private ArrayList<Lawan> mEnemies;
    private ArrayList<Star> mStars;
    private int layarX, layarY;
    private int mCounter = 0;
    private Plays player;
    private SharedPreferencesManager mSP;
    public static int NILAI = 0;
    public static int METEOR_BYE = 0;
    public static int LAWAN_BYE = 0;
    private volatile boolean mIsGameOver;
    private volatile boolean mNewHighScore;

    public GameView(Context context, int lsizeX, int lsizeY) {
        super(context);

        layarX = lsizeX;
        layarY = lsizeY;
        mSP = new SharedPreferencesManager(context);

        player = new Plays(context);
        mPaint = new Paint();
        mSurfaceHolder = getHolder();

        reset();
    }

    void reset() {
        NILAI = 0;
        mPlayer = new Pemain(getContext(), layarX, layarY, player);
        mLasers = new ArrayList<>();
        mMeteors = new ArrayList<>();
        mEnemies = new ArrayList<>();
        mStars = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mStars.add(new Star(getContext(), layarX, layarY, true));
        }
        mIsGameOver = false;
        mNewHighScore = false;
    }

    @Override
    public void run() {
        while (mIsPlaying) {
            if (!mIsGameOver) {
                update();
                draw();
                control();
            }
        }
        Log.d("GameThread", "Run stopped");
    }

    public void update() {
        mPlayer.update();
        if (mCounter % 200 == 0) {
            mPlayer.tembak();
        }

        for (Meteor m : mMeteors) {
            m.update();

            if (Rect.intersects(m.getTabrak(), mPlayer.getTabrak())) {
                m.destroy();
                mIsGameOver = true;
                if (NILAI >mSP.getHighScore()){
                    mNewHighScore = true;
                    mSP.saveHighScore(NILAI, METEOR_BYE, LAWAN_BYE);
                }
            }

            for (Laser l : mPlayer.getLasers()) {
                if (Rect.intersects(m.getTabrak(), l.getTabrak())) {
                    m.hit();
                    l.destroy();
                }
            }
        }

        boolean deleting = true;
        while (deleting) {
            if (mMeteors.size() != 0) {
                if (mMeteors.get(0).getY() > layarY) {
                    mMeteors.remove(0);
                }
            }

            if (mMeteors.size() == 0 || mMeteors.get(0).getY() <= layarY) {
                deleting = false;
            }
        }
        if (mCounter % 1000 == 0) {
            mMeteors.add(new Meteor(getContext(), layarX, layarY, player));
        }

        for (Lawan e : mEnemies) {
            e.update();
            if (Rect.intersects(e.getTabrak(), mPlayer.getTabrak())) {
                e.destroy();
                mIsGameOver = true;
                if (NILAI >=mSP.getHighScore()){
                    mSP.saveHighScore(NILAI, METEOR_BYE, LAWAN_BYE);
                }
            }

            for (Laser l : mPlayer.getLasers()) {
                if (Rect.intersects(e.getTabrak(), l.getTabrak())) {
                    e.hit();
                    l.destroy();
                }
            }
        }
        deleting = true;
        while (deleting) {
            if (mEnemies.size() != 0) {
                if (mEnemies.get(0).getY() > layarY) {
                    mEnemies.remove(0);
                }
            }

            if (mEnemies.size() == 0 || mEnemies.get(0).getY() <= layarY) {
                deleting = false;
            }
        }
        if (mCounter % 2000 == 0) {
            mEnemies.add(new Lawan(getContext(), layarX, layarY, player));
        }

        for (Star s : mStars) {
            s.update();
        }
        deleting = true;
        while (deleting) {
            if (mStars.size() != 0) {
                if (mStars.get(0).getY() > layarY) {
                    mStars.remove(0);
                }
            }

            if (mStars.size() == 0 || mStars.get(0).getY() <= layarY) {
                deleting = false;
            }
        }

        if (mCounter % 250 == 0) {
            Random random = new Random();
            for (int i = 0; i < random.nextInt(3) + 1; i++) {
                mStars.add(new Star(getContext(), layarX, layarY, false));
            }

        }


    }

    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.BLACK);
            mCanvas.drawBitmap(mPlayer.getBitmap(), mPlayer.getX(), mPlayer.getY(), mPaint);
            for (Star s : mStars) {
                mCanvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(), mPaint);
            }
            for (Laser l : mPlayer.getLasers()) {
                mCanvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), mPaint);
            }
            for (Meteor m : mMeteors) {
                mCanvas.drawBitmap(m.getBitmap(), m.getX(), m.getY(), mPaint);
            }
            for (Lawan e : mEnemies) {
                mCanvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), mPaint);
            }
            drawScore();
            if (mIsGameOver) {
                drawGameOver();
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    void drawScore() {
        Paint score = new Paint();
        score.setTextSize(30);
        score.setColor(Color.WHITE);
        mCanvas.drawText("Score : " + NILAI, 100, 50, score);
    }

    void drawGameOver() {
        Paint gameOver = new Paint();
        gameOver.setTextSize(100);
        gameOver.setTextAlign(Paint.Align.CENTER);
        gameOver.setColor(Color.WHITE);
        mCanvas.drawText("GAME OVER", layarX / 2, layarY / 2, gameOver);
        Paint highScore = new Paint();
        highScore.setTextSize(50);
        highScore.setTextAlign(Paint.Align.CENTER);
        highScore.setColor(Color.WHITE);
        if (mNewHighScore){
            mCanvas.drawText("New High Score : " + mSP.getHighScore(), layarX / 2, (layarY / 2) + 60, highScore);
            Paint enemyDestroyed = new Paint();
            enemyDestroyed.setTextSize(50);
            enemyDestroyed.setTextAlign(Paint.Align.CENTER);
            enemyDestroyed.setColor(Color.WHITE);
            mCanvas.drawText("Enemy Destroyed : " + mSP.getEnemyDestroyed(), layarX / 2, (layarY / 2) + 120, enemyDestroyed);
            Paint meteorDestroyed = new Paint();
            meteorDestroyed.setTextSize(50);
            meteorDestroyed.setTextAlign(Paint.Align.CENTER);
            meteorDestroyed.setColor(Color.WHITE);
            mCanvas.drawText("Meteor Destroyed : " + mSP.getMeteorDestroyed(), layarX / 2, (layarY / 2) + 180, meteorDestroyed);
        }

    }

    public void steerLeft(float speed) {
        mPlayer.belokKiri(speed);
    }

    public void steerRight(float speed) {
        mPlayer.belokKanan(speed);
    }

    public void stay() {
        mPlayer.stay();
    }

    public void control() {
        try {
            if (mCounter == 10000) {
                mCounter = 0;
            }
            mGameThread.sleep(20);
            mCounter += 20;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        Log.d("GameThread", "Main");
        mIsPlaying = false;
        try {
            mGameThread.join();
            player.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        mIsPlaying = true;
        player.resume();
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mIsGameOver){
                    ((Activity) getContext()).finish();
                    getContext().startActivity(new Intent(getContext(), MainMenuActivity.class));
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
