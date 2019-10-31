package com.example.dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dictionary.ui.VE.DatabaseAccessVE;
import com.example.dictionary.ui.home.DatabaseAccessEV;

import java.util.List;

public abstract class DatabaseAccess {
    public abstract List<String> getWord(String toString);

    public abstract String getMean(String word);

    public enum Mode {
        MODE_EV, MODE_VE, MODE_EE
    }

    public static Mode mode;
    private SQLiteOpenHelper openHelper1, openHelper2;
    protected SQLiteDatabase database;
    private static DatabaseAccess instance1, instance2;

    /**
     * Private constructor to aboit object creation from outside classes.
     */
    protected DatabaseAccess(Context context, Mode mode) {
        switch (mode) {
            case MODE_EV: {
                if (openHelper1 == null) {
                    openHelper1 = new DatabaseOpenHelper(context, pickDbFile(mode));
                }
            }
            case MODE_VE: {
                if (openHelper2 == null) {
                    this.openHelper2 = new DatabaseOpenHelper(context, pickDbFile(mode));
                }
            }
        }
        this.mode = mode;
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context, Mode mode) {
        DatabaseAccess.mode = mode;
        switch (mode) {
            case MODE_EV: {
                if (instance1 == null) {
                    System.out.println("Mo EVVV");
                    instance1 = new DatabaseAccessEV(context);
                }
                return instance1;
            }
            case MODE_VE: {
                if (instance2 == null) {
                    System.out.println("MO VEEEE");
                    instance2 = new DatabaseAccessVE(context);
                }
                return instance2;
            }
        }
        return instance1;
    }

    /**
     * Open the database connection.
     */
    public void open() {

        if (mode == Mode.MODE_EV)
            this.database = openHelper1.getWritableDatabase();
        if (mode == Mode.MODE_VE)
            this.database = openHelper2.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public Mode getMode() {
        return mode;
    }

    public String pickDbFile(DatabaseAccess.Mode MODE) {
        switch (MODE) {
            case MODE_EV:
                return "anh_viet.db";
            case MODE_EE:
                return "anh_anh.db";
            case MODE_VE:
                return "viet_anh.db";

            default:
                return null;
        }
    }
}

