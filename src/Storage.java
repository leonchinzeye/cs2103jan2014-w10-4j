//@author A0097304E
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * this class is responsible for reading from and writing to 
 * the designated files
 *
 */
public class Storage {

	private static final int FIRST_ARG = 0;
	private static final int SECOND_ARG = 1;
	private static final int THIRD_ARG = 2;
	private static final int FOURTH_ARG = 3;
	
	//2 separate files for storage. one for incomplete tasks, the other for archiving
	public static final String INCOMPLETE_TASKS_STORAGE_FILE_NAME = "incompletetasks.txt";
	public static final String INCOMPLETE_EVENTS_STORAGE_FILE_NAME = "incompleteevents.txt";
	public static final String COMPLETED_TASKS_STORAGE_FILE_NAME = "completedtasks.txt";
	public static final String COMPLETED_EVENTS_STORAGE_FILE_NAME = "completedevents.txt";
	
	private static final int NUMBER_OF_WRITTEN_LINES_FOR_EACH_TASK = 2;
	
	public static int numberOfIncompleteTasks = 0;
	public static int numberOfCompletedTasks = 0;
	
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss:SSSS");
	
	public Storage() {
	
	}
	
	/**
	 * reads the data stored in the text file
	 * @return
	 * returns an arraylist of tasks/events that can be manipulated
	 */
	public static ArrayList<TaskCard> openFile(String fileName) {
		ArrayList<TaskCard> file = new ArrayList<TaskCard>();
		
		try {
			FileReader fileRead = new FileReader(fileName);
			BufferedReader buffRead = new BufferedReader(fileRead);
			
			int numberOfTaskCards = Integer.parseInt(buffRead.readLine());
			
			for(int i = 0; i < numberOfTaskCards; i++) {
				TaskCard task = new TaskCard();
				ArrayList<String> taskDetails = new ArrayList<String>();
				
				taskDetails = getTaskDetailsFromFile(buffRead);
				setTaskDetailsForReading(taskDetails, task);
				
				file.add(task);
			}
			
			buffRead.close();
		} catch(FileNotFoundException ex) {
			createEmptyFile(fileName);
		} catch(IOException ex) {
			
		}
		
		return file;
	}
	
	/**
	 * writes the current stored database into the files
	 */
	public static void writeFile(ArrayList<TaskCard> file, int fileSize, String fileName) {
		try {
			FileWriter fileWrite = new FileWriter(fileName);
			BufferedWriter buffWrite = new BufferedWriter(fileWrite);
			
			buffWrite.write("" + fileSize);
			buffWrite.newLine();
			
			for(int i = 0; i < fileSize; i++) {
				TaskCard task = file.get(i);
				writeTaskCardDetails(buffWrite, task);
			}
			
			buffWrite.close();
		} catch(IOException ex) {
			
		}
	}

	/**
	 * writes the details in a format that is pre-determined
	 * @param buffWrite
	 * @param task
	 */
	private static void writeTaskCardDetails(BufferedWriter buffWrite,
			TaskCard task) {
		try {
			buffWrite.write("" + task.getName());
			buffWrite.newLine();
			
			String detailsToBeWritten = task.getType() + " " + dateString.format(task.getStartDay().getTime()) + 
					" " + dateString.format(task.getEndDay().getTime()) + " " + task.getPriority();
			
			buffWrite.write(detailsToBeWritten);
			buffWrite.newLine();
		} catch(IOException ex) {
			//error writing to file message
		}	
	}

	/**
	 * segment the details stored in the file for reading by the program
	 * @return
	 * returns an arraylist of strings of each segmented information
	 */
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

	/**
	 * reads the lines and sets the details stored into a TaskCard object
	 */
	private static void setTaskDetailsForReading(ArrayList<String> taskDetails,	TaskCard task) {
		Date start;
		Date end;
		task.setName(taskDetails.get(FIRST_ARG));
		
		String[] restOfDetails = taskDetails.get(SECOND_ARG).split(" ");
		task.setType(restOfDetails[FIRST_ARG]); //Type of task/event
		
		try {
			start = dateString.parse(restOfDetails[SECOND_ARG]);
			Calendar startCal = GregorianCalendar.getInstance();
			startCal.setTime(start);
			task.setStartDay(startCal);
		} catch(ParseException e) {
			//error reading from file
		}
		
		try {
			end = dateString.parse(restOfDetails[THIRD_ARG]);
			Calendar endCal = GregorianCalendar.getInstance();
			endCal.setTime(end);
			task.setEndDay(endCal);
		} catch(ParseException e) {
			//error reading from file
		}
		
		task.setPriority(Integer.parseInt(restOfDetails[FOURTH_ARG]));
	}

	/**
	 * creates and empty file with the file name
	 */
	private static void createEmptyFile(String fileStorageName) {
		ArrayList<TaskCard> emptyArrayList = new ArrayList<TaskCard>();
		int numberOfTasks = emptyArrayList.size();
		
		if(fileStorageName == INCOMPLETE_TASKS_STORAGE_FILE_NAME) {
			writeFile(emptyArrayList, numberOfTasks, INCOMPLETE_TASKS_STORAGE_FILE_NAME);
		} else if(fileStorageName == COMPLETED_TASKS_STORAGE_FILE_NAME) {
			writeFile(emptyArrayList, numberOfTasks, COMPLETED_TASKS_STORAGE_FILE_NAME);
		} else if(fileStorageName == INCOMPLETE_EVENTS_STORAGE_FILE_NAME) {
			writeFile(emptyArrayList, numberOfTasks, INCOMPLETE_EVENTS_STORAGE_FILE_NAME);
		} else {
			writeFile(emptyArrayList, numberOfTasks, COMPLETED_EVENTS_STORAGE_FILE_NAME);
		}
	}
}
