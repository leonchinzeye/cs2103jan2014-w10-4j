package application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class is to mark tasks that the user has deemed completed
 * it should encompass the ability to check for the current time 
 * and if it sees that an event has passed it's designated time,
 * it will be marked as done
 * does not include tasks
 * @author leon
 *
 */
public class Mark {

	private static final int MARK_INCOMPLETE_TASKS = 1;
	private static final int MARK_INCOMPLETE_EVENTS = 2;
	private static final int MARK_COMPLETE_TASKS = 3;
	private static final int MARK_COMPLETE_EVENTS = 4;
	
	private static final int FIRST_ARGUMENT = 0;
	private static final int SECOND_ARGUMENT = 1;
		
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You didn't specify a task to mark as complete! Please enter an ID!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You didn't specify an event to mark as complete! Please enter an ID!";
	private static final String FEEDBACK_PENDING_COMPLETE_TASK_INDEX = "You didn't specify a task to mark as incomplete! Please enter an ID!";
	private static final String FEEDBACK_PENDING_COMPLETE_EVENT_INDEX = "You didn't specify an event to mark as incomplete! Please enter an ID!";
	private static final String FEEDBACK_MARK_SUCCESSFUL = "\"%s\" has been archived!";
	private static final String FEEDBACK_UNMARK_SUCCESSFUL = "\"%s\" has been unarchived!";
	private static final String FEEDBACK_MARK_RANGE = "Please enter a number between 1 to %d!";
	private static final String FEEDBACK_UNRECOGNISABLE_MARK_COMMAND = "That was an unrecognisable mark command :(";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "You didn't enter a number! Please enter a number between 1 to %d!";
	
	private boolean state_inc_tasks;
	private boolean state_inc_event;
	private boolean state_comp_tasks;
	private boolean state_comp_event;
	
	private HashMap<String, Integer> cmdTable = new HashMap<String, Integer>();
	
	public Mark() {
		initialiseCmdTable();
		state_inc_tasks = false;
		state_inc_event = false;
		state_comp_tasks = false;
		state_comp_event = false;
	}
	
	public boolean executeMark(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		
		if(newMarkCmd()) {
			String[] tokenizedInput = userInput.trim().split("\\s+", 2);
			String cmd = tokenizedInput[FIRST_ARGUMENT];
			
			if(cmdTable.containsKey(cmd) != true) {
				notRecognisableCmd(fileLink, dataUI);
				return success = false;
			} else {
				success = identifyCmdAndPerform(tokenizedInput, fileLink, dataUI);
			}
		} else if(state_inc_tasks == true) {
			success = performIncTaskMark(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_tasks = false;
			}
		} else {
			success = performIncEventMark(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_event = false;
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
			case MARK_INCOMPLETE_TASKS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX);
					state_inc_tasks = true;
					
					return success = false;
				} else {
					success = performIncTaskMark(userIndex, fileLink, dataUI);
					
					if(success == false) {
						state_inc_tasks = true;
					} else {
						state_inc_tasks = false;
					}
				}
				break;
				
			case MARK_INCOMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX);
					state_inc_event = true;
					
					return success = false;
				} else {
					success = performIncEventMark(userIndex, fileLink, dataUI);
					
					if(success == false) {
						state_inc_event = true;
					} else {
						state_inc_event = false;
					}
				}
				break;
			case MARK_COMPLETE_TASKS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_EVENT_INDEX);
					state_comp_tasks = true;
					
					return success = false;
				} else {
					success = performComTaskMark(userIndex, fileLink, dataUI);
					
					if(success == false) {
						state_comp_tasks = true;
					} else {
						state_comp_tasks = false;
					}
				}
				break;
			case MARK_COMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_EVENT_INDEX);
					state_comp_event = true;
					
					return success = false;
				} else {
					success = performComEventMark(userIndex, fileLink, dataUI);
					
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

	private boolean performIncTaskMark(String userIndex, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		
		try {
			int markedIndex = Integer.parseInt(userIndex);
			
			if(markedIndex < 0 || markedIndex > incTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_RANGE, incTasks.size()));
				return success = false;
			} else {
				TaskCard taskToBeMarked = incTasks.get(markedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_MARK_SUCCESSFUL, taskToBeMarked.getName()));
				fileLink.markHandling(taskToBeMarked, markedIndex - 1, 1);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private boolean performIncEventMark(String userIndex, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();
		
		try {
			int markIndex = Integer.parseInt(userIndex);
			
			if(markIndex < 0 || markIndex > incEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_SUCCESSFUL, incEvent.size()));
				return success = false;
			} else {
				TaskCard eventToBeMarked = incEvent.get(markIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_MARK_SUCCESSFUL, eventToBeMarked.getName()));
				fileLink.markHandling(eventToBeMarked, markIndex - 1, 2);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incEvent.size()));
			success = false;
		}
		
		return success;
	}
	
	private boolean performComTaskMark(String userIndex, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> comTasks = fileLink.getCompletedTasks();
		
		try {
			int markedIndex = Integer.parseInt(userIndex);
			
			if(markedIndex < 0 || markedIndex > comTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_RANGE, comTasks.size()));
				return success = false;
			} else {
				TaskCard taskToBeMarked = comTasks.get(markedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_UNMARK_SUCCESSFUL, taskToBeMarked.getName()));
				fileLink.markHandling(taskToBeMarked, markedIndex - 1, 3);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, comTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private boolean performComEventMark(String userIndex, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> comEvent = fileLink.getCompletedEvents();
		
		try {
			int markIndex = Integer.parseInt(userIndex);
			
			if(markIndex < 0 || markIndex > comEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_SUCCESSFUL, comEvent.size()));
				return success = false;
			} else {
				TaskCard eventToBeMarked = comEvent.get(markIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_UNMARK_SUCCESSFUL, eventToBeMarked.getName()));
				fileLink.markHandling(eventToBeMarked, markIndex - 1, 4);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, comEvent.size()));
			success = false;
		}
		
		return success;
	}
	
	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {
		RefreshUI.executeRefresh(fileLink, dataUI);
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_MARK_COMMAND);
	}

	private boolean newMarkCmd() {
		if(state_inc_tasks == false && state_inc_event == false
				&& state_comp_tasks == false && state_comp_event == false) {
			return true;
		}
		return false;
	}
	
	private void initialiseCmdTable() {
		cmdTable.put("/markt", MARK_INCOMPLETE_TASKS);
		cmdTable.put("/marke", MARK_INCOMPLETE_EVENTS);
		cmdTable.put("/unmarkt", MARK_COMPLETE_TASKS);
		cmdTable.put("/unmarke", MARK_COMPLETE_EVENTS);
	}
	
	/**
	 * this method is supposed to mark those events which are repeated
	 * for shifting the repeated ones to their next designated date
	 * @author leon
	 * @param
	 */
	public static void markRepeated() {
		
	}
}
