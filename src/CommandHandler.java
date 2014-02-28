public class CommandHandler {
	
	private FileHandler fh;
	private Add addCmd;
	private Clear clearCmd;
	private Delete deleteCmd;
	private Search searchCmd;
	
	
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, SORT, SEARCH, EXIT, INVALID
	};
	
	/*
	 * Constructor for CommandHandler.
	 * When CommandHandler is declared in the main, everything is loaded up. The program has been
	 * started by the user.
	 * Should display the list of current events to the user in the constructor with a display 
	 * command used
	 */
	public CommandHandler() {
		this.fh = new FileHandler();
		
		this.addCmd = new Add(fh);
		this.clearCmd = new Clear(fh);
		this.deleteCmd = new Delete(fh);
		this.searchCmd = new Search(fh);
	}
	
	public String executeCommand(String userInput) {
		TaskCard newCard = new TaskCard();
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		String commandTypeString = tokenizedInput[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		
		String response = "";
		
		switch(commandType) {
			case ADD:
				response = Add.executeAdd(tokenizedInput);
				break;
			case DISPLAY:
				break;
			case CLEAR:
				break;
			case DELETE:
				break;
			case SORT:
				break;
			case SEARCH:
				break;
			case INVALID:
				response = "Invalid Command";
				break;
			case EXIT:
				System.exit(0);
				break;
			default:
				response = "Invalid Command";
				break;
		}
		FileHandler.writeIncompleteTasksFile();
		return response;
	}
	
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		commandTypeString = commandTypeString.toLowerCase();
		if (commandTypeString == null)
			throw new Error("Command type string cannot be null!");

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
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
}
