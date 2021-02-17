package com.yaroslavgorbachh.counter.Fragments;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.R;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    public Preference mRemoveAllCountersPref;

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mRemoveAllCountersPref = findPreference("removeAllCounters");
        mRemoveAllCountersPref.setOnPreferenceClickListener(preference -> {
            new DeleteCounterDialog(() -> {
                new Repo(getActivity().getApplication()).deleteCounters();
            },2).show(getChildFragmentManager(),"tag");
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


    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


}