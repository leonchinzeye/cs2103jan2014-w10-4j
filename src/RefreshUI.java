import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class RefreshUI {
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
	
	public static boolean executeRefresh(FileLinker fileLink, DataUI dataToBe) {
		ArrayList<TaskCard> incTasks = new ArrayList<TaskCard>();
		ArrayList<Integer> incTasksIndex = new ArrayList<Integer>();
		ArrayList<TaskCard> incEvent = new ArrayList<TaskCard>();
		ArrayList<Integer> incEventIndex = new ArrayList<Integer>();
		ArrayList<TaskCard> compTasks = new ArrayList<TaskCard>();
		ArrayList<Integer> compTasksIndex = new ArrayList<Integer>();
		ArrayList<TaskCard> compEvent = new ArrayList<TaskCard>();
		ArrayList<Integer> compEventIndex = new ArrayList<Integer>();
		
		filterComplete(fileLink, compTasks, compEvent, compTasksIndex, compEventIndex);
		filterIncomplete(fileLink, incTasks, incEvent, incTasksIndex, incEventIndex);
		
		return true;
	}
	
	private static void filterComplete(FileLinker fileLink,
			ArrayList<TaskCard> compTasks, ArrayList<TaskCard> compEvent, 
			ArrayList<Integer> compTasksIndex, ArrayList<Integer> compEventIndex) {
		ArrayList<TaskCard> complete = fileLink.completeRetrieval();
		
		for(int i = 0; i < complete.size(); i++) {
			TaskCard task = complete.get(i);
			
			if(task.getType() == "FT" || task.getType() == "T") {
				compTasks.add(task);
				compTasksIndex.add(i);
			} else {
				compEvent.add(task);
				compEventIndex.add(i);
			}
		}
	}

	private static void filterIncomplete(FileLinker fileLink,
			ArrayList<TaskCard> incTasks, ArrayList<TaskCard> incEvent, 
			ArrayList<Integer> incTasksIndex, ArrayList<Integer> incEventIndex) {
		ArrayList<TaskCard> incomplete = fileLink.incompleteRetrieval();
		
		for(int i = 0; i < incomplete.size(); i++) {
			TaskCard task = incomplete.get(i);
			
			if(task.getType() == "FT" || task.getType() == "T") {
				incTasks.add(task);
				incTasksIndex.add(i);
			} else {
				incEvent.add(task);
				incEventIndex.add(i);
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
