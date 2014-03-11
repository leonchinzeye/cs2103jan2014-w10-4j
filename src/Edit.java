import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Edit {
	/**
	 * First, edit will see whether the keyword exists.
	 * Next, it will check whether it's a date.
	 * If yes, it will search for the date in the to do list.
	 * If no, it will see whether it's a digit.
	 * If yes, it will search for the digit in the task name or task date in the list.
	 * If no, it will search for the keyword in the task name in the list.
	 * If not found, it will return the appropriate message.
	 * @param cmdArray
	 * @param fileLink
	 * @return
	 * @author Omar Khalid
	 */
	
	private static Scanner scan = new Scanner(System.in);
	private static ArrayList<Integer> editIndex = new ArrayList<Integer>();
	private static int indexNumber;
	private static int nextIndexNumber;
	private static TaskCard editedTask;
	private static boolean edited = false;
	private static boolean isDate = false;
	private static boolean isString = false;
	private static boolean isInteger = false;
	private static boolean editedFlag = false;
	private static boolean quitSecondMenu = false;
	private static String keyword = "";
	private static String response = "";
	private static Calendar dateQuery = GregorianCalendar.getInstance();
	private static Calendar editedDate = GregorianCalendar.getInstance();
	private static Calendar editedTime = GregorianCalendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
	
	private static final String EDIT_SUCCESS = "Your edit was successful!\n%s";
	private static final String EDIT_FAILURE = "Something seemed to have gone wrong somewhere :(. Please try something else instead.";
	private static final String KEYWORD_NOT_ENTERED = "You seem to have forgotten something! Please type in a keyword to search for: ";
	private static final String KEYWORD_NOT_FOUND = "We can't seem to find anything by your keyword :(";
	private static final String NOT_DIGIT_ENTERED = "You seem to have mistakenly entered something that is not a digit!";
	private static final String INVALID_INPUT_PROMPT_WITH_RANGE = "Please enter something between 1 and %d: ";
	private static final String FIRST_MENU_QUERY = "Which task would you like to edit?";
	private static final String SECOND_MENU_QUERY = "What attribute would you like to change?";
	private static final String INCORRECT_TIME_INPUT = "We can't seem to recognize the time that you have entered. :( \nPlease try again: ";
	private static final String INCORRECT_DATE_INPUT = "We can't seem to recognize a date from what you entered. :( \nPlease try again: ";
	
	public static String executeEdit (String[] tokenizedInput, FileLinker fileLink) {
		ArrayList<TaskCard> editList = new ArrayList<TaskCard>();
		
		//check whether there's an argument for edit
		if(tokenizedInput.length < 2) {
			keyword = getKeywordFromUser();
			if (keyword.equals("!q")) {
				response = null;
				return response;
			}
		} else {
			keyword = tokenizedInput[1];
		}
		
		checkKeywordType(keyword);
		editList = editFilter(fileLink);
		
		if (editList.isEmpty()) {
			print(KEYWORD_NOT_FOUND);
			return null;
		} else {
			firstEditMenu(editList);
		}
		
		if (editedFlag == false) {
			return null;
		} else {
			edited = fileLink.editHandling(editedTask, editIndex.get(indexNumber - 1));
		}
		
		if (edited == true) {
			response = String.format(EDIT_SUCCESS, editedTask.getTaskString());
		} else {
			response = EDIT_FAILURE;
		}
		
		return response;
	}

	/**
	 * Check whether keyword is a date, an integer, or a string.
	 * @param keyword
	 * @author Omar Khalid
	 */
	private static void checkKeywordType(String keyword) {
		isInteger = checkIsInteger(keyword);
		isDate = checkIsDate(keyword);
	}

	private static ArrayList<TaskCard> editFilter(FileLinker fileLink) {
		ArrayList<TaskCard> editList = new ArrayList<TaskCard>();

		if (isDate == true) {
			editList = searchByDate(fileLink);
		} else if (isInteger == true) {
			editList = searchByDigit(fileLink);
		} else {
			editList = searchByKeyword(fileLink);
		}
		
		return editList;
	}

	private static ArrayList<TaskCard> editBasedOnDate(FileLinker fileLink) {
		return null;
	}

	private static ArrayList<TaskCard> searchByDate(FileLinker fileLink) {
		ArrayList<TaskCard> incompleteTasks = fileLink.editRetrieval();
		ArrayList<TaskCard> editList = new ArrayList<TaskCard>();
		
		int numberOfIncompleteTasks = incompleteTasks.size();
		for (int i = 0; i < numberOfIncompleteTasks; i++) {
			if (dateQuery.equals(incompleteTasks.get(i).getStartDay())
				|| dateQuery.equals(incompleteTasks.get(i).getEndDay())) {
				editList.add(incompleteTasks.get(i));
				editIndex.add(i);
			}
		}
		return editList;
	}
	
	private static ArrayList<TaskCard> searchByDigit(FileLinker fileLink) {
		ArrayList<TaskCard> incompleteTasks = fileLink.editRetrieval();
		ArrayList<TaskCard> editList = new ArrayList<TaskCard>();
		
		int numberOfIncompleteTasks = incompleteTasks.size();
		for (int i = 0; i < numberOfIncompleteTasks; i++) {
			if (incompleteTasks.get(i).getTaskString().contains(keyword)) {
				editList.add(incompleteTasks.get(i));
				editIndex.add(i);
			}
		}
		return editList;
	}
	
	private static ArrayList<TaskCard> searchByKeyword(FileLinker fileLink) {
		ArrayList<TaskCard> incompleteTasks = fileLink.editRetrieval();
		ArrayList<TaskCard> editList = new ArrayList<TaskCard>();
		
		int numberOfIncompleteTasks = incompleteTasks.size();
		for (int i = 0; i < numberOfIncompleteTasks; i++) {
			if (incompleteTasks.get(i).getTaskString().contains(keyword)) {
				editList.add(incompleteTasks.get(i));
				editIndex.add(i);
			}
		}
		return editList;
	}
	
	private static void firstEditMenu(ArrayList<TaskCard> editList) {
		printFirstMenu(editList);
		
		boolean correctUserInput = false;
		while(correctUserInput == false) {
			String userInput = scan.nextLine();
			if(userInput.equals("!q")) {
				break;
			} else if (checkIsInteger(userInput) == false) {
				print(NOT_DIGIT_ENTERED);
				print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, editList.size()));
			} else {
				indexNumber = Integer.parseInt(userInput);
				if (indexNumber > editList.size() || indexNumber < 1) {
					print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, editList.size()));
				} else {
					correctUserInput = true;
					editedTask = editList.get(indexNumber - 1);		
					while (quitSecondMenu == false) {
						secondEditMenu(editList);
					}
				}
			}
		}
	}

	private static void printFirstMenu(ArrayList<TaskCard> editList) {
		Collections.sort(editList, new SortPriority());
		print(FIRST_MENU_QUERY);
		for (int j = 0; j < editList.size(); j++) {
			print(j+1 + ". " + editList.get(j).getTaskString());
		}
	}

	private static void secondEditMenu(ArrayList<TaskCard> editList) {
		printSecondMenu();
		
		boolean correctNextIndex = false;
		while(correctNextIndex == false) {
			String nextIndex = scan.nextLine();
			if(nextIndex.equals("7")) {
				quitSecondMenu = true;
				break;
			} else if (!checkIsInteger(nextIndex)){
				print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, 6));
			} else {
				nextIndexNumber = Integer.parseInt(nextIndex);
				if (nextIndexNumber > 6 || nextIndexNumber < 1) {
					print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, 6));
				} else {
					correctNextIndex = true;
					editTaskAttribute(nextIndexNumber, editList);
					if (editedFlag == true) {
						print(editedTask.getTaskString());
					}
				}
			}
		}
	}

	private static void printSecondMenu() {
		print(SECOND_MENU_QUERY);
		print("1. Name");
		print("2. Start date");
		print("3. End date");
		print("4. Start time");
		print("5. End time");
		print("6. Priority");
		print("7. Exit");
	}

	private static void editTaskAttribute(int nextIndex, ArrayList<TaskCard> editList) {
		switch (nextIndex) {
			case 1:
				print("Please enter a new name for this task: ");
				String newName = scan.nextLine();
				editedTask.setName(newName);
				editedFlag = true;
				break;
			case 2:
				print("Please enter a new start date for this task: ");
				editedDate = editList.get(indexNumber - 1).getStartDay();
				editDate();
				editedTask.setStartDay(editedDate);
				editedFlag = true;
				break;
			case 3:
				print("Please enter a new end date for this task: ");
				editedDate = editList.get(indexNumber - 1).getEndDay();
				editedTask.setEndDay(editedDate);
				editedFlag = true;
				break;
			case 4:
				print("Please enter a new start time for this task: ");
				editedTime = editList.get(indexNumber - 1).getStartDay();
				editTime();
				editedTask.setStartDay(editedTime);
				editedFlag = true;
				break;
			case 5:
				print("Please enter a new end time for this task: ");
				editedTime = editList.get(indexNumber - 1).getEndDay();
				editTime();
				editedTask.setEndDay(editedTime);
				editedFlag = true;
				break;
			case 6:
				print("Please enter a new priority for this task: ");
				int priority = scan.nextInt();
				editedTask.setPriority(priority);
				editedFlag = true;
				break;
			case 7:
				break;
			default:
				break;
		}
	}

	private static void editDate() {
		boolean correctDateInput = false;
		while (correctDateInput == false) {
			String newDate = scan.nextLine();
			try {
				dateString.parse(newDate);
				correctDateInput = true;
			} catch(ParseException e) {
				print(INCORRECT_DATE_INPUT);
			}
			
			if (correctDateInput == true) {
				String[] dateArray = newDate.split("/");
				int[] date = new int[dateArray.length];
				for (int i = 0; i < dateArray.length; i++) {
					date[i] = Integer.parseInt(dateArray[i]);
				}
				int day = date[0];
				int month = date[1];
				int year = date[2];
				editedDate.set(Calendar.DAY_OF_MONTH, day);
				editedDate.set(Calendar.MONTH, month - 1);
				editedDate.set(Calendar.YEAR, year);
			}
		}
	}
	
	private static void editTime() {
		boolean correctTimeInput = false;
		while (correctTimeInput == false) {
			String newTime = scan.nextLine();
			try {
				timeString.parse(newTime);
				correctTimeInput = true;
			} catch(ParseException e) {
				print(INCORRECT_TIME_INPUT);
			}
			
			if (correctTimeInput == true) {
				String[] timeArray = newTime.split(":");
				int[] time = new int[timeArray.length];
				for (int i = 0; i < timeArray.length; i++) {
					time[i] = Integer.parseInt(timeArray[i]);
				}
				int hour = time[0];
				int minute = time[1];
				editedTime.set(Calendar.HOUR, hour);
				editedTime.set(Calendar.MINUTE, minute);
			}
		}
	}

	private static boolean checkIsDate(String query) {
		boolean isDate;
		//check if keyword is a date
		try {
			dateQuery.setTime(dateAndTime.parse(query));
			dateQuery.setTime(dateString.parse(query));
			dateQuery.setTime(timeString.parse(query));
			isDate = true;
		} catch (ParseException e){
			isDate = false;
		}
		return isDate;
	}
	
	private static boolean checkIsInteger(String keyword) {
		boolean isInteger;
		//check if keyword is a digit
		try {
			Integer.parseInt(keyword);
			isInteger = true;
		} catch (NumberFormatException e) {
			isInteger = false;
		}
		return isInteger;
	}
	
	private static String getKeywordFromUser() {
		boolean enteredAction = false;
		String keyword = "";
		while(enteredAction == false) {
			System.out.println(KEYWORD_NOT_ENTERED);
			
			if(scan.hasNext()) {
				keyword = scan.nextLine();
				enteredAction = true;
			}
		}
		return keyword;
	}
	
	private static void print(String toPrint) {
		System.out.println(toPrint);
	}
	
	private static class SortPriority implements Comparator<TaskCard> {
		public int compare(TaskCard o1, TaskCard o2) {
			Integer i1 = (Integer) o1.getPriority();
			Integer i2 = (Integer) o2.getPriority();
			return i2.compareTo(i1);
		}
	}
}
