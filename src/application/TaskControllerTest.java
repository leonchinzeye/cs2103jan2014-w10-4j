package application;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskControllerTest {
	
	@Test
	public void test() {
		TaskController controller = new TaskController();
		
		String test1 = "add Read the Book Thief";
		String test2 = "add Meeting with the Boss; to 5.30pm";
		
		controller.executeCmd(test1);
		
		assertEquals("Task added!", controller.getTestingResponse());	
	}
	
}
