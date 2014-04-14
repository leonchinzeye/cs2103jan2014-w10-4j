//@author A0094534B
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TaskControllerTest {
	
	DateAndTimeFormats dateFormatsTest = new DateAndTimeFormats();
	Undo undoHandlerTest = new Undo();
	Add addHandlerTest = new Add();
	Mark markHandlerTest = new Mark();
	FileLinker fileLinkTest = new FileLinker();
	DataUI dataUITest = new DataUI();
	
	@Before
	public void populate() {
		fileLinkTest.reset();
		ArrayList<String> stuffToAdd = new ArrayList<String>();
		stuffToAdd.add("addu CS2105 Assignment due by 15 may");
		stuffToAdd.add("add Read Lord of the Rings");
		stuffToAdd.add("add Meeting with boss; 2pm to 4pm");
		stuffToAdd.add("add CS2107 Assignment due by 20 April");
		stuffToAdd.add("add Hello World; 12 April to 17 April");
		stuffToAdd.add("add CS2101 Presentation; 11 April, 12pm to 2pm");
		stuffToAdd.add("add CS2103 submission by 14 April, 23:59");
		stuffToAdd.add("add Freshman Camp; 12 Mar to 14 Mar");
		
		for(int i = 0; i < stuffToAdd.size(); i++) {
			addHandlerTest.executeAdd(stuffToAdd.get(i), fileLinkTest, 
					dataUITest, undoHandlerTest, dateFormatsTest);
		}
		
		markHandlerTest.executeMark("mark 1", fileLinkTest, dataUITest, 2, undoHandlerTest);
	}
	
	@Test
	public void addTest() {
		TaskController controller = new TaskController();
		
		String addWrongCmdTest = "addd test";
		String addExtraArgumentsTest = "add Test; 25 May 2014; 2pm";
		String addMissingDateTest = "add Meeting with the Boss; ";
		String addWrongDateTest = "add Meeting with the Boss; 32 March 2014";
		String addTimeTravelTest = "add Event; 15 April to 5 April";
		String addTaskTest = "add Play XCOM";
		String addEventTest = "add Star Wars marathon; 15 Apr, 2pm";
		
		controller.executeCmd(addWrongCmdTest);
		assertEquals("You've entered an invalid add command :(", controller.getTestingResponse());
		
		controller.executeCmd(addExtraArgumentsTest);
		assertEquals("You've entered an invalid add command :(", controller.getTestingResponse());
		
		controller.executeCmd(addMissingDateTest);
		assertEquals("You didn't enter a timing for this event :( Please try again!", controller.getTestingResponse());
		
		controller.executeCmd(addWrongDateTest);
		assertEquals("You've entered an invalid date format :(", controller.getTestingResponse());
		
		controller.executeCmd(addTimeTravelTest);
		assertEquals("Greaaat Scott! Are you a time traveller?", controller.getTestingResponse());
		
		controller.executeCmd(addTaskTest);
		assertEquals("\"Play XCOM\" has been successfully added!", controller.getTestingResponse());
		
		controller.executeCmd(addEventTest);
		assertEquals("\"Star Wars marathon\" has been successfully added!", controller.getTestingResponse());
	}
	
	@Test
	public void delTest() {
		TaskController controller = new TaskController();
		
		String delMissingIDTest = "del";
		String delOutOfRangeTest = "del 100";
		String delNonDigitTest = "del a";
		String delWrongCmdTest = "delk 1";
		String delIncompleteTaskTest = "delt 5";
		String delIncompleteEventTest = "dele 4";
		String delCompleteTaskTest = "deltc 1";
		String delCompleteEventTest = "delec 1";
		
		controller.executeCmd(delMissingIDTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", controller.getTestingResponse());
		
		controller.executeCmd(delOutOfRangeTest);
		assertEquals("Please enter a number between 1 to %d!", controller.getTestingResponse());
		
		controller.executeCmd(delNonDigitTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to %d!", controller.getTestingResponse());
		
		controller.executeCmd(delWrongCmdTest);
		assertEquals("That was an unrecognisable delete command :(", controller.getTestingResponse());
		
		controller.executeCmd(delIncompleteTaskTest);
		assertEquals("\"Play XCOM\" has been deleted!", controller.getTestingResponse());
		
		controller.executeCmd(delIncompleteEventTest);
		assertEquals("\"Star Wars marathon\" has been deleted!", controller.getTestingResponse());
		
		controller.executeCmd(delCompleteTaskTest);
		assertEquals("", controller.getTestingResponse());
		
		controller.executeCmd(delCompleteEventTest);
		assertEquals("\"Freshman Camp\" has been deleted!", controller.getTestingResponse());
	}
	
	@Test
	public void markTest() {
		TaskController controller = new TaskController();

		String markTest1 = "marke 10";
		String unmarkTest1 = "unmarke 6";
		
		controller.executeCmd(markTest1);
		assertEquals("\"Meeting with new clients\" has been archived!", controller.getTestingResponse());
		
		controller.executeCmd(unmarkTest1);
		assertEquals("\"Meeting with new clients\" has been unarchived!", controller.getTestingResponse());
	}
}
