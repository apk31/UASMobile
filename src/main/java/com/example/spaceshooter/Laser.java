package com.example.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Laser {
    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private Rect mTabrak;
    private int layarX;
    private int layarY;
    private boolean mLawan;

    public Laser(Context context, int lSizeX, int lSizeY, int spaceShipX, int spaceShipY, Bitmap spaceShip, boolean lawan){
        layarX = lSizeX;
        layarY = lSizeY;
        mLawan = lawan;

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser_1_01);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 3/5, mBitmap.getHeight() * 3/5, false);

        mX = spaceShipX + spaceShip.getWidth()/2 - mBitmap.getWidth()/2;

        if (mLawan){ mY = spaceShipY + mBitmap.getHeight() + 10; }
        else{ mY = spaceShipY - mBitmap.getHeight() - 10; }

        mTabrak = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());
    }

    public int getX() {
        return mX;
    }
    public int getY() {
        return mY;
    }
    public Rect getTabrak() {
        return mTabrak;
    }
    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void update(){
        if (mLawan){
            mY += mBitmap.getHeight() + 10;

        }else{
            mY -= mBitmap.getHeight() - 10;
        }
        mTabrak.left = mX;
        mTabrak.top = mY;
        mTabrak.right = mX + mBitmap.getWidth();
        mTabrak.bottom = mY + mBitmap.getHeight();
    }
    public void destroy(){
        if (mLawan){
            mY = layarY;
        }else{
            mY = -mBitmap.getHeight();
        }

    }
}
