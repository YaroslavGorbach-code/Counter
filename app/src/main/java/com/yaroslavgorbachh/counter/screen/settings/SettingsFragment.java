package com.yaroslavgorbachh.counter.screen.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.component.settings.Settings;
import com.yaroslavgorbachh.counter.util.CommonUtil;
import com.yaroslavgorbachh.counter.util.DateAndTimeUtil;
import com.yaroslavgorbachh.counter.util.ViewUtil;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String THEME_CHANGED_BROADCAST = "THEME_CHANGED_BROADCAST";
    private static final int RESTORE_REQUEST_CODE = 0;
    private static final int CREATE_FILE = 1;

    @Inject Settings settings;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                settings.backup(data, requireContext());
            }
        }

        if (requestCode == RESTORE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                settings.restore(data, requireContext());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // inject component
        SettingsViewModel vm = new ViewModelProvider(this).get(SettingsViewModel.class);
        vm.settingsComponent.inject(this);

        // init view
        SettingsFragmentView v = new SettingsFragmentView(this, new SettingsFragmentView.Callback() {
            @Override
            public void deleteAll() {
                settings.deleteAll();
            }

            @Override
            public void resetAll() {
                settings.resetAll();
            }

            @Override
            public void onExportAll() {
              startActivity(CommonUtil.getExportCSVIntent(settings.getAll()));
            }

            @Override
            public void onCreateBackup() {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, "CounterBackup " + DateAndTimeUtil.getCurrentDate());
                startActivityForResult(intent, CREATE_FILE);
            }

            @Override
            public void onRestoreBackup() {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                startActivityForResult(intent, RESTORE_REQUEST_CODE);
            }
        });

    }


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("nightMod") && sharedPreferences.getBoolean("nightMod", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            ViewUtil.animate(requireActivity());
        }
        if (key.equals("nightMod") && !sharedPreferences.getBoolean("nightMod", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            ViewUtil.animate(requireActivity());
        }
        if (key.equals("keepScreenOn") && sharedPreferences.getBoolean("keepScreenOn", true)) {
            requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (key.equals("keepScreenOn") && !sharedPreferences.getBoolean("keepScreenOn", false)) {
            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (key.equals("lockOrientation") && sharedPreferences.getBoolean("lockOrientation", true)) {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (key.equals("lockOrientation") && !sharedPreferences.getBoolean("lockOrientation", true)) {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}