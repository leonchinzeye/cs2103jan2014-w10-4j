package application;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Search {

	private static final int SECOND_ARGUMENT = 1;
	
	private static final int SEARCH_TODAY = 1;
	private static final int SEARCH_PRIORITY = 2;
	private static final int SEARCH_FREQUENCY = 3;

	private static final String FEEDBACK_SEARCH_PROMPT = "What is it that you want to search for?";

	private HashMap<String, Integer> reservedKeywords;
	
	private boolean state_error;
	
	private static Date today;
	private static SimpleDateFormat fullDateString = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	
	public Search() {
		reservedKeywords = new HashMap<String, Integer>();
		initKeywordTable();
		
		today = new Date();
		fullDateString.setLenient(false);
		dateString.setLenient(false);
		
		state_error = false;
	}
	
	public boolean executeSearch(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);

		if(newCmd()) {			
			if(!checkForArg(tokenizedInput)) {
				noArgument(dataUI);
				return false;
			} else {
				checkKeywordAndIdentify(tokenizedInput[SECOND_ARGUMENT], fileLink, dataUI);
				RefreshUI.executeRefresh(fileLink, dataUI);
				success = true;
			}
		} else {
			checkKeywordAndIdentify(tokenizedInput[SECOND_ARGUMENT], fileLink, dataUI);
			RefreshUI.executeRefresh(fileLink, dataUI);
			success = true;
		}
		return success;
	}

	private void noArgument(DataUI dataUI) {
	  dataUI.setFeedback(FEEDBACK_SEARCH_PROMPT);
	  state_error = true;
  }
	
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

	private void searchByDate(String searchInput, FileLinker fileLink,
	    DataUI dataUI) {
		 ArrayList<Integer> incTaskIndex = searchIncompleteTasksByDate(searchInput, fileLink);
		 ArrayList<Integer> incEventIndex = searchIncompleteEventsByDate(searchInput, fileLink);
		 ArrayList<Integer> compTaskIndex = searchCompleteTasksByDate(searchInput, fileLink);
		 ArrayList<Integer> compEventIndex = searchCompleteEventByDate(searchInput, fileLink);
	 
		 fileLink.searchHandling(incTaskIndex, incEventIndex, compTaskIndex, compEventIndex);
	}

	private void searchByKeyword(String searchInput, FileLinker fileLink,
	    DataUI dataUI) {
		ArrayList<Integer> incTaskIndex = new ArrayList<Integer>();
		ArrayList<Integer> incEventIndex = new ArrayList<Integer>(); 
		ArrayList<Integer> compTaskIndex = new ArrayList<Integer>();
		ArrayList<Integer> compEventIndex = new ArrayList<Integer>();
		
		switch(reservedKeywords.get(searchInput)) {
			case SEARCH_TODAY:
				incTaskIndex = searchIncTaskToday(fileLink, dataUI);
				incEventIndex = searchIncEventToday(fileLink, dataUI);
				compTaskIndex = searchCompTaskToday(fileLink, dataUI);
				compEventIndex = searchCompEventToday(fileLink, dataUI);
				break;
			case SEARCH_PRIORITY:
				incTaskIndex = searchIncTaskPriority(searchInput, fileLink, dataUI);
				incEventIndex = searchIncEventPriority(searchInput, fileLink, dataUI);
				compTaskIndex = searchCompTaskPriority(searchInput, fileLink, dataUI);
				compEventIndex = searchCompEventPriority(searchInput, fileLink, dataUI);
				break;
			case SEARCH_FREQUENCY:
				incTaskIndex = searchIncTaskFreq(searchInput, fileLink, dataUI);
				incEventIndex = searchIncEventFreq(searchInput, fileLink, dataUI);
				compTaskIndex = searchCompTaskFreq(searchInput, fileLink, dataUI);
				compEventIndex = searchCompEventFreq(searchInput, fileLink, dataUI);
			default:
				break;
		}
	  
		fileLink.searchHandling(incTaskIndex, incEventIndex, compTaskIndex, compEventIndex);
	}

	private ArrayList<Integer> searchIncTaskFreq(String searchInput,
      FileLinker fileLink, DataUI dataUI) {
	  ArrayList<Integer> index = new ArrayList<Integer>();
	  ArrayList<TaskCard> incTask = fileLink.getIncompleteTasks();
	  
	  String freq;
	  if(searchInput.equals("daily")) {
	  	freq = "D";
	  } else if(searchInput.equals("monthly")) {
	  	freq = "M";
	  } else {
	  	freq = "Y";
	  }
	  
	  for(int i = 0; i < incTask.size(); i++) {
	  	TaskCard task = incTask.get(i);
	  	if(task.getFrequency() == freq) {
	  		index.add(i);
	  	}
	  }
	  
		return index;
  }

	private ArrayList<Integer> searchIncEventFreq(String searchInput,
      FileLinker fileLink, DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
	  ArrayList<TaskCard> incEvent = fileLink.getIncompleteEvents();
	  
	  String freq;
	  if(searchInput.equals("daily")) {
	  	freq = "D";
	  } else if(searchInput.equals("monthly")) {
	  	freq = "M";
	  } else {
	  	freq = "Y";
	  }
	  
	  for(int i = 0; i < incEvent.size(); i++) {
	  	TaskCard event = incEvent.get(i);
	  	if(event.getFrequency() == freq) {
	  		index.add(i);
	  	}
	  }
	  
		return index;
  }

	private ArrayList<Integer> searchCompTaskFreq(String searchInput,
      FileLinker fileLink, DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
	  ArrayList<TaskCard> compTask = fileLink.getCompletedTasks();
	  
	  String freq;
	  if(searchInput.equals("daily")) {
	  	freq = "D";
	  } else if(searchInput.equals("monthly")) {
	  	freq = "M";
	  } else {
	  	freq = "Y";
	  }
	  
	  for(int i = 0; i < compTask.size(); i++) {
	  	TaskCard task = compTask.get(i);
	  	if(task.getFrequency() == freq) {
	  		index.add(i);
	  	}
	  }
	  
		return index;
  }

	private ArrayList<Integer> searchCompEventFreq(String searchInput,
      FileLinker fileLink, DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
	  ArrayList<TaskCard> compEvent = fileLink.getCompletedEvents();
	  
	  String freq;
	  if(searchInput.equals("daily")) {
	  	freq = "D";
	  } else if(searchInput.equals("monthly")) {
	  	freq = "M";
	  } else {
	  	freq = "Y";
	  }
	  
	  for(int i = 0; i < compEvent.size(); i++) {
	  	TaskCard event = compEvent.get(i);
	  	if(event.getFrequency() == freq) {
	  		index.add(i);
	  	}
	  }
	  
		return index;
  }

	private ArrayList<Integer> searchIncTaskPriority(String searchInput, FileLinker fileLink,
      DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		ArrayList<TaskCard> incTask = fileLink.getIncompleteTasks();
		
	  int priority;
	  if(searchInput == "LOW") {
	  	priority = 1;
	  } else if(searchInput == "MED") {
	  	priority = 2;
	  } else {
	  	priority = 3;
	  }
	  
	  for(int i = 0; i < incTask.size(); i++) {
	  	TaskCard task = incTask.get(i);
	  	if(task.getPriority() == priority) {
	  		index.add(i);
	  	}
	  }
	  
	  return index;
  }

	private ArrayList<Integer> searchIncEventPriority(String searchInput, FileLinker fileLink,
      DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		ArrayList<TaskCard> incTask = fileLink.getIncompleteEvents();
		
	  int priority;
	  if(searchInput == "LOW") {
	  	priority = 1;
	  } else if(searchInput == "MED") {
	  	priority = 2;
	  } else {
	  	priority = 3;
	  }
	  
	  for(int i = 0; i < incTask.size(); i++) {
	  	TaskCard event = incTask.get(i);
	  	if(event.getPriority() == priority) {
	  		index.add(i);
	  	}
	  }
	  
	  return index;
  }

	private ArrayList<Integer> searchCompTaskPriority(String searchInput, FileLinker fileLink,
      DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		ArrayList<TaskCard> compTask = fileLink.getCompletedTasks();
		
	  int priority;
	  if(searchInput == "LOW") {
	  	priority = 1;
	  } else if(searchInput == "MED") {
	  	priority = 2;
	  } else {
	  	priority = 3;
	  }
	  
	  for(int i = 0; i < compTask.size(); i++) {
	  	TaskCard task = compTask.get(i);
	  	if(task.getPriority() == priority) {
	  		index.add(i);
	  	}
	  }
	  
	  return index;
  }

	private ArrayList<Integer> searchCompEventPriority(String searchInput, FileLinker fileLink,
      DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
		ArrayList<TaskCard> compEvent = fileLink.getIncompleteTasks();
		
	  int priority;
	  if(searchInput == "LOW") {
	  	priority = 1;
	  } else if(searchInput == "MED") {
	  	priority = 2;
	  } else {
	  	priority = 3;
	  }
	  
	  for(int i = 0; i < compEvent.size(); i++) {
	  	TaskCard event = compEvent.get(i);
	  	if(event.getPriority() == priority) {
	  		index.add(i);
	  	}
	  }
	  
	  return index;
  }

	private void performNormalSearch(String searchInput, FileLinker fileLink,
	    DataUI dataUI) {
	  ArrayList<Integer> incTaskIndex = searchIncompleteTasks(searchInput, fileLink);
	  ArrayList<Integer> incEventIndex = searchIncompleteEvents(searchInput, fileLink);
	  ArrayList<Integer> compTaskIndex = searchCompleteTasks(searchInput, fileLink);
	  ArrayList<Integer> compEventIndex = searchCompleteEvent(searchInput, fileLink);
	  
	  fileLink.searchHandling(incTaskIndex, incEventIndex, compTaskIndex, compEventIndex);

	}

	private ArrayList<Integer> searchIncTaskToday(FileLinker fileLink,
	    DataUI dataUI) {
	  ArrayList<Integer> index = new ArrayList<Integer>();
	  ArrayList<TaskCard> incTask = fileLink.getIncompleteTasks();
	  
	  for(int i = 0; i < incTask.size(); i++) {
	  	TaskCard task = incTask.get(i);
	  	if(task.getEndDay().after(today)) {
	  		index.add(i);
	  	}
	  }
		
	  return index;
	}

	private ArrayList<Integer> searchIncEventToday(FileLinker fileLink,
	    DataUI dataUI) {
		Date todayStartRange = setTodayStartRange();
		Date todayEndRange = setTodayEndRange();
		ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int i = 0; i < incEvents.size(); i++) {
			TaskCard event = incEvents.get(i);
			Date eventStart = event.getStartDay().getTime();
			Date eventEnd = event.getEndDay().getTime();
			
			if(eventStart.before(today) && eventEnd.after(today)) {
				index.add(i);
			} else if(eventStart.after(todayStartRange) && eventEnd.before(todayEndRange)) {
				index.add(i);
			}
		}
		
	  return index;
	}

	private ArrayList<Integer> searchCompTaskToday(FileLinker fileLink,
	    DataUI dataUI) {
		ArrayList<Integer> index = new ArrayList<Integer>();
	  ArrayList<TaskCard> incTask = fileLink.getCompletedTasks();
	  
	  for(int i = 0; i < incTask.size(); i++) {
	  	TaskCard task = incTask.get(i);
	  	if(task.getEndDay().after(today)) {
	  		index.add(i);
	  	}
	  }
		
	  return index;
	}

	private ArrayList<Integer> searchCompEventToday(FileLinker fileLink,
	    DataUI dataUI) {
		Date todayStartRange = setTodayStartRange();
		Date todayEndRange = setTodayEndRange();
		ArrayList<TaskCard> compEvents = fileLink.getCompletedEvents();
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int i = 0; i < compEvents.size(); i++) {
			TaskCard event = compEvents.get(i);
			Date eventStart = event.getStartDay().getTime();
			Date eventEnd = event.getEndDay().getTime();
			
			if(eventStart.before(today) && eventEnd.after(today)) {
				index.add(i);
			} else if(eventStart.after(todayStartRange) && eventEnd.before(todayEndRange)) {
				index.add(i);
			}
		}
		
	  return index;
	}

	private ArrayList<Integer> searchIncompleteTasksByDate(String searchInput,
      FileLinker fileLink) {
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < incTasks.size(); i++) {
	  	TaskCard task = incTasks.get(i);
	  	String taskDetails = task.getName().toLowerCase();
	  	String taskTime = fullDateString.format(task.getEndDay().getTime());
	  	String fullDetails = taskDetails + " " + taskTime;
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(fullDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
  }

	private ArrayList<Integer> searchIncompleteEventsByDate(String searchInput,
      FileLinker fileLink) {
		ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < incEvents.size(); i++) {
	  	TaskCard task = incEvents.get(i);
	  	String eventDetails = task.getName().toLowerCase();
	  	String eventStartTime = fullDateString.format(task.getStartDay().getTime());
	  	String eventEndTime = fullDateString.format(task.getEndDay().getTime());
	  	String fullDetails = eventDetails + " "+ eventStartTime + " " + eventEndTime;
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(fullDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
  }

	private ArrayList<Integer> searchCompleteTasksByDate(String searchInput,
      FileLinker fileLink) {
		ArrayList<TaskCard> compTasks = fileLink.getCompletedTasks();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < compTasks.size(); i++) {
	  	TaskCard task = compTasks.get(i);
	  	String taskDetails = task.getName().toLowerCase();
	  	String taskTime = fullDateString.format(task.getEndDay().getTime());
	  	String fullDetails = taskDetails + " " + taskTime;
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(fullDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
  }

	private ArrayList<Integer> searchCompleteEventByDate(String searchInput,
      FileLinker fileLink) {
		ArrayList<TaskCard> compEvents = fileLink.getCompletedEvents();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < compEvents.size(); i++) {
	  	TaskCard task = compEvents.get(i);
	  	String eventDetails = task.getName().toLowerCase();
	  	String eventStartTime = fullDateString.format(task.getStartDay().getTime());
	  	String eventEndTime = fullDateString.format(task.getEndDay().getTime());
	  	String fullDetails = eventDetails + " "+ eventStartTime + " " + eventEndTime;
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(fullDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
  }

	private ArrayList<Integer> searchIncompleteTasks(String searchInput, FileLinker fileLink) {
		ArrayList<TaskCard> incTasks = fileLink.getIncompleteTasks();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < incTasks.size(); i++) {
	  	TaskCard task = incTasks.get(i);
	  	String taskDetails = task.getName().toLowerCase();
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(taskDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
  }

	private ArrayList<Integer> searchIncompleteEvents(String searchInput, FileLinker fileLink) {
		ArrayList<TaskCard> incEvents = fileLink.getIncompleteEvents();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < incEvents.size(); i++) {
	  	TaskCard event = incEvents.get(i);
	  	String eventDetails = event.getName().toLowerCase();
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(eventDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
	}

	private ArrayList<Integer> searchCompleteTasks(String searchInput, FileLinker fileLink) {
		ArrayList<TaskCard> compTasks = fileLink.getCompletedTasks();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < compTasks.size(); i++) {
	  	TaskCard task = compTasks.get(i);
	  	String taskDetails = task.getName().toLowerCase();
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(taskDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
	}

	private ArrayList<Integer> searchCompleteEvent(String searchInput, FileLinker fileLink) {
	  ArrayList<TaskCard> compEvents = fileLink.getCompletedEvents();
	  ArrayList<Integer> indexes = new ArrayList<Integer>();
	  for(int i = 0; i < compEvents.size(); i++) {
	  	TaskCard event = compEvents.get(i);
	  	String eventDetails = event.getName().toLowerCase();
	  	searchInput = searchInput.toLowerCase();
	  	
	  	if(eventDetails.contains(searchInput)) {
	  		indexes.add(i);
	  	}
	  }
	  
	  return indexes;
	}
	
	@SuppressWarnings("deprecation")
  private Date setTodayStartRange() {
		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		
		return date;
	}

	@SuppressWarnings("deprecation")
  private Date setTodayEndRange() {
		Date date = new Date();
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		
		return date;
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

	private boolean newCmd() {
	  if(state_error) {
	  	return false;
	  }
	  return true;
  }
	
	private void initKeywordTable() {
		reservedKeywords.put("today", 1);
		reservedKeywords.put("LOW", 2);
		reservedKeywords.put("MED", 2);
		reservedKeywords.put("HIGH", 2);
		reservedKeywords.put("daily", 3);
		reservedKeywords.put("weekly", 3);
		reservedKeywords.put("yearly", 3);
	}
	
	private static class SortPriority implements Comparator<TaskCard> {
		public int compare(TaskCard o1, TaskCard o2) {
			Integer i1 = (Integer) o1.getPriority();
			Integer i2 = (Integer) o2.getPriority();
			return i2.compareTo(i1);
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
}
