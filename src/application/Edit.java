package application;
import java.util.ArrayList;
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
	private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private String[] parameterToEdit;
	
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
	public void executeEdit(String userInput, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = false;

		String[] tokenizedInput = userInput.trim().split("\\s+", 3);
		String cmd = tokenizedInput[FIRST_ARGUMENT];
			
		if(cmdTable.containsKey(cmd) != true) {
			notRecognisableCmd(fileLink, dataUI);
		} else {
			success = identifyCmdAndPerform(cmd, tokenizedInput, fileLink, dataUI);
		}
		
		if(success) {
			undoHandler.flushRedo();
		}
	}
	
	private boolean identifyCmdAndPerform(String cmd, String[] tokenizedInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		boolean noIndexArgument = false;
		boolean noAttributesArgument = false;
		boolean noIndexOrAttributesArgument = false;
		String userIndex = null;
		String attributesToEdit = null;
		int size = 0;
		
		if (cmd.equals("editt")) {
			ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
			size = incTasks.size();
		} else if (cmd.equals("edite")) {
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
		
		switch(cmdTable.get(cmd)) {
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
					success = checkAndGetIncTaskID(userIndex, attributesToEdit, fileLink, dataUI);
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
					success = checkAndGetIncEventID(userIndex, attributesToEdit, fileLink, dataUI);
				}
				break;
			default:
				break;
		}
		return success;
	}
	
	private boolean checkAndGetIncTaskID(String userIndex, String attributesToEdit, FileLinker fileLink, DataUI dataUI) {
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
				success = performIncTaskEdit(task, attributesToEdit, fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		return success;
	}
	
	private boolean checkAndGetIncEventID(String userIndex, String attributesToEdit, FileLinker fileLink, DataUI dataUI) {
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
				success = performIncEventEdit(event, attributesToEdit, fileLink, dataUI);
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
	private boolean performIncTaskEdit(TaskCard origTask, String attributes, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		TaskCard replacementTask = new TaskCard();
		
		replacementTask.setStartDay(origTask.getStartDay());
		
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeEndDate = false;
		
		boolean isDateAndTime = false;
		boolean isDate = false;
		boolean isTime = false;
		
		String[] tokenizedAttributes = attributes.split(";");
		
		/*
		 * first step is to check if all the parameters make sense
		 */
		
		for(int i = 0; i < tokenizedAttributes.length; i++) {
			if (tokenizedAttributes[i].contains("Name: ")) {
				changeName = true;
			}
			if (tokenizedAttributes[i].contains("Priority: ")) {
				changePriority = true;
			}
			if (tokenizedAttributes[i].contains("End Date: ")) {
				changeEndDate = true;
			}
			if (!changeName && !changePriority && !changeEndDate) {
				return success = false;
			}
		}
		
		if(changeName == false) {
			replacementTask.setName(origTask.getName());
		}
		
		if (changePriority == false) {
			replacementTask.setPriority(origTask.getPriority());
			
		}
		
		/*
		 * Need to correct this one.
		 */
		if(changeEndDate == false) {
			replacementTask.setEndDay(origTask.getEndDay());
			replacementTask.setType(origTask.getType());
		}
		
		/*
		 * identifying formatting
		 * 1) split based on (";")
		 * 2) identify length of the string[];
		 * 3) for loop with i running from 0 to length of string[]
		 * 		- get string[i]
		 * 		- split based on ":"
		 * 		- if the array != 2 means error
		 * 		- if the fields that he type don't make sense, just throw error message eg dataUI.setfeedback(blah blah)
		 * 4) if success, perform edit
		 * 		if error, return the feedback message with false, but must still be in state_input_inc_task
		 */
		
		for(int i = 0; i < tokenizedAttributes.length; i++) {
			parameterToEdit = tokenizedAttributes[i].split(":", 2);
			
			if (parameterToEdit.length != 2) {
				dataUI.setFeedback(String.format("You seem to have left %s blank!", parameterToEdit[0]));
			} else if (parameterToEdit[0].equalsIgnoreCase("Name")){
				replacementTask.setName(parameterToEdit[1].trim());
			} else if (parameterToEdit[0].equalsIgnoreCase("Priority")) {
				//check the String if LOW then set to 1, MED etc.
				if (parameterToEdit[1].trim().equalsIgnoreCase("low")) {
					replacementTask.setPriority(1);
				} else if (parameterToEdit[1].trim().equalsIgnoreCase("med")) {
					replacementTask.setPriority(2);
				} else if (parameterToEdit[1].trim().equalsIgnoreCase("high")) {
					replacementTask.setPriority(3);
				} else {
					dataUI.setFeedback("You've entered an invalid priority level :(");
					return false;
				}
			} else if(parameterToEdit[0].equalsIgnoreCase("End Date")) {
				/*
				 * Need to fix this whole section, and need to add End Time editing below
				 */
				String endDateEntry = parameterToEdit[1].trim();				
				//check if time changes (reserved keywords: nth, empty, nothing) 
				
				try {
					Date changeDate = dateAndTimeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementTask.setEndDay(cal);
					isDateAndTime = true;
					continue;
				} catch(ParseException e) {
					//may be date or time format only
				}
				
				if((origTask.getType().equals("FT")) && (isDateAndTime == true)) {
					replacementTask.setType("T");
				}
				
				
				try {
					Date changeDate = dateFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementTask.setEndDay(cal);
					isDate = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				if((origTask.getType().equals("FT")) && (isDate == true)) {
					replacementTask.setType("T");
				}
				
				try {
					Date changeDate = timeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementTask.setEndDay(cal);
					isTime = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				if((origTask.getType().equals("FT")) && (isTime == true)) {
					replacementTask.setType("T");
				}
				
				if((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}
			}
		}
		
		dataUI.setFeedback(FEEDBACK_TASK_EDIT_SUCCESSFUL);
		fileLink.editHandling(replacementTask, userEnteredID, 1);
		RefreshUI.executeRefresh(fileLink, dataUI);
		return success;
	}
	
	/**
	 * Function for editing incomplete Events
	 * 
	 * @param attributes
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	
	private boolean performIncEventEdit(TaskCard origEvent, String attributes, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		TaskCard replacementEvent = new TaskCard();
		
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeStartDate = false;
		boolean changeEndDate = false;
		
		boolean isDateAndTime = false;
		boolean isDate = false;
		boolean isTime = false;
		
		/*
		 * first step is to check if all the parameters make sense
		 */
		String[] tokenizedAttributes = attributes.split(";");
		
		for (int i = 0; i < tokenizedAttributes.length; i++) {
				if (tokenizedAttributes[i].contains("Name: ")) {
					changeName = true;
				}
				if (tokenizedAttributes[i].contains("Priority: ")) {
					changePriority = true;
				}
				if (tokenizedAttributes[i].contains("Start Date: ")) {
					changeStartDate = true;
				}
				if (tokenizedAttributes[i].contains("End Date: ")) {
					changeEndDate = true;
				}
				if (!changeName && !changePriority && !changeStartDate && !changeEndDate) {
					return success = false;
				}
		}
		
		/*
		 * second step is to pull out the details that he did not want to edit and put them in
		 * helper functions would be good for abstraction
		 */
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
		
		/*
		 * identifying formatting
		 * 1) split based on (";")
		 * 2) identify length of the string[];
		 * 3) for loop with i running from 0 to length of string[]
		 * 		- get string[i]
		 * 		- split based on ":"
		 * 		- if the array != 2 means error
		 * 		- if the fields that he type don't make sense, just throw error message eg dataUI.setfeedback(blah blah)
		 * 4) if success, perform edit
		 * 		if error, return the feedback message with false, but must still be in state_input_inc_task
		 */
	
		for (int i = 0; i < tokenizedAttributes.length; i++) {
			parameterToEdit = tokenizedAttributes[i].split(":", 2);
			
			if (parameterToEdit.length != 2) {
				dataUI.setFeedback(String.format("You seem to have left %s blank!", parameterToEdit[0]));
			} else if (parameterToEdit[0].equalsIgnoreCase("Name")){
				replacementEvent.setName(parameterToEdit[1].trim());
			} else if (parameterToEdit[0].equalsIgnoreCase("Priority")) {
				//check the String if LOW then set to 1, MED etc.
				if (parameterToEdit[1].trim().equalsIgnoreCase("low")) {
					replacementEvent.setPriority(1);
				}	else if (parameterToEdit[1].trim().equalsIgnoreCase("med")) {
					replacementEvent.setPriority(2);
				} else if (parameterToEdit[1].trim().equalsIgnoreCase("high")) {
					replacementEvent.setPriority(3);
				} else {
					dataUI.setFeedback("You've entered an invalid priority level :(");
					return false;
				}
				/*
				 * Need to settle edit for Start Date, Start Time, End Date, and End Time below.
				 */
			} else if (parameterToEdit[0].equalsIgnoreCase("Start Date")) {
				String startDateEntry = parameterToEdit[1].trim();
				
				try {
					Date changeDate = dateAndTimeFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDateAndTime = true;
					continue;
				} catch (ParseException e) {
					//may be date or time format only
				}
				
				try {
					Date changeDate = dateFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDate = true;
					continue;
				} catch (ParseException e) {
					
				}
				
				try {
					Date changeDate = timeFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isTime = true;
					continue;
				} catch (ParseException e) {
					
				}
				
				if ((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}		
			} else if (parameterToEdit[0].contains("End Date")) {
				String endDateEntry = parameterToEdit[1].trim();
				
				try {
					Date changeDate = dateAndTimeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDateAndTime = true;
					continue;
				} catch (ParseException e) {
					//may be date or time format only
				}
				
				try {
					Date changeDate = dateFormat.parse(endDateEntry);
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDate = true;
					continue;
				} catch (ParseException e) {
					
				}
				
				try {
					Date changeDate = timeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isTime = true;
					continue;
				} catch (ParseException e) {
					
				}
				
				if ((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}
			}
		}
		
		dataUI.setFeedback(FEEDBACK_EVENT_EDIT_SUCCESSFUL);
		fileLink.editHandling(replacementEvent, userEnteredID, 2);
		RefreshUI.executeRefresh(fileLink, dataUI);
		return success;
	}
	
	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {
		RefreshUI.executeRefresh(fileLink, dataUI);
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_EDIT_COMMAND);
	}
	
	private void initialiseCmdTable() {
		cmdTable.put("editt", EDIT_INCOMPLETE_TASKS);
		cmdTable.put("edite", EDIT_INCOMPLETE_EVENTS);
	}
}
