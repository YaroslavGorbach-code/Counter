package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.yaroslavgorbachh.counter.Database.Models.AppStyle;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.R;

public class ColorPickerDialogViewModel extends AndroidViewModel {
    private final Repo mRepo;
    public ColorPickerDialogViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
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
