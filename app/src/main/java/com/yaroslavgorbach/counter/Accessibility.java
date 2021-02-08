package com.yaroslavgorbach.counter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.preference.PreferenceManager;

public class Accessibility {

    private final SoundPool mSoundPool;
    private final int mSoundIncId;
    private final int mSoundDecId;
    private boolean mVibrationIsAllowed;


    public Accessibility(Context context){
       AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();
       mSoundDecId = mSoundPool.load(context, R.raw.dec_click_sound, 1);
       mSoundIncId = mSoundPool.load(context, R.raw.inc_click_sound, 1);

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mVibrationIsAllowed = mSharedPreferences.getBoolean("clickVibration", false);

   }

    public void playIncSoundEffect(){
        mSoundPool.play(mSoundIncId, 1, 1, 1, 0, 1f);
    }

    public void playDecSoundEffect(){
        mSoundPool.play(mSoundDecId, 1, 1, 1, 0, 1f);
    }

    public void playIncVibrationEffect(View view){
        if (mVibrationIsAllowed)
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }

    public void playDecVibrationEffect(View view){
        if (mVibrationIsAllowed)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
    }
}
