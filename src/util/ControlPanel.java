package util;

import algorithms.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * The ControlPanel is a secondary window that allows the user to control many more aspects
 *      of how the algorithms run. They can pause at any time, change the millisecond delay,
 *      advance individual frames, and change the order of the algorithms to run.
 * In general, the ControlPanel sends commands to the GlobalManager, which forwards them to
 *      the SortingManager.
 */
public class ControlPanel implements Runnable {

    //Don't change these
    private final static int WINDOW_WIDTH = 246;
    private final static int WINDOW_HEIGHT = 550;
    private final static ArrayList<Algorithm> ALL_ALGORITHMS = new ArrayList<>();

    //Declare components of the JFrame
    private JFrame frmControl;
    private JPanel pnlContentPane;
    private JButton btnPause, btnFrameUp, btnBigFrameUp, btnSkip;
    private JSpinner spnrDelay;
    private JLabel lblDelay;
    private JButton btnListUp, btnListDown, btnListAdd, btnListRemove, btnListEdit;
    private JList<Algorithm> sortList;
    private JCheckBox chkbxStop;
    private JScrollPane pnList;

    //The order of algorithms to run
    private ArrayList<Algorithm> sortingOrder;

    //Default constructor
    public ControlPanel() {}

    //Initialize everything, including the list ALL_ALGORITHMS
    private void init() {

        initFrame();

        initComponents();

        initDefaultSortingOrder();

        ALL_ALGORITHMS.add(new BubbleSort());
        ALL_ALGORITHMS.add(new CombSort());
        ALL_ALGORITHMS.add(new HeapSort());
        ALL_ALGORITHMS.add(new InsertionSort());
        ALL_ALGORITHMS.add(new MergeSort());
        ALL_ALGORITHMS.add(new QuickSort());
        ALL_ALGORITHMS.add(new RadixSort());
        ALL_ALGORITHMS.add(new SelectionSort());
        ALL_ALGORITHMS.add(new ShellSort());
        ALL_ALGORITHMS.add(new TimSort());

        frmControl.setVisible(true);

    }

    //Initialize the frame and content pane
    private void initFrame() {

        //Initialize the frame itself
        frmControl = new JFrame("Controller");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frmControl.setBounds(
                (int) screen.getWidth() - (WINDOW_WIDTH), 10,
                WINDOW_WIDTH, WINDOW_HEIGHT
        ); //The ControlPanel is placed at the right top of the screen
        frmControl.setResizable(false); //This is intentional, because the window is formatted for its size only.
        frmControl.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //Don't let the ControlPanel be closed.

        //Initialize the contentPane, which contains all other components
        pnlContentPane = new JPanel();
        pnlContentPane.setLayout(null); //Absolute Positioning
        pnlContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        frmControl.setContentPane(pnlContentPane);

    }

