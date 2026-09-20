import java.util.Arrays;
import java.util.Random;

// Standalone version of the correctness checks that also run inside Main.
// Kept separate so the "tests" folder required by the assignment has real content.
public class CorrectnessTests {

    public static void main(String[] args) {
        testMergeSortVsArraysSort();
        testQuickSortVsArraysSort();
        testSelectVsArraysSort();
        testClosestPairVsBruteForce();
        testEdgeCases();
    }

    static void testMergeSortVsArraysSort() {
        Random rnd = new Random();
        for (int t = 0; t < 200; t++) {
            int n = rnd.nextInt(500);
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] actual = a.clone();
            MergeSorter.sort(actual);
            if (!Arrays.equals(expected, actual)) throw new AssertionError("MergeSort mismatch");
        }
        System.out.println("MergeSort matches Arrays.sort on 200 random tests");
    }

    static void testQuickSortVsArraysSort() {
        Random rnd = new Random();
        for (int t = 0; t < 200; t++) {
            int n = rnd.nextInt(500);
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] actual = a.clone();
            QuickSorter.sort(actual);
            if (!Arrays.equals(expected, actual)) throw new AssertionError("QuickSort mismatch");
        }
        System.out.println("QuickSort matches Arrays.sort on 200 random tests");
    }

    static void testSelectVsArraysSort() {
        Random rnd = new Random();
        for (int t = 0; t < 200; t++) {
            int n = rnd.nextInt(200) + 1;
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int k = rnd.nextInt(n);
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] actual = a.clone();
            int result = DeterministicSelector.select(actual, k);
            if (result != expected[k]) throw new AssertionError("Select mismatch");
        }
        System.out.println("DeterministicSelect matches Arrays.sort(a)[k] on 200 random tests");
    }

    static void testClosestPairVsBruteForce() {
        Random rnd = new Random();
        for (int t = 0; t < 100; t++) {
            int n = rnd.nextInt(200) + 2;
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);
            }
            double fast = ClosestPairSolver.closestPair(points);
            double brute = ClosestPairSolver.bruteForce(points);
            if (Math.abs(fast - brute) > 1e-6) throw new AssertionError("ClosestPair mismatch");
        }
        System.out.println("ClosestPair matches brute force on 100 random tests");
    }

    static void testEdgeCases() {
        int[] empty = {};
        MergeSorter.sort(empty);
        QuickSorter.sort(empty);

        int[] single = {42};
        MergeSorter.sort(single);
        QuickSorter.sort(single);
        if (single[0] != 42) throw new AssertionError("Single element sort failed");

        int[] allSame = new int[1000];
        Arrays.fill(allSame, 7);
        QuickSorter.sort(allSame);
        DeterministicSelector.select(allSame.clone(), 500);

        System.out.println("Edge cases (empty, single, all-duplicates) passed");
    }
}
