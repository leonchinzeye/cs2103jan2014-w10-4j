import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler {

	//2 separate arraylists for handling the completed and incomplete tasks
	private static ArrayList<TaskCard> incompleteTasks;
	private static ArrayList<TaskCard> completedTasks;
	
	//2 separate files for storage. one for incomplete tasks, the other for archiving
	private static final String INCOMPLETE_TASKS_STORAGE_FILE_NAME = "incompletetasks.txt";
	private static final String COMPLETED_TASKS_STORAGE_FILE_NAME = "completedtasks.txt";
	
	private static int numberOfIncompleteTasks = 0;
	private static int numberOfCompletedTasks = 0;
	
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
			
			for(int i = 0; i < numberOfTaskCards; i++) {
				TaskCard task = new TaskCard();
				ArrayList<String> taskDetails = new ArrayList<String>();
				
				taskDetails = getTaskDetailsFromFile(buffRead);
				setTaskDetails(taskDetails, task);
				
				completedTasks.add(task);
			}
			
			buffRead.close();
		} catch (FileNotFoundException ex) {
			createFile(COMPLETED_TASKS_STORAGE_FILE_NAME);
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

	private static void setTaskDetails(ArrayList<String> taskDetails,
			TaskCard task) {
		
		task.setName(taskDetails.get(1));
		task.setType(taskDetails.get(2));
		
		task.setStartDate(Integer.parseInt(taskDetails.get(3)));
		task.setStartMonth(Integer.parseInt(taskDetails.get(4)));
		task.setStartYear(Integer.parseInt(taskDetails.get(5)));
		
		task.setEndDate(Integer.parseInt(taskDetails.get(6)));
		task.setEndMonth(Integer.parseInt(taskDetails.get(7)));
		task.setEndYear(Integer.parseInt(taskDetails.get(8)));
		
		task.setStartTime(Integer.parseInt(taskDetails.get(9)));
		task.setEndTime(Integer.parseInt(taskDetails.get(10)));
		
		task.setFrequency(taskDetails.get(11));
		task.setPriority(Integer.parseInt(taskDetails.get(12)));
	}

	private static void openIncompleteStorageFile() {
		
	}
	
	private static void createFile(String completedTasksStorageFileName) {
		// TODO Auto-generated method stub
		
	}

	public static void saveFile() {
		
	}
}
