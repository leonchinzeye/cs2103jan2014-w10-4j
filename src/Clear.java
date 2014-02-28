import java.util.Scanner;
import java.util.HashMap;

public class Clear {

	private static final int CLEAR_INCOMPLETE = 1;
	private static final int CLEAR_ALL = 2;
	private static final int CLEAR_EVENTS = 3;
	private static final int CLEAR_TASKS = 4;
	private static final int CLEAR_HISTORY = 5;
	
	private static final int FIRST_ARGUMENT = 0;
	
	private static final String MESSAGE_ERROR_CMD = "Error in reading your input. "
			+ "What is it that you want to clear?";
	
	private static HashMap<String, Integer> commandTable;
	/*
	 * Clear constructor
	 */
	public Clear(FileHandler fh) {
		initialiseCmdTypes();
	}

	public static void executeClear(String[] userInput) {
		boolean correctCmd;
		
		correctCmd = checkCmdInput(userInput[FIRST_ARGUMENT]);
		
		if(correctCmd == true) {
			String cmd = userInput[FIRST_ARGUMENT];
			identifyCmdTypeAndPerform(cmd);
		} else {
			clearErrorHandling();
		}
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

	private static void identifyCmdTypeAndPerform(String cmd) {
		int cmdType = commandTable.get(cmd);
		
		if(cmdType == CLEAR_INCOMPLETE) {
			clearIncomplete();
		} else if(cmdType == CLEAR_HISTORY) {
			clearHistory();
		} else if(cmdType == CLEAR_ALL) {
			clearHistory();
			clearIncomplete();
		} else if(cmdType == CLEAR_EVENTS) {
			clearEvents();
		} else {
			clearTasks();
		}
	}

	private static void clearTasks() {
		// TODO Auto-generated method stub
		
	}

	private static void clearEvents() {
		// TODO Auto-generated method stub
		
	}

	private static void clearHistory() {
		FileHandler.initialiseFileDetails(FileHandler.COMPLETED_TASKS_STORAGE_FILE_NAME);
		FileHandler.writeCompleteTasksFile();
	}
	
	private static void clearIncomplete() {
		FileHandler.initialiseFileDetails(FileHandler.INCOMPLETE_TASKS_STORAGE_FILE_NAME);
		FileHandler.writeIncompleteTasksFile();
	}
	
	/*
	 * handle the error identified in checkCmdInput
	 */
	private static void clearErrorHandling() {
		
	}

	private static void initialiseCmdTypes() {
		commandTable.put("/clear", CLEAR_INCOMPLETE);
		commandTable.put("/clearall", CLEAR_ALL);
		commandTable.put("/cleare", CLEAR_EVENTS);
		commandTable.put("cleart", CLEAR_TASKS);
		commandTable.put("/clearh", CLEAR_HISTORY);
		
	}
}
