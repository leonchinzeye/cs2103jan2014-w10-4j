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
	private static final int EDIT_COMPLETE_TASK = 3;
	private static final int EDIT_COMPLETE_EVENTS = 4;
	
	private static final int FIRST_ARGUMENT = 0;
	private static final int SECOND_ARGUMENT = 1;
	
	private static final String FEEDBACK_PENDING_INCOMPLETE_TASK_INDEX = "You didn't specify an incomplete task to edit! Please enter an ID to edit!";
	private static final String FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX = "You didn't specify an incomplete event to edit! Please enter an ID to edit!";
	private static final String FEEDBACK_PENDING_COMPLETE_TASK_INDEX = "You didn't specify an complete task to edit! Please enter an ID to edit!";
	private static final String FEEDBACK_PENDING_COMPLETE_EVENT_INDEX = "You didn't specify an complete event to edit! Please enter an ID to edit!";
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
	private String[] parameterToEnter;
	
	//state to check if the command is of known format  
	private boolean state_inc_tasks;
	private boolean state_inc_event;
	private boolean state_comp_tasks;
	private boolean state_comp_event;
	
	//states for second layer of checks 
	private boolean state_input_inc_tasks;
	private boolean state_input_inc_event;
	private boolean state_input_comp_tasks;
	private boolean state_input_comp_event;
	
	public Edit() {
		initialiseCmdTable();
		
		userEnteredID = null;
		
		state_inc_tasks = false;
		state_inc_event = false;
		state_comp_tasks = false;
		state_comp_event = false;
		
		state_input_inc_tasks = false;
		state_input_inc_event = false;
		state_input_comp_tasks = false;
		state_input_comp_event = false;
		
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
	
	public boolean checkBeforeExecuteEdit(String userInput, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = false;
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
			success = checkAndGetIncTaskID(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_tasks = false;
				state_input_inc_tasks = true;
				return success = false;
			}
		} else if(state_inc_event == true) {
			success = checkAndGetIncEventID(userInput, fileLink, dataUI);
			if(success == true) {
				state_inc_event = false;
				state_input_inc_event = true;
				return success = false;
			}
		} else if(state_comp_tasks == true) {
			success = checkAndGetCompTaskID(userInput, fileLink, dataUI);
			if(success == true) {
				state_comp_tasks = false;
				state_input_comp_tasks = true;
				return success = false;	
			}
			
		} else if(state_comp_event) {
			success = checkAndGetCompEventID(userInput, fileLink, dataUI);
			if(success == true) {
				state_comp_event = false;
				state_input_comp_event = true;
				return success = false;
			}
		} else if(state_input_inc_tasks) {
			success = checkIncTaskInputAndPerform(userInput, fileLink, dataUI);
			if(success) {
				resetStates();
				state_input_inc_tasks = false;
			}
		}
		
		else if(state_input_inc_event) {
			success = checkIncEventInputAndPerform(userInput, fileLink, dataUI);
			if(success) {
				resetStates();
				state_input_inc_event = false;
			}
		}
		
		else if(state_input_comp_tasks) {
			success = checkCompTaskInputAndPerform(userInput, fileLink, dataUI);
			if(success) {
				resetStates();
				state_input_comp_tasks = false;
			}
		}
		
		else if(state_input_comp_event) {
			success = checkCompEventInputAndPerform(userInput, fileLink, dataUI);
			if(success) {
				resetStates();
				state_input_comp_event = false;
			}
		}
		/*
		 * implement the other 4 else if for the 2nd layer states
		 * these will call the 4 extra functions that you will implement to perform the specific edits
		 */
		return success;
	}
	
	//the actual editing
	
	
	
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
					success = checkAndGetIncTaskID(userIndex, fileLink, dataUI);
					
					if(success) {
						state_input_inc_tasks = true;
						state_inc_tasks = false;
						return false;
					} else {
						state_inc_tasks = true;
						success = false;
					}
				}
				break;
			case EDIT_INCOMPLETE_EVENTS:
				if(noIndexArgument == true) {
					dataUI.setFeedback(FEEDBACK_PENDING_INCOMPLETE_EVENT_INDEX);
					state_inc_event = true;
					
					return success = false;
				} else {
					success = checkAndGetIncEventID(userIndex, fileLink, dataUI);
					
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
					success = checkAndGetCompTaskID(userIndex, fileLink, dataUI);
					
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
					success = checkAndGetCompEventID(userIndex, fileLink, dataUI);
					
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
	private boolean checkAndGetIncTaskID(String userIndex, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > incTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_EDITION_RANGE, incTasks.size()));
				return success = false;
			} else {
				TaskCard task = incTasks.get(editIndex - 1);
				
				dataUI.setFeedback(String.format("Please list the parameters you would like to change for %s task based on the column headers", task.getName()));
				userEnteredID = editIndex;
				success = true;
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incTasks.size()));
			return success = false;
		}
		
		return success;
	}
	
	private boolean checkAndGetIncEventID(String userIndex, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > incEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_EDITION_RANGE, incEvent.size()));
				return success = false;
			} else {
				TaskCard event = incEvent.get(editIndex - 1);
				dataUI.setFeedback(String.format("Please list the parameters you would like to change for %s task based on the column headers", event.getName()));
				userEnteredID = editIndex;
				//fileLink.deleteHandling(editIndex - 1, EDIT_INCOMPLETE_EVENTS);
				//RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, incEvent.size()));
			success = false;
		}
		
		return success;
	}
	
	private boolean checkAndGetCompTaskID(String userIndex, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> compTasks = fileLink.getIncompleteTasks();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > compTasks.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_EDITION_RANGE, compTasks.size()));
				return success = false;
			} else {
				TaskCard task = compTasks.get(editIndex - 1);
				dataUI.setFeedback(String.format("Please list the parameters you would like to change for %s task based on the column headers", task.getName()));
				userEnteredID = editIndex;
				//fileLink.deleteHandling(editIndex - 1, EDIT_INCOMPLETE_TASKS);
				//RefreshUI.executeRefresh(fileLink, dataUI);
			}
		} catch(NumberFormatException ex) {
			dataUI.setFeedback(String.format(FEEDBACK_NOT_NUMBER_ENTERED, compTasks.size()));
			return success = false;
		}
		
		return success;
	}
	
	private boolean checkAndGetCompEventID(String userIndex, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> compEvent = fileLink.getIncompleteEvents();
		
		try {
			int editIndex = Integer.parseInt(userIndex);
			
			if(editIndex < 0 || editIndex > compEvent.size()) {
				dataUI.setFeedback(String.format(FEEDBACK_EDITION_RANGE, compEvent.size()));
				return success = false;
			} else {
				TaskCard event = compEvent.get(editIndex - 1);
				dataUI.setFeedback(String.format("Please list the parameters you would like to change for %s task based on the column headers", event.getName()));
				userEnteredID = editIndex;
				//fileLink.deleteHandling(editIndex - 1, EDIT_INCOMPLETE_EVENTS);
				//RefreshUI.executeRefresh(fileLink, dataUI);
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
	private boolean checkIncTaskInputAndPerform(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = true;
		ArrayList<TaskCard> incTask = fileLink.getIncompleteTasks();
		TaskCard origTask = incTask.get(userEnteredID - 1);
		TaskCard replacementTask = new TaskCard();
		
		replacementTask.setStartDay(origTask.getStartDay());
		
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeEndDate = false;
		
		boolean isDateAndTime = false;
		boolean isDate = false;
		boolean isTime = false;
		
		String[] tokenizedInput = userInput.split(";");
		
		/*
		 * first step is to check if all the parameters make sense
		 */
		
		for(int i = 0; i < tokenizedInput.length; i++) {
			if(!(tokenizedInput[i].contains("Name") || tokenizedInput[i].equals("Priority") || tokenizedInput[i].equals("EndDate"))) {
				dataUI.setFeedback("TaskWorthy could not understand the format entered :(");
				
				return success = false;
			} else if(tokenizedInput[i].contains("Name")) {
				changeName = true;
			} else if(tokenizedInput[i].contains("Priority")) {
				changePriority = true;
			} else if(tokenizedInput[i].contains("EndDate")) {
				changeEndDate = true;
			}
		}
		
		/*
		 * second step is to pull out the details that he did not want to edit and put them in
		 * helper functions would be good for abstraction
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
		
		for(int i = 0; i < tokenizedInput.length; i++) {
			
			parameterToEnter = tokenizedInput[i].split(":", 2);
			
			if(parameterToEnter.length != 2) {
				dataUI.setFeedback("TaskWorthy seems to not have found any change to the field entered :(");
			} else if(parameterToEnter[0].contains("Name")){
				replacementTask.setName(parameterToEnter[1].trim());
			} else if(parameterToEnter[0].contains("Priority")) {
				//check the String if LOW then set to 1, MED etc.
				if(parameterToEnter[1].equals("LOW")) {
					replacementTask.setPriority(1);
				} else if(parameterToEnter[1].equals("MED")) {
					replacementTask.setPriority(2);
				} else if(parameterToEnter[1].equals("HIGH")) {
					replacementTask.setPriority(3);
				} else {
					dataUI.setFeedback("You've entered an invalid priority level :(");
					return false;
				}
			} else if(parameterToEnter[0].contains("EndDate")) {
				String endDateEntry = parameterToEnter[1].trim();
				
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
				
				try {
					Date changeDate = dateFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementTask.setEndDay(cal);
					isDate = true;
					continue;
				} catch(ParseException e) {
					
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
	 * @param userInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	
	private boolean checkIncEventInputAndPerform(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		TaskCard event = new TaskCard();
		TaskCard replacementEvent = new TaskCard();
		String[] tokenizedInput;
		ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeEndDate = false;
		boolean changeStartDate = false;
		boolean changeFrequency = false;
		
		boolean isDateAndTime = false;
		boolean isDate = false;
		boolean isTime = false;
		
		/*
		 * first step is to check if all the parameters make sense
		 */
		
		tokenizedInput = userInput.split(";");
		
		for(int i = 0; i<tokenizedInput.length; i++) {
			if((tokenizedInput[i].contains("Name") || tokenizedInput[i].equals("Priority") || tokenizedInput[i].equals("EndDate"))) {
				
				dataUI.setFeedback("TaskWorthy could not understand the format entered :(");
				return success = false;
			}
			
			else if(tokenizedInput[i].contains("Name")) {
				changeName = true;
			}
			
			else if(tokenizedInput[i].contains("Priority")) {
				changePriority = true;
			}
			
			
			else if(tokenizedInput[i].contains("EndDate")) {
				changeEndDate = true;
			}
			
			else if(tokenizedInput[i].contains("StartDate")) {
				changeStartDate = true;
			}
			
			else if(tokenizedInput[i].contains("Frequency")) {
				changeFrequency = true;
			}
		}
		
		/*
		 * second step is to pull out the details that he did not want to edit and put them in
		 * helper functions would be good for abstraction
		 */
		event = incEvent.get(userEnteredID);
		if(changeName == false) {
			replacementEvent.setName(event.getName());
		}
		
		else if (changePriority == false) {
			replacementEvent.setPriority(event.getPriority());
		}
		
		else if (changeEndDate == false) {
			replacementEvent.setEndDay(event.getEndDay());
		}
		
		else if (changeStartDate == false) {
			replacementEvent.setStartDay(event.getStartDay());
		}
		
		else if (changeFrequency == false) {
			replacementEvent.setFrequency(event.getFrequency());
		}
		
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
		
		String[] tokensForEditing = tokenizedInput;
		
		for(int i=0; i<tokensForEditing.length; i++) {
			
			parameterToEnter = tokensForEditing[i].split(":", 2);
			
			if(parameterToEnter.length != 2) {
				dataUI.setFeedback("TaskWorthy seems to not have found any change to the field entered :(");
			} else if(parameterToEnter[0].contains("Name")){
				replacementEvent.setName(parameterToEnter[1]);
			} else if(parameterToEnter[0].contains("Priority")) {
				//check the String if LOW then set to 1, MED etc.
				if(parameterToEnter[1].equals("LOW"))
					replacementEvent.setPriority(1);
				else if(parameterToEnter[1].equals("MED"))
					replacementEvent.setPriority(2);
				else if(parameterToEnter[1].equals("HIGH"))
					replacementEvent.setPriority(3);
			} else if(parameterToEnter[0].contains("Frequency")) {
				//check if the String is 
				replacementEvent.setFrequency(parameterToEnter[1]);;
			} else if(parameterToEnter[0].contains("EndDate")) {
				
				String endDateEntry = parameterToEnter[1].trim();
				
				try {
					Date changeDate = dateAndTimeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDateAndTime = true;
					continue;
				} catch(ParseException e) {
					//may be date or time format only
				}
				
				try {
					Date changeDate = dateFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDate = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				try {
					Date changeDate = timeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isTime = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				if((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}
				
			} else if(parameterToEnter[0].contains("StartDate")) {
				
				String startDateEntry = parameterToEnter[1].trim();
				
				try {
					Date changeDate = dateAndTimeFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDateAndTime = true;
					continue;
				} catch(ParseException e) {
					//may be date or time format only
				}
				
				try {
					Date changeDate = dateFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDate = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				try {
					Date changeDate = timeFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isTime = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				if((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}
				
			}
			
			
		}
		
		dataUI.setFeedback(FEEDBACK_EVENT_EDIT_SUCCESSFUL);
		fileLink.editHandling(event, userEnteredID, 2);
		return success;
	}
	
	/**
	 * Complete task edit
	 * @param userInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	
	private boolean checkCompTaskInputAndPerform(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		TaskCard task = new TaskCard();
		TaskCard replacementTask = new TaskCard();
		String[] tokenizedInput;
		ArrayList<TaskCard> compTask = fileLink.getCompletedTasks();
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeEndDate = false;
		
		boolean isDateAndTime = false;
		boolean isDate = false;
		boolean isTime = false;
		
		/*
		 * first step is to check if all the parameters make sense
		 */
		
		tokenizedInput = userInput.split(";");
		
		for(int i = 0; i<tokenizedInput.length; i++) {
			if((tokenizedInput[i].contains("Name") || tokenizedInput[i].equals("Priority") || tokenizedInput[i].equals("EndDate"))) {
				
				dataUI.setFeedback("TaskWorthy could not understand the format entered :(");
				return success = false;
			}
			
			else if(tokenizedInput[i].contains("Name")) {
				changeName = true;
			}
			
			else if(tokenizedInput[i].contains("Priority")) {
				changePriority = true;
			}
			
			
			else if(tokenizedInput[i].contains("EndDate")) {
				changeEndDate = true;
			}
		}
		
		/*
		 * second step is to pull out the details that he did not want to edit and put them in
		 * helper functions would be good for abstraction
		 */
		task = compTask.get(userEnteredID);
		if(changeName == false) {
			replacementTask.setName(task.getName());
		}
		
		else if (changePriority == false) {
			replacementTask.setPriority(task.getPriority());
		}
		
		else if (changeEndDate == false) {
			replacementTask.setEndDay(task.getEndDay());
		}
		
		
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
		
		String[] tokensForEditing = tokenizedInput;
		
		for(int i=0; i<tokensForEditing.length; i++) {
			
			parameterToEnter = tokensForEditing[i].split(":", 2);
			
			if(parameterToEnter.length != 2) {
				dataUI.setFeedback("TaskWorthy seems to not have found any change to the field entered :(");
			} else if(parameterToEnter[0].contains("Name")){
				replacementTask.setName(parameterToEnter[1]);
			} else if(parameterToEnter[0].contains("Priority")) {
				//check the String if LOW then set to 1, MED etc.
				if(parameterToEnter[1].equals("LOW"))
					replacementTask.setPriority(1);
				else if(parameterToEnter[1].equals("MED"))
					replacementTask.setPriority(2);
				else if(parameterToEnter[1].equals("HIGH"))
					replacementTask.setPriority(3);
			} else if(parameterToEnter[0].contains("EndDate")) {
				
				String endDateEntry = parameterToEnter[1].trim();
				
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
				
				try {
					Date changeDate = dateFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementTask.setEndDay(cal);
					isDate = true;
					continue;
				} catch(ParseException e) {
					
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
				
				if((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}
				
			} 
			
		}
		
		dataUI.setFeedback(FEEDBACK_TASK_EDIT_SUCCESSFUL);
		fileLink.editHandling(task, userEnteredID, 3);
		return success;
	}
	
	/**
	 * Complete Event Edit
	 * 
	 * @param userInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	
	private boolean checkCompEventInputAndPerform(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		TaskCard event = new TaskCard();
		TaskCard replacementEvent = new TaskCard();
		String[] tokenizedInput;
		ArrayList<TaskCard> compEvent = fileLink.getCompletedEvents();
		boolean changeName = false;
		boolean changePriority = false;
		boolean changeEndDate = false;
		boolean changeStartDate = false;
		boolean changeFrequency = false;
		
		boolean isDateAndTime = false;
		boolean isDate = false;
		boolean isTime = false;
		
		/*
		 * first step is to check if all the parameters make sense
		 */
		
		tokenizedInput = userInput.split(";");
		
		for(int i = 0; i<tokenizedInput.length; i++) {
			if((tokenizedInput[i].contains("Name") || tokenizedInput[i].equals("Priority") || tokenizedInput[i].equals("EndDate"))) {
				
				dataUI.setFeedback("TaskWorthy could not understand the format entered :(");
				return success = false;
			}
			
			else if(tokenizedInput[i].contains("Name")) {
				changeName = true;
			}
			
			else if(tokenizedInput[i].contains("Priority")) {
				changePriority = true;
			}
			
			
			else if(tokenizedInput[i].contains("EndDate")) {
				changeEndDate = true;
			}
			
			else if(tokenizedInput[i].contains("StartDate")) {
				changeStartDate = true;
			}
			
			else if(tokenizedInput[i].contains("Frequency")) {
				changeFrequency = true;
			}
		}
		
		/*
		 * second step is to pull out the details that he did not want to edit and put them in
		 * helper functions would be good for abstraction
		 */
		event = compEvent.get(userEnteredID);
		if(changeName == false) {
			replacementEvent.setName(event.getName());
		}
		
		else if (changePriority == false) {
			replacementEvent.setPriority(event.getPriority());
		}
		
		else if (changeEndDate == false) {
			replacementEvent.setEndDay(event.getEndDay());
		}
		
		else if (changeStartDate == false) {
			replacementEvent.setStartDay(event.getStartDay());
		}
		
		else if (changeFrequency == false) {
			replacementEvent.setFrequency(event.getFrequency());
		}
		
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
		
		String[] tokensForEditing = tokenizedInput;
		
		for(int i=0; i<tokensForEditing.length; i++) {
			
			parameterToEnter = tokensForEditing[i].split(":", 2);
			
			if(parameterToEnter.length != 2) {
				dataUI.setFeedback("TaskWorthy seems to not have found any change to the field entered :(");
			} else if(parameterToEnter[0].contains("Name")){
				replacementEvent.setName(parameterToEnter[1]);
			} else if(parameterToEnter[0].contains("Priority")) {
				//check the String if LOW then set to 1, MED etc.
				if(parameterToEnter[1].equals("LOW"))
					replacementEvent.setPriority(1);
				else if(parameterToEnter[1].equals("MED"))
					replacementEvent.setPriority(2);
				else if(parameterToEnter[1].equals("HIGH"))
					replacementEvent.setPriority(3);
			} else if(parameterToEnter[0].contains("Frequency")) {
				//check if the String is 
				replacementEvent.setFrequency(parameterToEnter[1]);;
			} else if(parameterToEnter[0].contains("EndDate")) {
				
				String endDateEntry = parameterToEnter[1].trim();
				
				try {
					Date changeDate = dateAndTimeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDateAndTime = true;
					continue;
				} catch(ParseException e) {
					//may be date or time format only
				}
				
				try {
					Date changeDate = dateFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isDate = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				try {
					Date changeDate = timeFormat.parse(endDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setEndDay(cal);
					isTime = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				if((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}
				
			} else if(parameterToEnter[0].contains("StartDate")) {
				
				String startDateEntry = parameterToEnter[1].trim();
				
				try {
					Date changeDate = dateAndTimeFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setStartDay(cal);
					isDateAndTime = true;
					continue;
				} catch(ParseException e) {
					//may be date or time format only
				}
				
				try {
					Date changeDate = dateFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setStartDay(cal);
					isDate = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				try {
					Date changeDate = timeFormat.parse(startDateEntry);
					Calendar cal = Calendar.getInstance();
					cal.setTime(changeDate);
					replacementEvent.setStartDay(cal);
					isTime = true;
					continue;
				} catch(ParseException e) {
					
				}
				
				if((isDateAndTime && isTime && isDate) == false) {
					dataUI.setFeedback("The format you entered for editing the date and time was not recognized");
				}
				
			}
			
			
		}
		
		dataUI.setFeedback(FEEDBACK_EVENT_EDIT_SUCCESSFUL);
		fileLink.editHandling(event, userEnteredID, 4);
		return success;
	}
	
	
	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {
		RefreshUI.executeRefresh(fileLink, dataUI);
		dataUI.setFeedback(FEEDBACK_UNRECOGNISABLE_EDIT_COMMAND);
	}
	
	private boolean newEditCmd() {
		if(state_inc_tasks == false && state_inc_event == false
				&& state_comp_tasks == false && state_comp_event == false && state_input_inc_tasks == false
				&& state_input_inc_event == false && state_input_comp_tasks == false && state_input_comp_event == false) {
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
