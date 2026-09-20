Assignment 1: Divide-and-Conquer Algorithm Analysis
Student

Name: Mardan
Group: SE-2527

Project Overview

This project implements and analyzes four classic Divide-and-Conquer algorithms in Java:

MergeSort
QuickSort
Deterministic Select (Median-of-Medians)
Closest Pair of Points

The main objective of this project is to investigate the theoretical and practical performance of divide-and-conquer algorithms.

The experiments measure:

execution time;
recursion depth;
number of element comparisons;
algorithm behavior for different input distributions.

The experiments use four types of input data:

Random
Sorted
Reverse-sorted
Duplicate-heavy

The raw experimental results are stored in:

results/results.csv

The project also contains generated graphs for comparing execution time and recursion depth.


DAA-ass1/
│
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
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── AlgorithmTest.java
│
├── plots/
│   ├── depth_vs_n.png
│   └── time_vs_n.png
│
├── docs/
│   └── screenshots/
│       └── main_output.png
│
├── results/
│   └── results.csv
│
├── README.md
├── pom.xml
└── .gitignore

Algorithm Analysis
1. MergeSort
Mechanism

MergeSort divides the input array into two approximately equal halves. Each half is recursively sorted, and the two sorted subarrays are then merged into one sorted array.

Recurrence

T(n) = 2T(n/2) + O(n)

Complexity
| Case    | Time Complexity |
| ------- | --------------- |
| Best    | O(n log n)      |
| Average | O(n log n)      |
| Worst   | O(n log n)      |
Auxiliary Space: O(n)

MergeSort provides predictable performance because its running time does not depend strongly on the initial ordering of the input.

2. QuickSort
Mechanism

QuickSort selects a pivot element and partitions the array around the pivot. Elements smaller than the pivot are placed on one side, while larger elements are placed on the other side. The two partitions are then processed recursively.

Recurrence

For a balanced partition:

T(n) = 2T(n/2) + O(n)

Complexity
| Case    | Time Complexity |
| ------- | --------------- |
| Best    | O(n log n)      |
| Average | O(n log n)      |
| Worst   | O(n²)           |
Average Stack Space: O(log n)

QuickSort is performed in-place, which reduces additional memory usage compared with MergeSort.

3. Deterministic Select — Median-of-Medians
Mechanism

The Deterministic Select algorithm finds the k-th smallest element.

The algorithm:

Divides the elements into groups of five.
Finds the median of each group.
Recursively finds the median of those medians.
Uses this value as a guaranteed pivot.
Partitions the input around the pivot.
Recursively continues only in the partition containing the required element.
Recurrence

T(n) ≤ T(n/5) + T(7n/10) + O(n)


Complexity

Worst-case Time: O(n)

Stack Space: O(log n)

Unlike ordinary QuickSelect, the Median-of-Medians method provides a guaranteed linear worst-case running time.

4. Closest Pair of Points
Mechanism

The Closest Pair algorithm finds the two points with the smallest Euclidean distance.

The algorithm:

Sorts points by their x coordinate.
Divides the points into two halves.
Recursively finds the closest pair in each half.
Computes:

d = min(dL, dR)

Builds a central strip containing points within distance d from the dividing line.
Sorts/processes the strip according to the y coordinate.
Checks the relevant neighboring points in the strip.


Recurrence
T(n) = 2T(n/2) + O(n)
Complexity

Time: O(n log n)

Space: O(n)

Experimental Setup

The algorithms were tested using four different input distributions:

Input Type	Description
Random	Randomly generated input values
Sorted	Elements arranged in ascending order
Reverse-sorted	Elements arranged in descending order
Duplicate-heavy	Input containing many repeated values

The following input sizes were used:

N = 100
N = 1,000
N = 5,000
N = 10,000

The experiment measures three main metrics:

Execution time
Maximum recursion depth
Number of element comparisons

Execution time was measured using Java's:

System.nanoTime()

All raw results were saved to:

results/results.csv
Experimental Results
Execution Time — Random Input

The following results were obtained for randomly generated inputs.

| Algorithm            |      N = 100 |    N = 1,000 |     N = 5,000 |    N = 10,000 |
| -------------------- | -----------: | -----------: | ------------: | ------------: |
| MergeSort            |    94,800 ns |   506,600 ns |    592,100 ns |  6,719,900 ns |
| QuickSort            |   333,600 ns |   137,500 ns |    418,100 ns |    772,000 ns |
| Deterministic Select |    97,200 ns |   464,400 ns |    593,500 ns |    440,600 ns |
| Closest Pair         | 7,091,100 ns | 3,303,900 ns | 14,184,500 ns | 12,995,400 ns |

Execution time can vary between runs because of JVM warm-up, system load, memory allocation, and other runtime factors.

Recursion Depth

The measured recursion depths for the random input were:


| Algorithm            | N = 100 | N = 1,000 | N = 5,000 | N = 10,000 |
| -------------------- | ------: | --------: | --------: | ---------: |
| MergeSort            |       5 |         8 |        10 |         11 |
| QuickSort            |       4 |         6 |         9 |         10 |
| Deterministic Select |       7 |        10 |        12 |         13 |
| Closest Pair         |       7 |        10 |        12 |         13 |

