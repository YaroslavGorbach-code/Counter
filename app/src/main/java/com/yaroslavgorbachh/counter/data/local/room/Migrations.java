package com.yaroslavgorbachh.counter.data.local.room;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yaroslavgorbachh.counter.R;

public class Migrations {

    public static final Migration MIGRATION_24_25 = new Migration(24, 25) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE counter_table ADD COLUMN createDataSort INTEGER DEFAULT null ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetData INTEGER DEFAULT null");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMaxValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMinValue INTEGER NOT NULL DEFAULT 0 ");
        }
    };

    public static final Migration MIGRATION_25_26 = new Migration(25, 26) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE app_style (" +
                    "id INTEGER PRIMARY KEY NOT NULL," +
                    "style INTEGER NOT NULL DEFAULT 0) ");
            database.execSQL("INSERT INTO app_style (id, style) VALUES(1, 0)");
        }
    };

    public static final Migration MIGRATION_26_27 = new Migration(26, 27) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE counter_table ADD COLUMN widgetId INTEGER DEFAULT null ");
        }
    };

    public static final Migration MIGRATION_27_28 = new Migration(27, 28) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS app_style");
        }
    };

    public static final Migration MIGRATION_28_29 = new Migration(28, 29) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE counter_table ADD COLUMN colorId INTEGER DEFAULT null");
            ContentValues cv = new ContentValues();
            cv.put("colorId", R.color.purple);
            database.update("counter_table", SQLiteDatabase.CONFLICT_REPLACE, cv, null, null);
        }
    };
}
