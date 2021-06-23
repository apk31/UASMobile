package com.example.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import static com.example.spaceshooter.GameView.LAWAN_BYE;
import static com.example.spaceshooter.GameView.NILAI;

public class Lawan {
    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private final int layarX;
    private final int layarY;
    private int nyawa;
    private final int mSpeed;
    private final Rect mTabrak;
    private final int[] mLawan;
    private final int mMaxX;
    private final int mMaxY;
    private boolean mKanan;
    private final Plays mPlays;

    public Lawan(Context context, int lSizeX, int lsizeY, Plays playSound) {
        layarX = lSizeX;
        layarY = lsizeY;
        mPlays = playSound;

        nyawa = 12;

        mLawan = new int[]{R.drawable.enemy_red_1_01, R.drawable.enemy_red_2_01, R.drawable.enemy_red_3_01};
        Random random = new Random();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), mLawan[random.nextInt(3)]);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() * 3/5, mBitmap.getHeight() * 3/5, false);
        mSpeed = random.nextInt(3) + 1;

        mMaxX = lSizeX - mBitmap.getWidth();
        mMaxY = lsizeY - mBitmap.getHeight();
        mX = random.nextInt(mMaxX);
        mY = -mBitmap.getHeight();

        mKanan = mX < mMaxX;
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

    public void hit(){
        if (--nyawa ==0){
            NILAI += 50;
            LAWAN_BYE++;
            destroy(); }
        else{ mPlays.playSFX(); }
    }
    public void update(){
        mY += 7 * mSpeed;

        if (mX<=0){ mKanan = true; }
        else if (mX>= layarX -mBitmap.getWidth()){ mKanan = false; }

        if (mKanan){ mX += 7 * mSpeed; }
        else{ mX -= 7 * mSpeed; }

        mTabrak.left = mX;
        mTabrak.top = mY;
        mTabrak.right = mX + mBitmap.getWidth();
        mTabrak.bottom = mY + mBitmap.getHeight();
    }
    public void destroy(){
        mY = layarY + 1;
        mPlays.playTabrak();
    }
}
