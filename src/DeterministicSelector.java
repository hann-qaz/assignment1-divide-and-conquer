public class DeterministicSelector {

    private static int maxDepth;
    public static long comparisons;

    public static int select(int[] arr, int k) {
        maxDepth = 0;
        comparisons = 0;
        return select(arr, 0, arr.length - 1, k, 0);
    }

    private static int select(int[] arr, int lo, int hi, int k, int depth) {
        maxDepth = Math.max(maxDepth, depth);

        if (lo == hi) return arr[lo];

        int pivotValue = medianOfMedians(arr, lo, hi, depth);

        int lt = lo, gt = hi, i = lo;
        while (i <= gt) {
            comparisons++;
            if (arr[i] < pivotValue) {
                swap(arr, lt++, i++);
            } else if (arr[i] > pivotValue) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }

        if (k < lt) return select(arr, lo, lt - 1, k, depth + 1);
        if (k <= gt) return arr[k];
        return select(arr, gt + 1, hi, k, depth + 1);
    }

    private static int medianOfMedians(int[] arr, int lo, int hi, int depth) {
        int n = hi - lo + 1;

        if (n <= 5) {
            insertionSort(arr, lo, hi);
            return arr[lo + n / 2];
        }

        int numGroups = (n + 4) / 5;
        for (int i = 0; i < numGroups; i++) {
            int groupLo = lo + i * 5;
            int groupHi = Math.min(groupLo + 4, hi);
            insertionSort(arr, groupLo, groupHi);
            int medianIndex = groupLo + (groupHi - groupLo) / 2;
            swap(arr, lo + i, medianIndex);
        }

        return select(arr, lo, lo + numGroups - 1, lo + numGroups / 2, depth + 1);
    }

    private static void insertionSort(int[] arr, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= lo && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private static void swap(int[] arr, int a, int b) {
        int t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
    }

    public static int getMaxDepth() {
        return maxDepth;
    }
}
