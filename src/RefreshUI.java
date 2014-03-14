import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class RefreshUI {
	private static ArrayList <TaskCard> displayIncomplete;
	private static Calendar today = GregorianCalendar.getInstance();
	
	private static final String MESSAGE_YOU_HAVE_NO_TASK = "You have no tasks! Use the \"/add\" command to add a new task.";
	private static final String TYPE_EVENTS = "events";
	private static final String TYPE_TASKS = "tasks";
	
	private static final int FIRST_NUMBER_ENDS_FIRST = -1;
	private static final int SECOND_NUMBER_ENDS_FIRST = 1;
	private static final int FIRST_NUMBER_HAS_LOWER_PRIORITY = 1;
	private static final int SECOND_NUMBER_HAS_LOWER_PRIORITY = -1;
	/*
	 * error handle the /discard
	 */
	
	public static boolean executeRefresh(FileLinker fileLink, DataUI dataToBe) {
		ArrayList<TaskCard> incompleteTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> incompleteEvent = new ArrayList<TaskCard>();
		ArrayList<TaskCard> completedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> completedEvent = new ArrayList<TaskCard>();
		
		filterIncomplete(fileLink, incompleteTasks, incompleteEvent);
		filterComplete(fileLink, completedTasks, completedEvent);
		return true;
	}
	
	private static void filterComplete(FileLinker fileLink,
			ArrayList<TaskCard> completedTasks, ArrayList<TaskCard> completedEvent) {
		ArrayList<TaskCard> complete = fileLink.completeRetrieval();
		
		for(int i = 0; i < complete.size(); i++) {
			TaskCard task = complete.get(i);
			
			if(task.getType() == "FT" || task.getType() == "T") {
				completedTasks.add(task);
			} else {
				completedEvent.add(task);
			}
		}
	}

	private static void filterIncomplete(FileLinker fileLink,
			ArrayList<TaskCard> incompleteTasks, ArrayList<TaskCard> incompleteEvent) {
		ArrayList<TaskCard> incomplete = fileLink.incompleteRetrieval();
		
		for(int i = 0; i < incomplete.size(); i++) {
			TaskCard task = incomplete.get(i);
			
			if(task.getType() == "FT" || task.getType() == "T") {
				incompleteTasks.add(task);
			} else {
				incompleteEvent.add(task);
			}
		}
	}

	public static String executeDis(FileLinker fileLink, DataUI dataToBePassedToUI) {
		String response = "";
		displayIncomplete = fileLink.incompleteRetrieval();
		if (!displayIncomplete.isEmpty()) {
			Collections.sort(displayIncomplete, new SortByDeadlineAndPriority());
			
			for (int i = 0; i < displayIncomplete.size(); i++) {
				if (!displayIncomplete.get(i).getEndDay().before(today)) { //if the Deadline has not already passed
					
					response += displayIncomplete.get(i).getTaskString() + "\n";
				}
			}
		} else {
			response += MESSAGE_YOU_HAVE_NO_TASK;
		}
		return response;
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
