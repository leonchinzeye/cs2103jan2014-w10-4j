import java.util.Scanner;

public class CommandHandler {
	
	private Scanner scan = new Scanner(System.in);
	private FileLinker fileLink;
	private DataUI dataToBePassedToUI;
	
	private boolean state_add;
	private boolean state_delete;
	private boolean state_edit;
	private boolean state_refresh;
	private boolean state_search;
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "It appears you have typed "
			+ "something wrongly! Please try another command.";
	
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, EDIT, SEARCH, RESET, EXIT, INVALID
	}
	
	public enum ADD_ERROR {
		INVALID_FORMAT_FLOAT, INVALID_FORMAT_TASK, INVALID_FORMAT_EVENT
	}
	
	public enum DELETE_ERROR {
		INVALID_FORMAT, NO_INDEX 
	}
	
	public enum EDIT_ERROR {
		
	}
	
	public CommandHandler() {
		fileLink = new FileLinker();
		dataToBePassedToUI = new DataUI();
		
		state_add = false;
		state_delete = false;
		state_edit = false;
		state_refresh = false;
		state_search = false;
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
				response = invalidCommandErrorHandling();
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
