package com.yaroslavgorbachh.counter.Database.BackupAndRestore;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.RoomDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;



public class MyBackup {
    private static final ArrayList<String> SQLITE_TABLES = new ArrayList<String>() {{
        add("android_metadata");
        add("room_master_table");
        add("sqlite_sequence");
    }};
    private static boolean isInSQLiteTables(String table){
        return SQLITE_TABLES.contains(table);
    }
    private static final String STRING_FOR_NULL_VALUE = "!!!string_for_null_value!!!";

    public static class Init{
        private RoomDatabase database;
        private Uri uri;
        private Context context;
        private OnCompleteListener onCompleteListener;

        public Init database(RoomDatabase database){
            this.database = database;
            return this;
        }

        public Init uri(Uri uri){
            this.uri = uri;
            return this;
        }
        public Init setContext(Context context){
            this.context = context;
            return this;
        }


        public Init OnCompleteListener(OnCompleteListener onCompleteListener){
            this.onCompleteListener = onCompleteListener;
            return this;
        }

        public void execute(){
            if(database==null){
                onCompleteListener.onComplete(false, "Database not specified");
                return;
            }
            if(uri==null){
                onCompleteListener.onComplete(false, "Backup uri not specified");
                return;
            }

            Cursor tablesCursor = database.query("SELECT name FROM sqlite_master WHERE type='table'", null);
            ArrayList<String> tables = new ArrayList<>();
            Json.Builder dbBuilder = new Json.Builder();
            if (tablesCursor.moveToFirst()) {
                while ( !tablesCursor.isAfterLast() ) {
                    tables.add(tablesCursor.getString(0));
                    tablesCursor.moveToNext();
                }
                for(String table : tables) {
                    if(isInSQLiteTables(table)) continue;
                    ArrayList<Json> rows = new ArrayList<>();
                    Cursor rowsCursor = database.query("select * from " + table,null);
                    Cursor tableSqlCursor = database.query("select sql from sqlite_master where name= \'"+ table + "\'" , null);
                    tableSqlCursor.moveToFirst();
                    String tableSql = tableSqlCursor.getString(0);
                    tableSql = tableSql.substring(tableSql.indexOf("("));
                    String aic = "";
                    if(tableSql.contains("AUTOINCREMENT")){
                        tableSql = tableSql.substring(0,tableSql.indexOf("AUTOINCREMENT"));
                        tableSql = tableSql.substring(0,tableSql.lastIndexOf("`"));
                        aic = tableSql.substring(tableSql.lastIndexOf("`") + 1);
                    }
                    if (rowsCursor.moveToFirst()) {
                        do {
                            int columnCount = rowsCursor.getColumnCount();
                            Json.Builder rowBuilder = new Json.Builder();
                            for(int i=0; i<columnCount; i++){
                                String columnName = rowsCursor.getColumnName(i);
                                if(columnName.equals(aic)) continue;
                                rowBuilder.putItem(columnName,(rowsCursor.getString(i)!=null) ? rowsCursor.getString(i) : STRING_FOR_NULL_VALUE);
                            }
                            rows.add(rowBuilder.build());
                        } while (rowsCursor.moveToNext());
                    }
                    dbBuilder.putItem(table,rows);
                }
                JsonObject jsonDB = dbBuilder.build().getAsJsonObject();
                Gson gson = new Gson();
                Type type = new TypeToken<JsonObject>(){}.getType();
                String jsonTextDB = gson.toJson(jsonDB, type);
                try {
                    byte[] data = jsonTextDB.getBytes(StandardCharsets.UTF_8);

                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    outputStream.write(data);
                    outputStream.close();

                    if(onCompleteListener!=null)
                        onCompleteListener.onComplete(true, "success");
                } catch (Exception e) {
                    if(onCompleteListener!=null){
                        onCompleteListener.onComplete(false,e.toString());
                    }
                }
            }
        }
    }
}

