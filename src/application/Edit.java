package application;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author leon
 *
 */
public class Edit {
	
	private static final int EDIT_INCOMPLETE_TASKS = 1;
	private static final int EDIT_INCOMPLETE_EVENTS = 2;
	private static final int EDIT_COMPLETE_TASK = 3;
	private static final int EDIT_COMPLETE_EVENTS = 4;
	
	private static final int FIRST_ARGUMENT = 0;
	private static final int SECOND_ARGUMENT = 1;
		
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You didn't specify an incomplete task to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You didn't specify an incomplete event to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_PENDING_COMPLETE_TASK_INDEX = "You didn't specify an complete task to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_PENDING_COMPLETE_EVENT_INDEX = "You didn't specify an complete event to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_DELETE_SUCCESSFUL = "\"%s\" has been deleted!";
	private static final String FEEDBACK_DELETION_RANGE = "Please enter a valid number between 1 to %d!";
	private static final String FEEDBACK_UNRECOGNISABLE_DELETE_COMMAND = "That was an unrecognisable delete command :(";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "You didn't enter a number! Please enter a number between 1 to %d!";
	
	private HashMap<String, Integer> cmdTable = new HashMap<String, Integer>();
	
	private Integer userEnteredID;
	
	private boolean state_inc_tasks;
	private boolean state_inc_event;
	private boolean state_comp_tasks;
	private boolean state_comp_event;
	
	private boolean state_input_inc_tasks;
	
	public Edit() {
		initialiseCmdTable();
		
		userEnteredID = null;
		
		state_inc_tasks = false;
		state_inc_event = false;
		state_comp_tasks = false;
		state_comp_event = false;
		
		state_input_inc_tasks = false;
	}

	/**
	 * this method checks if the program is currently in any edit error handling state
	 * if it is, it calls the individual edit methods
	 * else, it will identify what type of edit command the user has entered before
	 * handling the user input
	 * if errors still persist, it will remain in a state of error
	 * @param userInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 * the return type will signal to commandhandler whether the delete was successful
	 * or that there was an error involved
	 */
		boolean success = false;
		public boolean checkBeforeExecuteEdit(String userInput, FileLinker fileLink, DataUI dataUI) {
		
		if(newEditCmd()) {
			String[] tokenizedInput = userInput.trim().split("\\s+", 2);
			String cmd = tokenizedInput[FIRST_ARGUMENT];
			
			if(cmdTable.containsKey(cmd) != true) {
				notRecognisableCmd(fileLink, dataUI);
				return success = true;
			} else {
				success = identifyCmdAndPerform(tokenizedInput, fileLink, dataUI);
			}
		} else if(state_inc_tasks == true) {
			success = performIncTaskEdit(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_tasks = false;
				state_input_inc_tasks = true;
				return success = false;
			}
		} else if(state_inc_event == true) {
			success = performIncEventEdit(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_event = false;
			}
		} else if(state_comp_tasks == true) {
			success = performCompTaskEdit(userInput, fileLink, dataUI);
			if(success == true) {
				state_comp_tasks = false;
			}
			
		} else if(state_comp_event) {
			success = performCompEventEdit(userInput, fileLink, dataUI);
			if(success == true) {
				state_comp_event= false;
			}
		} else if(state_input_inc_tasks) {
			success = checkIncInputAndPerform(userInput, fileLink, dataUI);
			if(success) {
				resetStates();
				state_input_inc_tasks = false;
			}
		}
		/*
		 * implement the other 4 else if for the 2nd layer states
		 * these will call the 4 extra functions that you will implement to perform the specific edits
		 */
		return success;
	}
	
	//the actual editing
	
	public boolean executeEdit(String secondUserInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		
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
			case EDIT_INCOMPLETE_TASKS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX);
					state_inc_tasks = true;
					
