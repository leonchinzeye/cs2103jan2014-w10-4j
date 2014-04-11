package application;

import java.util.ArrayList;

//@author A0097304E
public class Undo {

	private ArrayList<String> undoCmdType;
	private ArrayList<String> redoCmdType;
	
	private ArrayList<Integer> undoFileToBeModified;
	private ArrayList<Integer> redoFileToBeModified;
	
	private ArrayList<TaskCard> undoTasksOld;
	private ArrayList<TaskCard> redoTasksOld;
	
	private ArrayList<TaskCard> undoTasksNew;
	private ArrayList<TaskCard> redoTasksNew;
	
	private int indexOfLastCmdUndo;
	private int indexOfLastCmdRedo;
	
	private final String RESPONSE_SUCCESSFUL_UNDO_ADD = "\"%s\" has been removed!";
	private final String RESPONSE_SUCCESSFUL_REDO_ADD = "\"%s\" has been added back again!";
	private final String RESPONSE_SUCCESSFUL_UNDO_DELETE = "\"%s\" has been added back!";
	private final String RESPONSE_SUCCESSFUL_REDO_DELETE = "\"%s\" has been removed again!";
	private final String RESPONSE_SUCCESSFUL_UNDO_EDIT = "\"s\" has been added back again!";
	private final String RESPONSE_SUCCESSFUL_REDO_EDIT = "\"s\" has been added back again!";
	
	public enum COMMAND_TYPE {
		ADD, DELETE, EDIT, MARK, UNMARK
	}
	
	public Undo() {
		initVariables();
	}
	
	/**
	 * This method takes in the necessary details to store into the arraylist for that will be manipulated
	 * like a stack for the undo commands. Since the undo command works based on command-based and not state
	 * based, it stores only the taskcards
	 */
	public void storeUndo(String command, int fileModified, TaskCard oldTask, TaskCard newTask) {
		COMMAND_TYPE cmdType = determineCmdType(command);
		
		switch(cmdType) {
			case ADD:
				addUndoStorage(oldTask, fileModified);
				break;
			case DELETE:
				deleteUndoStorage(oldTask, fileModified);
				break;
			case EDIT:
				editUndoStorage(oldTask, newTask, fileModified);
				break;
			case MARK:
				markUndoStorage(oldTask, fileModified);
				break;
			case UNMARK:
				unmarkUndoStorage(oldTask, fileModified);
				break;
			default:
				break;
		}
	}

	/**
	 * This method is the main method that CommmandHandler calls when an undo action is called.
	 * It then performs the necessary undo actions and returns feedback
	 * @return
	 * returns a successful feedback message pertaining to the action that was just undone if the
	 * arraylist is not empty
	 * returns a false feedback message if there is nothing to be undone
	 */
	public String executeUndo(FileLinker fileLink) {
		String response = "";
		if(indexOfLastCmdUndo < 0) {
			response = "Nothing to undo!";
		} else {
			response = identifyUndoAndPerform(fileLink);
		}
		return response;
	}
	
	/**
	 * This method is the main method that CommmandHandler calls when an redo action is called.
	 * It then performs the necessary redo actions and returns feedback
	 * @return
	 * returns a successful feedback message pertaining to the action that was just redone if the
	 * arraylist is not empty
	 * returns a false feedback message if there is nothing to be redone
	 */
	public String executeRedo(FileLinker fileLink) {
		String response = "";
		if(indexOfLastCmdRedo < 0) {
			response = "Nothing to redo!";
		} else {
			response = identifyRedoAndPerform(fileLink);
		}
		return response;
	}

	/**
	 * this method pulls the most recent action from the undo arraylist and determines which
	 * action needs to be undone
	 * @return
	 * returns a string response
	 */
	private String identifyUndoAndPerform(FileLinker fileLink) {
	  String actionToBeDone = undoCmdType.get(indexOfLastCmdUndo);
	  String response = "";
	  
	  switch(actionToBeDone) {
	  	case "add":
	  		response = undoAdd(fileLink);
	  		break;
	  	case "delete":
	  		response = undoDelete(fileLink);
	  		break;
	  	case "edit":
	  		response = undoEdit(fileLink);
	  		break;
	  	case "mark":
	  		response = undoMark(fileLink);
	  		break;
	  	case "unmark":
	  		response = undoUnmark(fileLink);
	  		break;
	  	default:
	  		break;
	  }
	  return response;
  }

