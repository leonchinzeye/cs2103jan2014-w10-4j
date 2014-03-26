package application;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Reset {

	private static final String MESSAGE_SUCCESS_RESET_INCOMPLETE = "Tasks and schedule have been cleared!";
	private static final String MESSAGE_SUCCESS_RESET_HISTORY = "Archived tasks have been cleared!";
	private static final String MESSAGE_SUCCESS_RESET_BOTH = "Both uncompleted and archived tasks have been cleared!";
	
	private static final String MESSAGE_ERROR_VALID_SELECTION = "Please enter a valid selection (yes/no)";
	private static final String MESSAGE_RESET_CMD_ERROR = "The clear command you've entered is not recognised. Please re-enter your command: ";

	private static final String MESSAGE_CONFIRMATION_HISTORY = "Are you sure you want to clear you archived files? (yes/no)";
	private static final String MESSAGE_CONFIRMATION_UNCOMPLETED = "Are you sure you want to clear your uncompleted tasks? (yes/no)";
	private static final String MESSAGE_CONFIRMATION_BOTH = "Are you sure you want to clear both current and history files? (yes/no)";
	
	private static final int RESET_INCOMPLETE = 1;
	private static final int RESET_ALL = 2;
	private static final int RESET_EVENTS = 3;
	private static final int RESET_TASKS = 4;
	private static final int RESET_HISTORY = 5;
	
	private static final int FIRST_ARGUMENT = 0;
	
	private static Calendar today = Calendar.getInstance(); 
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final String MESSAGE_ERROR_CMD = "Error in reading your input. "
			+ "What is it that you want to clear?";
	
	
	private static HashMap<String, Integer> commandTable = new HashMap<String, Integer>();
	private static Scanner scan = new Scanner(System.in);
	
	/*
	 * Reset constructor
	 */
	public Reset() {
		
	}

	public static String executeReset(String[] userInput, FileLinker fileLink, DataUI dataToBePassedToUI) {
		initialiseCmdTypes();
		String action = null;
		
		boolean correctCmd;
		String response = null;
		
		correctCmd = checkCmdInput(userInput[FIRST_ARGUMENT]);
		
		if(correctCmd == true) {
			String cmd = userInput[FIRST_ARGUMENT];
			
			if(userInput.length > 1) {
				action = userInput[1];
			}
			
			response = identifyCmdTypeAndPerform(cmd, action, fileLink);
		} else {
			response = MESSAGE_RESET_CMD_ERROR;
		}
		
		return response;
	}
	
	/*
	 * for checking if the first argument is exactly "/clear"
	 * user might have input "/clearCS2105" and forgotten the spacing
	 */
	private static boolean checkCmdInput(String cmd) {
		String cmdLowerCase = cmd.toLowerCase();
		
		if(commandTable.containsKey(cmdLowerCase)) {
			return true;
		} else {
			return false;
		}
	}

	private static String identifyCmdTypeAndPerform(String cmd, String action, FileLinker fileLink) {
		int cmdType = commandTable.get(cmd);
		boolean resetConfirmation = false;
		String response = null;
		
		if(cmdType == RESET_INCOMPLETE) {
			resetConfirmation = resetIncomplete(fileLink);
			if(resetConfirmation)
				response = MESSAGE_SUCCESS_RESET_INCOMPLETE;
					
		} else if(cmdType == RESET_HISTORY) {
			resetConfirmation = resetHistory(fileLink);
			if(resetConfirmation)
				response = MESSAGE_SUCCESS_RESET_HISTORY;
			
		} else if(cmdType == RESET_ALL) {
			resetBothFiles(fileLink);
			response = MESSAGE_SUCCESS_RESET_BOTH;
			
		} else if(cmdType == RESET_EVENTS) {
			resetEvents(action, fileLink);
			
		} else {
			resetTasks();
		}
		
		return response;
	}

	private static void resetTasks() {
		// TODO Auto-generated method stub
		
	}

	private static void resetEvents(String action, FileLinker fileLink) {
		// TODO Auto-generated method stub
		// clear today
		// clear date
		// clear an instance of a specified event (all of it)
		
		
	}

	/**
	 * method to clear history
	 * @param fileLink 
	 * 
	 */
	private static boolean resetHistory(FileLinker fileLink) {
		boolean clearConfirmation = false;
		print(MESSAGE_CONFIRMATION_HISTORY);
		
		clearConfirmation = isValidConfirmation(clearConfirmation);
		
		if(clearConfirmation == true) {
			fileLink.resetCompleteHandling();
		}
		
		return clearConfirmation;
	}

	private static boolean resetIncomplete(FileLinker fileLink) {
		boolean clearConfirmation = false;
		print(MESSAGE_CONFIRMATION_UNCOMPLETED);
		
		clearConfirmation = isValidConfirmation(clearConfirmation);
		
		if(clearConfirmation == true) {
			fileLink.resetCompleteHandling();
		}
		
		return clearConfirmation;
	}
	
	private static void resetBothFiles(FileLinker fileLink) {
		boolean clearConfirmation = false;
		print(MESSAGE_CONFIRMATION_BOTH);
		
		clearConfirmation = isValidConfirmation(clearConfirmation);
		
		if(clearConfirmation == true) {
			fileLink.resetCompleteHandling();
			fileLink.resetIncompleteHandling();
		}
	}
	
	private static boolean isValidConfirmation(boolean clearConfirmation) {
		boolean correctUserConfirmation = false;
		while(correctUserConfirmation == false) {
			String userConfirmation = scan.nextLine();
			
			userConfirmation = userConfirmation.toLowerCase();
			
			if(userConfirmation.equals("yes") || userConfirmation.equals("y")) {
				correctUserConfirmation = true;
				clearConfirmation = true; 
			} else if(userConfirmation.equals("no") || userConfirmation.equals("n")) {
				correctUserConfirmation = true;
				clearConfirmation = false;
			} else {
				print(MESSAGE_ERROR_VALID_SELECTION);
			}
		}
		return clearConfirmation;
	}

	/**
	 * handle the error identified in checkCmdInput
	 */
	private static void clearErrorHandling() {
		
	}
	
	private static void print(String message) {
		System.out.println(message);
	}

	private static void initialiseCmdTypes() {
		commandTable.put("/reset", RESET_INCOMPLETE);
		commandTable.put("/resetall", RESET_ALL);
		commandTable.put("/resete", RESET_EVENTS);
		commandTable.put("/resett", RESET_TASKS);
		commandTable.put("/reseth", RESET_HISTORY);
	}
	
	
}
