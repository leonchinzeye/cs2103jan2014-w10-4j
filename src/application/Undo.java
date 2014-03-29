package application;

import java.util.ArrayList;

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
		ADD, DELETE, EDIT, MARK
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
	public boolean storeUndo(String command, int fileModified, TaskCard oldTask, TaskCard newTask) {
		boolean success = false;
		
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
				
			default:
				break;
		}
		
		return success;
	}
	
	public void executeUndo(FileLinker fileLink) {
		if(indexOfLastCmdUndo < 0) {
			return;
		} else {
			identifyUndoAndPerform(fileLink);
		}
	}
	
	public void executeRedo(FileLinker fileLink) {
		if(indexOfLastCmdRedo < 0) {
			return;
		} else {
			identifyRedoAndPerform(fileLink);
		}
	}

	private void identifyUndoAndPerform(FileLinker fileLink) {
	  String actionToBeDone = undoCmdType.get(indexOfLastCmdUndo);
	  
	  switch(actionToBeDone) {
	  	case "add":
	  		undoAdd(fileLink);
	  		break;
	  	case "delete":
	  		undoDelete(fileLink);
	  		break;
	  	case "edit":
	  		undoEdit(fileLink);
	  		break;
	  	case "mark":
	  		undoMark(fileLink);
	  	default:
	  		break;
	  }
  }

	private void identifyRedoAndPerform(FileLinker fileLink) {
		String actionToBeDone = redoCmdType.get(indexOfLastCmdRedo);
		
		switch(actionToBeDone) {
			case "add":
				redoAdd(fileLink);
				break;
			case "delete":
				redoDelete(fileLink);
				break;
			case "edit":
				redoEdit(fileLink);
				break;
			case "mark":
				redoMark(fileLink);
		}
	}

	private void undoAdd(FileLinker fileLink) {
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
  }
	
	private void redoAdd(FileLinker fileLink) {
		TaskCard taskToBeRedone = redoTasksOld.get(indexOfLastCmdRedo); 
		
		fileLink.addHandling(taskToBeRedone);
		
		pushRedoToUndo();
	}

	private void undoDelete(FileLinker fileLink) {
	  TaskCard taskToBeAddedBack = undoTasksOld.get(indexOfLastCmdUndo);
	  
	  //must consider deleting from completed tasks too
	  fileLink.addHandling(taskToBeAddedBack);
	  
	  pushUndoToRedo();
  }

	private void redoDelete(FileLinker fileLink) {
		TaskCard taskToBeDeletedBack = redoTasksOld.get(indexOfLastCmdRedo);
		int modifiedFile = redoFileToBeModified.get(indexOfLastCmdRedo);
		ArrayList<TaskCard> arrayToBeModified;
		
		if(modifiedFile == 1) {
			arrayToBeModified = fileLink.getIncompleteTasks();
		} else {
			arrayToBeModified = fileLink.getIncompleteEvents();
		}
	  
		int indexOfTaskToBeDeleted = arrayToBeModified.indexOf(taskToBeDeletedBack);
		fileLink.deleteHandling(indexOfTaskToBeDeleted, modifiedFile);
		
		pushRedoToUndo();
	}

	private void undoEdit(FileLinker fileLink) {
	  // TODO Auto-generated method stub
	  
  }

	private void redoEdit(FileLinker fileLink) {
	  // TODO Auto-generated method stub
	  
	}

	private void undoMark(FileLinker fileLink) {
	  // TODO Auto-generated method stub
	  
  }

	private void redoMark(FileLinker fileLink) {
	  // TODO Auto-generated method stub
	  
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
		} else {
			return COMMAND_TYPE.MARK;
		}
	}
}
