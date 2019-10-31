package com.example.dictionary.ui.VE;

import android.content.Context;
import android.database.Cursor;

import com.example.dictionary.DatabaseAccess;
import com.example.dictionary.IDatabaseAccess;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessVE extends DatabaseAccess implements IDatabaseAccess {
    public DatabaseAccessVE(Context context) {
        super(context, Mode.MODE_VE);
    }

    public List<String> getWord(String s) {
        ArrayList<String> word = new ArrayList<String>();
        String sql = "SELECT word FROM viet_anh WHERE word LIKE '"
                + s.toString().toLowerCase()
                + "%' LIMIT 20";
        System.out.println(sql);
        Cursor cursor = database.rawQuery(sql
                , null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                word.add(cursor.getString(0));
                System.out.println(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return word;
    }

    public String getMean(String s) {
        String content = "";
        String sql = "SELECT content FROM viet_anh WHERE word = '"
                + s + "';";
        System.out.println(sql);
        Cursor cursor = database.rawQuery(sql
                , null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            content = cursor.getString(0);
        }
        cursor.close();
        return content;
    }
}