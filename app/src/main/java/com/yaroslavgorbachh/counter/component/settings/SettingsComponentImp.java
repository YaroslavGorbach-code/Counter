package com.yaroslavgorbachh.counter.component.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

public class SettingsComponentImp implements SettingsComponent {
    private final Repo mRepo;

    public SettingsComponentImp(Repo repo) {
        mRepo = repo;
    }

    @Override
    public void backup(Intent data, Context context) {
        mRepo.backup(data, context);
    }

    @Override
    public void restore(Intent data, Context context) {
        mRepo.restore(data, context);
    }

    @Override
    public void deleteAll() {
        mRepo.deleteCounters();
    }

    @Override
    public void resetAll() {
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

    @Override
    public void changeTheme(int color, Resources resources) {
//        if (color == resources.getColor(ThemesColors.RED.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.RED.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.ORANGE.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.ORANGE.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.BLUE.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.BLUE.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.YELLOW.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.YELLOW.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.PURPLE.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.PURPLE.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.BLUE_LIGHT.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.BLUE_LIGHT.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.GREEN_DARK.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.GREEN_DARK.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.GREEN.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.GREEN.getStyleId()));
//        }
//        if (color == resources.getColor(ThemesColors.GREY.getColorId())) {
//            mRepo.changeTheme(new AppStyle(1, Themes.GREY.getStyleId()));
//        }
    }

    @Override
    public LiveData<List<Counter>> getAll() {
        return mRepo.getCounters();
    }
}
