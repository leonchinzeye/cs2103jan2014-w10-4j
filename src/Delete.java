import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 * delete class that will delete specific tasks
 * 1) check for keyword
 * 			- if exist, will print a list of those tasks containing the keyword
 * 			- else report no such tasks
 * 2) delete tasks on a specific date
 * 			- will pull out all the tasks that are on a specific date
 * 			- if there are no tasks, report no, else show all the tasks on that date
 * @author leon
 *
 */
public class Delete {
	
	private static final String MESSAGE_DELETION_PROMPTING_RANGE = "The number you have entered is not "
			+ "within range of tasks shown. Please re-enter a number between 1 to %d.";
	
	private static HashMap<String, Integer> cmdTable = new HashMap<String, Integer>();
	private static Scanner scan = new Scanner(System.in);
	private static boolean isDate = false;
	private static boolean isString = false;
	private static boolean isInteger = false;
	private static Calendar dateKeyword = GregorianCalendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM");
	private static SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
	

	public Delete() {
		// TODO Auto-generated constructor stub
	}

	public static String executeDelete(String[] tokenizedInput, FileLinker fileLink) {
		String response = "";
		
		initialiseCmdTable();
		
		if(checkCmdInput(tokenizedInput[0]) == true) {
			response = identifyCmdTypeAndPerform(tokenizedInput, fileLink);
		} else {
			response = "You've entered an unrecognisable delete command. Please re-enter your command:";
		}
				
		return response;
	}
	
	private static String identifyCmdTypeAndPerform(String[] tokenizedInput,
			FileLinker fileLink) {
		String response = "";
		int cmdType = cmdTable.get(tokenizedInput[0]);
		
		switch(cmdType) {
			case 1:
				response = performSpecificDelete(tokenizedInput, fileLink);
				break;
			case 2:
				
				break;
			case 3:
				
				break;
			default:
				break;
		}
		
		return response;
	}

	private static String performSpecificDelete(String[] tokenizedInput,
			FileLinker fileLink) {
		String response = "";
		String keyword = "";
				
		//only /delete
		if(tokenizedInput.length < 2) {
			keyword = getKeywordFromUser();
			
			if(keyword.equals("!q")) {
				response = null;
				return response;
			}
		} else {
			keyword = tokenizedInput[1];
		}
		
		checkKeywordType(keyword);
		
		if(isDate == true) {
			deleteBasedOnDate(keyword, fileLink);
		} else if(isInteger == true) {
			deleteBasedOnDateAndString(keyword, fileLink); 
		} else
			response = deleteBasedOnString(keyword, fileLink);
		
		return response;
	}

	private static String deleteBasedOnString(String keyword, FileLinker fileLink) {
		ArrayList<TaskCard> incompleteTasks = fileLink.deleteRetrieval();
		ArrayList<TaskCard> taskCardsToBeDeleted = new ArrayList<TaskCard>();
		ArrayList<Integer> deletedIndex = new ArrayList<Integer>();
		int userConfirmedIndex = -1;
		String response = "";
		
		int numberOfIncompleteTasks = incompleteTasks.size();
		
		for(int i = 0; i < numberOfIncompleteTasks; i++) {
			TaskCard task = incompleteTasks.get(i);
			String taskDescription = task.getName();
			taskDescription = taskDescription.toLowerCase();
			keyword = keyword.toLowerCase();
			
			if(taskDescription.contains(keyword)) {
				taskCardsToBeDeleted.add(task);
				deletedIndex.add(i);
			}
		}
		
		if(taskCardsToBeDeleted.isEmpty()) {
			print("Keyword is not found among the lists of tasks you have.");
		} else {
			userConfirmedIndex = getDeletionConfirmation(taskCardsToBeDeleted);
		}
		
		if(userConfirmedIndex == -1) {
			response = null;
		} else {
			String deletedTask = taskCardsToBeDeleted.get(userConfirmedIndex - 1).getTaskString();
			print("\"" + deletedTask + "\" has been deleted.");
			int fileIndexToBeDeleted = deletedIndex.get(userConfirmedIndex - 1);
			fileLink.deleteHandling(fileIndexToBeDeleted);
		}
		
		return response;
	}

