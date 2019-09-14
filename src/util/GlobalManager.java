package util;

import algorithms.Algorithm;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalManager {

    private static boolean global_paused;
    private static Thread sorting_manager_thread, control_panel_thread;
    private static SortingManager manager;
    private static ControlPanel control;

    private final static AtomicBoolean STOP_BETWEEN_SORTS = new AtomicBoolean(false);
    private final static AtomicBoolean TO_END_ALGORITHM = new AtomicBoolean(false);

    public static void init(Thread sortingManager, Thread controlPanel,
                            SortingManager manager, ControlPanel control) {
        global_paused = false;
        GlobalManager.sorting_manager_thread = sortingManager;
        GlobalManager.control_panel_thread = controlPanel;
        GlobalManager.manager = manager;
        GlobalManager.control = control;

        control_panel_thread.setDaemon(true);
    }

    public static void start() {
        control_panel_thread.start();
    }

    public static void beginSort() {
        sorting_manager_thread.start();
    }

    public static ArrayList<Algorithm> getSortingOrder() { return control.getSortingOrder(); }

    public static boolean isPaused() { return global_paused; }
    public static boolean togglePaused() {
        global_paused ^= true;
        sorting_manager_thread.interrupt();
        return global_paused;
    }

    public static Algorithm getNextSort() {
        return control.cutSort();
    }

    public static void initiateRepaintCount(int count) {
        manager.countRepaintsTo(count);
    }

    public static void toggleStopBtwnSorts() {
        STOP_BETWEEN_SORTS.set(!STOP_BETWEEN_SORTS.get());
    }
    public static boolean stopBtwnSorts() {
        if(STOP_BETWEEN_SORTS.get()) control.toggleBtnShowPaused();
        return STOP_BETWEEN_SORTS.get();
    }
    public static int getDelayMs() {
        return manager.getDelayMs();
    }
    public static void setDelayMs(int delay) {
        manager.setDelayMs(delay);
    }

    public static void triggerSkip() {
        TO_END_ALGORITHM.set(!TO_END_ALGORITHM.get());
    }
    public static boolean shouldEnd() {
        return TO_END_ALGORITHM.get();
    }

    public static void toggleCanSkip() { control.toggleCanSkip(); }

}
