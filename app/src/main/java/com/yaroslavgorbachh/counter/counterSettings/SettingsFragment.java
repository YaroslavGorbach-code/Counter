package com.yaroslavgorbachh.counter.counterSettings;

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
import com.yaroslavgorbachh.counter.CopyBeforeReset;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.database.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.database.CounterDatabase;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;
import com.yaroslavgorbachh.counter.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int RESTORE_REQUEST_CODE = 0;
    private static final int CREATE_FILE = 1;
    private Repo mRepo;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
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
        Preference mBackupPref = findPreference("backup");

        mRemoveAllCountersPref.setOnPreferenceClickListener(preference -> {
            new DeleteCounterDialog(() -> {
                mRepo.deleteCounters();
            }, 2).show(getChildFragmentManager(), "tag");
            return true;
        });

        mResetAllCountersPref.setOnPreferenceClickListener(preference -> {
            resetAllCounters();
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
        }
        if (key.equals("nightMod") && !sharedPreferences.getBoolean("nightMod", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if (key.equals("keepScreenOn") && sharedPreferences.getBoolean("keepScreenOn", true)) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (key.equals("keepScreenOn") && !sharedPreferences.getBoolean("keepScreenOn", false)) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (key.equals("lockOrientation") && sharedPreferences.getBoolean("lockOrientation", true)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (key.equals("lockOrientation") && !sharedPreferences.getBoolean("lockOrientation", true)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    public void resetAllCounters() {
        Disposable disposable = mRepo.getAllCountersNoLiveData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(counters -> {
                    CopyBeforeReset copyBeforeReset = new CopyBeforeReset();
                    for (Counter counter : counters) {
                        copyBeforeReset.addCounter(counter);
                        counter.reset(mRepo);
                    }
                    Snackbar.make(requireView(), getResources().getString(R.string
                            .countersReset), BaseTransientBottomBar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                                for (Counter counter : copyBeforeReset.getCounters()) {
                                    mRepo.updateCounter(counter);
                                }
                            }).show();
                });
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