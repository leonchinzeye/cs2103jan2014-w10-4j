import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler {

	//2 separate arraylists for handling the completed and uncompleted tasks
	private static ArrayList<TaskCards> uncompletedTasks;
	private static ArrayList<TaskCards> completedTasks;
	
	//2 separate files for storage. one for uncompleted tasks, the other for archiving
	private static String uncompletedTasksStorageFileName = "uncompletedTasks.txt";
	private static String completedTasksStorageFileName = "completedTasks.txt";
	
	private static int numberOfUncompletedTasks = 0;
	private static int numberOfCompletedTasks = 0;
	
	public FileHandler() {
		loadFileDetails();
	}
	
	private void loadFileDetails() {
		openCompletedStorageFile();
		openUncompletedStorageFile();
	}

	/*
	 * When writing to the program, will instantiate a new TaskCard object with the parameters
	 * filled and then it will be added to the ArrayList of completed/incomplete tasks
	 */
	private static void openCompletedStorageFile() {
		
	}
	
	private static void openUncompletedStorageFile() {
		
	}
	
	public static void saveFile() {
		
	}
}
