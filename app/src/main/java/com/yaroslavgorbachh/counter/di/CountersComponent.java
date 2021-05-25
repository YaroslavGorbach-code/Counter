package com.yaroslavgorbachh.counter.di;

import android.content.Context;
import android.media.AudioManager;

import com.yaroslavgorbachh.counter.component.counters.Counters;
import com.yaroslavgorbachh.counter.component.counters.CountersImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.screen.counters.CountersFragment;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@ViewModelScope
@Component(dependencies = {AppComponent.class},
        modules = {CountersComponent.CountersModule.class, CountersCommonModule.class})

public interface CountersComponent {
    void inject(CountersFragment fragment);

    @Component.Factory
    interface Factory {
        CountersComponent create(@BindsInstance Context context, AppComponent appComponent);
    }

    @Module
    class CountersModule {
        @ViewModelScope
        @Provides
        public Counters provideCounters(Repo repo, Accessibility accessibility, AudioManager audioManager) {
            return new CountersImp(repo, accessibility, audioManager);
        }

        @ViewModelScope
        @Provides
        public AudioManager provideAudioManager (Context context) {
            return  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }
}
