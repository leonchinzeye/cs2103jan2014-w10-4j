package application;

import static org.junit.Assert.*;

import org.junit.Test;

public class MarkTest {
	/**
	 * The MarkTest 
	 */
	
	@Test
	public void testExecuteMark() {
		Undo undoHandler = new Undo();
		
		/*This is an equivalence partition to test for entering a non-digit*/
		FileLinker markFilelink = new FileLinker();
		DataUI markUI = new DataUI();
		markUI.configIncompleteTasks(markFilelink);
		Mark newMarkWithWrongInput = new Mark();
		int tableNo = 1;
		newMarkWithWrongInput.executeMark("markt h", markFilelink, markUI, tableNo, undoHandler);
		
		assertEquals("You didn't enter a number! Please enter a number between 1 to 4!", markUI.getFeedback());
		
		/*This is to test entering a digit that's out of range.*/
		markFilelink = new FileLinker();
		markUI = new DataUI();
		markUI.configIncompleteTasks(markFilelink);
		newMarkWithWrongInput = new Mark();
		tableNo = 1;
		newMarkWithWrongInput.executeMark("markt 10", markFilelink, markUI, tableNo, undoHandler);
		
		assertEquals("Please enter a number between 1 to 4!", markUI.getFeedback());
		
		/*This is to test entering a digit that's less than the range.*/
		markFilelink = new FileLinker();
		markUI = new DataUI();
		markUI.configIncompleteTasks(markFilelink);
		newMarkWithWrongInput = new Mark();
		tableNo = 1;
		newMarkWithWrongInput.executeMark("markt -1", markFilelink, markUI, tableNo, undoHandler);
		
		assertEquals("Please enter a number between 1 to 4!", markUI.getFeedback());
		
		/*This is to test entering a digit that's successful.*/
		markFilelink = new FileLinker();
		markUI = new DataUI();
		markUI.configIncompleteTasks(markFilelink);
		newMarkWithWrongInput = new Mark();
		tableNo = 1;
		newMarkWithWrongInput.executeMark("markt 4", markFilelink, markUI, tableNo, undoHandler);
		
		assertEquals("\"baddass\" has been archived!", markUI.getFeedback());
		
		/*This is to test entering the command without an argument.*/
		markFilelink = new FileLinker();
		markUI = new DataUI();
		markUI.configIncompleteTasks(markFilelink);
		newMarkWithWrongInput = new Mark();
		tableNo = 1;
		newMarkWithWrongInput.executeMark("markt", markFilelink, markUI, tableNo, undoHandler);
		
		assertEquals("You didn't specify a task to mark as complete! Please enter an ID!", markUI.getFeedback());
	}

}
