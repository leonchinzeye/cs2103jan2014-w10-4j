
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * this class contains the add functionalities
 * @author leon
 *
 */
public class Add {

	private static final int NUMBER_MILISECOND_ZERO = 0;
	private static final int NUMBER_SECOND_ZERO = 0;
	private static final int NUMBER_MINUTE_ZERO = 0;
	private static final int NUMBER_HOUR_OF_DAY_ZERO = 0;
	private static final int NUMBER_DATE_TIME_END_TWO = 2;
	private static final int NUMBER_DATE_TIME_START_TWO = 2;
	private static final int NUMBER_DATE_TIME_END_ONE = 1;
	private static final int NUMBER_DATE_TIME_START_ONE = 1;
	private static final int NUMBER_DATE_TIME_END_ZERO = 0;
	private static final int NUMBER_DATE_TIME_START_ZERO = 0;
	private static final int NUMBER_DATE_TIME_END = 2;
	private static final int NUMBER_DATE_TIME_START = 2;
	private static final int NUMBER_DATE_TIME_TWO = 2;
	private static final int NUMBER_DATE_TIME_ONE = 1;
	private static final int NUMBER_CHECK_FOR_TWO_TIMINGS = 2;
	private static final int NUMBER_CHECK_FOR_ONE_TIMING = 1;
	private static final int NUMBER_CHECK_FOR_DUE_DATE = 2;
	private static final int NUMBER_CHECK_FOR_FLOATING = 1;
	private static final int NUMBER_CHECK_LENGTH_FOR_EVENT = 2;
	private static final int NUMBER_CHECK_LENGTH_FOR_TASK = 1;
	private static final int NUMBER_CHECK_INVALID_TOKEN_LENGTH = 2;
	private static final int NUMBER_CHECK_ADDU_COMMAND = 2;
	private static final int NUMBER_SPLIT_BY_TWO = 2;
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
	
	private Calendar floatingDefaultEndDay = new GregorianCalendar(9999, 11, 31, 23, 59);
	
	private final String FEEDBACK_NO_ARG_ENTERED = "You forgot to enter a task/event to be added!";
	private final String FEEDBACK_EXTRA_DETAILS_ARG = "You seem to have entered more than you need to :(  Please try again!";
	private final String FEEDBACK_EXTRA_DETAILS_ARG_TASK = "You seem to have entered more than you need to :(  Please try again!";
	private final String FEEDBACK_SUCCESSFUL_ADD_TASK = "\"%s\" has been successfully added!";
	private final String FEEDBACK_SUCCESSFUL_ADD_EVENT = "\"%s\" has been successfully added!";
	private final String FEEDBACK_INVALID_ADD_COMMAND = "You've entered an invalid add command :(";
	private final String FEEDBACK_INVALID_DATE_FORMAT = "You've entered an invalid date format :(";
	private final String FEEDBACK_NO_TIME_SPECIFIED_FOR_EVENT = "You didn't enter a timing for this event :( Please try again!";
	private final String FEEDBACK_GOING_BACK_INTO_TIME = "Greaaat Scott! Are you a time traveller?";
	
	private HashMap<String, Integer> cmdTable;
	
	private boolean urgent_flag;
	
	public Add() {
		initCmdTable();
		resetFlag();
	}
	
	public boolean executeAdd(String userInput, FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success = false;
		
		String[] tokenizedInput = userInput.trim().split("\\s+", NUMBER_SPLIT_BY_TWO);
		
		if(!cmdTable.containsKey(tokenizedInput[ARRAY_FIRST_ARG])) {
			dataUI.setFeedback(FEEDBACK_INVALID_ADD_COMMAND);
			return false;
		}
		
		if(cmdTable.get(tokenizedInput[ARRAY_FIRST_ARG]) == NUMBER_CHECK_ADDU_COMMAND) {
			urgent_flag = true;
		}
		
		if(tokenizedInput.length < NUMBER_CHECK_INVALID_TOKEN_LENGTH) {
			dataUI.setFeedback(FEEDBACK_NO_ARG_ENTERED);
			return false;
		} else {
			String userDetails = tokenizedInput[ARRAY_SECOND_ARG];
			success = identifyTypeAndPerform(userDetails, fileLink, dataUI, undoHandler, dateFormats);
		}
		
		if(success) {
			undoHandler.flushRedo();
		}
		
		resetFlag();
		return success;
	}
	
