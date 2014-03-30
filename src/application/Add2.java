package application;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Add2 {
	
	
	private int ARRAY_FIRST_ARG = 0;
	private int ARRAY_SECOND_ARG = 1;
	
	private final String TYPE_DUE_DATE_TASK = "T";
	private final String TYPE_FLOATING_TASK = "FT";
	
	private final int DEFAULT_FLOATING_TASKS_PRIORITY = 1;
	private final int DEFAULT_TASKS_AND_EVENTS_PRIORITY = 2;
	private final int URGENT_PRIORITY = 3;
	
	private final int ADD_TO_TASKS = 1;
	private final int ADD_TO_EVENTS = 2;
	
	private Calendar floatingDefaultEndDay = new GregorianCalendar(9999, 11, 31, 23, 59);
	
	private final String FEEDBACK_NO_ARG_ENTERED = "Please enter a task/event to be added!";
	private final String FEEDBACK_EXTRA_DETAILS_ARG = "You've entered something extra :(  Please re-enter!";
	private final String FEEDBACK_SUCCESSFUL_ADD_TASK = "Task added!";
	private final String FEEDBACK_SUCCESSFUL_ADD_EVENT = "Event added!";
	private final String FEEDBACK_INVALID_DATE_FORMAT = "You've entered an invalid date format :( Please re-enter!";
	
	private boolean urgent_flag;
	
	public Add2() {
		initVar();
	}
	
	public boolean executeAdd(String userInput, FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success = false;
		
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		if(tokenizedInput[ARRAY_FIRST_ARG].equals("addu")) {
			urgent_flag = true;
		}
		
		if(tokenizedInput.length < 2) {
			dataUI.setFeedback(FEEDBACK_NO_ARG_ENTERED);
			return false;
		} else {
			String userDetails = tokenizedInput[ARRAY_SECOND_ARG];
			success = identifyTypeAndPerform(userDetails, fileLink, dataUI, undoHandler, dateFormats);
		}
		
		initVar();
		return success;
	}
	
	private boolean identifyTypeAndPerform(String userDetails,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success;
		
		String[] details = userDetails.trim().split(";| at ");
		
		if(details.length == 1) {
			success = addTask(details, fileLink, dataUI, undoHandler, dateFormats);
		} else if(details.length == 2) {
			success = addEvent(details, fileLink, dataUI, undoHandler, dateFormats);
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG);
			return false;
		}
		
		return success;
	}
	
	private boolean addTask(String[] details, FileLinker fileLink, DataUI dataUI,
	    Undo undoHandler, DateAndTimeFormats dateFormats) {
	  boolean success;
		
		String[] detailsAndTime = details[ARRAY_FIRST_ARG].trim().split(" due by | due on | to be done by ");
		if(detailsAndTime.length == 1) {
			success = addFloatingTask(detailsAndTime[ARRAY_FIRST_ARG], fileLink, dataUI, undoHandler);
		} else if(detailsAndTime.length == 2) {
			success = addDueDateTask(detailsAndTime, fileLink, dataUI, undoHandler, dateFormats);
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG);
			return false;
		}
		
	  return success;
	}

	private boolean addDueDateTask(String[] detailsAndTime, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
	  boolean success = true;
		TaskCard taskToBeAdded = new TaskCard();
	  Date date;
	  
		String details = detailsAndTime[ARRAY_FIRST_ARG];
		String[] dateAndTime = detailsAndTime[ARRAY_SECOND_ARG].trim().split(",");
		
		if(dateAndTime.length == 1 || dateAndTime.length == 2) {
			date = checkAndGetDate(dateAndTime, dateFormats);
			
			if(date == null) {
				dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
				return false;
			}
			
			setDueDateTaskDetails(taskToBeAdded, date, details);
			dataUI.setFeedback(FEEDBACK_SUCCESSFUL_ADD_TASK);
			fileLink.addHandling(taskToBeAdded, ADD_TO_TASKS);
			RefreshUI.executeRefresh(fileLink, dataUI);
			undoHandler.storeUndo("add", ADD_TO_TASKS, taskToBeAdded, null);
			
			
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG);
			return false;
		}

	  return success;
  }

	private boolean addFloatingTask(String taskDetails, FileLinker fileLink,
	    DataUI dataUI, Undo undoHandler) {
	  TaskCard taskToBeAdded = new TaskCard();
	  
	  setFloatingTaskDetails(taskDetails, taskToBeAdded);
	  dataUI.setFeedback(FEEDBACK_SUCCESSFUL_ADD_TASK);
	  fileLink.addHandling(taskToBeAdded, ADD_TO_TASKS);
	  RefreshUI.executeRefresh(fileLink, dataUI);
	  undoHandler.storeUndo("add", ADD_TO_TASKS, taskToBeAdded, null);
	  
	  return true;
	}

	private void setDueDateTaskDetails(TaskCard taskToBeAdded, Date date,
      String details) {
	  Calendar startDay = GregorianCalendar.getInstance();
	  Calendar endDay = GregorianCalendar.getInstance();
	  endDay.setTime(date);
	  
	  if(urgent_flag) {
	  	taskToBeAdded.setPriority(URGENT_PRIORITY);
	  } else {
	  	taskToBeAdded.setPriority(DEFAULT_TASKS_AND_EVENTS_PRIORITY);
	  }
	  taskToBeAdded.setName(details);
	  taskToBeAdded.setType(TYPE_DUE_DATE_TASK);
	  taskToBeAdded.setStartDay(startDay);
	  taskToBeAdded.setEndDay(endDay);
  }

	private Date checkAndGetDate(String[] dateAndTime, DateAndTimeFormats dateFormats) {
	  if(dateAndTime.length == 1) {
	  	String toBeIdentified = dateAndTime[ARRAY_FIRST_ARG];
	  	Date time = null;
	  	Date date = null;
	  	
	  	if(dateFormats.isHourOnly(toBeIdentified) != null) {
	  		time = dateFormats.isHourOnly(toBeIdentified);
	  	} else if(dateFormats.isComplete12Hr(toBeIdentified) != null) {
	  		time = dateFormats.isComplete12Hr(toBeIdentified);
	  	} else if(dateFormats.isComplete24Hr(toBeIdentified) != null) {
	  		time = dateFormats.isComplete24Hr(toBeIdentified);
	  	}
	  	
	  	if(dateFormats.isProperDate(toBeIdentified) != null) {
	  		date = dateFormats.isProperDate(toBeIdentified);
	  	} else if(dateFormats.isLazyYearDate(toBeIdentified) != null) {
	  		date = dateFormats.isLazyYearDate(toBeIdentified);
	  	} else if(dateFormats.isLazyDate(toBeIdentified) != null) {
	  		date = dateFormats.isLazyDate(toBeIdentified);
	  	}
	  	
	  	if(time != null && date != null) {
	  		return null;
	  	} else if(time == null && date == null) {
	  		return null;
	  	} else if(time != null) {
	  		return time;
	  	} else {
	  		return date;
	  	}
	  } else {
	  	String dateComponent = dateAndTime[ARRAY_FIRST_ARG];
	  	String timeComponent = dateAndTime[ARRAY_SECOND_ARG];
	  	Date time = null;
	  	Date date = null;
	  	
	  	if(dateFormats.isHourOnly(timeComponent) != null) {
	  		time = dateFormats.isHourOnly(timeComponent);
	  	} else if(dateFormats.isComplete12Hr(timeComponent) != null) {
	  		time = dateFormats.isComplete12Hr(timeComponent);
	  	} else if(dateFormats.isComplete24Hr(timeComponent) != null) {
	  		time = dateFormats.isComplete24Hr(timeComponent);
	  	}
	  	
	  	if(dateFormats.isProperDate(dateComponent) != null) {
	  		date = dateFormats.isProperDate(dateComponent);
	  	} else if(dateFormats.isLazyYearDate(dateComponent) != null) {
	  		date = dateFormats.isLazyYearDate(dateComponent);
	  	} else if(dateFormats.isLazyDate(dateComponent) != null) {
	  		date = dateFormats.isLazyDate(dateComponent);
	  	}
	  	
	  	if(time == null || date == null) {
	  		return null;
	  	}
	  	
	  	Calendar cal = GregorianCalendar.getInstance();
	  	cal.setTime(date);
	  	cal.set(Calendar.HOUR_OF_DAY, time.getHours());
	  	cal.set(Calendar.MINUTE, time.getMinutes());
	  	cal.set(Calendar.SECOND, 0);
	  	cal.set(Calendar.MILLISECOND, 0);
	  	
	  	return cal.getTime();
	  }
  }

	private void setFloatingTaskDetails(String taskDetails, TaskCard taskToBeAdded) {
	  taskToBeAdded.setName(taskDetails);
	  taskToBeAdded.setPriority(DEFAULT_FLOATING_TASKS_PRIORITY);
	  taskToBeAdded.setType(TYPE_FLOATING_TASK);
	  taskToBeAdded.setStartDay(GregorianCalendar.getInstance());
	  taskToBeAdded.setEndDay(floatingDefaultEndDay);
  }

	private boolean addEvent(String[] details, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
	  // TODO Auto-generated method stub
	  return false;
  }

	private void initVar() {
		urgent_flag = false;
	}
}