	/**
	 * this method pulls the most recent action from the redo arraylist and determines which
	 * action needs to be redone
	 * @return
	 * returns a string response
	 */
	private String identifyRedoAndPerform(FileLinker fileLink) {
		String actionToBeDone = redoCmdType.get(indexOfLastCmdRedo);
		String response = "";
		
		switch(actionToBeDone) {
			case "add":
				response = redoAdd(fileLink);
				break;
			case "delete":
				response = redoDelete(fileLink);
				break;
			case "edit":
				response = redoEdit(fileLink);
				break;
			case "mark":
				response = redoMark(fileLink);
				break;
			case "unmark":
				response = redoUnmark(fileLink);
				break;
			default:
				break;
		}
		return response;
	}

	/**
	 * this method undoes the add method by deleting the taskcard that was added into the
	 * database
	 * @return
	 * returns a string response to indicate which task was undone
	 */
	private String undoAdd(FileLinker fileLink) {		
		TaskCard taskToBeUndone = undoTasksOld.get(indexOfLastCmdUndo);
		int modifiedFile = undoFileToBeModified.get(indexOfLastCmdUndo);
		ArrayList<TaskCard> arrayToBeModified;
	  if(modifiedFile == 1) {
	  	arrayToBeModified = fileLink.getIncompleteTasks();
	  } else {
	  	arrayToBeModified = fileLink.getIncompleteEvents();
	  }
	  
	  int indexOfTaskToBeDeleted = arrayToBeModified.indexOf(taskToBeUndone);
	  fileLink.deleteHandling(indexOfTaskToBeDeleted, modifiedFile);
	  
	  pushUndoToRedo();
	  
	  return String.format(RESPONSE_SUCCESSFUL_UNDO_ADD, taskToBeUndone.getName());
  }
	
	/**
	 * this method re-does the add method by adding the taskcard back into the database
	 * @return
	 * returns a string response for successfully re-doing an add action
	 */
	private String redoAdd(FileLinker fileLink) {
		TaskCard taskToBeRedone = redoTasksOld.get(indexOfLastCmdRedo); 
		int modifiedFile = redoFileToBeModified.get(indexOfLastCmdRedo);
		
		fileLink.addHandling(taskToBeRedone, modifiedFile);
		
		pushRedoToUndo();
		
		return String.format(RESPONSE_SUCCESSFUL_REDO_ADD, taskToBeRedone.getName()); 
	}

	/**
	 * this method is for carrying out an undo action for delete by adding the task
	 * back into the database
	 * @return
	 * returns a response string
	 */
	private String undoDelete(FileLinker fileLink) {
	  TaskCard taskToBeAddedBack = undoTasksOld.get(indexOfLastCmdUndo);
		int modifiedFile = undoFileToBeModified.get(indexOfLastCmdUndo);
	  
	  fileLink.addHandling(taskToBeAddedBack, modifiedFile);
	  
	  pushUndoToRedo();
	  
	  return String.format(RESPONSE_SUCCESSFUL_UNDO_DELETE, taskToBeAddedBack.getName());
  }

	/**
	 * this method re-does the delete action by deleting the taskcard away from the
	 * database
	 * @return
	 * returns a response of the action being completed successfully with the task name
	 * that was deleted
	 */
	private String redoDelete(FileLinker fileLink) {
		TaskCard taskToBeDeletedBack = redoTasksOld.get(indexOfLastCmdRedo);
		int modifiedFile = redoFileToBeModified.get(indexOfLastCmdRedo);
		ArrayList<TaskCard> arrayToBeModified;
		
		if(modifiedFile == 1) {
			arrayToBeModified = fileLink.getIncompleteTasks();
		} else if(modifiedFile == 2) {
			arrayToBeModified = fileLink.getIncompleteEvents();
		} else if(modifiedFile == 3) {
			arrayToBeModified = fileLink.getCompletedTasks();
		} else {
			arrayToBeModified = fileLink.getCompletedEvents();
		}
	  
		int indexOfTaskToBeDeleted = arrayToBeModified.indexOf(taskToBeDeletedBack);
		fileLink.deleteHandling(indexOfTaskToBeDeleted, modifiedFile);
		
		pushRedoToUndo();
		
		return String.format(RESPONSE_SUCCESSFUL_REDO_DELETE, taskToBeDeletedBack.getName());
	}

