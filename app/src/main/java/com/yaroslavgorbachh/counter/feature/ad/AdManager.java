package com.yaroslavgorbachh.counter.feature.ad;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

public interface AdManager {
    int INTERSTITIAL_SHOW_LIMIT = 4;
    void showBanner(Context context, ViewGroup adContainer);
    void showInterstitialAd(Activity activity);
    void loadInterstitialAd(Context context);
}
