package algorithms;

import util.ArrayToSort;
import util.GlobalManager;
import util.Timing;

public class TimSort extends Algorithm {

    private final static int MIN_TO_MERGE = 64;

    private int RUN = 32;

    @Override
    public String getName() { return "TimSort"; }

    @Override
    public void run(ArrayToSort array) {

        int len = array.arraySize();
        this.RUN = calcMinRun(len);

        for(int i = 0; i < len; i += RUN) {
            insertionSort(array, i, Math.min(i + RUN + 1, len));
            //insertionSort(array, i, (len < i + RUN + 1 ? len : i + RUN + 1));
        }

        for(int size = RUN; size <= len; size *= 2) {
            for(int lo = 0; lo < len; lo += 2 * size) {
                int mid = lo + size - 1;
                int hi = Math.min(lo + 2 * size - 1, len - 1);

                if(mid > hi)
                    mid = (lo + hi) / 2;

                merge(array, lo, mid, hi);
            }
        }

    }

    private void insertionSort(ArrayToSort array, int lo, int hi) {

        for(int i = lo + 1; i < hi; i++) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            int x = array.getValue(i, true), j = i - 1;
            for(; j >= lo && array.getValue(j, true) > x; j--) {
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

    @Override
    public long timeRun(ArrayToSort array) {

        int[] arr = Algorithm.getArray(array);
        int len = array.arraySize();
        this.RUN = calcMinRun(len);

        long startTime = System.nanoTime();

        for(int i = 0; i < len; i += RUN) {
            timeInsertionSort(arr, i, Math.min(i + RUN + 1, len));
        }

        System.out.println(Timing.nanoToSeconds(System.nanoTime() - startTime));

        for(int size = RUN; size <= len; size *= 2) {
            for(int lo = 0; lo < len; lo += 2 * size) {
                int mid = lo + size - 1;
                int hi = Math.min(lo + 2 * size - 1, len - 1);

                if(mid > hi)
                    mid = (lo + hi) / 2;

                timeMerge(arr, lo, mid, hi);
            }
        }

        return System.nanoTime() - startTime;
    }
    private void timeInsertionSort(int[] arr, int lo, int hi) {
        int x, j;
        for(int i = lo + 1; i < hi; i++) {
            x = arr[i];
            j = i - 1;
            do {
                Algorithm.swap(arr, j, j + 1);
                j--;
            } while(j >= lo && arr[j] > x);
            /*for(; j >= lo && arr[j] > x; j--) {
                Algorithm.swap(arr, j, j + 1);
            }*/
            arr[j + 1] = x;
        }
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

    private int calcMinRun(int len) {

        if(len < MIN_TO_MERGE)
            return len;

        final double LOG_2 = Math.log(2);

        double x = Math.log(len / 32) / LOG_2;
        double minDiff = x - (int) x, temp;
        int minVal = 32;

        for(int minrun = 33; minrun <= 64; minrun++) {
            x = Math.log(len / minrun) / LOG_2;
            if ((temp = x - (int) x) < minDiff) {
                minDiff = temp;
                minVal = minrun;
            }
        }
        return minVal;
    }
}
