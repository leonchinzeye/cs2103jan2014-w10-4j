import java.util.ArrayList;

/**
 * this class processes the information that has been handled by the logic
 * and passes it back to the GUI for printing on the GUI
 * 
 * Things to take note of:
 * 		- depending on the command type, it will process the info
 * 		- needs attributes to tell which command
 * @author leon
 *
 */
public class DataUI {

	private ArrayList<ArrayList<String>> incTasks;
	private ArrayList<ArrayList<String>> incEvent;
	private String feedback = null;
	
	private ArrayList<TaskCard> incompleteTasks;
	private ArrayList<TaskCard> incompleteEvents;
	private ArrayList<TaskCard> completedTasks;
	private ArrayList<TaskCard> completedEvents;
	
	
	
	public enum COMMAND_TYPE {
		
	}
	/**
	 * Constructor for DataUI
	 * Takes in an integer and calls the appropriate methods within DataUI
	 * to arrange the information for the GUI to print it out
	 * @param
	 * int cmdType: indicates the command type
	 */
	public DataUI() {
		
	}
}
