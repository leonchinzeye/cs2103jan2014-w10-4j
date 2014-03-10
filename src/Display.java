import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Display {
	static String response = "";
	static ArrayList <TaskCard> displayIncomplete;
	static Calendar today = GregorianCalendar.getInstance();
	
	
	/*
	 * error handle the /discard
	 */
	public static String executeDis(FileLinker fileLink) {
		displayIncomplete = fileLink.displayHandler();
		if (!displayIncomplete.isEmpty()) {
			for (int i = 0; i < displayIncomplete.size(); i++) {
				Collections.sort(displayIncomplete, new SortDeadlineThenPriority());
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
	
	private static class SortDeadlineThenPriority implements Comparator<TaskCard> {
		public int compare(TaskCard o1, TaskCard o2) {
			Calendar c1 = (Calendar) o1.getEndDay();
			Calendar c2 = (Calendar) o2.getEndDay();
			int cComp = c1.compareTo(c2);
			if (cComp != 0) {
				return cComp;
			} else {
				Integer i1 = (Integer) o1.getPriority();
				Integer i2 = (Integer) o2.getPriority();
				return i2.compareTo(i1);
			}
		}
	}
}
