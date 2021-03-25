package com.yaroslavgorbachh.counter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.yaroslavgorbachh.counter.R;

import java.util.Locale;

import javax.inject.Inject;

public class Accessibility {

    private final SoundPool mSoundPool;
    private int mSoundIncId;
    private int mSoundDecId;
    private boolean mVibrationIsAllowed;
    private boolean mClickSoundIsAllowed;
    private boolean mSpeechOutputIsAllowed;
    private TextToSpeech mTextToSpeech;
    private SharedPreferences mSharedPreferences;

    @Inject
    public Accessibility(Context context, SharedPreferences sharedPreferences){
       AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();
            try {
                mSoundDecId = mSoundPool.load(context, R.raw.dec_click_sound, 1);
                mSoundIncId = mSoundPool.load(context, R.raw.inc_click_sound, 1);
            }catch (Exception e){
                Log.e(null, "Resources not found");
            }

        mTextToSpeech = new TextToSpeech(context, status -> {
            if(status != TextToSpeech.ERROR) {
                mTextToSpeech.setLanguage(Locale.getDefault());
            }
        });
            mSharedPreferences = sharedPreferences;
   }

    public void playIncFeedback(View view, String text){
        checkPref();
        playIncSoundEffect();
        playIncVibrationEffect(view);
        speechOutput(text);
    }

    public void playDecFeedback(View view, String text){
        checkPref();
        playDecSoundEffect();
        playDecVibrationEffect(view);
        speechOutput(text);
    }

    public void speechOutput(String text){
        if (mSpeechOutputIsAllowed)
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ID");
    }


    private void playIncSoundEffect(){
        if (mClickSoundIsAllowed)
        mSoundPool.play(mSoundIncId, 1, 1, 1, 0, 1f);
    }

    private void playDecSoundEffect(){
        if (mClickSoundIsAllowed)
        mSoundPool.play(mSoundDecId, 1, 1, 1, 0, 1f);

    }

    private void playIncVibrationEffect(View view){
        if (mVibrationIsAllowed)
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }

    private void playDecVibrationEffect(View view){
        if (mVibrationIsAllowed)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
    }

    private void checkPref(){
        mVibrationIsAllowed = mSharedPreferences.getBoolean("clickVibration", false);
        mClickSoundIsAllowed = mSharedPreferences.getBoolean("clickSound", true);
        mSpeechOutputIsAllowed = mSharedPreferences.getBoolean("clickSpeak", false);

    }


}
