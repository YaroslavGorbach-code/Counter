package com.yaroslavgorbachh.counter.feature.ad;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.yaroslavgorbachh.counter.data.Repo;

public class AdManagerImp implements AdManager  {
    private InterstitialAd mInterstitialAd;
    private final Repo mRepo;

    public AdManagerImp(Repo repo){
        mRepo = repo;
    }

    public void showBanner(Context context, ViewGroup adContainer) {
        if (mRepo.getAdIsAllow()){
            AdView adView = new AdView(context);
            adView.setAdUnitId("ca-app-pub-6043694180023070/5706428455");
            adView.setAdSize(new AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT));
            adContainer.removeAllViews();
            adContainer.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }


    @Override
    public void showInterstitialAd(Activity activity) {
        if (mRepo.getAdIsAllow()){
            if (mInterstitialAd != null && mRepo.getInterstitialAdIsAllow()) mInterstitialAd.show(activity);
            mRepo.incInterstitialAdCount();
        }
    }

    @Override
    public void loadInterstitialAd(Context context) {
        if (mRepo.getAdIsAllow()){
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(
                    context,
                    "ca-app-pub-6043694180023070/1739263284",
                    adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            interstitialAd.setFullScreenContentCallback(
                                    new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            mInterstitialAd = null;
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                            mInterstitialAd = null;
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() { }
                                    });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mInterstitialAd = null;
                        }
                    });
        }
    }
}
