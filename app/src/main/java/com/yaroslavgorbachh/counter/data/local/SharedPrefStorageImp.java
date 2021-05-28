package com.yaroslavgorbachh.counter.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.yaroslavgorbachh.counter.feature.ad.AdManager;

import java.util.Date;

public class SharedPrefStorageImp implements SharedPrefStorage {
    private final SharedPreferences mSharedPreferences;

    public SharedPrefStorageImp(Context context){
        mSharedPreferences = context.getSharedPreferences("com.YaroslavGorbach.delusionalgenerator", Context.MODE_PRIVATE);
    }

    @Override
    public void setInterstitialAdCount(int count) {
        mSharedPreferences.edit().putInt("interstitialAdCount", count).apply();
    }

    @Override
    public int getInterstitialAdCount() {
        return mSharedPreferences.getInt("interstitialAdCount", 0);
    }

    @Override
    public void setNightMod(boolean nightMod) {
        mSharedPreferences.edit().putBoolean("nightMod", nightMod).apply();
    }

    @Override
    public boolean getNightMod() {
        return mSharedPreferences.getBoolean("nightMod", false);
    }

    @Override
    public boolean getAdIsAllow() {
        return mSharedPreferences.getBoolean("adIsAllow", true);
    }

    @Override
    public void setAdIsAllow(boolean isAllow) {
        mSharedPreferences.edit().putBoolean("adIsAllow", isAllow).apply();
    }

    @Override
    public void setTimeLastReviewAsc(long time) {
        mSharedPreferences.edit().putLong("timeLastReviewAsc", time).apply();
    }

    @Override
    public long getTimeLastReviewAsc() {
        return mSharedPreferences.getLong("timeLastReviewAsc", new Date().getTime());
    }

}
