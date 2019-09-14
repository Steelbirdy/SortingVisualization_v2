package algorithms;

import util.ArrayToSort;
import util.GlobalManager;

/**
 * Heapsort is an extremely complex algorithm.
 */
public class HeapSort extends Algorithm {

    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public void run(ArrayToSort array) {
        int len = array.arraySize();

        heapify(array);
        if(GlobalManager.shouldEnd()) {
            return;
        }
        int end = len - 1;
        do {
            if(GlobalManager.shouldEnd()) {
                break;
            }
            array.checkRepaints();
            array.waitForResume();
            array.swap(end, 0);
            end--;
            siftDown(array, 0, end);
            if(GlobalManager.shouldEnd()) {
                break;
            }
        } while(end > 0);
        if(GlobalManager.shouldEnd()) {
            return;
        }
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
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            siftDown(array, start, len - 1);
            start--;
        } while(start >= 0);
    }
    private void siftDown(ArrayToSort array, int start, int end) {
        int root = start;

        while(iLeftChild(root) <= end) {
            if(GlobalManager.shouldEnd()) {
                return;
            }
            array.checkRepaints();
            array.waitForResume();
            int child = iLeftChild(root);
            int swap = root;
            if(array.compare(swap, child) < 0)
                swap = child;
            if(child + 1 <= end && array.compare(swap, child + 1) < 0)
                swap = child + 1;
            if(swap == root)
                return;
            else {
                array.swap(root, swap);
                root = swap;
            }
        }
    }

    @Override
    public long timeRun(ArrayToSort array) {

        int[] arr = Algorithm.getArray(array);
        long startTime = System.nanoTime();

        int len = arr.length;

        timeHeapify(arr);
        int end = len - 1;
        do {
            Algorithm.swap(arr, 0, end);
            end--;
            timeSiftDown(arr, 0, end);
        } while(end > 0);

        return System.nanoTime() - startTime;

    }
    private void timeHeapify(int[] array) {
        int len = array.length;
        int start = iParent(len - 1);

        do {
            timeSiftDown(array, start, len - 1);
            start--;
        } while(start >= 0);
    }
    private void timeSiftDown(int[] array, int start, int end) {
        int root = start;

        while(iLeftChild(root) <= end) {
            int child = iLeftChild(root);
            int swap = root;
            if(array[swap] < array[child])
                swap = child;
            if(child + 1 <= end && array[swap] < array[child + 1])
                swap = child + 1;
            if(swap == root)
                return;
            else {
                Algorithm.swap(array, root, swap);
                root = swap;
            }
        }
    }

}
