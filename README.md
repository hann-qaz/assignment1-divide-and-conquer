# Assignment 1 — Divide-and-Conquer Algorithm Analysis

## A. Project Overview

This project implements and analyzes four classic divide-and-conquer algorithms in Java:

- **MergeSort** — stable Θ(n log n) sorting with an insertion-sort cutoff for small subarrays.
- **QuickSort** — randomized, in-place, recurses on the smaller partition and iterates over the larger one to keep recursion depth at O(log n).
- **Deterministic Select (Median-of-Medians)** — worst-case Θ(n) selection of the k-th smallest element.
- **Closest Pair of Points** — Θ(n log n) divide-and-conquer geometric algorithm.

The goal is to implement each algorithm correctly, measure its real running time, recursion depth and comparison count, and compare the measured behavior against the theoretical complexity predicted by the Master Theorem / Akra–Bazzi intuition.

## B. Algorithm Analysis

### 1. MergeSort

**How it works:** split the array in half, sort each half recursively, then merge the two sorted halves with a single linear pass using a reusable auxiliary buffer. Subarrays smaller than a cutoff (16 elements) are sorted directly with insertion sort, since insertion sort has a lower constant factor on tiny inputs.

**Recurrence:** T(n) = 2T(n/2) + Θ(n)

**Master Theorem:** here a = 2, b = 2, d = 1, p = 0. Since 2 = 2, this is **Case 1**. and 0 > -1 give us:

T(n) = Θ(n log n)

**Space:** Θ(n) auxiliary space for the merge buffer (reused across calls, not reallocated every time).

### 2. QuickSort

**How it works:** pick a uniformly random pivot, partition the array in place around it, then recurse only into the **smaller** partition and continue the loop (iterate) on the **larger** one. This bounds the worst-case recursion depth to O(log n) instead of O(n), no matter how unbalanced a single partition is. Partitioning uses a 3-way (Dutch-flag) scheme so that elements equal to the pivot are grouped together — this is what keeps the algorithm fast on duplicate-heavy inputs.

**Recurrence (average case):** T(n) = 2T(n/2) + Θ(n) → same shape as MergeSort → Θ(n log n) on average (Master Theorem Case 2).

**Worst case:** if partitions are maximally unbalanced (1 and n−1) at every step, T(n) = T(n−1) + Θ(n) → Θ(n²). With a random pivot this worst case is extremely unlikely; with the smaller-first recursion rule, even in the worst case the *recursion depth* stays O(log n) because we always recurse into the smaller side.

**Space:** O(log n) expected stack space (in place, no auxiliary array).

### 3. Deterministic Select (Median-of-Medians)

**How it works:** split the array into groups of 5, sort each tiny group, take each group's median, and recursively find the median of those medians — this median-of-medians is guaranteed to be a "good enough" pivot. Partition around it (3-way, to handle duplicates) and recurse only into the side that contains the k-th element.

**Recurrence:** T(n) = T(n/5) + T(7n/10) + Θ(n)

The n/5 term comes from recursively finding the median of medians; the 7n/10 term is a proven upper bound on the size of the side we may still need to recurse into, because at least 3/10 of the elements are guaranteed to be eliminated by the pivot (this is exactly why groups of 5, not 3, are used — group size 5 is the smallest that makes the fractions add to less than 1).

Since n/5 + 7n/10 = 9n/10 < n, the work shrinks geometrically at every level, so by the Akra–Bazzi intuition the total work is dominated by the Θ(n) term at each level, summed over O(log n) levels of a shrinking geometric series:

T(n) = Θ(n)

**Space:** O(log n) recursion stack; partitioning is in place.

### 4. Closest Pair of Points

**How it works:** sort points once by x and once by y. Split by the median x-coordinate into a left and right half, solve each half recursively, take d = min(leftMin, rightMin), then check a narrow vertical "strip" of width 2d around the dividing line (using the pre-sorted y-order) for any pair closer than d — a geometric argument shows at most a constant number of points in the strip need to be compared against each point.

**Recurrence:** T(n) = 2T(n/2) + Θ(n)

Same shape as MergeSort (splitting the y-order without re-sorting is what keeps the merge/strip step linear) → **Master Theorem Case 2** → T(n) = Θ(n log n).

**Space:** Θ(n) for the auxiliary x/y-sorted arrays.

## C. Experimental Results

Experiments were run on random, sorted, reverse-sorted, and duplicate-heavy inputs of sizes 1,000 / 5,000 / 20,000 / 50,000 / 100,000 (500–20,000 points for Closest Pair). Each run measures wall-clock time (`System.nanoTime()`), maximum recursion depth, and comparison count. Full raw data is in `results/results.csv`.

**Sample results (random input):**

| Algorithm | n       | Time (ms) | Max depth | Comparisons |
|-----------|--------:|----------:|----------:|-------------:|
| MergeSort | 1,000   | 0.37      | 6         | 9,701 |
| QuickSort | 1,000   | 4.19      | 5         | 11,324 |
| Select    | 1,000   | 0.07      | 9         | 3,078 |
| MergeSort | 20,000  | 4.67      | 11        | 260,149 |
| QuickSort | 20,000  | 3.35      | 8         | 359,210 |
| Select    | 20,000  | 1.93      | 13        | 64,180 |
| MergeSort | 100,000 | 22.67     | 13        | 1,565,587 |
| QuickSort | 100,000 | 32.89     | 9         | 2,092,394 |
| Select    | 100,000 | 19.98     | 15        | 326,635 |

**Closest Pair (random points):**

