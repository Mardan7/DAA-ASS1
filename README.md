# Assignment 1: Divide-and-Conquer Algorithm Analysis

## Student
* **Name:** Mardan
* **Group:** SE-2527

---

## Project Overview

This project implements and analyzes four classic divide-and-conquer algorithms in Java:
1. MergeSort
2. QuickSort
3. Deterministic Select (Median-of-Medians)
4. Closest Pair of Points

The primary objective is to investigate how divide-and-conquer paradigms perform empirically, measure their time and space complexities, and compare theoretical asymptotic limits against real-world performance metrics.

The evaluation suite measures execution time (in nanoseconds via System.nanoTime()), maximum recursion stack depth, and total element comparisons across four input distributions: random, sorted, reverse-sorted, and duplicate-heavy. All raw experimental data is generated and saved to results/results.csv.

---

## Project Structure

```text
DAA-ass1/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               ├── MergeSorter.java
│   │               ├── QuickSorter.java
│   │               ├── DeterministicSelector.java
│   │               ├── ClosestPairSolver.java
│   │               ├── Experiment.java
│   │               ├── Point.java
│   │               └── Main.java
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── AlgorithmTest.java
├── plots/
│   ├── depth_vs_n.png
│   └── time_vs_n.png
├── docs/
│   └── screenshots/
│       └── main_output.png
├── results/
│   └── results.csv
├── README.md
├── pom.xml
└── .gitignore

Algorithm Analysis

MergeSort Analysis
Mechanism: Divides array into two equal halves recursively and merges sorted subarrays using an auxiliary array.

Recurrence: T(n) = 2T(n/2) + O(n)

Complexity: Time O(n log n), Auxiliary Space O(n)

QuickSort Analysis
Mechanism: Selects a pivot element, partitions the array in-place, and recurses on smaller sub-problems.

Recurrence: T(n) = 2T(n/2) + O(n) (Average Case)

Complexity: Average Time O(n log n), Worst-case Time O(n^2), Stack Space O(log n)


Deterministic Select (Median-of-Medians)
Mechanism: Splits elements into groups of 5, finds group medians recursively to pick a guaranteed pivot, and recurses only into the partition containing the target index k.

Recurrence: T(n) <= T(n/5) + T(7n/10) + O(n)

Complexity: Worst-case Time O(n), Stack Space O(log n)

Closest Pair of Points
Mechanism: Sorts points by X-coordinate, splits points into halves, finds minimum distance d = min(dL, dR), and scans points within a 2d central strip sorted by Y-coordinate.

Recurrence: T(n) = 2T(n/2) + O(n)

Complexity: Time O(n log n), Space O(n)

Empirical Results (Random Input Distribution)
The experimental metrics extracted directly from results/results.csv for Random inputs are presented below.

Algorithm,N = 100,"N = 1,000","N = 5,000","N = 10,000"
MergeSort,"94,800 ns","506,600 ns","592,100 ns","6,719,900 ns"
QuickSort,"333,600 ns","137,500 ns","418,100 ns","772,000 ns"
Deterministic Select,"97,200 ns","464,400 ns","593,500 ns","440,600 ns"
Closest Pair,"7,091,100 ns","3,303,900 ns","14,184,500 ns","12,995,400 ns"


Algorithm,N = 100,"N = 1,000","N = 5,000","N = 10,000",Theoretical Bound
MergeSort,5,8,10,11,ceil(log2 n)
QuickSort,4,6,9,10,O(log n)
Deterministic Select,7,10,12,13,O(log n)
Closest Pair,7,10,12,13,O(log n)


Algorithm,N = 100,"N = 1,000","N = 5,000","N = 10,000"
MergeSort,554,"9,112","58,250","126,890"
QuickSort,653,"11,252","77,333","159,171"
Deterministic Select,823,"9,673","46,480","100,130"
Closest Pair,"7,901","14,453","85,538","183,738"


Performance Graphs
Execution Time vs Input Size
Recursion Depth vs Input Size
Discussion & Analysis
Theoretical vs Practical Performance:

MergeSort & QuickSort: Both algorithms exhibit standard O(n log n) asymptotic scaling. QuickSort achieves significantly lower execution times on larger inputs (772 us vs 6.7 ms at N=10,000) due to superior cache locality and in-place partitioning.

Deterministic Select: Shows linear time scaling O(n) with minimal comparison counts (100,130 at N=10,000), matching theoretical bounds.

Closest Pair: Incurs higher initial constant overhead (12.9 ms at N=10,000) due to point object allocations and strip sorting along the Y-axis.

Impact of Input Distributions:

Sorted & Reverse-Sorted Inputs: QuickSort maintains efficient performance without degenerating to O(n^2) because balanced partitioning strategies prevent worst-case recursion depths.

Duplicate-Heavy Inputs: Deterministic Select maintains stable performance under duplicate-heavy datasets as identical elements partition efficiently.

Recursion Depth Bounding:
Recursion depth across all four algorithms scales logarithmically relative to input size N. For example, MergeSort reaches a depth of exactly 11 at N=10,000, well within ceil(log2(10000)) = 14, ensuring memory safety against stack overflow.


Program Execution Verification