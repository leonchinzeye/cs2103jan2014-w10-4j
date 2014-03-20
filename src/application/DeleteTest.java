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
		
		String wrongDelete = "/delete";
		FileLinker deleteFileLinker = new FileLinker();
		DataUI deleteUI = new DataUI();
		deleteUI.configIncompleteTasks(deleteFileLinker);
		Delete newDeleteWithWrongCommand = new Delete();
		newDeleteWithWrongCommand.executeDelete(wrongDelete, deleteFileLinker, deleteUI);
		
		assertEquals("That was an unrecognisable delete command :(", deleteUI.getFeedback());
		
		/**
		 * For valid command name without number input
		 */
		
		String deleteIncompTaskNoID = "/dt";
		FileLinker deleteFLForDTWithoutNum = new FileLinker();
		DataUI deleteUIForValidDeleteName = new DataUI();
		deleteUIForValidDeleteName.configIncompleteTasks(deleteFLForDTWithoutNum);
		Delete newDeleteForIncompTaskNoId = new Delete();
		newDeleteForIncompTaskNoId.executeDelete(deleteIncompTaskNoID, deleteFLForDTWithoutNum, deleteUIForValidDeleteName);
		
		assertEquals("You didn't specify an incomplete task to delete! Please enter an ID to delete!", deleteUIForValidDeleteName.getFeedback());
	}

}
