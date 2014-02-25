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
	private static final String INCOMPLETE_TASKS_STORAGE_FILE_NAME = "incompletetasks.txt";
	private static final String COMPLETED_TASKS_STORAGE_FILE_NAME = "completedtasks.txt";
	
	public static int numberOfIncompleteTasks = 0;
	public static int numberOfCompletedTasks = 0;
	
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
			createFile(COMPLETED_TASKS_STORAGE_FILE_NAME);
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
			createFile(INCOMPLETE_TASKS_STORAGE_FILE_NAME);
		} catch (IOException ex) {
			//throw error reading file message
		}
	}

	private static ArrayList<String> getTaskDetailsFromFile(
			BufferedReader buffRead) {
		ArrayList<String> taskDetails = new ArrayList<String>();
		
		try {
			for(int i = 0; i < 12; i++) {
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
		task.setType(taskDetails.get(1));
		
		task.setStartDate(Integer.parseInt(taskDetails.get(2)));
		task.setStartMonth(Integer.parseInt(taskDetails.get(3)));
		task.setStartYear(Integer.parseInt(taskDetails.get(4)));
		
		task.setEndDate(Integer.parseInt(taskDetails.get(5)));
		task.setEndMonth(Integer.parseInt(taskDetails.get(6)));
		task.setEndYear(Integer.parseInt(taskDetails.get(7)));
		
		task.setStartTime(Integer.parseInt(taskDetails.get(8)));
		task.setEndTime(Integer.parseInt(taskDetails.get(9)));
		
		task.setFrequency(taskDetails.get(10));
		task.setPriority(Integer.parseInt(taskDetails.get(11)));
	}

	private static void createFile(String completedTasksStorageFileName) {
		
		
	}

	public static void writeCompleteTasksFile() {
		try {
			FileWriter fileWrite = new FileWriter(COMPLETED_TASKS_STORAGE_FILE_NAME);		
			BufferedWriter buffWrite = new BufferedWriter(fileWrite);
			
			buffWrite.write(numberOfCompletedTasks);
			
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
