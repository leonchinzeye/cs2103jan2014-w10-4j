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
		try {
			FileReader fileRead = new FileReader(COMPLETED_TASKS_STORAGE_FILE_NAME);
			BufferedReader buffRead = new BufferedReader(fileRead);
			
			int numberOfTaskCards = Integer.parseInt(buffRead.readLine());
			
			TaskCard task = new TaskCard();
			ArrayList<String> taskDetails = new ArrayList<String>();
			
			for(int i = 0; i < numberOfTaskCards; i++) {
				taskDetails = getTaskDetailsFromFile(buffRead);
				
				String name = buffRead.readLine();
				String Type = buffRead.readLine();
				
				int startDate = Integer.parseInt(buffRead.readLine());
				int startMonth = Integer.parseInt(buffRead.readLine());
				int startYear = Integer.parseInt(buffRead.readLine());
				
				int endDate = Integer.parseInt(buffRead.readLine());
				int endMonth = Integer.parseInt(buffRead.readLine());
				int endYear = Integer.parseInt(buffRead.readLine());
				
				int startTime = Integer.parseInt(buffRead.readLine());
				int endTime = Integer.parseInt(buffRead.readLine());
				
				int priority = Integer.parseInt(buffRead.readLine());
			}
		} catch (FileNotFoundException ex) {
			createFile(COMPLETED_TASKS_STORAGE_FILE_NAME);
		} catch (IOException ex) {
			
		}
	}
	
	private static ArrayList<String> getTaskDetailsFromFile(
			BufferedReader buffRead) {
		ArrayList<String> taskDetails = new ArrayList<String>();
		
		
		return null;
	}

	private static void openIncompleteStorageFile() {
		
	}
	
	private static void createFile(String completedTasksStorageFileName) {
		// TODO Auto-generated method stub
		
	}

	public static void saveFile() {
		
	}
}
