package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class Monkey {
    public static void fuzzerTestOneInput(FuzzedDataProvider p){
       String s = p.consumeRemainingAsString();
       if(s.equals("WarAndPeaceWarAndPeaceWarAndPeaceWarAndPeaceWarAndPeaceWarAndPeaceWarAndPeace"))
           throw new RuntimeException("New book from Monkey D Fuzzer");
    }
}
