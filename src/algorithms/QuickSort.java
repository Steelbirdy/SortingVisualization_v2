package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

public class QuickSort extends Algorithm {
    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public void run(ArrayToSort array) {
        quickSort(array, 0, array.arraySize() - 1);
    }
    private void quickSort(ArrayToSort array, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int pivotPoint = findPivotPoint(array, lowIndex, highIndex);
            if(GlobalManager.shouldEnd()) {
                return;
            }
            quickSort(array, lowIndex, pivotPoint - 1);
            if(GlobalManager.shouldEnd()) {
                return;
            }
            quickSort(array, pivotPoint + 1, highIndex);
        }
    }
    private int findPivotPoint(ArrayToSort array, int lowIndex, int highIndex) {
        int pivotValue = array.getValue(highIndex, true);
        int i = lowIndex - 1;
        for (int j = lowIndex; j <= highIndex - 1; j++) {
            if(GlobalManager.shouldEnd()) {
                return 0;
            }
            array.checkRepaints();
            array.waitForResume();
            if (array.getValue(j, true) <= pivotValue) {
                i++;
                array.swap(i, j);
            }
        }
        array.swap(i + 1, highIndex);
        return i + 1;
    }

    @Override
    public long timeRun(ArrayToSort arr) {

        int[] array = Algorithm.getArray(arr);

        long startTime = System.nanoTime();

        timeQuickSort(array, 0, array.length - 1);

        return System.nanoTime() - startTime;

    }
    private void timeQuickSort(int[] array, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int pivotPoint = timeFindPivotPoint(array, lowIndex, highIndex);
            timeQuickSort(array, lowIndex, pivotPoint - 1);
            timeQuickSort(array, pivotPoint + 1, highIndex);
        }
    }
    private int timeFindPivotPoint(int[] array, int lowIndex, int highIndex) {
        int pivotValue = array[highIndex];
        int i = lowIndex - 1;
        for (int j = lowIndex; j <= highIndex - 1; j++) {
            if (array[j] <= pivotValue) {
                i++;
                Algorithm.swap(array, i, j);
            }
        }
        Algorithm.swap(array, i + 1, highIndex);
        return i + 1;
    }

}
