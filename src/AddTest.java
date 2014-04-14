//@author A0097304E
import static org.junit.Assert.*;

import org.junit.Test;

public class AddTest {
	/**
	 * The AddTest will try to implement all the possible variations of successful 
	 * and unsuccessful adds Task and Events  
	 * 
	 */
	@Test
	public void testExecuteAdd() {
		Undo undoHandler = new Undo();
		
		String wrongAdd = "addd";
		FileLinker adddFileLinker = new FileLinker();
		DataUI addUI = new DataUI();
		addUI.configIncompleteTasks(adddFileLinker);
		Add newAddWithWrongCommand = new Add();
		
//		newAddWithWrongCommand.executeAdd(wrongAdd, adddFileLinker, addUI, undoHandler);
		
		assertEquals("That was an unrecognisable add command! :(", addUI.getFeedback());
		
		
		String addWithOutAnyEntry = "add";
		FileLinker addFileLinker = new FileLinker();
		DataUI addWithoutEntry = new DataUI();
		addWithoutEntry.configIncompleteTasks(addFileLinker);
		Add newAddWithoutEntry = new Add();
//		newAddWithoutEntry.executeAdd(addWithOutAnyEntry, addFileLinker, addWithoutEntry, undoHandler);
		
		assertEquals("You didn't enter a task! Please enter a task!", addWithoutEntry.getFeedback());
		
		/*
		 * This is an Equivalence partition as any number of tokenizers more than 3 will produce
		 * the same output	
		 */
		String addWithTooManyTokenizers = "add this; is; something; yes";
		FileLinker addTokenLinker = new FileLinker();
		DataUI addTokenUI = new DataUI();
		addTokenUI.configIncompleteTasks(addTokenLinker);
		Add newAddWithTooManyTokens = new Add();
//		newAddWithTooManyTokens.executeAdd(addWithTooManyTokenizers, addTokenLinker, addTokenUI, undoHandler);
		
		assertEquals("That was an invalid format for adding a task :(", addTokenUI.getFeedback());
		
		/*
		 * Successful add for the task 
		 * 
		 */
		String addFloatingTask = "addf A new Task";
		FileLinker addFLinker = new FileLinker();
		DataUI addfUI = new DataUI();
		addfUI.configIncompleteTasks(addFLinker);
		Add newAddFProper = new Add();
//		newAddFProper.executeAdd(addFloatingTask, addFLinker, addfUI, undoHandler);
		
		assertEquals ("Task added!", addfUI.getFeedback());
		
	}
	
	
	
}
