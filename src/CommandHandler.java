public class CommandHandler {
	static FileHandler temp = new FileHandler();
	
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, SORT, SEARCH, EXIT, INVALID
	};
	
	public static String executeCommand(String cmd_full) {
		String response = "";
		String[] cmd = cmd_full.split(" ", 2);
		String commandTypeString = cmd[1];

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
			
		switch(commandType) {
			case ADD:
				response = executeAddCmd(cmd);
				break;
			case DISPLAY:
				response = display();
				break;
			case CLEAR:
				response = clear();
				break;
			case DELETE:
				response = executeDelCmd(cmd);
				break;
			case SORT:
				response = sort();
				break;
			case SEARCH:
				response = executeSearchCmd(cmd);
				break;
			case INVALID:
				response = MESSAGE_ERROR_UNRECOGNISABLE_COMMAND;
				break;
			case EXIT:
				System.exit(0);
				break;
			default:
				response = MESSAGE_ERROR_UNRECOGNISABLE_COMMAND;
				break;
		}
		temp.saveFile();
		return response;
	}
	
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("Command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("/add") || 
			commandTypeString.equalsIgnoreCase("/a")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("/display") || 
				   commandTypeString.equalsIgnoreCase("/dis")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("/delete") || 
				   commandTypeString.equalsIgnoreCase("/del")) {
			 	return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("/clear")) {
		 	return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("/sort")) {
		 	return COMMAND_TYPE.SORT;
		} else if (commandTypeString.equalsIgnoreCase("/search")) {
		 	return COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("/exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
}