					return success = false;
				} else {
					success = performIncTaskEdit(userIndex, fileLink, dataUI);
						
					if(success == false) {
						state_inc_tasks = true;
					} else {
						state_inc_tasks = false;
					}
				}
				break;
			case EDIT_INCOMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX);
					state_inc_event = true;
					
					return success = false;
				} else {
					success = performIncEventEdit(userIndex, fileLink, dataUI);
					
					if(success == false) {
						state_inc_event = true;
					} else {
						state_inc_event = false;
					}
				}
				break;
			case EDIT_COMPLETE_TASK:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_TASK_INDEX);
					state_comp_tasks = true;
					
					return success = false;
				} else {
					success = performCompTaskEdit(userIndex, fileLink, dataUI);
						
					if(success == false) {
						state_comp_tasks = true;
					} else {
						state_comp_tasks = false;
					}
				}
				break;
			case EDIT_COMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_EVENT_INDEX);
					state_comp_event = true;
					
					return success = false;
				} else {
					success = performCompEventEdit(userIndex, fileLink, dataUI);
					
					if(success == false) {
						state_comp_event = true;
					} else {
						state_comp_event = false;
					}
				}
				break;
			default:
				break;
		}
		
		return success;
	}

	/**
	 * should name it checkAndGetIncTaskID
	 * @param userIndex
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	private boolean performIncTaskEdit(String userIndex, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > incTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, incTasks.size()));
				return success = false;
			} 
			//edit this one for the edit function to work 
			else {
				TaskCard task = incTasks.get(editIndex - 1);
				
				//set feedback should be
				dataUI.setFeedback(String.format("Please list the parameters you would like to change for %s task based on the column headers", task.getName()));
				
				//this filelink should not be called
				fileLink.deleteHandling(editIndex - 1, EDIT_INCOMPLETE_TASKS);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private boolean performIncEventEdit(String userIndex, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > incEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, incEvent.size()));
				return success = false;
			} else {
				TaskCard event = incEvent.get(editIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, event.getName()));
				fileLink.deleteHandling(editIndex - 1, EDIT_INCOMPLETE_EVENTS);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incEvent.size()));
			success = false;
		}
		
		return success;
	}

	private boolean performCompTaskEdit(String userIndex, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> compTasks = fileLink.getIncompleteTasks();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > compTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, compTasks.size()));
				return success = false;
			} else {
				TaskCard task = compTasks.get(editIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, task.getName()));
				fileLink.deleteHandling(editIndex - 1, EDIT_INCOMPLETE_TASKS);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, compTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private boolean performCompEventEdit(String userIndex, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> compEvent = fileLink.getIncompleteEvents();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > compEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, compEvent.size()));
				return success = false;
			} else {
				TaskCard event = compEvent.get(editIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, event.getName()));
				fileLink.deleteHandling(editIndex - 1, EDIT_INCOMPLETE_EVENTS);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, compEvent.size()));
			success = false;
		}
		
		return success;
	}

	/**
	 * performs the actual edit
	 * create a new task card, put in details from the old one that he didn't want to change
	 * put in new ones that he wanted to change
	 * and call filelinker to edit it. Make sure you pass a new taskcard with the id that the user entered initially
	 * @param dataUI 
	 * @param fileLink 
	 * @return
	 */
	private boolean checkIncInputAndPerform(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		TaskCard task = new TaskCard();
		
		/*
		 * first step is to check if all the parameters make sense
		 */
		
		/*
		 * second step is to pull out the details that he did not want to edit and put them in
		 * helper functions would be good for abstraction
		 */
		
		/*
		 * 3rd step would be to put the new details that he wanted to edit
		 * again helper functions would be good 
		 */
		
		/*
		 * identifying formatting
		 * 1) split based on (";")
		 * 2) idenfity length of the string[];
		 * 3) for loop with i running from 0 to length of string[]
		 * 		- get string[i]
		 * 		- split based on ":"
		 * 		- if the array != 2 means error
		 * 		- if the fields that he type don't make sense, just throw error message eg dataUI.setfeedback(blah blah)
		 * 4) if success, perform edit
		 * 		if error, return the feedback message with false, but must still be in state_input_inc_task
		 */
		
		fileLink.editHandling(task, userEnteredID, 1);
		return success;
	}
	
	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {
		RefreshUI.executeRefresh(fileLink, dataUI);
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_DELETE_COMMAND);
	}

	private boolean newEditCmd() {
		if(state_inc_tasks == false && state_inc_event == false
				&& state_comp_tasks == false && state_comp_event == false
				/*
				 * here you need to check for the other 4 states also and make sure that they are false
				 */) {
			return true;
		}
		return false;
	}

	private void resetStates() {
		state_inc_tasks = false;
		state_inc_event = false;
		state_comp_tasks = false;
		state_comp_event = false;
	}
	
	private void initialiseCmdTable() {
		cmdTable.put("/et", EDIT_INCOMPLETE_TASKS);
		cmdTable.put("/ee", EDIT_INCOMPLETE_EVENTS);
		cmdTable.put("/etc", EDIT_COMPLETE_TASK);
		cmdTable.put("/eec", EDIT_COMPLETE_EVENTS);
	}
}