    //Initialize the components inside of pnlContentPane
    private void initComponents() {

        /*
         * btnPause allows the user to pause the visualization.
         * Each algorithm listens to the GlobalManager to determine
         * when it should and should not pause through calls to Thread#interrupt
         */
        btnPause = new JButton("Start");
        btnPause.setBackground(Color.RED); //Red when stopped, green when running
        btnPause.setBounds(10, 12, 100, 40);
        btnPause.addActionListener(e -> {
            if(btnPause.getText().equals("Start")) {
                GlobalManager.beginSort();
                btnPause.setText("Pause");
                btnPause.setBackground(Color.GREEN);
                btnListRemove.setEnabled(false);
                btnListAdd.setEnabled(false);
                btnListEdit.setEnabled(false);
                btnListUp.setEnabled(false);
                btnListDown.setEnabled(false);
            } else {
                btnPause.setText((GlobalManager.togglePaused() ? "Resume" : "Pause"));
                btnPause.setBackground((GlobalManager.isPaused() ? Color.RED : Color.GREEN));

                btnFrameUp.setEnabled(GlobalManager.isPaused());
                btnBigFrameUp.setEnabled(GlobalManager.isPaused());

                btnListRemove.setEnabled(GlobalManager.isPaused());
                btnListAdd.setEnabled(GlobalManager.isPaused());
                btnListEdit.setEnabled(GlobalManager.isPaused());
                btnListUp.setEnabled(GlobalManager.isPaused());
                btnListDown.setEnabled(GlobalManager.isPaused());
            }
        });
        pnlContentPane.add(btnPause);

        // btnFrameUp allows the user to advance by single frames
        btnFrameUp = new JButton("+1 Frame");
        btnFrameUp.setEnabled(false);
        btnFrameUp.addActionListener(e -> GlobalManager.initiateRepaintCount(1));
        btnFrameUp.setBounds(10, 64, 100, 40);
        pnlContentPane.add(btnFrameUp);

        // btnBigFrameUp allows the user to advance by 10 frames at once
        btnBigFrameUp = new JButton("+10 Frames");
        btnBigFrameUp.setEnabled(false);
        btnBigFrameUp.addActionListener(e -> GlobalManager.initiateRepaintCount(10));
        btnBigFrameUp.setBounds(120, 64, 100, 40);
        pnlContentPane.add(btnBigFrameUp);

        // btnSkip allows the user to skip the current algorithm altogether
        btnSkip = new JButton("Skip Algorithm");
        btnSkip.setEnabled(false);
        btnSkip.addActionListener(e -> {
            GlobalManager.triggerSkip();
        });
        btnSkip.setBounds(10, 116, 210, 40);
        pnlContentPane.add(btnSkip);

        // spnrDelay allows the user to change the minimum delay in between repaints
        spnrDelay = new JSpinner();
        spnrDelay.setModel(new SpinnerNumberModel(SortingManager.DEFAULT_DELAY_MILLISECONDS, 1, 99, 1));
        spnrDelay.addChangeListener(e -> {
            GlobalManager.setDelayMs((int) spnrDelay.getValue());
        });
        spnrDelay.setBounds(187, 12, 33, 20);
        pnlContentPane.add(spnrDelay);

        lblDelay = new JLabel("Delay (ms)");
        lblDelay.setHorizontalAlignment(SwingConstants.CENTER);
        lblDelay.setBounds(122, 14, 67, 16);
        pnlContentPane.add(lblDelay);

        //If chkbxStop is selected, the visualization will pause after each sort
        chkbxStop = new JCheckBox("Stop btwn sorts");
        chkbxStop.addActionListener(e -> {
            GlobalManager.toggleStopBtwnSorts();
        });
        chkbxStop.setBounds(115, 33, 140, 24);
        pnlContentPane.add(chkbxStop);

        // btnListRemove removes the selected algorithm from the list
        btnListRemove = new JButton("Remove");
        btnListRemove.addActionListener(e -> {
            if(sortList.getSelectedValue() != null) {
                ((SortingListModel) sortList.getModel()).removeElement(sortList.getSelectedIndex());
            }
        });
        btnListRemove.setBounds(10, 225, 100, 26);
        pnlContentPane.add(btnListRemove);

        // btnListUp moves the selected algorithm one place up in the list
        btnListUp = new JButton("\u2191");
        btnListUp.addActionListener(e -> {
            if(sortList.getSelectedValue() != null && sortList.getSelectedIndex() != 0) {
                int i;
                ((SortingListModel) sortList.getModel()).swapElements(
                        i = sortList.getSelectedIndex() - 1, sortList.getSelectedIndex()
                );
                sortList.setSelectedIndex(i);
            }
        });
        btnListUp.setBounds(120, 225, 45, 26);
        pnlContentPane.add(btnListUp);

        // btnListDown moves the selected algorithm one place down in the list
        btnListDown = new JButton("\u2193");
        btnListDown.addActionListener(e -> {
            if(sortList.getSelectedValue() != null && sortList.getSelectedIndex() != sortList.getModel().getSize() - 1) {
                int i;
                ((SortingListModel) sortList.getModel()).swapElements(
                        sortList.getSelectedIndex(), i = sortList.getSelectedIndex() + 1
                );
                sortList.setSelectedIndex(i);
            }
        });
        btnListDown.setBounds(177, 225, 45, 26);
        pnlContentPane.add(btnListDown);

        // btnListAdd opens a dialog that allows the user to add another algorithm to the list
        btnListAdd = new JButton("Add");
        btnListAdd.addActionListener(e -> {
            Algorithm toAdd = new SortDialog().getResult();
            ((SortingListModel) sortList.getModel()).addElement(toAdd, 0);
            sortList.setSelectedIndex(0);
        });
        btnListAdd.setBounds(10, 187, 100, 27);
        pnlContentPane.add(btnListAdd);

        //TODO: Allow the user to edit different instances of algorithms individually
        /**/
        btnListEdit = new JButton("Edit");
        btnListEdit.setEnabled(false);
        btnListEdit.setBounds(120, 187, 102, 27);
        pnlContentPane.add(btnListEdit);

        //The list object that displays the algorithms in order of when they will run
        sortList = new JList<>();
        sortList.setModel(this.new SortingListModel());

        //The scroll pane in case there are too many algorithms to display
        pnList = new JScrollPane(sortList);
        pnList.setBounds(10, 263, 212, 238);
        pnlContentPane.add(pnList);

    }

