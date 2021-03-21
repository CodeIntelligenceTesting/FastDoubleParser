/*
 * Copyright © 2021. Werner Randelshofer, Switzerland. MIT License.
 */

package ch.randelshofer.math;

import java.util.LongSummaryStatistics;
import java.util.Random;

public class FastDoubleParserBenchmark {
    public static void main(String... args) {
        new FastDoubleParserBenchmark().runBenchmark();
    }

    /**
     * Compares the performance of {@link FastDoubleParser#parseDouble(CharSequence)}
     * against {@link Double#parseDouble(String)};
     */
    public void runBenchmark() {
        Random r = new Random(0);
        String[] strings = r.longs(100_000)
                .mapToDouble(Double::longBitsToDouble)
                .mapToObj(Double::toString)
                .toArray(String[]::new);
        LongSummaryStatistics baselineStats = new LongSummaryStatistics();
        LongSummaryStatistics doubleParseDoubleStats = new LongSummaryStatistics();
        LongSummaryStatistics fastDoubleParserStats = new LongSummaryStatistics();

        double d = 0;
        for (int i = 0; i < 32; i++) {
            {
                long start = System.nanoTime();
                for (String string : strings) {
                    d += string.length();
                }
                long end = System.nanoTime();
                baselineStats.accept(end - start);
            }
            {
                long start = System.nanoTime();
                for (String string : strings) {
                    d += FastDoubleParser.parseDouble(string);
                }
                long end = System.nanoTime();
                fastDoubleParserStats.accept(end - start);
            }
            {
                long start = System.nanoTime();
                for (String string : strings) {
                    d += Double.parseDouble(string);
                }
                long end = System.nanoTime();
                doubleParseDoubleStats.accept(end - start);
            }
        }
        System.out.println(d);

        System.out.println("baseline (loop + add String length):"
                + "\n  " + baselineStats
                + "\n  " + baselineStats.getAverage() / strings.length + "ns per double"
        );
        double doubleParseDoubleNsPerDouble = (doubleParseDoubleStats.getAverage() - baselineStats.getAverage()) / strings.length;
        System.out.println("Double.parseDouble:"
                + "\n  " + doubleParseDoubleStats
                + "\n  " + doubleParseDoubleNsPerDouble + "ns per double (adjusted to baseline)"
        );
        double fastDoubleParserNsPerDouble = (fastDoubleParserStats.getAverage() - baselineStats.getAverage()) / strings.length;
        System.out.println("FastDoubleParser.parseDouble:"
                + "\n  " + fastDoubleParserStats
                + "\n  " + fastDoubleParserNsPerDouble + "ns per double (adjusted to baseline)"
        );

        System.out.println("Speedup factor: " + (doubleParseDoubleNsPerDouble / fastDoubleParserNsPerDouble));

    }
}
