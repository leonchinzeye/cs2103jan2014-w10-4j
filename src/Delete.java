import java.util.ArrayList;
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
	
	private static HashMap<String, Integer> cmdTable = new HashMap<String, Integer>();
	private static Scanner scan = new Scanner(System.in);

	public Delete() {
		// TODO Auto-generated constructor stub
	}

	public static String executeDelete(String[] tokenizedInput, FileLinker fileLink) {
		String response = "";
		
		initialiseCmdTable();
		
		if(checkCmdInput(tokenizedInput[0]) == true) {
			identifyCmdTypeAndPerform(tokenizedInput, fileLink);
		} else {
			response = "You've entered an unrecognisable delete command. Please re-enter your command:";
		}
				
		return response;
	}
	
	private static void identifyCmdTypeAndPerform(String[] tokenizedInput,
			FileLinker fileLink) {
		int cmdType = cmdTable.get(tokenizedInput[0]);
		
		switch(cmdType) {
			case 1:
				performSpecificDelete(tokenizedInput, fileLink);
				break;
			case 2:
				
				break;
			case 3:
				
				break;
			default:
				break;
		}
	}

	private static void performSpecificDelete(String[] tokenizedInput,
			FileLinker fileLink) {
		String action = "";
		
		//only /delete
		if(tokenizedInput.length < 2) {
			action = getActionFromUser();
		}
		
	}

	private static String getActionFromUser() {
		boolean enteredAction = false;
		String action = "";
		
		while(enteredAction == false) {
			print("You did not specify a date or keyword you wish to delete. Please enter a date or keyword: ");
			
			if(scan.hasNext()) {
				action = scan.nextLine();
				enteredAction = true;
			}
		}

		return action;
	}

	private static boolean checkCmdInput(String cmd) {
		boolean isCorrectCmd = false;
		
		if(cmdTable.containsKey(cmd)) {
			isCorrectCmd = true;
		}
		
		return isCorrectCmd;
	}
	
	private static void initialiseCmdTable() {
		cmdTable.put("/delete", 1);
		cmdTable.put("/deletet", 2);
		cmdTable.put("/deletee", 3);
	}
	
	private static void print(String message) {
		System.out.println(message);
	}
}
