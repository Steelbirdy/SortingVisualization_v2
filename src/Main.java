import util.ControlPanel;
import util.GlobalManager;
import util.SortingManager;

public class Main {

    public static void main(String[] args) {

        SortingManager manager = new SortingManager();
        ControlPanel controlPanel = new ControlPanel();

        Thread visualThread = new Thread(manager);
        Thread controlThread = new Thread(controlPanel);

        GlobalManager.init(visualThread, controlThread, manager, controlPanel);

        GlobalManager.start();

    }

}
