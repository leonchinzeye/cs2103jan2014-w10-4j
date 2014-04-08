package application;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Leon/Atul
 *
 */

public class Edit {
	
	private static final String FEEDBACK_EXTRA_DETAILS = "Did you enter something extra?";

	private static final String FEEDBACK_MISSING_DETAILS = "Are you missing something?";

	private static final String FEEDBACK_ENTER_PROPER_RANGE = "Please enter a number between 1 to %d";

	private static final String FEEDBACK_NOTHING_TO_BE_EDITED = "There is nothing to be edited!";

	private static final String TYPE_EDIT = "edit";
	
	private static final int EDIT_BOTH_EVENTS_TASKS = 0;
	private static final int EDIT_INCOMPLETE_TASKS = 1;
	private static final int EDIT_INCOMPLETE_EVENTS = 2;
	private static final int FIRST_ARG = 0;
	private static final int SECOND_ARG = 1;
	private static final int THIRD_ARGUMENT = 2;
	
	private static final int TYPE_INC_TASKS = 1;
	private static final int TYPE_INC_EVENTS = 2;
	
	private static final int PRIORITY_LOW = 1;
	private static final int PRIORITY_MED = 2;
	private static final int PRIORITY_HIGH = 3;
	
	private static final int EDIT_NAME = 1;
	private static final int EDIT_PRIORITY = 2;
	private static final int EDIT_START = 3;
	private static final int EDIT_END = 4;
	
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You seem to have forgotten something! Please enter an ID to edit!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You seem to have forgotten something! Please enter an ID to edit!";
	private static final String FEEDBACK_PENDING_EVENT_ATTRIBUTES = "You didn't specify anything to edit for event %s!";
	private static final String FEEDBACK_PENDING_TASK_ATTRIBUTES = "You didn't specify anything to edit for task %s!";
	private static final String FEEDBACK_TASK_EDIT_SUCCESSFUL = "The task has been successfully edited!";
	private static final String FEEDBACK_EVENT_EDIT_SUCCESSFUL = "The event has been successfully edited!";
	private static final String FEEDBACK_EDITION_RANGE = "Please enter a number between 1 to %d!";
	private static final String FEEDBACK_UNRECOGNISABLE_EDIT_COMMAND = "That was an unrecognisable edit command :(";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "You didn't enter a number! Please enter a number between 1 to %d!";
	private static final String FEEDBACK_INVALID_PRIORITY_LEVEL = "You've entered an invalid priority level :(";
	private static final String FEEDBACK_UNRECOGNIZED_DATE_TIME_FORMAT = "The format you entered for editing the date and time was not recognized";
	private static final String FEEDBACK_LEFT_SOMETHING_BLANK = "You seem to have left %s blank!";
	private static final String FEEDBACK_TIME_TRAVEL = "Greaaat Scott! Are you a time traveler?";
	
	private HashMap<String, Integer> cmdTable = new HashMap<String, Integer>();
	private HashMap<String, Integer> editTable = new HashMap<String, Integer>();
	private HashMap<String, Integer> priorityTable = new HashMap<String, Integer>();
	
	private Integer userEnteredID;
	private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private String[] attArray;
	
