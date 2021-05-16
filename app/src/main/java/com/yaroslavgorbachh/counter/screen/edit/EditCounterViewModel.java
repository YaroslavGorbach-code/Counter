package com.yaroslavgorbachh.counter.screen.edit;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.edit.EditCounter;
import com.yaroslavgorbachh.counter.component.edit.EditCounterImp;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class EditCounterViewModel extends ViewModel {
    private EditCounter editCounter;

    public EditCounter getEditCounter(Repo repo, long id){
        if (editCounter == null){
            editCounter = new EditCounterImp(repo, id);
        }
        return editCounter;
    }

}