	/**
	 * this method un-does the previous edit command and replaces the new taskcard in the
	 * database with the original taskcard
	 * @return
	 * returns a response with the task that was edited/undone
	 */
	private String undoEdit(FileLinker fileLink) {
		ArrayList<TaskCard> arrayToBeModified;
	  int modifiedFile = undoFileToBeModified.get(indexOfLastCmdUndo);
		TaskCard taskToBeAddedBack = undoTasksOld.get(indexOfLastCmdUndo);
	  TaskCard taskToBeReplaced = undoTasksNew.get(indexOfLastCmdUndo);
	  
	  if(modifiedFile == 1) {
			arrayToBeModified = fileLink.getIncompleteTasks();
		} else if(modifiedFile == 2) {
			arrayToBeModified = fileLink.getIncompleteEvents();
		} else if(modifiedFile == 3) {
			arrayToBeModified = fileLink.getCompletedTasks();
		} else {
			arrayToBeModified = fileLink.getCompletedEvents();
		}
	  
	  int indexOfTaskToBeEdited = arrayToBeModified.indexOf(taskToBeReplaced);
	  fileLink.editHandling(taskToBeAddedBack, indexOfTaskToBeEdited, modifiedFile);
	  
	  pushUndoToRedo();
	  
		return "Undo for editing \"" + taskToBeReplaced.getName() + "\" successful!";
  }

	/**
	 * this method re-does the previous edit command and replaces the old task card with
	 * new task card in the database 
	 * @return
	 * returns a response with the task that was edited/redone
	 */
	private String redoEdit(FileLinker fileLink) {
	  ArrayList<TaskCard> arrayToBeModified;
	  int modifiedFile = redoFileToBeModified.get(indexOfLastCmdRedo);
	  TaskCard taskToBeAddedBack = redoTasksNew.get(indexOfLastCmdRedo);
	  TaskCard taskToBeReplaced = redoTasksOld.get(indexOfLastCmdRedo);
	  
	  if(modifiedFile == 1) {
	  	arrayToBeModified = fileLink.getIncompleteTasks();
	  } else {
	  	arrayToBeModified = fileLink.getIncompleteEvents();
	  }
	  
	  int indexOfTaskToBeEdited = arrayToBeModified.indexOf(taskToBeReplaced);
	  fileLink.editHandling(taskToBeAddedBack, indexOfTaskToBeEdited, modifiedFile);
	  
	  pushRedoToUndo();
	  
		return "Redo for editing \"" + taskToBeReplaced.getName() + "\" successful!";
	}

	/**
	 * this method un-does the previous mark that the user performed by moving the taskcard
	 * from completed to incomplete
	 * @return
	 * returns a response with the task that was unmarked back to incomplete
	 */
	private String undoMark(FileLinker fileLink) {
	  ArrayList<TaskCard> arrayToBeMarked;
	  int fileToBeDeletedFrom;
	  int modifiedFile = undoFileToBeModified.get(indexOfLastCmdUndo);
	  TaskCard taskToBeAddedBack = undoTasksOld.get(indexOfLastCmdUndo);
	  
	  if(modifiedFile == 1) {
	  	fileToBeDeletedFrom = 3;
	  	arrayToBeMarked = fileLink.getCompletedTasks();
	  } else {
	  	fileToBeDeletedFrom = 4;
	  	arrayToBeMarked = fileLink.getCompletedEvents();
	  }
	  
	  int indexOfTaskToBeDeleted = arrayToBeMarked.indexOf(taskToBeAddedBack);
	  fileLink.markHandling(taskToBeAddedBack, indexOfTaskToBeDeleted, fileToBeDeletedFrom);
	  
	  pushUndoToRedo();
		
		return "Undo for archiving \"" + taskToBeAddedBack.getName() + "\" successful!";
  }

