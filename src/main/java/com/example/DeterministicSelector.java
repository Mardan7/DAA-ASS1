package com.example;

import java.util.Arrays;

public class DeterministicSelector {
    public long comparisons = 0;
    public int maxDepth = 0;

    public int select(int[] a, int k) {
        comparisons = 0;
        maxDepth = 0;
        if (a == null || k < 0 || k >= a.length) {
            throw new IllegalArgumentException("Invalid input or index k out of bounds.");
        }
        int[] copy = a.clone();
        return selectRecursive(copy, 0, copy.length - 1, k, 1);
    }

    private int selectRecursive(int[] a, int low, int high, int k, int currentDepth) {
        if (currentDepth > maxDepth) maxDepth = currentDepth;

        if (low == high) return a[low];

        int pivotIndex = getPivotIndex(a, low, high);
        int pIndex = partition(a, low, high, pivotIndex);

        if (k == pIndex) {
            return a[k];
        } else if (k < pIndex) {
            return selectRecursive(a, low, pIndex - 1, k, currentDepth + 1);
        } else {
            return selectRecursive(a, pIndex + 1, high, k, currentDepth + 1);
        }
    }

    private int getPivotIndex(int[] a, int low, int high) {
        int n = high - low + 1;
        if (n <= 5) {
            return partition5(a, low, high);
        }

        int numMedians = 0;
        for (int i = low; i <= high; i += 5) {
            int subHigh = Math.min(i + 4, high);
            int medianIdx = partition5(a, i, subHigh);
            swap(a, low + numMedians, medianIdx);
            numMedians++;
        }

        int medianOfMediansK = low + (numMedians - 1) / 2;
        selectRecursive(a, low, low + numMedians - 1, medianOfMediansK, 1);
        return medianOfMediansK;
    }

    private int partition5(int[] a, int low, int high) {
        Arrays.sort(a, low, high + 1);
        return low + (high - low) / 2;
    }

    private int partition(int[] a, int low, int high, int pivotIndex) {
        int pivotValue = a[pivotIndex];
        swap(a, pivotIndex, high);
        int storeIndex = low;

        for (int i = low; i < high; i++) {
            comparisons++;
            if (a[i] < pivotValue) {
                swap(a, i, storeIndex);
                storeIndex++;
            }
        }
        swap(a, storeIndex, high);
        return storeIndex;
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}