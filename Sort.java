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
                        // sortedArray = insertionSort(arrayToSort);
                        // sortedArray = mergeSort(arrayToSort);
                        // sortedArray = heapSort(arrayToSort);
                        // sortedArray = quickSort(arrayToSort, 0, arrayToSort.length - 1);
                        sortedArray = selectionSort(arrayToSort);
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

    // Method to perform merge sort
    public static int[] mergeSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int mid = array.length / 2;
        int[] left = new int[mid];
        int[] right = new int[array.length - mid];

        for (int i = 0; i < mid; i++) {
            left[i] = array[i];
        }
        for (int i = mid; i < array.length; i++) {
            right[i - mid] = array[i];
        }

        mergeSort(left);
        mergeSort(right);

        merge(array, left, right);

        return array;
    }

    // Method to merge two halves
    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }
        while (i < left.length) {
            array[k++] = left[i++];
        }
        while (j < right.length) {
            array[k++] = right[j++];
        }

    }

    // Method to perform heap sort
    public static int[] heapSort(int[] array) {
        int length = array.length;

        // Build a max heap
        for (int i = length / 2 - 1; i >= 0; i--) {
            heapify(array, length, i);
        }

        // One by one extract elements from heap
        for (int i = length - 1; i > 0; i--) {
            // Move current root to end
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // call max heapify on the reduced heap
            heapify(array, i, 0);
        }

        return array;
    }

    // Method to heapify a subtree rooted with node i, which is an index in array[]
    private static void heapify(int[] array, int heapSize, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1; // left = 2*i + 1
        int right = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (left < heapSize && array[left] > array[largest]) {
            largest = left;
        }

        // If right child is larger than largest so far
        if (right < heapSize && array[right] > array[largest]) {
            largest = right;
        }

        // If largest is not root
        if (largest != i) {
            int swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(array, heapSize, largest);
        }
    }

    // Method to perform quick sort
    public static int[] quickSort(int[] array, int low, int high) {
        if (low < high) {
            // pi is partitioning index, array[pi] is now at right place
            int pi = partition(array, low, high);

            // Recursively sort elements before partition and after partition
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }

        return array;
    }

    // Method to partition the array on the basis of pivot element
    private static int partition(int[] array, int low, int high) {
        int pivot = array[high]; // using the last element as pivot
        int i = (low - 1); // Index of smaller element and indicates the right position of pivot found so
                           // far

        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if (array[j] < pivot) {
                i++;

                // swap array[i] and array[j]
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // swap array[i+1] and array[high] (or pivot)
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    // Method to perform selection sort
    public static int[] selectionSort(int[] array) {
        int n = array.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (array[j] < array[min_idx])
                    min_idx = j;

            // Swap the found minimum element with the first element
            int temp = array[min_idx];
            array[min_idx] = array[i];
            array[i] = temp;
        }
        return array;
    }

}
