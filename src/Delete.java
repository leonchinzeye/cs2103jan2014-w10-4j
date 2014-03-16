import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 * delete class that will delete specific tasks
 * 1) check for keyword
 * 			- if exist, will print a list of those tasks containing the keyword
 * 			- else report no such tasks
 * 2) delete tasks on a specific date
 * 			- will pull out all the tasks that are on a specific date
 * 			- if there are no tasks, report no, else show all the tasks on that date
 * @author leon
 *
 */
public class Delete {	
	
	private static final int DELETE_INCOMPLETE_TASKS = 1;
	private static final int DELETE_INCOMPLETE_EVENTS = 2;
	private static final int DELETE_COMPLETE_TASK = 3;
	private static final int DELETE_COMPLETE_EVENTS = 4;
	
	private static final int FIRST_ARGUMENT = 0;
	private static final int SECOND_ARGUMENT = 1;
	
	private static final String COMMAND_QUIT_TO_TOP = "!q";
	
	private static final String FEEDBACK_DELETE_SUCCESSFUL = "\"%s\" has been deleted!";
	private static final String FEEDBACK_DELETION_RANGE = "Please enter a valid number between 1 to %d!";
	private static final String FEEDBACK_UNRECOGNISABLE_DELETE_COMMAND = "That was an unrecognisable delete command :(";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "Please enter a number between 1 to %d!";
	private static final String MESSAGE_PROMPT_GET_KEYWORD = "You did not specify a keyword. "
			+ "Please enter a keyword: ";
	private static final String MESSAGE_LIST_FOR_DELETION = "Here is the list of items that contain "
			+ "your keyword. Which do you want to delete?";
	private static final String MESSAGE_KEYWORD_NOT_FOUND = "\"%s\" is not found among the lists of "
			+ "tasks you have.";

	private HashMap<String, Integer> cmdTable = new HashMap<String, Integer>();	
	
	private boolean state_inc_tasks;
	private boolean state_inc_event;
	private boolean state_comp_tasks;
	private boolean state_comp_event;
	
	public Delete() {
		initialiseCmdTable();
		state_inc_tasks = false;
		state_inc_event = false;
		state_comp_tasks = false;
		state_comp_event = false;
	}

	public boolean executeDelete(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		
		if(newDeleteCmd()) {
			String[] tokenizedInput;
			tokenizedInput = userInput.trim().split("\\s+", 2);
			String cmd = tokenizedInput[FIRST_ARGUMENT];
			
			if(cmdTable.containsValue(cmd) != true) {
				notRecognisableCmd(fileLink, dataUI);
				return success = true;
			} else {
				success = identifyCmdAndPerform(tokenizedInput, fileLink, dataUI);
			}
		} else if(state_inc_tasks == true) {
			
		} else if(state_inc_event == true) {
			
		} else if(state_comp_tasks == true) {
			
		} else {
			
		}
		return success;
	}
	
	private boolean identifyCmdAndPerform(String[] tokenizedInput,
			FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		
		String cmd = tokenizedInput[FIRST_ARGUMENT];
		
		switch(cmdTable.get(cmd)) {
			case DELETE_INCOMPLETE_TASKS:
				success = performIncTaskDelete(tokenizedInput, fileLink, dataUI);
				if(success == false) {
					state_inc_tasks = true;
				}
				break;
			case DELETE_INCOMPLETE_EVENTS:
				
				break;
			case DELETE_COMPLETE_TASK:
				
				break;
			case DELETE_COMPLETE_EVENTS:
				
				break;
		}
		
		return false;
	}

	private boolean performIncTaskDelete(String[] tokenizedInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		
		if(tokenizedInput.length < 2) {
			dataUI.setFeedback("You didn't specify an incomplete task to delete! Please enter an ID to delete!");
			return success = false;
		} 
		
		try {
			int deletedIndex = Integer.parseInt(tokenizedInput[SECOND_ARGUMENT]) - 1;
			
			if(deletedIndex < 0 || deletedIndex > incTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, incTasks.size()));
				return success = false;
			} else {
				TaskCard task = incTasks.get(deletedIndex);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, task.getName()));
				fileLink.deleteHandling(deletedIndex, DELETE_INCOMPLETE_TASKS);
				RefreshUI.executeDis(fileLink, dataUI);
			}
			
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {
		RefreshUI.executeRefresh(fileLink, dataUI);
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_DELETE_COMMAND);
	}

	private boolean newDeleteCmd() {
		if(state_inc_tasks == false && state_inc_event == false
				&& state_comp_tasks == false && state_comp_event == false) {
			return true;
		}
		return false;
	}

	private void initialiseCmdTable() {
		cmdTable.put("/dt", DELETE_INCOMPLETE_TASKS);
		cmdTable.put("/de", DELETE_INCOMPLETE_EVENTS);
		cmdTable.put("/dtc", DELETE_COMPLETE_TASK);
		cmdTable.put("/dec", DELETE_COMPLETE_EVENTS);
	}
}
