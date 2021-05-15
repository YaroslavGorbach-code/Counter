package com.yaroslavgorbachh.counter.screen.counterSettings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.R;

import com.yaroslavgorbachh.counter.screen.counterSettings.themes.Themes;
import com.yaroslavgorbachh.counter.screen.counterSettings.themes.ThemesColors;
import com.yaroslavgorbachh.counter.data.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.data.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.data.CounterDatabase;
import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SettingsViewModel extends ViewModel {
    private final CounterDatabase mDatabase;
    private final Repo repo;
    private final CompositeDisposable mDisposables = new CompositeDisposable();


    @Inject
    SettingsViewModel(CounterDatabase counterDatabase, Repo repo){
        mDatabase = counterDatabase;
        this.repo = repo;
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
        repo.deleteCounters();
    }

    public LiveData<List<Counter>> getAllCounters() {
        return repo.getAllCounters();
    }

    public void resetAllCounters(View view) {
        // TODO: 5/15/2021 reset all
//        Disposable disposable = repo.getAllCountersNoLiveData()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(counters -> {
//                    CopyCounterBeforeReset copyCounterBeforeReset = new CopyCounterBeforeReset();
//                    for (Counter counter : counters) {
//                        copyCounterBeforeReset.addCounter(counter);
//                        repo.resetCounter(counter.id);
//                    }
//                    Snackbar.make(view, view.getContext().getResources().getString(R.string
//                            .countersReset), BaseTransientBottomBar.LENGTH_LONG)
//                            .setAction( view.getContext().getString(R.string.counterResetUndo), v1 -> {
//                                for (Counter counter : copyCounterBeforeReset.getCounters()) {
//                                    repo.updateCounter(counter);
//                                }
//                            }).show();
//                });
//        mDisposables.add(disposable);
    }


    public void changeTheme(int color, Resources resources){
        if (color == resources.getColor(ThemesColors.RED.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.RED.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.ORANGE.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.ORANGE.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.BLUE.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.BLUE.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.YELLOW.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.YELLOW.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.PURPLE.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.PURPLE.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.BLUE_LIGHT.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.BLUE_LIGHT.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.GREEN_DARK.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.GREEN_DARK.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.GREEN.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.GREEN.getStyleId()));
        }
        if (color == resources.getColor(ThemesColors.GREY.getColorId())){
            repo.changeTheme(new AppStyle(1, Themes.GREY.getStyleId()));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.dispose();
    }
}
