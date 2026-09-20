# Assignment 1: Divide-and-Conquer Algorithm Analysis

## Student
* **Name:** Mardan
* **Group:** SE-2527

---

## Project Overview

This project implements and analyzes four classic divide-and-conquer algorithms in Java:
1. **MergeSort**
2. **QuickSort**
3. **Deterministic Select (Median-of-Medians)**
4. **Closest Pair of Points**

The primary objective is to investigate how divide-and-conquer paradigms perform empirically, measure their time and space complexities, and compare theoretical asymptotic limits against real-world performance metrics.

The evaluation suite measures execution time (in nanoseconds via `System.nanoTime()`), maximum recursion stack depth, and total element comparisons across four input distributions: **random**, **sorted**, **reverse-sorted**, and **duplicate-heavy**. All raw experimental data is generated and saved to `results/results.csv`.

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
│   ├── execution_time.png
│   └── recursion_depth.png
├── docs/
│   └── screenshots/
│       └── main_output.png
├── results/
│   └── results.csv
├── README.md
├── pom.xml
└── .gitignore
Algorithm AnalysisMergeSort AnalysisMechanism: Divides array into two equal halves recursively and merges sorted subarrays using an auxiliary array.Recurrence: $T(n) = 2T(n/2) + O(n)$Complexity: Time $\Theta(n \log n)$, Auxiliary Space $O(n)$QuickSort AnalysisMechanism: Selects a pivot element, partitions the array in-place, and recurses on smaller sub-problems.Recurrence: $T(n) = 2T(n/2) + O(n)$ (Average Case)Complexity: Average Time $O(n \log n)$, Worst-case Time $O(n^2)$, Stack Space $O(\log n)$Deterministic Select (Median-of-Medians)Mechanism: Splits elements into groups of 5, finds group medians recursively to pick a guaranteed pivot, and recurses only into the partition containing the target index $k$.Recurrence: $T(n) \le T(\lceil n/5 \rceil) + T(7n/10 + 6) + O(n)$Complexity: Worst-case Time $O(n)$, Stack Space $O(\log n)$Closest Pair of PointsMechanism: Sorts points by X-coordinate, splits points into halves, finds minimum distance $d = \min(d_L, d_R)$, and scans points within a $2d$ central strip sorted by Y-coordinate.Recurrence: $T(n) = 2T(n/2) + O(n)$Complexity: Time $\Theta(n \log n)$, Space $O(n)$Empirical Results (Random Input Distribution)The experimental metrics extracted directly from results/results.csv for Random inputs are presented below.Execution Time (Nanoseconds)AlgorithmN=100N=1,000N=5,000N=10,000MergeSort94,800 ns506,600 ns592,100 ns6,719,900 nsQuickSort333,600 ns137,500 ns418,100 ns772,000 nsDeterministic Select97,200 ns464,400 ns593,500 ns440,600 nsClosest Pair7,091,100 ns3,303,900 ns14,184,500 ns12,995,400 nsMaximum Recursion DepthAlgorithmN=100N=1,000N=5,000N=10,000Theoretical BoundMergeSort581011$\lceil \log_2 n \rceil$QuickSort46910$O(\log n)$Deterministic Select7101213$O(\log n)$Closest Pair7101213$O(\log n)$Comparisons Count (Random Distribution)AlgorithmN=100N=1,000N=5,000N=10,000MergeSort5549,11258,250126,890QuickSort65311,25277,333159,171Deterministic Select8239,67346,480100,130Closest Pair7,90114,45385,538183,738Performance GraphsExecution Time vs Input SizeRecursion Depth vs Input SizeDiscussion & AnalysisTheoretical vs Practical Performance:MergeSort & QuickSort: Both algorithms exhibit standard $O(n \log n)$ asymptotic scaling. QuickSort achieves significantly lower execution times on larger inputs ($772\,\text{µs}$ vs $6.7\,\text{ms}$ at $N=10,000$) due to superior cache locality and in-place partitioning.Deterministic Select: Shows linear time scaling ($O(n)$) with minimal comparison counts ($100,130$ at $N=10,000$), matching theoretical bounds.Closest Pair: Incurs higher initial constant overhead ($12.9\,\text{ms}$ at $N=10,000$) due to point object allocations and strip sorting along the Y-axis.Impact of Input Distributions:Sorted & Reverse-Sorted Inputs: QuickSort maintains efficient performance without degenerating to $O(n^2)$ because balanced partitioning strategies prevent worst-case recursion depths.Duplicate-Heavy Inputs: Deterministic Select maintains stable performance under duplicate-heavy datasets as identical elements partition efficiently.Recursion Depth Bounding:Recursion depth across all four algorithms scales logarithmically relative to input size $N$. For example, MergeSort reaches a depth of exactly $11$ at $N=10,000$, well within $\lceil \log_2(10000) \rceil = 14$, ensuring memory safety against stack overflow.Program Execution VerificationTerminal Run Output