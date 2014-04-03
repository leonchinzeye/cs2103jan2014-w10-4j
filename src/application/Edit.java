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
	
	private static final int EDIT_INCOMPLETE_TASKS = 1;
	private static final int EDIT_INCOMPLETE_EVENTS = 2;
	private static final int FIRST_ARGUMENT = 0;
	private static final int SECOND_ARGUMENT = 1;
	private static final int THIRD_ARGUMENT = 2;
	
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You didn't specify an task to edit! Please enter an ID to edit!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You didn't specify an event to edit! Please enter an ID to edit!";
	private static final String FEEDBACK_PENDING_EVENT_ATTRIBUTES = "You didn't specify anything to edit for event %s!";
	private static final String FEEDBACK_PENDING_TASK_ATTRIBUTES = "You didn't specify anything to edit for task %s!";
	private static final String FEEDBACK_TASK_EDIT_SUCCESSFUL = "The task has been edited!";
	private static final String FEEDBACK_EVENT_EDIT_SUCCESSFUL = "The event has been edited!";
	private static final String FEEDBACK_EDITION_RANGE = "Please enter a valid number between 1 to %d!";
	private static final String FEEDBACK_UNRECOGNISABLE_EDIT_COMMAND = "That was an unrecognisable edit command :(";
	private static final String FEEDBACK_NOT_NUMBER_ENTERED = "You didn't enter a number! Please enter a number between 1 to %d!";
	
	private HashMap<String, Integer> cmdTable = new HashMap<String, Integer>();
	
	private Integer userEnteredID;
	private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private String[] attArray;
	
	public Edit() {
		initialiseCmdTable();
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
	
	public void executeEdit(String userInput, FileLinker fileLink, DataUI dataUI, int tableNo, Undo undoHandler) {
		boolean success = false;
		String[] tokenizedInput = userInput.trim().split("\\s+", 3);
		
		String cmd = tokenizedInput[FIRST_ARGUMENT];
		
		if(cmdTable.containsKey(cmd) != true) {	
			notRecognisableCmd(fileLink, dataUI);
		} else {
			success = identifyCmdAndPerform(cmd, tokenizedInput, fileLink, dataUI, tableNo, undoHandler);
		}
		
		if(success) {
			undoHandler.flushRedo();
		}
	}
	
	private boolean identifyCmdAndPerform(String cmd, String[] tokenizedInput, FileLinker fileLink, DataUI dataUI, int tableNo, Undo undoHandler) {
		boolean success = false;	
		boolean noIndexArgument = false;
		boolean noAttributesArgument = false;
		boolean noIndexOrAttributesArgument = false;
		String userIndex = null;
		String attributesToEdit = null;
		int size = 0;
		
		if (tableNo == EDIT_INCOMPLETE_TASKS) {
			ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
			size = incTasks.size();
		} else if (tableNo == EDIT_INCOMPLETE_EVENTS) {
			ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();
			size = incEvents.size();
		}
		
		if (tokenizedInput.length < 2) {	
			noIndexOrAttributesArgument = true; //if there's no index in the argument
		} else if (tokenizedInput.length < 3) {
			userIndex = tokenizedInput[1];
			try {
				Integer.parseInt(tokenizedInput[1]);
			} catch(NumberFormatException ex) {
				noIndexArgument = true;
			}
			noAttributesArgument = true; //if there's no specified attribute in the argument
		} else {
			userIndex = tokenizedInput[SECOND_ARGUMENT];
			attributesToEdit = tokenizedInput[THIRD_ARGUMENT];
		}
		
		switch(tableNo) {
			case EDIT_INCOMPLETE_TASKS:
				if (noIndexArgument == true) {
					dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, size));					
					return success = false;					
				} else if (noIndexOrAttributesArgument) {					
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX);
					return success = false;
				} else if (noAttributesArgument) {
					dataUI.setFeedback(String.format(FEEDBACK_PENDING_TASK_ATTRIBUTES, userIndex));
					return success = false;
				} else {
					success = checkAndGetIncTaskID(userIndex, attributesToEdit, fileLink, dataUI, undoHandler);
				}
				break;
			case EDIT_INCOMPLETE_EVENTS:				
				if (noIndexArgument == true) {		
					dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, size));
					return success = false;
				} else if (noIndexOrAttributesArgument) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX);					
					return success = false;					
				} else if (noAttributesArgument) {				
					dataUI.setFeedback(String.format(FEEDBACK_PENDING_EVENT_ATTRIBUTES, userIndex));					
					return success = false;				
				} else {				
					success = checkAndGetIncEventID(userIndex, attributesToEdit, fileLink, dataUI, undoHandler);					
				}
				break;
			default:	
				break;				
		}	
		return success;		
	}
	
	private boolean checkAndGetIncTaskID(String userIndex, String attributesToEdit, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {	
		boolean success = true;	
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();		
		
		try {		
			int editIndex = Integer.parseInt(userIndex);					
			if(editIndex <= 0 || editIndex > incTasks.size()) {				
				dataUI.setFeedback(String.format(FEEDBACK_EDITION_RANGE, incTasks.size()));				
				return success = false;				
			} else {			
				TaskCard task = incTasks.get(editIndex - 1);				
				userEnteredID = editIndex;				
				success = performIncTaskEdit(task, attributesToEdit, fileLink, dataUI, undoHandler);				
			}
		} catch(NumberFormatException ex) {		
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));			
			return success = false;		
		}		
		return success;		
	}
		
	private boolean checkAndGetIncEventID(String userIndex, String attributeToEdit, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {		
		boolean success = true;		
		ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();		
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			if(editIndex <= 0 || editIndex > incEvent.size()) {				
				dataUI.setFeedback(String.format(FEEDBACK_EDITION_RANGE, incEvent.size()));				
				return success = false;				
			} else {				
				TaskCard event = incEvent.get(editIndex - 1);				
				userEnteredID = editIndex;				
				success = performIncEventEdit(event, attributeToEdit, fileLink, dataUI, undoHandler);				
			}
		} catch(NumberFormatException ex) {			
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incEvent.size()));			
			return success = false;			
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
	
	private boolean performIncTaskEdit(TaskCard origTask, String attribute, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = true;
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeEndDate = false;
		boolean isDate = false;
		boolean isTime = false;
		TaskCard replacementTask = new TaskCard();
		replacementTask.setStartDay(origTask.getStartDay());
		attArray = attribute.split(":", 2);
		
		if (attArray.length < 2) {
			dataUI.setFeedback(String.format("You seem to have left %s blank!", attArray[0]));				
		} else if (attArray[0].equalsIgnoreCase("Name")){
			changeName = true;
			replacementTask.setName(attArray[1].trim());				
		} else if (attArray[0].equalsIgnoreCase("Priority")) {
			changePriority = true;
			if (attArray[1].trim().equalsIgnoreCase("low")) {					
				replacementTask.setPriority(1);					
			} else if (attArray[1].trim().equalsIgnoreCase("med")) {					
				replacementTask.setPriority(2);					
			} else if (attArray[1].trim().equalsIgnoreCase("high")) {					
				replacementTask.setPriority(3);					
			} else {					
				dataUI.setFeedback("You've entered an invalid priority level :(");					
				return false;					
			}
		} else if(attArray[0].equalsIgnoreCase("End")) {
			/*
			 * Need to fix this whole section, and need to add End Time editing below
			 */
			changeEndDate = true;
			String endDateEntry = attArray[1].trim();
			try {					
				Date changeDate = dateFormat.parse(endDateEntry);						
				//here we need to check if the task already has an end time 						
				//if it does and it is not being modified, it need to be saved and 						
				//set for replacement task												
				Calendar cal = GregorianCalendar.getInstance();						
				cal.setTime(changeDate);						
				if(!(origTask.getType().equals("FT"))) {							
					cal.set(Calendar.HOUR_OF_DAY, origTask.getEndDay().get(Calendar.HOUR_OF_DAY));							
					cal.set(Calendar.MINUTE, origTask.getEndDay().get(Calendar.MINUTE));							
				} else {						
					cal.set(Calendar.HOUR_OF_DAY, 0);							
					cal.set(Calendar.MINUTE, 00);							
				}						
				replacementTask.setEndDay(cal);						
				isDate = true;
			} catch(ParseException e) {
					
			}					

			if(isDate == true) {
				replacementTask.setType("T");
			}
			try {
				Date changeDate = timeFormat.parse(endDateEntry);						
				//here we need to check if the task already has
				//an end date and if it is not to be modified, need to
				//set it for replacement task
				Calendar cal = GregorianCalendar.getInstance();												
				if(!(origTask.getType().equals("FT"))) {							
					cal.setTime(changeDate);							
					cal.set(Calendar.DATE, origTask.getEndDay().get(Calendar.DATE));							
					cal.set(Calendar.MONTH, origTask.getEndDay().get(Calendar.MONTH));							
					cal.set(Calendar.YEAR, origTask.getEndDay().get(Calendar.YEAR));							
				} else {
					cal.set(Calendar.HOUR_OF_DAY, changeDate.getHours());							
					cal.set(Calendar.MINUTE, changeDate.getMinutes());							
				}						
				replacementTask.setEndDay(cal);						
				isTime = true;
			} catch(ParseException e) {
				
			}
			if(isTime == true) {
				replacementTask.setType("T");
			}
			
			if((isTime && isDate) == false) {
				dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				return success = false;
			}
		}
		
		if (!changeName && !changePriority && !changeEndDate) {
			return success = false;
		}
		
		if(changeName == false) {
			replacementTask.setName(origTask.getName());
		}
		
		if (changePriority == false) {			
			replacementTask.setPriority(origTask.getPriority());					
		}
		
		if(changeEndDate == false) {			
			replacementTask.setEndDay(origTask.getEndDay());			
			replacementTask.setType(origTask.getType());			
		}
		
		dataUI.setFeedback(FEEDBACK_TASK_EDIT_SUCCESSFUL);		
		fileLink.editHandling(replacementTask, userEnteredID, 1);
		
		undoHandler.storeUndo("edit", EDIT_INCOMPLETE_TASKS, replacementTask, null);
		RefreshUI.executeRefresh(fileLink, dataUI);		
		return success;		
	}
	
	/**
	 * Function for editing incomplete Events
	 * 
	 * @param attribute
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
		
	private boolean performIncEventEdit(TaskCard origEvent, String attribute, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {	
		boolean success = false;
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeStartDate = false;
		boolean changeEndDate = false;
		boolean isDate = false;
		boolean isTime = false;
		TaskCard replacementEvent = new TaskCard();
		attArray = attribute.split(":", 2);

		if (attArray.length < 2) {				
			dataUI.setFeedback(String.format("You seem to have left %s blank!", attArray[0]));				
		} else if (attArray[0].equalsIgnoreCase("Name")){
			changeName = true;
			replacementEvent.setName(attArray[1].trim());				
		} else if (attArray[0].equalsIgnoreCase("Priority")) {
			changePriority = true;
			if (attArray[1].trim().equalsIgnoreCase("low")) {					
				replacementEvent.setPriority(1);
			}	else if (attArray[1].trim().equalsIgnoreCase("med")) {					
				replacementEvent.setPriority(2);					
			} else if (attArray[1].trim().equalsIgnoreCase("high")) {					
				replacementEvent.setPriority(3);					
			} else {					
				dataUI.setFeedback("You've entered an invalid priority level :(");					
				return false;					
			}
		} else if (attArray[0].equalsIgnoreCase("Start")) {
			changeStartDate = true;
			String startDateEntry = attArray[1].trim();
			try {						
				Date changeDate = dateFormat.parse(startDateEntry);						
				Calendar cal = GregorianCalendar.getInstance();						
				cal.setTime(changeDate);						
				cal.set(Calendar.HOUR_OF_DAY, origEvent.getStartDay().get(Calendar.HOUR_OF_DAY));						
				cal.set(Calendar.MINUTE, origEvent.getStartDay().get(Calendar.MINUTE));						
				replacementEvent.setStartDay(cal);						
				isDate = true;
			} catch (ParseException e) {												
				isDate = false;
			}

			try {					
				Date changeDate = timeFormat.parse(startDateEntry);						
				Calendar cal = GregorianCalendar.getInstance();						
				cal.setTime(changeDate);						
				cal.set(Calendar.DATE, origEvent.getStartDay().get(Calendar.DATE));						
				cal.set(Calendar.MONTH, origEvent.getStartDay().get(Calendar.MONTH));						
				cal.set(Calendar.YEAR, origEvent.getStartDay().get(Calendar.YEAR));						
				replacementEvent.setStartDay(cal);						
				isTime = true;
			} catch (ParseException e) {
				isTime = false;
			}
			if (!isTime && !isDate) {				
				dataUI.setFeedback("The format you entered for editing the date and time was not recognized");	
				return success = false;
			}				
		} else if (attArray[0].equalsIgnoreCase("End")) {
			changeEndDate = true;
			String endDateEntry = attArray[1].trim();			
			try {						
				Date changeDate = dateFormat.parse(endDateEntry);												
				Calendar cal = GregorianCalendar.getInstance();						
				cal.setTime(changeDate);						
				cal.set(Calendar.HOUR_OF_DAY, origEvent.getEndDay().get(Calendar.HOUR_OF_DAY));						
				cal.set(Calendar.MINUTE, origEvent.getEndDay().get(Calendar.MINUTE));						
				replacementEvent.setEndDay(cal);						
				isDate = true;
			} catch (ParseException e) {						
				isDate = false;
			}

			try {						
				Date changeDate = timeFormat.parse(endDateEntry);						
				Calendar cal = GregorianCalendar.getInstance();						
				cal.setTime(changeDate);						
				cal.set(Calendar.DATE, origEvent.getEndDay().get(Calendar.DATE));					
				cal.set(Calendar.MONTH, origEvent.getEndDay().get(Calendar.MONTH));						
				cal.set(Calendar.YEAR, origEvent.getEndDay().get(Calendar.YEAR));						
				replacementEvent.setEndDay(cal);						
				isTime = true;
			} catch (ParseException e) {						
				isTime = false;
			}
			if (!isTime && !isDate) {					
				dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				return success = false;
			}				
		}
		
		if (!changeName && !changePriority && !changeStartDate && !changeEndDate) {				
			return success = false;
		}
		
		if(changeName == false) {			
			replacementEvent.setName(origEvent.getName());			
		}
		
		if (changePriority == false) {			
			replacementEvent.setPriority(origEvent.getPriority());			
		}
		
		if (changeEndDate == false) {			
			replacementEvent.setEndDay(origEvent.getEndDay());			
			replacementEvent.setType(origEvent.getType());			
		}
		
		if (changeStartDate == false) {			
			replacementEvent.setStartDay(origEvent.getStartDay());			
			replacementEvent.setType(origEvent.getType());			
		}
		
		dataUI.setFeedback(FEEDBACK_EVENT_EDIT_SUCCESSFUL);		
		fileLink.editHandling(replacementEvent, userEnteredID, 2);
		
		undoHandler.storeUndo("edit", EDIT_INCOMPLETE_EVENTS, replacementEvent, null);
		RefreshUI.executeRefresh(fileLink, dataUI);		
		return success;	
	}
	
	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {		
		RefreshUI.executeRefresh(fileLink, dataUI);		
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_EDIT_COMMAND);		
	}
	
	private void initialiseCmdTable() {
		cmdTable.put("edit", 0);
		cmdTable.put("editt", EDIT_INCOMPLETE_TASKS);		
		cmdTable.put("edite", EDIT_INCOMPLETE_EVENTS);		
	}
}