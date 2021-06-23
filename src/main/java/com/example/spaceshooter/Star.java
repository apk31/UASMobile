package com.example.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Star {
    private Bitmap mBitmap;
    private final int mX;
    private int mY;
    private final int mMaxX;
    private final int mSpeed;
    private final int mScreenSizeY;

    public Star(Context context, int screenSizeX, int screenSizeY, boolean randomY){
        mScreenSizeY = screenSizeY;

        Random random = new Random();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_1_01);
        float scale = (float)(random.nextInt(3) + 1)/5;
        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth() * scale), (int)(mBitmap.getHeight() * scale), false);

        mMaxX = screenSizeX - mBitmap.getWidth();

        mSpeed = random.nextInt(1) + 1;

        mX = random.nextInt(mMaxX);
        if (randomY){
            mY = random.nextInt(mScreenSizeY); }
        else{
            mY = -mBitmap.getHeight(); }
    }
    public Bitmap getBitmap() { return mBitmap; }
    public int getX() { return mX; }
    public int getY() { return mY; }
    public void update(){
        mY += 7 * mSpeed;
    }
}
