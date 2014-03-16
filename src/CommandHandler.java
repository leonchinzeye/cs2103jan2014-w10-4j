import java.util.ArrayList;
import java.util.Scanner;

public class CommandHandler {
	
	private Scanner scan = new Scanner(System.in);
	private FileLinker fileLink;
	private DataUI dataToBePassedToUI;
	
	private boolean state_add;
	private boolean state_del;
	private boolean state_edit;
	private boolean state_ref;
	private boolean state_search;
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "It appears you have typed "
			+ "something wrongly! Please try another command.";
	
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, EDIT, SEARCH, RESET, EXIT, INVALID
	}
	
	public CommandHandler() {
		fileLink = new FileLinker();
		dataToBePassedToUI = new DataUI();
		
		RefreshUI.executeRefresh(fileLink, dataToBePassedToUI);
		
		state_add = false;
		state_del = false;
		state_edit = false;
		state_ref = false;
		state_search = false;
	}
	
	public DataUI executeCmd(String userInput) {
		if(newCommand()) {
			executeCommand(userInput);
		} else {
			checkStatesAndPerform(userInput);
		}
		
		return dataToBePassedToUI;
	}
	
	private void checkStatesAndPerform(String userInput) {
		if(state_add == true) {
			//Add.executeAdd(userInput, fileLink, dataToBePassedToUI);
		} else if(state_del == true) {
			//Delete.executeDelete(userInput, fileLink, dataToBePassedToUI);
		} else if(state_edit == true) {
			//Edit.executeEdit(userInput, fileLink, dataToBePassedToUI);
		} else if(state_ref == true) {
			RefreshUI.executeRefresh(fileLink, dataToBePassedToUI);
		} else if(state_search == true) {
			//Search.executeSearch(userInput, fileLink, dataToBePassedToUI);
		}
	}

	private boolean newCommand() {
		if(state_add == false && state_del == false && state_edit == false 
				&& state_ref == false && state_search == false)
			return true;
		
		return false;
	}

	public String executeCommand(String userInput) {
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		String commandTypeString = tokenizedInput[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		
		String response = "";
				
		switch(commandType) {
			case ADD:
				response = Add.executeAdd(tokenizedInput, fileLink, dataToBePassedToUI);	
				break;
			case DISPLAY:
				response = RefreshUI.executeDis(fileLink, dataToBePassedToUI);
				break;
			case RESET:
				response = Reset.executeReset(tokenizedInput, fileLink, dataToBePassedToUI);
				break;
			case DELETE:
				response = Delete.executeDelete(tokenizedInput, fileLink, dataToBePassedToUI);
				break;
			case EDIT:
				response = Edit.executeEdit(tokenizedInput, fileLink, dataToBePassedToUI);
				break;
			case SEARCH:
				response = Search.executeSearch(tokenizedInput, fileLink, dataToBePassedToUI);
				break;
			case INVALID:
				dataToBePassedToUI.setFeedback(MESSAGE_ERROR_INVALID_COMMAND);
				//response = invalidCommandErrorHandling();
				break;
			case EXIT:
				System.exit(0);
				break;
			default:
				invalidCommandErrorHandling();
				break;
		}
		return response;
	}
	
	private COMMAND_TYPE determineCommandType(String commandTypeString) {
		commandTypeString = commandTypeString.toLowerCase();
		
		if (commandTypeString.contains("/add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equals("/display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.contains("/del")) {
			 return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.contains("/clear")) {
		 	return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.contains("/edit")) {
		 	return COMMAND_TYPE.EDIT;
		} else if (commandTypeString.contains("/search")) {
		 	return COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.contains("/reset")) {
			return COMMAND_TYPE.RESET;
		} else if (commandTypeString.contains("/exit")) {
		 	return COMMAND_TYPE.EXIT;
		}	else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private String invalidCommandErrorHandling() {
		String response = "";
		
		printErrorMessage();	
		String userInput = scan.nextLine();

		response = executeCommand(userInput);
		
		return response;
	}
	
	private void printErrorMessage() {
		System.out.println(MESSAGE_ERROR_INVALID_COMMAND);
	}
}
