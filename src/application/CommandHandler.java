package application;

public class CommandHandler {
	
	private FileLinker fileLink;
	private DataUI dataUI;
	
	//handlers
	private static Delete deleteHandler;
	private static Search searchHandler;
	private static Mark markHandler;
	private static Edit editHandler;
	private static Undo undoHandler;
	private static Add addHandler;
	private static DateAndTimeFormats dateFormats;
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "It appears you have typed "
			+ "something wrongly! Please try another command.";
	
	public enum COMMAND_TYPE {
		ADD, DELETE, CLEAR, EDIT, SEARCH, MARK, RESET, EXIT, INVALID, ENTER, UNDO, REDO, HELP, VIEW
	}
	
	public CommandHandler() {
		addHandler = new Add();
		deleteHandler = new Delete();
		searchHandler = new Search();
		markHandler = new Mark();
		editHandler = new Edit();
		undoHandler = new Undo();
		dateFormats = new DateAndTimeFormats();
		
		fileLink = new FileLinker();
		dataUI = new DataUI();
		
		RefreshUI.executeRefresh(fileLink, dataUI);
	}
	
	/**
	 * checks if the program is currently handling any error or prompting states.
	 * if it is not, it will take user input as a fresh new command.
	 * @param userInput
	 * @return
	 */
	public DataUI executeCmd(String userInput, Integer tableNo) {
		checkCmdAndPerform(userInput, tableNo);	
		return dataUI;
	}

	private void checkCmdAndPerform(String userInput, Integer tableNo) {
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		String commandTypeString = tokenizedInput[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		
		String response = "";
		switch(commandType) {
			case ADD:
				fileLink.resetState();
				addHandler.executeAdd(userInput, fileLink, dataUI, undoHandler, dateFormats);
				break;
			case DELETE:
				deleteHandler.executeDelete(userInput, fileLink, dataUI, tableNo, undoHandler);
				break;
			case EDIT:
				editHandler.executeEdit(userInput, fileLink, dataUI, tableNo, undoHandler);
				break;
			case SEARCH:
				fileLink.resetState();
				searchHandler.executeSearch(userInput, fileLink, dataUI);
				break;
			case ENTER:
				fileLink.resetState();
				RefreshUI.executeRefresh(fileLink, dataUI);
				dataUI.setFeedback("Read me!");
				break;
			case MARK:
				markHandler.executeMark(userInput, fileLink, dataUI, tableNo, undoHandler);
				break;
			case UNDO:
				response = undoHandler.executeUndo(fileLink);
				RefreshUI.executeRefresh(fileLink, dataUI);
				dataUI.setFeedback(response);
				break;
			case REDO:
				response = undoHandler.executeRedo(fileLink);
				RefreshUI.executeRefresh(fileLink, dataUI);
				dataUI.setFeedback(response);
				break;
			case HELP:
				dataUI.setFeedback("This will return responses to you based on your commands.");
				break;
			case VIEW:
				dataUI.setFeedback("Read me!");
				break;
			case INVALID:
				dataUI.setFeedback(MESSAGE_ERROR_INVALID_COMMAND);
				break;
			case EXIT:
				System.exit(0);
				break;
			default:
				break;
		}
	}
	
	private COMMAND_TYPE determineCommandType(String commandTypeString) {
		commandTypeString = commandTypeString.toLowerCase();
		
		if (commandTypeString.contains("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.contains("del")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.contains("edit")) {
		 	return COMMAND_TYPE.EDIT;
		} else if (commandTypeString.contains("search")) {
		 	return COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.contains("mark")) {
		 	return COMMAND_TYPE.MARK;
		} else if (commandTypeString.isEmpty()) {
			return COMMAND_TYPE.ENTER;
		} else if(commandTypeString.equals("undo")) {
			return COMMAND_TYPE.UNDO;
		} else if(commandTypeString.equals("redo")) {
			return COMMAND_TYPE.REDO;
		} else if(commandTypeString.equals("help")) {
			return COMMAND_TYPE.HELP;
		} else if(commandTypeString.equals("view")) {
			return COMMAND_TYPE.VIEW;
		} else if (commandTypeString.equals("/x")) {
		 	return COMMAND_TYPE.EXIT;
		}	else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public DataUI getDataUI() {
		return dataUI;
	}
}
