package com.example.dictionary.ui.home;

import android.content.Context;
import android.database.Cursor;

import com.example.dictionary.DatabaseAccess;
import com.example.dictionary.IDatabaseAccess;
import com.example.dictionary.Word;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessEV extends DatabaseAccess implements IDatabaseAccess {

    public DatabaseAccessEV(Context context) {
        super(context, Mode.MODE_EV);
    }

    /**
     * Read all words from anh_viet dictionary
     *
     * @return a List of word from dictionary
     */

    public List<String> getWord(String s) {
        ArrayList<String> word = new ArrayList<String>();
        String sql = "SELECT word FROM anh_viet WHERE word LIKE '"
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
        String sql = "SELECT content FROM anh_viet WHERE word = '"
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

    public Word getNextWord(String s) {
        Word word = null;
        String sql = "SELECT * FROM anh_viet WHERE word > 'hell' ORDER BY id LIMIT 1";
        System.out.println(sql);
        Cursor cursor = database.rawQuery(sql
                , null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            word = new Word(cursor.getLong(0), cursor.getString(1), cursor.getString(2)) ;
        }
        cursor.close();
        return word;
    }
}
