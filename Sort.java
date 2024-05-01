import java.util.Arrays;
import java.util.Random;

public class Sort {

    // Constants for the test setup
    private static final int START = 10; // Minimum number of entries
    private static final int STOP = 50; // Maximum number of entries
    private static final int STEPS = 5; // Intervals
    private static final int REP = 3; // Number of repetitions for each size
    private static boolean alreadySorted = false;

    // Main method to initiate sorting tests
    public static void main(String[] args) {
        // Loop over different array sizes
        for (int size = START; size <= STOP; size += STEPS) {
            System.out.println("Testing size: " + size);
            System.out.println();
            for (double presortedness : new double[] { 0, 0.5, 1 }) {
                int[] baseArray = generateArray(size, presortedness);
                for (int i = 0; i < REP; i++) {
                    // Make a copy of the base array for sorting
                    int[] arrayToSort = Arrays.copyOf(baseArray, baseArray.length);
                    int[] sortedArray;
                    long startTime = System.nanoTime();
                    if (!alreadySorted) {
                        sortedArray = insertionSort(arrayToSort);
                    } else {
                        sortedArray = arrayToSort;
                    }
                    long endTime = System.nanoTime();
                    for (int j = 0; j < sortedArray.length; j++) {
                        System.out.print(sortedArray[j] + " ");
                    }
                    System.out.println();
                    System.out.printf("Presortedness: %.1f, Repetition %d: %d nanoseconds%n",
                            presortedness, i + 1,
                            endTime - startTime);
                    System.out.println();
                }
                System.out.println();
            }
            alreadySorted = false;
            System.out.println();
        }
    }

    // Method to perform insertion sort
    public static int[] insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int current = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > current) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = current;
        }
        return array;
    }

    // Method to generate an array with a specified level of presortedness
    public static int[] generateArray(int size, double presortedness) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        Random rand = new Random();

        if (presortedness == 0) { // Fully reversed
            for (int i = 0; i < size / 2; i++) {
                int temp = array[i];
                array[i] = array[size - 1 - i];
                array[size - 1 - i] = temp;
            }
        } else if (presortedness < 1) { // Random order based on degree of presortedness
            for (int i = 0; i < size * (1 - presortedness); i++) {
                int j = rand.nextInt(size);
                int k = rand.nextInt(size);
                int temp = array[j];
                array[j] = array[k];
                array[k] = temp;
            }
        } else {
            alreadySorted = true;
        }

        return array;
    }
}
