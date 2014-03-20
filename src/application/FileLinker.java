package application;
import java.util.ArrayList;

/**
 * Class that links the individual logic components to the FileHandler
 * and vice versa
 * @author leon
 *
 */
public class FileLinker {

	private ArrayList<TaskCard> incompleteTasks;
	private ArrayList<TaskCard> incompleteEvents;
	private ArrayList<TaskCard> completedTasks;
	private ArrayList<TaskCard> completedEvents;
	
	public FileLinker() {
		this.incompleteTasks = Storage.openFile(Storage.INCOMPLETE_TASKS_STORAGE_FILE_NAME);
		this.incompleteEvents = Storage.openFile(Storage.INCOMPLETE_EVENTS_STORAGE_FILE_NAME);
		this.completedTasks = Storage.openFile(Storage.COMPLETED_TASKS_STORAGE_FILE_NAME);
		this.completedEvents = Storage.openFile(Storage.COMPLETED_EVENTS_STORAGE_FILE_NAME);
	}
	
	public ArrayList<TaskCard> getIncompleteTasks() {
		return incompleteTasks;
	}
	
	public ArrayList<TaskCard> getIncompleteEvents() {
		return incompleteEvents;
	}
	
	public ArrayList<TaskCard> getCompletedTasks() {
		return completedTasks;
	}
	
	public ArrayList<TaskCard> getCompletedEvents() {
		return completedEvents;
	}
	
	/**
	 * method that add logic will call to update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public void addHandling(TaskCard taskToBeAdded) {
		if (taskToBeAdded.getType().contains("E")) { //if it's an Event
			incompleteEvents.add(taskToBeAdded);
			callStorageWriteIncompleteEvents();
		} else { //if it's a Task
			incompleteTasks.add(taskToBeAdded);
			callStorageWriteIncompleteTasks();
		}
	}
	
	public void markHandling(TaskCard taskToBeMarked, int taskNumberToBeDeleted, 
			int fileToBeDeletedFrom) {
		if(fileToBeDeletedFrom == 1) {
			completedTasks.add(taskToBeMarked);
			callStorageWriteIncompleteTasks();
			
			deleteHandling(taskNumberToBeDeleted, 2);
		} else if(fileToBeDeletedFrom == 2) {
			completedEvents.add(taskToBeMarked);
			callStorageWriteCompletedEvents();
			
			deleteHandling(taskNumberToBeDeleted, 1);
		} else if(fileToBeDeletedFrom == 3) {
			incompleteTasks.add(taskToBeMarked);
			callStorageWriteIncompleteTasks();
			
			deleteHandling(taskNumberToBeDeleted, 3);
		} else {
			incompleteEvents.add(taskToBeMarked);
			callStorageWriteIncompleteEvents();
			
			deleteHandling(taskNumberToBeDeleted, 4);
		}
	}
	
	public boolean deleteHandling(int taskNumberToBeDeleted, int fileToBeDeletedFrom) {
		if(fileToBeDeletedFrom == 1) {
			incompleteTasks.remove(taskNumberToBeDeleted);
			callStorageWriteIncompleteTasks();
		} else if(fileToBeDeletedFrom == 2) {
			incompleteEvents.remove(taskNumberToBeDeleted);
			callStorageWriteIncompleteEvents();
		} else if(fileToBeDeletedFrom == 3) {
			completedTasks.remove(taskNumberToBeDeleted);
			callStorageWriteCompletedTasks();
		} else {
			completedEvents.remove(taskNumberToBeDeleted);
			callStorageWriteCompletedEvents();
		}
		return true;
	}
	
	/**
	 * method for edit logic that will update modified data to the file
	 * @param arrayToBeUpdated
	 */
	
	public boolean editHandling(TaskCard modifiedTask, int taskNumberToBeModified) {
		incompleteTasks.set(taskNumberToBeModified, modifiedTask);
		callStorageWriteIncompleteTasks();
		return true;
	}
	
	public void resetIncompleteHandling() {
		incompleteTasks = new ArrayList<TaskCard>();
		int numberOfIncompleteTasks = incompleteTasks.size();
		Storage.writeFile(incompleteTasks, numberOfIncompleteTasks, Storage.INCOMPLETE_TASKS_STORAGE_FILE_NAME);
	}
	
	public void resetCompleteHandling() {
		completedTasks = new ArrayList<TaskCard>(); 
		int numberOfCompletedTasks = completedTasks.size();
		Storage.writeFile(completedTasks, numberOfCompletedTasks, Storage.COMPLETED_TASKS_STORAGE_FILE_NAME);;
	}
	
	private void callStorageWriteIncompleteTasks() {
		int numberOfIncompleteTasks = incompleteTasks.size();
		Storage.writeFile(incompleteTasks, numberOfIncompleteTasks, Storage.INCOMPLETE_TASKS_STORAGE_FILE_NAME);
	}
	
	private void callStorageWriteIncompleteEvents() {
		int numberOfIncompleteEvents = incompleteEvents.size();
		Storage.writeFile(incompleteEvents, numberOfIncompleteEvents, Storage.INCOMPLETE_EVENTS_STORAGE_FILE_NAME);
	}
	
	private void callStorageWriteCompletedTasks() {
		int numberOfCompletedTasks = completedTasks.size();
		Storage.writeFile(completedTasks, numberOfCompletedTasks, Storage.COMPLETED_TASKS_STORAGE_FILE_NAME);
	}
	
	private void callStorageWriteCompletedEvents() {
		int numberOfCompletedEvents = completedEvents.size();
		Storage.writeFile(completedEvents, numberOfCompletedEvents, Storage.COMPLETED_EVENTS_STORAGE_FILE_NAME);
	}
}
