package application;

import java.util.ArrayList;

public class Undo {

	private ArrayList<String> cmdTypeUndo;
	private ArrayList<String> cmdTypeRedo;
	
	private ArrayList<Integer> undoFileToBeModified;
	private ArrayList<Integer> redoFileToBeModified;
	
	private ArrayList<TaskCard> undoTasksOld;
	private ArrayList<TaskCard> redoTasksOld;
	
	private ArrayList<TaskCard> undoTasksNew;
	private ArrayList<TaskCard> redoTasksNew;
	
	private int indexOfLastCmd;

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
	public boolean storeUndo(String command, int fileModified, TaskCard oldTask, TaskCard newTask, int taskIndex) {
		boolean success = false;
		
		COMMAND_TYPE cmdType = determineCmdType(command);
		
		switch(cmdType) {
			case ADD:
				addUndoStorage(oldTask, fileModified);
				break;
			case DELETE:
				deleteUndoStorage(oldTask, taskIndex, fileModified);
				break;
			case EDIT:
				editUndoStorage(oldTask, newTask, taskIndex, fileModified);
				break;
			case MARK:
				markUndoStorage(oldTask, taskIndex, fileModified);
				break;
				
			default:
				break;
		}
		
		return success;
	}
	
	public void executeUndo(FileLinker fileLink) {
		if(indexOfLastCmd == 0) {
			return;
		} else {
			identifyUndoAndPerform(fileLink);
		}
	}
	
	public void executeRedo(FileLinker fileLink) {
		
	}

	private void identifyUndoAndPerform(FileLinker fileLink) {
		int lastActionIndex = cmdTypeUndo.size() - 1;
	  String actionToBeDone = cmdTypeUndo.get(lastActionIndex);
	  
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

	private void undoAdd(FileLinker fileLink) {
		TaskCard taskToBeUndone = undoTasksOld.get(indexOfLastCmd);
		int modifiedFile = undoFileToBeModified.get(indexOfLastCmd);
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

	private void pushUndoToRedo() {
	  redoTasksOld.add(undoTasksOld.get(indexOfLastCmd));
	  redoTasksNew.add(undoTasksNew.get(indexOfLastCmd));
	  cmdTypeRedo.add(cmdTypeUndo.get(indexOfLastCmd));
	  redoFileToBeModified.add(undoFileToBeModified.get(indexOfLastCmd));
	  indexOfLastCmd--;
  }
	
	private void pushRedoToUndo() {
		
	}

	private void undoDelete(FileLinker fileLink) {
	  // TODO Auto-generated method stub
	  
  }

	private void undoEdit(FileLinker fileLink) {
	  // TODO Auto-generated method stub
	  
  }

	private void undoMark(FileLinker fileLink) {
	  // TODO Auto-generated method stub
	  
  }

	private void addUndoStorage(TaskCard task, int fileModified) {
		cmdTypeUndo.add("add");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmd++;
  }

	private void deleteUndoStorage(TaskCard task, int taskIndex, int fileModified) {
	  cmdTypeUndo.add("delete");
	  undoTasksOld.add(task);
	  undoTasksNew.add(null);
	  undoFileToBeModified.add(fileModified);
	  indexOfLastCmd++;
  }

	private void editUndoStorage(TaskCard oldTask, TaskCard newTask, int taskIndex, int fileModified) {
	  cmdTypeUndo.add("edit");
	  undoTasksOld.add(oldTask);
	  undoTasksNew.add(newTask);
	  undoFileToBeModified.add(fileModified);
	  indexOfLastCmd++;
  }

	private void markUndoStorage(TaskCard task, int taskIndex, int fileModified) {
		cmdTypeUndo.add("mark");
		undoTasksOld.add(task);
		undoTasksNew.add(null);
		undoFileToBeModified.add(fileModified);
		indexOfLastCmd++;
	}

	public void flushRedo() {
		cmdTypeRedo = new ArrayList<String>();
		redoTasksOld = new ArrayList<TaskCard>();
	}
	
	private void initVariables() {
	  cmdTypeUndo = new ArrayList<String>();
	  cmdTypeRedo = new ArrayList<String>();
	  
	  undoFileToBeModified = new ArrayList<Integer>();
	  redoFileToBeModified = new ArrayList<Integer>();
	  
	  undoTasksOld = new ArrayList<TaskCard>();
	  redoTasksOld = new ArrayList<TaskCard>();
	  
	  undoTasksNew = new ArrayList<TaskCard>();
	  redoTasksNew= new ArrayList<TaskCard>();
	  
	  indexOfLastCmd = 0;
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
