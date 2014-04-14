//@author A0100720E
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

import org.junit.Before;

public class EditTest {
	DateAndTimeFormats dateFormatsTest = new DateAndTimeFormats();
	Undo undoHandlerTest = new Undo();
	Add addHandlerTest = new Add();
	FileLinker fileLinkTest = new FileLinker();
	DataUI dataUITest = new DataUI();
	Edit editHandlerTest = new Edit();
	
	@Before
	public void populate() {
		fileLinkTest.reset();
		ArrayList<String> stuffToAdd = new ArrayList<String>();
		stuffToAdd.add("add Read Lord of the Rings");
		stuffToAdd.add("add CS2107 Assignment due by 20 April, 12pm");
		stuffToAdd.add("add Hello World; 12 April to 17 April");
		stuffToAdd.add("add CS2101 Presentation; 11 April, 12pm to 2pm");
		
		for(int i = 0; i < stuffToAdd.size(); i++) {
			addHandlerTest.executeAdd(stuffToAdd.get(i), fileLinkTest, 
					dataUITest, undoHandlerTest, dateFormatsTest);
		}
		
	}
	
	//test for edit with without number, edit name, priority for task and event, edit start and end for event, edit end for task
	@Test
	public void test() {
		String userInputDelete = "edit";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("Are you missing the task number and details you want to edit?", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test1() {
		String userInputDelete = "edit 1";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("Are you missing the details that you want to edit?", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test3() {
		String userInputDelete = "edit 1 name: CS2101 OP2";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("Event edited!", dataUITest.getFeedback());
		
		assertEquals("CS2101 OP2", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	 
	@Test
	public void test4() {
		String userInputDelete = "edit 1 priority: high";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("Event edited!", dataUITest.getFeedback());
		
		assertEquals(3, fileLinkTest.getIncompleteEvents().get(0).getPriority());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test5() {
		String userInputDelete = "edit 1 start: 10 apr";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 10, 2014 12:00:00 PM", fileLinkTest.getIncompleteEvents().get(0).getStartDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test6() {
		String userInputDelete = "edit 1 start: 11am";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 11, 2014 11:00:00 AM", fileLinkTest.getIncompleteEvents().get(0).getStartDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test7() {
		String userInputDelete = "edit 1 start: 10 apr, 11am";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 10, 2014 11:00:00 AM", fileLinkTest.getIncompleteEvents().get(0).getStartDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test8() {
		String userInputDelete = "edit 1 end: 14 apr";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 14, 2014 2:00:00 PM", fileLinkTest.getIncompleteEvents().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test9() {
		String userInputDelete = "edit 1 end: 5.30pm";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 11, 2014 5:30:00 PM", fileLinkTest.getIncompleteEvents().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test10() {
		String userInputDelete = "edit 1 end: 14 apr, 5.30pm";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 14, 2014 5:30:00 PM", fileLinkTest.getIncompleteEvents().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test11() {
		String userInputDelete = "editt";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("Are you missing the task number and details you want to edit?", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test12() {
		String userInputDelete = "edite";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("Are you missing the task number and details you want to edit?", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test13() {
		String userInputDelete = "editt 1";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("Are you missing the details that you want to edit?", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test14() {
		String userInputDelete = "edite 1";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("Are you missing the details that you want to edit?", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test15() {
		String userInputDelete = "edite 1 name: CS2101 OP2";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("Event edited!", dataUITest.getFeedback());
		
		assertEquals("CS2101 OP2", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	 
	@Test
	public void test16() {
		String userInputDelete = "edite 1 priority: high";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("Event edited!", dataUITest.getFeedback());
		
		assertEquals(3, fileLinkTest.getIncompleteEvents().get(0).getPriority());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test17() {
		String userInputDelete = "edite 1 start: 10 apr";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 10, 2014 12:00:00 PM", fileLinkTest.getIncompleteEvents().get(0).getStartDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test18() {
		String userInputDelete = "edite 1 start: 11am";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 11, 2014 11:00:00 AM", fileLinkTest.getIncompleteEvents().get(0).getStartDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test19() {
		String userInputDelete = "edite 1 end: 14 apr";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 14, 2014 2:00:00 PM", fileLinkTest.getIncompleteEvents().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test20() {
		String userInputDelete = "edite 1 end: 7.30pm";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 11, 2014 7:30:00 PM", fileLinkTest.getIncompleteEvents().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test21() {
		String userInputDelete = "edite 1 end: 14 apr, 7.30pm";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 2, undoHandlerTest,dateFormatsTest);
		assertEquals("The event has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 14, 2014 7:30:00 PM", fileLinkTest.getIncompleteEvents().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test22() {
		String userInputDelete = "editt 1 name: CS2105 Assignment 2";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("Task edited!", dataUITest.getFeedback());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2105 Assignment 2", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test23() {
		String userInputDelete = "editt 1 priority: high";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("Task edited!", dataUITest.getFeedback());
		
		assertEquals(3, fileLinkTest.getIncompleteTasks().get(0).getPriority());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test24() {
		String userInputDelete = "editt 1 end: 14 apr";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("The task has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 14, 2014 12:00:00 PM", fileLinkTest.getIncompleteTasks().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test25() {
		String userInputDelete = "editt 1 end: 7.30pm";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("The task has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 20, 2014 7:30:00 PM", fileLinkTest.getIncompleteTasks().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
	@Test
	public void test26() {
		String userInputDelete = "editt 1 end: 14 apr, 7.30pm";
		editHandlerTest.executeEdit(userInputDelete, fileLinkTest, dataUITest, 1, undoHandlerTest,dateFormatsTest);
		assertEquals("The task has been successfully edited!", dataUITest.getFeedback());
		
		assertEquals("Apr 14, 2014 7:30:00 PM", fileLinkTest.getIncompleteTasks().get(0).getEndDay().getTime().toLocaleString());
		
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName()); 
		
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(1).getName());
	}
	
}
