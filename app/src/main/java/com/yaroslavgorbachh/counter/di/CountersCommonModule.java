package com.yaroslavgorbachh.counter.di;

import android.content.Context;

import com.yaroslavgorbachh.counter.feature.Accessibility;

import dagger.Module;
import dagger.Provides;

@Module
public class CountersCommonModule {

    @ViewModelScope
    @Provides
    public Accessibility provideAccessibility (Context context) {
        return new Accessibility(context);
    }
}
