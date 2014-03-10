import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Display {
	private static ArrayList <TaskCard> displayIncomplete;
	private static Calendar today = GregorianCalendar.getInstance();
	
	private static final String MESSAGE_YOU_HAVE_NO_TASK = "You have no tasks! Use the \"/add\" command to add a new task.";
	
	private static final int FIRST_NUMBER_ENDS_FIRST = -1;
	private static final int SECOND_NUMBER_ENDS_FIRST = 1;
	private static final int FIRST_NUMBER_HAS_LOWER_PRIORITY = 1;
	private static final int SECOND_NUMBER_HAS_LOWER_PRIORITY = -1;
	/*
	 * error handle the /discard
	 */
	public static String executeDis(FileLinker fileLink) {
		String response = "";
		displayIncomplete = fileLink.displayHandler();
		if (!displayIncomplete.isEmpty()) {
			Collections.sort(displayIncomplete, new SortByDeadlineAndPriority());
			
			for (int i = 0; i < displayIncomplete.size(); i++) {
				if (!displayIncomplete.get(i).getEndDay().before(today)) { //if the Deadline has not already passed
					if (i > 0) {
						response += "\n";
					}
					response += displayIncomplete.get(i).getTaskString();
				}
			}
		} else {
			response += MESSAGE_YOU_HAVE_NO_TASK;
		}
		return response + "\n";
	}
	
	private static class SortByDeadlineAndPriority implements Comparator<TaskCard> {
		public int compare(TaskCard o1, TaskCard o2) {
			Calendar c1 = (Calendar) o1.getEndDay();
			Calendar c2 = (Calendar) o2.getEndDay();
			
			Integer i1 = (Integer) o1.getPriority();
			Integer i2 = (Integer) o2.getPriority();
			
			if(c1.compareTo(c2)<0)
				return FIRST_NUMBER_ENDS_FIRST;
			else if(c1.compareTo(c2)>0)
				return SECOND_NUMBER_ENDS_FIRST;
			else if(c1.compareTo(c2) == 0 && i1.compareTo(i2)<0)
				return FIRST_NUMBER_HAS_LOWER_PRIORITY;
			else 
				return SECOND_NUMBER_HAS_LOWER_PRIORITY;
		}
	}
	
	
}