	/**
	 * this method re-does the mark function by moving the task back to the completed
	 * list of taskcard
	 * @return
	 * returns a response with the taskname that was marked back into completed
	 */
	private String redoMark(FileLinker fileLink) {
		ArrayList<TaskCard> arrayToBeMarked;
	  int modifiedFile = redoFileToBeModified.get(indexOfLastCmdRedo);
	  TaskCard taskToBeMarked = redoTasksOld.get(indexOfLastCmdRedo);
	  
	  if(modifiedFile == 1) {
	  	arrayToBeMarked = fileLink.getIncompleteTasks();
	  } else {
	  	arrayToBeMarked = fileLink.getIncompleteEvents();
	  }
	  
	  int taskNumberToBeDeleted = arrayToBeMarked.indexOf(taskToBeMarked);
	  fileLink.markHandling(taskToBeMarked, taskNumberToBeDeleted, modifiedFile);
	  
	  pushRedoToUndo();
	  
	  return "Redo for unarchiving \"" + taskToBeMarked.getName() + "\" successful!";
	}
	
	/**
	 * this method un-does the unmarking by moving the unmarked task back into the 
	 * list of completed tasks
	 * @return
	 * returns a response with the task name
	 */
	private String undoUnmark(FileLinker fileLink) {
		ArrayList<TaskCard> arrayToBeMarked;
		int fileToBeDeletedFrom;
		int modifiedFile = undoFileToBeModified.get(indexOfLastCmdUndo);
		TaskCard taskToBeMarked = undoTasksOld.get(indexOfLastCmdUndo);
		
		if(modifiedFile == 3) {
			fileToBeDeletedFrom = 1;
			arrayToBeMarked = fileLink.getIncompleteTasks();
		} else {
			fileToBeDeletedFrom = 2;
			arrayToBeMarked = fileLink.getIncompleteEvents();
		}
		
		int taskNumberToBeDeleted = arrayToBeMarked.indexOf(taskToBeMarked);
		fileLink.markHandling(taskToBeMarked, taskNumberToBeDeleted, fileToBeDeletedFrom);
		
		pushUndoToRedo();
		
		return "Undo for unarchiving \"" + taskToBeMarked.getName() + "\" successful!";
  }
	
	/**
	 * this method re-does the unmark by unmarking the task back into the list of
	 * incompleted tasks
	 * @return
	 * returns a response with the taskname that was moved back into incomplete
	 */
	private String redoUnmark(FileLinker fileLink) {
	  ArrayList<TaskCard> arrayToBeMarked;
	  int modifiedFile = redoFileToBeModified.get(indexOfLastCmdRedo);
	  TaskCard taskToBeMarked = redoTasksOld.get(indexOfLastCmdRedo);
	  
	  if(modifiedFile == 3) {
	  	arrayToBeMarked = fileLink.getCompletedTasks();
	  } else {
	  	arrayToBeMarked = fileLink.getCompletedEvents();
	  }
	  
	  int taskNumberToBeDeleted = arrayToBeMarked.indexOf(taskToBeMarked);
	  fileLink.markHandling(taskToBeMarked, taskNumberToBeDeleted, modifiedFile);
	  
	  pushRedoToUndo();
	  
		return "Redo for archiving \"" + taskToBeMarked.getName() + "\" successful!";
  }

	/**
	 * this method acts like the pop action from a stack. when an action is undone,
	 * the details of it are pushed into the redo arraylist
	 */
	private void pushUndoToRedo() {
	  redoTasksOld.add(undoTasksOld.get(indexOfLastCmdUndo));
	  redoTasksNew.add(undoTasksNew.get(indexOfLastCmdUndo));
	  redoCmdType.add(undoCmdType.get(indexOfLastCmdUndo));
	  redoFileToBeModified.add(undoFileToBeModified.get(indexOfLastCmdUndo));
	  
	  undoTasksOld.remove(indexOfLastCmdUndo);
	  undoTasksNew.remove(indexOfLastCmdUndo);
	  undoCmdType.remove(indexOfLastCmdUndo);
	  undoFileToBeModified.remove(indexOfLastCmdUndo);
	  
	  indexOfLastCmdUndo--;
	  indexOfLastCmdRedo++;
	}

