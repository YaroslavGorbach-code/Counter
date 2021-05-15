package com.yaroslavgorbachh.counter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.yaroslavgorbachh.counter.R;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

public class Accessibility {
    private final SoundPool mSoundPool;
    private int mSoundIncId;
    private int mSoundDecId;
    private boolean mVibrationIsAllowed;
    private boolean mClickSoundIsAllowed;
    private boolean mSpeechOutputIsAllowed;
    private TextToSpeech mTextToSpeech;
    private final SharedPreferences mSharedPreferences;
    private final Vibrator vibrator;


    @Inject
    public Accessibility(Context context, SharedPreferences sharedPreferences){
       AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(1)
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

       vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
   }

    public void playIncFeedback(String text){
        checkPref();
        playIncSoundEffect();
        playIncVibrationEffect();
        speechOutput(text);
        Log.v("counter","incFeedback");
    }

    public void playDecFeedback(String text){
        checkPref();
        playDecSoundEffect();
        playDecVibrationEffect();
        speechOutput(text);
    }

    public void speechOutput(String text){
        if (mSpeechOutputIsAllowed)
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ID");
    }


    private void playIncSoundEffect(){
        if (mClickSoundIsAllowed)
        mSoundPool.play(mSoundIncId, 1f, 1f, 1, 0, 1f);
    }

    private void playDecSoundEffect(){
        if (mClickSoundIsAllowed)
        mSoundPool.play(mSoundDecId, 1f, 1f, 1, 0, 1f);

    }

    private void playIncVibrationEffect(){
        if (mVibrationIsAllowed)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }
    }

    private void playDecVibrationEffect(){
        if (mVibrationIsAllowed)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50);
            }
    }

    private void checkPref(){
        mVibrationIsAllowed = mSharedPreferences.getBoolean("clickVibration", false);
        mClickSoundIsAllowed = mSharedPreferences.getBoolean("clickSound", true);
        mSpeechOutputIsAllowed = mSharedPreferences.getBoolean("clickSpeak", false);

    }

}
