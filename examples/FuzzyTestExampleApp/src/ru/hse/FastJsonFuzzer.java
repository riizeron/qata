
package ru.hse;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

// Found the issues described in
// https://github.com/alibaba/fastjson/issues/3631
public class FastJsonFuzzer {
    private static Gson gs;
    public static void fuzzerTestOneInput(FuzzedDataProvider data) {
        String s = data.consumeRemainingAsString();
        if(s.isEmpty())
            return;
        try {
            gs.fromJson(s,JavaCl.class);

        } catch (JsonSyntaxException ignored) {

        }
    }
}