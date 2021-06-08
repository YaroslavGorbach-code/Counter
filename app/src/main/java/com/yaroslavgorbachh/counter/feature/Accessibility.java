package com.yaroslavgorbachh.counter.feature;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.yaroslavgorbachh.counter.R;

import java.util.Locale;

public class Accessibility {
    private final SoundPool mSoundPool;
    private final Vibrator vibrator;
    private int mSoundIncId;
    private int mSoundDecId;
    private TextToSpeech mTextToSpeech;

    public Accessibility(Context context) {
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
        } catch (Exception e) {
            Log.e(null, "Resources not found");
        }

        mTextToSpeech = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                mTextToSpeech.setLanguage(Locale.getDefault());
            }
        });
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void speechOutput(String text) {
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ID");
    }

    public void playIncSoundEffect() {
        mSoundPool.play(mSoundIncId, 1f, 1f, 1, 0, 1f);
    }

    public void playDecSoundEffect() {
        mSoundPool.play(mSoundDecId, 1f, 1f, 1, 0, 1f);
    }

    public void playIncVibrationEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(100);
        }
    }

    public void playDecVibrationEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(50);
        }
    }
}
