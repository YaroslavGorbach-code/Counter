package com.yaroslavgorbachh.counter.counterSettings;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.CopyBeforeReset;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.database.CounterDatabase;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingsViewModel extends ViewModel {
    private final CounterDatabase mDatabase;
    private final Repo mRepo;

    @Inject
    SettingsViewModel(CounterDatabase counterDatabase, Repo repo){
        mDatabase = counterDatabase;
        mRepo = repo;
    }

    public void backup(Intent data, Context context) {
        new MyBackup.Init()
                        .database(mDatabase)
                .setContext(context)
                .uri(data.getData())
                .OnCompleteListener((success, message) -> {
                    if (success) {
                        Toast.makeText(context, context.getResources().getString(R.string.successCreatedToast,
                                data.getData().getPath()), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,
                                context.getResources().getString(R.string.failCreatedToast, message),
                                Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    public void restoreDb(Intent data, Context context) {
        new MyRestore.Init()
                .database(mDatabase)
                .uri(data.getData())
                .setContext(context)
                .OnCompleteListener((success, message) -> {
                    if (success) {
                        Toast.makeText(context,
                                context.getResources().getString(R.string.successRestore),
                                Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(() -> Runtime.getRuntime().exit(0), 3000);
                    } else {
                        Toast.makeText(context,  context.getResources().getString(R.string.failRestore), Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    public void deleteCounters() {
        mRepo.deleteCounters();
    }

    public LiveData<List<Counter>> getAllCounters() {
        return mRepo.getAllCounters();
    }

    public void resetAllCounters(View view) {
        Disposable disposable = mRepo.getAllCountersNoLiveData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(counters -> {
                    CopyBeforeReset copyBeforeReset = new CopyBeforeReset();
                    for (Counter counter : counters) {
                        copyBeforeReset.addCounter(counter);
                        counter.reset(mRepo);
                    }
                    Snackbar.make(view, view.getContext().getResources().getString(R.string
                            .countersReset), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction( view.getContext().getString(R.string.counterResetUndo), v1 -> {
                                for (Counter counter : copyBeforeReset.getCounters()) {
                                    mRepo.updateCounter(counter);
                                }
                            }).show();
                });
    }
}
