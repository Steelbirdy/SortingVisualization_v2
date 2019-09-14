package util;

import algorithms.Algorithms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * SortingManager controls the flow of the visualizations, and responds to commands from the ControlPanel
 * Commands from the ControlPanel must first flow through the GlobalManager, which operates in the main Thread.
 * SortingManager operates in its own separate thread to allow the user to operate the ControlPanel in real time
 */
public class SortingManager implements Runnable {


    //These can be changed as you want
    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720;
    public final static int BAR_WIDTH = 1;
    //The prefix for the title of the window
    private final static String NAME_PREFIX = "Sorting Visualizer v3.0";


    //I do not recommend changing these in the programming
    public final static double ASPECT_RATIO = (double) WINDOW_HEIGHT / WINDOW_WIDTH;
    public final static int NUM_BARS = WINDOW_WIDTH / BAR_WIDTH;
    public final static int DEFAULT_DELAY_MILLISECONDS = 3 * BAR_WIDTH;


    //Initialize the objects related to the canvas
    private JFrame frame;
    private JButton btnPause;
    private ArrayToSort array;
    private ArrayList<Algorithms> sorting;
    private long currentTime;

    //The current delay, in milliseconds, after a write to the array
    private int msDelay = DEFAULT_DELAY_MILLISECONDS;

    //Constructor
    public SortingManager() {

        initFrame();

    }

    //Init elements relating to the canvas
    private void initFrame() {

        frame = new JFrame();
        array = new ArrayToSort(NUM_BARS);
        sorting = new ArrayList<>();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setTitle(NAME_PREFIX);

        frame.add(array, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

    }

    //Used before each sort
    private void prepareArray() {
        array.shuffle();
        array.resetColors();
        Timing.waitFor(Timing.secondsToNano(1));
    }

    /**
     * Initiates a frame counter
     * @param count the number of frames to count
     */
    public void countRepaintsTo(int count) {
        array.countRepaintsTo(count);
    }

    /**
     * Changes the current delay
     * @param delay the new delay, in milliseconds
     */
    public void setDelayMs(int delay) {
        msDelay = delay;
    }

    /**
     * @return the current delay, in milliseconds
     */
    public int getDelayMs() {
        return this.msDelay;
    }

    //The method that controls the actual running of the algorithms
    @Override
    public void run() {

        this.sorting = GlobalManager.getSortingOrder();

        Algorithms temp;

        while((temp = GlobalManager.getNextSort()) != null) {

            //Prepare the array (shuffling, etc)
            frame.setTitle(NAME_PREFIX + " : shuffling...");
            Timing.waitFor(Timing.secondsToNano(2));
            prepareArray();

            // Change the name of the frame to match the algorithm
            frame.setTitle(NAME_PREFIX + " : " + temp.getName());

            //Update the real time the algorithm takes to run on the canvas
            currentTime = temp.timeRun(array);
            array.setRealTime(currentTime);

            //Allow the user to press the "Skip Algorithm" button on the Control Panel
            GlobalManager.toggleCanSkip();

            //Run the algorithm
            temp.run(array);

            //Prevent the user from pressing the "Skip Algorithm" button on the Control Panel
            GlobalManager.toggleCanSkip();

            //If the algorithm was skipped, go right to the next one instead of sweeping
            if(GlobalManager.shouldEnd()) {
                GlobalManager.triggerSkip();
                array.resetArray();
            } else {
                array.finalSweep();
            }

            //Pause if necessary
            if(GlobalManager.stopBtwnSorts())
                GlobalManager.togglePaused();

        }

        //After all of the algorithms have run, reset the colors of the data to white
        array.resetColors();
    }

}
