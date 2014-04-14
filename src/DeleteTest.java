//@author A0100720E
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

import org.junit.Before;

public class DeleteTest {
	
	DateAndTimeFormats dateFormatsTest = new DateAndTimeFormats();
	Undo undoHandlerTest = new Undo();
	Add addHandlerTest = new Add();
	FileLinker fileLinkTest = new FileLinker();
	DataUI dataUITest = new DataUI();
	Delete deleteHandlerTest = new Delete();
	Mark markHandlerTest = new Mark();
	
	@Before
	public void populate() {
		fileLinkTest.reset();
		ArrayList<String> stuffToAdd = new ArrayList<String>();
		stuffToAdd.add("addu CS2105 assignment due by 15 may");
		stuffToAdd.add("add Read Lord of the Rings");
		stuffToAdd.add("add Meeting with boss; 2pm to 4pm");
		stuffToAdd.add("add CS2107 Assignment due by 20 April");
		stuffToAdd.add("add Hello World; 12 April to 17 April");
		stuffToAdd.add("add CS2101 Presentation; 11 April, 12pm to 2pm");
		stuffToAdd.add("add CS2101 OP1; 29 mar, 4pm to 6pm");
		stuffToAdd.add("add SSA2204 meeting; 27 mar, 12pm");
		stuffToAdd.add("add Watch Star Wars Movies");
		stuffToAdd.add("add SSA2204 assignment by 25 mar");
		
		for(int i = 0; i < stuffToAdd.size(); i++) {
			addHandlerTest.executeAdd(stuffToAdd.get(i), fileLinkTest, 
					dataUITest, undoHandlerTest, dateFormatsTest);
		}
		markHandlerTest.executeMark("mark 1", fileLinkTest, dataUITest, 2, undoHandlerTest);
		markHandlerTest.executeMark("mark 1", fileLinkTest, dataUITest, 2, undoHandlerTest);
		
		markHandlerTest.executeMark("mark 5", fileLinkTest, dataUITest, 1, undoHandlerTest);
		markHandlerTest.executeMark("mark 2", fileLinkTest, dataUITest, 1, undoHandlerTest);
	}
	
	
	//need to check for del, delt, dele, deltc, delec, with input without input, invalid range for input
	
	@Test
	public void test() {
		String userInputDelete = "del";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test2() {
		String userInputDelete = "dele";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test3() {
		String userInputDelete = "delt";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName()); 
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test4() {
		String userInputDelete = "deltc";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 3, undoHandlerTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test5() {
		String userInputDelete = "delec";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 4, undoHandlerTest);
		assertEquals("You seem to have forgotten something! Please enter an ID to delete!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test6() {
		String userInputDelete = "del 1";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("\"CS2101 Presentation\" has been deleted!", dataUITest.getFeedback());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName()); 
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test7() {
		String userInputDelete = "dele 1";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("\"CS2101 Presentation\" has been deleted!", dataUITest.getFeedback());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName()); 
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test8() {
		String userInputDelete = "delt 1";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("\"CS2105 assignment\" has been deleted!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName());
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName()); 
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	
	@Test
	public void test9() {
		String userInputDelete = "deltc 1";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 3, undoHandlerTest);
		assertEquals("\"Watch Star Wars Movies\" has been deleted!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName());
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName()); 
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(0).getName());
		
	}
	@Test
	public void test10() {
		String userInputDelete = "delec 1";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 4, undoHandlerTest);
		assertEquals("\"SSA2204 meeting\" has been deleted!", dataUITest.getFeedback());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName());
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName()); 
		
		
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(0).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
	}
	@Test
	public void test11() {
		String userInputDelete = "deletwtey";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("That was an unrecognisable delete command :(", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	@Test
	public void test12() {
		String userInputDelete = "del noNum";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 3!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test13() {
		String userInputDelete = "dele noNum";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 3!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test14() {
		String userInputDelete = "delt noNum";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 3!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test15() {
		String userInputDelete = "deltc noNum";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 3, undoHandlerTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 2!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test16() {
		String userInputDelete = "delec noNum";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 4, undoHandlerTest);
		assertEquals("You didn't enter a number! Please enter a number between 1 to 2!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test17() {
		String userInputDelete = "del 10";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("Please enter a number between 1 to 3!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test18() {
		String userInputDelete = "dele 10";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("Please enter a number between 1 to 3!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test19() {
		String userInputDelete = "delt 10";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest);
		assertEquals("Please enter a number between 1 to 3!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test20() {
		String userInputDelete = "deltc 10";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 3, undoHandlerTest);
		assertEquals("Please enter a number between 1 to 2!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
	
	@Test
	public void test21() {
		String userInputDelete = "delec 10";
		deleteHandlerTest.executeDelete(userInputDelete, fileLinkTest, dataUITest, 4, undoHandlerTest);
		assertEquals("Please enter a number between 1 to 2!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName()); 
		
		assertEquals("CS2105 assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		
		assertEquals("SSA2204 meeting", fileLinkTest.getCompletedEvents().get(0).getName());
		assertEquals("CS2101 OP1", fileLinkTest.getCompletedEvents().get(1).getName());
		
		assertEquals("Watch Star Wars Movies", fileLinkTest.getCompletedTasks().get(0).getName());
		assertEquals("SSA2204 assignment", fileLinkTest.getCompletedTasks().get(1).getName());
		
	}
}
