import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Correctness tests ===");
        testMergeSort();
        testQuickSort();
        testSelect();
        testClosestPair();

        System.out.println();
        System.out.println("=== Running experiments (this can take a minute) ===");
        Experiment.runAll("results/results.csv");
    }

    private static void testMergeSort() {
        Random rnd = new Random();
        for (int t = 0; t < 100; t++) {
            int n = rnd.nextInt(500);
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] actual = a.clone();
            MergeSorter.sort(actual);
            if (!Arrays.equals(expected, actual)) {
                System.out.println("MergeSort FAILED on test " + t);
                return;
            }
        }
        System.out.println("MergeSort: all 100 tests passed (vs Arrays.sort)");
    }

    private static void testQuickSort() {
        Random rnd = new Random();
        for (int t = 0; t < 100; t++) {
            int n = rnd.nextInt(500);
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] actual = a.clone();
            QuickSorter.sort(actual);
            if (!Arrays.equals(expected, actual)) {
                System.out.println("QuickSort FAILED on test " + t);
                return;
            }
        }
        System.out.println("QuickSort: all 100 tests passed (vs Arrays.sort)");
    }

    private static void testSelect() {
        Random rnd = new Random();
        for (int t = 0; t < 100; t++) {
            int n = rnd.nextInt(200) + 1;
            int[] a = rnd.ints(n, -1000, 1000).toArray();
            int k = rnd.nextInt(n);
            int[] expected = a.clone();
            Arrays.sort(expected);
            int[] actual = a.clone();
            int result = DeterministicSelector.select(actual, k);
            if (result != expected[k]) {
                System.out.println("Select FAILED on test " + t);
                return;
            }
        }
        System.out.println("DeterministicSelect: all 100 tests passed (vs Arrays.sort(a)[k])");
    }

    private static void testClosestPair() {
        Random rnd = new Random();
        for (int t = 0; t < 50; t++) {
            int n = rnd.nextInt(200) + 2;
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);
            }
            double fast = ClosestPairSolver.closestPair(points);
            double brute = ClosestPairSolver.bruteForce(points);
            if (Math.abs(fast - brute) > 1e-6) {
                System.out.println("ClosestPair FAILED on test " + t);
                return;
            }
        }
        System.out.println("ClosestPair: all 50 tests passed (vs brute force)");
    }
}
