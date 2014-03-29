package application;

public class CommandHandler {
	
	private FileLinker fileLink;
	private DataUI dataUI;
	
	//handlers
	private static Add addHandler;
	private static Delete deleteHandler;
	private static Search searchHandler;
	private static Mark markHandler;
	private static Edit editHandler;
	private static Undo undoHandler;
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "It appears you have typed "
			+ "something wrongly! Please try another command.";
	
	public enum COMMAND_TYPE {
		ADD, DELETE, CLEAR, EDIT, SEARCH, MARK, RESET, EXIT, INVALID, ENTER, UNDO, REDO
	}
	
	public CommandHandler() {
		addHandler = new Add();
		deleteHandler = new Delete();
		searchHandler = new Search();
		markHandler = new Mark();
		editHandler = new Edit();
		undoHandler = new Undo();
		
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
	public DataUI executeCmd(String userInput) {
		checkCmdAndPerform(userInput);	
		return dataUI;
	}

	private void checkCmdAndPerform(String userInput) {
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		String commandTypeString = tokenizedInput[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
				
		switch(commandType) {
			case ADD:
				fileLink.resetState();
				addHandler.executeAdd(userInput, fileLink, dataUI, undoHandler);
				break;
			case DELETE:
				deleteHandler.executeDelete(userInput, fileLink, dataUI, undoHandler);
				break;
			case EDIT:
				editHandler.executeEdit(userInput, fileLink, dataUI, undoHandler);
				break;
			case SEARCH:
				fileLink.resetState();
				searchHandler.executeSearch(userInput, fileLink, dataUI);
				break;
			case ENTER:
				fileLink.resetState();
				RefreshUI.executeRefresh(fileLink, dataUI);
				break;
			case MARK:
				markHandler.executeMark(userInput, fileLink, dataUI, undoHandler);
				break;
			case UNDO:
				String response = undoHandler.executeUndo(fileLink);
				RefreshUI.executeRefresh(fileLink, dataUI);
				dataUI.setFeedback(response);
				break;
			case REDO:
				undoHandler.executeRedo(fileLink);
				RefreshUI.executeRefresh(fileLink, dataUI);
				dataUI.setFeedback(null);
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
		} else if (commandTypeString.contains("exit")) {
		 	return COMMAND_TYPE.EXIT;
		}	else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public DataUI getDataUI() {
		return dataUI;
	}
}
