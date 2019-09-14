package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

/**
 * Similarly to Shell sort, comb sort runs through the array with smaller and smaller "combs"
 * defined by n / s^p, where n is the size of the array, s is the Shrink Factor, and p is the
 * iteration number.
 *
 * Worst-case scenario, Shell sort runs in quadratic [O(n^2)] time complexity.
 * Best-case, it runs in O(n * log(n)) time, which is the most efficient complexity possible
 * On average, it runs in O(n^2 / 2^p) time, where p is the number of combs
 */
public class CombSort extends Algorithm {

    final double SHRINK_FACTOR = 1.3;

    @Override
    public String getName() {
        return "Comb Sort";
    }

    @Override
    public void run(ArrayToSort array) {
        int gap = array.arraySize();
        boolean sorted = false;

        while(!sorted) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            gap /= SHRINK_FACTOR;
            if(gap <= 1) {
                gap = 1;
                sorted = true;
            }

            for(int i = 0; i + gap < array.arraySize(); i++) {
                if(GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                if(array.compare(i, i + gap) > 0) {
                    array.swap(i, i + gap);
                    sorted = false;
                }
            }

        }
    }

    @Override
    public long timeRun(ArrayToSort array) {

        int[] arr = Algorithm.getArray(array);
        int gap = array.arraySize();
        boolean sorted = false;

        long startTime = System.nanoTime();

        while(!sorted) {
            gap /= SHRINK_FACTOR;
            if(gap <= 1) {
                gap = 1;
                sorted = true;
            }

            for(int i = 0; i + gap < arr.length; i++) {
                if(arr[i] > arr[i + gap]) {
                    Algorithm.swap(arr, i, i + gap);
                    sorted = false;
                }
            }

        }

        return System.nanoTime() - startTime;
    }

}
