package application;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

//@author A0097304E
public class FileLinker {

	private ArrayList<TaskCard> incompleteTasks;
	private ArrayList<TaskCard> incompleteEvents;
	private ArrayList<TaskCard> completedTasks;
	private ArrayList<TaskCard> completedEvents;
	
	private ArrayList<TaskCard> searchIncTasks;
	private ArrayList<TaskCard> searchIncEvents;
	private ArrayList<TaskCard> searchCompTasks;
	private ArrayList<TaskCard> searchCompEvents;
	
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
		} else if(fileToBeAddedTo == 2) {
			if(state_search) {
				searchIncEvents.add(taskToBeAdded);
			}
			incompleteEvents.add(taskToBeAdded);
		} else if(fileToBeAddedTo == 3) {
			if(state_search) {
				searchCompTasks.add(taskToBeAdded);
			}
			completedTasks.add(taskToBeAdded);
		} else {
			if(state_search) {
				searchCompEvents.add(taskToBeAdded);
			}
			completedEvents.add(taskToBeAdded);
		}
		
		sortFiles();
		writeToFiles();
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
		}
		
		else if (fileToBeEditedFrom == 2) {
			if(state_search) {
				TaskCard origEvent = searchIncEvents.get(taskNumberToBeModified);
				searchIncEvents.set(taskNumberToBeModified, modifiedTask);
				taskNumberToBeModified = incompleteEvents.indexOf(origEvent);
			}
			incompleteEvents.set(taskNumberToBeModified, modifiedTask);
		}
	
		sortFiles();
		writeToFiles();
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
		} else if(fileToBeDeletedFrom == 2) {
			if(state_search) {
				TaskCard toBeDeleted = searchIncEvents.get(taskNumberToBeDeleted);
				int origFileIndex = incompleteEvents.indexOf(toBeDeleted);
				incompleteEvents.remove(origFileIndex);
				searchIncEvents.remove(taskNumberToBeDeleted);
			} else {
				incompleteEvents.remove(taskNumberToBeDeleted);
			}
		} else if(fileToBeDeletedFrom == 3) {
			if(state_search) {
				TaskCard toBeDeleted = searchCompTasks.get(taskNumberToBeDeleted);
				int origFileIndex = completedTasks.indexOf(toBeDeleted);
				completedTasks.remove(origFileIndex);
				searchCompTasks.remove(taskNumberToBeDeleted);
			} else {
				completedTasks.remove(taskNumberToBeDeleted);	
			}
		} else {
			if(state_search) {
				TaskCard toBeDeleted = searchCompEvents.get(taskNumberToBeDeleted);
				int origFileIndex = completedEvents.indexOf(toBeDeleted);
				completedEvents.remove(origFileIndex);
				searchCompEvents.remove(taskNumberToBeDeleted);
			} else {
				completedEvents.remove(taskNumberToBeDeleted);
			}
		}
		
		sortFiles();
		writeToFiles();
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
		} else if(fileToBeDeletedFrom == 2) {
			if(state_search) {
				searchCompEvents.add(taskToBeMarked);
			}
			completedEvents.add(taskToBeMarked);
			deleteHandling(taskNumberToBeDeleted, 2);
		} else if(fileToBeDeletedFrom == 3) {
			if(state_search) {
				searchIncTasks.add(taskToBeMarked);
			}
			
			incompleteTasks.add(taskToBeMarked);
			deleteHandling(taskNumberToBeDeleted, 3);
		} else {
			if(state_search) {
				searchIncEvents.add(taskToBeMarked);
			}
			
			incompleteEvents.add(taskToBeMarked);
			deleteHandling(taskNumberToBeDeleted, 4);
		}
		
		sortFiles();
		writeToFiles();
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

	public void resetCompleteHandling() {
		completedTasks = new ArrayList<TaskCard>(); 
		int numberOfCompletedTasks = completedTasks.size();
		Storage.writeFile(completedTasks, numberOfCompletedTasks, Storage.COMPLETED_TASKS_STORAGE_FILE_NAME);;
	}
	
	private void writeToFiles() {
		callStorageWriteIncompleteTasks();
		callStorageWriteIncompleteEvents();
		callStorageWriteCompletedTasks();
		callStorageWriteCompletedEvents();
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
	
	private void sortFiles() {
		Collections.sort(incompleteTasks, new SortTasksByPriorityThenDeadline());
		Collections.sort(incompleteEvents, new SortEventsByPriorityThenDeadline());
	}
	
	private class SortTasksByPriorityThenDeadline implements Comparator<TaskCard> {
		public int compare(TaskCard task1, TaskCard task2) {
			int prior1 = task1.getPriority();
			int prior2 = task2.getPriority();
			
			Calendar end1 = task1.getEndDay();
			Calendar end2 = task2.getEndDay();
			
			String name1 = task1.getName().toLowerCase();
			String name2 = task2.getName().toLowerCase();
			
			String type1 = task1.getType();
			String type2 = task2.getType();
			
			if(prior1 < prior2) {
				return 1;
			} else if(prior1 > prior2) {
				return -1;
			} else if(end1.before(end2)) {
				return -1;
			} else if(end1.after(end2)) {
				return 1;
			} else if(name1.compareTo(name2) < 0) {
				return -1;
			} else if(name1.compareTo(name2) > 0) {
				return 1;
			} else if(type1.equals("FT") && type2.equals("T")) {
				return 1;
			} else if(type1.equals("T") && type2.equals("FT")) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	private class SortEventsByPriorityThenDeadline implements Comparator<TaskCard> {
		public int compare(TaskCard event1, TaskCard event2) {
			int prior1 = event1.getPriority();
			int prior2 = event2.getPriority();
			
			Calendar start1 = event1.getStartDay();
			Calendar start2 = event2.getStartDay();
			
			Calendar end1 = event1.getEndDay();
			Calendar end2 = event2.getEndDay();
			
			String name1 = event1.getName().toLowerCase();
			String name2 = event2.getName().toLowerCase();
			
			String type1 = event1.getType();
			String type2 = event2.getType();
			
			if(start1.before(start2)) {
				return -1;
			} else if(start1.after(start2)) {
				return 1;
			} else if(end1.before(end2)) {
				return -1;
			} else if(end1.after(end2)) {
				return 1;
			} else if(prior1 < prior2) {
				return 1;
			} else if(prior1 > prior2) {
				return -1;
			} else if(name1.compareTo(name2) < 0) {
				return -1;
			} else if(name1.compareTo(name2) > 0) {
				return 1;
			} else if(type1.equals("AE") && type2.equals("E")) {
				return 1;
			} else if(type1.equals("T") && type2.equals("FT")) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
