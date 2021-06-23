package com.example.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import static com.example.spaceshooter.GameView.METEOR_BYE;
import static com.example.spaceshooter.GameView.NILAI;

public class Meteor {
    private Bitmap mBitmap;
    private final int mX;
    private int mY;
    private final int mMaxX;
    private final int mMinX;
    private final int mMaxY;
    private final int mMinY;

    private final int mSpeed;
    private final Rect mTabrak;
    private final int layarX;
    private final int layarY;
    private int nyawa;
    private final Plays mPlays;
    private final int[] mMeteor;

    public Meteor(Context context, int lSizeX, int lSizeY, Plays playSound) {
        layarX = lSizeX;
        layarY = lSizeY;
        mPlays = playSound;
        mMeteor= new int[]{R.drawable.meteor_1_01, R.drawable.meteor_2_01, R.drawable.meteor_3_01,R.drawable.meteor_4_01};
        Random random = new Random();

        mBitmap = BitmapFactory.decodeResource(context.getResources(), mMeteor[random.nextInt(4)]);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 3 / 5, mBitmap.getHeight() * 3 / 5, false);

        mMaxX = lSizeX - mBitmap.getWidth();
        mMaxY = lSizeY - mBitmap.getHeight();
        nyawa = 7;
        mMinX = 0;
        mMinY = 0;

        random = new Random();
        mSpeed = random.nextInt(3) + 1;

        mX = random.nextInt(mMaxX);
        mY = -mBitmap.getHeight();

        mTabrak = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());
    }
    public int getX() {
        return mX;
    }
    public int getY() {
        return mY;
    }
    public Bitmap getBitmap() {
        return mBitmap;
    }
    public Rect getTabrak() {
        return mTabrak;
    }

    public void update(){
        mY += 7 * mSpeed;

        mTabrak.left = mX;
        mTabrak.top = mY;
        mTabrak.right = mX + mBitmap.getWidth();
        mTabrak.bottom = mY + mBitmap.getHeight();
    }
    public void hit(){
        if (--nyawa ==0){
            NILAI += 20;
            METEOR_BYE++;
            destroy(); }
        else{
            mPlays.playSFX(); }
    }
    public void destroy(){
        mY = layarY + 1;
        mPlays.playTabrak();
    }
}
