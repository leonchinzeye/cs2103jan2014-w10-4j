import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is used to manipulate text in a file. It can be used to add or delete text
 * in a file. It can also display and show the current details in the file. 
 * 
 * Assumptions
 * - if the file is not present, the program would then create a new file with the name
 *   as specified in the argument
 * - if file is present, details from the file would be read and further additions/deletions
 *   would be made after reading the details of the file
 * - there are only 5 commands: add, delete, display, clear and exit
 * - any other commands would result in an error
 * - whatever text following a space after an add command would still be considered and added
 *   into the text file
 * - file will be saved after every command(other than the display command)
 * @author leon
 *
 */
public class TaskWorthy {
	
	private static final int COMMAND_ARGUMENT = 0;
	private static final int ONLY_ONE_ARGUMENT = 1;
	private static final int SPLIT_TWO_ARGUMENTS = 2;
	private static final int FIRST_ARGUMENT = 1;
	
	private static final int TYPE_EXIT = 7;
	private static final int TYPE_SEARCH = 6;
	private static final int TYPE_SORT = 5;
	private static final int TYPE_CLEAR = 4;
	private static final int TYPE_DISPLAY = 3;
	private static final int TYPE_DELETE = 2;
	private static final int TYPE_ADD = 1;
	
	private static final String COMMAND_PROMPT = "command: ";
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %s is ready for use";
	private static final String MESSAGE_NO_TASK_ENTERED = "no task entered";
	private static final String MESSAGE_ERROR_WRITING_TO_FILE = "error writing to file %s";
	private static final String MESSAGE_CLEAR_FILE_SUCCESSFUL = "all content deleted from %s";
	private static final String MESSAGE_ERROR_UNRECOGNISABLE_COMMAND = "command not recognised."
			+ " please re-enter command";
	private static final String MESSAGE_ERROR_NOT_NUMBER = "please enter a number";
	private static final String MESSAGE_ERROR_NO_SUCH_TASK_FOUND = "please enter a valid task number";
	private static final String MESSAGE_ERROR_INVALID_TASK_NUMBER = "there are only %d task(s)."
			+ " task %d can't be found";
	private static final String MESSAGE_ERROR_READING_FILE = "error reading file %s";
	private static final String MESSAGE_INVALID_ARGUMENT = "invalid argument";
	private static final String MESSAGE_ADD_SUCCESSFUL = "added to %s: \"%s\"";
	private static final String MESSAGE_DELETE_SUCCESSFUL = "deleted from %s: \"%s\"";
	private static final String MESSAGE_EMPTY_FILE = "%s is empty";
	private static final String MESSAGE_SORT_UNSUCCESSFUL = "%s is empty. there is nothing "
			+ "to be sorted";
	private static final String MESSAGE_SORT_SUCCESSFUL = "%s has been sorted";
	private static final String SEARCH_LINE_WITH_WORD = "%d. %s";
	private static final String MESSAGE_UNABLE_TO_FIND_WORD = "\"%s\" was not found";
	private static final String MESSAGE_ERROR_SEARCH_EMPTY_FILE =
	"%s is empty. there is nothing to be searched";
	private static final String MESSAGE_ERROR_NO_SEARCH_OPTION_ENTERED = "no search option entered";

	
	private static ArrayList<String> fileDetails = new ArrayList<String>();
	private static String fileName = null;
	private static int fileSize = 0;
	private static HashMap<String, Integer> commandTable = new HashMap<String, Integer>();
	public enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, SORT, SEARCH, EXIT, INVALID
	};
	
	private static boolean terminationStatus = false;
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		String response = "";
		CommandHandler ch = new CommandHandler();
		response = ch.executeCommand("/addf CS2103 V0.5");
		System.out.println(response);
		
		while(terminationStatus == false) {
			String userInput = scan.nextLine();
			
			if(userInput.isEmpty()) {
				//no input detected, user just pressed enter only
			} else {
				ch.executeCommand(userInput);
			}
			
			terminationStatus = ch.terminateStatus;
		}
		
		/*
		checkValidArgument(args);
		
		openFile(args);
		
		printWelcomeMessage();
		
		initialiseCommands();
		
		letUserEnterTillErrorOrExit();
		*/
		return;
	}
	
	private static void checkValidArgument(String[] args) {
		if(args.length != ONLY_ONE_ARGUMENT) {
			exitWithErrorMessage(MESSAGE_INVALID_ARGUMENT);
		}
	}


	public static void openFile(String[] args) {
		fileName = args[0];
		String lineRead = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader buffReader = new BufferedReader(fileReader);
			
			while((lineRead = buffReader.readLine()) != null) {
				fileDetails.add(lineRead);
			}
			
			fileSize = fileDetails.size();
			buffReader.close();	
		} catch(FileNotFoundException ex) {
				createFile();		
		} catch(IOException ex) {
				System.out.println(String.format(MESSAGE_ERROR_READING_FILE, fileName));
		}
	}
	
	private static void createFile() {
		initialiseFileDetails();
		saveFile();
	}

	private static void initialiseFileDetails() {
		fileDetails = new ArrayList<String>();
		fileSize = 0;
	}
	
	public static void initialiseCommands() {
		commandTable.put("add", TYPE_ADD);
		commandTable.put("delete", TYPE_DELETE);
		commandTable.put("display", TYPE_DISPLAY);
		commandTable.put("clear", TYPE_CLEAR);
		commandTable.put("sort", TYPE_SORT);
		commandTable.put("search", TYPE_SEARCH);
		commandTable.put("exit", TYPE_EXIT);
	}
	
	private static String executeAddFunction(String[] individualWords) {
		String checkingMessage;
		
		checkingMessage = addToFileDetails(individualWords);
		saveFile();
		
		return checkingMessage;
	}
	
	private static String executeDeleteFunction(String[] individualWords) {
		String checkingMessage;
		
		checkingMessage = deleteFromFileDetails(individualWords);
		saveFile();
		
		return checkingMessage;
	}
	
	private static String executeDisplayFunction() {
		String checkingMessage = "";
		
		if(fileSize == 0) {
			System.out.println(String.format(MESSAGE_EMPTY_FILE, fileName));
			checkingMessage = String.format(MESSAGE_EMPTY_FILE, fileName);
		} else {
		int count = 1;
			for(int i = 0; i < fileSize; i++) {
				System.out.println(count + ". " + fileDetails.get(i));
				checkingMessage += fileDetails.get(i);
				count++;
			}
		}
		return checkingMessage;
	}
	
	private static String executeClearFunction() {
		String checkingMessage;
		
		checkingMessage = clearFileDetails();
		saveFile();
		
		return checkingMessage;
	}
	
	private static String executeSortFunction() {
		String checkingMessage;
		
		checkingMessage = sortFileDetails();
		saveFile();
		
		return checkingMessage;
	}
	
	private static String executeSearchFunction(String[] individualWords) {
		String checkingMessage;
		
		checkingMessage = searchFileDetails(individualWords);
		
		return checkingMessage;
	}
	
	private static void executeExitFunction() {
		saveFile();	
		System.exit(0);
	}

	private static String addToFileDetails(String[] individualWords) {
		String commandToBeAdded = null;
		String checkingMessage = null;
		int commandLength = individualWords.length;
		
		if(commandLength == ONLY_ONE_ARGUMENT) {
			checkingMessage = MESSAGE_NO_TASK_ENTERED;
			System.out.println(MESSAGE_NO_TASK_ENTERED);
		} else {
			commandToBeAdded = individualWords[FIRST_ARGUMENT];
			fileDetails.add(commandToBeAdded);
			fileSize++;
			System.out.println(String.format(MESSAGE_ADD_SUCCESSFUL, fileName, commandToBeAdded));
			checkingMessage = String.format(MESSAGE_ADD_SUCCESSFUL, fileName, commandToBeAdded);
		}
		
		return checkingMessage;
	}

	private static String deleteFromFileDetails(String[] individualWords) {
		boolean notNumber = false;
		int lineToBeDeleted = 0;
		String checkingMessage;
		
		try {
			lineToBeDeleted = (int)Integer.parseInt(individualWords[1]);
		} catch(Exception ex) {
			notNumber = true;
		}
		
		if(notNumber == true) {
			checkingMessage = MESSAGE_ERROR_NOT_NUMBER;
			printErrorMessageOnly(MESSAGE_ERROR_NOT_NUMBER);
		} else if(lineToBeDeleted > fileSize) {
			checkingMessage = String.format(MESSAGE_ERROR_INVALID_TASK_NUMBER, 
					fileSize, lineToBeDeleted);
			printErrorMessageOnly(String.format(MESSAGE_ERROR_INVALID_TASK_NUMBER, 
					fileSize, lineToBeDeleted));
		} else if (lineToBeDeleted <= 0) {
			checkingMessage = MESSAGE_ERROR_NO_SUCH_TASK_FOUND;
			printErrorMessageOnly(MESSAGE_ERROR_NO_SUCH_TASK_FOUND);
		} else {
			String commandToBeDeleted = fileDetails.get(lineToBeDeleted - 1);	
			
			fileDetails.remove(lineToBeDeleted - 1);
			fileSize--;
			
			checkingMessage = String.format(MESSAGE_DELETE_SUCCESSFUL, fileName, commandToBeDeleted);
			System.out.println(String.format(MESSAGE_DELETE_SUCCESSFUL, fileName, commandToBeDeleted));
		}
		
		return checkingMessage;
	}

	private static String clearFileDetails() {
		String checkingMessage;
		
		fileDetails.clear();
		fileSize = 0;
		
		checkingMessage = String.format(MESSAGE_CLEAR_FILE_SUCCESSFUL, fileName);
		System.out.println(String.format(MESSAGE_CLEAR_FILE_SUCCESSFUL, fileName));
		
		return checkingMessage;
	}
	
	private static String sortFileDetails() {
		String checkingMessage;
		
		if(fileSize == 0) {
			System.out.println(String.format(MESSAGE_SORT_UNSUCCESSFUL, fileName));
			checkingMessage = String.format(MESSAGE_SORT_UNSUCCESSFUL, fileName);
		} else {
			Collections.sort(fileDetails, new SortIgnoreCase());
			System.out.println(String.format(MESSAGE_SORT_SUCCESSFUL, fileName));
			checkingMessage = String.format(MESSAGE_SORT_SUCCESSFUL, fileName);
		}
		
		return checkingMessage;
	}
	
	private static String searchFileDetails(String[] individualWords) {
		String checkingMessage = "";
		ArrayList<String> linesWhichContainWord = new ArrayList<String>();
		int commandLength = individualWords.length;
		
		if(commandLength == ONLY_ONE_ARGUMENT) {
			checkingMessage = MESSAGE_ERROR_NO_SEARCH_OPTION_ENTERED;
			System.out.println(MESSAGE_ERROR_NO_SEARCH_OPTION_ENTERED);
		} else if(fileSize == 0) {
			checkingMessage = String.format(MESSAGE_ERROR_SEARCH_EMPTY_FILE, fileName);
			System.out.println(String.format(MESSAGE_ERROR_SEARCH_EMPTY_FILE, fileName));
		} else {
			String wordToSearch = individualWords[FIRST_ARGUMENT];
			linesWhichContainWord = findWordInFile(wordToSearch);
			
			checkingMessage = checkIfSearchFailElsePrint(linesWhichContainWord, wordToSearch);
		}
		
		return checkingMessage;
	}

	private static String checkIfSearchFailElsePrint(ArrayList<String> linesWhichContainWord,
			String wordToSearch) {
		String checkingMessage = "";
		
		if(linesWhichContainWord == null) {
			checkingMessage = String.format(MESSAGE_UNABLE_TO_FIND_WORD, wordToSearch);
			System.out.println(String.format(MESSAGE_UNABLE_TO_FIND_WORD, wordToSearch));
		} else {
			int count = 1;
			
			for(int i = 0; i < linesWhichContainWord.size(); i++) {
				String lineWithWord = linesWhichContainWord.get(i);
				System.out.println(String.format(SEARCH_LINE_WITH_WORD, count, lineWithWord));
				checkingMessage += lineWithWord;
				count++;
			}
		}
		return checkingMessage;
	}
	
	private static ArrayList<String> findWordInFile(String wordToSearch) {
		ArrayList<String> linesWhichContainWord = new ArrayList<String>();
		wordToSearch = wordToSearch.toLowerCase();
				
		for(int i = 0; i < fileSize; i++) {
			String lineToCheck = fileDetails.get(i);
			lineToCheck = lineToCheck.toLowerCase();
			
			if(lineToCheck.contains(wordToSearch)) {
				linesWhichContainWord.add(fileDetails.get(i));
			}
		}
		
		if(linesWhichContainWord.isEmpty())
			return null;
		else
			return linesWhichContainWord;
	}

	private static void letUserEnterTillErrorOrExit() {
		Scanner scanInput = new Scanner(System.in);
		String commandLine = null;
		String[] individualWords = null;
		
		while(true) {
			System.out.print(COMMAND_PROMPT);
			commandLine = scanInput.nextLine();
			individualWords = commandLine.split(" ", SPLIT_TWO_ARGUMENTS);
			
			if(commandTable.containsKey(individualWords[0]) == false) {
				printErrorMessageOnly(MESSAGE_ERROR_UNRECOGNISABLE_COMMAND);
				continue;
			} else {
				identifyCommand(individualWords);
			}	
		}	
	}

	public static String identifyCommand(String[] individualWords) {
		int getCommandKey = commandTable.get(individualWords[COMMAND_ARGUMENT]);
		String checkingMessage = "";
		
		if(getCommandKey == TYPE_ADD) {
			checkingMessage = executeAddFunction(individualWords);
		} else if(getCommandKey == TYPE_DELETE) {
			checkingMessage = executeDeleteFunction(individualWords);
		} else if(getCommandKey == TYPE_DISPLAY) {
			checkingMessage = executeDisplayFunction();
		} else if(getCommandKey == TYPE_CLEAR) {
			checkingMessage = executeClearFunction();
		} else if(getCommandKey == TYPE_SORT) {
			checkingMessage = executeSortFunction();
		} else if(getCommandKey == TYPE_SEARCH) {
			checkingMessage = executeSearchFunction(individualWords);
		} else {
			executeExitFunction();
		}
		
		return checkingMessage;
	}
	
	
	private static void saveFile() {
		try {
			FileWriter fileWriter = new FileWriter(fileName);		
			BufferedWriter buffWriter = new BufferedWriter(fileWriter);
			
			for(int i = 0; i < fileSize; i++) {
				buffWriter.write(fileDetails.get(i));
				buffWriter.newLine();
			}
			buffWriter.close();
		} catch(IOException ex) {
			System.out.println(String.format(MESSAGE_ERROR_WRITING_TO_FILE, fileName));
		}
	}
	
	private static void printWelcomeMessage() {
		System.out.println(String.format(WELCOME_MESSAGE, fileName));
	}

	private static void printErrorMessageOnly(String error) {
		System.out.println(error);
	}
	
	private static void exitWithErrorMessage(String error) {
		System.out.println(error);
		System.exit(0);
	}

	public static class SortIgnoreCase implements Comparator<Object> {
    public int compare(Object o1, Object o2) {
        String s1 = (String) o1;
        String s2 = (String) o2;
        return s1.toLowerCase().compareTo(s2.toLowerCase());
    }
	}
}

