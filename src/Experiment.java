import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Experiment {

    private static final Random rnd = new Random();

    public static void runAll(String csvPath) throws IOException {
        FileWriter writer = new FileWriter(csvPath);
        writer.write("algorithm,inputType,size,timeMs,maxDepth,comparisons\n");

        int[] sizes = {1000, 5000, 20000, 50000, 100000};
        String[] types = {"random", "sorted", "reverse", "duplicates"};

        for (int size : sizes) {
            for (String type : types) {
                int[] data = generate(size, type);

                int[] mergeData = data.clone();
                long t1 = System.nanoTime();
                MergeSorter.sort(mergeData);
                long t2 = System.nanoTime();
                writer.write(String.format("MergeSort,%s,%d,%.3f,%d,%d%n",
                        type, size, (t2 - t1) / 1e6, MergeSorter.getMaxDepth(), MergeSorter.comparisons));

                int[] quickData = data.clone();
                long t3 = System.nanoTime();
                QuickSorter.sort(quickData);
                long t4 = System.nanoTime();
                writer.write(String.format("QuickSort,%s,%d,%.3f,%d,%d%n",
                        type, size, (t4 - t3) / 1e6, QuickSorter.getMaxDepth(), QuickSorter.comparisons));

                int[] selectData = data.clone();
                long t5 = System.nanoTime();
                DeterministicSelector.select(selectData, size / 2);
                long t6 = System.nanoTime();
                writer.write(String.format("Select,%s,%d,%.3f,%d,%d%n",
                        type, size, (t6 - t5) / 1e6, DeterministicSelector.getMaxDepth(), DeterministicSelector.comparisons));
            }
        }

        int[] cpSizes = {500, 1000, 2000, 5000, 20000};
        for (int size : cpSizes) {
            Point[] points = generatePoints(size);
            long t7 = System.nanoTime();
            ClosestPairSolver.closestPair(points);
            long t8 = System.nanoTime();
            writer.write(String.format("ClosestPair,random,%d,%.3f,%d,0%n",
                    size, (t8 - t7) / 1e6, ClosestPairSolver.getMaxDepth()));
        }

        writer.close();
        System.out.println("Results saved to " + csvPath);
    }

    private static int[] generate(int size, String type) {
        int[] arr = new int[size];
        switch (type) {
            case "random":
                for (int i = 0; i < size; i++) arr[i] = rnd.nextInt(1_000_000);
                break;
            case "sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;
            case "reverse":
                for (int i = 0; i < size; i++) arr[i] = size - i;
                break;
            case "duplicates":
                for (int i = 0; i < size; i++) arr[i] = rnd.nextInt(10);
                break;
        }
        return arr;
    }

    private static Point[] generatePoints(int size) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(rnd.nextDouble() * 100000, rnd.nextDouble() * 100000);
        }
        return points;
    }
}
