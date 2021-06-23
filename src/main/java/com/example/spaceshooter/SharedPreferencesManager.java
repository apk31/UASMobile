package com.example.spaceshooter;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private final String mName = "SpaceShooter";
    private final Context mContext;

    public SharedPreferencesManager(Context context) {
        mContext = context;
    }

    public void saveHighScore(int score, int meteorBye, int lawanBye){
        SharedPreferences sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt("high_score", score);
        e.putInt("meteor", meteorBye);
        e.putInt("lawan", lawanBye);
        e.apply();
    }

    public int getHighScore(){
        SharedPreferences sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        return sp.getInt("high_score", 0);
    }

    public int getMeteorDestroyed(){
        SharedPreferences sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        return sp.getInt("meteor", 0);
    }

    public int getEnemyDestroyed(){
        SharedPreferences sp = mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
        return sp.getInt("lawan", 0);
    }
}
