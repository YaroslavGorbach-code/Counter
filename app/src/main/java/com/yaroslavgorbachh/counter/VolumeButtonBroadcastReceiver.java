package com.yaroslavgorbachh.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VolumeButtonBroadcastReceiver extends BroadcastReceiver {
    public interface VolumeKeyDownResponse{
        void decCounters();
        void incCounters();
        void lowerVolume();
        void raiseVolume();
    }

    public static final String ON_KEY_DOWN_BROADCAST = "ON_KEY_DOWN_BROADCAST";
    public static final String KEYCODE_EXTRA = "KEYCODE_EXTRA";
    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_VOLUME_DOWN = 25;

    public static final Intent INTENT_VOLUME_UP = new Intent(ON_KEY_DOWN_BROADCAST)
            .putExtra(KEYCODE_EXTRA, KEYCODE_VOLUME_UP);

    public static final Intent INTENT_VOLUME_DOWN = new Intent(ON_KEY_DOWN_BROADCAST
    ).putExtra(KEYCODE_EXTRA, KEYCODE_VOLUME_DOWN);

    private boolean mIsSelectionMod = true;
    private final VolumeKeyDownResponse mVolumeKeyDownResponse;

    public void setSelectionMod(boolean selectionMod){
        mIsSelectionMod = selectionMod;
    }

    public VolumeButtonBroadcastReceiver(VolumeKeyDownResponse volumeKeyDownResponse){
        mVolumeKeyDownResponse = volumeKeyDownResponse;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get extra data included in the Intent
        switch (intent.getIntExtra(KEYCODE_EXTRA,-1)){
            case KEYCODE_VOLUME_DOWN:
                if (mIsSelectionMod){
                    mVolumeKeyDownResponse.decCounters();
                }else {
                   mVolumeKeyDownResponse.lowerVolume();
                }
                break;
            case KEYCODE_VOLUME_UP:
                if (mIsSelectionMod){
                    mVolumeKeyDownResponse.incCounters();
                }else {
                    mVolumeKeyDownResponse.raiseVolume();
                }
                break;
        }
    }
}
