//@author A0100720E
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
 *
 */

public class DataUI {

	private static final String MESSAGE_ONGOING_EVENTS_ARE_HIGHLIGHTED_IN_BLUE = "Ongoing events are highlighted in blue.";
	private static final String MESSAGE_EXPIRED_EVENTS_ARE_HIGHLIGHTED_IN_RED = "Expired events are highlighted in red.";
	
	private static final String DEFAULT_FEEDBACK = "Read me!";
	private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mmaa");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");	
	private SimpleDateFormat timeUIFormat = new SimpleDateFormat("EEE h:mmaa");
	
	private static final String NUMBER_SET_ID_TWO = "2";
	private static final String NUMBER_SET_ID_ONE = "1";
	private static final int PRIORITY_NUM_MED = 2;
	private static final int PRIORITY_NUM_LOW = 1;
	
	private ArrayList<TaskDataUI> incompleteTasks;
	private ArrayList<TaskDataUI> completeTasks;
	private ArrayList<EventDataUI> incompleteEvents;
	private ArrayList<EventDataUI> completeEvents;
	private ArrayList<EventDataUI> helpEvents;
	private String UIclock;
	private String UIdate;
	
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
		helpEvents = new ArrayList<EventDataUI>();
	}
	
	/**
	 * Passes the information on Incomplete Tasks in a format readable for GUI 
	 * @param fileLink
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
				Calendar now = GregorianCalendar.getInstance();
				
				if(endDay.before(now) || endDay.equals(now)) {
					taskData.setIsExpired(true);
				} else {
					taskData.setIsExpired(false);
				}
				
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
	 * Passes the information on Incomplete Events in a format readable for GUI 
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
			
			Calendar now = GregorianCalendar.getInstance();
			
			if(event.getEndDay().before(now) || event.getEndDay().equals(now)) {
				eventData.setIsExpired(true);
			}
			
			if(event.getStartDay().before(now) && event.getEndDay().after(now)) {
				eventData.setIsOngoing(true);
			}
			
			incompleteEvents.add(eventData);
		}
	}

	/**
	 * Passes the information on Completed Tasks in a format readable for GUI 
	 * @param fileLink
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
				
				if(endDay.get(Calendar.HOUR_OF_DAY) == 0 && endDay.get(Calendar.MINUTE) == 0) {
					taskData.setEndTime("-");
				} else {
					taskData.setEndTime(timeFormat.format(endDay.getTime()));
				}
			}
			
			completeTasks.add(taskData);
		}
	}
	
	
	/**
	 * Passes the information on Completed Tasks in a format readable for GUI 
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
			
			completeEvents.add(eventData);
		}
	}
	
	public void configHelpEvents() {
		EventDataUI helpEvent1 = new EventDataUI();
		EventDataUI helpEvent2 = new EventDataUI();
		
		helpEvent1.setID(NUMBER_SET_ID_ONE);
		helpEvent1.setName(MESSAGE_EXPIRED_EVENTS_ARE_HIGHLIGHTED_IN_RED);
		helpEvent1.setPriority("HIGH");
		helpEvent1.setStartDate("-");
		helpEvent1.setStartTime("-");
		helpEvent1.setEndDate("-");
		helpEvent1.setEndTime("-");
		helpEvent1.setIsExpired(true);
		helpEvent1.setIsOngoing(false);
		
		helpEvent2.setID(NUMBER_SET_ID_TWO);
		helpEvent2.setName(MESSAGE_ONGOING_EVENTS_ARE_HIGHLIGHTED_IN_BLUE);
		helpEvent2.setPriority("LOW");
		helpEvent2.setStartDate("-");
		helpEvent2.setStartTime("-");
		helpEvent2.setEndDate("-");
		helpEvent2.setEndTime("-");
		helpEvent2.setIsExpired(false);
		helpEvent2.setIsOngoing(true);
		
		helpEvents.add(helpEvent1);
		helpEvents.add(helpEvent2);
	}
	
	private String determinePriority(int priority) {
		if(priority == PRIORITY_NUM_LOW) {
			return "LOW";
		} else if(priority == PRIORITY_NUM_MED) {
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
	
	public ArrayList<EventDataUI> getHelpEvents() {
		configHelpEvents();
		return helpEvents;
	}
	
	public String getFeedback() {
		return feedback;
	}
	
	public void setFeedback(String message) {
		feedback = message;
	}
	
	public int getRowAdded() {
		return row_added;
	}
	
	public int getFileAdded() {
		return file_added;
	}
	
	public void setRowAdded(int added) {
		row_added = added;
	}
	
	public void setFileAdded(int added) {
		file_added = added;
	}
	
	public String getUIClock() {
		return UIclock;
	}
	
	public void setUIclock(Calendar now) {
		Date clock = now.getTime();
		UIclock = timeUIFormat.format(clock);
	}
	
	public String getUIdate() {
		return UIdate;
	}
	
	public void setUIdate(Calendar now) {
		Date date = now.getTime();
		UIdate = dateFormat.format(date);
	}
}
