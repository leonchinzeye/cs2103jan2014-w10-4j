import java.util.Scanner;

public class CommandHandler {
	
	private Scanner scan = new Scanner(System.in);
	private FileLinker fileLink;
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "It appears you have typed "
			+ "something wrongly! Please try another command.";
	
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, EDIT, SEARCH, RESET, EXIT, INVALID
	}
	
	public CommandHandler() {
		fileLink = new FileLinker();
	}
	
	public String executeCommand(String userInput) {
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		String commandTypeString = tokenizedInput[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		
		String response = "";
		
		switch(commandType) {
			case ADD:
				response = Add.executeAdd(tokenizedInput, fileLink);	
				break;
			case DISPLAY:
				response = Display.executeDis(fileLink);
				break;
			case RESET:
				response = Reset.executeReset(tokenizedInput, fileLink);
				break;
			case DELETE:
				response = Delete.executeDelete(tokenizedInput, fileLink);
				break;
			case EDIT:
				response = Edit.executeEdit(tokenizedInput, fileLink);
				break;
			case SEARCH:
				response = Search.executeSearch(tokenizedInput, fileLink);
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
