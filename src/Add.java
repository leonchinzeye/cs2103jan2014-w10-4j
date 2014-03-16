import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
	private boolean state_add_float_task;
	private boolean state_add_timed_task;
	private boolean state_add_timed_event;
	private boolean state_add_all_day_event;
	private boolean state_add_repeating_event;
	
	private static final int FIRST_ARGUMENT = 0;
	
	private static Scanner scan = new Scanner(System.in);
	private static HashMap<String, Integer> addCmdTable = new HashMap<String, Integer>();
	private static final int DEFAULT_PRIORITY_TASK = 2;
	private static final int DEFAULT_PRIORITY_FLOATING_TASK = 1;
	private static Calendar today = GregorianCalendar.getInstance();
	private static Calendar startDay = GregorianCalendar.getInstance();
	private static Calendar endDay = Calendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
	private static boolean successWriteToFile = false;
	
	private static final String FEEDBACK_UNRECOGNIZABLE_COMMAND = "That was an unrecognisable add command! :(";
	private static final String ADD_FAILURE = "Something seemed to have gone wrong somewhere :(. Please try something else instead.";
	private static final String ADD_SUCCESS = "%s has been successfully added!";
	private static final String COMMAND_QUIT_TO_TOP = "!q";
	//Calendar.MONTH is 0-based, so every instance call for month has to be incremented by 1
	
	public Add() {
		initialiseAddCmdTable();
		state_add_float_task = false;
		state_add_timed_task = false;
		state_add_timed_event = false;
		state_add_all_day_event = false;
		state_add_repeating_event = false;
	}
	
	public boolean executeA(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		
		if(newAddCmd()) {
			String[] tokenizedInput = userInput.trim().split("\\s+", 2);
			String cmd = tokenizedInput[FIRST_ARGUMENT];
			
			if(addCmdTable.containsKey(cmd) != true) {
				notRecognisableCmd(fileLink, dataUI);
			} else {
				
			}
		} else if(state_add_float_task) {
			
		} else if(state_add_timed_task) {
			
		} else if(state_add_timed_event) {
			
		} else if(state_add_all_day_event) {
		
		} else {
			
		}
		
		return success;
	}
	
	private void notRecognisableCmd(FileLinker fileLink, DataUI dataUI) {
		RefreshUI.executeDis(fileLink, dataUI);
		dataUI.setFeedback(FEEDBACK_UNRECOGNIZABLE_COMMAND);
	}

	private boolean newAddCmd() {
		if(state_add_float_task == false && state_add_timed_task == false
				&& state_add_timed_event == false && state_add_all_day_event == false
				&& state_add_repeating_event == false)
			return true;
		return false;
	}

	public static String executeAdd(String[] tokenizedInput, FileLinker fileLink, DataUI dataToBePassedToUI) {
		String response = "";
		initialiseAddCmdTable();
		
		if(checkAddCmdInput(tokenizedInput[0]) == true) {
			response = performAddCommand(tokenizedInput, fileLink);
		} else {
			response = FEEDBACK_UNRECOGNIZABLE_COMMAND;
		}
		return response;
	}
	
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

	/**
	 * Main add function for adding tasks.
	 * Will parse the second entry in the array into Date and Time
	 * e.g. 12/02/2014 9:00
	 * StartDate will be set to System date, to signify date of creation
	 * EndDate will be date input by user
	 * Input Format: [Name], [EndDate EndTime], [Optional Priority]
	 * @param argument
	 * @author Omar Khalid
	 */
	private static String addTask(String argument, FileLinker fileLink) {
		String response = "";
		String[] argArray = argument.split(";");
		TaskCard taskToBeAdded = new TaskCard();
		
		if(argArray.length < 2 || argArray.length > 3) {
			//catch and print error message
			//wrong format, please re-enter valid format [][][]
			print("not valid argument");
			response = null;
		} else { 
			setTaskDetails(argArray, taskToBeAdded);
			setStartDateAndTime(taskToBeAdded);
			setEndDateAndTime(argArray[1], taskToBeAdded);
			fileLink.addHandling(taskToBeAdded);
		}
		
		return response;
	}

	private static void print(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

	private static void setTaskDetails(String[] argArray, TaskCard taskToBeAdded) {
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("T");
		taskToBeAdded.setFrequency("N");
		if (argArray.length == 3) {
			taskToBeAdded.setPriority(Integer.parseInt(argArray[2]));
		} else {
			taskToBeAdded.setPriority(DEFAULT_PRIORITY_TASK);
		}
	}
	
	private static void setEndDateAndTime(String endDate, TaskCard taskToBeAdded) {
		try {
			endDay.setTime(dateAndTime.parse(endDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		taskToBeAdded.setEndDay(endDay); //stores the Calendar object
	}

	/**
	 * Add task without end date.
	 * StartDate will be set to System date to signify date of creation.
	 * Input Format: [Name], [Optional Priority]
	 * @param argument
	 * @author Omar Khalid
	 */
	private static void addFloatingTask(String argument, FileLinker fileLink) {
		String argArray[] = argument.split(";");
		TaskCard taskToBeAdded = new TaskCard();
		
		setFloatingTaskDetails(argArray, taskToBeAdded);
		setStartDateAndTime(taskToBeAdded);
		setFloatingEnd(taskToBeAdded);
		
		fileLink.addHandling(taskToBeAdded);
		
	}
	
	private static void setFloatingTaskDetails(String[] argArray, TaskCard taskToBeAdded) {
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("FT");
		taskToBeAdded.setFrequency("N");
		if (argArray.length == 2) {
			taskToBeAdded.setPriority(Integer.parseInt(argArray[1]));
		} else {
			taskToBeAdded.setPriority(DEFAULT_PRIORITY_FLOATING_TASK);
		}
	}
	
	private static void setStartDateAndTime(TaskCard taskToBeAdded) {
		taskToBeAdded.setStartDay(today);
	}
	
	private static void setFloatingEnd(TaskCard taskToBeAdded) {
		endDay.set(9999, 11, 31, 23, 59);
		taskToBeAdded.setEndDay(endDay);
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
	 */
	private static void addEvent(String argument, FileLinker fileLink) {
		String[] argArray = argument.split(";");
		String[] dateRange = argArray[1].split("-");
		TaskCard taskToBeAdded = new TaskCard();
		
		if (dateRange.length == 2) {
			addAllDayEvent (argArray, taskToBeAdded);
		} else {
			addTimedEvent (argArray, dateRange, taskToBeAdded);
		}
	}
	
	private static void addAllDayEvent (String[] argArray, TaskCard taskToBeAdded) {		
		Date startDate = new Date();
		try { //get the Start Date ONLY
			startDate = dateString.parse(argArray[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setAllDayDetails(argArray, taskToBeAdded);
		startDay.setTime(startDate);
		setAllDayStart(taskToBeAdded);
		setAllDayEnd(taskToBeAdded);
	}

	private static void setAllDayDetails(String[] argArray, TaskCard taskToBeAdded) {
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("AE");
		taskToBeAdded.setFrequency("N");
		if (argArray.length == 3) {
			taskToBeAdded.setPriority(Integer.parseInt(argArray[2]));
		} else {
			taskToBeAdded.setPriority(2);
		}
	}
	
	private static void addTimedEvent(String[] argArray, String[] dateRange, TaskCard taskToBeAdded) {
		boolean withEndDate = true; //to see whether endDate was input by user
		
		Date startDateAndTime = new Date();
		Date endDateAndTime = new Date();
		Date endTime = new Date();
		try { //get the Start Date AND Start Time
			startDateAndTime = dateAndTime.parse(dateRange[0].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		try { //get the End Date AND End Time
			endDateAndTime = dateAndTime.parse(dateRange[1].trim());
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
				endTime = timeString.parse(dateRange[1].trim());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		setTimedEventDetails(argArray, taskToBeAdded);
		startDay.setTime(startDateAndTime);
		if (withEndDate) { //events span over a couple days
			setTimedEventStart(taskToBeAdded);
			setTimedEventEnd(endDateAndTime, taskToBeAdded);
		} else {
			setTimedEventWithoutEndDate(endTime, taskToBeAdded);
		}
	}

	private static void setTimedEventWithoutEndDate(Date endTime, TaskCard taskToBeAdded) {
		endDay.setTime(endTime);
		if (endDay.get(Calendar.HOUR_OF_DAY) < startDay.get(Calendar.HOUR_OF_DAY)) {
		//events are a couple hours apart and are on two sequential days
			setTimedEventStart(taskToBeAdded);
			setTimedEventNextDayEnd(taskToBeAdded);
		} else {
		//events are only a couple hours apart and they're on the same day
			setTimedEventStart(taskToBeAdded);
			setTimedEventSameDayEnd(taskToBeAdded);
		}
	}

	private static void setTimedEventDetails(String[] argArray, TaskCard taskToBeAdded) {
		taskToBeAdded.setName(argArray[0]);
		taskToBeAdded.setType("E");
		taskToBeAdded.setFrequency("N");
		if (argArray.length == 3) {
			taskToBeAdded.setPriority(Integer.parseInt(argArray[2]));
		} else {
			taskToBeAdded.setPriority(2);
		}
	}
	
	private static void setTimedEventEnd(Date endDateAndTime, TaskCard taskToBeAdded) {
		endDay.setTime(endDateAndTime);
		taskToBeAdded.setEndDay(endDay);
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
	 */
	private static void addRepeatingEvent(String argument, FileLinker fileLink) {
		String[] argArray = argument.split(";");
		String[] dateRange = argArray[1].split("-");
		TaskCard taskToBeAdded = new TaskCard();
		
		if (dateRange.length == 2) {
			addAllDayRepeatingEvent (argArray, dateRange, taskToBeAdded);
		} else {
			addTimedRepeatingEvent (argArray, dateRange, taskToBeAdded);
		}
	}
	
	private static void addAllDayRepeatingEvent(String[] argArray, String[] dateRange, TaskCard taskToBeAdded) {
		Date startDate = new Date();
		try { //get the Start Date AND Start Time
			startDate = dateAndTime.parse(dateRange[0]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setRepeatedEventDetails(argArray, taskToBeAdded);
		startDay.setTime(startDate);		
		setAllDayStart(taskToBeAdded);
		setAllDayEnd(taskToBeAdded);
	}
	
	private static void addTimedRepeatingEvent(String[] argArray, String[] dateRange, TaskCard taskToBeAdded) {
		Date startDate = new Date();
		Date endTime = new Date();
		try { //get the Start Date AND Start Time
			startDate = dateAndTime.parse(dateRange[0].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		try { //get the End Time ONLY
			endTime = timeString.parse(dateRange[1].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setRepeatedEventDetails(argArray, taskToBeAdded);
		startDay.setTime(startDate);
		endDay.setTime(endTime);
		if (endDay.get(Calendar.HOUR_OF_DAY) < startDay.get(Calendar.HOUR_OF_DAY)) {
		//events are a couple hours apart and are on two sequential days
			setTimedEventStart(taskToBeAdded);
			setTimedEventNextDayEnd(taskToBeAdded);
		} else {
		//events are only a couple hours apart and they're on the same day
			setTimedEventStart(taskToBeAdded);
			setTimedEventSameDayEnd(taskToBeAdded);
		}
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
	
	private static void setAllDayStart(TaskCard taskToBeAdded) {
		startDay.set(Calendar.HOUR_OF_DAY, 00);
		startDay.set(Calendar.MINUTE, 00);
		taskToBeAdded.setStartDay(startDay);
	}
	
	private static void setAllDayEnd(TaskCard taskToBeAdded) {
		startDay.set(Calendar.HOUR_OF_DAY, 23);
		startDay.set(Calendar.MINUTE, 59);
		taskToBeAdded.setEndDay(startDay);
	}
	
	private static void setTimedEventStart(TaskCard taskToBeAdded) {
		taskToBeAdded.setStartDay(startDay);
	}
	
	private static void setTimedEventNextDayEnd(TaskCard taskToBeAdded) {
		startDay.add(Calendar.DATE, 1);
		setTimedEventSameDayEnd(taskToBeAdded);
	}
	
	private static void setTimedEventSameDayEnd(TaskCard taskToBeAdded) {
		taskToBeAdded.setEndDay(startDay);
	}
	
	private static boolean checkAddCmdInput(String cmd) {
		boolean isCorrectCmd = false;
		
		if(addCmdTable.containsKey(cmd)) {
			isCorrectCmd = true;
		}
		return isCorrectCmd;
	}
	
	private static void initialiseAddCmdTable() {
		addCmdTable.put("/add", 1);
		addCmdTable.put("/addt", 2);
		addCmdTable.put("/addf", 3);
		addCmdTable.put("/adde", 3);
		addCmdTable.put("/addr", 3);
	}
}
