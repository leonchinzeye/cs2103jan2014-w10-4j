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
	private static ArrayList <TaskCard> originalList = new ArrayList<TaskCard>(); //this is the cloned to do list
	private static ArrayList <TaskCard> editList = new ArrayList<TaskCard>(); //this is the list that will be printed out on the screen
	private static ArrayList <Integer> editIndex = new ArrayList<Integer>(); //this is to store the indices of the TaskCards that match the keyword
	private static ArrayList <Integer> secondaryIndex = new ArrayList<Integer>(); //this is a temporary storage
	private static int indexNumber;
	private static int nextIndexNumber;
	private static TaskCard editedTask;
	private static boolean edited = false;
	private static boolean hasDate;
	private static boolean isDigit = false;
	private static boolean editedFlag = false;
	private static String query = "";
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
		String keyword = "";
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
		
		originalList = fileLink.editRetrieval();
		checkKeywordType(keyword);
		editFilter(fileLink);
		
		if (editList.isEmpty()) {
			print(KEYWORD_NOT_FOUND);
			return null;
		} else {
			firstEditMenu();
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

	private static void checkKeywordType(String keyword) {
		boolean tempCheckDate;
		String[] tokenizedKeyword = keyword.trim().split("\\s+");
		//need to account if search query has both a keyword and date
		for (int i = 0; i < tokenizedKeyword.length; i++) {
			tempCheckDate = false;
			tempCheckDate = checkForDate(tokenizedKeyword[i]);
			if (tempCheckDate == false) {
				query.concat(tokenizedKeyword[i]);
				query += " ";
			}
		}
		query.replaceFirst("\\s+$", ""); //remove any trailing whitespace
	}

	private static void editFilter(FileLinker fileLink) {
		if (hasDate == true) {
			ArrayList <TaskCard> tempEditList = new ArrayList <TaskCard>();
			ArrayList <Integer> tempIndex = new ArrayList <Integer>();
			searchByKeyword(fileLink, originalList, tempEditList); //filter by keyword first
			if (!tempEditList.isEmpty()) {
				tempIndex = secondaryIndex;
				secondaryIndex.clear();
				searchByDate(fileLink, tempEditList, editList); //then filter by date
				for (int i = 0; i < secondaryIndex.size(); i++) {
					editIndex.add(tempIndex.get(i));
				}
			} else {
				editIndex = secondaryIndex;
			}
		} else {
			isDigit = checkForDigit(query);
			if (isDigit == true) {
				searchByDigit(fileLink, originalList, editList);
			} else {
				searchByKeyword(fileLink, originalList, editList);
			}
		}
		editIndex = secondaryIndex;
	}

	private static void searchByDate(FileLinker fileLink, ArrayList<TaskCard> lookThru, ArrayList<TaskCard> addTo) {
		int numberOfIncompleteTasks = originalList.size();
		for (int i = 0; i < numberOfIncompleteTasks; i++) {
			if (dateQuery.equals(lookThru.get(i).getStartDay()) 
				|| dateQuery.equals(lookThru.get(i).getEndDay())) {
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}
	
	private static void searchByDigit(FileLinker fileLink, ArrayList<TaskCard> lookThru, ArrayList<TaskCard> addTo) {
		int numberOfIncompleteTasks = originalList.size();
		for (int i = 0; i < numberOfIncompleteTasks; i++) {
			if (lookThru.get(i).getName().contains(query)
				|| lookThru.get(i).getTaskString().contains(query)
				|| lookThru.get(i).getTaskString().contains(query)) {
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}
	
	private static void searchByKeyword(FileLinker fileLink, ArrayList<TaskCard> lookThru, ArrayList<TaskCard> addTo) {
		int numberOfIncompleteTasks = originalList.size();
		for (int i = 0; i < numberOfIncompleteTasks; i++) {
			if (lookThru.get(i).getName().contains(query)) {
				System.out.println(lookThru.get(i).getName());
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}
	
	private static void firstEditMenu() {
		printFirstMenu();
		
		boolean correctUserInput = false;
		while(correctUserInput == false) {
			String userInput = scan.nextLine();
			if(userInput.equals("!q")) {
				break;
			} else if (checkForDigit(userInput) == false) {
				print(NOT_DIGIT_ENTERED);
				print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, editList.size()));
			} else {
				indexNumber = Integer.parseInt(userInput);
				if (indexNumber > editList.size() || indexNumber < 1) {
					print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, editList.size()));
				} else {
					correctUserInput = true;
					editedTask = editList.get(indexNumber - 1);		
					secondEditMenu();
				}
			}
		}
	}

	private static void printFirstMenu() {
		Collections.sort(editList, new SortPriority());
		print(FIRST_MENU_QUERY);
		for (int j = 0; j < editList.size(); j++) {
			print(j+1 + ". " + editList.get(j).getTaskString());
		}
	}

	private static void secondEditMenu() {
		printSecondMenu();
		
		boolean correctNextIndex = false;
		while(correctNextIndex == false) {
			String nextIndex = scan.nextLine();
			if(nextIndex.equals("!q")) {
				break;
			} else if (!checkForDigit(nextIndex)){
				print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, 6));
			} else {
				nextIndexNumber = Integer.parseInt(nextIndex);
				if (nextIndexNumber > 6 || nextIndexNumber < 1) {
					print(String.format(INVALID_INPUT_PROMPT_WITH_RANGE, 6));
				} else {
					correctNextIndex = true;
					editTaskAttribute(nextIndexNumber);
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
	}

	private static void editTaskAttribute(int nextIndex) {
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

	private static boolean checkForDate(String query) {
		//check if keyword is a date
		try {
			dateQuery.setTime(dateAndTime.parse(query));
			dateQuery.setTime(dateString.parse(query));
			dateQuery.setTime(timeString.parse(query));
		} catch (ParseException e){
			return false;
		}
		hasDate = true;
		return true;
	}
	
	private static boolean checkForDigit(String query) {
		//check if keyword is a digit
		try {
			Integer.parseInt(query);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
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
