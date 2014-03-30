package application;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Add2 {
	
	private int ARRAY_FIRST_ARG = 0;
	private int ARRAY_SECOND_ARG = 1;
	
	private final String TYPE_FLOATING_TASK = "FT";
	
	private final int DEFAULT_FLOATING_TASKS_PRIORITY = 1;
	private final int DEFAULT_TASKS_AND_EVENTS_PRIORITY = 2;
	
	private final int ADD_TO_TASKS = 1;
	private final int ADD_TO_EVENTS = 2;
	
	private Calendar floatingDefaultEndDay = new GregorianCalendar(9999, 11, 31, 23, 59);
	
	private final String FEEDBACK_NO_ARG_ENTERED = "Please enter a task/event to be added!";
	private final String FEEDBACK_EXTRA_DETAILS_ARG = "You've entered something extra :(  Please re-enter!";
	private final String FEEDBACK_SUCCESSFUL_ADD_TASK = "Task added!";
	private final String FEEDBACK_SUCCESSFUL_ADD_EVENT = "Event added!";
	
	public Add2() {
		
	}
	
	public boolean executeAdd(String userInput, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = false;
		
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		if(tokenizedInput.length < 2) {
			dataUI.setFeedback(FEEDBACK_NO_ARG_ENTERED);
			return false;
		} else {
			String userDetails = tokenizedInput[ARRAY_SECOND_ARG];
			success = identifyTypeAndPerform(userDetails, fileLink, dataUI, undoHandler);
		}
		return success;
	}
	
	private boolean identifyTypeAndPerform(String userDetails,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success;
		
		String[] details = userDetails.trim().split(";");
		
		if(details.length == 1) {
			success = addTask(details, fileLink, dataUI, undoHandler);
		} else if(details.length == 2) {
			success = addEvent(details, fileLink, dataUI, undoHandler);
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG);
			return false;
		}
		
		return success;
	}
	
	private boolean addTask(String[] details, FileLinker fileLink, DataUI dataUI,
	    Undo undoHandler) {
	  boolean success;
		
		String[] detailsAndTime = details[ARRAY_FIRST_ARG].trim().split("due by|due on|to be done by");
		if(detailsAndTime.length == 1) {
			success = addFloatingTask(detailsAndTime[ARRAY_FIRST_ARG], fileLink, dataUI, undoHandler);
		} else if(detailsAndTime.length == 2) {
			success = addDueDateTask(detailsAndTime, fileLink, dataUI, undoHandler);
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG);
			return false;
		}
		
	  return success;
	}

	private boolean addDueDateTask(String[] detailsAndTime, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler) {
	  
	  return false;
  }

	private boolean addFloatingTask(String taskDetails, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler) {
	  TaskCard taskToBeAdded = new TaskCard();
	  
	  setFloatingTaskDetails(taskDetails, taskToBeAdded);
	  dataUI.setFeedback(FEEDBACK_SUCCESSFUL_ADD_TASK);
	  fileLink.addHandling(taskToBeAdded, ADD_TO_TASKS);
	  RefreshUI.executeRefresh(fileLink, dataUI);
	  undoHandler.storeUndo("add", ADD_TO_TASKS, taskToBeAdded, null);
	  
	  return true;
  }

	private void setFloatingTaskDetails(String taskDetails, TaskCard taskToBeAdded) {
	  taskToBeAdded.setName(taskDetails);
	  taskToBeAdded.setPriority(DEFAULT_FLOATING_TASKS_PRIORITY);
	  taskToBeAdded.setType(TYPE_FLOATING_TASK);
	  taskToBeAdded.setStartDay(GregorianCalendar.getInstance());
	  taskToBeAdded.setEndDay(floatingDefaultEndDay);
	  taskToBeAdded.setFrequency("N");
  }

	private boolean addEvent(String[] details, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler) {
	  // TODO Auto-generated method stub
	  return false;
  }

	private void initVar() {
		
	}
}
