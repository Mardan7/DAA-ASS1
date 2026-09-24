package com.example;

import java.util.Random;

public class QuickSorter {
    private final Random random;
    public long comparisons = 0;
    public long swaps = 0;
    public int maxDepth = 0;

    public QuickSorter() { this(new Random()); }
    public QuickSorter(long seed) { this(new Random(seed)); }
    private QuickSorter(Random random) { this.random = random; }

    public void sort(int[] a) {
        comparisons = 0;
        swaps = 0;
        maxDepth = 0;
        if (a == null || a.length <= 1) return;
        quickSortRecursive(a, 0, a.length - 1, 1);
    }

    private void quickSortRecursive(int[] a, int low, int high, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        while (low < high) {
            int pivot = a[low + random.nextInt(high - low + 1)];
            int lt = low, i = low, gt = high;
            while (i <= gt) {
                comparisons++;
                if (a[i] < pivot) swap(a, lt++, i++);
                else {
                    comparisons++;
                    if (a[i] > pivot) swap(a, i, gt--);
                    else i++;
                }
            }
            // Recurse only on a nontrivial smaller side; loop over the larger side.
            if (lt - low < high - gt) {
                if (lt - low > 1) quickSortRecursive(a, low, lt - 1, depth + 1);
                low = gt + 1;
            } else {
                if (high - gt > 1) quickSortRecursive(a, gt + 1, high, depth + 1);
                high = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        if (i != j) {
            swaps++;
            int value = a[i];
            a[i] = a[j];
            a[j] = value;
        }
    }
}