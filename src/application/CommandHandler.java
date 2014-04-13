//@author A0097304E
package application;

/**
 * This class works as a command handler.
 * It parses in the first input and checks if it is a valid command.
 * If the command is valid, then CommandHandler will pass the inputs
 * into the respective functions based on the commands that the user
 * has entered
 */
public class CommandHandler {
	
	private static FileLinker fileLink;
	private static DataUI dataUI;
	
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
		ADD, DELETE, CLEAR, EDIT, SEARCH, MARK, RESET, EXIT, INVALID, ENTER, UNDO, REDO, HELP, VIEW, THEME, HIGHLIGHT
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
	public DataUI executeCmd(String userInput, int tableNo) {	
		checkCmdAndPerform(userInput, tableNo);
		return dataUI;
	}

	private void checkCmdAndPerform(String userInput, int tableNo) {
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
				editHandler.executeEdit(userInput, fileLink, dataUI, tableNo, undoHandler, dateFormats);
				break;
			case SEARCH:
				fileLink.resetState();
				searchHandler.executeSearch(userInput, fileLink, dataUI, dateFormats);
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
			case THEME:
				if (tokenizedInput[1].equalsIgnoreCase("Jedi")) {
					dataUI.setFeedback("May the Force be with you.");
				} else if (tokenizedInput[1].equalsIgnoreCase("Sith")) {
					dataUI.setFeedback("Only a Sith deals in absolutes.");
				} else if (tokenizedInput[1].equalsIgnoreCase("Australia")) {
					dataUI.setFeedback("G'day mate!");
				} else if (tokenizedInput[1].equalsIgnoreCase("Italy")) {
					dataUI.setFeedback("Mamma mia!");
				} else {
					dataUI.setFeedback("We don't have that theme!");
				}
				break;
			case HIGHLIGHT:
				RefreshUI.executeRefresh(fileLink, dataUI);
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
		} else if(commandTypeString.equals("theme")) {
			return COMMAND_TYPE.THEME;
		} else if (commandTypeString.equals("/x")) {
		 	return COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equals("highlightexpiredandongoingrows")) {
			return COMMAND_TYPE.HIGHLIGHT;
		}	else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public DataUI getDataUI() {
		return dataUI;
	}
}
