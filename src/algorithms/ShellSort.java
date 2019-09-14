package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

public class ShellSort extends Algorithm {

    private final int[] gaps = new int[] {701, 301, 132, 57, 23, 10, 4, 1};

    @Override
    public String getName() {
        return "Shell Sort";
    }

    @Override
    public void run(ArrayToSort array) {

        for(int gap : gaps) {

            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            for(int i = gap; i < array.arraySize(); i++) {
                if(GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                int temp = array.getValue(i, true);
                int j;
                for(j = i; j >= gap && array.getValue(j - gap, true) > temp; j -= gap) {

                    if(GlobalManager.shouldEnd()) {
                        return;
                    }
                    array.checkRepaints();
                    array.waitForResume();
                    array.setValue(j, array.getValue(j - gap, true));
                }
                array.setValue(j, temp);
            }
        }

    }

    @Override
    public long timeRun(ArrayToSort array) {

        int[] arr = Algorithm.getArray(array);

        long startTime = System.nanoTime();

        for(int gap : gaps) {
            for(int i = gap; i < arr.length; i++) {
                int temp = arr[i];
                int j;
                for(j = i; j >= gap && arr[j - gap] > temp; j -= gap)
                    arr[j] = arr[j - gap];
                arr[j] = temp;
            }
        }

        return System.nanoTime() - startTime;
    }

}
