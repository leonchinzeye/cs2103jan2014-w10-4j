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
	
	
	
	private static final int EDIT_BOTH_EVENTS_TASKS = 0;
	private static final int EDIT_INCOMPLETE_TASKS = 1;
	private static final int EDIT_INCOMPLETE_EVENTS = 2;
	private static final int FIRST_ARGUMENT = 0;
	private static final int SECOND_ARGUMENT = 1;
	private static final int THIRD_ARGUMENT = 2;
	
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
	
	public void executeEdit(String userInput, FileLinker fileLink, DataUI dataUI, int tableNo, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success = false;
		String[] tokenizedInput = userInput.trim().split("\\s+", 3);
		
		String cmd = tokenizedInput[FIRST_ARGUMENT];
		
		if(cmdTable.containsKey(cmd) != true) {	
			notRecognisableCmd(fileLink, dataUI);
		} else {
			success = identifyCmdAndPerform(cmd, tokenizedInput, fileLink, dataUI, tableNo, undoHandler, dateFormats);
		}
		
		if(success) {
			undoHandler.flushRedo();
		}
	}
	
	private boolean identifyCmdAndPerform(String cmd, String[] tokenizedInput, FileLinker fileLink, DataUI dataUI, int tableNo, Undo undoHandler, DateAndTimeFormats dateFormats) {
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
					success = checkAndGetIncTaskID(userIndex, attributesToEdit, fileLink, dataUI, undoHandler, dateFormats);
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
					success = checkAndGetIncEventID(userIndex, attributesToEdit, fileLink, dataUI, undoHandler, dateFormats);					
				}
				break;
			default:	
				break;				
		}	
		return success;		
	}
	
	/**
	 * This section of the code will extract the actual task ID from the command entered by the 
	 * user and pass it down to the actual editing functions
	 * 
	 * @param userIndex
	 * @param attributesToEdit
	 * @param fileLink
	 * @param dataUI
	 * @param undoHandler
	 * @return
	 */
	
	private boolean checkAndGetIncTaskID(String userIndex, String attributesToEdit, FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {	
		boolean success = true;	
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();			
		success = checkAndGetID(userIndex, attributesToEdit, fileLink, dataUI, undoHandler, incTasks, 1, dateFormats);
		return success;		
	}
	
	
	private boolean checkAndGetIncEventID(String userIndex, String attributesToEdit, FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {		
		boolean success = true;		
		ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();		
		success = checkAndGetID(userIndex, attributesToEdit, fileLink, dataUI, undoHandler, incEvents, 2, dateFormats);
		return success;		
	}
	
	private boolean checkAndGetID(String userIndex, String attributesToEdit,FileLinker fileLink, DataUI dataUI, Undo undoHandler, ArrayList<TaskCard> listOfEntries, int taskOrEvent, DateAndTimeFormats dateFormats) {
		boolean success = true; 
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			if(editIndex <= 0 || editIndex > listOfEntries.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_EDITION_RANGE, listOfEntries.size()));
				return success = false;
			} else {
				TaskCard eventOrTask = listOfEntries.get(editIndex - 1);
				userEnteredID = editIndex;
				if(taskOrEvent == 1) {
					success = performIncTaskEdit(eventOrTask, attributesToEdit, fileLink, dataUI, undoHandler, dateFormats);
				}	else {
					success = performIncEventEdit(eventOrTask, attributesToEdit, fileLink, dataUI, undoHandler, dateFormats);
				}
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, listOfEntries.size()));			
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
	
	private boolean performIncTaskEdit(TaskCard origTask, String attribute, FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success = true;
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeEndDate = false;
		boolean isDate = false;
		boolean isTime = false;
		boolean isDateAndTime = false;
		TaskCard replacementTask = new TaskCard();
		replacementTask.setStartDay(origTask.getStartDay());
		attArray = attribute.split(":", 2);
		
		
		if (attArray.length != 2) {
			dataUI.setFeedback(String.format(FEEDBACK_LEFT_SOMETHING_BLANK, attArray[0]));
			return success = false;
		}	else {
			String parameterToEdit = attArray[0].toLowerCase();
			
			switch(editTable.get(parameterToEdit)) {
				case EDIT_NAME:
					changeName = settingNewName(replacementTask);
					break;
				case EDIT_PRIORITY:
					changePriority = true;
					String typeOfPriority = attArray[1].toLowerCase().trim();
					
					if(priorityTable.get(typeOfPriority) != null) {
						settingNewPriority(replacementTask);
					} else {					
						dataUI.setFeedback(FEEDBACK_INVALID_PRIORITY_LEVEL);					
						return false;					
					}
					break;
				case EDIT_END:
					changeEndDate = true;
					String endDateEntry = attArray[1].trim();
					try {
						Date changeDateAndTime = dateAndTimeFormat.parse(endDateEntry);
						Calendar cal = GregorianCalendar.getInstance();
						cal.setTime(changeDateAndTime);
						replacementTask.setEndDay(cal);
						isDateAndTime = true;
					} catch(ParseException e) {
						
					}
					
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
							cal.set(Calendar.SECOND, origTask.getEndDay().get(Calendar.SECOND));
							cal.set(Calendar.MILLISECOND, origTask.getEndDay().get(Calendar.MILLISECOND));
						} else {						
							cal.set(Calendar.HOUR_OF_DAY, 0);							
							cal.set(Calendar.MINUTE, 0);
							cal.set(Calendar.SECOND, 0);
							cal.set(Calendar.MILLISECOND, 0);
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
						Calendar calChangeDate = GregorianCalendar.getInstance();
						calChangeDate.setTime(changeDate);
						if(!(origTask.getType().equals("FT"))) {							
							cal.setTime(changeDate);							
							cal.set(Calendar.DATE, origTask.getEndDay().get(Calendar.DATE));							
							cal.set(Calendar.MONTH, origTask.getEndDay().get(Calendar.MONTH));							
							cal.set(Calendar.YEAR, origTask.getEndDay().get(Calendar.YEAR));							
						} else {
							cal.set(Calendar.HOUR_OF_DAY, calChangeDate.get(Calendar.HOUR_OF_DAY));							
							cal.set(Calendar.MINUTE, calChangeDate.get(Calendar.MINUTE));
							cal.set(Calendar.SECOND, calChangeDate.get(Calendar.SECOND));
							cal.set(Calendar.MILLISECOND, calChangeDate.get(Calendar.MILLISECOND));
						}						
						replacementTask.setEndDay(cal);						
						isTime = true;
					} catch(ParseException e) {
						
					}
					if(isTime == true) {
						replacementTask.setType("T");
					}
					
					if(!isTime && !isDate && !isDateAndTime) {
						dataUI.setFeedback(FEEDBACK_UNRECOGNIZED_DATE_TIME_FORMAT);
						return success = false;
					}
					break;
				default:
					break;
			}
			/**
			 * if none of the parameters are changed 
			 */
			
			if (!changeName && !changePriority && !changeEndDate) {
				return success = false;
			}
			
			/**
			 * set the unedited task details back into the replacement task 
			 */
			
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
			fileLink.editHandling(replacementTask, userEnteredID - 1, 1);
			
			undoHandler.storeUndo("edit", EDIT_INCOMPLETE_TASKS, origTask, replacementTask);
			RefreshUI.executeRefresh(fileLink, dataUI);		
			return success;
			
		}
		
	}

	private void settingNewPriority(TaskCard replacementTask) {
		if (attArray[1].trim().equalsIgnoreCase("low")) {					
			replacementTask.setPriority(1);					
		} else if (attArray[1].trim().equalsIgnoreCase("med")) {					
			replacementTask.setPriority(2);					
		} else if (attArray[1].trim().equalsIgnoreCase("high")) {					
			replacementTask.setPriority(3);
		}
	}

	private boolean settingNewName(TaskCard replacementTask) {
		boolean changeName;
		changeName = true;
		replacementTask.setName(attArray[1].trim());
		return changeName;
	}
	
	/**
	 * Function for editing incomplete Events
	 * 
	 * @param attribute
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	
	private boolean performIncEventEdit(TaskCard origEvent, String attribute, FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {	
		boolean success = false;
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeStartDate = false;
		boolean changeEndDate = false;
		boolean isDate = false;
		boolean isTime = false;
		TaskCard replacementEvent = new TaskCard();
		attArray = attribute.split(":", 2);
		
		if (attArray.length != 2) {				
			dataUI.setFeedback(String.format(FEEDBACK_LEFT_SOMETHING_BLANK, attArray[0]));
			return success = false;
		} else {
			String parameterToEdit = attArray[0].toLowerCase();
			
			switch(editTable.get(parameterToEdit)) {
				case EDIT_NAME:
					changeName = settingNewName(replacementEvent);
					break;
				case EDIT_PRIORITY:
					changePriority = true;
					String typeOfPriority = attArray[1].toLowerCase().trim();
					
					if(priorityTable.get(typeOfPriority) != null) {
						settingNewPriority(replacementEvent);
					} else {					
						dataUI.setFeedback(FEEDBACK_INVALID_PRIORITY_LEVEL);					
						return false;					
					}
					break;
				case EDIT_START:
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
						dataUI.setFeedback(FEEDBACK_UNRECOGNIZED_DATE_TIME_FORMAT);	
						return success = false;
					}
					break;
				case EDIT_END:
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
						dataUI.setFeedback(FEEDBACK_UNRECOGNIZED_DATE_TIME_FORMAT);
						return success = false;
					}
					break;
				default: 
					break;
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
			}
			
			if (changeStartDate == false) {			
				replacementEvent.setStartDay(origEvent.getStartDay());			
			}
			
			if (isTime) {
				replacementEvent.setType("E");
			}
			else {
				replacementEvent.setType(origEvent.getType());
			}
			
			if(replacementEvent.getEndDay().before(replacementEvent.getStartDay())) {
				dataUI.setFeedback(FEEDBACK_TIME_TRAVEL);
				return false;
			}
			
			dataUI.setFeedback(FEEDBACK_EVENT_EDIT_SUCCESSFUL);		
			fileLink.editHandling(replacementEvent, userEnteredID - 1, 2);
			
			undoHandler.storeUndo("edit", EDIT_INCOMPLETE_EVENTS, origEvent, replacementEvent);
			RefreshUI.executeRefresh(fileLink, dataUI);		
			return success;
		}
		
	}
	
	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {		
		RefreshUI.executeRefresh(fileLink, dataUI);		
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_EDIT_COMMAND);		
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