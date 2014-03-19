package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * this class allows the user to refresh the GUI
 * things it should do:
 *   - update the UI based
 *   - the GUI should call refresh every hour of the clock
 *   - refresh should be able to handle marking of events or tasks once
 *     past the deadline
 *   - when refreshing and marking events that have past their designated
 *  	 timeslots, for those repeating ones, it should know how to put it 
 *  	 to the next repeated time
 * @author leon
 *
 */
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
	
	public static boolean executeRefresh(FileLinker fileLink, DataUI dataUI) {
		//should check for events that have passed the designated time (not done yet)
		dataUI.configureIncompleteTasks(fileLink);
		dataUI.configureIncompleteEvents(fileLink);
		dataUI.configureCompletedTasks(fileLink);
		dataUI.configureCompletedEvents(fileLink);
		
		return true;
	}

	public static String executeDis(FileLinker fileLink, DataUI dataToBePassedToUI) {
		String response = "";
		displayIncomplete = fileLink.getIncompleteTasks();
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
