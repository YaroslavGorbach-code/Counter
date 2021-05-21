package com.yaroslavgorbachh.counter.screen.edit;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.edit.EditComponent;
import com.yaroslavgorbachh.counter.component.edit.EditComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class EditCounterViewModel extends ViewModel {
    private EditComponent editComponent;

    public EditComponent getEditCounter(Repo repo, long id){
        if (editComponent == null){
            editComponent = new EditComponentImp(repo, id);
        }
        return editComponent;
    }

}
