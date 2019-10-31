package com.example.dictionary;

import java.util.List;

public interface IDatabaseAccess {
    List<String> getWord(String s);

    String getMean(String s);
}
