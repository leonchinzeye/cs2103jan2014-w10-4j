//@author A0094534B
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class IntegrationTest {
	
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
		stuffToAdd.add("add Freshman Camp; 12 Mar to 14 Mar");
		stuffToAdd.add("add Meeting with boss; 2pm to 4pm");
		stuffToAdd.add("add CS2101 Presentation; 11 April, 12pm to 2pm");
		stuffToAdd.add("add Hello World; 12 April to 17 April");
		
		stuffToAdd.add("addu CS2105 Assignment due by 15 may");
		stuffToAdd.add("add CS2103 submission by 14 April, 23:59");
		stuffToAdd.add("add CS2107 Assignment due by 20 April");
		stuffToAdd.add("add Read Lord of the Rings");
		
		for(int i = 0; i < stuffToAdd.size(); i++) {
			addHandlerTest.executeAdd(stuffToAdd.get(i), fileLinkTest, 
					dataUITest, undoHandlerTest, dateFormatsTest);
		}
	}
	
	@Test
	public void addTest() {		
		String addWrongCmdTest = "addd test";
		String addExtraArgumentsTest = "add Test; 25 May 2014; 2pm";
		String addMissingDateTest = "add Meeting with the Boss; 14 April, 2pm - 4pm - 6pm";
		String addWrongDateTest = "add Meeting with the Boss; 32 March 2014";
		String addTimeTravelTest = "add Event; 15 April to 5 April";
		String addTaskTest = "addu Play XCOM";
		String addEventTest = "add Star Wars marathon; 19 Dec";
		
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
	}
	
	@Test
	public void markTest() {
		String markMissingIDTest = "markt ";
		String markOutOfRangeTest = "marke 100";
		String markNonDigitTest = "marke a";
		String markWrongCmdTest = "markk 1";
		String markIncompleteTaskTest = "markt 1";
		String markIncompleteEventTest = "marke 1";
		String unmarkCompleteTaskTest = "unmarkt 1";
		String unmarkCompleteEventTest = "unmarke 1";
		
		testController.executeCmd(markMissingIDTest);
		assertEquals("You seem to have forgotten something! Please enter an ID!", testController.getTestingResponse());
		
		testController.executeCmd(markOutOfRangeTest);
		assertEquals("Please enter a number between 1 to 1!", testController.getTestingResponse());
		
		testController.executeCmd(markNonDigitTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 1!", testController.getTestingResponse());
		
		testController.executeCmd(markWrongCmdTest);
		assertEquals("That was an unrecognisable mark command :(", testController.getTestingResponse());
		
		testController.executeCmd(markIncompleteTaskTest);
		assertEquals("\"CS2105 Assignment\" has been successfully archived!", testController.getTestingResponse());
		
		testController.executeCmd(markIncompleteEventTest);
		assertEquals("\"Freshman Camp\" has been successfully archived!", testController.getTestingResponse());
		
		testController.executeCmd(unmarkCompleteTaskTest);
		assertEquals("\"CS2105 Assignment\" has been successfully unarchived!", testController.getTestingResponse());
		
		testController.executeCmd(unmarkCompleteEventTest);
		assertEquals("\"Freshman Camp\" has been successfully unarchived!", testController.getTestingResponse());
	}
	
	@Test
	public void delTest() {
		String delMissingIDTest = "delt ";
		String delOutOfRangeTest = "dele 100";
		String delNonDigitTest = "dele a";
		String delWrongCmdTest = "delk 1";
		String delIncompleteTaskTest = "delt 1";
		String delIncompleteEventTest = "dele 1";
		String delCompleteTaskTest = "deltc 1";
		String delCompleteEventTest = "delec 1";
		
		testController.executeCmd(delMissingIDTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", testController.getTestingResponse());
		
		testController.executeCmd(delOutOfRangeTest);
		assertEquals("Please enter a number between 1 to 1!", testController.getTestingResponse());
		
		testController.executeCmd(delNonDigitTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 1!", testController.getTestingResponse());
		
		testController.executeCmd(delWrongCmdTest);
		assertEquals("That was an unrecognisable delete command :(", testController.getTestingResponse());
		
		testController.executeCmd(delIncompleteTaskTest);
		assertEquals("\"CS2105 Assignment\" has been deleted!", testController.getTestingResponse());
		
		testController.executeCmd(delIncompleteEventTest);
		assertEquals("\"Freshman Camp\" has been deleted!", testController.getTestingResponse());
		
		testController.executeCmd(delCompleteTaskTest);
		assertEquals("There is nothing to delete!", testController.getTestingResponse());
		
		testController.executeCmd(delCompleteEventTest);
		assertEquals("There is nothing to delete!", testController.getTestingResponse());
	}
}
