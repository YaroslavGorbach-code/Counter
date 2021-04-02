package com.yaroslavgorbachh.counter.counterSettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.CopyBeforeReset;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.counterSettings.Animations.AnimateThemeChange;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.database.CounterDatabase;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;
import com.yaroslavgorbachh.counter.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.di.ViewModelProviderFactory;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int RESTORE_REQUEST_CODE = 0;
    private static final int CREATE_FILE = 1;

    private SettingsViewModel mViewModel;
    @Inject ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.settingsComponentFactory().create().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(SettingsViewModel.class);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
               mViewModel.backup(data, requireContext());
            }
        }


        if (requestCode == RESTORE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mViewModel.restoreDb(data, requireContext());
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
        Preference mRemoveAllCountersPref = findPreference("removeAllCounters");
        Preference mResetAllCountersPref = findPreference("resetAllCounters");
        Preference mExportAllCountersPref = findPreference("exportAllCounters");
        Preference mChangeAccentColorPref = findPreference("changeAccentColor");
        Preference mBackupPref = findPreference("backup");

        assert mRemoveAllCountersPref != null;
        mRemoveAllCountersPref.setOnPreferenceClickListener(preference -> {
            new DeleteCounterDialog(() -> mViewModel.deleteCounters(), 2).show(getChildFragmentManager(), "tag");
            return true;
        });

        assert mResetAllCountersPref != null;
        mResetAllCountersPref.setOnPreferenceClickListener(preference -> {
            mViewModel.resetAllCounters(requireView());
            return true;
        });

        assert mExportAllCountersPref != null;
        mExportAllCountersPref.setOnPreferenceClickListener(preference -> {
            mViewModel.getAllCounters().observe(getViewLifecycleOwner(), list -> startActivity(Utility.getShareCountersInCSVIntent(list)));
            return true;
        });

        assert mChangeAccentColorPref != null;
        mChangeAccentColorPref.setOnPreferenceClickListener(preference -> {
            ColorPickerDialog.newInstance().show(getParentFragmentManager(), "colorPicker");
            return true;
        });

        assert mBackupPref != null;
        mBackupPref.setOnPreferenceClickListener(preference -> {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.backup_dialog,
                    null);
            new MaterialAlertDialogBuilder(requireContext())
                    .setView(view).show();
            MaterialButton create_bt = view.findViewById(R.id.createCopy);
            MaterialButton restore_bt = view.findViewById(R.id.restoreCopy);
            create_bt.setOnClickListener(v -> createFile());
            restore_bt.setOnClickListener(v -> openFile());

            return true;
        });
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("nightMod") && sharedPreferences.getBoolean("nightMod", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            AnimateThemeChange.animate(requireActivity());
        }
        if (key.equals("nightMod") && !sharedPreferences.getBoolean("nightMod", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            AnimateThemeChange.animate(requireActivity());
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

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, RESTORE_REQUEST_CODE);
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "CounterBackup " + Utility.getCurrentDate());
        startActivityForResult(intent, CREATE_FILE);
    }

}