package com.yaroslavgorbachh.counter.Fragments;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    public Preference mRemoveAllCountersPref;
    public Preference mResetAllCountersPref;
    public Preference mExportAllCountersPref;
    private Repo mRepo;
    private final List<Counter> mCopyBeforeReset = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
       mRepo = new Repo(getActivity().getApplication());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mRemoveAllCountersPref = findPreference("removeAllCounters");
        mResetAllCountersPref = findPreference("resetAllCounters");
        mExportAllCountersPref = findPreference("exportAllCounters");

        mRemoveAllCountersPref.setOnPreferenceClickListener(preference -> {
            new DeleteCounterDialog(() -> {
                mRepo.deleteCounters();
            },2).show(getChildFragmentManager(),"tag");
            return true;
        });

        mResetAllCountersPref.setOnPreferenceClickListener(preference -> {
            resetSelectedCounters();
            Snackbar.make(requireView(), getResources().getString(R.string
                    .counterReset), BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                        for (Counter counterBeforeReset : mCopyBeforeReset) {
                            mRepo.updateCounter(counterBeforeReset);
                        }
                    }).show();
            return true;
        });

        mExportAllCountersPref.setOnPreferenceClickListener(preference -> {
            mRepo.getAllCounters().observe(getViewLifecycleOwner(), list -> {
                startActivity(Utility.getShareCountersInCSVIntent(list));
            });
            return true;
        });
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("nightMod") && sharedPreferences.getBoolean("nightMod", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if (key.equals("nightMod") && !sharedPreferences.getBoolean("nightMod", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if (key.equals("keepScreenOn") && sharedPreferences.getBoolean("keepScreenOn", false)){
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (key.equals("keepScreenOn") && !sharedPreferences.getBoolean("keepScreenOn", false)){
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (key.equals("lockOrientation") && sharedPreferences.getBoolean("lockOrientation", true ) ){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (key.equals("lockOrientation") && !sharedPreferences.getBoolean("lockOrientation", true)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void resetSelectedCounters() {
                new Thread(() -> {
                    for (Counter counter : mRepo.getAllCountersNoLiveData()){
                        Counter copy = new Counter(counter.title, counter.value,
                                counter.maxValue, counter.minValue, counter.step,
                                counter.grope, counter.createDate, counter.createDateSort,
                                counter.lastResetDate, counter.lastResetValue,
                                counter.counterMaxValue, counter.counterMinValue);
                        copy.setId(counter.id);
                        mCopyBeforeReset.add(copy);
                        counter.lastResetValue = counter.value;
                        counter.lastResetDate = new Date();

                        if (counter.minValue > 0){
                            counter.value = counter.minValue;
                        }else {
                            counter.value = 0;
                        }
                        mRepo.updateCounter(counter);
                    }
                }).start();
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


}