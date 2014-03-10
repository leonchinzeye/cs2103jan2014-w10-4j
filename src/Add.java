import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
	private static HashMap<String, Integer> addCmdTable = new HashMap<String, Integer>();
	private static final int DEFAULT_PRIORITY_TASK = 2;
	private static final int DEFAULT_PRIORITY_FLOATING_TASK = 1;
	private static TaskCard newCard = new TaskCard();
	private static Calendar today = GregorianCalendar.getInstance();
	private static Calendar startDay = GregorianCalendar.getInstance();
	private static Calendar endDay = Calendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
	private static boolean success = false;
	
	private static final String UNRECOGNIZABLE_COMMAND = "You must have typed something wrongly! We can't recognize that command.";
	private static final String ADD_FAILURE = "Something seemed to have gone wrong somewhere :(. Please try something else instead.";
	private static final String ADD_SUCCESS = "%s has been successfully added!";
	//Calendar.MONTH is 0-based, so every instance call for month has to be incremented by 1
	
	public static String executeAdd(String[] tokenizedInput, FileLinker fileLink) {
		String response = "";
		initialiseAddCmdTable();
		
		if(checkAddCmdInput(tokenizedInput[0]) == true) {
			identifyAddCmdTypeAndPerform(tokenizedInput, fileLink);
			success = fileLink.addHandling(newCard);
			if (success == true) {
				response = String.format(ADD_SUCCESS, newCard.getTaskString());
			} else {
				response = ADD_FAILURE;
			}
		} else {
			response = UNRECOGNIZABLE_COMMAND;
		}
		return response;
	}
	
	private static void identifyAddCmdTypeAndPerform(String[] tokenizedInput, FileLinker fileLink) {
		int addType = addCmdTable.get(tokenizedInput[0]);
		
		switch(addType) {
			case 1:
				addTask(tokenizedInput[1]);
				break;
			case 2:
				addTask(tokenizedInput[1]);
				break;
			case 3:
				addFloatingTask(tokenizedInput[1]);
				break;
			case 4:
				addEvent(tokenizedInput[1]);
				break;
			case 5:
				addRepeatingEvent(tokenizedInput[1]);
				break;
			default:
				break;
		}
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
	private static void addTask(String argument) {
		String[] argArray = argument.split(",");
		
		setTaskDetails(argArray);
		setStartDateAndTime();
		setEndDateAndTime(argArray[1]);
	}

	private static void setTaskDetails(String[] argArray) {
		newCard.setName(argArray[0]);
		newCard.setType("T");
		newCard.setFrequency("N");
		if (argArray.length == 3) {
			newCard.setPriority(Integer.parseInt(argArray[2]));
		} else {
			newCard.setPriority(DEFAULT_PRIORITY_TASK);
		}
	}
	
	private static void setEndDateAndTime(String endDate) {
		try {
			endDay.setTime(dateAndTime.parse(endDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newCard.setEndDay(endDay); //stores the Calendar object
	}

	/**
	 * Add task without end date.
	 * StartDate will be set to System date to signify date of creation.
	 * Input Format: [Name], [Optional Priority]
	 * @param argument
	 * @author Omar Khalid
	 */
	private static void addFloatingTask(String argument) {
		String argArray[] = argument.split(",");
		
		setFloatingTaskDetails(argArray);
		setStartDateAndTime();
		setFloatingEnd();
	}
	
	private static void setFloatingTaskDetails(String[] argArray) {
		newCard.setName(argArray[0]);
		newCard.setType("FT");
		newCard.setFrequency("N");
		if (argArray.length == 2) {
			newCard.setPriority(Integer.parseInt(argArray[1]));
		} else {
			newCard.setPriority(DEFAULT_PRIORITY_FLOATING_TASK);
		}
	}
	
	private static void setStartDateAndTime() {
		newCard.setStartDay(today);
	}
	
	private static void setFloatingEnd() {
		endDay.set(9999, 12, 31, 23, 59);
		newCard.setEndDay(endDay);
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
	 */
	private static void addEvent(String argument) {
		String[] argArray = argument.split(",");
		String[] dateRange = argArray[1].split("-");
		
		if (dateRange.length == 2) {
			addAllDayEvent (argArray);
		} else {
			addTimedEvent (argArray, dateRange);
		}
	}
	
	private static void addAllDayEvent (String[] argArray) {		
		Date startDate = new Date();
		try { //get the Start Date ONLY
			startDate = dateString.parse(argArray[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setAllDayDetails(argArray);
		startDay.setTime(startDate);
		setAllDayStart();
		setAllDayEnd();
	}

	private static void setAllDayDetails(String[] argArray) {
		newCard.setName(argArray[0]);
		newCard.setType("AE");
		newCard.setFrequency("N");
		if (argArray.length == 3) {
			newCard.setPriority(Integer.parseInt(argArray[2]));
		} else {
			newCard.setPriority(2);
		}
	}
	
	private static void addTimedEvent(String[] argArray, String[] dateRange) {
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
		
		setTimedEventDetails(argArray);
		startDay.setTime(startDateAndTime);
		if (withEndDate) { //events span over a couple days
			setTimedEventStart();
			setTimedEventEnd(endDateAndTime);
		} else {
			setTimedEventWithoutEndDate(endTime);
		}
	}

	private static void setTimedEventWithoutEndDate(Date endTime) {
		endDay.setTime(endTime);
		if (endDay.get(Calendar.HOUR_OF_DAY) < startDay.get(Calendar.HOUR_OF_DAY)) {
		//events are a couple hours apart and are on two sequential days
			setTimedEventStart();
			setTimedEventNextDayEnd();
		} else {
		//events are only a couple hours apart and they're on the same day
			setTimedEventStart();
			setTimedEventSameDayEnd();
		}
	}

	private static void setTimedEventDetails(String[] argArray) {
		newCard.setName(argArray[0]);
		newCard.setType("E");
		newCard.setFrequency("N");
		if (argArray.length == 3) {
			newCard.setPriority(Integer.parseInt(argArray[2]));
		} else {
			newCard.setPriority(2);
		}
	}
	
	private static void setTimedEventEnd(Date endDateAndTime) {
		endDay.setTime(endDateAndTime);
		newCard.setEndDay(endDay);
	}
	
	/**
	 * Adds an event, all-day or (same day) timed event that repeats based on user preference
	 * Frequency settings: Daily, Weekly, Monthly, Yearly
	 * All-Day Input Format: [Name], [StartDate], [Frequency], [Optional Priority] 
	 * Same-Day Input Format:[Name], [StartDate StartTime - EndTime], [Frequency], [Optional Priority]
	 * 
	 * @param argument
	 * @author Omar Khalid
	 */
	private static void addRepeatingEvent(String argument) {
		String[] argArray = argument.split(",");
		String[] dateRange = argArray[1].split("-");
		
		if (dateRange.length == 2) {
			addAllDayRepeatingEvent (argArray, dateRange);
		} else {
			addTimedRepeatingEvent (argArray, dateRange);
		}
	}
	
	private static void addAllDayRepeatingEvent(String[] argArray, String[] dateRange) {
		Date startDate = new Date();
		try { //get the Start Date AND Start Time
			startDate = dateAndTime.parse(dateRange[0]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setRepeatedEventDetails(argArray);
		startDay.setTime(startDate);		
		setAllDayStart();
		setAllDayEnd();
	}
	
	private static void addTimedRepeatingEvent(String[] argArray, String[] dateRange) {
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
		
		setRepeatedEventDetails(argArray);
		startDay.setTime(startDate);
		endDay.setTime(endTime);
		if (endDay.get(Calendar.HOUR_OF_DAY) < startDay.get(Calendar.HOUR_OF_DAY)) {
		//events are a couple hours apart and are on two sequential days
			setTimedEventStart();
			setTimedEventNextDayEnd();
		} else {
		//events are only a couple hours apart and they're on the same day
			setTimedEventStart();
			setTimedEventSameDayEnd();
		}
	}

	private static void setRepeatedEventDetails(String[] argArray) {
		newCard.setName(argArray[0]);
		newCard.setType("RE");
		setEventFrequency(argArray[2]);
		if (argArray.length == 4) {
			newCard.setPriority(Integer.parseInt(argArray[3]));
		} else {
			newCard.setPriority(2);
		}
	}

	private static void setEventFrequency(String freq) {
		if (freq.equals("daily")) {
			newCard.setFrequency("Daily");
		} else if (freq.equals("weekly")) {
			newCard.setFrequency("Weekly");
		} else if (freq.equals("monthly")) {
			newCard.setFrequency("Monthly");
		} else if (freq.equals("yearly")) {
			newCard.setFrequency("Yearly");
		} else {
			//output invalid frequency input, wait for correct input
		}
	}
	
	private static void setAllDayStart() {
		startDay.set(Calendar.HOUR_OF_DAY, 00);
		startDay.set(Calendar.MINUTE, 00);
		newCard.setStartDay(startDay);
	}
	
	private static void setAllDayEnd() {
		startDay.set(Calendar.HOUR_OF_DAY, 23);
		startDay.set(Calendar.MINUTE, 59);
		newCard.setEndDay(startDay);
	}
	
	private static void setTimedEventStart() {
		newCard.setStartDay(startDay);
	}
	
	private static void setTimedEventNextDayEnd() {
		startDay.add(Calendar.DATE, 1);
		setTimedEventSameDayEnd();
	}
	
	private static void setTimedEventSameDayEnd() {
		newCard.setEndDay(startDay);
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
