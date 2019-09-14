package algorithms;

import util.ArrayToSort;

public abstract class Algorithm {

    //This is currently unused
    public boolean isPaused = false;

    /**
     * @return the display name of the algorithm
     */
    public abstract String getName();

    /**
     * The run method used for the visualization
     * @param array the array to sort
     */
    public abstract void run(ArrayToSort array);

    /**
     * The run method used to provide an accurate runtime for the algorithm
     * @param array the array to sort
     * @return the time it took, in nanoseconds
     */
    public abstract long timeRun(ArrayToSort array);

    /**
     * Convert an ArrayToSort object into an integer array
     * @param array the array to convert
     * @return the array as an integer array object
     */
    public static int[] getArray(ArrayToSort array) {
        int[] tr = new int[array.arraySize()];
        for(int i = 0; i < tr.length; i++)
            tr[i] = array.getValue(i, false);
        return tr;
    }

    static void swap(int[] arr, int index0, int index1) {
        int temp = arr[index0];
        arr[index0] = arr[index1];
        arr[index1] = temp;
    }

    @Override
    public String toString() { return this.getName(); }

}
