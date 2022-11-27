package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class TestClass3 {
    public static void fuzzerTestOneInput(FuzzedDataProvider p){
        String s = p.consumeRemainingAsString();
        boolean b = true;
        for(char c:s.toCharArray())
            if(!Character.isDigit(c)&&c!='-')
                b = false;
        if(b) {
            int c = Integer.parseInt(s);
        }
    }
}
