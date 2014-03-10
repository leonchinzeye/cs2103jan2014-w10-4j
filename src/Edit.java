import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	private static String keyword = "";
	private static Calendar dateQuery = GregorianCalendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM");
	private static SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
	
	public static String executeEdit (String[] cmdArray, FileLinker fileLink) {
		if (cmdArray.length == 1) {
			return "Invalid query! Please enter a keyword to search for.";
		} else {
			originalList = fileLink.editRetrieval();
			boolean tempCheckDate;
			//need to account if search query has both a keyword and date
			for (int i = 1; i < cmdArray.length; i++) {
				tempCheckDate = false;
				tempCheckDate = checkForDate(cmdArray[i]);
				if (tempCheckDate == false) {
					keyword.concat(cmdArray[i]);
					keyword += " ";
				}
			}
			keyword.replaceFirst("\\s+$", ""); //remove any trailing whitespace
			
			if (hasDate == true) {
				ArrayList <TaskCard> tempEditList = new ArrayList <TaskCard>();
				ArrayList <Integer> tempIndex = new ArrayList <Integer>();
				searchByKeyword(fileLink, originalList, tempEditList); //filter by keyword first
				if (!tempEditList.isEmpty()) {
					tempIndex = secondaryIndex;
					secondaryIndex.clear();
					searchByDate(fileLink, tempEditList, editList); //then filter by date
					//filter out editIndex
					for (int i = 0; i < secondaryIndex.size(); i++) {
						editIndex.add(tempIndex.get(i));
					}
				} else {
					editIndex = secondaryIndex;
				}
				
			} else {
				isDigit = checkForDigit(keyword);
				if (isDigit == true) {
					searchByDigit(fileLink, originalList, editList);
				} else {
					searchByKeyword(fileLink, originalList, editList);
				}
			}
			editIndex = secondaryIndex;
			printEditList();
			edited = fileLink.editHandling(editedTask, editIndex.get(choice));
			
			if (edited == true) {
				return "Edit successful";
			} else {
				return null;
			}
		}
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
			if (lookThru.get(i).getName().contains(keyword)
				|| lookThru.get(i).getStartDay().toString().contains(keyword)
				|| lookThru.get(i).getEndDay().toString().contains(keyword)) {
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}
	
	private static void searchByKeyword(FileLinker fileLink, ArrayList<TaskCard> lookThru, ArrayList<TaskCard> addTo) {
		for (int i = 0; i < Storage.numberOfIncompleteTasks; i++) {
			if (lookThru.get(i).getName().equals(keyword)) {
				addTo.add(lookThru.get(i));
				secondaryIndex.add(i);
			}
		}
	}

	private static void printEditList() {
		if (editList.isEmpty()) {
			System.out.println("Keyword not found! Try a different keyword.");
		} else {
			System.out.println("Which would you like to edit?");
			for (int j = 0; j < editList.size(); j++) {
				System.out.println(j+1 + ". " + editList.get(j).getTaskString());
			}
			String input = scan.nextLine();
			if (input.toLowerCase().equals("exit")) {
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
}
