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
	public void addHandling(TaskCard taskToBeAdded, int fileToBeAddedTo) {
		if(fileToBeAddedTo == 1) {
			if(state_search) {
				searchIncTasks.add(taskToBeAdded);
			}
			incompleteTasks.add(taskToBeAdded);
			callStorageWriteIncompleteTasks();
		} else if(fileToBeAddedTo == 2) {
			if(state_search) {
				searchIncEvents.add(taskToBeAdded);
			}
			incompleteEvents.add(taskToBeAdded);
			callStorageWriteIncompleteEvents();
		} else if(fileToBeAddedTo == 3) {
			if(state_search) {
				searchCompTasks.add(taskToBeAdded);
			}
			completedTasks.add(taskToBeAdded);
			callStorageWriteCompletedTasks();
		} else {
			if(state_search) {
				searchCompEvents.add(taskToBeAdded);
			}
			completedEvents.add(taskToBeAdded);
			callStorageWriteCompletedEvents();
		}
	}
	
	/**
	 * method for edit logic that will update modified data to the file
	 * @param arrayToBeUpdated
	 */
	public boolean editHandling(TaskCard modifiedTask, int taskNumberToBeModified, int fileToBeEditedFrom) {				

		if(fileToBeEditedFrom == 1) {
			if(state_search) {
				TaskCard origTask = searchIncTasks.get(taskNumberToBeModified);
				searchIncTasks.set(taskNumberToBeModified, modifiedTask);
				taskNumberToBeModified = incompleteTasks.indexOf(origTask);
			}
			incompleteTasks.set(taskNumberToBeModified, modifiedTask);
			callStorageWriteIncompleteTasks();
		}
		
		else if (fileToBeEditedFrom == 2) {
			if(state_search) {
				TaskCard origEvent = searchIncEvents.get(taskNumberToBeModified);
				searchIncEvents.set(taskNumberToBeModified, modifiedTask);
				taskNumberToBeModified = incompleteEvents.indexOf(origEvent);
			}
			incompleteEvents.set(taskNumberToBeModified, modifiedTask);
			callStorageWriteIncompleteEvents();
		}
	
		return true;
	}

	public boolean deleteHandling(int taskNumberToBeDeleted, int fileToBeDeletedFrom) {
		if(fileToBeDeletedFrom == 1) {
			if(state_search) {
				TaskCard toBeDeleted = searchIncTasks.get(taskNumberToBeDeleted);
				int origFileIndex = incompleteTasks.indexOf(toBeDeleted);
				incompleteTasks.remove(origFileIndex); 
				searchIncTasks.remove(taskNumberToBeDeleted);
			} else {
				incompleteTasks.remove(taskNumberToBeDeleted);
			}
			callStorageWriteIncompleteTasks();
		} else if(fileToBeDeletedFrom == 2) {
			if(state_search) {
				TaskCard toBeDeleted = searchIncEvents.get(taskNumberToBeDeleted);
				int origFileIndex = incompleteEvents.indexOf(toBeDeleted);
				incompleteEvents.remove(origFileIndex);
				searchIncEvents.remove(taskNumberToBeDeleted);
			} else {
				incompleteEvents.remove(taskNumberToBeDeleted);
			}
			callStorageWriteIncompleteEvents();
		} else if(fileToBeDeletedFrom == 3) {
			if(state_search) {
				TaskCard toBeDeleted = searchCompTasks.get(taskNumberToBeDeleted);
				int origFileIndex = completedTasks.indexOf(toBeDeleted);
				completedTasks.remove(origFileIndex);
				searchCompTasks.remove(taskNumberToBeDeleted);
			} else {
				completedTasks.remove(taskNumberToBeDeleted);	
			}
			callStorageWriteCompletedTasks();
		} else {
			if(state_search) {
				TaskCard toBeDeleted = searchCompEvents.get(taskNumberToBeDeleted);
				int origFileIndex = completedEvents.indexOf(toBeDeleted);
				completedEvents.remove(origFileIndex);
				searchCompEvents.remove(taskNumberToBeDeleted);
			} else {
				completedEvents.remove(taskNumberToBeDeleted);
			}
			callStorageWriteCompletedEvents();
		}
		return true;
	}
	
	public void markHandling(TaskCard taskToBeMarked, int taskNumberToBeDeleted, 
			int fileToBeDeletedFrom) {
		if(fileToBeDeletedFrom == 1) {
			if(state_search) {
				searchCompTasks.add(taskToBeMarked);
			} 
			
			completedTasks.add(taskToBeMarked);
			deleteHandling(taskNumberToBeDeleted, 1);
			
			callStorageWriteCompletedTasks();
			
		} else if(fileToBeDeletedFrom == 2) {
			if(state_search) {
				searchCompEvents.add(taskToBeMarked);
			}
			
			completedEvents.add(taskToBeMarked);
			deleteHandling(taskNumberToBeDeleted, 2);
			
			callStorageWriteCompletedEvents();
			
		} else if(fileToBeDeletedFrom == 3) {
			if(state_search) {
				searchIncTasks.add(taskToBeMarked);
			}
			
			incompleteTasks.add(taskToBeMarked);
			deleteHandling(taskNumberToBeDeleted, 3);
			
			callStorageWriteIncompleteTasks();
		
		} else {
			if(state_search) {
				searchIncEvents.add(taskToBeMarked);
			}
			
			incompleteEvents.add(taskToBeMarked);
			deleteHandling(taskNumberToBeDeleted, 4);
			
			callStorageWriteIncompleteEvents();
		}
	}

	public boolean searchHandling(ArrayList<TaskCard> searchedIncTasks, ArrayList<TaskCard> searchedIncEvents,
			ArrayList<TaskCard> searchedCompTasks, ArrayList<TaskCard> searchedCompEvents) {
		state_search = true;
		
		searchIncTasks = searchedIncTasks;
		searchIncEvents = searchedIncEvents;
		searchCompTasks = searchedCompTasks;
		searchCompEvents = searchedCompEvents;
		
		return true;
	}
	
	private void initSearchArrays() {
	  searchIncTasks = new ArrayList<TaskCard>();
	  searchIncEvents = new ArrayList<TaskCard>();
	  searchCompTasks = new ArrayList<TaskCard>();
	  searchCompEvents = new ArrayList<TaskCard>();
  }

	private void fillIncTaskSearch(ArrayList<Integer> incTasksList) {
		for(int i = 0; i < incTasksList.size(); i++) {
			int index = incTasksList.get(i);
			incTasksIndex.add(index);
			TaskCard searchedTask = incompleteTasks.get(index);
			searchIncTasks.add(searchedTask);
		}
  }

	private void fillIncEventsSearch(ArrayList<Integer> incEventsList) {
		for(int i = 0; i < incEventsList.size(); i++) {
			int index = incEventsList.get(i);
			incEventsIndex.add(index);
			TaskCard searchedTask = incompleteEvents.get(index);
			searchIncEvents.add(searchedTask);
		}	  
  }

	private void fillCompTasksSearch(ArrayList<Integer> compTasksList) {
		for(int i = 0; i < compTasksList.size(); i++) {
			int index = compTasksList.get(i);
			compTasksIndex.add(index);
			TaskCard searchedTask = completedTasks.get(index);
			searchCompTasks.add(searchedTask);
		}
  }

	private void fillCompEventsSearch(ArrayList<Integer> compEventsList) {
		for(int i = 0; i < compEventsList.size(); i++) {
			int index = compEventsList.get(i);
			compEventsIndex.add(index);
			TaskCard searchedTask = completedEvents.get(index);
			searchCompEvents.add(searchedTask);
		}
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
