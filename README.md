# Assignment 1: Divide-and-Conquer Algorithm Analysis

## 1. Project Overview

This project implements and analyzes four classic divide-and-conquer algorithms in Java:

1. MergeSort
2. QuickSort
3. Deterministic Select (Median-of-Medians)
4. Closest Pair of Points

The purpose of the assignment is to study divide-and-conquer algorithms, analyze their theoretical complexity, measure practical performance, and compare experimental results with theoretical expectations.

---

## 2. Project Structure

```text
assignment1-divide-and-conquer/
├── src/
│   ├── MergeSorter.java
│   ├── QuickSorter.java
│   ├── DeterministicSelector.java
│   ├── ClosestPairSolver.java
│   ├── Experiment.java
│   ├── Point.java
│   └── Main.java
├── tests/
├── docs/
│   ├── screenshots/
│   └── plots/
├── results/
│   └── results.csv
├── README.md
├── pom.xml
└── .gitignore
```

---

# 3. Algorithms

## 3.1 MergeSort

MergeSort divides the array into two halves, recursively sorts both halves, and merges them.

The implementation uses a linear merge, a reusable auxiliary buffer, and a small-input cutoff using Insertion Sort.

### Complexity

- Best case: `Θ(n log n)`
- Average case: `Θ(n log n)`
- Worst case: `Θ(n log n)`
- Auxiliary space: `O(n)`

### Recurrence

```text
T(n) = 2T(n/2) + Θ(n)
```

Using the Master Theorem:

```text
T(n) = Θ(n log n)
```

---

## 3.2 QuickSort

QuickSort chooses a pivot, partitions the array around it, and sorts the partitions.

The implementation uses a randomized pivot, in-place partitioning, and recursively processes the smaller partition while iterating over the larger one.

### Complexity

- Best case: `Θ(n log n)`
- Average/typical case: `Θ(n log n)`
- Worst case: `O(n²)`
- Recursion depth with smaller-first recursion: typically `O(log n)`

### Recurrence

For balanced partitions:

```text
T(n) = 2T(n/2) + Θ(n)
```

Therefore:

```text
T(n) = Θ(n log n)
```

For a highly unbalanced partition:

```text
T(n) = T(n - 1) + Θ(n)
```

which gives:

```text
T(n) = O(n²)
```

---

## 3.3 Deterministic Select (Median-of-Medians)

Deterministic Select finds the `k`-th smallest element without fully sorting the array.

The algorithm divides the elements into groups of five, finds the median of each group, recursively finds the median of those medians, and uses it as the pivot.

Only the partition containing the required element is processed recursively.

### Complexity

- Worst case: `Θ(n)`

### Recurrence intuition

```text
T(n) = T(n/5) + T(7n/10) + Θ(n)
```

This recurrence gives:

```text
T(n) = Θ(n)
```

The median-of-medians pivot guarantees that a sufficiently large part of the input is discarded at every step.

---

## 3.4 Closest Pair of Points

The Closest Pair algorithm finds the two points with the smallest Euclidean distance.

The algorithm:

1. Sorts points by x-coordinate.
2. Divides them into two halves.
3. Recursively finds the closest pair in each half.
4. Builds a strip around the dividing line.
5. Checks relevant points in y-order.
6. Returns the smallest distance.

### Complexity

```text
Θ(n log n)
```

### Recurrence

```text
T(n) = 2T(n/2) + Θ(n)
```

By the Master Theorem:

```text
T(n) = Θ(n log n)
```

---

# 4. Experimental Methodology

The program measures execution time using:

```java
System.nanoTime()
```

Multiple input sizes are tested:

- small
- medium
- large

Sorting algorithms are tested with:

- random input
- sorted input
- reverse-sorted input
- duplicate-heavy input

The following metrics are measured:

- execution time
- maximum recursion depth
- at least one additional metric such as comparisons, swaps, recursive calls, or allocations

Experimental results are saved in:

```text
results/results.csv
```

---

# 5. Correctness Testing

## Sorting

MergeSort and QuickSort are compared with:

```java
Arrays.sort()
```

The tests include:

- random arrays
- sorted arrays
- reverse-sorted arrays
- duplicate-heavy arrays
- empty arrays
- single-element arrays

## Deterministic Select

At least 100 random tests are performed.

The result is compared with:

```java
Arrays.sort(a)[k]
```

## Closest Pair

For small datasets (`n <= 2000`), the divide-and-conquer result is compared with an `O(n²)` brute-force solution.

