package com.yaroslavgorbachh.counter.screen.settings;

import android.view.LayoutInflater;
import android.view.View;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.util.CommonUtil;

public class SettingsFragmentView {
    public interface Callback{
        void deleteAll();
        void resetAll();
        void onExportAll();
        void onCreateBackup();
        void onRestoreBackup();
    }
    public SettingsFragmentView(PreferenceFragmentCompat fragment, Callback callback){
        Preference removeAllCountersPref = fragment.findPreference("removeAllCounters");
        Preference resetAllCountersPref = fragment.findPreference("resetAllCounters");
        Preference exportAllCountersPref = fragment.findPreference("exportAllCounters");
        Preference backupPref = fragment.findPreference("backup");

        assert removeAllCountersPref != null;
        removeAllCountersPref.setOnPreferenceClickListener(preference -> {
            new MaterialAlertDialogBuilder(fragment.requireContext())
                    .setTitle(fragment.requireContext().getString(R.string.deleteCountersDeleteDialog))
                    .setMessage(R.string.deleteCounterDialogText)
                    .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which)
                            -> callback.deleteAll())
                    .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null)
                    .show();
            return true;
        });

        assert resetAllCountersPref != null;
        resetAllCountersPref.setOnPreferenceClickListener(preference -> {
            callback.resetAll();
            return true;
        });

        assert exportAllCountersPref != null;
        exportAllCountersPref.setOnPreferenceClickListener(preference -> {
            callback.onExportAll();
            return true;
        });

        assert backupPref != null;
        backupPref.setOnPreferenceClickListener(preference -> {
            View view = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.dialog_backup,
                    null);
            new MaterialAlertDialogBuilder(fragment.requireContext())
                    .setView(view).show();
            MaterialButton create_bt = view.findViewById(R.id.copy);
            MaterialButton restore_bt = view.findViewById(R.id.restore);
            create_bt.setOnClickListener(v -> callback.onCreateBackup());
            restore_bt.setOnClickListener(v -> callback.onRestoreBackup());
            return true;
        });
    }
}
