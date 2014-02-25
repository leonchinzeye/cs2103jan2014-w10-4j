public class CommandHandler {
	static FileHandler fh = new FileHandler();
	
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, SORT, SEARCH, EXIT, INVALID
	};
	
	public String executeCommand(String commandFull) {
		TaskCard newCard = new TaskCard();
		String response = "";
		String[] cmd = commandFull.split(" ", 2);
		String commandTypeString = cmd[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
			
		switch(commandType) {
			case ADD:
				Add addCmd = new Add();
				newCard = addCmd.executeAdd(commandFull);
				if (newCard != null) {
					FileHandler.incompleteTasks.add(newCard);
					response = "Added " + newCard.getName();
				} else {
					response = "Invalid argument";
				}
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
