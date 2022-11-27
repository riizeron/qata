package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class TestClass5 {
    public static void fuzzerTestOneInput(FuzzedDataProvider p){
        String s = p.consumeRemainingAsString();
        boolean b = s.length()>0&&(s.startsWith("-")||Character.isDigit(s.charAt(0)));
        for(char c:s.toCharArray())
            if(!Character.isDigit(c)&&c!='-')
                b = false;
        if(b) {
            int c = Integer.parseInt(s);
        }
    }
}
