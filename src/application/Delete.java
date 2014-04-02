package application;

import java.util.ArrayList;

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
	
	private static final int SECOND_ARGUMENT = 1;
		
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You didn't specify an incomplete task to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You didn't specify an incomplete event to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_PENDING_COMPLETE_TASK_INDEX = "You didn't specify an complete task to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_PENDING_COMPLETE_EVENT_INDEX = "You didn't specify an complete event to delete! Please enter an ID to delete!";
	private static final String FEEDBACK_DELETE_SUCCESSFUL = "\"%s\" has been deleted!";
	private static final String FEEDBACK_DELETION_RANGE = "Please enter a valid number between 1 to %d!";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "You didn't enter a number! Please enter a number between 1 to %d!";
	
	public Delete() {
		
	}

	/**
	 * this method checks if the program is currently in any delete error handling state
	 * if it is, it calls the individual delete methods
	 * else, it will identify what type of delete command the user has entered before
	 * handling the user input
	 * if errors still persist, it will remain in a state of error
	 * @param userInput
	 * @param fileLink
	 * @param dataUI
	 * @param undoHandler 
	 * @return
	 * the return type will signal to commandhandler whether the delete was successful
	 * or that there was an error involved
	 */
	public void executeDelete(String userInput, FileLinker fileLink, DataUI dataUI, Integer tableNo, Undo undoHandler) {
		boolean success = false;

		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
			
		success = identifyCmdAndPerform(tokenizedInput, fileLink, dataUI, tableNo, undoHandler);
		
		if(success) {
			undoHandler.flushRedo();
		}
	}
	
	private boolean identifyCmdAndPerform(String[] tokenizedInput, FileLinker fileLink, DataUI dataUI, Integer tableNo, Undo undoHandler) {
		boolean success = false;
		boolean noIndexArgument = false;
		String userIndex = null;
		
		if(tokenizedInput.length < 2) {
			noIndexArgument = true;
		} else {
			userIndex = tokenizedInput[SECOND_ARGUMENT];
		}
		
		switch(tableNo) {
			case DELETE_INCOMPLETE_TASKS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX);
					return success = false;
				} else {
					success = performIncTaskDelete(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			case DELETE_INCOMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX);
					return success = false;
				} else {
					success = performIncEventDelete(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			case DELETE_COMPLETE_TASK:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_TASK_INDEX);
					return success = false;
				} else {
					success = performCompTaskDelete(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			case DELETE_COMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_EVENT_INDEX);
					return success = false;
				} else {
					success = performCompEventDelete(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			default:
				break;
		}
		return success;
	}

	private boolean performIncTaskDelete(String userIndex, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		
		try {
			int deletedIndex = Integer.parseInt(userIndex);
			
			if(deletedIndex <= 0 || deletedIndex > incTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, incTasks.size()));
				return success = false;
			} else {
				TaskCard task = incTasks.get(deletedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, task.getName()));
				fileLink.deleteHandling(deletedIndex - 1, DELETE_INCOMPLETE_TASKS);
				
				undoHandler.storeUndo("delete", DELETE_INCOMPLETE_TASKS, task, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private boolean performIncEventDelete(String userIndex, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();
		
		try {
			int deletedIndex = Integer.parseInt(userIndex);
			
			if(deletedIndex < 0 || deletedIndex > incEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, incEvent.size()));
				return success = false;
			} else {
				TaskCard event = incEvent.get(deletedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, event.getName()));
				fileLink.deleteHandling(deletedIndex - 1, DELETE_INCOMPLETE_EVENTS);
				
				undoHandler.storeUndo("delete", DELETE_INCOMPLETE_EVENTS, event, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incEvent.size()));
			success = false;
		}
		
		return success;
	}

	private boolean performCompTaskDelete(String userIndex, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> compTasks = fileLink.getCompletedTasks();
		
		try {
			int deletedIndex = Integer.parseInt(userIndex);
			
			if(deletedIndex < 0 || deletedIndex > compTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, compTasks.size()));
				return success = false;
			} else {
				TaskCard task = compTasks.get(deletedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, task.getName()));
				fileLink.deleteHandling(deletedIndex - 1, DELETE_COMPLETE_TASK);
				
				undoHandler.storeUndo("delete", DELETE_COMPLETE_TASK, task, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, compTasks.size()));
			return success = false;
		}
		
		return success;
	}

	private boolean performCompEventDelete(String userIndex, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> compEvent = fileLink.getCompletedEvents();
		
		try {
			int deletedIndex = Integer.parseInt(userIndex);
			
			if(deletedIndex < 0 || deletedIndex > compEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_DELETION_RANGE, compEvent.size()));
				return success = false;
			} else {
				TaskCard event = compEvent.get(deletedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_DELETE_SUCCESSFUL, event.getName()));
				fileLink.deleteHandling(deletedIndex - 1, DELETE_COMPLETE_EVENTS);
				
				undoHandler.storeUndo("delete", DELETE_COMPLETE_EVENTS, event, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, compEvent.size()));
			success = false;
		}
		
		return success;
	}
}