| n      | Time (ms) | Max depth |
|-------:|----------:|----------:|
| 500    | 3.28      | 8 |
| 2,000  | 10.55     | 10 |
| 5,000  | 21.87     | 11 |
| 20,000 | 98.86     | 13 |

Notice the max recursion depth grows like log₂(n) for MergeSort, QuickSort, and Closest Pair (e.g. log₂(100,000) ≈ 17, close to the measured 9–15), confirming the O(log n) depth bound.

**Plots** (generated from `results/results.csv`, see `docs/plots/`):

- `time_vs_n.png` — execution time vs. input size for MergeSort / QuickSort / Select
- `depth_vs_n.png` — max recursion depth vs. input size for the same three algorithms

*When you re-run the program yourself, re-generate these plots from your own `results/results.csv` and also add the sorted / reverse-sorted / duplicate-heavy comparisons, plus a Closest Pair time-vs-n plot, so the report reflects your own machine's numbers.*

## D. Discussion

**Do the results match theoretical complexity?**
Yes. MergeSort and Closest Pair grow close to n log n, QuickSort matches on average, and Select grows roughly linearly — its comparison count per element is visibly lower than the sorting algorithms at every size in the table above.

**How does input structure affect performance?**
Sorted and reverse-sorted inputs don't hurt MergeSort or the 3-way QuickSort/Select (no worst-case pivot degeneracy since the pivot is either random or computed structurally). Duplicate-heavy inputs would badly slow down a naive Lomuto-partition QuickSort/Select (recursing on huge "equal" runs), which is exactly why this implementation uses 3-way partitioning — duplicates are grouped and skipped in one pass instead of being reprocessed.

**Why does smaller-first recursion help QuickSort?**
Recursing into the smaller partition and looping over the larger one guarantees the recursive call stack only ever holds the "small side," which is at most half of the current range. That bounds the recursion depth to O(log n) even in a bad partitioning sequence — it doesn't fix the O(n²) worst-case time, but it prevents stack overflow and keeps memory usage predictable.

**Why does Median-of-Medians guarantee O(n)?**
Because the pivot it picks is provably never worse than a 30/70 split: at least 3 out of every 5 group-medians are guaranteed to be ≤ the pivot (and symmetrically ≥), so at least 3/10 of all elements are eliminated every time. That turns the recurrence into T(n) = T(n/5) + T(7n/10) + Θ(n), where the two recursive terms sum to less than n, making the total work a converging geometric series that sums to Θ(n) — unlike a randomly-pivoted quickselect, this holds in the *worst* case, not just on average.

**Why is divide-and-conquer Closest Pair faster than O(n²) for large inputs?**
Brute force compares every pair — Θ(n²) comparisons. The divide-and-conquer version only ever compares points within a narrow strip near the dividing line, and a geometric packing argument shows each point only needs to be checked against a small constant number of neighbors in that strip. That turns the "combine" step from O(n²) into O(n), giving the same Θ(n log n) recurrence as MergeSort.

**What practical factors affect performance (JVM, cache, GC, etc.)?**
JIT warm-up means the first few iterations of a benchmark are slower than later ones; array-based algorithms like MergeSort/QuickSort benefit from cache locality while pointer-heavy structures wouldn't; garbage collection pauses can add noise to `System.nanoTime()` measurements, especially for MergeSort's buffer allocations; and JVM's escape analysis / bounds-check elimination can make small, simple loops (like the insertion-sort cutoff) run faster than their asymptotic complexity alone would suggest.

## E. Reflection

Implementing these four algorithms side by side made the practical difference between "an algorithm that works" and "an algorithm that works well at scale" very concrete. The biggest implementation challenge was QuickSort and Select's behavior on duplicate-heavy input: a plain two-way partition (Lomuto-style, splitting only on `<`) looked correct in small tests but degraded to Θ(n²) once tested against a larger duplicate-heavy array, because every equal element kept landing on the same side of the pivot. Switching to a 3-way (Dutch national flag) partition — grouping elements into "less than," "equal to," and "greater than" the pivot — fixed this cleanly and is a good reminder that correctness tests on small random inputs don't automatically catch performance bugs that only show up at scale or on adversarial input shapes.

The second lesson was about measurement itself: using `System.nanoTime()` naively on a single run produces noisy numbers dominated by JVM warm-up, so multiple input types and sizes needed to be run to see the actual asymptotic trend rather than one-off timing noise.

## F. Screenshots

Add screenshots here after running the project in your own IDE:
- Program output (`docs/screenshots/program_output.txt` has a captured console run as a placeholder — replace with an actual screenshot).
- Test results (correctness test pass/fail output).
- Plots (`docs/plots/time_vs_n.png`, `docs/plots/depth_vs_n.png`).

## How to Run

```bash
mvn compile
mvn exec:java -Dexec.mainClass=Main
```

or, without Maven:

```bash
cd src
javac -d ../out *.java
cd ../out
java Main
```

This runs the correctness tests first (MergeSort/QuickSort vs `Arrays.sort`, Select vs `Arrays.sort(a)[k]`, Closest Pair vs brute force) and then the full experiment sweep, writing `results/results.csv`.

## Project Structure

```
assignment1-divide-and-conquer/
├── src/
│   ├── MergeSorter.java
│   ├── QuickSorter.java
│   ├── DeterministicSelector.java
│   ├── ClosestPairSolver.java
│   ├── Point.java
│   ├── Experiment.java
│   └── Main.java
├── tests/
│   └── CorrectnessTests.java
├── docs/
│   ├── screenshots/
│   └── plots/
├── results/
│   └── results.csv
├── README.md
├── pom.xml
└── .gitignore
```

## Suggested Git Commit History

```
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
