import java.util.ArrayList;

/**
 * Class that links the individual logic components to the FileHandler
 * and vice versa
 * @author leon
 *
 */
public class FileLinker {

	private ArrayList<TaskCard> incompleteTasks;
	private ArrayList<TaskCard> completedTasks;
	
	public FileLinker() {
		this.incompleteTasks = Storage.openIncompleteStorageFile();
		this.completedTasks = Storage.openCompletedStorageFile();
	}
	
	/**
	 * method that add logic will call to update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public boolean addHandling(TaskCard taskToBeAdded) {
		incompleteTasks.add(taskToBeAdded);
		callStorageWriteIncomplete();
		
		return true;
	}
	
	/**
	 * method that delete logic will call to update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public ArrayList<TaskCard> deleteRetrieval() {
		
		return incompleteTasks;
	}
	
	public boolean deleteHandling(int taskNumberToBeDelete) {
		incompleteTasks.remove(taskNumberToBeDelete);
		callStorageWriteIncomplete();
		return true;
	}
	
	/**
	 * method for edit logic that will update modified data to the file
	 * @param arrayToBeUpdated
	 */
	
	public ArrayList<TaskCard> displayHandler() {
		
		return incompleteTasks;
	}
	
	public ArrayList<TaskCard> editRetrieval() {
		return incompleteTasks;
	}
	
	public boolean editHandling(TaskCard modifiedTask, int taskNumberToBeModified) {
		incompleteTasks.set(taskNumberToBeModified, modifiedTask);
		callStorageWriteIncomplete();
		return true;
	}
	
	public ArrayList<TaskCard> searchRetrieval() {
		return incompleteTasks;
	}
	
	public void resetIncompleteHandling() {
		incompleteTasks = new ArrayList<TaskCard>();
		int numberOfIncompleteTasks = incompleteTasks.size();
		Storage.writeIncompleteTasksFile(incompleteTasks, numberOfIncompleteTasks);
	}
	
	public void resetCompleteHandling() {
		completedTasks = new ArrayList<TaskCard>(); 
		int numberOfCompletedTasks = completedTasks.size();
		Storage.writeCompleteTasksFile(completedTasks, numberOfCompletedTasks);
	}
	
	private void callStorageWriteIncomplete() {
		int numberOfIncompleteTasks = incompleteTasks.size();
		Storage.writeIncompleteTasksFile(incompleteTasks, numberOfIncompleteTasks);
	}
	
	private void callStorageWriteCompleted() {
		int numberOfCompletedTasks = completedTasks.size();
		Storage.writeCompleteTasksFile(completedTasks, numberOfCompletedTasks);
	}
}
