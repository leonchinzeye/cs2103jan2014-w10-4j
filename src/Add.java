import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Add {
	TaskCard newCard = new TaskCard();
	Calendar today = GregorianCalendar.getInstance();
	Calendar endDay = GregorianCalendar.getInstance();
	SimpleDateFormat time = new SimpleDateFormat("HH:mm");
	SimpleDateFormat date = new SimpleDateFormat("dd/MM/YYYY");
	SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/YYYY HH:mm");
	
	private static FileHandler fileHand;
	
	public Add(FileHandler fileHand) {
		this.fileHand = fileHand;
	}
	
	public TaskCard executeAdd(String commandFull) {
		String[] commandArray = commandFull.split(" ", 2);
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
		return newCard;
	}

	private void addTask(String argument) {
		String[] argArray = argument.split(",");
		Date endDateAndTime = null;
		newCard.setName(argArray[0]);
		try {
			endDateAndTime = dateAndTime.parse(argArray[1]);
		} catch (ParseException e) {
			// Ask user to input date and time in proper format here
			e.printStackTrace();
		}
		
		Date endTime = endDay.getTime();
		//newCard.setEndTime(endTime);
		newCard.setEndDate(endDay.get(Calendar.DATE));
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
		
	}
	
	private void addRepeatingEvent(String argument) {
		
	}
}
