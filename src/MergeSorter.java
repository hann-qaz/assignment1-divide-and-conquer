public class MergeSorter {

    private static final int CUTOFF = 16;
    private static int maxDepth;
    public static long comparisons;

    public static void sort(int[] arr) {
        maxDepth = 0;
        comparisons = 0;
        int[] buffer = new int[arr.length];
        sort(arr, buffer, 0, arr.length - 1, 0);
    }

    private static void sort(int[] arr, int[] buffer, int lo, int hi, int depth) {
        maxDepth = Math.max(maxDepth, depth);

        if (hi - lo <= CUTOFF) {
            insertionSort(arr, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(arr, buffer, lo, mid, depth + 1);
        sort(arr, buffer, mid + 1, hi, depth + 1);
        merge(arr, buffer, lo, mid, hi);
    }

    private static void merge(int[] arr, int[] buffer, int lo, int mid, int hi) {
        for (int i = lo; i <= hi; i++) buffer[i] = arr[i];

        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            comparisons++;
            if (buffer[i] <= buffer[j]) arr[k++] = buffer[i++];
            else arr[k++] = buffer[j++];
        }
        while (i <= mid) arr[k++] = buffer[i++];
        while (j <= hi) arr[k++] = buffer[j++];
    }

    private static void insertionSort(int[] arr, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= lo && arr[j] > key) {
                comparisons++;
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static int getMaxDepth() {
        return maxDepth;
    }
}
