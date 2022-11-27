package hse.qa.fuzzing;

import hse.qa.calc.*;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class Fuzzer {

    public static void fuzzerTestOneInput(FuzzedDataProvider provider) {
        try {
            Calc.calculate(provider.consumeRemainingAsString());
        } catch (CalcException e) {
            System.out.println(e.getMessage());
        }
    }
}