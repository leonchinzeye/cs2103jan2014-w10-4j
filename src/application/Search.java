package application;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Search {
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Possible search keywords:
	 * Chronological - Today, Tomorrow
	 * @return
	 * @author Omar Khalid
	 * @param dataToBePassedToUI 
	 */
	public static String executeSearch(String[] cmdArray, FileLinker fileLink, DataUI dataToBePassedToUI) {
		if (cmdArray[1].equals("today")){
			return searchToday(fileLink);
		} /*else if (!cmdArray[1].isEmpty()){
			return searchByKey();
		}*/ else {
			return null;
		}
	}
	
	private static String searchToday(FileLinker fileLink) {
		ArrayList <TaskCard> incomplete = fileLink.getIncompleteTasks();
		ArrayList <TaskCard> todayTasks = new ArrayList<TaskCard>();
		Calendar today = GregorianCalendar.getInstance();
		String dateForToday = dateString.format(today.getTime());
		String finalOutput = dateForToday + "\n";
		for (int i = 0; i < Storage.numberOfIncompleteTasks; i++) {
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
			return i2.compareTo(i1);
		}
	}
}
