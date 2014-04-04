package application;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Search {
	
	private static final int SECOND_ARGUMENT = 1;
	
	private static final int SEARCH_TODAY = 1;
	private static final int SEARCH_PRIORITY = 2;
	private static final int SEARCH_TMR = 3;
	
	private static final int TYPE_INC_TASKS = 1;
	private static final int TYPE_INC_EVENTS = 2;
	private static final int TYPE_COMP_TASKS = 3;
	private static final int TYPE_COMP_EVENTS = 4;
	
	private static final String FEEDBACK_SEARCH_PROMPT = "What is it that you are looking for?";
	
	private HashMap<String, Integer> reservedKeywords;
	
	private static Calendar today;
	private static SimpleDateFormat fullDateString = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	
	public Search() {
		reservedKeywords = new HashMap<String, Integer>();
		initKeywordTable();
		
		getToday();
		fullDateString.setLenient(false);
		dateString.setLenient(false);
	}

	public void executeSearch(String userInput, FileLinker fileLink, DataUI dataUI) {
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		if(!checkForArg(tokenizedInput)) {
			noArgument(dataUI);
		} else {
			checkKeywordAndIdentify(tokenizedInput[SECOND_ARGUMENT], fileLink, dataUI);
			RefreshUI.executeRefresh(fileLink, dataUI);
		}
		dataUI.setFeedback("Displaying results for \"" + tokenizedInput[1] + "\"");
	}
	
	private void noArgument(DataUI dataUI) {
		dataUI.setFeedback(FEEDBACK_SEARCH_PROMPT);
	}
	
	/**
	 * checks to see what the search is
	 * it can be either 3 possibilities
	 * 1) search by a date
	 * 2)	search using reserved keywords
	 * 3) a normal search using user defined keywords
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 */
	private void checkKeywordAndIdentify(String searchInput, FileLinker fileLink,
			DataUI dataUI) {
		
		boolean isDate = checkIsDate(searchInput);
		if(isDate) {
			searchByDate(searchInput, fileLink, dataUI);
			return;
		}
		
		boolean hasKeyword = reservedKeywords.containsKey(searchInput);
		if(hasKeyword) {
			searchByKeyword(searchInput, fileLink, dataUI);
			return;
		}
		
		performNormalSearch(searchInput, fileLink, dataUI);
	}
	
	/**
	 * takes in the user specified date and searches for tasks that have that date
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 */
	private void searchByDate(String searchInput, FileLinker fileLink,
			DataUI dataUI) {
		ArrayList<TaskCard> searchedIncTasks = searchIncompleteTasksByDate(searchInput, fileLink);
		ArrayList<TaskCard> searchedIncEvents = searchIncompleteEventsByDate(searchInput, fileLink);
		ArrayList<TaskCard> searchedCompTasks = searchCompleteTasksByDate(searchInput, fileLink);
		ArrayList<TaskCard> searchedCompEvents = searchCompleteEventsByDate(searchInput, fileLink);
		
		fileLink.searchHandling(searchedIncTasks, searchedIncEvents, searchedCompTasks, searchedCompEvents);
	}
	
	/**
	 * user input has been identified as a reserved keyword and will perform roles based on that
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 */
	private void searchByKeyword(String searchInput, FileLinker fileLink,
			DataUI dataUI) {

		ArrayList<TaskCard> searchedIncTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> searchedIncEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> searchedCompTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> searchedCompEvents = new ArrayList<TaskCard>();
		
		switch(reservedKeywords.get(searchInput)) {
			case SEARCH_TODAY:
				searchedIncTasks = searchTaskToday(fileLink, dataUI, TYPE_INC_TASKS);
				searchedIncEvents = searchEventToday(fileLink, dataUI, TYPE_INC_EVENTS);
				searchedCompTasks = searchTaskToday(fileLink, dataUI, TYPE_COMP_TASKS);
				searchedCompEvents = searchEventToday(fileLink, dataUI, TYPE_COMP_EVENTS);
				break;
			case SEARCH_PRIORITY:
				searchedIncTasks = searchIncTaskPriority(searchInput, fileLink, dataUI);
				searchedIncEvents = searchIncEventPriority(searchInput, fileLink, dataUI);
				searchedCompTasks = searchCompTaskPriority(searchInput, fileLink, dataUI);
				searchedCompEvents = searchCompEventPriority(searchInput, fileLink, dataUI);
				break;
			case SEARCH_TMR:
				searchedIncTasks = searchTaskTmr(searchInput, fileLink, dataUI, TYPE_INC_TASKS);
				searchedIncEvents = searchEventTmr(searchInput, fileLink, dataUI, TYPE_INC_EVENTS);
				searchedCompTasks= searchTaskTmr(searchInput, fileLink, dataUI, TYPE_COMP_TASKS);
				searchedCompEvents = searchEventTmr(searchInput, fileLink, dataUI, TYPE_COMP_EVENTS);
			default:
				break;
		}
		
		fileLink.searchHandling(searchedIncTasks, searchedIncEvents, searchedCompTasks, searchedCompEvents);
	}
	
	private ArrayList<TaskCard> searchTaskTmr(String searchInput,
      FileLinker fileLink, DataUI dataUI, int type) {
	  ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
	  ArrayList<TaskCard> listOfTasks = new ArrayList<TaskCard>();
	  if(type == TYPE_INC_TASKS) {
	  	listOfTasks = fileLink.getIncompleteTasks();
	  } else {
	  	listOfTasks = fileLink.getCompletedTasks();
	  }
	  
	  Calendar tmr = getTmr();
	  
	  for(int i = 0; i < listOfTasks.size(); i++) {
	  	TaskCard task = listOfTasks.get(i);
	  	if(tmr.before(task.getEndDay())) {
	  		searchedTasks.add(task);
	  	}
	  }

	  return searchedTasks;
  }

	private ArrayList<TaskCard> searchEventTmr(String searchInput,
      FileLinker fileLink, DataUI dataUI, int type) {
		ArrayList<TaskCard> searchedEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> listOfEvents = new ArrayList<TaskCard>();
		
		if(type == TYPE_INC_EVENTS) {
			listOfEvents = fileLink.getIncompleteEvents();
		} else {
			listOfEvents = fileLink.getCompletedEvents();
		}
		
		Calendar tmr = getTmr();
		
		for(int i = 0; i < listOfEvents.size(); i++) {
			TaskCard event = listOfEvents.get(i);
			if(tmr.before(event.getEndDay()) && tmr.after(event.getStartDay())) {
				listOfEvents.add(event);
			} else if(event.getStartDay().after(tmr) && event.getEndDay().before(getTmrEnd())) {
				listOfEvents.add(event);
			}
		}
		
		return searchedEvents;
	}

	/**
	 * searches incomplete tasks that can be done today
	 * @author leon
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	private ArrayList<TaskCard> searchTaskToday(FileLinker fileLink,
			DataUI dataUI, int type) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> listOfTasks = new ArrayList<TaskCard>();
		if(type == TYPE_INC_TASKS) {
			listOfTasks = fileLink.getIncompleteTasks();
		} else {
			listOfTasks = fileLink.getCompletedTasks();
		}
		for(int i = 0; i < listOfTasks.size(); i++) {
			TaskCard task = listOfTasks.get(i);
			if(task.getEndDay().after(today)) {
				searchedTasks.add(task);
			}
		}
		
		return searchedTasks;
	}

	/**
	 * searches incomplete events that happen today
	 * @author leon
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	private ArrayList<TaskCard> searchEventToday(FileLinker fileLink,
			DataUI dataUI, int type) {
		ArrayList<TaskCard> searchedEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> listOfEvents = new ArrayList<TaskCard>();
		
		if(type == TYPE_INC_EVENTS) {
			listOfEvents = fileLink.getIncompleteEvents();
		} else {
			listOfEvents = fileLink.getCompletedEvents();
		}
		
		for(int i = 0; i < listOfEvents.size(); i++) {
			TaskCard event = listOfEvents.get(i);
			Calendar eventStart = event.getStartDay();
			Calendar eventEnd = event.getEndDay();
			
			if(eventStart.after(today) && eventEnd.before(getTmr())) {
				searchedEvents.add(event);
			} else if(today.before(eventEnd) && today.after(eventStart)) {
				searchedEvents.add(event);
			}
		}
		
		return searchedEvents;
	}

	/**
	 * searches based on priority for incompleted tasks
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	private ArrayList<TaskCard> searchIncTaskPriority(String searchInput, FileLinker fileLink,
			DataUI dataUI) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> incTask = fileLink.getIncompleteTasks();
		
		int priority;
		if(searchInput.equals("LOW")) {
			priority = 1;
		} else if(searchInput.equals("MED")) {
			priority = 2;
		} else {
			priority = 3;
		}
		
		for(int i = 0; i < incTask.size(); i++) {
			TaskCard task = incTask.get(i);
			if(task.getPriority() == priority) {
				searchedTasks.add(task);
			}
		}
		
		return searchedTasks;
	}
	
	/**
	 * searches based on priority for incomplete events
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	private ArrayList<TaskCard> searchIncEventPriority(String searchInput, FileLinker fileLink,
			DataUI dataUI) {
		ArrayList<TaskCard> searchedEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> incTask = fileLink.getIncompleteEvents();
		
		int priority;
		if(searchInput.equals("LOW")) {
			priority = 1;
		} else if(searchInput.equals("MED")) {
			priority = 2;
		} else {
			priority = 3;
		}
		
		for(int i = 0; i < incTask.size(); i++) {
			TaskCard event = incTask.get(i);
			if(event.getPriority() == priority) {
				searchedEvents.add(event);
			}
		}
		
		return searchedEvents;
	}
	
	/**
	 * searches based on priority for completed tasks
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	private ArrayList<TaskCard> searchCompTaskPriority(String searchInput, FileLinker fileLink,
			DataUI dataUI) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> compTask = fileLink.getCompletedTasks();
		
		int priority;
		if(searchInput.equals("LOW")) {
			priority = 1;
		} else if(searchInput.equals("MED")) {
			priority = 2;
		} else {
			priority = 3;
		}
		
		for(int i = 0; i < compTask.size(); i++) {
			TaskCard task = compTask.get(i);
			if(task.getPriority() == priority) {
				searchedTasks.add(task);
			}
		}
		
		return searchedTasks;
	}
	
	/**
	 * searches based on priority for completed events
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 * @return
	 */
	private ArrayList<TaskCard> searchCompEventPriority(String searchInput, FileLinker fileLink,
			DataUI dataUI) {
		ArrayList<TaskCard> searchedEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> compEvent = fileLink.getCompletedEvents();
		
		int priority;
		if(searchInput.equals("LOW")) {
			priority = 1;
		} else if(searchInput.equals("MED")) {
			priority = 2;
		} else {
			priority = 3;
		}
		
		for(int i = 0; i < compEvent.size(); i++) {
			TaskCard event = compEvent.get(i);
			if(event.getPriority() == priority) {
				searchedEvents.add(event);
			}
		}
		
		return searchedEvents;
	}
	
	/**
	 * searches based on user keyword
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param dataUI
	 */
	private void performNormalSearch(String searchInput, FileLinker fileLink,
			DataUI dataUI) {
		ArrayList<TaskCard> searchedIncTasks = searchIncompleteTasks(searchInput, fileLink);
		ArrayList<TaskCard> searchedIncEvents = searchIncompleteEvents(searchInput, fileLink);
		ArrayList<TaskCard> searchedCompTasks = searchCompleteTasks(searchInput, fileLink);
		ArrayList<TaskCard> searchedCompEvents = searchCompleteEvents(searchInput, fileLink);
		
		fileLink.searchHandling(searchedIncTasks, searchedIncEvents, searchedCompTasks, searchedCompEvents);
		
	}
	
	/**
	 * searches incomplete tasks for a specific due date
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchIncompleteTasksByDate(String searchInput,
			FileLinker fileLink) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();

		for(int i = 0; i < incTasks.size(); i++) {
			TaskCard task = incTasks.get(i);
			String taskDetails = task.getName().toLowerCase();
			String taskTime = fullDateString.format(task.getEndDay().getTime());
			String fullDetails = taskDetails + " " + taskTime;
			searchInput = searchInput.toLowerCase();
			
			if(fullDetails.contains(searchInput)) {
				searchedTasks.add(task);
			}
		}
		
		return searchedTasks;
	}
	
	/**
	 * searches incomplete events that fall on that date
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchIncompleteEventsByDate(String searchInput,
			FileLinker fileLink) {
		ArrayList<TaskCard> searchedEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();

		for(int i = 0; i < incEvents.size(); i++) {
			TaskCard event = incEvents.get(i);
			String eventDetails = event.getName().toLowerCase();
			String eventStartTime = fullDateString.format(event.getStartDay().getTime());
			String eventEndTime = fullDateString.format(event.getEndDay().getTime());
			String fullDetails = eventDetails + " "+ eventStartTime + " " + eventEndTime;
			searchInput = searchInput.toLowerCase();
			
			if(fullDetails.contains(searchInput)) {
				searchedEvents.add(event);
			}
		}
		
		return searchedEvents;
	}
	
	/**
	 * searches completed tasks that are due on that date
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchCompleteTasksByDate(String searchInput,
			FileLinker fileLink) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> compTasks = fileLink.getCompletedTasks();
		for(int i = 0; i < compTasks.size(); i++) {
			TaskCard task = compTasks.get(i);
			String taskDetails = task.getName().toLowerCase();
			String taskTime = fullDateString.format(task.getEndDay().getTime());
			String fullDetails = taskDetails + " " + taskTime;
			searchInput = searchInput.toLowerCase();
			
			if(fullDetails.contains(searchInput)) {
				searchedTasks.add(task);
			}
		}
		
		return searchedTasks;
	}
	
	/**
	 * searches completed events that passed on that day
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchCompleteEventsByDate(String searchInput,
			FileLinker fileLink) {
		ArrayList<TaskCard> searchedEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> compEvents = fileLink.getCompletedEvents();
		for(int i = 0; i < compEvents.size(); i++) {
			TaskCard event = compEvents.get(i);
			String eventDetails = event.getName().toLowerCase();
			String eventStartTime = fullDateString.format(event.getStartDay().getTime());
			String eventEndTime = fullDateString.format(event.getEndDay().getTime());
			String fullDetails = eventDetails + " "+ eventStartTime + " " + eventEndTime;
			searchInput = searchInput.toLowerCase();
			
			if(fullDetails.contains(searchInput)) {
				searchedEvents.add(event);
			}
		}
		
		return searchedEvents;
	}
	
	/**
	 * searches incomplete tasks for a user defined keyword
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchIncompleteTasks(String searchInput, FileLinker fileLink) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
		for(int i = 0; i < incTasks.size(); i++) {
			TaskCard task = incTasks.get(i);
			String taskDetails = task.getName().toLowerCase();
			searchInput = searchInput.toLowerCase();
			
			if(taskDetails.contains(searchInput)) {
				searchedTasks.add(task);
			}
		}
		
		return searchedTasks;
	}
	
	/**
	 * searches incomplete events for a user defined keyword
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchIncompleteEvents(String searchInput, FileLinker fileLink) {
		ArrayList<TaskCard> searchedEvents= new ArrayList<TaskCard>();
		ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();
		for(int i = 0; i < incEvents.size(); i++) {
			TaskCard event = incEvents.get(i);
			String eventDetails = event.getName().toLowerCase();
			searchInput = searchInput.toLowerCase();
			
			if(eventDetails.contains(searchInput)) {
				searchedEvents.add(event);
			}
		}
		
		return searchedEvents;
	}
	
	/**
	 * searches completed tasks for a user defined keyword
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchCompleteTasks(String searchInput, FileLinker fileLink) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> compTasks = fileLink.getCompletedTasks();
		for(int i = 0; i < compTasks.size(); i++) {
			TaskCard task = compTasks.get(i);
			String taskDetails = task.getName().toLowerCase();
			searchInput = searchInput.toLowerCase();
			
			if(taskDetails.contains(searchInput)) {
				searchedTasks.add(task);
			}
		}
		
		return searchedTasks;
	}
	
	/**
	 * searches completed events for a user defined keyword
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @return
	 */
	private ArrayList<TaskCard> searchCompleteEvents(String searchInput, FileLinker fileLink) {
		ArrayList<TaskCard> searchedEvents= new ArrayList<TaskCard>();
		ArrayList<TaskCard> compEvents = fileLink.getCompletedEvents();
		for(int i = 0; i < compEvents.size(); i++) {
			TaskCard event = compEvents.get(i);
			String eventDetails = event.getName().toLowerCase();
			searchInput = searchInput.toLowerCase();
			
			if(eventDetails.contains(searchInput)) {
				searchedEvents.add(event);
			}
		}
		
		return searchedEvents;
	}
	
	private void getToday() {
	  today = GregorianCalendar.getInstance();
	  today.set(Calendar.HOUR_OF_DAY, 0);
	  today.set(Calendar.MINUTE, 0);
	  today.set(Calendar.SECOND, 0);
	  today.set(Calendar.MILLISECOND, 0);
	}

	private Calendar getTmr() {
		Calendar tmr = GregorianCalendar.getInstance();
		tmr.add(Calendar.DATE, 1);
		tmr.set(Calendar.HOUR_OF_DAY, 0);
		tmr.set(Calendar.MINUTE, 0);
		tmr.set(Calendar.SECOND, 0);
		tmr.set(Calendar.MILLISECOND, 0);
		
	  return tmr;
	}
	
	private Calendar getTmrEnd() {
		Calendar tmr = GregorianCalendar.getInstance();
		tmr.add(Calendar.DATE, 1);
		tmr.set(Calendar.HOUR_OF_DAY, 23);
		tmr.set(Calendar.MINUTE, 59);
		tmr.set(Calendar.SECOND, 59);
		tmr.set(Calendar.MILLISECOND, 999);
		
	  return tmr;
	}

	private boolean checkIsDate(String searchInput) {
		try {
			dateString.parse(searchInput);
			return true;
		} catch(ParseException e) {
			return false;
		}
	}
	
	private boolean checkForArg(String[] tokenizedInput) {
		if(tokenizedInput.length < 2) {
			return false;
		} else {
			return true;
		}
	}
	
	private void initKeywordTable() {
		reservedKeywords.put("today", 1);
		reservedKeywords.put("tdy", 1);
		reservedKeywords.put("LOW", 2);
		reservedKeywords.put("MED", 2);
		reservedKeywords.put("HIGH", 2);
		reservedKeywords.put("tomorrow", 3);
		reservedKeywords.put("tmr", 3);
	}
}
