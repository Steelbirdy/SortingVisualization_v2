package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

/**
 * Bubble sort is an extremely simple and inefficient sorting algorithm
 * It iterates over each pair of indices and swaps them if necessary,
 *  essentially filtering larger objects towards the back one at a time
 * Bubble sort runs with quadratic [O(n^2)] time complexity, much slower than more efficient algorithms such as Merge sort
 */
public class BubbleSort extends Algorithm {

    @Override
    public String getName() { return "Bubble Sort"; }

    @Override
    public void run(ArrayToSort array) {

        //These is used to optimize the algorithm
        boolean sorted = false;
        int length = array.arraySize();

        while(!sorted && length > 0) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            sorted = true;
            for(int i = 0; i < length - 1; i++) {
                if(GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                if(array.compare(i, i + 1) > 0) {
                    array.swap(i, i + 1);
                    sorted = false;
                }
            }
            length--;
        }

    }

    @Override
    public long timeRun(ArrayToSort arr) {

        int[] array = Algorithm.getArray(arr);
        int length = array.length;
        boolean sorted = false;


        long startTime = System.nanoTime();

        while(!sorted) {
            sorted = true;
            for(int i = 0; i < length - 1; i++) {
                if(array[i] > array[i + 1]) {
                    Algorithm.swap(array, i, i + 1);
                    sorted = false;
                }
            }
            length--;
        }

        return System.nanoTime() - startTime;

    }

}
