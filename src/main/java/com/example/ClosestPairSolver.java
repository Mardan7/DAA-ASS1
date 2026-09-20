package com.example;

import java.util.Arrays;

public class ClosestPairSolver {
    public int maxDepth = 0;
    public long comparisons = 0;

    public double findClosestPair(Point[] points) {
        maxDepth = 0;
        comparisons = 0;
        if (points == null || points.length < 2) return Double.POSITIVE_INFINITY;

        Point[] ptsX = points.clone();
        Arrays.sort(ptsX, (p1, p2) -> Double.compare(p1.x, p2.x));

        Point[] ptsY = points.clone();
        Arrays.sort(ptsY, (p1, p2) -> Double.compare(p1.y, p2.y));

        return closestPairRec(ptsX, ptsY, 1);
    }

    private double closestPairRec(Point[] ptsX, Point[] ptsY, int currentDepth) {
        if (currentDepth > maxDepth) maxDepth = currentDepth;

        int n = ptsX.length;
        if (n <= 3) {
            return bruteForce(ptsX);
        }

        int mid = n / 2;
        Point midPoint = ptsX[mid];

        Point[] leftX = Arrays.copyOfRange(ptsX, 0, mid);
        Point[] rightX = Arrays.copyOfRange(ptsX, mid, n);

        Point[] leftY = new Point[mid];
        Point[] rightY = new Point[n - mid];

        int lIdx = 0, rIdx = 0;
        for (Point p : ptsY) {
            if (p.x < midPoint.x || (p.x == midPoint.x && lIdx < mid && ptsX[lIdx].equals(p))) {
                if (lIdx < leftY.length) leftY[lIdx++] = p;
                else rightY[rIdx++] = p;
            } else {
                if (rIdx < rightY.length) rightY[rIdx++] = p;
                else leftY[lIdx++] = p;
            }
        }

        double dLeft = closestPairRec(leftX, leftY, currentDepth + 1);
        double dRight = closestPairRec(rightX, rightY, currentDepth + 1);
        double d = Math.min(dLeft, dRight);

        Point[] strip = new Point[n];
        int stripSize = 0;
        for (Point p : ptsY) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip[stripSize++] = p;
            }
        }

        return Math.min(d, stripClosest(strip, stripSize, d));
    }

    private double stripClosest(Point[] strip, int size, double d) {
        double min = d;
        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size && (strip[j].y - strip[i].y) < min; ++j) {
                comparisons++;
                double dist = strip[i].distanceTo(strip[j]);
                if (dist < min) {
                    min = dist;
                }
            }
        }
        return min;
    }

    public static double bruteForce(Point[] points) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < min) min = dist;
            }
        }
        return min;
    }
}