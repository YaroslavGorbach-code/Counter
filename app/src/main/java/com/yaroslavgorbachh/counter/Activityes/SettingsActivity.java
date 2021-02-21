package com.yaroslavgorbachh.counter.Activityes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.yaroslavgorbachh.counter.Database.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.Database.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.Database.CounterDatabase;
import com.yaroslavgorbachh.counter.Fragments.SettingsFragment;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

public class SettingsActivity extends AppCompatActivity {
    private static final int RESTORE_REQUEST_CODE = 0;
    private static final int CREATE_FILE = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK){
            if (data != null){
                    new MyBackup.Init()
                            .database(CounterDatabase.getInstance(SettingsActivity.this))
                            .setContext(SettingsActivity.this)
                            .uri(data.getData())
                            .OnCompleteListener((success, message) -> {
                                if (success) {
                                    Toast.makeText(SettingsActivity.this, "Копия успешно сохранена в "
                                            + data.getData().getPath(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SettingsActivity.this,
                                            "Ошибка " + message,
                                            Toast.LENGTH_LONG).show();
                                }
                            }).execute();
                }
        }


        if (requestCode == RESTORE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                new MyRestore.Init()
                        .database(CounterDatabase.getInstance(SettingsActivity.this))
                        .uri(data.getData())
                        .setContext(SettingsActivity.this)
                        .OnCompleteListener((success, message) -> {
                            if (success) {
                                Toast.makeText(SettingsActivity.this,
                                        "Копия успешно востановлена. Сейчас приложение перезапуститься.",
                                        Toast.LENGTH_LONG).show();
                                new Handler().postDelayed((Runnable) () -> Runtime.getRuntime().exit(0), 3000);
                            } else {
                                Toast.makeText(SettingsActivity.this, "Не коректный файл", Toast.LENGTH_LONG).show();
                            }
                        }).execute();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new  Utility().setTheme(PreferenceManager.getDefaultSharedPreferences(this),this);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
        Toolbar mToolbar = findViewById(R.id.toolbar_settings);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mToolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.backUp) {
                createFile();
                return true;
            }

            if (item.getItemId() == R.id.restoreBackUp) {
                openFile();
                return true;
            }
            return false;
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("lockOrientation", true)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        intent.putExtra(Intent.EXTRA_TITLE, "CounterBackup.txt");
        startActivityForResult(intent, CREATE_FILE);
    }

}
