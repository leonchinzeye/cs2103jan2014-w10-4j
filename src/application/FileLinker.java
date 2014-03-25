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
	
	private ArrayList<TaskCard> searchIncTasks;
	private ArrayList<TaskCard> searchIncEvents;
	private ArrayList<TaskCard> searchCompTasks;
	private ArrayList<TaskCard> searchCompEvents;
	
	private ArrayList<Integer> incTasksIndex;
	private ArrayList<Integer> incEventsIndex;
	private ArrayList<Integer> compTasksIndex;
	private ArrayList<Integer> compEventsIndex;
	
	private boolean state_search;
	
	public FileLinker() {
		this.incompleteTasks = Storage.openFile(Storage.INCOMPLETE_TASKS_STORAGE_FILE_NAME);
		this.incompleteEvents = Storage.openFile(Storage.INCOMPLETE_EVENTS_STORAGE_FILE_NAME);
		this.completedTasks = Storage.openFile(Storage.COMPLETED_TASKS_STORAGE_FILE_NAME);
		this.completedEvents = Storage.openFile(Storage.COMPLETED_EVENTS_STORAGE_FILE_NAME);
		
		this.searchIncTasks = new ArrayList<TaskCard>();
		this.searchIncEvents = new ArrayList<TaskCard>();
		this.searchCompTasks = new ArrayList<TaskCard>();
		this.searchCompEvents = new ArrayList<TaskCard>();
		
		this.incTasksIndex = new ArrayList<Integer>();
		this.incEventsIndex = new ArrayList<Integer>();
		this.compTasksIndex = new ArrayList<Integer>();
		this.compEventsIndex = new ArrayList<Integer>();
		
		this.state_search = false;
		
	}
	
	public ArrayList<TaskCard> getIncompleteTasks() {
		if(state_search) {
			return searchIncTasks;
		} else {
			return incompleteTasks;
		}
	}
	
	public ArrayList<TaskCard> getIncompleteEvents() {
		if(state_search) {
			return searchIncEvents;
		} else {
			return incompleteEvents;
		}
	}
	
	public ArrayList<TaskCard> getCompletedTasks() {
		if(state_search) {
			return searchCompTasks;
		} else {
			return completedTasks;
		}
	}
	
	public ArrayList<TaskCard> getCompletedEvents() {
		if(state_search) {
			return searchCompEvents;
		} else {
			return completedEvents;
		}
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
	
	public boolean searchHandling(ArrayList<Integer> incTasksList, ArrayList<Integer> incEventsList,
			ArrayList<Integer> compTasksList, ArrayList<Integer> compEventsList) {
		state_search = true;
		
		fillIncTaskSearch(incTasksList);
		fillIncEventsSearch(incEventsList);
		fillCompTasksSearch(compTasksList);
		fillCompEventsSearch(compEventsList);
		
		return true;
	}
	
	private void fillIncTaskSearch(ArrayList<Integer> incTasksList) {
		for(int i = 0; i < incTasksList.size(); i++) {
			int index = incTasksList.get(i);
			TaskCard searchedTask = incompleteTasks.get(index);
			searchIncTasks.add(searchedTask);
		}
  }

	private void fillIncEventsSearch(ArrayList<Integer> incEventsList) {
		for(int i = 0; i < incEventsList.size(); i++) {
			int index = incEventsList.get(i);
			TaskCard searchedTask = incompleteEvents.get(index);
			searchIncTasks.add(searchedTask);
		}	  
  }

	private void fillCompTasksSearch(ArrayList<Integer> compTasksList) {
		for(int i = 0; i < compTasksList.size(); i++) {
			int index = compTasksList.get(i);
			TaskCard searchedTask = completedTasks.get(index);
			searchIncTasks.add(searchedTask);
		}
  }

	private void fillCompEventsSearch(ArrayList<Integer> compEventsList) {
		for(int i = 0; i < compEventsList.size(); i++) {
			int index = compEventsList.get(i);
			TaskCard searchedTask = completedEvents.get(index);
			searchIncTasks.add(searchedTask);
		}
  }

	/**
	 * method for edit logic that will update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public boolean editHandling(TaskCard modifiedTask, int taskNumberToBeModified, int fileToBeDeletedFrom) {
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
	
	public void resetState() {
		state_search = false;
	}
}
