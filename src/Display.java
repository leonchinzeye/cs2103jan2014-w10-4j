import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Display {
	static ArrayList <TaskCard> displayIncomplete;
	static Calendar today = GregorianCalendar.getInstance();
	
	
	/*
	 * error handle the /discard
	 */
	public static String executeDis(FileLinker fileLink) {
		String response = "";
		displayIncomplete = fileLink.displayHandler();
		if (!displayIncomplete.isEmpty()) {
			Collections.sort(displayIncomplete, new SortDeadline());
			
			for (int i = 0; i < displayIncomplete.size(); i++) {
				if (!displayIncomplete.get(i).getEndDay().before(today)) { //if the Deadline has not already passed
					if (i > 0) {
						response += "\n";
					}
					response += displayIncomplete.get(i).getTaskString();
				}
			}
		} else {
			response += "You have no tasks! Use the \"/add\" command to add a new task.";
		}
		return response + "\n";
	}
	
	private static class SortDeadline implements Comparator<TaskCard> {
		public int compare(TaskCard o1, TaskCard o2) {
			Calendar c1 = (Calendar) o1.getEndDay();
			Calendar c2 = (Calendar) o2.getEndDay();
			
			Integer i1 = (Integer) o1.getPriority();
			Integer i2 = (Integer) o2.getPriority();
			
			if(c1.compareTo(c2)<0)
				return -1;
			else if(c1.compareTo(c2)>0)
				return 1;
			else if(c1.compareTo(c2) == 0 && i1.compareTo(i2)<0)
				return 1;
			else 
				return -1;
		}
	}
	
	
}
