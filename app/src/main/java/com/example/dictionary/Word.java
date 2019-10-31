package com.example.dictionary;

import androidx.annotation.NonNull;

public class Word {
    private long mId;
    private String mWord;
    private String mContent;

    public Word(long mId, String mWord, String mContent) {
        this.mId = mId;
        this.mWord = mWord;
        this.mContent = mContent;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmWord() {
        return mWord;
    }

    public void setmWord(String mWord) {
        this.mWord = mWord;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    @NonNull
    @Override
    public String toString() {
        return getmWord();
    }
}
