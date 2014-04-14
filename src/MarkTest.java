//@author A0097304E
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class MarkTest {
	DateAndTimeFormats dateFormatsTest = new DateAndTimeFormats();
	Undo undoHandlerTest = new Undo();
	Add addHandlerTest = new Add();
	FileLinker fileLinkTest = new FileLinker();
	DataUI dataUITest = new DataUI();
	Search searchHandlerTest = new Search();
	Mark markHandlerTest = new Mark();
	
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
	}
	
	@Test
	public void test() {
		String userInput = "mark 1";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("\"Freshman Camp\" has been successfully archived!", dataUITest.getFeedback());
	}
	
	@Test
	public void test2() {
		fileLinkTest.reset();
		String userInput = "mark 1";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("There is nothing to archive!", dataUITest.getFeedback());
	}
	
	@Test
	public void test3() {
		fileLinkTest.reset();
		String userInput = "unmark 1";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 3, undoHandlerTest);
		assertEquals("There is nothing to unarchive!", dataUITest.getFeedback());
	}
	
	@Test
	public void test4() {
		String userInput = "mark";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("You seem to have forgotten something! Please enter an ID!", dataUITest.getFeedback());
	}
	
	@Test
	public void test5() {
		String userInput = "mark 7";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("Please enter a number between 1 to 4!", dataUITest.getFeedback());
	}
	
	@Test
	public void test6() {
		String userInput = "markeetet 2";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("That was an unrecognisable mark command :(", dataUITest.getFeedback());
	}
	
	@Test
	public void test7() {
		String userInput = "mark jsf";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 4!", dataUITest.getFeedback());
	}
	
	@Test
	public void test8() {
		String userInput = "mark 2";
		markHandlerTest.executeMark(userInput, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("\"CS2101 Presentation\" has been successfully archived!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getCompletedEvents().get(0).getName());
	}
	
	@Test
	public void test9() {
		String userInput1 = "mark 2";
		String userInput2 = "unmark 1";
		markHandlerTest.executeMark(userInput1, fileLinkTest, dataUITest, 2, undoHandlerTest);
		markHandlerTest.executeMark(userInput2, fileLinkTest, dataUITest, 4, undoHandlerTest);
		assertEquals("\"CS2101 Presentation\" has been successfully unarchived!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(1).getName());
	}
}
