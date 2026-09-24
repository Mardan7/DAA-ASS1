package com.example;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    public int maxDepth = 0;
    /** Number of distance evaluations, including recursive base cases. */
    public long comparisons = 0;

    public double findClosestPair(Point[] points) {
        maxDepth = 0;
        comparisons = 0;
        if (points == null || points.length < 2) return Double.POSITIVE_INFINITY;
        Point[] sorted = points.clone();
        Arrays.sort(sorted, Comparator.comparingDouble((Point p) -> p.x).thenComparingDouble(p -> p.y));
        return closestPairRec(sorted, new Point[sorted.length], 0, sorted.length, 1);
    }

    // Each slice enters in x-order and returns in y-order. Splitting by index handles x ties.
    private double closestPairRec(Point[] points, Point[] buffer, int low, int high, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        if (high - low <= 3) {
            double best = Double.POSITIVE_INFINITY;
            for (int i = low; i < high; i++) {
                for (int j = i + 1; j < high; j++) {
                    comparisons++;
                    best = Math.min(best, points[i].distanceTo(points[j]));
                }
            }
            Arrays.sort(points, low, high, Comparator.comparingDouble(p -> p.y));
            return best;
        }
        int mid = low + (high - low) / 2;
        double midX = points[mid].x;
        double best = Math.min(closestPairRec(points, buffer, low, mid, depth + 1),
                closestPairRec(points, buffer, mid, high, depth + 1));

        int i = low, j = mid, out = low;
        while (i < mid && j < high) {
            buffer[out++] = points[i].y <= points[j].y ? points[i++] : points[j++];
        }
        while (i < mid) buffer[out++] = points[i++];
        while (j < high) buffer[out++] = points[j++];
        System.arraycopy(buffer, low, points, low, high - low);

        int count = 0;
        for (i = low; i < high; i++) {
            if (Math.abs(points[i].x - midX) < best) buffer[low + count++] = points[i];
        }
        for (i = low; i < low + count; i++) {
            // The packing bound requires at most the next seven y-neighbours.
            for (j = i + 1; j < low + count && j <= i + 7
                    && buffer[j].y - buffer[i].y < best; j++) {
                comparisons++;
                best = Math.min(best, buffer[i].distanceTo(buffer[j]));
            }
        }
        return best;
    }

    public static double bruteForce(Point[] points) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                best = Math.min(best, points[i].distanceTo(points[j]));
            }
        }
        return best;
    }
}