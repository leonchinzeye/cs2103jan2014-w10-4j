import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler {

	//2 separate arraylists for handling the completed and incomplete tasks
	public static ArrayList<TaskCard> incompleteTasks;
	public static ArrayList<TaskCard> completedTasks;
	
	//2 separate files for storage. one for incomplete tasks, the other for archiving
	public static final String INCOMPLETE_TASKS_STORAGE_FILE_NAME = "incompletetasks.txt";
	public static final String COMPLETED_TASKS_STORAGE_FILE_NAME = "completedtasks.txt";
	
	public static int numberOfIncompleteTasks = 0;
	public static int numberOfCompletedTasks = 0;
	
	private static final int NUMBER_OF_WRITTEN_LINES_FOR_EACH_TASK = 2;
	
	public FileHandler() {
		loadFileDetails();
	}
	
	private void loadFileDetails() {
		openCompletedStorageFile();
		openIncompleteStorageFile();
	}

	/*
	 * When writing to the program, will instantiate a new TaskCard object with the parameters
	 * filled and then it will be added to the ArrayList of completed/incomplete tasks
	 */
	private static void openCompletedStorageFile() {
		completedTasks = new ArrayList<TaskCard>();
		
		try {
			FileReader fileRead = new FileReader(COMPLETED_TASKS_STORAGE_FILE_NAME);
			BufferedReader buffRead = new BufferedReader(fileRead);
			
			int numberOfTaskCards = Integer.parseInt(buffRead.readLine());
			numberOfCompletedTasks = numberOfTaskCards;
			
			for(int i = 0; i < numberOfTaskCards; i++) {
				TaskCard task = new TaskCard();
				ArrayList<String> taskDetails = new ArrayList<String>();
				
				taskDetails = getTaskDetailsFromFile(buffRead);
				setTaskDetailsForReading(taskDetails, task);
				
				completedTasks.add(task);
			}
			
			buffRead.close();
		} catch (FileNotFoundException ex) {
			createEmptyFile(COMPLETED_TASKS_STORAGE_FILE_NAME);
		} catch (IOException ex) {
			//throw error reading file message
		}
	}
	
	private static void openIncompleteStorageFile() {
		incompleteTasks = new ArrayList<TaskCard>();
		
		try {
			FileReader fileRead = new FileReader(INCOMPLETE_TASKS_STORAGE_FILE_NAME);
			BufferedReader buffRead = new BufferedReader(fileRead);
			
			int numberOfTaskCards = Integer.parseInt(buffRead.readLine());
			numberOfIncompleteTasks = numberOfTaskCards;
			
			for(int i = 0; i < numberOfTaskCards; i++) {
				TaskCard task = new TaskCard();
				ArrayList<String> taskDetails = new ArrayList<String>();
				
				taskDetails = getTaskDetailsFromFile(buffRead);
				setTaskDetailsForReading(taskDetails, task);
				
				incompleteTasks.add(task);
			}
			
			buffRead.close();
		} catch (FileNotFoundException ex) {
			createEmptyFile(INCOMPLETE_TASKS_STORAGE_FILE_NAME);
		} catch (IOException ex) {
			//throw error reading file message
		}
	}

	private static ArrayList<String> getTaskDetailsFromFile(
			BufferedReader buffRead) {
		ArrayList<String> taskDetails = new ArrayList<String>();
		
		try {
			for(int i = 0; i < 2; i++) {
				taskDetails.add(buffRead.readLine());
			}
			return taskDetails;
		} catch(IOException ex) {
			return null;
		}
	}

	private static void setTaskDetailsForReading(ArrayList<String> taskDetails,
			TaskCard task) {
		
		task.setName(taskDetails.get(0));
		
		String[] restOfDetails = taskDetails.get(1).split(" ");
		task.setType(restOfDetails[0]);
		
		task.setStartDate(Integer.parseInt(restOfDetails[1]));
		task.setStartMonth(Integer.parseInt(restOfDetails[2]));
		task.setStartYear(Integer.parseInt(restOfDetails[3]));
		
		task.setEndDate(Integer.parseInt(restOfDetails[4]));
		task.setEndDate(Integer.parseInt(restOfDetails[5]));
		task.setEndDate(Integer.parseInt(restOfDetails[6]));
		
		task.setStartTime(Integer.parseInt(restOfDetails[7]));
		task.setEndTime(Integer.parseInt(restOfDetails[8]));
		
		task.setFrequency(restOfDetails[9]);
		task.setPriority(Integer.parseInt(restOfDetails[10]));
	}

	private static void createEmptyFile(String fileStorageName) {
		initialiseFileDetails(fileStorageName);
		
		if(fileStorageName == INCOMPLETE_TASKS_STORAGE_FILE_NAME) {
			writeIncompleteTasksFile();
		} else {
			writeCompleteTasksFile();
		}
	}

	private static void initialiseFileDetails(String fileStorageName) {
		if(fileStorageName == INCOMPLETE_TASKS_STORAGE_FILE_NAME) {
			numberOfIncompleteTasks = 0;
			incompleteTasks = new ArrayList<TaskCard>();
		} else {
			numberOfCompletedTasks = 0;
			completedTasks = new ArrayList<TaskCard>();
		}
	}

	public static void writeCompleteTasksFile() {
		try {
			FileWriter fileWrite = new FileWriter(COMPLETED_TASKS_STORAGE_FILE_NAME);		
			BufferedWriter buffWrite = new BufferedWriter(fileWrite);
			
			buffWrite.write("" + numberOfCompletedTasks);
			buffWrite.newLine();
			
			for(int i = 0; i < numberOfCompletedTasks; i++) {
				TaskCard task = completedTasks.get(i);
				writeTaskCardDetails(buffWrite, task);
			}
			buffWrite.close();
		} catch(IOException ex) {
			//print error writing to file message
		}
	}
	
	public static void writeIncompleteTasksFile() {
		try {
			FileWriter fileWrite = new FileWriter(INCOMPLETE_TASKS_STORAGE_FILE_NAME);		
			BufferedWriter buffWrite = new BufferedWriter(fileWrite);
			
			buffWrite.write("" + numberOfIncompleteTasks);
			buffWrite.newLine();
			
			for(int i = 0; i < numberOfIncompleteTasks; i++) {
				TaskCard task = incompleteTasks.get(i);
				writeTaskCardDetails(buffWrite, task);
			}
			buffWrite.close();
		} catch(IOException ex) {
			//print error writing to file message
		}
	}

	private static void writeTaskCardDetails(BufferedWriter buffWrite,
			TaskCard task) {
		try {
			buffWrite.write("" + task.getName());
			buffWrite.newLine();
			
			String detailsToBeWritten = task.getType() + " " + task.getStartDate() + " " 
																	+ task.getStartMonth() + " " + task.getStartYear() + " "
																	+ task.getEndDate() + " " + task.getEndMonth() + " " 
																	+ task.getEndYear() + " " + task.getStartTime() + " "
																	+ task.getEndTime() + " " + task.getFrequency() + " "
																	+ task.getPriority();
			buffWrite.write(detailsToBeWritten);
			buffWrite.newLine();
		} catch(IOException ex) {
			//error writing to file message
		}	
	}
}
