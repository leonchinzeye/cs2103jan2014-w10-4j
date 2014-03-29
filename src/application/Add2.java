package application;

public class Add2 {
	
	private int ARRAY_FIRST_ARG = 0;
	private int ARRAY_SECOND_ARG = 1;
	
	private final int DEFAULT_FLOATING_TASKS_PRIORITY = 1;
	private final int DEFAULT_TASKS_AND_EVENTS_PRIORITY = 2;
	
	private final String FEEDBACK_NO_ARG_ENTERED = "Please enter a task/event to be added!";
	private final String FEEDBACK_EXTRA_DETAILS_ARG = "You've entered something extra :(  Please re-enter!";
	
	public Add2() {
		
	}
	
	public boolean executeAdd(String userInput, FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success = false;
		
		String[] tokenizedInput = userInput.trim().split("\\s+", 2);
		
		if(tokenizedInput.length < 2) {
			dataUI.setFeedback(FEEDBACK_NO_ARG_ENTERED);
			return false;
		} else {
			String userDetails = tokenizedInput[ARRAY_SECOND_ARG];
			success = identifyTypeAndPerform(userDetails, fileLink, dataUI, undoHandler);
		}
		return success;
	}
	
	private boolean identifyTypeAndPerform(String userDetails,
			FileLinker fileLink, DataUI dataUI, Undo undoHandler) {
		boolean success;
		
		String[] details = userDetails.split(";");
		
		if(details.length == 1) {
			success = addTask(details, fileLink, dataUI, undoHandler);
		} else if(details.length == 2) {
			success = addEvent(details, fileLink, dataUI, undoHandler);
		} else {
			dataUI.setFeedback(FEEDBACK_EXTRA_DETAILS_ARG);
			return false;
		}
		
		return false;
	}
	
	private boolean addTask(String[] details, FileLinker fileLink, DataUI dataUI,
	    Undo undoHandler) {
	  // TODO Auto-generated method stub
	  return false;
	}

	private boolean addEvent(String[] details, FileLinker fileLink,
      DataUI dataUI, Undo undoHandler) {
	  // TODO Auto-generated method stub
	  return false;
  }

	private void initVar() {
		
	}
}
