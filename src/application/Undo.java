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

	public enum COMMAND_TYPE {
		ADD, DELETE, EDIT, MARK, UNMARK
	}
	
	public Undo() {
		initVariables();
	}
	
	/**
	 * 
	 * @param command: refers to the command that was executed, the opposite of it will be stored
	 * @param oldTask
	 * @param taskIndex
	 * @return
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

	public String executeUndo(FileLinker fileLink) {
		String response = "";
		if(indexOfLastCmdUndo < 0) {
			response = "Nothing to undo!";
		} else {
			response = identifyUndoAndPerform(fileLink);
		}
		return response;
	}
	
	public String executeRedo(FileLinker fileLink) {
		String response = "";
		if(indexOfLastCmdRedo < 0) {
			response = "Nothing to redo!";
		} else {
			response = identifyRedoAndPerform(fileLink);
		}
		return response;
	}

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
	  
	  return "Undo for adding \"" + taskToBeUndone.getName() + "\" successful!";
  }
	
	private String redoAdd(FileLinker fileLink) {
		TaskCard taskToBeRedone = redoTasksOld.get(indexOfLastCmdRedo); 
		int modifiedFile = redoFileToBeModified.get(indexOfLastCmdRedo);
		
		fileLink.addHandling(taskToBeRedone, modifiedFile);
		
		pushRedoToUndo();
		
		return "Redo for adding \"" + taskToBeRedone.getName() + "\" successful!";
	}

	private String undoDelete(FileLinker fileLink) {
	  TaskCard taskToBeAddedBack = undoTasksOld.get(indexOfLastCmdUndo);
		int modifiedFile = undoFileToBeModified.get(indexOfLastCmdUndo);
	  
	  //must consider deleting from completed tasks too
	  fileLink.addHandling(taskToBeAddedBack, modifiedFile);
	  
	  pushUndoToRedo();
	  
	  return "Undo for deleting \"" + taskToBeAddedBack.getName() + "\" successful!";
  }

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
		
		return "Undo for deleting \"" + taskToBeDeletedBack.getName() + "\" successful!";
	}

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

	private void addUndoStorage(TaskCard task, int fileModified) {
		undoCmdType.add("add");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmdUndo++;
  }

	private void deleteUndoStorage(TaskCard task, int fileModified) {
	  undoCmdType.add("delete");
	  undoTasksOld.add(task);
	  undoTasksNew.add(null);
	  undoFileToBeModified.add(fileModified);
	  indexOfLastCmdUndo++;
  }

	private void editUndoStorage(TaskCard oldTask, TaskCard newTask, int fileModified) {
	  undoCmdType.add("edit");
	  undoTasksOld.add(oldTask);
	  undoTasksNew.add(newTask);
	  undoFileToBeModified.add(fileModified);
	  indexOfLastCmdUndo++;
  }

	private void markUndoStorage(TaskCard task, int fileModified) {
		undoCmdType.add("mark");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmdUndo++;
	}
	
	private void unmarkUndoStorage(TaskCard task, int fileModified) {
		undoCmdType.add("unmark");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmdUndo++;
  }

	public void flushRedo() {
		redoCmdType = new ArrayList<String>();
		redoFileToBeModified = new ArrayList<Integer>();
		redoTasksOld = new ArrayList<TaskCard>();
		redoTasksNew = new ArrayList<TaskCard>();
		indexOfLastCmdRedo = -1;
	}
	
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