For MergeSort, the measured depth at N = 10,000 is 11, while:

ceil(log2(10000)) = 14

This demonstrates logarithmic growth of recursion depth for the tested inputs.

Element Comparisons

| Algorithm            | N = 100 | N = 1,000 | N = 5,000 | N = 10,000 |
| -------------------- | ------: | --------: | --------: | ---------: |
| MergeSort            |     554 |     9,112 |    58,250 |    126,890 |
| QuickSort            |     653 |    11,252 |    77,333 |    159,171 |
| Deterministic Select |     823 |     9,673 |    46,480 |    100,130 |
| Closest Pair         |   7,901 |    14,453 |    85,538 |    183,738 |

The comparison counts provide another way to evaluate the behavior of the algorithms beyond execution time.

Performance Graphs
Execution Time vs Input Size

The graph compares the execution time of the four algorithms as the input size increases.

Recursion Depth vs Input Size

The graph demonstrates how recursion depth changes as the input size increases.

Discussion
Theoretical vs Practical Performance

The theoretical complexity gives an asymptotic description of algorithm behavior, while the experimental results show how the implementations behave on actual hardware and with the selected input sizes.

MergeSort and QuickSort

Both algorithms have O(n log n) average/typical scaling under balanced recursion.

In the provided random-input experiment, QuickSort has a lower measured execution time at N = 10,000:

QuickSort: 772,000 ns
MergeSort: 6,719,900 ns

One reason is that QuickSort performs partitioning in-place, which can reduce memory operations and improve cache behavior.

However, QuickSort has a theoretical worst-case complexity of O(n²), while MergeSort maintains O(n log n) time complexity.

Deterministic Select

Deterministic Select demonstrates the expected linear-time behavior of the Median-of-Medians approach.

At N = 10,000, the measured number of comparisons was:

100,130

The algorithm also maintained relatively small recursion depth.

Its main theoretical advantage is that the pivot selection guarantees a sufficiently balanced partition, providing O(n) worst-case time complexity.

Closest Pair

The Closest Pair implementation has higher measured execution times than the sorting and selection algorithms.

At N = 10,000:

Closest Pair: 12,995,400 ns

This additional cost can be associated with operations specific to the problem, including point processing, object handling, sorting, and processing the central strip.

Impact of Input Distribution

The experiment considers four different input distributions:

Random Input

Random data provides a general case for observing algorithm behavior without a predetermined ordering.

Sorted Input

Sorted data can affect algorithms such as QuickSort depending on the pivot-selection strategy.

Reverse-Sorted Input

Reverse ordering provides another structured input case and can expose unfavorable partitioning behavior for some QuickSort implementations.

Duplicate-Heavy Input

Duplicate-heavy input is useful for evaluating how algorithms handle many equal values.

The experimental results allow the behavior of the algorithms under these different distributions to be compared using the generated CSV data.

Recursion Depth Analysis

The measured recursion depth generally increases logarithmically as the input size grows.

For example, MergeSort produced:
|      N | Recursion Depth |
| -----: | --------------: |
|    100 |               5 |
|  1,000 |               8 |
|  5,000 |              10 |
| 10,000 |              11 |

The theoretical depth of a balanced divide-and-conquer algorithm is related to:

O(log n)

Therefore, the measured results are consistent with the expected logarithmic recursion behavior for the tested cases.

Program Execution Verification

The project includes program output demonstrating the execution of the algorithms and collection of experimental data.

Main Program Output

The program executes the experimental suite and records the resulting measurements.

Automated Tests

Unit tests are located in:

src/test/java/com/example/AlgorithmTest.java

The tests are used to verify the correctness of the implemented algorithms.

The project uses Maven for build and test management.

To run the tests:

mvn test

To build the project:

mvn clean package
Results Storage

All experimental measurements are stored in CSV format:

results/results.csv

This makes it possible to reproduce the graphs and perform additional analysis without manually entering the experimental results.

Conclusion

This project demonstrates the practical implementation and analysis of four divide-and-conquer algorithms:

MergeSort
QuickSort
Deterministic Select
Closest Pair of Points

The experiments show that theoretical complexity provides a useful model for algorithm behavior, while actual execution time is also affected by implementation details, memory usage, object allocation, partitioning strategy, and the Java runtime environment.

The recursion-depth measurements generally demonstrate logarithmic growth for the tested algorithms and input sizes.

The experimental data, graphs, source code, tests, and execution screenshots are included in the repository to provide a complete overview of the implementation and analysis.

Technologies
Java
Maven
JUnit
Git
GitHub
CSV
Java System.nanoTime()
Repository Contents

| Directory/File      | Purpose                       |
| ------------------- | ----------------------------- |
| `src/main/java/`    | Algorithm implementations     |
| `src/test/java/`    | Automated tests               |
| `results/`          | Experimental CSV data         |
| `plots/`            | Generated graphs              |
| `docs/screenshots/` | Program execution screenshots |
| `pom.xml`           | Maven configuration           |
| `README.md`         | Project documentation         |







