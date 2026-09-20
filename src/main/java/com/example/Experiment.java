package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class Experiment {
    private static final Random random = new Random();

    public static void runExperiments() {
        int[] sizes = {100, 1000, 5000, 10000, 50000};
        String[] types = {"Random", "Sorted", "ReverseSorted", "Duplicates"};

        DefaultCategoryDataset timeDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset depthDataset = new DefaultCategoryDataset();

        File resultsDir = new File("results");
        if (!resultsDir.exists()) resultsDir.mkdirs();

        File plotsDir = new File("plots");
        if (!plotsDir.exists()) plotsDir.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("Algorithm,InputType,Size,ExecutionTimeNs,MaxRecursionDepth,Comparisons");

            for (int n : sizes) {
                for (String type : types) {
                    int[] arr = generateArray(n, type);

                    // 1. MergeSort
                    MergeSorter mergeSorter = new MergeSorter();
                    int[] copy1 = arr.clone();
                    long start = System.nanoTime();
                    mergeSorter.sort(copy1);
                    long elapsed = System.nanoTime() - start;
                    writer.printf("%s,%s,%d,%d,%d,%d\n", "MergeSort", type, n, elapsed, mergeSorter.maxDepth, mergeSorter.comparisons);

                    if (type.equals("Random")) {
                        timeDataset.addValue(elapsed / 1000.0, "MergeSort", String.valueOf(n));
                        depthDataset.addValue(mergeSorter.maxDepth, "MergeSort", String.valueOf(n));
                    }

                    // 2. QuickSort
                    QuickSorter quickSorter = new QuickSorter();
                    int[] copy2 = arr.clone();
                    start = System.nanoTime();
                    quickSorter.sort(copy2);
                    elapsed = System.nanoTime() - start;
                    writer.printf("%s,%s,%d,%d,%d,%d\n", "QuickSort", type, n, elapsed, quickSorter.maxDepth, quickSorter.comparisons);

                    if (type.equals("Random")) {
                        timeDataset.addValue(elapsed / 1000.0, "QuickSort", String.valueOf(n));
                        depthDataset.addValue(quickSorter.maxDepth, "QuickSort", String.valueOf(n));
                    }

                    // 3. Deterministic Select
                    DeterministicSelector selector = new DeterministicSelector();
                    int[] copy3 = arr.clone();
                    int k = n / 2;
                    start = System.nanoTime();
                    selector.select(copy3, k);
                    elapsed = System.nanoTime() - start;
                    writer.printf("%s,%s,%d,%d,%d,%d\n", "DeterministicSelect", type, n, elapsed, selector.maxDepth, selector.comparisons);

                    if (type.equals("Random")) {
                        timeDataset.addValue(elapsed / 1000.0, "DeterministicSelect", String.valueOf(n));
                        depthDataset.addValue(selector.maxDepth, "DeterministicSelect", String.valueOf(n));
                    }
                }

                // 4. Closest Pair
                Point[] points = generatePoints(n);
                ClosestPairSolver closestSolver = new ClosestPairSolver();
                long start = System.nanoTime();
                closestSolver.findClosestPair(points);
                long elapsed = System.nanoTime() - start;
                writer.printf("%s,%s,%d,%d,%d,%d\n", "ClosestPair", "RandomPoints", n, elapsed, closestSolver.maxDepth, closestSolver.comparisons);

                timeDataset.addValue(elapsed / 1000.0, "ClosestPair", String.valueOf(n));
                depthDataset.addValue(closestSolver.maxDepth, "ClosestPair", String.valueOf(n));
            }

            System.out.println("[+] Эксперименты выполнены. Файл results/results.csv создан.");

            // Генерация графиков PNG
            generatePlot(timeDataset, "Execution Time vs n", "Input Size (n)", "Time (microseconds)", "plots/time_vs_n.png");
            generatePlot(depthDataset, "Recursion Depth vs n", "Input Size (n)", "Max Recursion Depth", "plots/depth_vs_n.png");
            System.out.println("[+] Графики созданы в папке plots/");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePlot(DefaultCategoryDataset dataset, String title, String categoryAxisLabel, String valueAxisLabel, String filePath) throws IOException {
        JFreeChart chart = ChartFactory.createLineChart(title, categoryAxisLabel, valueAxisLabel, dataset);
        ChartUtils.saveChartAsPNG(new File(filePath), chart, 800, 600);
    }

    private static int[] generateArray(int n, String type) {
        int[] a = new int[n];
        switch (type) {
            case "Random":
                for (int i = 0; i < n; i++) a[i] = random.nextInt(n * 10);
                break;
            case "Sorted":
                for (int i = 0; i < n; i++) a[i] = i;
                break;
            case "ReverseSorted":
                for (int i = 0; i < n; i++) a[i] = n - i;
                break;
            case "Duplicates":
                for (int i = 0; i < n; i++) a[i] = random.nextInt(10);
                break;
        }
        return a;
    }

    private static Point[] generatePoints(int n) {
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        }
        return points;
    }
}