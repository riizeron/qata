package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class TestClass2 {
    public static void fuzzerTestOneInput(FuzzedDataProvider p){
        String s = p.consumeRemainingAsString();
        if(s.isEmpty())
            return;
        int c = Integer.parseInt(s);
    }
}
