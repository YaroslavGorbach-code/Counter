package com.yaroslavgorbachh.counter.di;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {

    @Provides
    public SharedPreferences provideDefaultSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
