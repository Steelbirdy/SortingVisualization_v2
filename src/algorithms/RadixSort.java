package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

import java.util.Arrays;

public class RadixSort extends Algorithm {

    @Override
    public String getName() {
        return "Radix Sort";
    }

    @Override
    public void run(ArrayToSort array) {
        int m = getMax(array);
        for(int exp = 1; m / exp > 0; exp *= 10) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            countSort(array, exp);
        }
    }
    private int getMax(ArrayToSort array) {
        int max = array.getValue(0, true);
        int temp;
        for(int i = 1; i < array.arraySize(); i++) {
            if(GlobalManager.shouldEnd()) {
                return 0;
            }
            array.checkRepaints();
            array.waitForResume();
            if ((temp = array.getValue(i, true)) > max)
                max = temp;
        }
        return max;
    }
    private void countSort(ArrayToSort array, int exp) {
        int[] output = new int[array.arraySize()];
        int i;
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for(i = 0; i < array.arraySize(); i++) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            count[(array.getValue(i, true) / exp) % 10]++;
        }
        for(i = 1; i < 10; i++) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            count[i] += count[i - 1];
        }
        for(i = array.arraySize() - 1; i >= 0; i--) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            output[count[(array.getValue(i, true) / exp) % 10] - 1] = array.getValue(i, true);
            count[(array.getValue(i, true) / exp) % 10]--;
        }
        for(i = 0; i < array.arraySize(); i++) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            array.setValue(i, output[i]);
        }
    }

    @Override
    public long timeRun(ArrayToSort array) {

        long startTime = System.nanoTime();

        int[] arr = Algorithm.getArray(array);

        int m = timeGetMax(arr);
        for(int exp = 1; m / exp > 0; exp *= 10)
            timeCountSort(arr, exp);

        return System.nanoTime() - startTime;
    }
    private int timeGetMax(int[] array) {
        int max = array[0];
        for(int i = 1; i < array.length; i++)
            if(array[i] > max) max = array[i];
        return max;
    }
    private void timeCountSort(int[] array, int exp) {
        int[] output = new int[array.length];
        int i;
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for(i = 0; i < array.length; i++) {
            count[(array[i] / exp) % 10]++;
        }
        for(i = 1; i < 10; i++)
            count[i] += count[i - 1];
        for(i = array.length - 1; i >= 0; i--) {
            output[count[(array[i] / exp) % 10] - 1] = array[i];
            count[(array[i] / exp) % 10]--;
        }
        for(i = 0; i < array.length; i++)
            array[i] = output[i];
    }



}
