import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
	private static TaskCard newCard = new TaskCard();
	private static Calendar today = GregorianCalendar.getInstance();
	private static Calendar startDay = GregorianCalendar.getInstance();
	private static Calendar endDay = GregorianCalendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/YYYY HH:mm");
	private static SimpleDateFormat date = new SimpleDateFormat("dd/MM/YYYY");
	private static SimpleDateFormat time = new SimpleDateFormat("HH:mm");
	
	private static FileHandler fileHand;
	
	public Add(FileHandler fileHand) {
		this.fileHand = fileHand;
	}
	
	public static String executeAdd(String[] cmdArr) {
		if (cmdArr[1].trim().length() <= 0) {
			return null; //need to return message for invalid input
		} else if (cmdArr[0].equals("/add") || cmdArr[0].equals("/addt")){
			addTask(cmdArr[1]);
		} else if (cmdArr[0].equals("/addf")) {
			addFloatingTask(cmdArr[1]);
		} else if (cmdArr[0].equals("/adde")) {
			addEvent (cmdArr[1]);
		} else if (cmdArr[0].equals("/addr")) {
			addRepeatingEvent (cmdArr[1]);
		}
		else {
			//return message for invalid input
		}
		fileHand.incompleteTasks.add(newCard);
		//make the filesize++;
		fileHand.writeIncompleteTasksFile();
		return null;//return string that prints success message to user
	}

	/**
	 * Main add function for adding tasks.
	 * Will parse the second entry in the array into Date and Time
	 * e.g. 12/02/2014 9:00
	 * StartDate will be set to System date, to signify date of creation
	 * EndDate will be date input by user
	 * Input Format: [Name][EndDate EndTime]
	 * @param argument
	 * @author Omar Khalid
	 */
	private static void addTask(String argument) {
		String[] argArray = argument.split(",");
		Date endDateAndTime = new Date();
		try { //get the End Date AND End Time
			endDateAndTime = dateAndTime.parse(argArray[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setTaskDetails(argArray);
		setStartDateAndTime();
		setEndDateAndTime(endDateAndTime);
	}

	private static void setTaskDetails(String[] argArray) {
		newCard.setName(argArray[0]);
		newCard.setType("T");
		newCard.setFrequency("N");
		newCard.setPriority(2);
	}
	
	private static void setEndDateAndTime(Date endDateAndTime) {
		endDay.setTime(endDateAndTime);
		newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
		newCard.setEndDate(endDay.get(Calendar.DATE));
		newCard.setEndMonth(endDay.get(Calendar.MONTH));
		newCard.setEndYear(endDay.get(Calendar.YEAR));
	}

	/**
	 * Add task without end date.
	 * StartDate will be set to System date to signify date of creation.
	 * Input Format: [Name]
	 * @param argument
	 * @author Omar Khalid
	 */
	private static void addFloatingTask(String argument) {
		setFloatingTaskDetails(argument);
		setStartDateAndTime();
		setFloatingEnd();
	}
	
	private static void setStartDateAndTime() {
		newCard.setStartDate(today.get(Calendar.DATE));
		newCard.setStartMonth(today.get(Calendar.MONTH));
		newCard.setStartYear(today.get(Calendar.YEAR));
		newCard.setStartTime((today.get(Calendar.HOUR_OF_DAY) * 100) + today.get(Calendar.MINUTE));
	}
	
	private static void setFloatingEnd() {
		newCard.setEndDate(0);
		newCard.setEndMonth(0);
		newCard.setEndYear(0);
		newCard.setEndTime(0);
	}
	
	private static void setFloatingTaskDetails(String argument) {
		newCard.setName(argument);
		newCard.setType("FT");
		newCard.setFrequency("N");
		newCard.setPriority(1);
	}

	/**
	 * Add Event based on input, i.e. timed or all-day.
	 * All-Day input format: [Name][StartDate]
	 * Timed input format: [Name][StartDate StartTime - EndDate EndTime]
	 * or: [Name][StartDate StartTime - EndTime]
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
			addTimedEvent (argArray[1], dateRange);
		}
	}
	
	private static void addAllDayEvent (String[] arg) {		
		Date startDate = new Date();
		try { //get the Start Date ONLY
			startDate = date.parse(arg[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setAllDayDetails(arg);
		startDay.setTime(startDate);
		setAllDayStart();
		setAllDayEnd();
	}

	private static void setAllDayDetails(String[] arg) {
		newCard.setName(arg[0]);
		newCard.setType("AE");
		newCard.setFrequency("N");
		newCard.setPriority(2);
	}
	
	private static void addTimedEvent(String eventName, String[] dateRange) {
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
			timeParser(dateRange);//in the case where user input only endTime
			withEndDate = false;
		}
		
		setTimedEventDetails(eventName);
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

	private static void setTimedEventDetails(String eventName) {
		newCard.setName(eventName);
		newCard.setType("E");
		newCard.setFrequency("N");
		newCard.setPriority(2);
	}
	
	private static void setTimedEventEnd(Date endDateAndTime) {
		endDay.setTime(endDateAndTime);
		newCard.setEndDate(endDay.get(Calendar.DATE));
		newCard.setEndMonth(endDay.get(Calendar.MONTH));
		newCard.setEndYear(endDay.get(Calendar.YEAR));
		newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
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
	private static void timeParser(String[] dateRange) {
		Date endTime;
		try { //get the End Time ONLY
			endTime = time.parse(dateRange[1].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an event, all-day or (same day) timed event that repeats based on user preference
	 * Frequency settings: Daily, Weekly, Monthly, Yearly
	 * All-Day Input Format: [Name][StartDate][Frequency]
	 * Same-Day Input Format:[Name][StartDate StartTime - EndTime][Frequency]
	 * 
	 * @param argument
	 * @author Omar Khalid
	 */
	private static void addRepeatingEvent(String argument) {
		String[] argArray = argument.split(",");
		String[] dateRange = argArray[1].split("-");
		String eventName = argArray[0];
		String frequency = argArray[argArray.length - 1].toLowerCase();
		
		if (dateRange.length == 2) {
			addAllDayRepeatingEvent (eventName, dateRange, frequency);
		} else {
			addTimedRepeatingEvent (eventName, dateRange, frequency);
		}
	}
	
	private static void addAllDayRepeatingEvent(String name, String[] dateRange, String freq) {
		Date startDate = new Date();
		try { //get the Start Date AND Start Time
			startDate = dateAndTime.parse(dateRange[0]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setRepeatedEventDetails(name, freq);
		startDay.setTime(startDate);		
		setAllDayStart();
		setAllDayEnd();
	}
	
	private static void addTimedRepeatingEvent(String name, String[] dateRange, String freq) {
		Date startDate = new Date();
		Date endTime = new Date();
		try { //get the Start Date AND Start Time
			startDate = dateAndTime.parse(dateRange[0].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		try { //get the End Time ONLY
			endTime = time.parse(dateRange[1].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		setRepeatedEventDetails(name, freq);
		startDay.setTime(startDate);
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

	private static void setRepeatedEventDetails(String name, String freq) {
		newCard.setName(name);
		newCard.setType("RE");
		setEventFrequency(freq);
		newCard.setPriority(2);
	}

	private static void setEventFrequency(String freq) {
		if (freq.equals("daily")) {
			newCard.setFrequency("D");
		} else if (freq.equals("weekly")) {
			newCard.setFrequency("W");
		} else if (freq.equals("monthly")) {
			newCard.setFrequency("M");
		} else if (freq.equals("yearly")) {
			newCard.setFrequency("Y");
		} else {
			//output invalid frequency input, wait for correct input
		}
	}
	
	private static void setAllDayStart() {
		newCard.setStartDate(startDay.get(Calendar.DATE));
		newCard.setStartMonth(startDay.get(Calendar.MONTH));
		newCard.setStartYear(startDay.get(Calendar.YEAR));
		newCard.setStartTime(0);
	}
	
	private static void setAllDayEnd() {
		newCard.setEndDate(startDay.get(Calendar.DATE));
		newCard.setEndMonth(startDay.get(Calendar.MONTH));
		newCard.setEndYear(startDay.get(Calendar.YEAR));
		newCard.setEndTime(2359);
	}
	
	private static void setTimedEventStart() {
		newCard.setStartDate(startDay.get(Calendar.DATE));
		newCard.setStartMonth(startDay.get(Calendar.MONTH));
		newCard.setStartYear(startDay.get(Calendar.YEAR));
		newCard.setStartTime((startDay.get(Calendar.HOUR_OF_DAY) * 100) + startDay.get(Calendar.MINUTE));
	}
	
	private static void setTimedEventNextDayEnd() {
		startDay.add(Calendar.DATE, 1);
		setTimedEventSameDayEnd();
	}
	
	private static void setTimedEventSameDayEnd() {
		newCard.setEndDate(startDay.get(Calendar.DATE));
		newCard.setEndMonth(startDay.get(Calendar.MONTH));
		newCard.setEndYear(startDay.get(Calendar.YEAR));
		newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
	}
}
