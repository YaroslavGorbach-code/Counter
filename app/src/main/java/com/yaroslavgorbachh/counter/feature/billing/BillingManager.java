package com.yaroslavgorbachh.counter.feature.billing;

import android.app.Activity;

public interface BillingManager {
    String SKU_REMOVE_AD = "remove_ad";
    interface Callback{ void onAdRemoved(boolean isAdRemoved);}
    void showPurchasesDialog(Activity Activity);
    void queryPurchases(Callback callback);
    void endConnection();
}
