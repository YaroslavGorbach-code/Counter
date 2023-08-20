package com.yaroslavgorbachh.counter.feature.billing;

import static com.android.billingclient.api.BillingClient.ProductType.INAPP;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetailsParams;

import java.util.ArrayList;
import java.util.List;

public class BillingManagerImp implements BillingManager {
    private BillingClient mBillingClient;

    public BillingManagerImp(Activity activity) {
        initBillingClient(activity);
    }

    private void initBillingClient(Activity activity) {
        mBillingClient = BillingClient.newBuilder(activity)
                .setListener((billingResult, purchases) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                        for (Purchase purchase : purchases) {
                            handlePurchase(purchase);
                        }
                    }
                }).enablePendingPurchases().build();
    }

    @Override
    public void showPurchasesDialog(Activity activity) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    List<String> skuList = new ArrayList<>();
                    skuList.add(BillingManager.SKU_REMOVE_AD);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

                    mBillingClient.querySkuDetailsAsync(params.build(), (billingResult2, skuDetailsList) -> {
                        if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetailsList.get(0))
                                    .build();
                            int responseCode = mBillingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
                        }
                    });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    @Override
    public void queryPurchases(Callback callback) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                mBillingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(INAPP).build(), (billingResult1, list) -> callback.onAdRemoved(!list.isEmpty()));


                // FOR TEST ONLY
                //mBillingClient.consumeAsync( ConsumeParams.newBuilder().setPurchaseToken(purchases.get(0).getPurchaseToken()).build(), (billingResult1, s) -> {});
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    @Override
    public void endConnection() {
        mBillingClient.endConnection();
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                });
            }
        }
    }
}
