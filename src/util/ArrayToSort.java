package util;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ArrayToSort extends JPanel {

    //How much the shading of the color should decrease by each tick
    private final static int COLORING_DECREMENT = 20;

    //The dimensions of the array
    private int dim;

    //The array to be sorted
    private int[] array;

    //The coloring of the array (for showing swaps)
    private int[] coloring;

    //Storage for the real time the algorithm took
    private double realTime = 0;

    //The number of reads and writes the algorithm has performed
    private int reads, writes;

    private int repaints, repaintsGoal;

    //The only constructor I need...
    public ArrayToSort(int arrayDimensions) {

        dim = arrayDimensions;
        array = new int[dim];
        coloring = new int[dim];

        setBackground(Color.BLACK);

        for(int i = 0; i < array.length; i++) {
            array[i] = (int) (i * (SortingManager.ASPECT_RATIO / 1.125) * SortingManager.BAR_WIDTH);
            coloring[i] = 0;
        }

        reads = writes = repaints = repaintsGoal = 0;

    }

    public void countRepaintsTo(int count) {
        repaintsGoal = count;
        repaints = 0;
        GlobalManager.togglePaused();
    }

    public void checkRepaints() {
        if(repaints >= repaintsGoal && repaintsGoal != 0) {
            repaintsGoal = 0;
            GlobalManager.togglePaused();
        }
    }

    public void resetArray() {
        for(int i = 0; i < array.length; i++) {
            array[i] = (int) (i * (SortingManager.ASPECT_RATIO / 1.125) * SortingManager.BAR_WIDTH);
            coloring[i] = 0;
        }
        repaint();
    }

    //Reports the dimensions of the array
    public int arraySize() {
        return dim;
    }

    //For the SortingManager to report the actual time the algorithm takes, because the array
        //doesn't have access to the algorithm
    public void setRealTime(long time) {
        realTime = time / ((double) Math.pow(10, 9));
    }

    /**
     * Shuffles the array randomly
     */
    public void shuffle() {

        Random rand = new Random();

        for(int i = 0; i < array.length; i++) {
            checkRepaints();
            waitForResume();
            swap(i, rand.nextInt(array.length - 1));
            reads = 0;
            writes = 0;
        }

    }

    //The fancy final red scan line for the visualization
    //P.S. this is completely unnecessary
    public void finalSweep() {
        resetColors();
        for(int i = 0; i < array.length; i++) {
            checkRepaints();
            waitForResume();
            setValue(i, getValue(i, false));
        }
        resetColors();
    }

    //Reset the coloring to ensure that the visualization is white
    public void resetColors() {
        for(int i = 0; i < array.length; i++) {
            checkRepaints();
            waitForResume();
            coloring[i] = 0;
        }
        repaint();
    }

    public void waitForResume() {
        if(Thread.interrupted()) {
            while(true) {
                if(Thread.interrupted())
                    return;
            }
        }
    }

    /**
     * The swapping method used by all of the algorithms
     * Used so that the visualization knows what and when to update
     * Also reports +2 reads and +2 writes
     * @param index0 the index to swap with index1
     * @param index1 the index to swap with index0
     */
    public void swap(int index0, int index1) {
        reads += 2;
        writes += 2;
        coloring[index0] = coloring[index1] = 200;

        int temp = array[index0];
        array[index0] = array[index1];
        array[index1] = temp;

        repaint();
        Timing.waitFor(Timing.millisecondsToNano(GlobalManager.getDelayMs()));
    }

    /**
     * Returns either 1, 0, or -1, depending on the result of Integer#compare
     * Also reports 2 reads
     * @param index0 the index of the value to compare with the value in index1
     * @param index1 the index of the value to compare with the value in index0
     * @return the result of Integer#compare
     */
    public int compare(int index0, int index1) {
        reads += 2;

        return Integer.compare(array[index0], array[index1]);
    }

    /**
     * Allows algorithms to access the value of individual indices
     * @param index the index to read from
     * @param increment true if the visualization should increment the read count, false otherwise
     * @return the value in the inputted index
     */
    public int getValue(int index, boolean increment) {
        if(increment) reads++;

        return array[index];
    }

    /**
     * Allows algorithms to change the value of individual indices
     * Also reports 1 write
     * @param index the index to change the value of
     * @param value the value to write to the array at the inputted index
     */
    public void setValue(int index, int value) {
        writes++;
        coloring[index] = 200;

        array[index] = value;
        repaint();
        Timing.waitFor(Timing.millisecondsToNano(GlobalManager.getDelayMs()));
    }

    /**
     * Translates the value in the coloring array to a Color object
     * @param index the index to get the Color for
     * @return a Color object corresponding
     */
    public Color translate(int index) {
        int diff = coloring[index];
        if(diff == 0) return Color.WHITE;
        else {
            coloring[index] -= COLORING_DECREMENT;
            return new Color(255, 255 - diff, 255 - diff);
        }
    }

    //Increments the read count by 1
    //I don't remember why this was necessary, but it was (I think)
    public void incReads(int i) {
        reads += i;
    }

    /**
     * In order to update the visualization, I Override the #paintComponent method
     * @param g the Graphics object to update
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);

        repaints++;

        g2d.setColor(Color.WHITE);
        g2d.drawString("Real Time: " + realTime + " sec", 10, 15);
        g2d.drawString("Reads: " + reads, 10, 30);
        g2d.drawString("Writes: " + writes, 10, 45);

        for(int i = 0; i < SortingManager.NUM_BARS; i++) {

            waitForResume();

            int height =  array[i] + 1;
            int width = SortingManager.BAR_WIDTH;
            int x = i + (width - 1) * i;
            int y = SortingManager.WINDOW_HEIGHT - height;

            g2d.setColor(translate(i));
            g2d.fillRect(x, y, width, height);
        }

    }

    //Override the #getPreferredSize method so that the CANVAS is the correct size
        //as opposed to window
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SortingManager.WINDOW_WIDTH, SortingManager.WINDOW_HEIGHT);
    }

}
