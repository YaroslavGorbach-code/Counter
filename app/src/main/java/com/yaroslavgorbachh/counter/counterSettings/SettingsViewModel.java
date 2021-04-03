package com.yaroslavgorbachh.counter.counterSettings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.CopyCounterBeforeReset;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.counterSettings.di.SettingsScope;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.database.CounterDatabase;
import com.yaroslavgorbachh.counter.database.Models.AppStyle;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SettingsScope
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
                    CopyCounterBeforeReset copyCounterBeforeReset = new CopyCounterBeforeReset();
                    for (Counter counter : counters) {
                        copyCounterBeforeReset.addCounter(counter);
                        counter.reset(mRepo);
                    }
                    Snackbar.make(view, view.getContext().getResources().getString(R.string
                            .countersReset), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction( view.getContext().getString(R.string.counterResetUndo), v1 -> {
                                for (Counter counter : copyCounterBeforeReset.getCounters()) {
                                    mRepo.updateCounter(counter);
                                }
                            }).show();
                });
    }


    public void changeThemeColor(int color, Resources resources){
        if (color == resources.getColor(R.color.colorAccent)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme));
        }
        if (color == resources.getColor(R.color.colorAccent_orange)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_orange));
        }
        if (color == resources.getColor(R.color.colorAccent_blue)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_blue));
        }
        if (color == resources.getColor(R.color.colorAccent_yellow)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_yellow));
        }
        if (color == resources.getColor(R.color.colorAccent_purple)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_purple));
        }
        if (color == resources.getColor(R.color.colorAccent_blue_l)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_blue_light));
        }
        if (color == resources.getColor(R.color.colorAccent_green_d)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_green_dark));
        }
        if (color == resources.getColor(R.color.colorAccent_green)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_green));
        }
        if (color == resources.getColor(R.color.colorAccent_gray)){
            mRepo.changeTheme(new AppStyle(1, R.style.AppTheme_gray));
        }
    }
}
