package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTest {
    private final Random random = new Random(20260924L);

    @Test
    public void sortingRandomAndStructuredInputs() {
        for (int n : new int[]{0, 1, 2, 16, 17, 100, 2000, 50000}) {
            for (int type = 0; type < 4; type++) {
                int[] input = new int[n];
                for (int i = 0; i < n; i++) {
                    input[i] = switch (type) {
                        case 0 -> random.nextInt();
                        case 1 -> i;
                        case 2 -> n - i;
                        default -> random.nextInt(10);
                    };
                }
                int[] expected = input.clone();
                Arrays.sort(expected);
                int[] merged = input.clone();
                new MergeSorter().sort(merged);
                assertArrayEquals(expected, merged);
                QuickSorter quick = new QuickSorter(42);
                quick.sort(input);
                assertArrayEquals(expected, input);
                if (n > 1) assertTrue(quick.maxDepth <= 1 + (int) (Math.log(n) / Math.log(2)));
            }
        }
    }

    @Test
    public void selectionDuplicatesRanksAndValidation() {
        DeterministicSelector selector = new DeterministicSelector();
        for (int run = 0; run < 100; run++) {
            int[] input = random.ints(1 + random.nextInt(80), -4, 5).toArray();
            int[] original = input.clone();
            int[] expected = input.clone();
            Arrays.sort(expected);
            for (int k = 0; k < input.length; k++) assertEquals(expected[k], selector.select(input, k));
            assertArrayEquals(original, input);
        }
        int[] same = new int[50000];
        assertEquals(0, selector.select(same, same.length / 2));
        assertTrue(selector.comparisons < 5L * same.length, "Equal keys must take linear work");
        assertThrows(IllegalArgumentException.class, () -> selector.select(new int[0], 0));
        assertThrows(IllegalArgumentException.class, () -> selector.select(null, 0));
        assertThrows(IllegalArgumentException.class, () -> selector.select(new int[]{1}, -1));
        assertThrows(IllegalArgumentException.class, () -> selector.select(new int[]{1}, 1));
        int[] extremes = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE};
        assertEquals(Integer.MIN_VALUE, selector.select(extremes, 0));
        assertEquals(Integer.MAX_VALUE, selector.select(extremes, 2));
    }

    @Test
    public void closestPairTiedCoordinatesAndEdges() {
        ClosestPairSolver solver = new ClosestPairSolver();
        assertEquals(Double.POSITIVE_INFINITY, solver.findClosestPair(new Point[0]));
        assertEquals(Double.POSITIVE_INFINITY, solver.findClosestPair(new Point[]{new Point(0, 0)}));
        Point repeated = new Point(2, 3);
        assertEquals(0, solver.findClosestPair(new Point[]{repeated, repeated, new Point(9, 9), new Point(2, 3)}));
        for (int run = 0; run < 100; run++) {
            Point[] points = new Point[2 + random.nextInt(100)];
            for (int i = 0; i < points.length; i++) {
                points[i] = new Point(run % 2 == 0 ? 0 : random.nextInt(5), random.nextDouble() * 100);
            }
            Point[] original = points.clone();
            assertEquals(ClosestPairSolver.bruteForce(points), solver.findClosestPair(points), 1e-9);
            assertArrayEquals(original, points);
        }
        Point[] points = new Point[2000];
        for (int i = 0; i < points.length; i++) points[i] = new Point(random.nextDouble(), random.nextDouble());
        assertEquals(ClosestPairSolver.bruteForce(points), solver.findClosestPair(points), 1e-12);
    }

    @Test
    public void closestPairLargeKnownAnswer() {
        Point[] points = new Point[50000];
        for (int i = 0; i < points.length; i++) points[i] = new Point(i * 3.0, 0);
        assertEquals(3.0, new ClosestPairSolver().findClosestPair(points));
    }

    @Test
    public void metricsResetBetweenCalls() {
        MergeSorter merge = new MergeSorter();
        merge.sort(new int[]{3, 1, 2});
        merge.sort(new int[0]);
        assertEquals(0, merge.comparisons);
        assertEquals(0, merge.maxDepth);
        QuickSorter quick = new QuickSorter(1);
        quick.sort(new int[]{3, 1, 2});
        quick.sort(new int[0]);
        assertEquals(0, quick.comparisons);
        assertEquals(0, quick.maxDepth);
        DeterministicSelector select = new DeterministicSelector();
        select.select(new int[]{3, 1, 2}, 1);
        select.select(new int[]{1}, 0);
        assertEquals(0, select.comparisons);
        assertEquals(1, select.maxDepth);
        ClosestPairSolver closest = new ClosestPairSolver();
        closest.findClosestPair(new Point[]{new Point(0, 0), new Point(3, 4)});
        assertEquals(1, closest.comparisons);
        closest.findClosestPair(new Point[0]);
        assertEquals(0, closest.comparisons);
        assertEquals(0, closest.maxDepth);
    }

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
