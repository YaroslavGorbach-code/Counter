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
import com.yaroslavgorbachh.counter.counterHistory.HistoryManager;
import com.yaroslavgorbachh.counter.counterSettings.di.SettingsScope;
import com.yaroslavgorbachh.counter.counterSettings.themes.Themes;
import com.yaroslavgorbachh.counter.counterSettings.themes.ThemesColors;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.database.CounterDatabase;
import com.yaroslavgorbachh.counter.database.Models.AppStyle;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SettingsScope
public class SettingsViewModel extends ViewModel {
    private final CounterDatabase mDatabase;
    private final Repo mRepo;
    private final CompositeDisposable mDisposables = new CompositeDisposable();


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
        mDisposables.add(disposable);
    }


    public void changeTheme(int color, Resources resources){
        if (color == resources.getColor(ThemesColors.RED.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.RED.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.ORANGE.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.ORANGE.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.BLUE.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.BLUE.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.YELLOW.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.YELLOW.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.PURPLE.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.PURPLE.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.BLUE_LIGHT.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.BLUE_LIGHT.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.GREEN_DARK.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.GREEN_DARK.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.GREEN.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.GREEN.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.GREY.getColorId())){
            mRepo.changeTheme(new AppStyle(1, Themes.GREY.getStyleId()));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.dispose();
    }
}
