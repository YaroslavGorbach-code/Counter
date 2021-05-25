package com.yaroslavgorbachh.counter.di;
import com.yaroslavgorbachh.counter.component.history.HistoryCom;
import com.yaroslavgorbachh.counter.component.history.HistoryComImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.screen.history.HistoryFragment;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@ViewModelScope
@Component(dependencies = {AppComponent.class},
        modules = {HistoryComponent.HistoryModule.class})
public interface HistoryComponent {
    void inject(HistoryFragment fragment);

    @Component.Factory
    interface Factory {
        HistoryComponent create(@BindsInstance long counterId, AppComponent appComponent);
    }

    @Module
    class HistoryModule {
        @ViewModelScope
        @Provides
        HistoryCom provideHistory(Repo repo, long counterId){
            return new HistoryComImp(repo, counterId);
        }
    }
}
