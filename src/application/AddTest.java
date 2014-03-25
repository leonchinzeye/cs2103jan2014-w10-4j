package application;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddTest {
	
	@Test
	public void testExecuteAdd() {
		
		String wrongAdd = "/addd";
		FileLinker adddFileLinker = new FileLinker();
		DataUI addUI = new DataUI();
		addUI.configIncompleteTasks(adddFileLinker);
		Add newAddWithWrongCommand = new Add();
		newAddWithWrongCommand.executeAdd(wrongAdd, adddFileLinker, addUI);
		
		assertEquals("That was an unrecognisable add command! :(", addUI.getFeedback());
		
		
		String addWithOutAnyEntry = "/add";
		FileLinker addFileLinker = new FileLinker();
		DataUI addWithoutEntry = new DataUI();
		addWithoutEntry.configIncompleteTasks(addFileLinker);
		Add newAddWithoutEntry = new Add();
		newAddWithoutEntry.executeAdd(addWithOutAnyEntry, addFileLinker, addWithoutEntry);
		
		assertEquals("You didn't enter a task! Please enter a task!", addWithoutEntry.getFeedback());
		
		String addWithTooManyTokenizers = "/add this; is; something; yes";
		FileLinker addTokenLinker = new FileLinker();
		DataUI addTokenUI = new DataUI();
		addTokenUI.configIncompleteTasks(addTokenLinker);
		Add newAddWithTooManyTokens = new Add();
		newAddWithTooManyTokens.executeAdd(addWithTooManyTokenizers, addTokenLinker, addTokenUI);
		
		assertEquals("That was an invalid format for adding a task :(", addTokenUI.getFeedback());
		
		
	}
	
	
	
}
