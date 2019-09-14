package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

public class InsertionSort extends Algorithm {

    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public void run(ArrayToSort array) {
        for(int i = 1; i < array.arraySize(); i++) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            int x = array.getValue(i, true), j = i - 1;
            for(; j >= 0 && array.getValue(j, true) > x; j--) {
                if(GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                array.setValue(j + 1, array.getValue(j, true));
            }
            array.setValue(j + 1, x);
        }
    }

    @Override
    public long timeRun(ArrayToSort array) {

        int[] arr = Algorithm.getArray(array);

        long startTime = System.nanoTime();

        for(int i = 1; i < arr.length; i++) {
            int x = arr[i], j = i - 1;
            for(; j > 0 && arr[j] > x; j--) {
                Algorithm.swap(arr, j, j + 1);
            }
            arr[j + 1] = x;
        }

        return System.nanoTime() - startTime;
    }

}
