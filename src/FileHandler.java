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

public class FileHandler {

	//2 separate files for storage. one for incomplete tasks, the other for archiving
	public static final String INCOMPLETE_TASKS_STORAGE_FILE_NAME = "incompletetasks.txt";
	public static final String COMPLETED_TASKS_STORAGE_FILE_NAME = "completedtasks.txt";
	
	private static final int NUMBER_OF_WRITTEN_LINES_FOR_EACH_TASK = 2;
	
	//2 separate arraylists for handling the completed and incomplete tasks
	public static ArrayList<TaskCard> incompleteTasks;
	public static ArrayList<TaskCard> completedTasks;
	
	public static int numberOfIncompleteTasks = 0;
	public static int numberOfCompletedTasks = 0;
	
	private static Calendar startDay = new GregorianCalendar();
	private static Calendar endDay = new GregorianCalendar();
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
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
	
	public FileHandler() {
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
		
		startDay.set(startYear, startMonth, startDate, startHour, startMinute);
		endDay.set(endYear, endMonth, endDate, endHour, endMinute);
		
		/*task.setStartDate(Integer.parseInt(restOfDetails[1]));
		task.setStartMonth(Integer.parseInt(restOfDetails[2]));
		task.setStartYear(Integer.parseInt(restOfDetails[3]));
		
		task.setEndDate(Integer.parseInt(restOfDetails[4]));
		task.setEndDate(Integer.parseInt(restOfDetails[5]));
		task.setEndDate(Integer.parseInt(restOfDetails[6]));
		
		task.setStartTime(Integer.parseInt(restOfDetails[7]));
		task.setEndTime(Integer.parseInt(restOfDetails[8]));*/
		
		task.setFrequency(restOfDetails[5]);
		task.setPriority(Integer.parseInt(restOfDetails[6]));
	}

	private static void createEmptyFile(String fileStorageName) {
		initialiseFileDetails(fileStorageName);
		
		if(fileStorageName == INCOMPLETE_TASKS_STORAGE_FILE_NAME) {
			writeIncompleteTasksFile();
		} else {
			writeCompleteTasksFile();
		}
	}

	public static void initialiseFileDetails(String fileStorageName) {
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
			
			/*String detailsToBeWritten = task.getType() + " " + task.getStartDate() + " " 
																	+ task.getStartMonth() + " " + task.getStartYear() + " "
																	+ task.getEndDate() + " " + task.getEndMonth() + " " 
																	+ task.getEndYear() + " " + task.getStartTime() + " "
																	+ task.getEndTime() + " " + task.getFrequency() + " "
																	+ task.getPriority();*/
			String detailsToBeWritten = task.getType() + " " + dateFormat.format(task.getStartDay().getTime()) + 
					" " + dateFormat.format(task.getEndDay().getTime()) + " " + task.getFrequency() + " " + task.getPriority();
			
			buffWrite.write(detailsToBeWritten);
			buffWrite.newLine();
		} catch(IOException ex) {
			//error writing to file message
		}	
	}
}
