package com.yaroslavgorbachh.counter.di;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;

import dagger.Module;
import dagger.Provides;

@Module
public class AudioModule {

    @Provides
    public AudioManager provideAudioManager(Context context){
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Provides
    public Resources provideResources(Context context){
       return context.getResources();
    }
}
