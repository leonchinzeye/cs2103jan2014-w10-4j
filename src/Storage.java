import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Storage {

	//2 separate files for storage. one for incomplete tasks, the other for archiving
	public static final String INCOMPLETE_TASKS_STORAGE_FILE_NAME = "incompletetasks.txt";
	public static final String COMPLETED_TASKS_STORAGE_FILE_NAME = "completedtasks.txt";
	
	private static final int NUMBER_OF_WRITTEN_LINES_FOR_EACH_TASK = 2;
	
	public static int numberOfIncompleteTasks = 0;
	public static int numberOfCompletedTasks = 0;
	
	private static Calendar startDay = new GregorianCalendar();
	private static Calendar endDay = new GregorianCalendar();
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private static int startDate = 0;
	private static int startMonth = 0;
	private static int startYear = 0;
	private static int startHour = 0;
	private static int startMinute = 0;
	
	private static int endDate = 0;
	private static int endMonth = 0;
	private static int endYear = 0;
	private static int endHour = 0;
	private static int endMinute = 0;
	
	public Storage() {
		loadFileDetails();
	}
	
	public static void loadFileDetails() {
		openCompletedStorageFile();
		openIncompleteStorageFile();
	}

	/*
	 * When writing to the program, will instantiate a new TaskCard object with the parameters
	 * filled and then it will be added to the ArrayList of completed/incomplete tasks
	 */
	public static ArrayList<TaskCard> openCompletedStorageFile() {
		ArrayList<TaskCard> completedTasks = new ArrayList<TaskCard>();
		
		try {
			FileReader fileRead = new FileReader(COMPLETED_TASKS_STORAGE_FILE_NAME);
			BufferedReader buffRead = new BufferedReader(fileRead);
			
			int numberOfTaskCards = Integer.parseInt(buffRead.readLine());
			
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
		return completedTasks;
	}
	
	public static ArrayList<TaskCard> openIncompleteStorageFile() {
		ArrayList<TaskCard> incompleteTasks = new ArrayList<TaskCard>();
		
		try {
			FileReader fileRead = new FileReader(INCOMPLETE_TASKS_STORAGE_FILE_NAME);
			BufferedReader buffRead = new BufferedReader(fileRead);
			
			int numberOfTaskCards = Integer.parseInt(buffRead.readLine());
			
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
		return incompleteTasks;
	}

	private static ArrayList<String> getTaskDetailsFromFile(
			BufferedReader buffRead) {
		ArrayList<String> taskDetails = new ArrayList<String>();
		
		try {
			for(int i = 0; i < NUMBER_OF_WRITTEN_LINES_FOR_EACH_TASK; i++) {
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
		task.setType(restOfDetails[0]); //Type of task/event
		
		String[] startDateArray = restOfDetails[1].split("/");
		String[] startTimeArray = restOfDetails[2].split(":");
		
		String[] endDateArray = restOfDetails[3].split("/");
		String[] endTimeArray = restOfDetails[4].split(":");
		
		startDate = Integer.parseInt(startDateArray[0]);
		startMonth = Integer.parseInt(startDateArray[1]) - 1;
		startYear = Integer.parseInt(startDateArray[2]);
		startHour = Integer.parseInt(startTimeArray[0]);
		startMinute = Integer.parseInt(startTimeArray[1]);
		
		endDate = Integer.parseInt(endDateArray[0]);
		endMonth = Integer.parseInt(endDateArray[1]) - 1;
		endYear = Integer.parseInt(endDateArray[2]);
		endHour = Integer.parseInt(endTimeArray[0]);
		endMinute = Integer.parseInt(endTimeArray[1]);
		
		startDay = Calendar.getInstance(); //need to create a new Calendar instance
		endDay = Calendar.getInstance();
		startDay.set(startYear, startMonth, startDate, startHour, startMinute);
		endDay.set(endYear, endMonth, endDate, endHour, endMinute);
		
		task.setStartDay(startDay);
		task.setEndDay(endDay);
		
		task.setFrequency(restOfDetails[5]);
		task.setPriority(Integer.parseInt(restOfDetails[6]));
	}

	/*
	private static void createEmptyFile(String fileStorageName) {
		ArrayList<TaskCard> emptyArrayList = new ArrayList<TaskCard>();
		
		if(fileStorageName == INCOMPLETE_TASKS_STORAGE_FILE_NAME) {
			writeIncompleteTasksFile(emptyArrayList);
		} else {
			writeCompleteTasksFile(emptyArrayList);
		}
	}
	*/
	
	/*	public static void initialiseFileDetails(String fileStorageName) {
		if(fileStorageName == INCOMPLETE_TASKS_STORAGE_FILE_NAME) {
			numberOfIncompleteTasks = 0;
			incompleteTasks = new ArrayList<TaskCard>();
		} else {
			numberOfCompletedTasks = 0;
			completedTasks = new ArrayList<TaskCard>();
		}
	}*/
	
	public static void writeCompleteTasksFile(ArrayList<TaskCard> completedTasks, int numberOfCompletedTasks) {
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
	
	public static void writeIncompleteTasksFile(ArrayList<TaskCard> incompleteTasks, int numberOfIncompleteTasks) {
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
			
			String detailsToBeWritten = task.getType() + " " + dateString.format(task.getStartDay().getTime()) + 
					" " + dateString.format(task.getEndDay().getTime()) + " " + task.getFrequency() + " " + task.getPriority();
			
			buffWrite.write(detailsToBeWritten);
			buffWrite.newLine();
		} catch(IOException ex) {
			//error writing to file message
		}	
	}
}
