package application;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
	private boolean state_add_float_task;
	private boolean state_add_timed_task;
	private boolean state_add_event;
	private boolean state_add_repeating_event;
	
	private static final int FIRST_ARGUMENT = 0;
	private static final int SECOND_ARGUMENT = 1;
	
	private static final int ADD_TASK = 1;
	private static final int ADD_TIMED_TASK = 2;
	private static final int ADD_FLOATING_TASK = 3;
	private static final int ADD_EVENT = 4;
	private static final int ADD_REPEATING_EVENT = 5;
	
	private HashMap<String, Integer> addCmdTable = new HashMap<String, Integer>();
	private final int DEFAULT_PRIORITY_TASK = 2;
	private final int DEFAULT_PRIORITY_FLOATING_TASK = 1;
	private Calendar today = GregorianCalendar.getInstance();
	private Calendar startDay = GregorianCalendar.getInstance();
	private Calendar endDay = Calendar.getInstance();
	private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	private static final String FEEDBACK_UNRECOGNIZABLE_COMMAND = "That was an unrecognisable add command! :(";
	private static final String FEEDBACK_INVALID_FORMAT_TASK = "That was an invalid format for adding a task :(";
	private static final String FEEDBACK_PENDING_TIMED_TASK = "You didn't enter a task! Please enter a task!"; 
	private static final String FEEDBACK_ADD_TASK_SUCCESS = "Task added!";
	//Calendar.MONTH is 0-based, so every instance call for month has to be incremented by 1
	
	public Add() {
		initialiseAddCmdTable();
		
		state_add_float_task = false;
		state_add_timed_task = false;
		state_add_event = false;
		state_add_repeating_event = false;
		
		dateAndTimeFormat.setLenient(false);
		dateFormat.setLenient(false);
		timeFormat.setLenient(false);
	}
	
	public boolean executeAdd(String userInput, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = false;
		
		if(newAddCmd()) {
			String[] tokenizedInput = userInput.trim().split("\\s+", 2);
			String cmd = tokenizedInput[FIRST_ARGUMENT];
			
			if(addCmdTable.containsKey(cmd) != true) {
				notRecognisableCmd(fileLink, dataUI);
				return success = true;
			} else {
				success = identifyCmdAndPerform(tokenizedInput, fileLink, dataUI);
			}
		} else if(state_add_float_task) {
			success = addFloatingTask(userInput, fileLink, dataUI);
			
		} else if(state_add_timed_task) {
			success = addTask(userInput, fileLink, dataUI);
			
		} else if(state_add_event) {
			success = addEvent(userInput, fileLink, dataUI);
			
		} else {
			success = addRepeatingEvent(userInput, fileLink, dataUI);
			
		}
		
		if(success) {
			resetStates();
		}
		return success;
	}
	
	private boolean identifyCmdAndPerform(String[] tokenizedInput, FileLinker fileLink,
			DataUI dataUI) {
		boolean success = false;
		boolean noIndexArgument = false;
		String addInput = null;
		
		if(tokenizedInput.length < 2) {
			noIndexArgument = true;
		} else {
			addInput = tokenizedInput[SECOND_ARGUMENT]; 
		}
		
		String cmd = tokenizedInput[FIRST_ARGUMENT];
		
		switch(addCmdTable.get(cmd)) {
			case ADD_TASK:
			case ADD_TIMED_TASK:
				if(noIndexArgument) {
					dataUI.setFeedback(FEEDBACK_PENDING_TIMED_TASK);
					state_add_timed_task = true;
					return success = false;
				} else {
					success = addTask(addInput, fileLink, dataUI);
				}
				break;
			case ADD_FLOATING_TASK:
				if(noIndexArgument) {
					dataUI.setFeedback(FEEDBACK_PENDING_TIMED_TASK);
					state_add_float_task = true;
					return success = false;
				} else {
					success = addFloatingTask(addInput, fileLink, dataUI);
				}
				break;
			case ADD_EVENT:
				if(noIndexArgument) {
					dataUI.setFeedback("You didn't enter an event! Please enter an event!");
					state_add_event = true;
					return success = false;
				} else {
					success = addEvent(addInput, fileLink, dataUI);
				}
				break;
			case ADD_REPEATING_EVENT:
				if(noIndexArgument) {
					dataUI.setFeedback("You didn't enter an event! Please enter an event");
					state_add_repeating_event = true;
					return success = false;
				} else {
					success = addRepeatingEvent(addInput, fileLink, dataUI);
				}
				break;
		}
		return success;
	}

	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {
		RefreshUI.executeRefresh(fileLink, dataUI);
		dataUI.setFeedback(FEEDBACK_UNRECOGNIZABLE_COMMAND);
	}

	private boolean newAddCmd() {
		if(state_add_float_task == false && state_add_timed_task == false
				&& state_add_event == false && state_add_repeating_event == false)
			return true;
		return false;
	}

	/**
	 * Main add function for adding tasks.
	 * Will parse the second entry in the array into Date and Time
	 * e.g. 12/02/2014 9:00
	 * StartDate will be set to System date, to signify date of creation
	 * EndDate will be date input by user
	 * Input Format: [Name], [EndDate EndTime], [Optional Priority]
	 * @param argument
	 * @author Omar Khalid
	 * @param dataUI 
	 */
	private boolean addTask(String argument, FileLinker fileLink, DataUI dataUI) {
		boolean success;
		
		String[] argArray = argument.split(";");
		TaskCard taskToBeAdded = new TaskCard();
		
		if(argArray.length < 2 || argArray.length > 3) {
			dataUI.setFeedback(FEEDBACK_INVALID_FORMAT_TASK);
			state_add_timed_task = true;
			return success = false;
		} else {
			if(setEndDateAndTime(argArray[1], taskToBeAdded, dataUI) == true
					&& setTaskDetails(argArray, taskToBeAdded, dataUI) == true
					&& setStartDateAndTime(taskToBeAdded) == true) {
				
				fileLink.addHandling(taskToBeAdded);
				RefreshUI.executeRefresh(fileLink, dataUI);
				dataUI.setFeedback(String.format(FEEDBACK_ADD_TASK_SUCCESS, argArray[0]));
				state_add_timed_task = false;
				success = true;
			} else {
				state_add_timed_task = true;
				success = false;
			}
		}
		return success;
	}

	/**
	 * Add task without end date.
	 * StartDate will be set to System date to signify date of creation.
	 * Input Format: [Name], [Optional Priority]
	 * @param argument
	 * @author Omar Khalid
	 * @param dataUI 
	 */
	private boolean addFloatingTask(String argument, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
	
		String argArray[] = argument.split(";");
		TaskCard taskToBeAdded = new TaskCard();
		
		if(argArray.length != 1) {
			dataUI.setFeedback("That was an invalid format for a floating task! Please re-enter!");
			state_add_float_task = true;
			return success = false;
		} else {
			setFloatingTaskDetails(argArray, taskToBeAdded);
			setStartDateAndTime(taskToBeAdded);
			setFloatingEnd(taskToBeAdded);
			
			dataUI.setFeedback(FEEDBACK_ADD_TASK_SUCCESS);
			fileLink.addHandling(taskToBeAdded);
			RefreshUI.executeRefresh(fileLink, dataUI);
			
			state_add_float_task = false;
			success = true;
		}
		return success;
	}

	/**
	 * Add Event based on input, i.e. timed or all-day.
	 * All-Day input format: [Name], [StartDate], [Optional Priority]
	 * Timed input format: [Name], [StartDate StartTime - EndDate EndTime], [Optional Priority]
	 * or: [Name], [StartDate StartTime - EndTime], [Optional Priority]
	 * A case where event is from night to morning, e.g. 23:30 to 01:30 next day
	 * is also considered in setTimedEventNextDayEnd()
	 * @param argument
	 * @author Omar Khalid
	 * @param fileLink 
	 * @param dataUI 
	 */
	private boolean addEvent(String argument, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		
		String[] argArray = argument.split(";");
		String[] dateRange = argArray[1].split("-");
		
		if(argArray.length < 2 || argArray.length > 3) {
			dataUI.setFeedback("That was an invalid format for an event! Please re-enter!");
			state_add_event = true;
			return success = false;
		} else if(dateRange.length < 1) {
			dataUI.setFeedback("You didn't specify a date or time! Please re-enter!");
			state_add_event = true;
			return success = false;
		} else if(dateRange.length > 2) {
			dataUI.setFeedback("You've entered an extra timing! Please re-enter!");
			state_add_event = true;
			return success = false;
		}
		
		TaskCard taskToBeAdded = new TaskCard();
		
		if (dateRange.length == 1) {
			success = addAllDayEvent(argArray, taskToBeAdded, dataUI, fileLink);
		} else {
			success = addTimedEvent(argArray, dateRange, taskToBeAdded, dataUI, fileLink);
		}
		
		return success;
	}

	private boolean addAllDayEvent (String[] argArray, TaskCard taskToBeAdded, DataUI dataUI, FileLinker fileLink) {		
		boolean success = false;
		
		try { //get the Start Date ONLY
			startDay.setTime(dateFormat.parse(argArray[1]));
		} catch (ParseException e) {
			dataUI.setFeedback("Please enter the date in a correct format");
			state_add_event = true;
			success = false;
		}
		
		if(setAllDayDetails(argArray, taskToBeAdded, dataUI) == true) {
			setAllDayStart(taskToBeAdded);
			setAllDayEnd(taskToBeAdded);
			
			dataUI.setFeedback(String.format(FEEDBACK_ADD_TASK_SUCCESS, argArray[0]));
			fileLink.addHandling(taskToBeAdded);
			RefreshUI.executeRefresh(fileLink, dataUI);
			
			state_add_event = false;
			success = true;
		} else {
			state_add_event = true;
			success = false;
		}
		return success;
	}

	private boolean addTimedEvent(String[] argArray, String[] dateRange, TaskCard taskToBeAdded, DataUI dataUI, FileLinker fileLink) {
		boolean success = false;
		boolean withEndDate = true; //to see whether endDate was input by user
		
		Calendar endTime = new GregorianCalendar();
		
		try { //get the Start Date AND Start Time
			startDay.setTime(dateAndTimeFormat.parse(dateRange[0].trim()));
		} catch (ParseException e) {
			dataUI.setFeedback("Please re-enter the event with a proper time format!");
			state_add_event = true;
			return success = false;
		}
		
		try { //get the End Date AND End Time
			endDay.setTime(dateAndTimeFormat.parse(dateRange[1].trim()));
		} catch (ParseException e) {
			withEndDate = false;
		}
		
		/**
		 * Parse the input into a time of Date type.
		 * e.g. 22:45, 11:30
		 * Will only be called if ParseException earlier was caught
		 * after trying for Date and Time format
		 * e.g. 12/02/2014 9:00
		 * @param dateRange
		 * @author Omar Khalid
		 */
		if (!withEndDate) { //if End Date was not input
			try { //get End Time ONLY
				endTime.setTime(timeFormat.parse(dateRange[1].trim()));
			} catch (ParseException e) {
				dataUI.setFeedback("You didn't specify an ending time for the event! Please re-enter with an ending time!");
				state_add_event = true;
				return success = false;
			}
		}
		
		if(setTimedEventDetails(argArray, taskToBeAdded, dataUI)) {
			if (withEndDate) { //events span over a couple days
				taskToBeAdded.setStartDay(startDay);
				taskToBeAdded.setEndDay(endDay);
			} else {
				setTimedEventWithoutEndDate(endTime, taskToBeAdded);
			}
			
			dataUI.setFeedback(String.format(FEEDBACK_ADD_TASK_SUCCESS, argArray[0]));
			fileLink.addHandling(taskToBeAdded);
			RefreshUI.executeRefresh(fileLink, dataUI);
			
			state_add_event = false;
			success = true;
		} else {
			state_add_event = true;
			return success = false;
		}
		return success;
	}

	/**
	 * Adds an event, all-day or (same day) timed event that repeats based on user preference
	 * Frequency settings: Daily, Weekly, Monthly, Yearly
	 * All-Day Input Format: [Name], [StartDate], [Frequency], [Optional Priority] 
	 * Same-Day Input Format:[Name], [StartDate StartTime - EndTime], [Frequency], [Optional Priority]
	 * 
	 * @param argument
	 * @author Omar Khalid
	 * @param fileLink 
	 * @return 
	 */
	private boolean addRepeatingEvent(String argument, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		
		String[] argArray = argument.split(";");
		String[] dateRange = argArray[1].split("-");
		TaskCard taskToBeAdded = new TaskCard();
		
		//check to see if missing parameters for input
		if(argArray.length < 3 || argArray.length > 4) {
			
		}
		if (dateRange.length == 2) {
			addAllDayRepeatingEvent (argArray, dateRange, taskToBeAdded);
		} else {
			addTimedRepeatingEvent (argArray, dateRange, taskToBeAdded);
		}
		return success;
	}

	private void addAllDayRepeatingEvent(String[] argArray, String[] dateRange, TaskCard taskToBeAdded) {
		try { //get the Start Date AND Start Time
			startDay.setTime(dateAndTimeFormat.parse(dateRange[0]));
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setRepeatedEventDetails(argArray, taskToBeAdded);	
		setAllDayStart(taskToBeAdded);
		setAllDayEnd(taskToBeAdded);
	}

	private void addTimedRepeatingEvent(String[] argArray, String[] dateRange, TaskCard taskToBeAdded) {
		Calendar endTime = new GregorianCalendar();
		try { //get the Start Date AND Start Time
			startDay.setTime(dateAndTimeFormat.parse(dateRange[0].trim()));
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		try { //get the End Time ONLY
			endTime.setTime(timeFormat.parse(dateRange[1].trim()));
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setRepeatedEventDetails(argArray, taskToBeAdded);
		if (endTime.get(Calendar.HOUR_OF_DAY) < startDay.get(Calendar.HOUR_OF_DAY)) {
		//events are a couple hours apart and are on two sequential days
			taskToBeAdded.setStartDay(startDay);
			setTimedEventNextDayEnd(taskToBeAdded, endTime);
		} else {
		//events are only a couple hours apart and they're on the same day
			taskToBeAdded.setStartDay(startDay);
			setTimedEventSameDayEnd(taskToBeAdded, endTime);
		}
	}

	private boolean setTaskDetails(String[] argArray, TaskCard taskToBeAdded, DataUI dataUI) {
		boolean success = false;
		taskToBeAdded.setName(argArray[FIRST_ARGUMENT]);
		taskToBeAdded.setType("T");
		taskToBeAdded.setFrequency("N");
		if (argArray.length == 3) {
			try {
				taskToBeAdded.setPriority(Integer.parseInt(argArray[2]));
				success = true;
			} catch(NumberFormatException e) {
				dataUI.setFeedback("Priority has to be a digit! Please re-enter the task that you want to add!");
				return success = false;
			}
		} else {
			taskToBeAdded.setPriority(DEFAULT_PRIORITY_TASK);
			success = true;
		}
		return success;
	}
	
	private boolean setEndDateAndTime(String endDate, TaskCard taskToBeAdded, DataUI dataUI) {
		boolean success;
		Calendar end = Calendar.getInstance();
		
		try {
			end.setTime(dateAndTimeFormat.parse(endDate));
			taskToBeAdded.setEndDay(end);
			success = true;
		} catch (ParseException e) {
			dataUI.setFeedback("Please re-enter task with a proper date format!");
			success = false;
		}
		
		return success;
	}

	private void setFloatingTaskDetails(String[] argArray, TaskCard taskToBeAdded) {
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("FT");
		taskToBeAdded.setFrequency("N");
		taskToBeAdded.setPriority(DEFAULT_PRIORITY_FLOATING_TASK);
	
	}
	
	private boolean setStartDateAndTime(TaskCard taskToBeAdded) {
		taskToBeAdded.setStartDay(today);
		return true;
	}
	
	private void setFloatingEnd(TaskCard taskToBeAdded) {
		endDay.set(9999, 11, 31, 23, 59);
		taskToBeAdded.setEndDay(endDay);
	}

	private boolean setAllDayDetails(String[] argArray, TaskCard taskToBeAdded, DataUI dataUI) {
		boolean success = false;
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("AE");
		taskToBeAdded.setFrequency("N");
		if (argArray.length == 3) {
			try {
				taskToBeAdded.setPriority(Integer.parseInt(argArray[2]));
				success = true;
			} catch(NumberFormatException e) {
				dataUI.setFeedback("Priority has to be a digit! Please re-enter the event you want to add!");
				return success = false;
			}
		} else {
			taskToBeAdded.setPriority(2);
			success = true;
		}
		
		return success;
	}
	
	private void setTimedEventWithoutEndDate(Calendar endTime, TaskCard taskToBeAdded) {
		if (endTime.get(Calendar.HOUR_OF_DAY) < startDay.get(Calendar.HOUR_OF_DAY)) {
		//events are a couple hours apart and are on two sequential days
			taskToBeAdded.setStartDay(startDay);
			setTimedEventNextDayEnd(taskToBeAdded, endTime);
		} else {
		//events are only a couple hours apart and they're on the same day
			taskToBeAdded.setStartDay(startDay);
			setTimedEventSameDayEnd(taskToBeAdded, endTime);
		}
	}

	private boolean setTimedEventDetails(String[] argArray, TaskCard taskToBeAdded, DataUI dataUI) {
		boolean success = false;
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("E");
		taskToBeAdded.setFrequency("N");
		if (argArray.length == 3) {
			
			try {
				taskToBeAdded.setPriority(Integer.parseInt(argArray[2]));
				success = true;
			} catch(NumberFormatException e) {
				dataUI.setFeedback("Priority has to be a digit! Please re-enter the event you want to add!");
				return success = false;
			}
		} else {
			taskToBeAdded.setPriority(2);
			success = true;
		}
		
		return success;
	}
	
	private static void setRepeatedEventDetails(String[] argArray, TaskCard taskToBeAdded) {
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("RE");
		setEventFrequency(argArray[2], taskToBeAdded);
		if (argArray.length == 4) {
			taskToBeAdded.setPriority(Integer.parseInt(argArray[3]));
		} else {
			taskToBeAdded.setPriority(2);
		}
	}

	private static void setEventFrequency(String freq, TaskCard taskToBeAdded) {
		if (freq.equals("daily")) {
			taskToBeAdded.setFrequency("Daily");
		} else if (freq.equals("weekly")) {
			taskToBeAdded.setFrequency("Weekly");
		} else if (freq.equals("monthly")) {
			taskToBeAdded.setFrequency("Monthly");
		} else if (freq.equals("yearly")) {
			taskToBeAdded.setFrequency("Yearly");
		} else {
			//output invalid frequency input, wait for correct input
		}
	}
	
	private void setAllDayStart(TaskCard taskToBeAdded) {
		startDay.set(Calendar.HOUR_OF_DAY, 00);
		startDay.set(Calendar.MINUTE, 00);
		taskToBeAdded.setStartDay(startDay);
	}
	
	private void setAllDayEnd(TaskCard taskToBeAdded) {
		endDay = (Calendar) startDay.clone();
		endDay.set(Calendar.HOUR_OF_DAY, 23);
		endDay.set(Calendar.MINUTE, 59);
		taskToBeAdded.setEndDay(endDay);
	}
	
	private void setTimedEventNextDayEnd(TaskCard taskToBeAdded, Calendar endTime) {
		endDay = (Calendar) startDay.clone();
		endDay.add(Calendar.DATE, 1);
		setTimedEventSameDayEnd(taskToBeAdded, endTime);
	}
	
	private void setTimedEventSameDayEnd(TaskCard taskToBeAdded, Calendar endTime) {
		endDay.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
		endDay.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
		taskToBeAdded.setEndDay(endDay);
	}
	
	private void resetStates() {
		state_add_float_task = false;
		state_add_timed_task = false;
		state_add_event = false;
		state_add_repeating_event = false;
	}
	
	private void initialiseAddCmdTable() {
		addCmdTable.put("/add", ADD_TASK);
		addCmdTable.put("/addt", ADD_TIMED_TASK);
		addCmdTable.put("/addf", ADD_FLOATING_TASK);
		addCmdTable.put("/adde", ADD_EVENT);
		addCmdTable.put("/addr", ADD_REPEATING_EVENT);
	}
	
	/*
	public static String executeAdd(String[] tokenizedInput, FileLinker fileLink, DataUI dataToBePassedToUI) {
		String response = "";
		initialiseAddCmdTable();
		
		if(checkAddCmdInput(tokenizedInput[0]) == true) {
			//response = performAddCommand(tokenizedInput, fileLink);
		} else {
			response = FEEDBACK_UNRECOGNIZABLE_COMMAND;
		}
		return response;
	}
	*/
	/*
	private static String performAddCommand(String[] tokenizedInput, FileLinker fileLink) {
		String response = "";
		String taskDetails = "";
		
		if(tokenizedInput.length < 2) {
			taskDetails = getTaskDetailsFromUser();
			
			if(taskDetails.equals(COMMAND_QUIT_TO_TOP)) {
				response = null;
				return response;
			} else {
				response = identifyAddCmdTypeAndPerform(tokenizedInput[0], taskDetails, fileLink);
			}
		} else {
			taskDetails = tokenizedInput[1];
			response = identifyAddCmdTypeAndPerform(tokenizedInput[0], taskDetails, fileLink);
		}
		
		return response;
	}

	private static String getTaskDetailsFromUser() {
		boolean enteredInput = false;
		String taskDetails = "";
		
		while(enteredInput == false) {
			print("You seem to be forgetting something. Please enter a task to be added.");
			
			if(scan.hasNext()) {
				taskDetails = scan.nextLine();
				enteredInput = true;
			}
		}
		return taskDetails;
	}
	*/
	/*
	private static String identifyAddCmdTypeAndPerform(String cmd, String taskDetails, FileLinker fileLink) {
		int addType = addCmdTable.get(cmd);
		String response = "";
	
		switch(addType) {
			case 1:
			case 2:
				response = addTask(taskDetails, fileLink);
				break;
			case 3:
				addFloatingTask(taskDetails, fileLink);
				break;
			case 4:
				addEvent(taskDetails, fileLink);
				break;
			case 5:
				addRepeatingEvent(taskDetails, fileLink);
				break;
			default:
				break;
		} 
		return response;
	}
	*/
}
