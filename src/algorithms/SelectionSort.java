package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

public class SelectionSort extends Algorithm {

    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public void run(ArrayToSort array) {

        int len = array.arraySize();
        for (int i = 0; i < len - 1; i++) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            int minIndex = i;
            for (int j = i + 1; j < len; j++) {
                if(GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                if (array.getValue(j, true) < array.getValue(minIndex, true)) {
                    minIndex = j;
                }
            }
            array.swap(i, minIndex);
        }

    }

    @Override
    public long timeRun(ArrayToSort arr) {

        int[] array = Algorithm.getArray(arr);

        int len = array.length;

        long startTime = System.nanoTime();

        for (int i = 0; i < len - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < len; j++) {
                if (array[i] < array[minIndex]) {
                    minIndex = j;
                }
            }
            Algorithm.swap(array, i, minIndex);
        }

        return System.nanoTime() - startTime;

    }

}
