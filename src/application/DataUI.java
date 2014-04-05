package application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * this class processes the information that has been handled by the logic
 * and passes it back to the GUI for printing on the GUI
 * 
 * Things to take note of:
 * 		- depending on the command type, it will process the info
 * 		- needs attributes to tell which command
 * 
 * format of processing
 * Events: ID, Priority, Name, Start Date, Start Time, End Date, End Time, Frequency
 * Tasks: ID, Priority, Name, End Date, End Time
 * @author leon
 *
 */
public class DataUI {

	private static final String DEFAULT_FEEDBACK = "Read me!";
	private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mmaa");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");	
	
	private ArrayList<TaskDataUI> incompleteTasks;
	private ArrayList<TaskDataUI> completeTasks;
	private ArrayList<EventDataUI> incompleteEvents;
	private ArrayList<EventDataUI> completeEvents;
	
	private String feedback = DEFAULT_FEEDBACK;
	private int row_added;
	private int file_added;
	
	/**
	 * Constructor for DataUI
	 * Takes in an integer and calls the appropriate methods within DataUI
	 * to arrange the information for the GUI to print it out
	 * @param
	 * int cmdType: indicates the command type
	 */
	public DataUI() {
		incompleteTasks = new ArrayList<TaskDataUI>();
		incompleteEvents = new ArrayList<EventDataUI>();
		completeTasks = new ArrayList<TaskDataUI>();
		completeEvents = new ArrayList<EventDataUI>();
	}
	
	/**
	 * Incomplete Task 
	 * @param fileLink
	 * @author Atul
	 * 
	 */
	public void configIncompleteTasks(FileLinker fileLink) {
		incompleteTasks = new ArrayList<TaskDataUI>();
		ArrayList<TaskCard> incompTasks = fileLink.getIncompleteTasks();
				
		for(int i = 1; i <= incompTasks.size(); i++) {
			TaskDataUI taskData = new TaskDataUI();
			TaskCard task = incompTasks.get(i - 1);
			
			taskData.setID("" + i);
			String priority = determinePriority(task.getPriority());
			taskData.setPriority("" + priority);
			taskData.setName(task.getName());
			if(task.getType().equals("FT")){
				taskData.setEndTime("-");
				taskData.setEndDate("-");
			} else {
				Calendar endDay = task.getEndDay();
				taskData.setEndDate(dateFormat.format(endDay.getTime()));
				
				if(endDay.get(Calendar.HOUR_OF_DAY) == 0 && endDay.get(Calendar.MINUTE) == 0) {
					taskData.setEndTime("-");
				} else {
					taskData.setEndTime(timeFormat.format(endDay.getTime()));
				}
			}
			
			incompleteTasks.add(taskData);
		}
	}

	/**
	 * For incomplete Events
	 * @param fileLink
	 */
	public void configIncompleteEvents(FileLinker fileLink) {
		incompleteEvents = new ArrayList<EventDataUI>();
		ArrayList<TaskCard> incompEvents = fileLink.getIncompleteEvents();
		
		for(int i = 1; i <= incompEvents.size(); i++) {
			EventDataUI eventData = new EventDataUI();
			TaskCard event = incompEvents.get(i - 1);
			
			eventData.setID("" + i);
			eventData.setName(event.getName());
			String priority = determinePriority(event.getPriority());
			eventData.setPriority("" + priority);
			
			if(event.getType().equals("AE")) {
				Calendar startDay = event.getStartDay();
				Calendar endDay = event.getEndDay();
				eventData.setStartDate(dateFormat.format(startDay.getTime())); //start date
				eventData.setStartTime("-"); //no start time
				eventData.setEndDate(dateFormat.format(endDay.getTime())); //end date	
				eventData.setEndTime("-"); //no end time
			} else {
				Calendar startDay = event.getStartDay();
				Calendar endDay = event.getEndDay();
				eventData.setStartDate(dateFormat.format(startDay.getTime()));
				eventData.setStartTime(timeFormat.format(startDay.getTime()));
				eventData.setEndDate(dateFormat.format(endDay.getTime()));
				eventData.setEndTime(timeFormat.format(endDay.getTime()));
			}
			
			incompleteEvents.add(eventData);
		}
	}

	/**
	 * Complete Task 
	 * @param fileLink
	 * @author Atul
	 * 
	 */
	public void configCompleteTasks(FileLinker fileLink) {
		completeTasks = new ArrayList<TaskDataUI>();
		ArrayList<TaskCard> incompTasks = fileLink.getCompletedTasks();
				
		for(int i = 1; i <= incompTasks.size(); i++) {
			TaskDataUI taskData = new TaskDataUI();
			TaskCard task = incompTasks.get(i - 1);
			
			taskData.setID("" + i);
			String priority = determinePriority(task.getPriority());
			taskData.setPriority("" + priority);
			taskData.setName(task.getName());
			if(task.getType().equals("FT")){
				taskData.setEndTime("-");
				taskData.setEndDate("-");
			} else {
				Calendar endDay = task.getEndDay();
				taskData.setEndDate(dateFormat.format(endDay.getTime()));
				taskData.setEndTime(timeFormat.format(endDay.getTime()));
			}
			completeTasks.add(taskData);
		}
	}
	
	
	/**
	 * For Completed Events
	 * @param fileLink
	 */
	public void configCompletedEvents(FileLinker fileLink) {
		completeEvents = new ArrayList<EventDataUI>();
		ArrayList<TaskCard> incompEvents = fileLink.getCompletedEvents();
		
		for(int i = 1; i <= incompEvents.size(); i++) {
			EventDataUI eventData = new EventDataUI();
			TaskCard event = incompEvents.get(i - 1);
			
			eventData.setID("" + i);
			eventData.setName(event.getName());
			String priority = determinePriority(event.getPriority());
			eventData.setPriority("" + priority);
			
			if(event.getType().equals("AE")) {
				Calendar startDay = event.getStartDay();
				eventData.setStartDate(dateFormat.format(startDay.getTime())); //start date
				eventData.setStartTime("-"); //no start time
				eventData.setEndDate(dateFormat.format(startDay.getTime())); //end date	
				eventData.setEndTime("-"); //no end time
			} else {
				Calendar startDay = event.getStartDay();
				Calendar endDay = event.getEndDay();
				eventData.setStartDate(dateFormat.format(startDay.getTime()));
				eventData.setStartTime(timeFormat.format(startDay.getTime()));
				eventData.setEndDate(dateFormat.format(endDay.getTime()));
				eventData.setEndTime(timeFormat.format(endDay.getTime()));
			}
			
			completeEvents.add(eventData);
		}
	}
	
	private String determinePriority(int priority) {
		if(priority == 1) {
			return "LOW";
		} else if(priority == 2) {
			return "MED";
		} else
			return "HIGH";
	}
	
	public ArrayList<TaskDataUI> getIncompleteTasks() {
		return incompleteTasks;
	}
		
	public ArrayList<EventDataUI> getIncompleteEvents() {
		return incompleteEvents;
	}
	
	public ArrayList<TaskDataUI> getCompleteTasks() {
		return completeTasks;
	}
	
	public ArrayList<EventDataUI> getCompleteEvents() {
		return completeEvents;
	}
	
	public String getFeedback() {
		return feedback;
	}
	
	public void setFeedback(String message) {
		feedback = message;
	}
	
	public void setRowAdded(int added) {
		row_added = added;
	}
	
	public void setFileAdded(int added) {
		file_added = added;
	}
}
