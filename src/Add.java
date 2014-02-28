import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
	TaskCard newCard = new TaskCard();
	Calendar today = GregorianCalendar.getInstance();
	Calendar startDay = GregorianCalendar.getInstance();
	Calendar endDay = GregorianCalendar.getInstance();
	SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/YYYY HH:mm");
	SimpleDateFormat date = new SimpleDateFormat("dd/MM/YYYY");
	SimpleDateFormat time = new SimpleDateFormat("HH:mm");
	
	private static FileHandler fileHand;
	
	public Add(FileHandler fileHand) {
		this.fileHand = fileHand;
	}
	
	public String executeAdd(String[] commandArray) {
		if (commandArray[1].trim().length() <= 0) {
			return null;
		} else if (commandArray[0].equals("/add")){
			addTask(commandArray[1]);
		} else if (commandArray[0].equals("/addf")) {
			addFloatingTask(commandArray[1]);
		} else if (commandArray[0].equals("/adde")) {
			addEvent (commandArray[1]);
		} else if (commandArray[0].equals("/addr")) {
			addRepeatingEvent (commandArray[1]);
		}
		return null;
	}

	private void addTask(String argument) {
		String[] argArray = argument.split(",");
		Date endDateAndTime = new Date();
		newCard.setName(argArray[0]);
		try {
			endDateAndTime = dateAndTime.parse(argArray[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		endDay.setTime(endDateAndTime);
		newCard.setType("T");
		
		newCard.setStartDate(today.get(Calendar.DATE));
		newCard.setStartMonth(today.get(Calendar.MONTH));
		newCard.setStartYear(today.get(Calendar.YEAR));
		newCard.setStartTime((today.get(Calendar.HOUR_OF_DAY) * 100) + today.get(Calendar.MINUTE));
		
		newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
		newCard.setEndDate(endDay.get(Calendar.DATE));
		newCard.setEndMonth(endDay.get(Calendar.MONTH));
		newCard.setEndYear(endDay.get(Calendar.YEAR));
		
		newCard.setFrequency("N");
		newCard.setPriority(2);
	}

	public void addFloatingTask(String argument) {
		newCard.setName(argument);
		newCard.setType("FT");
		
		newCard.setStartDate(0);
		newCard.setStartMonth(0);
		newCard.setStartYear(0);
		newCard.setStartTime(0);
		
		newCard.setEndDate(0);
		newCard.setEndMonth(0);
		newCard.setEndYear(0);
		newCard.setEndTime(0);
		
		newCard.setFrequency("N");
		newCard.setPriority(1);
	}

	private void addEvent(String argument) {
		String[] argArray = argument.split(",");
		String[] dateRange = argArray[1].split("-");
		
		if (dateRange.length == 2) {
			addAllDayEvent (argArray);
		} else {
			addTimedEvent (argArray[1], dateRange);
		}
	}
	
	private void addAllDayEvent (String[] arg) {
		newCard.setName(arg[0]);
		newCard.setType("AE");
		
		Date startDate = new Date();
		try {
			startDate = date.parse(arg[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		startDay.setTime(startDate);
		
		newCard.setStartDate(startDay.get(Calendar.DATE));
		newCard.setStartMonth(startDay.get(Calendar.MONTH));
		newCard.setStartYear(startDay.get(Calendar.YEAR));
		newCard.setStartTime(0);
		
		newCard.setEndDate(startDay.get(Calendar.DATE));
		newCard.setEndMonth(startDay.get(Calendar.MONTH));
		newCard.setEndYear(startDay.get(Calendar.YEAR));
		newCard.setEndTime(2359);
		
		newCard.setFrequency("N");
		newCard.setPriority(2);
	}
	
	private void addTimedEvent(String eventName, String[] dateRange) {
		boolean withEndDate = true;
		
		newCard.setName(eventName);
		newCard.setType("E");
		
		Date startDateAndTime = new Date();
		Date endDateAndTime = new Date();
		Date endTime = new Date();
		try {
			startDateAndTime = dateAndTime.parse(dateRange[0].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		try {
			endDateAndTime = dateAndTime.parse(dateRange[1].trim());
		} catch (ParseException e) {
			timeParser(dateRange);
			withEndDate = false;
		}
		
		startDay.setTime(startDateAndTime);
		if (withEndDate) {
			endDay.setTime(endDateAndTime);
			
			newCard.setStartDate(startDay.get(Calendar.DATE));
			newCard.setStartMonth(startDay.get(Calendar.MONTH));
			newCard.setStartYear(startDay.get(Calendar.YEAR));
			newCard.setStartTime((startDay.get(Calendar.HOUR_OF_DAY) * 100) + startDay.get(Calendar.MINUTE));
			
			newCard.setEndDate(endDay.get(Calendar.DATE));
			newCard.setEndMonth(endDay.get(Calendar.MONTH));
			newCard.setEndYear(endDay.get(Calendar.YEAR));
			newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
		} else {
			endDay.setTime(endTime);
			if (endDay.get(Calendar.HOUR_OF_DAY) < startDay.get(Calendar.HOUR_OF_DAY)) {
				//events are a couple hours apart but end time is on the next day
				newCard.setStartDate(startDay.get(Calendar.DATE));
				newCard.setStartMonth(startDay.get(Calendar.MONTH));
				newCard.setStartYear(startDay.get(Calendar.YEAR));
				newCard.setStartTime((startDay.get(Calendar.HOUR_OF_DAY) * 100) + startDay.get(Calendar.MINUTE));
				
				startDay.add(Calendar.DATE, 1);
				
				newCard.setEndDate(startDay.get(Calendar.DATE));
				newCard.setEndMonth(startDay.get(Calendar.MONTH));
				newCard.setEndYear(startDay.get(Calendar.YEAR));
				newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
			} else { //events are only a couple hours apart and they're on the same day
				newCard.setStartDate(startDay.get(Calendar.DATE));
				newCard.setStartMonth(startDay.get(Calendar.MONTH));
				newCard.setStartYear(startDay.get(Calendar.YEAR));
				newCard.setStartTime((startDay.get(Calendar.HOUR_OF_DAY) * 100) + startDay.get(Calendar.MINUTE));
				
				newCard.setEndDate(startDay.get(Calendar.DATE));
				newCard.setEndMonth(startDay.get(Calendar.MONTH));
				newCard.setEndYear(startDay.get(Calendar.YEAR));
				newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
			}
		}
		
		newCard.setFrequency("N");
		newCard.setPriority(2);
	}

	private void timeParser(String[] dateRange) {
		Date endTime;
		try {
			endTime = time.parse(dateRange[1].trim());
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
	}
	
	private void addRepeatingEvent(String argument) {
		String[] argArray = argument.split(",");
	}
	
	private void addAllDayRepeatingEvent(String arg) {
		
	}
	
	private void addTimedRepeatingEvent(String arg) {
		
	}
}
