package application;

import java.util.ArrayList;

/**
 * This class allows the user to mark tasks as complete or
 * mark completed tasks as incomplete.
 * @author Omar Khalid
 *
 */
public class Mark {

	private static final int MARK_INCOMPLETE_TASKS = 1;
	private static final int MARK_INCOMPLETE_EVENTS = 2;
	private static final int UNMARK_COMPLETE_TASKS = 3;
	private static final int UNMARK_COMPLETE_EVENTS = 4;
	
	private static final int SECOND_ARGUMENT = 1;
		
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You didn't specify a task to mark as complete! Please enter an ID!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You didn't specify an event to mark as complete! Please enter an ID!";
	private static final String FEEDBACK_PENDING_COMPLETE_TASK_INDEX = "You didn't specify a task to mark as incomplete! Please enter an ID!";
	private static final String FEEDBACK_PENDING_COMPLETE_EVENT_INDEX = "You didn't specify an event to mark as incomplete! Please enter an ID!";
	private static final String FEEDBACK_MARK_SUCCESSFUL = "\"%s\" has been archived!";
	private static final String FEEDBACK_UNMARK_SUCCESSFUL = "\"%s\" has been unarchived!";
	private static final String FEEDBACK_MARK_RANGE = "Please enter a number between 1 to %d!";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "You didn't enter a number! Please enter a number between 1 to %d!";
	
	public Mark() {
		
	}
	
	public void executeMark(String userInput, FileLinker fileLink, DataUI dataUI, Integer tableNo, Undo undoHandler) {
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
			case MARK_INCOMPLETE_TASKS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX);		
					return success = false;
				} else {
					success = performIncTaskMark(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			case MARK_INCOMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX);
					return success = false;
				} else {
					success = performIncEventMark(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			case UNMARK_COMPLETE_TASKS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_TASK_INDEX);
					return success = false;
				} else {
					success = performComTaskMark(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			case UNMARK_COMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_COMPLETE_EVENT_INDEX);
					return success = false;
				} else {
					success = performComEventMark(userIndex, fileLink, dataUI, undoHandler);
				}
				break;
			default:
				break;
		}
		return success;
	}

	private boolean performIncTaskMark(String userIndex, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		
		try {
			int markedIndex = Integer.parseInt(userIndex);
			
			if(markedIndex <= 0 || markedIndex > incTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_RANGE, incTasks.size()));
				return success = false;
			} else {
				TaskCard taskToBeMarked = incTasks.get(markedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_MARK_SUCCESSFUL, taskToBeMarked.getName()));
				fileLink.markHandling(taskToBeMarked, markedIndex - 1, 1);
				
				undoHandler.storeUndo("mark", MARK_INCOMPLETE_TASKS, taskToBeMarked, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		return success;
	}

	private boolean performIncEventMark(String userIndex, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();
		
		try {
			int markIndex = Integer.parseInt(userIndex);
			
			if(markIndex <= 0 || markIndex > incEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_RANGE, incEvent.size()));
				return success = false;
			} else {
				TaskCard eventToBeMarked = incEvent.get(markIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_MARK_SUCCESSFUL, eventToBeMarked.getName()));
				fileLink.markHandling(eventToBeMarked, markIndex - 1, 2);
				
				undoHandler.storeUndo("mark", MARK_INCOMPLETE_EVENTS, eventToBeMarked, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incEvent.size()));
			success = false;
		}
		return success;
	}
	
	private boolean performComTaskMark(String userIndex, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> comTasks = fileLink.getCompletedTasks();
		
		try {
			int markedIndex = Integer.parseInt(userIndex);
			
			if(markedIndex <= 0 || markedIndex > comTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_RANGE, comTasks.size()));
				return success = false;
			} else {
				TaskCard taskToBeUnmarked = comTasks.get(markedIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_UNMARK_SUCCESSFUL, taskToBeUnmarked.getName()));
				fileLink.markHandling(taskToBeUnmarked, markedIndex - 1, 3);
				
				undoHandler.storeUndo("unmark", UNMARK_COMPLETE_TASKS, taskToBeUnmarked, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, comTasks.size()));
			return success = false;
		}
		return success;
	}

	private boolean performComEventMark(String userIndex, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		ArrayList<TaskCard> comEvent = fileLink.getCompletedEvents();
		
		try {
			int markIndex = Integer.parseInt(userIndex);
			
			if(markIndex <= 0 || markIndex > comEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_MARK_RANGE, comEvent.size()));
				return success = false;
			} else {
				TaskCard eventToBeUnmarked = comEvent.get(markIndex - 1);
				dataUI.setFeedback(String.format(FEEDBACK_UNMARK_SUCCESSFUL, eventToBeUnmarked.getName()));
				fileLink.markHandling(eventToBeUnmarked, markIndex - 1, 4);
				
				undoHandler.storeUndo("unmark", UNMARK_COMPLETE_EVENTS, eventToBeUnmarked, null);
				RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, comEvent.size()));
			success = false;
		}
		return success;
	}
}
