package com.example;

public class MergeSorter {
    private static final int INSERTION_SORT_THRESHOLD = 16;
    
    // Метрики
    public long comparisons = 0;
    public int maxDepth = 0;

    public void sort(int[] a) {
        comparisons = 0;
        maxDepth = 0;
        if (a == null || a.length <= 1) return;
        int[] buffer = new int[a.length];
        sortRecursive(a, buffer, 0, a.length - 1, 1);
    }

    private void sortRecursive(int[] a, int[] buffer, int left, int right, int currentDepth) {
        if (currentDepth > maxDepth) maxDepth = currentDepth;

        if (right - left + 1 <= INSERTION_SORT_THRESHOLD) {
            insertionSort(a, left, right);
            return;
        }

        int mid = left + (right - left) / 2;
        sortRecursive(a, buffer, left, mid, currentDepth + 1);
        sortRecursive(a, buffer, mid + 1, right, currentDepth + 1);

        merge(a, buffer, left, mid, right);
    }

    private void merge(int[] a, int[] buffer, int left, int mid, int right) {
        System.arraycopy(a, left, buffer, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            comparisons++;
            if (buffer[i] <= buffer[j]) {
                a[k++] = buffer[i++];
            } else {
                a[k++] = buffer[j++];
            }
        }

        while (i <= mid) {
            a[k++] = buffer[i++];
        }
        while (j <= right) {
            a[k++] = buffer[j++];
        }
    }

    private void insertionSort(int[] a, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left) {
                comparisons++;
                if (a[j] > key) {
                    a[j + 1] = a[j];
                    j--;
                } else {
                    break;
                }
            }
            a[j + 1] = key;
        }
    }
}
