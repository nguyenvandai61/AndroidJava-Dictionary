package com.example.dictionary;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class WordView extends AppCompatActivity {
    String word;
    WebView wvWord;
    String wordHtml;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_word);

        Toolbar toolbar = findViewById(R.id.toolbar);
        wvWord = findViewById(R.id.wv_word);
        setSupportActionBar(toolbar);
        // Title bar
        getResource();
        this.setTitle(word);

        // Load page
        wvWord.loadData(wordHtml, "text/html; charset=utf-8", "UTF-8");
    }

    void getResource() {
        word = getIntent().getStringExtra("word");
        wordHtml = getIntent().getStringExtra("contentHtml");
    }
}
