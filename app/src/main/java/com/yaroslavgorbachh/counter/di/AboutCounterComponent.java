package com.yaroslavgorbachh.counter.di;
import android.content.Context;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounter;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounterImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.screen.about.AboutCounterFragment;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@ViewModelScope
@Component(dependencies = {AppComponent.class},
        modules = {AboutCounterComponent.AboutCounterModule.class})
public interface AboutCounterComponent {
    void inject(AboutCounterFragment fragment);

    @Component.Factory
    interface Factory {
        AboutCounterComponent create(
                @BindsInstance long counterId,
                AppComponent appComponent);
    }

    @Module
    class AboutCounterModule {
        @ViewModelScope
        @Provides
        public AboutCounter provideAboutCounter(Repo repo, long counterId) {
            return new AboutCounterImp(repo, counterId);
        }
    }
}
