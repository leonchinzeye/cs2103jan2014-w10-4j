import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Display {
	public static String executeDis(String cmdArray[]) {
		if (cmdArray[1].toLowerCase().equals("today")) {
			return displayToday();
		} else {
			return null;
		}
	}

	private static String displayToday() {
		ArrayList <TaskCard> incomplete = FileHandler.incompleteTasks;
		ArrayList <TaskCard> todayTasks = new ArrayList<TaskCard>();
		Calendar today = GregorianCalendar.getInstance();
		String finalOutput = "";
		for (int i = 0; i < FileHandler.numberOfIncompleteTasks; i++) {
			if (incomplete.get(i).getEndDay().after(today)) { //need to take repeating tasks into consideration
				if (incomplete.get(i).getType() != "FT") { //don't display floating tasks
					todayTasks.add(incomplete.get(i));
				}
			}
		}
		if (todayTasks.isEmpty()) {
			finalOutput = "You have nothing for today!";
		} else {
			Collections.sort(todayTasks, new SortPriority());
			for (int i = 0; i < todayTasks.size(); i++) {
				if (i > 0) {
					finalOutput += "\n";
				}
				finalOutput += todayTasks.get(i).getTaskString();
			}
		}
		return finalOutput;
	}
	
	private static class SortPriority implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			Integer i1 = (Integer) o1;
			Integer i2 = (Integer) o2;
			return i1.compareTo(i2);
		}
	}
}
