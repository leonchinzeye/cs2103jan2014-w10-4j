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
	public boolean addHandling(ArrayList<TaskCard> arrayToBeUpdated) {
		
		return false;
	}
	
	/**
	 * method that delete logic will call to update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public boolean deleteHandling(ArrayList<TaskCard> arrayToBeUpdated) {
		
		return false;
	}
	
	/**
	 * method for edit logic that will update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public boolean editHandling(ArrayList<TaskCard> arrayToBeUpdated) {
		
		return false;
	}
	
	private void callStorageWrite() {
		
	}
}
