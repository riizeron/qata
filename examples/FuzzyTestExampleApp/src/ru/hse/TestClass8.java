package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class TestClass8 {
    public static void fuzzerTestOneInput(FuzzedDataProvider p){
        String s = p.consumeRemainingAsString();
        if(s.length()<7)
            return;
        s =s.substring(7);
        boolean b = s.length()>0;

        for(int i = 0;i<s.length();i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c) && (c != '-' || i>0))
                b = false;
        }
        if(b) {
            int c = Integer.parseInt(s);
        }
    }
}
