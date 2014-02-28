import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
	private static TaskCard newCard = new TaskCard();
	private static Calendar today = GregorianCalendar.getInstance();
	private static Calendar endDay = GregorianCalendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/YYYY HH:mm");
	
	private static FileHandler fileHand;
	
	public Add(FileHandler fileHand) {
		this.fileHand = fileHand;
	}
	
	public static String executeAdd(String[] commandArray) {
		
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

	private static void addTask(String argument) {
		String[] argArray = argument.split(",");
		Date endDateAndTime = null;
		newCard.setName(argArray[0]);
		try {
			endDateAndTime = dateAndTime.parse(argArray[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		endDay.setTime(endDateAndTime);
		newCard.setEndTime((endDay.get(Calendar.HOUR_OF_DAY) * 100) + endDay.get(Calendar.MINUTE));
		newCard.setEndDate(endDay.get(Calendar.DATE));
		newCard.setEndMonth(endDay.get(Calendar.MONTH));
		newCard.setEndYear(endDay.get(Calendar.YEAR));
	}

	public static void addFloatingTask(String argument) {
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

	private static void addEvent(String argument) {
		
	}
	
	private static void addRepeatingEvent(String argument) {
		
	}
}
