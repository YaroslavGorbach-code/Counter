package com.yaroslavgorbachh.counter.Database.BackupAndRestore;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import androidx.room.RoomDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MyRestore {
    private static final String STRING_FOR_NULL_VALUE = "!!!string_for_null_value!!!";

    public static class Init {
        private RoomDatabase database;
        private Uri uri;
        private Context context;
        private OnCompleteListener onCompleteListener;

        public Init database(RoomDatabase database) {
            this.database = database;
            return this;
        }

        public Init uri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Init setContext(Context context) {
            this.context = context;
            return this;
        }

        public Init OnCompleteListener(OnCompleteListener onCompleteListener) {
            this.onCompleteListener = onCompleteListener;
            return this;
        }

        public void execute() {
            try {
                if (database == null) {
                    onCompleteListener.onComplete(false, "Database not specified");
                    return;
                }
                if (uri == null) {
                    onCompleteListener.onComplete(false, "Urinot specified");
                    return;
                }

                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
                reader.close();
                String jsonTextDB = stringBuilder.toString();

                JsonObject jsonDB = new Gson().fromJson(jsonTextDB, JsonObject.class);
                for (String table : jsonDB.keySet()) {
                    Cursor c = database.query("delete from " + table, null);
                    int p = c.getCount();
                    Log.e("TAG", String.valueOf(p));
                    JsonArray tableArray = jsonDB.get(table).getAsJsonArray();

                    for (int i = 0; i < tableArray.size(); i++) {
                        JsonObject row = tableArray.get(i).getAsJsonObject();
                        String query = "insert into " + table + " (";

                        for (String column : row.keySet()) {
                            query = query.concat(column).concat(",");
                        }
                        query = query.substring(0, query.lastIndexOf(","));
                        query = query.concat(") ");
                        query = query.concat("values(");
                        for (String column : row.keySet()) {
                            String value = row.get(column).getAsString();
                            if (value.equals(STRING_FOR_NULL_VALUE)) {
                                query = query.concat("NULL").concat(",");
                            } else {
                                query = query.concat("'").concat(value).concat("'").concat(",");
                            }
                        }
                        query = query.substring(0, query.lastIndexOf(","));
                        query = query.concat(")");
                        Cursor cc = database.query(query, null);
                        int pp = cc.getCount();
                        Log.e("TAG", String.valueOf(pp));
                    }
                }
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(true, "success");
            } catch (Exception e) {
                if (onCompleteListener != null)
                    onCompleteListener.onComplete(false, e.toString());
            }
        }
    }
}
