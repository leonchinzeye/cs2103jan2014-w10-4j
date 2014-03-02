import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Display {
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	
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
		String dateForToday = dateString.format(today.getTime());
		String finalOutput = dateForToday + "\n";
		for (int i = 0; i < FileHandler.numberOfIncompleteTasks; i++) {
			if (incomplete.get(i).getEndDay().after(today)) { //need to take repeating tasks into consideration
				if (incomplete.get(i).getType() != "FT") { //don't display floating tasks
					todayTasks.add(incomplete.get(i));
				}
			}
		}
		if (todayTasks.isEmpty()) {
			finalOutput += "You have nothing for today!";
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
	
	private static class SortPriority implements Comparator<TaskCard> {
		public int compare(TaskCard o1, TaskCard o2) {
			Integer i1 = (Integer) o1.getPriority();
			Integer i2 = (Integer) o2.getPriority();
			return i1.compareTo(i2);
		}
	}
}
