package com.yaroslavgorbachh.counter.di;

import android.content.Context;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.counter.CounterCom;
import com.yaroslavgorbachh.counter.component.counter.CounterComImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.feature.ad.AdManager;
import com.yaroslavgorbachh.counter.feature.ad.AdManagerImp;
import com.yaroslavgorbachh.counter.screen.counter.CounterFragment;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@ViewModelScope
@Component(dependencies = {AppComponent.class},
        modules = {CounterComponent.CounterModule.class, CountersCommonModule.class, AdModule.class})
public interface CounterComponent {
    void inject(CounterFragment fragment);

    @Component.Factory
    interface Factory {
        CounterComponent create(
                @BindsInstance long counterId,
                @BindsInstance Context context,
                AppComponent appComponent);
    }

    @Module
    class CounterModule {
        @ViewModelScope
        @Provides
        public CounterCom provideCounter(Repo repo, long counterId, Accessibility accessibility) {
            return new CounterComImp(repo, counterId, accessibility);
        }
    }
}
