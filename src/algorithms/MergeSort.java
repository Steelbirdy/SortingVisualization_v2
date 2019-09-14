package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

public class MergeSort extends Algorithm {

    @Override
    public String getName() { return "Merge Sort"; }

    @Override
    public void run(ArrayToSort array) {
        int left = 0;
        int right = array.arraySize() - 1;
        mergeSort(array, left, right);
    }
    private int[] subArray(ArrayToSort array, int start, int len) {
        int[] tr = new int[len];
        for(int i = 0; i < len; i++) {
            if(GlobalManager.shouldEnd()) {
                return new int[]{};
            }
            array.checkRepaints();
            array.waitForResume();
            tr[i] = array.getValue(start + i, false);
        }
        return tr;
    }
    private void merge(ArrayToSort array, int left, int mid, int right) {
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        int[] leftArr = subArray(array, left, leftSize);
        int[] rightArr = subArray(array, mid + 1, rightSize);

        int i = 0, j = 0, k = left;
        while (i < leftSize && j < rightSize) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            array.incReads(2);
            if (leftArr[i] <= rightArr[j]) {
                array.setValue(k, leftArr[i]);
                i++;
            } else {
                array.setValue(k, rightArr[j]);
                j++;
            }
            k++;
        }
        while (i < leftSize) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            array.setValue(k, leftArr[i]);
            i++;
            k++;
        }
        while (j < rightSize) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            array.setValue(k, rightArr[j]);
            j++;
            k++;
        }

    }
    private void mergeSort(ArrayToSort array, int left, int right) {
        if(left < right) {
            int mid = (left + right) / 2;

            mergeSort(array, left, mid);
            if(GlobalManager.shouldEnd()) {
                return;
            }
            mergeSort(array, mid + 1, right);
            if(GlobalManager.shouldEnd()) {
                return;
            }
            merge(array, left, mid, right);
        }
    }

    @Override
    public long timeRun(ArrayToSort array) {

        int[] arr = Algorithm.getArray(array);

        int left = 0;
        int right = arr.length - 1;

        long startTime = System.nanoTime();

        timeMergeSort(arr, left, right);

        return System.nanoTime() - startTime;
    }
    private int[] timeSubArray(int[] array, int start, int len) {
        int[] tr = new int[len];
        for(int i = 0; i < len; i++) {
            tr[i] = array[start + i];
        }
        return tr;
    }
    private void timeMerge(int[] array, int left, int mid, int right) {
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        int[] leftArr = timeSubArray(array, left, leftSize);
        int[] rightArr = timeSubArray(array, mid + 1, rightSize);

        int i = 0, j = 0, k = left;
        while (i < leftSize && j < rightSize) {
            if (leftArr[i] <= rightArr[j]) {
                array[k] = leftArr[i];
                i++;
            } else {
                array[k] = rightArr[j];
                j++;
            }
            k++;
        }
        while (i < leftSize) {
            array[k] = leftArr[i];
            i++;
            k++;
        }
        while (j < rightSize) {
            array[k] = rightArr[j];
            j++;
            k++;
        }

    }
    private void timeMergeSort(int[] array, int left, int right) {
        if(left < right) {
            int mid = (left + right) / 2;

            timeMergeSort(array, left, mid);
            timeMergeSort(array, mid + 1, right);
            timeMerge(array, left, mid, right);
        }
    }

}
