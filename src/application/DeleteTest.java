package application;
import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;

public class DeleteTest {

	@Test
	public void testExecuteDelete() throws IOException{
		/**
		 * For invalid command name without number input
		 */
		Undo undoHandler = new Undo();
		
		String wrongDelete = "/delete";
		FileLinker deleteFileLinker = new FileLinker();
		DataUI deleteUI = new DataUI();
		deleteUI.configIncompleteTasks(deleteFileLinker);
		Delete newDeleteWithWrongCommand = new Delete();
		
		newDeleteWithWrongCommand.executeDelete(wrongDelete, deleteFileLinker, deleteUI, undoHandler);
		
		assertEquals("That was an unrecognisable delete command :(", deleteUI.getFeedback());
		
		/**
		 * For valid command name without number input
		 */
		
		String deleteIncompTaskNoID = "/dt";
		FileLinker deleteFLForDTWithoutNum = new FileLinker();
		DataUI deleteUIForValidDeleteName = new DataUI();
		deleteUIForValidDeleteName.configIncompleteTasks(deleteFLForDTWithoutNum);
		Delete newDeleteForIncompTaskNoId = new Delete();
		newDeleteForIncompTaskNoId.executeDelete(deleteIncompTaskNoID, deleteFLForDTWithoutNum, deleteUIForValidDeleteName, undoHandler);
		
		assertEquals("You didn't specify an incomplete task to delete! Please enter an ID to delete!", deleteUIForValidDeleteName.getFeedback());
	
		/**
		 * For valid command for type task 
		 */
		
		
		String deleteIncompTaskNo3 = "/dt 3";
		FileLinker deleteFlTaskWithNum = new FileLinker();
		DataUI deleteUIForDtWithNum = new DataUI();
		deleteUIForDtWithNum.configIncompleteTasks(deleteFlTaskWithNum);
		Delete newDeleteForIncompTaskWithID = new Delete();
		newDeleteForIncompTaskWithID.executeDelete(deleteIncompTaskNo3, deleteFlTaskWithNum, deleteUIForDtWithNum, undoHandler);
		
		assertEquals("\"hello world\" has been deleted!", deleteUIForDtWithNum.getFeedback());
		
		/**
		 * For valid command  for type event 
		 */
		
		String deleteIncompEventNo3 = "/de 3";
		FileLinker deleteFlEventWithNum = new FileLinker();
		DataUI deleteUIForDeWithNum = new DataUI();
		deleteUIForDeWithNum.configIncompleteEvents(deleteFlEventWithNum);
		Delete newDeleteForIncompEventWithID = new Delete();
		newDeleteForIncompEventWithID.executeDelete(deleteIncompEventNo3, deleteFlEventWithNum, deleteUIForDeWithNum, undoHandler);
	
		assertEquals("\"test2\" has been deleted!", deleteUIForDeWithNum.getFeedback());
	
	
	}

}