	private static int getDeletionConfirmation(
			ArrayList<TaskCard> taskCardsToBeDeleted) {
		int userConfirmedIndex = -1;
		int counterReference = 1;
		boolean correctUserInput = false;
		
		print("Here is the list of items that contain your keyword. Which do you want to delete?");
		
		for(int i = 0; i < taskCardsToBeDeleted.size(); i++) {
			TaskCard task = taskCardsToBeDeleted.get(i);
			String taskDetails = task.getName();
			String toBePrinted = counterReference + ") " + taskDetails;
			print(toBePrinted);
			
			counterReference++;
		}
		
		/*
		 * - user has to enter either a number or "n" to exit this loop
		 * - if user enters anything else, it will throw an error prompt to 
		 * 	 get the user to re-enter his selection
		 * - if is integer, has to check if integer is within the range
		 * 
		 */
		while(correctUserInput == false) {
			String userInput = scan.nextLine();
			
			if(userInput.equals("!q")) {
				break;
				
			} else if(checkIsInteger(userInput)) {
				//check range of values coincide with those shown
				int indexNumber = Integer.parseInt(userInput);
				
				if(indexNumber > taskCardsToBeDeleted.size() || indexNumber < 1) {
					print(String.format(MESSAGE_DELETION_PROMPTING_RANGE, taskCardsToBeDeleted.size()));
				} else {
					userConfirmedIndex = indexNumber;
					correctUserInput = true;
				}
				
			} else {
				print(String.format("Please enter a number between 1 to %d.", taskCardsToBeDeleted.size()));
			}
		}
		
		return userConfirmedIndex;
	}

	private static void deleteBasedOnDateAndString(String keyword,
			FileLinker fileLink) {
		int integerKeyword = Integer.parseInt(keyword);
		
		
	}

	private static void deleteBasedOnDate(String keyword, FileLinker fileLink) {
		// TODO Auto-generated method stub
		
	}

	private static void checkKeywordType(String keyword) {
		isInteger = checkIsInteger(keyword);
		isDate = checkIsDate(keyword);
		if(isDate == false && isInteger == false) {
			isString = true;
		}
		
	}

	private static boolean checkIsDate(String keyword) {
		boolean isDate;
		
		try {
			dateKeyword.setTime(dateAndTime.parse(keyword));
			dateKeyword.setTime(dateString.parse(keyword));
			dateKeyword.setTime(timeString.parse(keyword));
			isDate = true;
		} catch(ParseException e) {
			isDate = false;
		}
		return isDate;
	}

	private static boolean checkIsInteger(String keyword) {
		boolean isInteger;
		try {
			Integer.parseInt(keyword);
			isInteger = true;
		} catch(NumberFormatException e) {
			isInteger = false;
		}
		
		return isInteger;
	}

	private static String getKeywordFromUser() {
		boolean enteredAction = false;
		String keyword = "";
		
		while(enteredAction == false) {
			print("You did not specify a keyword. Please enter a keyword: ");
			
			if(scan.hasNext()) {
				keyword = scan.nextLine();
				enteredAction = true;
			}
		}

		return keyword;
	}

	private static boolean checkCmdInput(String cmd) {
		boolean isCorrectCmd = false;
		
		if(cmdTable.containsKey(cmd)) {
			isCorrectCmd = true;
		}
		
		return isCorrectCmd;
	}
	
	private static void initialiseCmdTable() {
		cmdTable.put("/del", 1);
		cmdTable.put("/delt", 2);
		cmdTable.put("/delev", 3);
	}
	
	private static void print(String toPrint) {
		System.out.println(toPrint);
	}
}
