import java.util.Random;

public class QuickSorter {

    private static final Random rnd = new Random();
    private static int maxDepth;
    public static long comparisons;
    public static long swaps;

    public static void sort(int[] arr) {
        maxDepth = 0;
        comparisons = 0;
        swaps = 0;
        sort(arr, 0, arr.length - 1, 0);
    }

    private static void sort(int[] arr, int lo, int hi, int depth) {
        while (lo < hi) {
            maxDepth = Math.max(maxDepth, depth);

            int pivotIndex = lo + rnd.nextInt(hi - lo + 1);
            int pivot = arr[pivotIndex];

            int lt = lo, gt = hi, i = lo;
            while (i <= gt) {
                comparisons++;
                if (arr[i] < pivot) {
                    swap(arr, lt++, i++);
                } else if (arr[i] > pivot) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - lo < hi - gt) {
                sort(arr, lo, lt - 1, depth + 1);
                lo = gt + 1;
            } else {
                sort(arr, gt + 1, hi, depth + 1);
                hi = lt - 1;
            }
        }
    }

    private static void swap(int[] arr, int a, int b) {
        if (a == b) return;
        int t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
        swaps++;
    }

    public static int getMaxDepth() {
        return maxDepth;
    }
}