---

# 6. Experimental Results

Detailed measurements are stored in:

```text
results/results.csv
```

The project includes the required plots:

### Time vs. n

```text
docs/plots/time_vs_n.png
```

This plot shows how execution time changes as the input size increases.

### Recursion Depth vs. n

```text
docs/plots/recursion_depth_vs_n.png
```

This plot shows how maximum recursion depth changes with input size.

---

# 7. Discussion

## Do the results match theoretical complexity?

The experiments can be compared with the expected asymptotic behavior.

MergeSort and Closest Pair are expected to grow approximately according to `Θ(n log n)`.

Deterministic Select has a linear worst-case bound, although its implementation has larger constant factors than some simpler selection methods.

QuickSort normally performs efficiently when partitions are reasonably balanced, but highly unbalanced partitions can lead toward its `O(n²)` worst case.

Actual measurements can differ from asymptotic theory because real execution is affected by the JVM and hardware.

## How does input structure affect performance?

Input structure can affect QuickSort because different arrangements can lead to different partition sizes.

Randomized pivot selection reduces dependence on a fixed input arrangement.

Sorted, reverse-sorted, and duplicate-heavy inputs can produce different comparison and partition behavior.

MergeSort is less sensitive to input ordering because it follows the same divide-and-merge structure.

## Why does smaller-first recursion help QuickSort?

If QuickSort recursively processes the larger partition, the recursion stack can become unnecessarily deep.

Processing the smaller partition recursively and handling the larger partition iteratively limits the amount of active recursion.

This helps maintain low recursion depth and avoid stack overflow on difficult inputs.

## Why does Median-of-Medians guarantee O(n)?

The algorithm groups elements into groups of five and uses the median of medians as the pivot.

The pivot has a guaranteed quality: it cannot repeatedly be extremely close to an extreme element.

Therefore, every partition removes a guaranteed fraction of the input.

The recurrence

```text
T(n) = T(n/5) + T(7n/10) + Θ(n)
```

is linear, giving:

```text
T(n) = Θ(n)
```

## Why is divide-and-conquer Closest Pair faster than O(n²)?

The brute-force algorithm checks every pair of points, producing `O(n²)` work.

The divide-and-conquer algorithm divides the points into two halves and only examines a limited set of points around the dividing line.

Its complexity is:

```text
Θ(n log n)
```

This becomes increasingly important as the number of points grows.

## Practical factors affecting performance

Real execution time is affected by:

- JVM warm-up
- JIT compilation
- garbage collection
- CPU cache behavior
- memory allocation
- operating-system scheduling
- Java implementation details
- input generation
- random-number generation

Therefore, experimental timing should be interpreted together with theoretical complexity.

---

# 8. Reflection

This assignment helped me understand how divide-and-conquer algorithms can be implemented and analyzed in Java. I practiced MergeSort, randomized QuickSort, Median-of-Medians selection, and the Closest Pair of Points algorithm. I also learned how recurrence relations are connected to the running time of algorithms.

One of the main challenges was implementing the algorithms while measuring recursion depth and other performance metrics. Testing edge cases and comparing the implementations with reference or brute-force solutions helped verify correctness. The experiments also showed that theoretical complexity does not completely determine practical execution time because the JVM, memory, cache behavior, and other system factors can affect performance.

---

# 9. Screenshots

Screenshots of the program output, test results, and plots are stored in:

```text
docs/screenshots/
```

and

```text
docs/plots/
```

---

# 10. GitHub Workflow

The repository should contain a development history that reflects the actual implementation process.

Example stages:

```text
init: project structure and tests
feat(mergesort): implement merge sort
feat(quicksort): implement randomized quicksort
feat(select): implement median-of-medians
feat(closest): implement closest pair
feat(metrics): add performance measurements
feat(testing): add correctness tests
docs(report): add analysis and plots
fix: handle edge cases
release: v1.0
```

The commit history should reflect the actual development process.

---

# 11. Conclusion

This project demonstrates four divide-and-conquer algorithms and compares their theoretical properties with practical measurements.

| Algorithm | Expected Complexity |
|---|---|
| MergeSort | `Θ(n log n)` |
| QuickSort | Average `Θ(n log n)`, worst `O(n²)` |
| Deterministic Select | Worst-case `Θ(n)` |
| Closest Pair | `Θ(n log n)` |

The combination of implementation, correctness testing, performance measurement, plots, and analysis demonstrates how algorithm design affects both theoretical and practical performance.
