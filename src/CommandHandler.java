import java.util.Scanner;

public class CommandHandler {
	
	private Scanner scan = new Scanner(System.in);
	private FileLinker fileLink;
	
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "It appears you have typed "
			+ "something wrongly! Please try another command.";
	
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, SORT, SEARCH, EXIT, INVALID, RESET
	};
	
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
				response = Add.executeAdd(tokenizedInput);	
				break;
			case DISPLAY:
				response = Display.executeDis();
				break;
			case CLEAR:
				response = Reset.executeReset(tokenizedInput);
				break;
			case DELETE:
				break;
			case SORT:
				break;
			case SEARCH:
				response = Search.executeSearch(tokenizedInput);
				break;
			case INVALID:
				invalidCommandErrorHandling();
				break;
			case EXIT:
				System.exit(0);
				break;
			default:
				response = "Invalid Command";
				break;
		}
		//FileHandler.writeIncompleteTasksFile();
		return response;
	}
	
	private COMMAND_TYPE determineCommandType(String commandTypeString) {
		commandTypeString = commandTypeString.toLowerCase();
		
		if (commandTypeString.contains("/add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.contains("/dis")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.contains("/del")) {
			 	return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.contains("/clear")) {
		 	return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.contains("/sort")) {
		 	return COMMAND_TYPE.SORT;
		} else if (commandTypeString.contains("/search")) {
		 	return COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.contains("/exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else if (commandTypeString.contains("/reset")) {
			return COMMAND_TYPE.RESET;
		}	else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private void invalidCommandErrorHandling() {
		printErrorMessage();	
		String userInput = scan.nextLine();

		executeCommand(userInput);
	}
	
	private void printErrorMessage() {
		System.out.println(MESSAGE_ERROR_INVALID_COMMAND);
	}
}