	/**
	 * this method acts like the pop action from a stack. when an action is redone,
	 * the details of it are pushed into the undo arraylist
	 */
	private void pushRedoToUndo() {
		undoTasksOld.add(redoTasksOld.get(indexOfLastCmdRedo));
	  undoTasksNew.add(redoTasksNew.get(indexOfLastCmdRedo));
	  undoCmdType.add(redoCmdType.get(indexOfLastCmdRedo));
	  undoFileToBeModified.add(redoFileToBeModified.get(indexOfLastCmdRedo));
	  
	  redoTasksOld.remove(indexOfLastCmdRedo);
	  redoTasksNew.remove(indexOfLastCmdRedo);
	  redoCmdType.remove(indexOfLastCmdRedo);
	  redoFileToBeModified.remove(indexOfLastCmdRedo);
	  
	  indexOfLastCmdRedo--;
	  indexOfLastCmdUndo++;
	}

	/**
	 * this method stores the last command action into the undo arraylist for an add
	 * command
	 */
	private void addUndoStorage(TaskCard task, int fileModified) {
		undoCmdType.add("add");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmdUndo++;
  }

	/**
	 * this method stores the last command action into the undo arraylist for a delete
	 * command
	 */
	private void deleteUndoStorage(TaskCard task, int fileModified) {
	  undoCmdType.add("delete");
	  undoTasksOld.add(task);
	  undoTasksNew.add(null);
	  undoFileToBeModified.add(fileModified);
	  indexOfLastCmdUndo++;
  }

	/**
	 * this method stores the last command action into the undo arraylist for an edit
	 * command
	 */
	private void editUndoStorage(TaskCard oldTask, TaskCard newTask, int fileModified) {
	  undoCmdType.add("edit");
	  undoTasksOld.add(oldTask);
	  undoTasksNew.add(newTask);
	  undoFileToBeModified.add(fileModified);
	  indexOfLastCmdUndo++;
  }

	/**
	 * this method stores the last command action into the undo arraylist for an mark
	 * command
	 */
	private void markUndoStorage(TaskCard task, int fileModified) {
		undoCmdType.add("mark");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmdUndo++;
	}
	
	/**
	 * this method stores the last command action into the undo arraylist for an add
	 * command
	 */
	private void unmarkUndoStorage(TaskCard task, int fileModified) {
		undoCmdType.add("unmark");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmdUndo++;
  }

	/**
	 * this method is important as it flushes the information in the redo arraylist
	 * whenever the user enters a new command
	 */
	public void flushRedo() {
		redoCmdType = new ArrayList<String>();
		redoFileToBeModified = new ArrayList<Integer>();
		redoTasksOld = new ArrayList<TaskCard>();
		redoTasksNew = new ArrayList<TaskCard>();
		indexOfLastCmdRedo = -1;
	}
	
	/**
	 * this method initialises all the global variables to be used in the undo object
	 */
	private void initVariables() {
	  undoCmdType = new ArrayList<String>();
	  redoCmdType = new ArrayList<String>();
	  
	  undoFileToBeModified = new ArrayList<Integer>();
	  redoFileToBeModified = new ArrayList<Integer>();
	  
	  undoTasksOld = new ArrayList<TaskCard>();
	  redoTasksOld = new ArrayList<TaskCard>();
	  
	  undoTasksNew = new ArrayList<TaskCard>();
	  redoTasksNew= new ArrayList<TaskCard>();
	  
	  indexOfLastCmdUndo = -1;
	  indexOfLastCmdRedo = -1;
  }
	
	/**
	 * this method determines which command the user had entered
	 * @return
	 * returns a command type enum for the program to execute the appropriate 
	 * undo command
	 */
	private COMMAND_TYPE determineCmdType(String cmd) {
		if(cmd.equals("add")) {
			return COMMAND_TYPE.ADD;
		} else if(cmd.equals("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if(cmd.equals("edit")) {
			return COMMAND_TYPE.EDIT;
		} else if (cmd.equals("mark")) {
			return COMMAND_TYPE.MARK;
		} else {
			return COMMAND_TYPE.UNMARK;
		}
	}
}