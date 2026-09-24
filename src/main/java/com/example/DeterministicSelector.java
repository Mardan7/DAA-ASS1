package com.example;

public class DeterministicSelector {
    public long comparisons = 0;
    public int maxDepth = 0;

    /** Returns the zero-based order statistic without changing the caller's array. */
    public int select(int[] a, int k) {
        comparisons = 0;
        maxDepth = 0;
        if (a == null || k < 0 || k >= a.length) {
            throw new IllegalArgumentException("Invalid input or index k out of bounds.");
        }
        return selectRecursive(a.clone(), 0, a.length - 1, k, 1);
    }

    private int selectRecursive(int[] a, int low, int high, int k, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        if (high - low < 5) {
            insertionSort(a, low, high);
            return a[k];
        }

        int count = 0;
        for (int start = low; start <= high; start += 5) {
            int end = Math.min(start + 4, high);
            insertionSort(a, start, end);
            swap(a, low + count++, start + (end - start) / 2);
        }
        int pivot = selectRecursive(a, low, low + count - 1,
                low + (count - 1) / 2, depth + 1);

        // Discard the entire equal region, including on all-equal input.
        int lt = low, i = low, gt = high;
        while (i <= gt) {
            comparisons++;
            if (a[i] < pivot) {
                swap(a, lt++, i++);
            } else {
                comparisons++;
                if (a[i] > pivot) swap(a, i, gt--);
                else i++;
            }
        }
        if (k < lt) return selectRecursive(a, low, lt - 1, k, depth + 1);
        if (k > gt) return selectRecursive(a, gt + 1, high, k, depth + 1);
        return pivot;
    }

    private void insertionSort(int[] a, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int value = a[i], j = i - 1;
            while (j >= low) {
                comparisons++;
                if (a[j] <= value) break;
                a[j + 1] = a[j--];
            }
            a[j + 1] = value;
        }
    }

    private void swap(int[] a, int i, int j) {
        int value = a[i];
        a[i] = a[j];
        a[j] = value;
    }
}