package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTest {
    private final Random random = new Random();

    @Test
    @DisplayName("MergeSort: краевые случаи и разная структура входных данных")
    public void testMergeSort() {
        MergeSorter sorter = new MergeSorter();

        // 1. Пустой массив и массив из 1 элемента
        int[] empty = {};
        sorter.sort(empty);
        assertArrayEquals(new int[]{}, empty);

        int[] single = {42};
        sorter.sort(single);
        assertArrayEquals(new int[]{42}, single);

        // 2. Стандартные случаи
        int[][] testCases = {
            {5, 2, 9, 1, 5, 6},
            {1, 2, 3, 4, 5},       // Упорядоченный
            {5, 4, 3, 2, 1},       // Обратно-упорядоченный
            {2, 2, 2, 2, 2},       // Дубликаты
            {-5, -1, 0, 10, -20}   // Отрицательные числа
        };

        for (int[] arr : testCases) {
            int[] expected = arr.clone();
            Arrays.sort(expected);

            int[] actual = arr.clone();
            sorter.sort(actual);

            assertArrayEquals(expected, actual, "Ошибка на массиве: " + Arrays.toString(arr));
        }
    }

    @Test
    @DisplayName("QuickSort: краевые случаи и разная структура входных данных")
    public void testQuickSort() {
        QuickSorter sorter = new QuickSorter();

        int[] empty = {};
        sorter.sort(empty);
        assertArrayEquals(new int[]{}, empty);

        int[] single = {7};
        sorter.sort(single);
        assertArrayEquals(new int[]{7}, single);

        int[][] testCases = {
            {10, -2, 0, 14, 8, 8, 3},
            {1, 2, 3, 4, 5, 6, 7},
            {9, 8, 7, 6, 5, 4},
            {4, 4, 4, 4}
        };

        for (int[] arr : testCases) {
            int[] expected = arr.clone();
            Arrays.sort(expected);

            int[] actual = arr.clone();
            sorter.sort(actual);

            assertArrayEquals(expected, actual, "Ошибка на массиве: " + Arrays.toString(arr));
        }
    }

    @Test
    @DisplayName("Deterministic Select: 100 случайных тестов в сравнении с Arrays.sort(a)[k]")
    public void testDeterministicSelect100RandomRuns() {
        DeterministicSelector selector = new DeterministicSelector();

        for (int run = 0; run < 100; run++) {
            int n = random.nextInt(100) + 1; // n от 1 до 100
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = random.nextInt(1000) - 500;
            }
            int k = random.nextInt(n);

            int[] expectedArr = arr.clone();
            Arrays.sort(expectedArr);
            int expectedValue = expectedArr[k];

            int actualValue = selector.select(arr, k);
            assertEquals(expectedValue, actualValue, "Ошибка на итерации " + run + ", k=" + k);
        }
    }

    @Test
    @DisplayName("Closest Pair: Сравнение с Brute-Force O(n^2) для малых датасетов")
    public void testClosestPairAgainstBruteForce() {
        ClosestPairSolver solver = new ClosestPairSolver();

        for (int run = 0; run < 20; run++) {
            int n = random.nextInt(200) + 2; // от 2 до 200 точек
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
            }

            double expected = ClosestPairSolver.bruteForce(points);
            double actual = solver.findClosestPair(points);

            assertEquals(expected, actual, 1e-9, "Несовпадение минимального расстояния на прогоне " + run);
        }
    }
}