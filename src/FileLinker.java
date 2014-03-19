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
	
	public void addEvent(TaskCard taskToBeAdded) {
		incompleteEvents.add(taskToBeAdded);
		callStorageWriteIncompleteEvents();
	}
	
	/**
	 * method that add logic will call to update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public void addHandling(TaskCard taskToBeAdded) {
		incompleteTasks.add(taskToBeAdded);
		callStorageWriteIncompleteTasks();
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
	
	public boolean deleteHandling(int taskNumberToBeDelete, int fileToBeDeleted) {
		if(fileToBeDeleted == 1) {
			incompleteTasks.remove(taskNumberToBeDelete);
			callStorageWriteIncompleteTasks();
		} else if(fileToBeDeleted == 2) {
			incompleteEvents.remove(taskNumberToBeDelete);
			callStorageWriteIncompleteEvents();
		} else if(fileToBeDeleted == 3) {
			completedTasks.remove(taskNumberToBeDelete);
			callStorageWriteCompletedTasks();
		} else {
			completedEvents.remove(taskNumberToBeDelete);
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