	public Edit() {
		initialiseCmdTable();
		initialiseEditTable();
		intialisePriorityTable();
		userEnteredID = null;
		dateAndTimeFormat.setLenient(false);
		dateFormat.setLenient(false);
		timeFormat.setLenient(false);
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
	 * the return type will signal to commandhandler whether the edit was successful
	 * or that there was an error involved
	 */
	
	public boolean executeEdit(String userInput, FileLinker fileLink, 
			DataUI dataUI, int tableNo, Undo undoHandler, 
			DateAndTimeFormats dateFormats) {
		boolean success = false;
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		String cmd = tokenizedInput[FIRST_ARG];
		
		if(tokenizedInput.length < 2) {
			dataUI.setFeedback("Are you missing the task number and details you want to edit?");
			return false;
		}
		
		if(cmdTable.containsKey(cmd) != true) {	
			notRecognisableCmd(fileLink, dataUI);
			return false;
		} else {
			success = identifyCmdAndPerform(tokenizedInput, fileLink, dataUI, tableNo, undoHandler, dateFormats);
		}
		
		if(success) {
			undoHandler.flushRedo();
		}
		
		return success;
	}
	
	private boolean identifyCmdAndPerform(String[] tokenizedInput, 
			FileLinker fileLink, DataUI dataUI, int tableNo, 
			Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success = false;
		String cmd = tokenizedInput[FIRST_ARG];
		String[] indexAndDetails = tokenizedInput[SECOND_ARG].trim().split("\\s+", 2);
		
		if(indexAndDetails.length < 2) {
			dataUI.setFeedback("Are you missing the details that you want to edit?");
			return false;
		}
		
		switch(cmdTable.get(cmd)) {
			case EDIT_BOTH_EVENTS_TASKS:
				if(tableNo == EDIT_INCOMPLETE_TASKS) {
					success = checkIndexIncTaskEdit(indexAndDetails, fileLink, dataUI, undoHandler, dateFormats);
				} else if(tableNo == EDIT_INCOMPLETE_EVENTS){
					success = checkIndexIncEventEdit(indexAndDetails, fileLink, dataUI, undoHandler, dateFormats);
				}
				break;
			case EDIT_INCOMPLETE_TASKS:
				success = checkIndexIncTaskEdit(indexAndDetails, fileLink, dataUI, undoHandler, dateFormats);
				break;
			case EDIT_INCOMPLETE_EVENTS:
				success = checkIndexIncEventEdit(indexAndDetails, fileLink, dataUI, undoHandler, dateFormats);
				break;
			default:
				break;
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
	
	private boolean checkIndexIncTaskEdit(String[] indexAndDetails, FileLinker fileLink, 
			DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		int indexToBeEdited = getIndex(indexAndDetails[FIRST_ARG]);
		boolean success = false;
		
		if(incTasks.size() == 0) {
			dataUI.setFeedback(FEEDBACK_NOTHING_TO_BE_EDITED);
			return false;
		}
		
		if(indexToBeEdited == -1 || indexToBeEdited < 0 || indexToBeEdited > incTasks.size()) {
			dataUI.setFeedback(String.format(FEEDBACK_ENTER_PROPER_RANGE, incTasks.size()));
			return false;
		}
		
		String paramAndDetails = indexAndDetails[SECOND_ARG];
		success = identifyParamsAndEditIncTask(paramAndDetails, fileLink, dataUI, undoHandler, dateFormats, indexToBeEdited);
		return success;
	}
	
	private boolean checkIndexIncEventEdit(String[] indexAndDetails, FileLinker fileLink, 
			DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {	
		boolean success = false;
		ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();
		int indexToBeEdited = getIndex(indexAndDetails[FIRST_ARG]);
		
		if(incEvents.size() == 0) {
			dataUI.setFeedback(FEEDBACK_NOTHING_TO_BE_EDITED);
			return false;
		}
		
		if(indexToBeEdited == -1 || indexToBeEdited < 0 || indexToBeEdited > incEvents.size()) {
			dataUI.setFeedback(String.format(FEEDBACK_ENTER_PROPER_RANGE, incEvents.size()));
			return false;
		}
	
		String paramAndDetails = indexAndDetails[SECOND_ARG];
		success = identifyParamsAndEditIncEvent(paramAndDetails, fileLink, dataUI, undoHandler, dateFormats, indexToBeEdited);
		return success;
	}

	private boolean identifyParamsAndEditIncEvent(String paramAndDetails,
      FileLinker fileLink, DataUI dataUI, Undo undoHandler,
      DateAndTimeFormats dateFormats, int indexToBeEdited) {
		boolean success = false;
		String[] paramAndDetailsSplit = paramAndDetails.trim().split(":");
		
		if(paramAndDetailsSplit.length < 2) {
			dataUI.setFeedback(FEEDBACK_MISSING_DETAILS);
			return false;
		} else if(paramAndDetailsSplit.length > 2) {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS);
			return false;
		}
		
		String param = paramAndDetailsSplit[FIRST_ARG].toLowerCase().trim();
		switch(editTable.get(param)) {
			case EDIT_NAME:
				success = editName(paramAndDetailsSplit[SECOND_ARG], fileLink, dataUI, undoHandler, indexToBeEdited, TYPE_INC_EVENTS);
				break;
			case EDIT_PRIORITY:
				success = editPriority(paramAndDetailsSplit[SECOND_ARG], fileLink, dataUI, undoHandler, indexToBeEdited, TYPE_INC_EVENTS);
				break;
			case EDIT_START:
				success = editStartEvent(paramAndDetailsSplit[SECOND_ARG], fileLink, dataUI, undoHandler, indexToBeEdited);
				break;
			case EDIT_END:
				success = editEndEvent(paramAndDetailsSplit[SECOND_ARG], fileLink, dataUI, undoHandler, indexToBeEdited);
				break;
			default:
				break;
		}
		return success;
  }

	private boolean editStartEvent(String string, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler, int indexToBeEdited) {
	  // TODO Auto-generated method stub
	  return false;
  }

	private boolean editEndEvent(String string, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler, int indexToBeEdited) {
	  // TODO Auto-generated method stub
	  return false;
  }

	private boolean identifyParamsAndEditIncTask(String paramAndDetails, FileLinker fileLink, 
			DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats, int indexToBeEdited) {
		boolean success = false;
		String[] paramAndDetailsSplit = paramAndDetails.trim().split(":");
		
		if(paramAndDetailsSplit.length < 2) {
			dataUI.setFeedback(FEEDBACK_MISSING_DETAILS);
			return false;
		} else if(paramAndDetailsSplit.length > 2) {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS);
			return false;
		}
		
		String param = paramAndDetailsSplit[FIRST_ARG].toLowerCase().trim();
		switch(editTable.get(param)) {
			case EDIT_NAME:
				success = editName(paramAndDetailsSplit[SECOND_ARG], fileLink, dataUI, undoHandler, indexToBeEdited, TYPE_INC_TASKS);
				break;
			case EDIT_PRIORITY:
				success = editPriority(paramAndDetailsSplit[SECOND_ARG], fileLink, dataUI, undoHandler, indexToBeEdited, TYPE_INC_TASKS);
				break;
			case EDIT_END:
				success = editTaskEnd(paramAndDetailsSplit[SECOND_ARG], fileLink, dataUI, undoHandler, indexToBeEdited);
				break;
			default:
				break;
		}
		
		return success;
	}
	
	private boolean editName(String name, FileLinker fileLink, DataUI dataUI,
			Undo undoHandler, int indexToBeEdited, int type) {
		boolean success = true;
		ArrayList<TaskCard> fileToBeEdited = new ArrayList<TaskCard>();
	
		if(type == TYPE_INC_TASKS) {
			fileToBeEdited = fileLink.getIncompleteTasks();
		} else {
			fileToBeEdited = fileLink.getIncompleteEvents();
		}
		
		TaskCard origTask = fileToBeEdited.get(indexToBeEdited - 1);
		TaskCard editedTask = (TaskCard) origTask.clone();
		
		editedTask.setName(name.trim());
		
		if(type == TYPE_INC_TASKS) {
			dataUI.setFeedback("Task edited!");
		} else {
			dataUI.setFeedback("Event edited!");
		}
		
		fileLink.editHandling(editedTask, indexToBeEdited, type);
		undoHandler.storeUndo(TYPE_EDIT, type, origTask, editedTask);
		RefreshUI.executeRefresh(fileLink, dataUI);
		
		return success;
	}
	
	private boolean editPriority(String priority, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler, int indexToBeEdited, int type) {
		boolean success = false;
		ArrayList<TaskCard> fileToBeEdited = new ArrayList<TaskCard>();
		
		if(!priorityTable.containsKey(priority)) {
			dataUI.setFeedback("You didn't enter a proper priority!");
			return false;
		}
		
		if(type == TYPE_INC_TASKS) {
			fileToBeEdited = fileLink.getIncompleteTasks();
		} else {
			fileToBeEdited = fileLink.getIncompleteEvents();
		}
		
		TaskCard origTask = fileToBeEdited.get(indexToBeEdited - 1);
		TaskCard editedTask = (TaskCard) origTask.clone();
		
		switch(priorityTable.get(priority)) {
			case PRIORITY_LOW:
				editedTask.setPriority(PRIORITY_LOW);
				break;
			case PRIORITY_MED:
				editedTask.setPriority(PRIORITY_MED);
				break;
			case PRIORITY_HIGH:
				editedTask.setPriority(PRIORITY_HIGH);
				break;
			default: 
				break;
		}
		
		if(type == TYPE_INC_TASKS) {
			dataUI.setFeedback("Task edited!");
		} else {
			dataUI.setFeedback("Event edited!");
		}
		
		fileLink.editHandling(editedTask, indexToBeEdited - 1, type);
		undoHandler.storeUndo(TYPE_EDIT, type, origTask, editedTask);
		RefreshUI.executeRefresh(fileLink, dataUI);
		success = true;
		
		return success;
	}

	private boolean editTaskEnd(String string, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler, int indexToBeEdited) {
		// TODO Auto-generated method stub
		return false;
	}

	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {		
		RefreshUI.executeRefresh(fileLink, dataUI);		
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_EDIT_COMMAND);		
	}
	
	private int getIndex(String string) {
		try {
			int index = Integer.parseInt(string);
			return index;
		} catch(NumberFormatException e) {
			return -1;
		}
	}

	private void initialiseCmdTable() {
		cmdTable.put("edit", EDIT_BOTH_EVENTS_TASKS);
		cmdTable.put("editt", EDIT_INCOMPLETE_TASKS);		
		cmdTable.put("edite", EDIT_INCOMPLETE_EVENTS);		
	}
	
	private void initialiseEditTable() {
		editTable.put("name", 1);
		editTable.put("priority", 2);
		editTable.put("start", 3);
		editTable.put("end", 4);
	}
	
	private void intialisePriorityTable() {
		priorityTable.put("low", 1);
		priorityTable.put("med", 2);
		priorityTable.put("medium", 2);
		priorityTable.put("high", 3);
		
	}
}