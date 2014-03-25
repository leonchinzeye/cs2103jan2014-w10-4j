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

	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");	
	
	private ArrayList<TaskDataUI> incompleteTasks;
	private ArrayList<TaskDataUI> completeTasks;
	private ArrayList<EventDataUI> incompleteEvents;
	private ArrayList<EventDataUI> completeEvents;
	
	private String feedback = null;
	
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
				taskData.setEndTime(timeFormat.format(endDay.getTime()));
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
				eventData.setStartDate(dateFormat.format(startDay.getTime())); //start date
				eventData.setStartTime("-"); //no start time
				eventData.setEndDate(dateFormat.format(startDay.getTime())); //end date	
				eventData.setEndTime("-"); //no end time
				eventData.setFrequency("-"); //no frequency
			} else {
				Calendar startDay = event.getStartDay();
				Calendar endDay = event.getEndDay();
				eventData.setStartDate(dateFormat.format(startDay.getTime()));
				eventData.setStartTime(timeFormat.format(startDay.getTime()));
				eventData.setEndDate(dateFormat.format(endDay.getTime()));
				eventData.setEndTime(timeFormat.format(endDay.getTime()));
				if(event.getType().equals("E")) {
					eventData.setFrequency("-");
				} else {
					eventData.setFrequency(event.getFrequency());
				}
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
				eventData.setFrequency("-"); //no frequency
			} else {
				Calendar startDay = event.getStartDay();
				Calendar endDay = event.getEndDay();
				eventData.setStartDate(dateFormat.format(startDay.getTime()));
				eventData.setStartTime(timeFormat.format(startDay.getTime()));
				eventData.setEndDate(dateFormat.format(endDay.getTime()));
				eventData.setEndTime(timeFormat.format(endDay.getTime()));
				if(event.getType().equals("E")) {
					eventData.setFrequency("-");
				} else {
					eventData.setFrequency(event.getFrequency());
				}
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
	
	/*public void configureIncompleteTasks(FileLinker fileLink) {
		incTasks = new ArrayList<ArrayList<String>>();
		ArrayList<TaskCard> incompleteTasks = fileLink.getIncompleteTasks();
				
		for(int i = 0; i < incompleteTasks.size(); i++) {
			ArrayList<String> taskDetails = new ArrayList<String>();
			TaskCard task = incompleteTasks.get(i);
			
			taskDetails = getInfoForTasks(task);
			incTasks.add(taskDetails);
		}
	}
	
	public void configureCompletedEvents(FileLinker fileLink) {
		compEvent = new ArrayList<ArrayList<String>>();
		ArrayList<TaskCard> completedEvents = fileLink.getIncompleteEvents();
		
		for(int i = 0; i < completedEvents.size(); i++) {
			ArrayList<String> eventDetails = new ArrayList<String>();
			TaskCard task = completedEvents.get(i);
			
			eventDetails = getInfoForEvents(task);
			compEvent.add(eventDetails);
		}		
	}

	public void configureIncompleteEvents(FileLinker fileLink) {
		incEvent = new ArrayList<ArrayList<String>>();
		ArrayList<TaskCard> incompleteEvents = fileLink.getIncompleteEvents();
		
		for(int i = 0; i < incompleteEvents.size(); i++) {
			ArrayList<String> eventDetails = new ArrayList<String>();
			TaskCard task = incompleteEvents.get(i);
			
			eventDetails = getInfoForEvents(task);
			incEvent.add(eventDetails);
		}
	}
	
	public void configureCompletedTasks(FileLinker fileLink) {
		compTasks = new ArrayList<ArrayList<String>>();
		ArrayList<TaskCard> completedTasks = fileLink.getCompletedTasks();
		
		for(int i = 0; i < completedTasks.size(); i++) {
			ArrayList<String> taskDetails = new ArrayList<String>();
			TaskCard task = completedTasks.get(i);
			
			taskDetails = getInfoForTasks(task);
			compTasks.add(taskDetails);
		}
	}
	
	private ArrayList<String> getInfoForTasks(TaskCard task) {
		ArrayList<String> taskDetails = new ArrayList<String>();
		
		String priority = determinePriority(task.getPriority());
		taskDetails.add(priority);
		taskDetails.add(task.getName());
		
		if(task.getType().equals("FT")) {
			taskDetails.add("-");
			taskDetails.add("-");
		} else {
			Calendar endDay = task.getEndDay();
			taskDetails.add(dateFormat.format(endDay.getTime()));
			taskDetails.add(timeFormat.format(endDay.getTime()));
		}
		return taskDetails;
	}
	
	private ArrayList<String> getInfoForEvents(TaskCard event) {
		ArrayList<String> eventDetails = new ArrayList<String>();
		
		String priority = determinePriority(event.getPriority());
		eventDetails.add(priority);
		eventDetails.add(event.getName());
		
		if(event.getType().equals("AE")) {
			Calendar startDay = event.getStartDay();
			eventDetails.add(dateFormat.format(startDay.getTime()));	//start date
			eventDetails.add("-");																		//no start time
			eventDetails.add(dateFormat.format(startDay.getTime()));	//end date	
			eventDetails.add("-");																		//no end time
			eventDetails.add("-");																		//no frequency
		} else {
			Calendar startDay = event.getStartDay();
			Calendar endDay = event.getEndDay();
			eventDetails.add(dateFormat.format(startDay.getTime()));
			eventDetails.add(timeFormat.format(startDay.getTime()));
			eventDetails.add(dateFormat.format(endDay.getTime()));
			eventDetails.add(timeFormat.format(endDay.getTime()));
			if(event.getType().equals("E")) {
				eventDetails.add("-");
			} else {
				eventDetails.add(event.getFrequency());
			}
		} 
		
		return eventDetails;
	}*/
}
