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
	
	private ArrayList<ArrayList<String>> incTasks;
	private ArrayList<ArrayList<String>> incEvent;
	private ArrayList<ArrayList<String>> compTasks;
	private ArrayList<ArrayList<String>> compEvent;
	
	private String feedback = null;
	
	/*
	private ArrayList<TaskCard> incompleteTasks;
	private ArrayList<TaskCard> incompleteEvents;
	private ArrayList<TaskCard> completedTasks;
	private ArrayList<TaskCard> completedEvents;
	*/
	
	
	public enum COMMAND_TYPE {
		
	}
	/**
	 * Constructor for DataUI
	 * Takes in an integer and calls the appropriate methods within DataUI
	 * to arrange the information for the GUI to print it out
	 * @param
	 * int cmdType: indicates the command type
	 */
	public DataUI() {
		incTasks = new ArrayList<ArrayList<String>>();
		
	}
	public void configureIncompleteTasks(FileLinker fileLink) {
		ArrayList<TaskCard> incompleteTasks = fileLink.getIncompleteTasks();
				
		for(int i = 0; i < incompleteTasks.size(); i++) {
			ArrayList<String> taskDetails = new ArrayList<String>();
			TaskCard task = incompleteTasks.get(i);
			
			taskDetails = getInfoForTasks(task);
			incTasks.add(taskDetails);
		}
	}
	
	public void configureIncompleteEvents(FileLinker fileLink) {
		ArrayList<TaskCard> incompleteEvents = fileLink.getIncompleteEvents();
		
		for(int i = 0; i < incompleteEvents.size(); i++) {
			ArrayList<String> eventDetails = new ArrayList<String>();
			TaskCard task = incompleteEvents.get(i);
			
			eventDetails = getInfoForEvents(task);
			incEvent.add(eventDetails);
		}
	}
	
	public void configureCompletedTasks(FileLinker fileLink) {
		ArrayList<TaskCard> completedTasks = fileLink.getCompletedTasks();
		
		for(int i = 0; i < completedTasks.size(); i++) {
			ArrayList<String> taskDetails = new ArrayList<String>();
			TaskCard task = completedTasks.get(i);
			
			taskDetails = getInfoForTasks(task);
			compTasks.add(taskDetails);
		}
	}
	
	public void configureCompletedEvents(FileLinker fileLink) {
		ArrayList<TaskCard> completedEvents = fileLink.getIncompleteEvents();
		
		for(int i = 0; i < completedEvents.size(); i++) {
			ArrayList<String> eventDetails = new ArrayList<String>();
			TaskCard task = completedEvents.get(i);
			
			eventDetails = getInfoForEvents(task);
			compEvent.add(eventDetails);
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
	}
	
	private String determinePriority(int priority) {
		if(priority == 1) {
			return "low";
		} else if(priority == 2) {
			return "med";
		} else
			return "high";
	}
	
	public ArrayList<ArrayList<String>> getIncTasks() {
		return incTasks;
	}
	
	public ArrayList<ArrayList<String>> getIncEvent() {
		return incEvent;
	}
	
	public ArrayList<ArrayList<String>> getCompTasks() {
		return compTasks;
	}
	
	public ArrayList<ArrayList<String>> getCompEvent() {
		return compEvent;
	}
	
	public String getFeedback() {
		return feedback;
	}
	
	public void setFeedback(String message) {
		feedback = message;
	}
}
