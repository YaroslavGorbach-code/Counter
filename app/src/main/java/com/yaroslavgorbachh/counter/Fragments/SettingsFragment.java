package com.yaroslavgorbachh.counter.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.Database.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.Database.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.Database.CounterDatabase;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.ColorPickerDialog;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int RESTORE_REQUEST_CODE = 0;
    private static final int CREATE_FILE = 1;
    private Repo mRepo;
    private final List<Counter> mCopyBeforeReset = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK){
            if (data != null){
                new MyBackup.Init()
                        .database(CounterDatabase.getInstance(getActivity()))
                        .setContext(requireContext())
                        .uri(data.getData())
                        .OnCompleteListener((success, message) -> {
                            if (success) {
                                Toast.makeText(getActivity(), getString(R.string.successCreatedToast,
                                        data.getData().getPath()), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(),
                                        getString(R.string.failCreatedToast, message),
                                        Toast.LENGTH_LONG).show();
                            }
                        }).execute();
            }
        }


        if (requestCode == RESTORE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                new MyRestore.Init()
                        .database(CounterDatabase.getInstance(getActivity()))
                        .uri(data.getData())
                        .setContext(requireContext())
                        .OnCompleteListener((success, message) -> {
                            if (success) {
                                Toast.makeText(requireContext(),
                                        getString(R.string.successRestore),
                                        Toast.LENGTH_LONG).show();
                                new Handler().postDelayed((Runnable) () -> Runtime.getRuntime().exit(0), 3000);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.failRestore), Toast.LENGTH_LONG).show();
                            }
                        }).execute();
            }
        }
    }

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
        Preference mRemoveAllCountersPref = findPreference("removeAllCounters");
        Preference mResetAllCountersPref = findPreference("resetAllCounters");
        Preference mExportAllCountersPref = findPreference("exportAllCounters");
        Preference mChangeAccentColorPref = findPreference("changeAccentColor");
        Preference mBackupPref= findPreference("backup");

        mRemoveAllCountersPref.setOnPreferenceClickListener(preference -> {
            new DeleteCounterDialog(() -> {
                mRepo.deleteCounters();
            },2).show(getChildFragmentManager(),"tag");
            return true;
        });

        mResetAllCountersPref.setOnPreferenceClickListener(preference -> {
            resetSelectedCounters();
            return true;
        });

        mExportAllCountersPref.setOnPreferenceClickListener(preference -> {
            mRepo.getAllCounters().observe(getViewLifecycleOwner(), list -> {
                startActivity(Utility.getShareCountersInCSVIntent(list));
            });
            return true;
        });

        mChangeAccentColorPref.setOnPreferenceClickListener(preference -> {
            ColorPickerDialog.newInstance().show(getParentFragmentManager(), "colorPicker");
            return true;
        });

        mBackupPref.setOnPreferenceClickListener(preference -> {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.backud_dialog,
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
        if (key.equals("nightMod") && sharedPreferences.getBoolean("nightMod", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if (key.equals("nightMod") && !sharedPreferences.getBoolean("nightMod", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if (key.equals("keepScreenOn") && sharedPreferences.getBoolean("keepScreenOn", true)){
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
        // TODO: 2/22/2021 переделать метод по другому
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
                    Snackbar.make(requireView(), getResources().getString(R.string
                            .countersReset), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                                for (Counter counterBeforeReset : mCopyBeforeReset) {
                                    mRepo.updateCounter(counterBeforeReset);
                                }
                            }).show();
                }).start();
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
        // TODO: 2/22/2021 добавить дату к названию
        intent.putExtra(Intent.EXTRA_TITLE, "CounterBackup.txt");
        startActivityForResult(intent, CREATE_FILE);
    }

}