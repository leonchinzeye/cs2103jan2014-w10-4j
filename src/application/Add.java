//@author A0097304E
package application;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Add {

	private int ARRAY_FIRST_ARG = 0;
	private int ARRAY_SECOND_ARG = 1;
	
	private final String TYPE_DUE_DATE_TASK = "T";
	private final String TYPE_FLOATING_TASK = "FT";
	private final String TYPE_ALL_DAY = "AE";
	private final String TYPE_EVENT = "E";
	
	private final int DEFAULT_FLOATING_TASKS_PRIORITY = 1;
	private final int DEFAULT_TASKS_AND_EVENTS_PRIORITY = 2;
	private final int URGENT_PRIORITY = 3;
	
	private final int ADD_TO_TASKS = 1;
	private final int ADD_TO_EVENTS = 2;
	
	private boolean urgent_flag;
	private HashMap<String, Integer> cmdTable;
	
	private Calendar floatingDefaultEndDay = new GregorianCalendar(9999, 11, 31, 23, 59);
	
	private static final String FEEDBACK_NO_ARG_ENTERED = "You forgot to enter a task/event to be added!";
	private static final String FEEDBACK_EXTRA_DETAILS_ARG = "You seem to have entered more than you need to :(  Please try again!";
	private static final String FEEDBACK_EXTRA_DETAILS_ARG_TASK = "You seem to have entered more than you need to :(  Please try again!";
	private static final String FEEDBACK_SUCCESSFUL_ADD_TASK = "Task has been successfully added!";
	private static final String FEEDBACK_SUCCESSFUL_ADD_EVENT = "Event has been successfully added!";
	private static final String FEEDBACK_INVALID_ADD_COMMAND = "You've entered an invalid add command :(";
	private static final String FEEDBACK_INVALID_DATE_FORMAT = "You've entered an invalid date format :(";
	private static final String FEEDBACK_NO_TIME_SPECIFIED_FOR_EVENT = "You didn't enter a timing for this event :( Please try again!";
	private static final String FEEDBACK_GOING_BACK_INTO_TIME = "Greaaat scott! Are you a time traveller?";
	
	/**
	 * constructor for add
	 */
	public Add() {
		initCmdTable();
		resetFlag();
	}
	
	/**
	 * this is the main method that is called by any external class in order to add tasks or
	 * events into TaskWorthy. It takes in the user input and does the parsing to determine
	 * if it is a task or an event
	 * @return
	 * this method returns a boolean to any external method calling it to indicate if 
	 * adding of a task/event has been successful
	 */
	public void executeAdd(String userInput, FileLinker fileLink, 
			DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success = false;
		
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		if(!cmdTable.containsKey(tokenizedInput[ARRAY_FIRST_ARG])) {
			dataUI.setFeedback(FEEDBACK_INVALID_ADD_COMMAND);
			return;
		}
		
		if(cmdTable.get(tokenizedInput[ARRAY_FIRST_ARG]) == 2) {
			urgent_flag = true;
		}
		
		if(tokenizedInput.length < 2) {
			dataUI.setFeedback(FEEDBACK_NO_ARG_ENTERED);
			return;
		} else {
			String userDetails = tokenizedInput[ARRAY_SECOND_ARG];
			success = identifyTypeAndPerform(userDetails, fileLink, dataUI, undoHandler, dateFormats);
		}
		
		if(success) {
			undoHandler.flushRedo();
		}
		
		resetFlag();
		return;
	}
	
	/**
	 * this method identifies if the adding is of a task or event type and calls the respective methods
	 * to add them
	 * @return
	 * returns a successful boolean if the adding was successful
	 */
	private boolean identifyTypeAndPerform(String userDetails,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success;
		
		String[] details = userDetails.trim().split(";");
		
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
	
	/**
	 * this method takes in the task details and determines if it is a floating task or a task that
	 * has a due date.
	 * @return
	 * returns true if the addition has been successful
	 */
	private boolean addTask(String[] details, FileLinker fileLink, DataUI dataUI,
			Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success;
		
		String[] detailsAndTime = details[ARRAY_FIRST_ARG].trim().split(" due by | due on | to be done by | by | due at ");
		
		if(detailsAndTime.length == 1) {
			success = addFloatingTask(detailsAndTime[ARRAY_FIRST_ARG], fileLink, dataUI, undoHandler);
		} else if(detailsAndTime.length == 2) {
			success = addDueDateTask(detailsAndTime, fileLink, dataUI, undoHandler, dateFormats);
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG_TASK);
			return false;
		}
		
		return success;
	}
	
	/**
	 * this method is mainly for adding events. it takes in the user timings and determines if
	 * the user has entered a single timing or 2 timings which will determine the start and end
	 * time of the event after which it calls the respective methods to add the event
	 * @return
	 * returns true if the user has entered the details correctly and that the event has been
	 * added into the storage
	 */
	private boolean addEvent(String[] details, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success;
		
		String eventName = details[ARRAY_FIRST_ARG].trim();
		String[] timeComponents = details[ARRAY_SECOND_ARG].trim().split("-| to ");
		
		if(timeComponents.length == 1) {
			success = addOneTimingEvent(eventName, timeComponents, fileLink, dataUI, undoHandler, dateFormats);
		} else if(timeComponents.length == 2) {
			success = addTwoTimingEvent(eventName, timeComponents, fileLink, dataUI, undoHandler, dateFormats);
		} else {
			dataUI.setFeedback(FEEDBACK_NO_TIME_SPECIFIED_FOR_EVENT);
			return false;
		}
		
		return success;
	}
	
	/**
	 * This method takes in the details and the time component and checks if the user has entered a
	 * valid date/time format. If it is correct, it parses the user input into a date format and stores
	 * it in storage.
	 * @return
	 * returns true if the user enters a valid date format that can be parsed. Task will then be added
	 * into storage.
	 * returns false if the user enters an invalid date format
	 */
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
			
			int indexAdded = fileLink.getIncompleteTasks().indexOf(taskToBeAdded);
			dataUI.setRowAdded(indexAdded);
			dataUI.setFileAdded(ADD_TO_TASKS);
			
			RefreshUI.executeRefresh(fileLink, dataUI);
			undoHandler.storeUndo("add", ADD_TO_TASKS, taskToBeAdded, null);
			
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG_TASK);
			return false;
		}
		return success;
	}
	
	/**
	 * this method takes in the user input and enters it as a floating task.
	 * Floating tasks do not have an end timing and thus, their default timing
	 * is set to 31 Dec 9999 of the calendar.
	 * @return
	 * floating tasks will always be true and added if the user ignores any command
	 * syntax when adding details into TaskWorthy
	 */
	private boolean addFloatingTask(String taskDetails, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler) {
		TaskCard taskToBeAdded = new TaskCard();
		
		setFloatingTaskDetails(taskDetails, taskToBeAdded);
		dataUI.setFeedback(FEEDBACK_SUCCESSFUL_ADD_TASK);
		fileLink.addHandling(taskToBeAdded, ADD_TO_TASKS);
		
		int indexAdded = fileLink.getIncompleteTasks().indexOf(taskToBeAdded);
		dataUI.setRowAdded(indexAdded);
		dataUI.setFileAdded(ADD_TO_TASKS);
		
		RefreshUI.executeRefresh(fileLink, dataUI);
		undoHandler.storeUndo("add", ADD_TO_TASKS, taskToBeAdded, null);
		
		return true;
	}
	
	/**
	 * this method is for when the user only enters one timing for events. the entered 
	 * event will be given a default 1 hr period block
	 * @return
	 * returns true if the date format is correct and event has been added into the database
	 * returns false if the date format user entered is invalid
	 */
	private boolean addOneTimingEvent(String eventName, String[] timeComponents,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler,
			DateAndTimeFormats dateFormats) {
		boolean success = true;
		TaskCard eventToBeAdded = new TaskCard();
		Date startDate;
		
		String[] dateAndTime = timeComponents[ARRAY_FIRST_ARG].trim().split(",");
		
		if(dateAndTime.length == 1 || dateAndTime.length == 2) {
			startDate = checkAndGetDate(dateAndTime, dateFormats);
			
			if(startDate == null) {
				dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
				return false;
			}
			
			setOneTimingEventDetails(eventToBeAdded, startDate, eventName);
			dataUI.setFeedback(FEEDBACK_SUCCESSFUL_ADD_EVENT);
			fileLink.addHandling(eventToBeAdded, ADD_TO_EVENTS);
			
			int indexAdded = fileLink.getIncompleteEvents().indexOf(eventToBeAdded);
			dataUI.setRowAdded(indexAdded);
			dataUI.setFileAdded(ADD_TO_EVENTS);
			
			RefreshUI.executeRefresh(fileLink, dataUI);
			undoHandler.storeUndo("add", ADD_TO_EVENTS, eventToBeAdded, null);
			
		} else {
			dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
			return false;
		}
		return success;
	}

	/**
	 * this method is for adding events when the user has entered a specific start and end time for 
	 * the event. Because TaskWorthy offers some flexibility in adding the date formats, there are
	 * several cases to identify with regards to the timing that the user enters
	 * @return
	 * returns true if the formats have been correct and the event has been added into the database
	 * returns false if the syntax or the dates that the user entered is invalid
	 */
	private boolean addTwoTimingEvent(String eventName, String[] timeComponents,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler,
			DateAndTimeFormats dateFormats) {
		boolean success = true;
		TaskCard eventToBeAdded = new TaskCard();
		Date startDate;
		Date endDate;
		
		String[] dateAndTimeStart = timeComponents[ARRAY_FIRST_ARG].trim().split(",");
		String[] dateAndTimeEnd = timeComponents[ARRAY_SECOND_ARG].trim().split(",");
		
		if(dateAndTimeStart.length > 2 || dateAndTimeEnd.length > 2 || dateAndTimeStart.length <= 0 || dateAndTimeEnd.length <= 0) {
			dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
			return false;
		} else {
			startDate = checkAndGetDate(dateAndTimeStart, dateFormats);
			endDate = checkAndGetDate(dateAndTimeEnd, dateFormats);
			
			if(startDate == null || endDate == null) {
				dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
				return false;
			}
			
			/**
			 * 1st if: could be a time and a time(event falls within a 
			 * 				 specific timing on current date) or a date and a 
			 * 				 date(event stretches over multiple days)
			 * 2nd if: a full date with a time at the end. ending on the same day
			 * 3rd if: event stretches over multiple days with specific timings
			 */
			if(dateAndTimeStart.length == 1 && dateAndTimeEnd.length == 1) {	
				Calendar today = GregorianCalendar.getInstance();
				Calendar startDay = GregorianCalendar.getInstance();
				Calendar endDay = GregorianCalendar.getInstance();
				startDay.setTime(startDate);
				endDay.setTime(endDate);
				
				if(checkToday(today, endDay) && checkToday(today, startDay) == false) {
					dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
					return false;
				}
				setCurrentDateTwoTimingEventDetails(eventToBeAdded, startDate, endDate, eventName);
			} else if(dateAndTimeStart.length == 2 && dateAndTimeEnd.length == 1) {
				setDateEnteredTwoTimingEventDetails(eventToBeAdded, startDate, endDate, eventName);
			} else if(dateAndTimeStart.length == 2 && dateAndTimeEnd.length == 2) {
				setMultDaysEnteredTwoTimingEventDetails(eventToBeAdded, startDate, endDate, eventName);
			} else if(dateAndTimeStart.length == 1 && dateAndTimeEnd.length == 2) {
				setMultDaysEnteredTwoTimingEventDetails(eventToBeAdded, startDate, endDate, eventName);
			}
			
			if(!checkValidityOfEvent(eventToBeAdded)) {
				dataUI.setFeedback(FEEDBACK_GOING_BACK_INTO_TIME);
				return false;
			}
			
			dataUI.setFeedback(FEEDBACK_SUCCESSFUL_ADD_EVENT);
			fileLink.addHandling(eventToBeAdded, ADD_TO_EVENTS);
			
			int indexAdded = fileLink.getIncompleteEvents().indexOf(eventToBeAdded);
			dataUI.setRowAdded(indexAdded);
			dataUI.setFileAdded(ADD_TO_EVENTS);
			
			RefreshUI.executeRefresh(fileLink, dataUI);
			undoHandler.storeUndo("add", ADD_TO_EVENTS, eventToBeAdded, null);
		}
		
		return success;
	}

	/**
	 * this method checks if the date entered corresponds to today's date
	 * @return
	 * returns true if the date is today
	 * returns false if the date is some other date
	 */
	private boolean checkToday(Calendar today, Calendar endDay) {
	  return endDay.get(Calendar.DATE) == today.get(Calendar.DATE)
	  		&& endDay.get(Calendar.MONTH) == today.get(Calendar.MONTH) 
	  		&& endDay.get(Calendar.YEAR) == today.get(Calendar.YEAR);
  }

	/**
	 * this method is for setting the event details and timings that the user has entered
	 * into a TaskCard format. In this case, the user has added a timing that could be
	 * on the current day he enters, or it could also be an event that spans over a couple of
	 * days without a specific timing, or from a specific timing to an end date
	 */
	private void setCurrentDateTwoTimingEventDetails(TaskCard eventToBeAdded,
			Date startDate, Date endDate, String eventName) {
		eventToBeAdded.setName(eventName);
		if(urgent_flag) {
			eventToBeAdded.setPriority(URGENT_PRIORITY);
		} else {
			eventToBeAdded.setPriority(DEFAULT_TASKS_AND_EVENTS_PRIORITY);
		}
		
		Calendar startDay = GregorianCalendar.getInstance();
		Calendar endDay = GregorianCalendar.getInstance();
		startDay.setTime(startDate);
		endDay.setTime(endDate);
		
		eventToBeAdded.setStartDay(startDay);
		eventToBeAdded.setEndDay(endDay);
		
		if(startDay.get(Calendar.HOUR_OF_DAY) == 0 && startDay.get(Calendar.MINUTE) == 0
				&& endDay.get(Calendar.HOUR_OF_DAY) == 0 && endDay.get(Calendar.MINUTE) == 0 
				&& startDay != endDay) {
			eventToBeAdded.setType(TYPE_ALL_DAY);
		} else {
			eventToBeAdded.setType(TYPE_EVENT);
		}
	}

	/**
	 * this method is called when the user enters a specific timing on a specific date for a starting
	 * date and either a specific date or a specific timing. it then sets the timing details along
	 * with the event type
	 */
	private void setDateEnteredTwoTimingEventDetails(TaskCard eventToBeAdded,
			Date startDate, Date endDate, String eventName) {
		eventToBeAdded.setName(eventName);
		if(urgent_flag) {
			eventToBeAdded.setPriority(URGENT_PRIORITY);
		} else {
			eventToBeAdded.setPriority(DEFAULT_TASKS_AND_EVENTS_PRIORITY);
		}	
		
		Calendar startDay = GregorianCalendar.getInstance();
		Calendar endDay = GregorianCalendar.getInstance();
		Calendar today = GregorianCalendar.getInstance();
		startDay.setTime(startDate);
		endDay.setTime(endDate);
		
		if(endDay.get(Calendar.DATE) == today.get(Calendar.DATE) 
				&& endDay.get(Calendar.MONTH) == today.get(Calendar.MONTH)
				&& endDay.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
			endDay.set(Calendar.DATE, startDay.get(Calendar.DATE));
			endDay.set(Calendar.MONTH, startDay.get(Calendar.MONTH));
			endDay.set(Calendar.YEAR, startDay.get(Calendar.YEAR));
		}
		
		eventToBeAdded.setStartDay(startDay);
		eventToBeAdded.setEndDay(endDay);
		
		eventToBeAdded.setType(TYPE_EVENT);
	}

	/**
	 * this method is called when the user is specific with the 2 timings that he has
	 * entered. Eg. 23/4, 4pm to 28/4, 4pm
	 * It then sets the details for the events in the format of a TaskCard that is to be
	 * added into the database
	 */
	private void setMultDaysEnteredTwoTimingEventDetails(TaskCard eventToBeAdded,
			Date startDate, Date endDate, String eventName) {
		eventToBeAdded.setName(eventName);
		if(urgent_flag) {
			eventToBeAdded.setPriority(URGENT_PRIORITY);
		} else {
			eventToBeAdded.setPriority(DEFAULT_TASKS_AND_EVENTS_PRIORITY);
		}	
		
		Calendar startDay = GregorianCalendar.getInstance();
		Calendar endDay = GregorianCalendar.getInstance();
		startDay.setTime(startDate);
		endDay.setTime(endDate);
	
		eventToBeAdded.setStartDay(startDay);
		eventToBeAdded.setEndDay(endDay);
		
		eventToBeAdded.setType(TYPE_EVENT);
	}

	/**
	 * this method sets the details for when only the one timing has been entered by the
	 * user. this is where the default of 1 hour is given because the user has only 
	 * entered the start timing
	 */
	private void setOneTimingEventDetails(TaskCard eventToBeAdded,
			Date startDate, String eventName) {
		eventToBeAdded.setName(eventName);
		
		if(urgent_flag) {
			eventToBeAdded.setPriority(URGENT_PRIORITY);
		} else {
			eventToBeAdded.setPriority(DEFAULT_TASKS_AND_EVENTS_PRIORITY);
		}
		
		Calendar start = GregorianCalendar.getInstance();
		Calendar end = GregorianCalendar.getInstance();
		
		start.setTime(startDate);
		eventToBeAdded.setStartDay(start);
		
		if(start.get(Calendar.HOUR_OF_DAY) == 0 && start.get(Calendar.MINUTE) == 0) {
			eventToBeAdded.setType(TYPE_ALL_DAY);
			end.setTime(startDate);
			end.add(Calendar.DATE, 1);
			end.add(Calendar.MILLISECOND, -1);
			
			eventToBeAdded.setEndDay(end);
		} else {
			eventToBeAdded.setType(TYPE_EVENT);
			end.setTime(startDate);
			end.add(Calendar.HOUR_OF_DAY, 1);
			
			eventToBeAdded.setEndDay(end);
		}
	}
	
	/**
	 * this method is for setting the details of a due date task into a TaskCard format
	 */
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
	
	/**
	 * this method sets the details of a floating task into a TaskCard format that will
	 * be added into the database
	 */
	private void setFloatingTaskDetails(String taskDetails, TaskCard taskToBeAdded) {
		taskToBeAdded.setName(taskDetails);
		
		if(urgent_flag) {
			taskToBeAdded.setPriority(URGENT_PRIORITY);
		} else {
			taskToBeAdded.setPriority(DEFAULT_FLOATING_TASKS_PRIORITY);
		}
		taskToBeAdded.setType(TYPE_FLOATING_TASK);
		taskToBeAdded.setStartDay(GregorianCalendar.getInstance());
		taskToBeAdded.setEndDay(floatingDefaultEndDay);
	}
	
	/**
	 * this entire method checks for the validity of the date that the user has entered.
	 * @return
	 * returns a date that the user has entered if he/she has entered it in a valid format 
	 * and that it was parsable
	 * returns null if the format was not valid for a date
	 */
	@SuppressWarnings("deprecation")
  private Date checkAndGetDate(String[] dateAndTime, DateAndTimeFormats dateFormats) {
		if(dateAndTime.length == 1) {
			String toBeIdentified = dateAndTime[ARRAY_FIRST_ARG].trim();
			Date time = null;
			Date date = null;
			
			if(dateFormats.isHourOnly(toBeIdentified) != null) {
				time = dateFormats.isHourOnly(toBeIdentified);
			} else if(dateFormats.isComplete12Hr(toBeIdentified) != null) {
				time = dateFormats.isComplete12Hr(toBeIdentified);
			} else if(dateFormats.isComplete24Hr(toBeIdentified) != null) {
				time = dateFormats.isComplete24Hr(toBeIdentified);
			}
			
			if(dateFormats.isLazyYearDate(toBeIdentified) != null) {
				date = dateFormats.isLazyYearDate(toBeIdentified);
			} else if(dateFormats.isProperDate(toBeIdentified) != null) {
				date = dateFormats.isProperDate(toBeIdentified); 
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
			String dateComponent = dateAndTime[ARRAY_FIRST_ARG].trim();
			String timeComponent = dateAndTime[ARRAY_SECOND_ARG].trim();
			Date time = null;
			Date date = null;
			
			if(dateFormats.isHourOnly(timeComponent) != null) {
				time = dateFormats.isHourOnly(timeComponent);
			} else if(dateFormats.isComplete12Hr(timeComponent) != null) {
				time = dateFormats.isComplete12Hr(timeComponent);
			} else if(dateFormats.isComplete24Hr(timeComponent) != null) {
				time = dateFormats.isComplete24Hr(timeComponent);
			}
			
			if(dateFormats.isLazyYearDate(dateComponent) != null) {
				date = dateFormats.isLazyYearDate(dateComponent);
			} else if(dateFormats.isProperDate(dateComponent) != null) {
				date = dateFormats.isProperDate(dateComponent); 
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
	
	/**
	 * this method checks if the user has entered an ending time that takes place
	 * before the starting time that he has entered.
	 * @return
	 * returns false if the timing is not a valid timing and that he's intending to go back
	 * into the past
	 * returns true if the timing is a valid start and end time
	 */
	private boolean checkValidityOfEvent(TaskCard eventToBeAdded) {
		Calendar start = eventToBeAdded.getStartDay();
		Calendar end = eventToBeAdded.getEndDay();
		
		if(end.before(start)) {
			return false;
		}
		
	  return true;
	}

	/**
	 * this method resets the urgent flag which is used when the user wants to add an urgent task
	 */
	private void resetFlag() {
		urgent_flag = false;
	}
	
	/**
	 * initialises the commands it detects for when it is an urgent task or a default priority
	 * task
	 */
	private void initCmdTable() {
		cmdTable = new HashMap<String, Integer>();
		cmdTable.put("add", 1);
		cmdTable.put("addu", 2);
	}
}
