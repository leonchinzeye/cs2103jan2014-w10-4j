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
	
	/**
	 * Possible search keywords:
	 * Chronological - Today, Tomorrow
	 * @return
	 * @author Omar Khalid
	 * @param dataToBePassedToUI 
	 */
	public boolean executeSearch(String userInput, FileLinker fileLink, DataUI dataUI) {
		boolean success = false;
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);

		if(newCmd()) {			
			if(!checkForArg(tokenizedInput)) {
				noArgument(dataUI);
				return false;
			} else {
				checkKeywordAndIdentify(tokenizedInput[SECOND_ARGUMENT], fileLink, dataUI);
				success = true;
			}
		} else {
			checkKeywordAndIdentify(tokenizedInput[SECOND_ARGUMENT], fileLink, dataUI);
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
		 ArrayList<Integer> IncTaskIndex = searchIncompleteTasksByDate(searchInput, fileLink);
		 ArrayList<Integer> IncEventIndex = searchIncompleteEventsByDate(searchInput, fileLink);
		 ArrayList<Integer> CompTaskIndex = searchCompleteTasksByDate(searchInput, fileLink);
		 ArrayList<Integer> CompEventIndex = searchCompleteEventByDate(searchInput, fileLink);
	  
	}

	private void searchByKeyword(String searchInput, FileLinker fileLink,
	    DataUI dataUI) {
		ArrayList<Integer> incTaskIndex = new ArrayList<Integer>();
		ArrayList<Integer> incEventIndex = new ArrayList<Integer>(); 
		ArrayList<Integer> compTaskIndex = new ArrayList<Integer>();
		ArrayList<Integer> compEventIndex = new ArrayList<Integer>();
		
		switch(reservedKeywords.get(searchInput)) {
			case 1:
				incTaskIndex = searchIncTaskToday(fileLink, dataUI);
				incEventIndex = searchIncEventToday(fileLink, dataUI);
				compTaskIndex = searchCompTaskToday(fileLink, dataUI);
				compEventIndex = searchCompEventToday(fileLink, dataUI);
				break;
			default:
				break;
		}
	  
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
		
	  return null;
  }

	private ArrayList<Integer> searchIncEventToday(FileLinker fileLink,
      DataUI dataUI) {
	  // TODO Auto-generated method stub
	  return null;
  }

	private ArrayList<Integer> searchCompTaskToday(FileLinker fileLink,
      DataUI dataUI) {
	  // TODO Auto-generated method stub
	  return null;
  }

	private ArrayList<Integer> searchCompEventToday(FileLinker fileLink,
      DataUI dataUI) {
	  // TODO Auto-generated method stub
	  return null;
  }

	private void performNormalSearch(String searchInput, FileLinker fileLink,
	    DataUI dataUI) {
	  ArrayList<Integer> IncTaskIndex = searchIncompleteTasks(searchInput, fileLink);
	  ArrayList<Integer> IncEventIndex = searchIncompleteEvents(searchInput, fileLink);
	  ArrayList<Integer> CompTaskIndex = searchCompleteTasks(searchInput, fileLink);
	  ArrayList<Integer> CompEventIndex = searchCompleteEvent(searchInput, fileLink);
	}

	private ArrayList<Integer> searchIncompleteTasksByDate(String searchInput,
      FileLinker fileLink) {
		ArrayList<TaskCard> incTasks = fileLink.getCompletedEvents();
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
		ArrayList<TaskCard> incEvents = fileLink.getCompletedEvents();
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
		ArrayList<TaskCard> compTasks = fileLink.getCompletedEvents();
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
		ArrayList<TaskCard> incTasks = fileLink.getCompletedEvents();
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
		ArrayList<TaskCard> incEvents = fileLink.getCompletedEvents();
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
		ArrayList<TaskCard> compTasks = fileLink.getCompletedEvents();
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
		reservedKeywords.put("tomorrow", 2);
		reservedKeywords.put("tmr", 2);
		reservedKeywords.put("next week", 3);
		reservedKeywords.put("daily", 4);
		reservedKeywords.put("weekly", 5);
		reservedKeywords.put("yearly", 6);
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
