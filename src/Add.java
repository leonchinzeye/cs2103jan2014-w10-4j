import java.util.Calendar;

public class Add {
	TaskCard newCard = new TaskCard();
	Calendar today = Calendar.getInstance();
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
