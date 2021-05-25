package com.yaroslavgorbachh.counter.di;

import com.yaroslavgorbachh.counter.component.edit.Edit;
import com.yaroslavgorbachh.counter.component.edit.EditImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.screen.edit.EditCounterFragment;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@ViewModelScope
@Component(dependencies = {AppComponent.class},
        modules = {EditCounterComponent.EditModule.class})
public interface EditCounterComponent {
    void inject(EditCounterFragment fragment);

    @Component.Factory
    interface Factory {
        EditCounterComponent create(@BindsInstance long counterId, AppComponent appComponent);
    }

    @Module
    class EditModule {
        @ViewModelScope
        @Provides
        Edit provideEdit(Repo repo, long counterId){
            return new EditImp(repo, counterId);
        }
    }
}
