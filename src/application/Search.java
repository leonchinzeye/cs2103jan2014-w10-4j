package application;

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

	public Search() {
		reservedKeywords = new HashMap<String, Integer>();
		initKeywordTable();
		
		getToday();
	}

	public void executeSearch(String userInput, FileLinker fileLink, DataUI dataUI, DateAndTimeFormats dateFormats) {
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		if(!checkForArg(tokenizedInput)) {
			noArgument(dataUI);
		} else {
			checkKeywordAndIdentify(tokenizedInput[SECOND_ARGUMENT], fileLink, dataUI, dateFormats);
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
	 * @param dateFormats 
	 */
	private void checkKeywordAndIdentify(String searchInput, FileLinker fileLink,
			DataUI dataUI, DateAndTimeFormats dateFormats) {
		
		Date date = checkIsDate(searchInput, dateFormats);
		if(date != null) {
			searchByDate(searchInput, fileLink, dataUI, date);
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
	 * @param date 
	 */
	private void searchByDate(String searchInput, FileLinker fileLink,
			DataUI dataUI, Date date) {
		ArrayList<TaskCard> searchedIncTasks = searchTasksByDate(searchInput, fileLink, date, TYPE_INC_TASKS);
		ArrayList<TaskCard> searchedIncEvents = searchEventsByDate(searchInput, fileLink, date, TYPE_INC_EVENTS);
		ArrayList<TaskCard> searchedCompTasks = searchTasksByDate(searchInput, fileLink, date, TYPE_COMP_TASKS);
		ArrayList<TaskCard> searchedCompEvents = searchEventsByDate(searchInput, fileLink, date, TYPE_COMP_EVENTS);
		
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
				searchedIncTasks = searchPriority(searchInput, fileLink, dataUI, TYPE_INC_TASKS);
				searchedIncEvents = searchPriority(searchInput, fileLink, dataUI, TYPE_INC_EVENTS);
				searchedCompTasks = searchPriority(searchInput, fileLink, dataUI, TYPE_COMP_TASKS);
				searchedCompEvents = searchPriority(searchInput, fileLink, dataUI, TYPE_COMP_EVENTS);
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
	 * @param typeIncTasks 
	 * @return
	 */
	private ArrayList<TaskCard> searchPriority(String searchInput, FileLinker fileLink,
			DataUI dataUI, int type) {
		ArrayList<TaskCard> searchedPriority = new ArrayList<TaskCard>();
		ArrayList<TaskCard> listToBeSearched = new ArrayList<TaskCard>();
		
		if(type == TYPE_INC_TASKS) {
			listToBeSearched = fileLink.getIncompleteTasks();
		} else if(type == TYPE_INC_EVENTS) {
			listToBeSearched = fileLink.getIncompleteEvents();
		} else if(type == TYPE_COMP_TASKS) {
			listToBeSearched = fileLink.getCompletedTasks();
		} else {
			listToBeSearched = fileLink.getCompletedEvents();
		}
		
		int priority;
		if(searchInput.equals("LOW")) {
			priority = 1;
		} else if(searchInput.equals("MED")) {
			priority = 2;
		} else {
			priority = 3;
		}
		
		for(int i = 0; i < listToBeSearched.size(); i++) {
			TaskCard task = listToBeSearched.get(i);
			if(task.getPriority() == priority) {
				searchedPriority.add(task);
			}
		}
		
		return searchedPriority;
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
		ArrayList<TaskCard> searchedIncTasks = searchByUserWord(searchInput, fileLink, TYPE_INC_TASKS);
		ArrayList<TaskCard> searchedIncEvents = searchByUserWord(searchInput, fileLink, TYPE_INC_EVENTS);
		ArrayList<TaskCard> searchedCompTasks = searchByUserWord(searchInput, fileLink, TYPE_COMP_TASKS);
		ArrayList<TaskCard> searchedCompEvents = searchByUserWord(searchInput, fileLink, TYPE_COMP_EVENTS);
		
		fileLink.searchHandling(searchedIncTasks, searchedIncEvents, searchedCompTasks, searchedCompEvents);
		
	}
	
	/**
	 * searches incomplete tasks for a specific due date
	 * @author leon
	 * @param searchInput
	 * @param fileLink
	 * @param date 
	 * @param type 
	 * @return
	 */
	private ArrayList<TaskCard> searchTasksByDate(String searchInput,
			FileLinker fileLink, Date date, int type) {
		ArrayList<TaskCard> searchedTasks = new ArrayList<TaskCard>();
		ArrayList<TaskCard> listToBeSearched = new ArrayList<TaskCard>();
		
		if(type == TYPE_INC_TASKS) {
			listToBeSearched = fileLink.getIncompleteTasks();
		} else {
			listToBeSearched = fileLink.getCompletedTasks();
		}
		
		for(int i = 0; i < listToBeSearched.size(); i++) {
			TaskCard task = listToBeSearched.get(i);
			Calendar dueDate = task.getEndDay();
			
			if(dueDate.get(Calendar.DATE) == date.getDate() && dueDate.get(Calendar.MONTH) == date.getMonth()
					&& dueDate.get(Calendar.YEAR) == date.getYear()) {
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
	 * @param date 
	 * @param type 
	 * @return
	 */
	private ArrayList<TaskCard> searchEventsByDate(String searchInput,
			FileLinker fileLink, Date date, int type) {
		ArrayList<TaskCard> searchedEvents = new ArrayList<TaskCard>();
		ArrayList<TaskCard> listToBeSearched = new ArrayList<TaskCard>();
		
		if(type == TYPE_INC_EVENTS) {
			listToBeSearched = fileLink.getIncompleteEvents();
		} else {
			listToBeSearched = fileLink.getCompletedEvents();
		}
		
		for(int i = 0; i < listToBeSearched.size(); i++) {
			TaskCard event = listToBeSearched.get(i);
			Calendar searchedDateStart = GregorianCalendar.getInstance();
			Calendar searchedDateEnd = getEndRange(date);
			searchedDateStart.setTime(date);

			if(event.getStartDay().after(searchedDateStart) && event.getEndDay().before(searchedDateEnd)) {
				searchedEvents.add(event);
			} else if(event.getStartDay().before(searchedDateStart) && event.getEndDay().after(searchedDateStart)) {
				searchedEvents.add(event);
			} else if(event.getStartDay().equals(searchedDateStart) || event.getEndDay().equals(searchedDateStart)) {
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
	 * @param type 
	 * @return
	 */
	private ArrayList<TaskCard> searchByUserWord(String searchInput, FileLinker fileLink, int type) {
		ArrayList<TaskCard> searchedList = new ArrayList<TaskCard>();
		ArrayList<TaskCard> listToBeSearched = new ArrayList<TaskCard>();
		
		if(type == TYPE_INC_TASKS) {
			listToBeSearched = fileLink.getIncompleteTasks();
		} else if(type == TYPE_INC_EVENTS) {
			listToBeSearched = fileLink.getIncompleteEvents();
		} else if(type == TYPE_COMP_TASKS) {
			listToBeSearched = fileLink.getCompletedTasks();
		} else {
			listToBeSearched = fileLink.getCompletedEvents();
		}
		
		for(int i = 0; i < listToBeSearched.size(); i++) {
			TaskCard task = listToBeSearched.get(i);
			String taskDetails = task.getName().toLowerCase();
			searchInput = searchInput.toLowerCase();
			
			if(taskDetails.contains(searchInput)) {
				searchedList.add(task);
			}
		}
		
		return searchedList;
	}
	
	private Calendar getEndRange(Date date) {
		Calendar endRange = GregorianCalendar.getInstance();
		endRange.setTime(date);
		endRange.set(GregorianCalendar.HOUR_OF_DAY, 23);
		endRange.set(GregorianCalendar.MINUTE, 59);
		endRange.set(GregorianCalendar.SECOND, 59);
		endRange.set(GregorianCalendar.MILLISECOND, 999);
		
		return endRange;
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

	private Date checkIsDate(String searchInput, DateAndTimeFormats dateFormats) {
		Date date = null;
		
		if(dateFormats.isLazyDate(searchInput) != null) {
			date = dateFormats.isLazyDate(searchInput);
		} else if(dateFormats.isLazyYearDate(searchInput) != null) {
			date = dateFormats.isLazyYearDate(searchInput);
		} else if(dateFormats.isProperDate(searchInput) != null) {
			date = dateFormats.isProperDate(searchInput);
		}
		
		return date;
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
