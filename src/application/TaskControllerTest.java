//@author A0094534B
package application;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskControllerTest {
	
	@Test
	public void test() {
		TaskController controller = new TaskController();
		
		String addTest1 = "add Read the Book Thief";
		String addTest2 = "add Meeting with the Boss; to 5.30pm";
		String delTest1 = "delt 11";
		String delTest2 = delTest1;
		String markTest1 = "marke 10";
		String unmarkTest1 = "unmarke 6";
		
		controller.executeCmd(addTest1);
		assertEquals("Task added!", controller.getTestingResponse());
		
		controller.executeCmd(addTest2);
		assertEquals("You've entered an invalid date format :( Please re-enter!", controller.getTestingResponse());
		
		controller.executeCmd(delTest1);
		assertEquals("\"Read the Book Thief\" has been deleted!", controller.getTestingResponse());
		
		controller.executeCmd(delTest2);
		assertEquals("Please enter a valid number between 1 to 10!", controller.getTestingResponse());
		
		controller.executeCmd(markTest1);
		assertEquals("\"Meeting with new clients\" has been archived!", controller.getTestingResponse());
		
		controller.executeCmd(unmarkTest1);
		assertEquals("\"Meeting with new clients\" has been unarchived!", controller.getTestingResponse());
	}
	
}
