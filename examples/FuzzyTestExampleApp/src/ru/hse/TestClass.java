package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class TestClass {
    public static void fuzzerTestOneInput(FuzzedDataProvider p){
        int i = 0, j = 0;
        try{
            i = p.consumeInt();
        }catch (Exception e){
        }
        double k = 20/Math.sqrt(i);
    }
}
