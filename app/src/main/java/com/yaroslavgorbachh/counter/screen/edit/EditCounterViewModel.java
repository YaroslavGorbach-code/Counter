package com.yaroslavgorbachh.counter.screen.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.edit.Edit;
import com.yaroslavgorbachh.counter.component.edit.EditImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.DaggerEditCounterComponent;
import com.yaroslavgorbachh.counter.di.EditCounterComponent;

public class EditCounterViewModel extends AndroidViewModel {
    private EditCounterComponent editCounterComponent = null;

    public EditCounterViewModel(@NonNull Application application) {
        super(application);
    }

    public EditCounterComponent getEditCounterComponent(long id){
        if (editCounterComponent == null){
            editCounterComponent = DaggerEditCounterComponent.factory().create(id, ((App)getApplication()).appComponent);
        }
        return editCounterComponent;
    }

}
