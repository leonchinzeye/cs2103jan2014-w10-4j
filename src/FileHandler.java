import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler {

	//2 separate arraylists for handling the completed and incomplete tasks
	private static ArrayList<TaskCards> incompleteTasks;
	private static ArrayList<TaskCards> completedTasks;
	
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
		} catch (FileNotFoundException ex) {
			createFile(COMPLETED_TASKS_STORAGE_FILE_NAME);
		} catch (IOException ex) {
			
		}
	}
	
	private static void createFile(String completedTasksStorageFileName) {
		// TODO Auto-generated method stub
		
	}

	private static void openIncompleteStorageFile() {
		
	}
	
	public static void saveFile() {
		
	}
}
