package com.yaroslavgorbachh.counter.di;

import android.content.Context;

import com.yaroslavgorbachh.counter.component.edit.Edit;
import com.yaroslavgorbachh.counter.component.edit.EditImp;
import com.yaroslavgorbachh.counter.component.fullscreen.Fullscreen;
import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.screen.fullscreen.FullscreenFragment;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@ViewModelScope
@Component(dependencies = {AppComponent.class},
        modules = {FullscreenComponent.FullscreenModule.class, CountersCommonModule.class})
public interface FullscreenComponent {
    void inject(FullscreenFragment fragment);

    @Component.Factory
    interface Factory {
        FullscreenComponent create(
                @BindsInstance long counterId,
                @BindsInstance Context context,
                AppComponent appComponent);
    }

    @Module
    class FullscreenModule {
        @ViewModelScope
        @Provides
        Fullscreen provideFullscreen(Repo repo, Accessibility accessibility, long counterId){
            return new FullscreenImp(repo, counterId, accessibility);
        }
    }
}
