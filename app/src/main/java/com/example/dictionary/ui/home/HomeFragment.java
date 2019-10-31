package com.example.dictionary.ui.home;

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

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    AutoCompleteTextView actvSearch;
    SharedPreferences sharedpreferences;
    List<String> anhViet;
    DatabaseAccess databaseAccess;
    MyList adapter;
    ListView lvList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        actvSearch = (AutoCompleteTextView)
                root.findViewById(R.id.actv_search);
        lvList = (ListView) root.findViewById(R.id.lv_list);

        // Setup view's logic
        actvSearch.setThreshold(1);
        // SharedPreferences store and retrieve data;
        sharedpreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();


        actvSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() >= 1) {
                    databaseAccess = DatabaseAccess.getInstance(getContext(), DatabaseAccess.Mode.MODE_EV);
                    databaseAccess.open();
                    anhViet = databaseAccess.getWord(s.toString());
                    databaseAccess.close();
                    adapter = new MyList(getActivity(), anhViet, DatabaseAccess.Mode.MODE_EV);
                    adapter.notifyDataSetChanged();
                    actvSearch.setAdapter(adapter);

                    lvList.setAdapter(adapter);
                }

                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

        });
        // Event: Press enter to submit
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

        DatabaseAccess db = DatabaseAccess.getInstance(getContext(), DatabaseAccess.Mode.MODE_EV);
        db.open();
        String wordHtml = db.getMean(word);
        db.close();


        intent.putExtra("contentHtml", wordHtml);
        getContext().startActivity(intent);
    }

    void storeDataApp(SharedPreferences.Editor editor) {
        Gson gson = new Gson();
        String jsontxt = gson.toJson(anhViet);
        editor.putString("EV", jsontxt);
        editor.commit();
    }

    void loadDataApp() {
        Gson gson = new Gson();
        String jsontxt = sharedpreferences.getString("EV", null);
        anhViet = Arrays.asList(gson.fromJson(jsontxt, String[].class));
        ;
    }
}