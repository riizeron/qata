package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class TestClass25 {
    public static void fuzzerTestOneInput(FuzzedDataProvider p){
        String s = p.consumeRemainingAsString();
        if(s.length() == 0)
            return;
        int c = Integer.parseInt(s);
    }
}
