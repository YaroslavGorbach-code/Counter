package com.yaroslavgorbachh.counter.screen.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.di.DaggerEditComponent;
import com.yaroslavgorbachh.counter.di.EditComponent;

public class EditCounterViewModel extends AndroidViewModel {
    private EditComponent editComponent = null;

    public EditCounterViewModel(@NonNull Application application) {
        super(application);
    }

    public EditComponent getEditCounterComponent(long id){
        if (editComponent == null){
            editComponent = DaggerEditComponent.factory().create(id, ((App)getApplication()).appComponent);
        }
        return editComponent;
    }

}
