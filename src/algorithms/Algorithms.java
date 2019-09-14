package algorithms;

import util.ArrayToSort;
import util.GlobalManager;
import util.Timing;

import java.util.Arrays;

public enum Algorithms {

    BUBBLE_SORT("Bubble Sort") {
        @Override
        public void run(ArrayToSort array) {
            boolean sorted = false;
            int length = array.arraySize();

            while (!sorted && length > 0) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                sorted = true;
                for (int i = 0; i < length - 1; i++) {
                    if (GlobalManager.shouldEnd()) {
                        return;
                    }
                    array.checkRepaints();
                    array.waitForResume();
                    if (array.compare(i, i + 1) > 0) {
                        array.swap(i, i + 1);
                        sorted = false;
                    }
                }
                length--;
            }
        }

        @Override
        public long timeRun(ArrayToSort array) {
            int[] arr = Algorithms.getArray(array);
            int length = arr.length;
            boolean sorted = false;


            long startTime = System.nanoTime();

            while (!sorted) {
                sorted = true;
                for (int i = 0; i < length - 1; i++) {
                    if (arr[i] > arr[i + 1]) {
                        Algorithms.swap(arr, i, i + 1);
                        sorted = false;
                    }
                }
                length--;
            }

            return System.nanoTime() - startTime;
        }
    },
    COMB_SORT("Comb Sort") {
        final double SHRINK_FACTOR = 1.3;

        @Override
        public void run(ArrayToSort array) {
            int gap = array.arraySize();
            boolean sorted = false;

            while (!sorted) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                gap /= SHRINK_FACTOR;
                if (gap <= 1) {
                    gap = 1;
                    sorted = true;
                }

                for (int i = 0; i + gap < array.arraySize(); i++) {
                    if (GlobalManager.shouldEnd()) {
                        return;
                    }
                    array.checkRepaints();
                    array.waitForResume();
                    if (array.compare(i, i + gap) > 0) {
                        array.swap(i, i + gap);
                        sorted = false;
                    }
                }

            }
        }

        @Override
        public long timeRun(ArrayToSort array) {

            int[] arr = Algorithms.getArray(array);
            int gap = array.arraySize();
            boolean sorted = false;

            long startTime = System.nanoTime();

            while (!sorted) {
                gap /= SHRINK_FACTOR;
                if (gap <= 1) {
                    gap = 1;
                    sorted = true;
                }

                for (int i = 0; i + gap < arr.length; i++) {
                    if (arr[i] > arr[i + gap]) {
                        Algorithms.swap(arr, i, i + gap);
                        sorted = false;
                    }
                }

            }

            return System.nanoTime() - startTime;
        }
    },
    HEAP_SORT("Heap Sort") {
        @Override
        public void run(ArrayToSort array) {
            int len = array.arraySize();

            heapify(array);
            if (GlobalManager.shouldEnd()) {
                return;
            }
            int end = len - 1;
            do {
                if (GlobalManager.shouldEnd()) {
                    break;
                }
                array.checkRepaints();
                array.waitForResume();
                array.swap(end, 0);
                end--;
                siftDown(array, 0, end);
                if (GlobalManager.shouldEnd()) {
                    break;
                }
            } while (end > 0);
            if (GlobalManager.shouldEnd()) {
                return;
            }
        }

        @Override
        public long timeRun(ArrayToSort array) {

            int[] arr = Algorithms.getArray(array);
            long startTime = System.nanoTime();

            int len = arr.length;

            timeHeapify(arr);
            int end = len - 1;
            do {
                Algorithms.swap(arr, 0, end);
                end--;
                timeSiftDown(arr, 0, end);
            } while (end > 0);

            return System.nanoTime() - startTime;

        }

        private int iParent(int index) {
            return (index - 1) / 2;
        }

        private int iLeftChild(int index) {
            return 2 * index + 1;
        }

        private int iRightChild(int index) {
            return 2 * index + 2;
        }

        private void heapify(ArrayToSort array) {
            int len = array.arraySize();
            int start = iParent(len - 1);

            do {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                siftDown(array, start, len - 1);
                start--;
            } while (start >= 0);
        }

        private void siftDown(ArrayToSort array, int start, int end) {
            int root = start;

            while (iLeftChild(root) <= end) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                int child = iLeftChild(root);
                int swap = root;
                if (array.compare(swap, child) < 0)
                    swap = child;
                if (child + 1 <= end && array.compare(swap, child + 1) < 0)
                    swap = child + 1;
                if (swap == root)
                    return;
                else {
                    array.swap(root, swap);
                    root = swap;
                }
            }
        }

        private void timeHeapify(int[] array) {
            int len = array.length;
            int start = iParent(len - 1);

            do {
                timeSiftDown(array, start, len - 1);
                start--;
            } while (start >= 0);
        }

        private void timeSiftDown(int[] array, int start, int end) {
            int root = start;

            while (iLeftChild(root) <= end) {
                int child = iLeftChild(root);
                int swap = root;
                if (array[swap] < array[child])
                    swap = child;
                if (child + 1 <= end && array[swap] < array[child + 1])
                    swap = child + 1;
                if (swap == root)
                    return;
                else {
                    Algorithms.swap(array, root, swap);
                    root = swap;
                }
            }
        }
    },
    INSERTION_SORT("Insertion Sort") {
        @Override
        public void run(ArrayToSort array) {
            for (int i = 1; i < array.arraySize(); i++) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                int x = array.getValue(i, true), j = i - 1;
                for (; j >= 0 && array.getValue(j, true) > x; j--) {
                    if (GlobalManager.shouldEnd()) {
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

            int[] arr = Algorithms.getArray(array);

            long startTime = System.nanoTime();

            for (int i = 1; i < arr.length; i++) {
                int x = arr[i], j = i - 1;
                for (; j > 0 && arr[j] > x; j--) {
                    Algorithms.swap(arr, j, j + 1);
                }
                arr[j + 1] = x;
            }

            return System.nanoTime() - startTime;
        }
    },
    MERGE_SORT("Merge Sort") {
        @Override
        public void run(ArrayToSort array) {
            int left = 0;
            int right = array.arraySize() - 1;
            mergeSort(array, left, right);
        }

        @Override
        public long timeRun(ArrayToSort array) {

            int[] arr = Algorithms.getArray(array);

            int left = 0;
            int right = arr.length - 1;

            long startTime = System.nanoTime();

            timeMergeSort(arr, left, right);

            return System.nanoTime() - startTime;
        }

        private int[] subArray(ArrayToSort array, int start, int len) {
            int[] tr = new int[len];
            for (int i = 0; i < len; i++) {
                if (GlobalManager.shouldEnd()) {
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
                if (GlobalManager.shouldEnd()) {
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
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                array.setValue(k, leftArr[i]);
                i++;
                k++;
            }
            while (j < rightSize) {
                if (GlobalManager.shouldEnd()) {
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
            if (left < right) {
                int mid = (left + right) / 2;

                mergeSort(array, left, mid);
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                mergeSort(array, mid + 1, right);
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                merge(array, left, mid, right);
            }
        }

        private int[] timeSubArray(int[] array, int start, int len) {
            int[] tr = new int[len];
            for (int i = 0; i < len; i++) {
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
            if (left < right) {
                int mid = (left + right) / 2;

                timeMergeSort(array, left, mid);
                timeMergeSort(array, mid + 1, right);
                timeMerge(array, left, mid, right);
            }
        }
    },
    QUICK_SORT("Quick Sort") {
        @Override
        public void run(ArrayToSort array) {
            quickSort(array, 0, array.arraySize() - 1);
        }

        @Override
        public long timeRun(ArrayToSort arr) {

            int[] array = Algorithms.getArray(arr);

            long startTime = System.nanoTime();

            timeQuickSort(array, 0, array.length - 1);

            return System.nanoTime() - startTime;

        }

        private void quickSort(ArrayToSort array, int lowIndex, int highIndex) {
            if (lowIndex < highIndex) {
                int pivotPoint = findPivotPoint(array, lowIndex, highIndex);
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                quickSort(array, lowIndex, pivotPoint - 1);
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                quickSort(array, pivotPoint + 1, highIndex);
            }
        }

        private int findPivotPoint(ArrayToSort array, int lowIndex, int highIndex) {
            int pivotValue = array.getValue(highIndex, true);
            int i = lowIndex - 1;
            for (int j = lowIndex; j <= highIndex - 1; j++) {
                if (GlobalManager.shouldEnd()) {
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
                    Algorithms.swap(array, i, j);
                }
            }
            Algorithms.swap(array, i + 1, highIndex);
            return i + 1;
        }
    },
    RADIX_SORT("Radix Sort") {
        @Override
        public void run(ArrayToSort array) {
            int m = getMax(array);
            for (int exp = 1; m / exp > 0; exp *= 10) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                countSort(array, exp);
            }
        }

        @Override
        public long timeRun(ArrayToSort array) {

            long startTime = System.nanoTime();

            int[] arr = Algorithms.getArray(array);

            int m = timeGetMax(arr);
            for (int exp = 1; m / exp > 0; exp *= 10)
                timeCountSort(arr, exp);

            return System.nanoTime() - startTime;
        }

        private int getMax(ArrayToSort array) {
            int max = array.getValue(0, true);
            int temp;
            for (int i = 1; i < array.arraySize(); i++) {
                if (GlobalManager.shouldEnd()) {
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

            for (i = 0; i < array.arraySize(); i++) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                count[(array.getValue(i, true) / exp) % 10]++;
            }
            for (i = 1; i < 10; i++) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                count[i] += count[i - 1];
            }
            for (i = array.arraySize() - 1; i >= 0; i--) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                output[count[(array.getValue(i, true) / exp) % 10] - 1] = array.getValue(i, true);
                count[(array.getValue(i, true) / exp) % 10]--;
            }
            for (i = 0; i < array.arraySize(); i++) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                array.setValue(i, output[i]);
            }
        }

        private int timeGetMax(int[] array) {
            int max = array[0];
            for (int i = 1; i < array.length; i++)
                if (array[i] > max) max = array[i];
            return max;
        }

        private void timeCountSort(int[] array, int exp) {
            int[] output = new int[array.length];
            int i;
            int[] count = new int[10];
            Arrays.fill(count, 0);

            for (i = 0; i < array.length; i++) {
                count[(array[i] / exp) % 10]++;
            }
            for (i = 1; i < 10; i++)
                count[i] += count[i - 1];
            for (i = array.length - 1; i >= 0; i--) {
                output[count[(array[i] / exp) % 10] - 1] = array[i];
                count[(array[i] / exp) % 10]--;
            }
            for (i = 0; i < array.length; i++)
                array[i] = output[i];
        }
    },
    SELECTION_SORT("Selection Sort") {
        @Override
        public void run(ArrayToSort array) {

            int len = array.arraySize();
            for (int i = 0; i < len - 1; i++) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                int minIndex = i;
                for (int j = i + 1; j < len; j++) {
                    if (GlobalManager.shouldEnd()) {
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

            int[] array = Algorithms.getArray(arr);

            int len = array.length;

            long startTime = System.nanoTime();

            for (int i = 0; i < len - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < len; j++) {
                    if (array[i] < array[minIndex]) {
                        minIndex = j;
                    }
                }
                Algorithms.swap(array, i, minIndex);
            }

            return System.nanoTime() - startTime;

        }
    },
    SHELL_SORT("Shell Sort") {
        private final int[] gaps = new int[]{701, 301, 132, 57, 23, 10, 4, 1};

        @Override
        public void run(ArrayToSort array) {

            for (int gap : gaps) {

                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                for (int i = gap; i < array.arraySize(); i++) {
                    if (GlobalManager.shouldEnd()) {
                        return;
                    }
                    array.checkRepaints();
                    array.waitForResume();
                    int temp = array.getValue(i, true);
                    int j;
                    for (j = i; j >= gap && array.getValue(j - gap, true) > temp; j -= gap) {

                        if (GlobalManager.shouldEnd()) {
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

            int[] arr = Algorithms.getArray(array);

            long startTime = System.nanoTime();

            for (int gap : gaps) {
                for (int i = gap; i < arr.length; i++) {
                    int temp = arr[i];
                    int j;
                    for (j = i; j >= gap && arr[j - gap] > temp; j -= gap)
                        arr[j] = arr[j - gap];
                    arr[j] = temp;
                }
            }

            return System.nanoTime() - startTime;
        }
    },
    TIM_SORT("TimSort") {
        private final static int MIN_TO_MERGE = 64;
        private int RUN = 32;

        @Override
        public void run(ArrayToSort array) {

            int len = array.arraySize();
            this.RUN = calcMinRun(len);

            for (int i = 0; i < len; i += RUN) {
                insertionSort(array, i, Math.min(i + RUN + 1, len));
                //insertionSort(array, i, (len < i + RUN + 1 ? len : i + RUN + 1));
            }

            for (int size = RUN; size <= len; size *= 2) {
                for (int lo = 0; lo < len; lo += 2 * size) {
                    int mid = lo + size - 1;
                    int hi = Math.min(lo + 2 * size - 1, len - 1);

                    if (mid > hi)
                        mid = (lo + hi) / 2;

                    merge(array, lo, mid, hi);
                }
            }

        }

        @Override
        public long timeRun(ArrayToSort array) {

            int[] arr = Algorithms.getArray(array);
            int len = array.arraySize();
            this.RUN = calcMinRun(len);

            long startTime = System.nanoTime();

            for (int i = 0; i < len; i += RUN) {
                timeInsertionSort(arr, i, Math.min(i + RUN + 1, len));
            }

            System.out.println(Timing.nanoToSeconds(System.nanoTime() - startTime));

            for (int size = RUN; size <= len; size *= 2) {
                for (int lo = 0; lo < len; lo += 2 * size) {
                    int mid = lo + size - 1;
                    int hi = Math.min(lo + 2 * size - 1, len - 1);

                    if (mid > hi)
                        mid = (lo + hi) / 2;

                    timeMerge(arr, lo, mid, hi);
                }
            }

            return System.nanoTime() - startTime;
        }

        private void insertionSort(ArrayToSort array, int lo, int hi) {

            for (int i = lo + 1; i < hi; i++) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                int x = array.getValue(i, true), j = i - 1;
                for (; j >= lo && array.getValue(j, true) > x; j--) {
                    if (GlobalManager.shouldEnd()) {
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
            for (int i = 0; i < len; i++) {
                if (GlobalManager.shouldEnd()) {
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
                if (GlobalManager.shouldEnd()) {
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
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                array.setValue(k, leftArr[i]);
                i++;
                k++;
            }
            while (j < rightSize) {
                if (GlobalManager.shouldEnd()) {
                    return;
                }
                array.checkRepaints();
                array.waitForResume();
                array.setValue(k, rightArr[j]);
                j++;
                k++;
            }

        }

        private void timeInsertionSort(int[] arr, int lo, int hi) {
            int x, j;
            for (int i = lo + 1; i < hi; i++) {
                x = arr[i];
                j = i - 1;
                do {
                    Algorithms.swap(arr, j, j + 1);
                    j--;
                } while (j >= lo && arr[j] > x);
            /*for(; j >= lo && arr[j] > x; j--) {
                Algorithm.swap(arr, j, j + 1);
            }*/
                arr[j + 1] = x;
            }
        }

        private int[] timeSubArray(int[] array, int start, int len) {
            int[] tr = new int[len];
            for (int i = 0; i < len; i++) {
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

            if (len < MIN_TO_MERGE)
                return len;

            final double LOG_2 = Math.log(2);

            double x = Math.log(len / 32) / LOG_2;
            double minDiff = x - (int) x, temp;
            int minVal = 32;

            for (int minrun = 33; minrun <= 64; minrun++) {
                x = Math.log(len / minrun) / LOG_2;
                if ((temp = x - (int) x) < minDiff) {
                    minDiff = temp;
                    minVal = minrun;
                }
            }
            return minVal;
        }
    };


    private String name;

    Algorithms(String name) {
        this.name = name;
    }

    /**
     * Convert an ArrayToSort object into an integer array
     *
     * @param array the array to convert
     * @return the array as an integer array object
     */
    public static int[] getArray(ArrayToSort array) {
        int[] tr = new int[array.arraySize()];
        for (int i = 0; i < tr.length; i++)
            tr[i] = array.getValue(i, false);
        return tr;
    }

    /**
     * Swaps the values of two indexes in an array
     *
     * @param arr    the array to perform the swap in
     * @param index0 the first index to swap
     * @param index1 the second index to swap
     */
    static void swap(int[] arr, int index0, int index1) {
        int temp = arr[index0];
        arr[index0] = arr[index1];
        arr[index1] = temp;
    }

    /**
     * @return the display name of the algorithm
     */
    public String getName() {
        return this.name;
    }

    /**
     * The run method used for the visualization
     *
     * @param array the array to sort
     */
    public abstract void run(ArrayToSort array);

    /**
     * The run method used to provide an accurate runtime for the algorithm
     *
     * @param array the array to sort
     * @return the time it took, in nanoseconds
     */
    public abstract long timeRun(ArrayToSort array);

    @Override
    public String toString() {
        return this.getName();
    }

}
