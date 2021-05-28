package com.yaroslavgorbachh.counter.data.local;

public interface SharedPrefStorage {
    void setInterstitialAdCount(int count);
    int getInterstitialAdCount();
    void setNightMod(boolean nightMod);
    boolean getNightMod();
    boolean getAdIsAllow();
    void setAdIsAllow(boolean isAdRemoved);
    void setTimeLastReviewAsc(long time);
    long getTimeLastReviewAsc();
    boolean getFirstOpen();
    void setFirstOpen(boolean firstOpen);
}
