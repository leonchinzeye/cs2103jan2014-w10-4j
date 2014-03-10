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
	private static int choice;
	private static ArrayList <TaskCard> originalList; //this is the cloned to do list
	private static ArrayList <TaskCard> editList; //this is the list that will be printed out on the screen
	private static ArrayList <Integer> editIndex; //this is to store the indices of the TaskCards that match the keyword
	private static ArrayList <Integer> secondaryIndex; //this is a temporary storage
	//as of right now, I'm still not sure what editIndex will do
	private static TaskCard editedTask;
	private static boolean edited = false;
	private static boolean hasDate;
	private static boolean isDigit = false;
	private static String query = "";
	private static String response = "";
	private static Calendar dateQuery = GregorianCalendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM");
	private static SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
	
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
		
		if (edited == true) {
			return "Edit successful";
		} else {
			return null;
		}
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
		listEditor();
		edited = fileLink.editHandling(editedTask, editIndex.get(choice));
	}

	private static void searchByDate(FileLinker fileLink, ArrayList<TaskCard> lookThru, ArrayList<TaskCard> addTo) {
		for (int i = 0; i < Storage.numberOfIncompleteTasks; i++) {
			if (dateQuery.equals(lookThru.get(i).getStartDay()) 
				|| dateQuery.equals(lookThru.get(i).getEndDay())) {
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}
	
	private static void searchByDigit(FileLinker fileLink, ArrayList<TaskCard> lookThru, ArrayList<TaskCard> addTo) {
		for (int i = 0; i < Storage.numberOfIncompleteTasks; i++) {
			if (lookThru.get(i).getName().contains(query)
				|| lookThru.get(i).getStartDay().toString().contains(query)
				|| lookThru.get(i).getEndDay().toString().contains(query)) {
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}
	
	private static void searchByKeyword(FileLinker fileLink, ArrayList<TaskCard> lookThru, ArrayList<TaskCard> addTo) {
		for (int i = 0; i < Storage.numberOfIncompleteTasks; i++) {
			if (lookThru.get(i).getName().equals(query)) {
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}

	private static void listEditor() {
		if (editList.isEmpty()) {
			System.out.println("Keyword not found! Try a different keyword.");
		} else {
			Collections.sort(editList, new SortPriority());
			System.out.println("Which would you like to edit?");
			for (int j = 0; j < editList.size(); j++) {
				System.out.println(j+1 + ". " + editList.get(j).getTaskString());
			}
			String input = scan.nextLine();
			if (input.toLowerCase().equals("!q")) {
				return;
			} else if (checkForDigit(input) == false) {
				//invalid input, ask for input again
			} else if (checkForDigit(input) == true) {
				choice = Integer.parseInt(input);
				if (choice > editList.size()) {
					//invalid input, ask for input again
				} else {
					editedTask = editList.get(choice - 1);
					System.out.println("What would you like to edit?");
					System.out.println("1. Name\n" + "2. Start date\n" + "3. End date\n"
										+ "4. Start time\n" + "5. End time\n" + "6. Priority");
					int nextChoice = scan.nextInt();
					editTaskAttribute(nextChoice);
				}
			}
		}
	}

	private static void editTaskAttribute(int nextChoice) {
		switch (nextChoice) {
			case 1:
				String newName = scan.nextLine();
				editedTask.setName(newName);
				break;
			case 2:
				//change date
				break;
			case 3:
				//change date
				break;
			case 4:
				//change time
				break;
			case 5:
				//change time
				break;
			case 6:
				int priority = scan.nextInt();
				editedTask.setPriority(priority);
				break;
			default:
				break;
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
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private static String getKeywordFromUser() {
		boolean enteredAction = false;
		String keyword = "";
		while(enteredAction == false) {
			System.out.println("You did not specify a keyword. Please enter a keyword: ");
			
			if(scan.hasNext()) {
				keyword = scan.nextLine();
				enteredAction = true;
			}
		}
		return keyword;
	}
	
	private static class SortPriority implements Comparator<TaskCard> {
		public int compare(TaskCard o1, TaskCard o2) {
			Integer i1 = (Integer) o1.getPriority();
			Integer i2 = (Integer) o2.getPriority();
			return i2.compareTo(i1);
		}
	}
}