    //Sets the order of algorithms to run to the default.
    //  This is only run once, upon initialization.
    private void initDefaultSortingOrder() {

        sortingOrder = new ArrayList<>();

        sortingOrder.add(new TimSort());
        sortingOrder.add(new RadixSort());
        sortingOrder.add(new CombSort());
        sortingOrder.add(new ShellSort());
        sortingOrder.add(new HeapSort());
        sortingOrder.add(new SelectionSort());
        sortingOrder.add(new MergeSort());
        sortingOrder.add(new QuickSort());
        sortingOrder.add(new InsertionSort());
        sortingOrder.add(new BubbleSort()); //BubbleSort takes absolutely forever to show with arrays of size 1280

        ((SortingListModel) sortList.getModel()).addElements(sortingOrder);

    }

    //Access to the algorithms that need to be run
    public ArrayList<Algorithm> getSortingOrder() { return this.sortingOrder; }

    /**
     * Cuts the next algorithm from the list to prepare for it running next
     * @return the algorithm that was cut
     */
    public Algorithm cutSort() {
        try {
            if (sortList.getModel().getElementAt(0) != null)
                return ((SortingListModel) sortList.getModel()).removeElement(0);
            else return null;
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            return null;
        }
    }

    public void toggleBtnShowPaused() {
        btnPause.setText("Resume");
        btnPause.setBackground(Color.RED);
    }

    //Toggles the visibility of the skip button
    public void toggleCanSkip() {
        btnSkip.setEnabled(!btnSkip.isEnabled());
    }

    /**
     * Inherited from {@link Runnable}
     * Runs when the ControlPanel thread from the Main class is started.
     */
    @Override
    public void run() {

        init();

    }

    /**
     * A custom list model that allows greater accessibility to the algorithms listed.
     * Also ensures that the list updates correctly
     */
    public class SortingListModel extends AbstractListModel<Algorithm> {

        private final ArrayList<Algorithm> listData = new ArrayList<>();

        public void swapElements(int index0, int index1) {
            Algorithm temp0 = removeElement(index0), temp1 = removeElement(index1 - 1);
            addElement(temp1, index0);
            addElement(temp0, index1);
        }

        public void addElement(Algorithm object) {
            sortingOrder.add(object);
            fireIntervalAdded(this, listData.size() - 1, listData.size() - 1);
        }

        public void addElement(Algorithm object, int index) {
            sortingOrder.add(index, object);
            fireIntervalAdded(this, index, index);
        }

        public void addElements(ArrayList<Algorithm> arr) {
            int ind = listData.size();
            listData.addAll(arr);
            fireIntervalAdded(this, ind, listData.size() - 1);
        }

        public Algorithm removeElement(int index) {
            Algorithm tr = sortingOrder.remove(index);
            fireIntervalRemoved(this, index, index);
            return tr;
        }

        @Override
        public int getSize() {
            return sortingOrder.size();
        }

        @Override
        public Algorithm getElementAt(int index) {
            return sortingOrder.get(index);
        }
    }

    public class SortDialog extends JDialog {

        private JPanel contentPane;
        private JComboBox<Algorithm> chooser;
        private JButton btnClose;

        private final static int FRAME_WIDTH = 100;
        private final static int FRAME_HEIGHT = 50;

        public SortDialog() {
            super(new JFrame(), "Add...", true);
            setTitle("Add...");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds((int) screen.getWidth() / 2 - FRAME_WIDTH, (int) screen.getHeight() / 2 - FRAME_HEIGHT,
                    FRAME_WIDTH, FRAME_HEIGHT);
            setContentPane(contentPane = new JPanel());
            contentPane.setLayout(new FlowLayout());

            chooser = new JComboBox<>();
            ALL_ALGORITHMS.forEach(chooser::addItem);
            contentPane.add(chooser);

            btnClose = new JButton("Done");
            btnClose.addActionListener(e -> dispose());
            contentPane.add(btnClose);

            pack();
            setVisible(true);
        }

        public Algorithm getResult() {
            return (Algorithm) chooser.getSelectedItem();
        }

    }

}
