package com.example.dictionary.ui.VE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dictionary.DatabaseAccess;
import com.example.dictionary.MyList;
import com.example.dictionary.R;
import com.example.dictionary.WordView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VEFragment extends Fragment {
    SharedPreferences sharedpreferences;
    List<String> vietAnh;
    DatabaseAccess databaseAccess;
    AutoCompleteTextView actvSearch;
    ListView lvList;
    MyList adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        lvList = (ListView) root.findViewById(R.id.lv_list);
        // Setup view's logic
        final AutoCompleteTextView actvSearch = (AutoCompleteTextView)
                root.findViewById(R.id.actv_search);

        // SharedPreferences store and retrieve data;
        sharedpreferences = getActivity().getSharedPreferences("MyPref2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();


        actvSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() >= 1) {

                    databaseAccess = DatabaseAccess.getInstance(getContext(), DatabaseAccess.Mode.MODE_VE);
                    databaseAccess.open();
                    vietAnh = databaseAccess.getWord(s.toString());
                    databaseAccess.close();
                    adapter = new MyList(getContext(), vietAnh, DatabaseAccess.Mode.MODE_VE);
                    actvSearch.setAdapter(adapter);
                    lvList.setAdapter(adapter);
                } else {
                    actvSearch.setAdapter(new MyList(getContext(),
                            new ArrayList<String>(), DatabaseAccess.Mode.MODE_VE));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

        });

        actvSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    getDetailWord();
                    return true;
                }
                return false;
            }
        });
        return root;
    }

    void getDetailWord() {
        Intent intent = new Intent(getContext(), WordView.class);
        String word = actvSearch.getText().toString();
        intent.putExtra("word", word);

        DatabaseAccess db = DatabaseAccess.getInstance(getContext(), DatabaseAccess.Mode.MODE_VE);
        db.open();
        String wordHtml = db.getMean(word);
        db.close();


        intent.putExtra("contentHtml", wordHtml);
        getContext().startActivity(intent);
    }

    void storeDataApp(SharedPreferences.Editor editor) {
        Gson gson = new Gson();
        String jsontxt = gson.toJson(vietAnh);
        editor.putString("VE", jsontxt);
        editor.commit();
    }

    void loadDataApp() {
        Gson gson = new Gson();
        String jsontxt = sharedpreferences.getString("VE", null);
        vietAnh = Arrays.asList(gson.fromJson(jsontxt, String[].class));
    }

}
