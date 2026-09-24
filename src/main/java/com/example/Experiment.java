package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Experiment {
    private static final int WARMUPS = 3;
    private static final int REPETITIONS = 7;
    private static final long SEED = 20260924L;
    private static final String[] ALGORITHMS = {"MergeSort", "QuickSort", "DeterministicSelect", "ClosestPair"};
    private record Measurement(long nanos, int depth, long comparisons) {}

    public static void runExperiments() throws IOException {
        int[] sizes = {100, 1000, 5000, 10000, 50000};
        String[] types = {"Random", "Sorted", "ReverseSorted", "Duplicates"};
        Random random = new Random(SEED);
        Files.createDirectories(Path.of("results"));
        Files.createDirectories(Path.of("docs/plots"));
        XYSeriesCollection times = new XYSeriesCollection();
        XYSeriesCollection depths = new XYSeriesCollection();
        for (String algorithm : ALGORITHMS) {
            times.addSeries(new XYSeries(algorithm));
            depths.addSeries(new XYSeries(algorithm));
        }
        System.out.printf("Java: %s; OS: %s %s; processors: %d%n",
                System.getProperty("java.version"), System.getProperty("os.name"),
                System.getProperty("os.arch"), Runtime.getRuntime().availableProcessors());
        System.out.printf("Seed: %d; warmups per case: %d; measured runs: %d%n", SEED, WARMUPS, REPETITIONS);
        try (PrintWriter csv = new PrintWriter(Files.newBufferedWriter(Path.of("results/results.csv"), StandardCharsets.UTF_8));
             PrintWriter raw = new PrintWriter(Files.newBufferedWriter(Path.of("results/raw_results.csv"), StandardCharsets.UTF_8))) {
            csv.println("Algorithm,InputType,Size,ExecutionTimeNs,MaxRecursionDepth,Comparisons");
            raw.println("Algorithm,InputType,Size,Run,ExecutionTimeNs,MaxRecursionDepth,Comparisons");
            for (int n : sizes) {
                for (String type : types) {
                    int[] input = generateArray(n, type, random);
                    int[] expected = input.clone();
                    Arrays.sort(expected);
                    for (int algorithm = 0; algorithm < 3; algorithm++) {
                        Measurement measured = benchmark(algorithm, input, expected, null, type, n, raw);
                        save(csv, algorithm, type, n, measured);
                        if (type.equals("Random")) addPlotPoint(times, depths, algorithm, n, measured);
                    }
                }
                Point[] points = generatePoints(n, random);
                Measurement measured = benchmark(3, null, null, points, "RandomPoints", n, raw);
                save(csv, 3, "RandomPoints", n, measured);
                addPlotPoint(times, depths, 3, n, measured);
                System.out.printf("n=%-6d complete: 13 cases, all result checks passed%n", n);
            }
            if (csv.checkError() || raw.checkError()) throw new IOException("Could not write experimental results");
        }
        generatePlot(times, "Time vs n (random inputs, median of 7)", "Time (ms)", "docs/plots/time_vs_n.png", true);
        generatePlot(depths, "Recursion depth vs n (random inputs)", "Maximum active recursive calls", "docs/plots/depth_vs_n.png", false);
        System.out.println("Saved 65 summary rows and 455 raw measurements in results/.");
        System.out.println("Saved plots in docs/plots/.");
    }

    private static Measurement benchmark(int algorithm, int[] input, int[] expected, Point[] points,
                                         String type, int n, PrintWriter raw) {
        Measurement[] measured = new Measurement[REPETITIONS];
        for (int run = -WARMUPS; run < REPETITIONS; run++) {
            Measurement result = measure(algorithm, input, expected, points);
            if (run >= 0) {
                measured[run] = result;
                raw.printf(Locale.ROOT, "%s,%s,%d,%d,%d,%d,%d%n",
                        ALGORITHMS[algorithm], type, n, run + 1, result.nanos, result.depth, result.comparisons);
            }
        }
        Arrays.sort(measured, (a, b) -> Long.compare(a.nanos, b.nanos));
        return measured[REPETITIONS / 2];
    }

    private static Measurement measure(int algorithm, int[] input, int[] expected, Point[] points) {
        // Preparation and reference checks are excluded from the timed interval.
        int[] copy = input == null ? null : input.clone();
        if (algorithm == 0) {
            MergeSorter sorter = new MergeSorter();
            long start = System.nanoTime();
            sorter.sort(copy);
            long elapsed = System.nanoTime() - start;
            if (!Arrays.equals(copy, expected)) throw new AssertionError("MergeSort mismatch");
            return new Measurement(elapsed, sorter.maxDepth, sorter.comparisons);
        }
        if (algorithm == 1) {
            QuickSorter sorter = new QuickSorter(SEED);
            long start = System.nanoTime();
            sorter.sort(copy);
            long elapsed = System.nanoTime() - start;
            if (!Arrays.equals(copy, expected)) throw new AssertionError("QuickSort mismatch");
            return new Measurement(elapsed, sorter.maxDepth, sorter.comparisons);
        }
        if (algorithm == 2) {
            DeterministicSelector selector = new DeterministicSelector();
            long start = System.nanoTime();
            int value = selector.select(copy, copy.length / 2);
            long elapsed = System.nanoTime() - start;
            if (value != expected[copy.length / 2]) throw new AssertionError("Selection mismatch");
            return new Measurement(elapsed, selector.maxDepth, selector.comparisons);
        }
        ClosestPairSolver solver = new ClosestPairSolver();
        long start = System.nanoTime();
        double distance = solver.findClosestPair(points);
        long elapsed = System.nanoTime() - start;
        if (!Double.isFinite(distance) || distance < 0) throw new AssertionError("Invalid distance");
        if (points.length <= 2000 && Math.abs(distance - ClosestPairSolver.bruteForce(points)) > 1e-9) {
            throw new AssertionError("ClosestPair mismatch");
        }
        return new Measurement(elapsed, solver.maxDepth, solver.comparisons);
    }

    private static void save(PrintWriter csv, int algorithm, String type, int n, Measurement m) {
        csv.printf(Locale.ROOT, "%s,%s,%d,%d,%d,%d%n", ALGORITHMS[algorithm], type, n, m.nanos, m.depth, m.comparisons);
    }

    private static void addPlotPoint(XYSeriesCollection times, XYSeriesCollection depths, int algorithm, int n, Measurement m) {
        times.getSeries(algorithm).add(n, m.nanos / 1_000_000.0);
        depths.getSeries(algorithm).add(n, m.depth);
    }

    private static void generatePlot(XYSeriesCollection data, String title, String yLabel, String path, boolean logY) throws IOException {
        JFreeChart chart = ChartFactory.createXYLineChart(title, "Input size n (log scale)", yLabel, data);
        chart.getXYPlot().setDomainAxis(new LogarithmicAxis("Input size n (log scale)"));
        if (logY) chart.getXYPlot().setRangeAxis(new LogarithmicAxis(yLabel + ", log scale"));
        ChartUtils.saveChartAsPNG(Path.of(path).toFile(), chart, 1100, 700);
    }

    private static int[] generateArray(int n, String type, Random random) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = switch (type) {
                case "Random" -> random.nextInt(n * 10);
                case "Sorted" -> i;
                case "ReverseSorted" -> n - i;
                case "Duplicates" -> random.nextInt(10);
                default -> throw new IllegalArgumentException(type);
            };
        }
        return a;
    }

    private static Point[] generatePoints(int n, Random random) {
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) points[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        return points;
    }
}