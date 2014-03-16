import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
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
	
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You didn't specify an incomplete task to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You didn't specify an incomplete event to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_DELETE_SUCCESSFUL = "\"%s\" has been deleted!";
	private static final String FEEDBACK_DELETION_RANGE = "Please enter a valid number between 1 to %d!";
	private static final String FEEDBACK_UNRECOGNISABLE_DELETE_COMMAND = "That was an unrecognisable delete command :(";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "You didn't enter a number! Please enter a number between 1 to %d!";
	
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
			
			if(cmdTable.containsKey(cmd) != true) {
				notRecognisableCmd(fileLink, dataUI);
				return success = true;
			} else {
				success = identifyCmdAndPerform(tokenizedInput, fileLink, dataUI);
			}
		} else if(state_inc_tasks == true) {
			success = performIncTaskDelete(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_tasks = false;
			}
		} else if(state_inc_event == true) {
			success = performIncEventDelete(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_event = false;
			}
		} else if(state_comp_tasks == true) {
			success = performCompTaskDelete(userInput, fileLink, dataUI);
			if(success == true) {
				state_comp_tasks = false;
			}
		} else {
			success = performCompEventDelete(userInput, fileLink, dataUI);
			if(success == true) {
				state_comp_event= false;
			}
		}
		return success;
	}
	
	private boolean identifyCmdAndPerform(String[] tokenizedInput,
			FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		boolean noIndexArgument = false;
		String userIndex = null;
		
		if(tokenizedInput.length < 2) {
			noIndexArgument = true;
		} else {
			userIndex = tokenizedInput[SECOND_ARGUMENT];
		}
		
		String cmd = tokenizedInput[FIRST_ARGUMENT];
		
		switch(cmdTable.get(cmd)) {
			case DELETE_INCOMPLETE_TASKS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX);
					state_inc_tasks = true;
					
					return success = false;
				} else {
					success = performIncTaskDelete(userIndex, fileLink, dataUI);
						
					if(success == false) {
						state_inc_tasks = true;
					}
				}
				break;
			case DELETE_INCOMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX);
					state_inc_event = true;
				}
				break;
			case DELETE_COMPLETE_TASK:
				
				break;
			case DELETE_COMPLETE_EVENTS:
				
				break;
		}
		
		return success;
	}

	private boolean performIncTaskDelete(String userIndex, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		
		try {
			int deletedIndex = Integer.parseInt(userIndex);
			
			if(deletedIndex < 0 || deletedIndex > incTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, incTasks.size()));
				return success = false;
			} else {
				TaskCard task = incTasks.get(deletedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, task.getName()));
				fileLink.deleteHandling(deletedIndex - 1, DELETE_INCOMPLETE_TASKS);
				RefreshUI.executeDis(fileLink, dataUI);
			}
			
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private boolean performIncEventDelete(String userInput, FileLinker fileLink,
			DataUI dataUI) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean performCompTaskDelete(String userInput, FileLinker fileLink,
			DataUI dataUI) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean performCompEventDelete(String userInput, FileLinker fileLink,
			DataUI dataUI) {
		// TODO Auto-generated method stub
		return false;
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
