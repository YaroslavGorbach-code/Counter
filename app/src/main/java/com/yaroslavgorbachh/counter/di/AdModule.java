package com.yaroslavgorbachh.counter.di;

import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.ad.AdManager;
import com.yaroslavgorbachh.counter.feature.ad.AdManagerImp;

import dagger.Module;
import dagger.Provides;

@Module
public class AdModule {

    @ViewModelScope
    @Provides
    public AdManager provideAdManager(Repo repo) {
        return new AdManagerImp(repo);
    }
}
