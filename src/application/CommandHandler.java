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
	
	private static String quitToTop = "!q";
	
	private boolean state_add;
	private boolean state_del;
	private boolean state_edit;
	private boolean state_mark;
	private boolean state_ref;
	private boolean state_search;
	
	private static final String MESSAGE_ERROR_INVALID_COMMAND = "It appears you have typed "
			+ "something wrongly! Please try another command.";
	
	public enum COMMAND_TYPE {
		ADD, DELETE, CLEAR, EDIT, SEARCH, MARK, RESET, EXIT, INVALID, ENTER
	}
	
	public CommandHandler() {
		addHandler = new Add();
		deleteHandler = new Delete();
		searchHandler = new Search();
		markHandler = new Mark();
		editHandler = new Edit();
		
		fileLink = new FileLinker();
		dataUI = new DataUI();
		
		RefreshUI.executeRefresh(fileLink, dataUI);
		
		resetStates();
	}
	
	/**
	 * checks if the program is currently handling any error or prompting states.
	 * if it is not, it will take user input as a fresh new command.
	 * @param userInput
	 * @return
	 */
	public DataUI executeCmd(String userInput) {
		if(newCommand()) {
			executeCommand(userInput);
		} else {
			if(userInput.equals(quitToTop) == false) {
				checkStatesAndPerform(userInput);
			} else {
				resetStates();
				dataUI.setFeedback(null);
			}
		}
		
		return dataUI;
	}

	/**
	 * in calling this method, this means that the program is currently in one of the error
	 * handling states. it checks for the state it is in and calls the affected handler
	 * directly
	 * @param userInput
	 */
	private void checkStatesAndPerform(String userInput) {
		boolean success;
		
		if(state_add == true) {
			fileLink.resetState();
			success = addHandler.executeAdd(userInput, fileLink, dataUI);
			
			if(success == true) {
				state_add = false;
			} else {
				state_add = true;
			}
		} else if(state_del == true) {
			success = deleteHandler.executeDelete(userInput, fileLink, dataUI);
			
			if(success == true) {
				state_del = false;
			} else {
				state_del = true;
			}
		} else if(state_mark == true) {
			/*
			success = markHandler.executeMark(userInput, fileLink, dataUI);
			
			if(success == true) {
				state_mark = false;
			} else {
				state_mark = true;
			}
			*/
		} else if(state_edit == true) {
			editHandler.checkBeforeExecuteEdit(userInput, fileLink, dataUI);
			//Edit.executeEdit(userInput, fileLink, dataToBePassedToUI);
		} else if(state_ref == true) {
			RefreshUI.executeRefresh(fileLink, dataUI);
		} else if(state_search == true) {
			fileLink.resetState();
			success = searchHandler.executeSearch(userInput, fileLink, dataUI);
			
			if(success) {
				state_search = false;
			} else {
				state_search = true;
			}
		}
	}

	private boolean newCommand() {
		if(state_add == false && state_del == false && state_mark == false && state_edit == false 
				&& state_ref == false && state_search == false)
			return true;
		
		return false;
	}

	public void executeCommand(String userInput) {
		boolean success = false;
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		String commandTypeString = tokenizedInput[0];
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
				
		switch(commandType) {
			case ADD:
				fileLink.resetState();
				success = addHandler.executeAdd(userInput, fileLink, dataUI);
				if(success == false) {
					state_add = true;
				}
				break;
			case RESET:
				//success = Reset.executeReset(tokenizedInput, fileLink, dataUI);
				break;
			case DELETE:
				success = deleteHandler.executeDelete(userInput, fileLink, dataUI);
				if(success == false) {
					state_del = true;
				}
				break;
			case EDIT:
				success = editHandler.checkBeforeExecuteEdit(userInput, fileLink, dataUI);
				if(success==false)
					state_edit = true;
					break;
			case SEARCH:
				fileLink.resetState();
				success = searchHandler.executeSearch(userInput, fileLink, dataUI);
				if(!success) {
					state_search = true;
				}
				break;
			case ENTER:
				fileLink.resetState();
				RefreshUI.executeRefresh(fileLink, dataUI);
				resetStates();
				break;
			case MARK:
				/*
				success = markHandler.executeMark(userInput, fileLink, dataUI);
				if (success == false) {
					state_mark = true;
				}
				break;
				*/
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
		
		if (commandTypeString.contains("/add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.contains("/d")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.contains("/clear")) {
		 	return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.contains("/e")) {
		 	return COMMAND_TYPE.EDIT;
		} else if (commandTypeString.contains("/s")) {
		 	return COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.contains("/mark") || commandTypeString.contains("/unmark")) {
		 	return COMMAND_TYPE.MARK;
		} else if (commandTypeString.contains("/reset")) {
			return COMMAND_TYPE.RESET;
		} else if (commandTypeString.isEmpty()) {
			return COMMAND_TYPE.ENTER;
		} else if (commandTypeString.contains("/x")) {
		 	return COMMAND_TYPE.EXIT;
		}	else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private void resetStates() {
		state_add = false;
		state_del = false;
		state_mark = false;
		state_edit = false;
		state_ref = false;
		state_search = false;
	}
	
	public DataUI getDataUI() {
		return dataUI;
	}
}