	private boolean identifyTypeAndPerform(String userDetails,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success;
		
		String[] details = userDetails.trim().split(";");
		
		if(details.length == NUMBER_CHECK_LENGTH_FOR_TASK) {
			success = addTask(details, fileLink, dataUI, undoHandler, dateFormats);
		} else if(details.length == NUMBER_CHECK_LENGTH_FOR_EVENT) {
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
		
		String[] detailsAndTime = details[ARRAY_FIRST_ARG].trim().split(" due by | due on | to be done by | by | due at ");
		
		if(detailsAndTime.length == NUMBER_CHECK_FOR_FLOATING) {
			success = addFloatingTask(detailsAndTime[ARRAY_FIRST_ARG], fileLink, dataUI, undoHandler);
		} else if(detailsAndTime.length == NUMBER_CHECK_FOR_DUE_DATE) {
			success = addDueDateTask(detailsAndTime, fileLink, dataUI, undoHandler, dateFormats);
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG_TASK);
			return false;
		}
		
		return success;
	}
	
	private boolean addEvent(String[] details, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler, DateAndTimeFormats dateFormats) {
		boolean success;
		
		String eventName = details[ARRAY_FIRST_ARG].trim();
		String[] timeComponents = details[ARRAY_SECOND_ARG].trim().split("-| to ");
		
		if(timeComponents.length == NUMBER_CHECK_FOR_ONE_TIMING) {
			success = addOneTimingEvent(eventName, timeComponents, fileLink, dataUI, undoHandler, dateFormats);
		} else if(timeComponents.length == NUMBER_CHECK_FOR_TWO_TIMINGS) {
			success = addTwoTimingEvent(eventName, timeComponents, fileLink, dataUI, undoHandler, dateFormats);
		} else {
			dataUI.setFeedback(FEEDBACK_NO_TIME_SPECIFIED_FOR_EVENT);
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
		
		if(dateAndTime.length == NUMBER_DATE_TIME_ONE || dateAndTime.length == NUMBER_DATE_TIME_TWO) {
			date = checkAndGetDate(dateAndTime, dateFormats);
			
			if(date == null) {
				dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
				return false;
			}
			
			setDueDateTaskDetails(taskToBeAdded, date, details);
			dataUI.setFeedback(String.format(FEEDBACK_SUCCESSFUL_ADD_TASK, taskToBeAdded.getName()));
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
	
	private boolean addFloatingTask(String taskDetails, FileLinker fileLink,
			DataUI dataUI, Undo undoHandler) {
		TaskCard taskToBeAdded = new TaskCard();
		
		setFloatingTaskDetails(taskDetails, taskToBeAdded);
		dataUI.setFeedback(String.format(FEEDBACK_SUCCESSFUL_ADD_TASK, taskToBeAdded.getName()));
		fileLink.addHandling(taskToBeAdded, ADD_TO_TASKS);
		
		int indexAdded = fileLink.getIncompleteTasks().indexOf(taskToBeAdded);
		dataUI.setRowAdded(indexAdded);
		dataUI.setFileAdded(ADD_TO_TASKS);
		
		RefreshUI.executeRefresh(fileLink, dataUI);
		undoHandler.storeUndo("add", ADD_TO_TASKS, taskToBeAdded, null);
		
		return true;
	}
	
	/**
	 * this method is for when the user only enters one timing. the entered event will be given a default 1 hr period
	 * @param eventName
	 * @param timeComponents
	 * @param fileLink
	 * @param dataUI
	 * @param undoHandler
	 * @param dateFormats
	 * @return
	 */
	private boolean addOneTimingEvent(String eventName, String[] timeComponents,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler,
			DateAndTimeFormats dateFormats) {
		boolean success = true;
		TaskCard eventToBeAdded = new TaskCard();
		Date startDate;
		
		String[] dateAndTime = timeComponents[ARRAY_FIRST_ARG].trim().split(",");
		
		if(dateAndTime.length == NUMBER_DATE_TIME_ONE || dateAndTime.length == NUMBER_DATE_TIME_TWO) {
			startDate = checkAndGetDate(dateAndTime, dateFormats);
			
			if(startDate == null) {
				dataUI.setFeedback(FEEDBACK_INVALID_DATE_FORMAT);
				return false;
			}
			
			setOneTimingEventDetails(eventToBeAdded, startDate, eventName);
			dataUI.setFeedback(String.format(FEEDBACK_SUCCESSFUL_ADD_EVENT, eventToBeAdded.getName()));
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

	private boolean addTwoTimingEvent(String eventName, String[] timeComponents,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler,
			DateAndTimeFormats dateFormats) {
		boolean success = true;
		TaskCard eventToBeAdded = new TaskCard();
		Date startDate;
		Date endDate;
		
		String[] dateAndTimeStart = timeComponents[ARRAY_FIRST_ARG].trim().split(",");
		String[] dateAndTimeEnd = timeComponents[ARRAY_SECOND_ARG].trim().split(",");
		
		if(dateAndTimeStart.length > NUMBER_DATE_TIME_START || dateAndTimeEnd.length > NUMBER_DATE_TIME_END || dateAndTimeStart.length <= NUMBER_DATE_TIME_START_ZERO || dateAndTimeEnd.length <= NUMBER_DATE_TIME_END_ZERO) {
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
			 * 1st if: could be a time and a time(event falls within a specific timing on current date) or a date and a date(event stretches over multiple days)
			 * 2nd if: a full date with a time at the end. ending on the same day
			 * 3rd if: event stretches over multiple days with specific timings
			 */
			if(dateAndTimeStart.length == NUMBER_DATE_TIME_START_ONE && dateAndTimeEnd.length == NUMBER_DATE_TIME_END_ONE) {	
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
			} else if(dateAndTimeStart.length == NUMBER_DATE_TIME_START_TWO && dateAndTimeEnd.length == NUMBER_DATE_TIME_END_ONE) {
				setDateEnteredTwoTimingEventDetails(eventToBeAdded, startDate, endDate, eventName);
			} else if(dateAndTimeStart.length == NUMBER_DATE_TIME_START_TWO && dateAndTimeEnd.length == NUMBER_DATE_TIME_END_TWO) {
				setMultDaysEnteredTwoTimingEventDetails(eventToBeAdded, startDate, endDate, eventName);
			} else if(dateAndTimeStart.length == NUMBER_DATE_TIME_START_ONE && dateAndTimeEnd.length == NUMBER_DATE_TIME_END_TWO) {
				setTodayToUserEndDate(eventToBeAdded, startDate, endDate, eventName);
			}
			
			if(!checkValidityOfEvent(eventToBeAdded)) {
				dataUI.setFeedback(FEEDBACK_GOING_BACK_INTO_TIME);
				return false;
			}
			
			dataUI.setFeedback(String.format(FEEDBACK_SUCCESSFUL_ADD_EVENT, eventToBeAdded.getName()));
			fileLink.addHandling(eventToBeAdded, ADD_TO_EVENTS);
			
			int indexAdded = fileLink.getIncompleteEvents().indexOf(eventToBeAdded);
			dataUI.setRowAdded(indexAdded);
			dataUI.setFileAdded(ADD_TO_EVENTS);
			
			RefreshUI.executeRefresh(fileLink, dataUI);
			undoHandler.storeUndo("add", ADD_TO_EVENTS, eventToBeAdded, null);
		}
		
		
		return success;
	}

	private boolean checkToday(Calendar today, Calendar endDay) {
	  return endDay.get(Calendar.DATE) == today.get(Calendar.DATE) && endDay.get(Calendar.MONTH) == today.get(Calendar.MONTH) 
	  		&& endDay.get(Calendar.YEAR) == today.get(Calendar.YEAR);
  }

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
		
		if(startDay.get(Calendar.HOUR_OF_DAY) == NUMBER_HOUR_OF_DAY_ZERO && startDay.get(Calendar.MINUTE) == NUMBER_MINUTE_ZERO
				&& endDay.get(Calendar.HOUR_OF_DAY) == NUMBER_HOUR_OF_DAY_ZERO && endDay.get(Calendar.MINUTE) == NUMBER_MINUTE_ZERO && startDay != endDay) {
			eventToBeAdded.setType(TYPE_ALL_DAY);
		} else {
			eventToBeAdded.setType(TYPE_EVENT);
		}
	}

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
		startDay.setTime(startDate);
		endDay.setTime(endDate);
		
		endDay.set(Calendar.DATE, startDay.get(Calendar.DATE));
		endDay.set(Calendar.MONTH, startDay.get(Calendar.MONTH));
		endDay.set(Calendar.YEAR, startDay.get(Calendar.YEAR));
		
		eventToBeAdded.setStartDay(startDay);
		eventToBeAdded.setEndDay(endDay);
		
		eventToBeAdded.setType(TYPE_EVENT);
	}

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

	private void setTodayToUserEndDate(TaskCard eventToBeAdded, Date startDate,
	    Date endDate, String eventName) {
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
		
		if(start.get(Calendar.HOUR_OF_DAY) == NUMBER_HOUR_OF_DAY_ZERO && start.get(Calendar.MINUTE) == NUMBER_MINUTE_ZERO) {
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
			cal.set(Calendar.SECOND, NUMBER_SECOND_ZERO);
			cal.set(Calendar.MILLISECOND, NUMBER_MILISECOND_ZERO);
			
			return cal.getTime();
		}
	}
	
	private boolean checkValidityOfEvent(TaskCard eventToBeAdded) {
		Calendar start = eventToBeAdded.getStartDay();
		Calendar end = eventToBeAdded.getEndDay();
		
		if(end.before(start)) {
			return false;
		}
		
	  return true;
	}

	private void resetFlag() {
		urgent_flag = false;
	}
	
	private void initCmdTable() {
		cmdTable = new HashMap<String, Integer>();
		cmdTable.put("add", 1);
		cmdTable.put("addu", 2);
	}
}
