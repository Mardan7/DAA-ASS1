package com.example;

import java.util.Random;

public class QuickSorter {
    private static final Random random = new Random();
    
    public long comparisons = 0;
    public long swaps = 0;
    public int maxDepth = 0;

    public void sort(int[] a) {
        comparisons = 0;
        swaps = 0;
        maxDepth = 0;
        if (a == null || a.length <= 1) return;
        quickSortRecursive(a, 0, a.length - 1, 1);
    }

    private void quickSortRecursive(int[] a, int low, int high, int currentDepth) {
        while (low < high) {
            if (currentDepth > maxDepth) maxDepth = currentDepth;

            int pivotIndex = low + random.nextInt(high - low + 1);
            int pIndex = partition(a, low, high, pivotIndex);

            // Оптимизация хвостовой рекурсии: рекурсия для меньшей части, итерация для большей
            if (pIndex - low < high - pIndex) {
                quickSortRecursive(a, low, pIndex - 1, currentDepth + 1);
                low = pIndex + 1;
            } else {
                quickSortRecursive(a, pIndex + 1, high, currentDepth + 1);
                high = pIndex - 1;
            }
        }
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
        if (i != j) {
            swaps++;
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
    }
}
