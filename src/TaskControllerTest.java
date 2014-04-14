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
	TaskController testController = new TaskController();
	
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
		String addWrongCmdTest = "addd test";
		String addExtraArgumentsTest = "add Test; 25 May 2014; 2pm";
		String addMissingDateTest = "add Meeting with the Boss; 14 April, 2pm - 4pm - 6pm";
		String addWrongDateTest = "add Meeting with the Boss; 32 March 2014";
		String addTimeTravelTest = "add Event; 15 April to 5 April";
		String addTaskTest = "add Play XCOM";
		String addEventTest = "add Star Wars marathon; 15 Apr, 2pm";
		
		testController.executeCmd(addWrongCmdTest);
		assertEquals("You've entered an invalid add command :(", testController.getTestingResponse());
		
		testController.executeCmd(addExtraArgumentsTest);
		assertEquals("You seem to have entered more than you need to :( Please try again!", testController.getTestingResponse());
		
		testController.executeCmd(addMissingDateTest);
		assertEquals("You didn't enter a timing for this event :( Please try again!", testController.getTestingResponse());
		
		testController.executeCmd(addWrongDateTest);
		assertEquals("You've entered an invalid date format :(", testController.getTestingResponse());
		
		testController.executeCmd(addTimeTravelTest);
		assertEquals("Greaaat Scott! Are you a time traveller?", testController.getTestingResponse());
		
		testController.executeCmd(addTaskTest);
		assertEquals("\"Play XCOM\" has been successfully added!", testController.getTestingResponse());
		
		testController.executeCmd(addEventTest);
		assertEquals("\"Star Wars marathon\" has been successfully added!", testController.getTestingResponse());

		String delMissingIDTest = "delt ";
		String delOutOfRangeTest = "dele 100";
		String delNonDigitTest = "dele a";
		String delWrongCmdTest = "delk 1";
		String delIncompleteTaskTest = "delt 5";
		String delIncompleteEventTest = "dele 4";
		String delCompleteTaskTest = "deltc 1";
		String delCompleteEventTest = "delec 1";
		
		testController.executeCmd(delMissingIDTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", testController.getTestingResponse());
		
		testController.executeCmd(delOutOfRangeTest);
		assertEquals("Please enter a number between 1 to %d!", testController.getTestingResponse());
		
		testController.executeCmd(delNonDigitTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to %d!", testController.getTestingResponse());
		
		testController.executeCmd(delWrongCmdTest);
		assertEquals("That was an unrecognisable delete command :(", testController.getTestingResponse());
		
		testController.executeCmd(delIncompleteTaskTest);
		assertEquals("\"Play XCOM\" has been deleted!", testController.getTestingResponse());
		
		testController.executeCmd(delIncompleteEventTest);
		assertEquals("\"Star Wars marathon\" has been deleted!", testController.getTestingResponse());
		
		testController.executeCmd(delCompleteTaskTest);
		assertEquals("There is nothing to delete!", testController.getTestingResponse());
		
		testController.executeCmd(delCompleteEventTest);
		assertEquals("\"Freshman Camp\" has been deleted!", testController.getTestingResponse());
	}
	
	/*@Test
	public void markTest() {
		TaskController controller = new TaskController();

		String markTest1 = "marke 10";
		String unmarkTest1 = "unmarke 6";
		
		controller.executeCmd(markTest1);
		assertEquals("\"Meeting with new clients\" has been archived!", controller.getTestingResponse());
		
		controller.executeCmd(unmarkTest1);
		assertEquals("\"Meeting with new clients\" has been unarchived!", controller.getTestingResponse());
	}*/
}
