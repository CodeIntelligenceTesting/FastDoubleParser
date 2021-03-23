/*
 * Copyright © 2021. Werner Randelshofer, Switzerland. MIT License.
 */

package ch.randelshofer;

import ch.randelshofer.math.FastDoubleParser;
import com.code_intelligence.jazzer.api.FuzzedDataProvider;

public class Fuzzer {

    public static void fuzzerTestOneInput(FuzzedDataProvider data) {
        String input = data.consumeRemainingAsString();
        if (!input.matches("^[eE.0-9+-]*$"))
            return;

        boolean doubleThrew = false;
        boolean fastDoubleParserThrew = false;
        double expected = 0;
        double actual = 0;

        try {
            expected = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            doubleThrew = true;
        }

        try {
            actual = FastDoubleParser.parseDouble(input);
        } catch (NumberFormatException e) {
            fastDoubleParserThrew = true;
        }

        if (doubleThrew != fastDoubleParserThrew) {
            // Use these two lines instead if you also want to maintain exception parity with Double.
            // String message = "\"" + input + "\": " + doubleThrew + " != " + fastDoubleParserThrew;
            // throw new IllegalStateException(message);
            return;
        }

        if (Double.doubleToLongBits(expected) != Double.doubleToLongBits(actual)) {
            String message = "\"" + input + "\": " + expected + " != " + actual;
            throw new IllegalStateException(message);
        }
    }
}
