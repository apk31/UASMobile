package com.example.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

public class Pemain {
    private Bitmap mBitmap;

    private int mX;
    private int mY;
    private int mSpeed;
    private int mMaxX;
    private int mMinX;
    private int mMaxY;
    private int mMinY;
    private int mMargin = 16;
    private boolean mKiri, mKanan;
    private float mSteerSpeed;
    private Rect mTabrak;
    private ArrayList<Laser> mLasers;
    private Plays mPlays;
    private Context mContext;
    private int layarX, layarY;

    public Pemain(Context context, int lSizeX, int lSizeY, Plays playSound) {
        layarX = lSizeX;
        layarY = lSizeY;
        mContext = context;

        mSpeed = 1;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 3/5, mBitmap.getHeight() * 3/5, false);

        mMaxX = lSizeX - mBitmap.getWidth();
        mMaxY = lSizeY - mBitmap.getHeight();
        mMinX = 0;
        mMinY = 0;

        mX = lSizeX/2 - mBitmap.getWidth()/2;
        mY = lSizeY - mBitmap.getHeight() - mMargin;

        mLasers = new ArrayList<>();
        mPlays = playSound;

        mTabrak = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());
    }
    public ArrayList<Laser> getLasers() {
        return mLasers;
    }
    public Rect getTabrak() {
        return mTabrak;
    }
    public Bitmap getBitmap() {
        return mBitmap;
    }
    public int getX() {
        return mX;
    }
    public int getY() {
        return mY;
    }

    public void update(){
        if (mKiri){
            mX -= 10 * mSteerSpeed;
            if (mX<mMinX){
                mX = mMinX; } }
        else if (mKanan){
            mX += 10 * mSteerSpeed;
            if (mX>mMaxX){
                mX = mMaxX; } }

        mTabrak.left = mX;
        mTabrak.top = mY;
        mTabrak.right = mX + mBitmap.getWidth();
        mTabrak.bottom = mY + mBitmap.getHeight();

        for (Laser l : mLasers) { l.update(); }

        boolean deleting = true;
        while (deleting) {
            if (mLasers.size() != 0) {
                if (mLasers.get(0).getY() < 0) {
                    mLasers.remove(0); } }
            if (mLasers.size() == 0 || mLasers.get(0).getY() >= 0) { deleting = false; }
        }
    }
    public void tembak(){
        mLasers.add(new Laser(mContext, layarX, layarY, mX, mY, mBitmap, false));
        mLasers.add(new Laser(mContext, layarX, layarY, mX+50, mY, mBitmap, false));
        mLasers.add(new Laser(mContext, layarX, layarY, mX-50, mY, mBitmap, false));
        mPlays.playLaser();
    }
    public void belokKanan(float speed){
        mKiri = false;
        mKanan = true;
        mSteerSpeed = Math.abs(speed);
    }
    public void belokKiri(float speed){
        mKanan = false;
        mKiri = true;
        mSteerSpeed = Math.abs(speed);
    }
    public void stay(){
        mKiri = false;
        mKanan = false;
        mSteerSpeed = 0;
    }
}
