import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UndoTest {
	
	DateAndTimeFormats dateFormatsTest = new DateAndTimeFormats();
	Undo undoHandlerTest = new Undo();
	Add addHandlerTest = new Add();
	Delete deleteHandlerTest = new Delete();
	Edit editHandlerTest = new Edit();
	Mark markHandlerTest = new Mark();
	FileLinker fileLinkTest = new FileLinker();
	DataUI dataUITest = new DataUI();

	@Before
	public void populate() {
		fileLinkTest.reset();
	}
	
	@Test
	public void test() {
		assertEquals("Nothing to undo!", undoHandlerTest.executeUndo(fileLinkTest));
	}
	
	@Test
	public void test2() {
		assertEquals("Nothing to redo!", undoHandlerTest.executeRedo(fileLinkTest));
	}
	
	@Test
	public void test3() {
		addHandlerTest.executeAdd("add hello world", fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("\"hello world\" has been removed!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("\"hello world\" has been added back again!", undoHandlerTest.executeRedo(fileLinkTest));
	}
	
	@Test
	public void test4() {
		deleteHandlerTest.executeDelete("del 1", fileLinkTest, dataUITest, 2, undoHandlerTest);
		assertEquals("Nothing to undo!", undoHandlerTest.executeUndo(fileLinkTest));
		addHandlerTest.executeAdd("add hello world", fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		deleteHandlerTest.executeDelete("del 1", fileLinkTest, dataUITest, 1, undoHandlerTest);
		
		assertEquals("\"hello world\" has been added back!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("\"hello world\" has been removed!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("\"hello world\" has been added back again!", undoHandlerTest.executeRedo(fileLinkTest));
		assertEquals("\"hello world\" has been removed again!", undoHandlerTest.executeRedo(fileLinkTest));
	}
	
	@Test
	public void test5() {
		String userInput = "add meeting with someone; 12 april, 2pm to 4pm";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		editHandlerTest.executeEdit("edit 1 name: testing 2", fileLinkTest, dataUITest, 2, undoHandlerTest, dateFormatsTest);
		
		assertEquals("\"meeting with someone\" has been added back again!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("\"testing 2\" has been added back again!", undoHandlerTest.executeRedo(fileLinkTest));
		assertEquals("Nothing to redo!", undoHandlerTest.executeRedo(fileLinkTest));
	}
	
	@Test
	public void test6() {
		String userInput = "add freshmen camp; 16 april to 18 april";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		markHandlerTest.executeMark("mark 1", fileLinkTest, dataUITest, 2, undoHandlerTest);
		
		assertEquals("\"freshmen camp\" has been unmarked!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("\"freshmen camp\" has been removed!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("Nothing to undo!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("\"freshmen camp\" has been added back again!", undoHandlerTest.executeRedo(fileLinkTest));
		assertEquals("\"freshmen camp\" has been marked to "
				+ "the completed section again!", undoHandlerTest.executeRedo(fileLinkTest));
		assertEquals("Nothing to redo!", undoHandlerTest.executeRedo(fileLinkTest));
	}
	
	@Test
	public void test7() {
		String userInput = "add testing 7";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		markHandlerTest.executeMark("mark 1", fileLinkTest, dataUITest, 1, undoHandlerTest);
		markHandlerTest.executeMark("unmark 1", fileLinkTest, dataUITest, 3, undoHandlerTest);
		
		assertEquals("\"testing 7\" has been marked back to completed!", undoHandlerTest.executeUndo(fileLinkTest));
		assertEquals("\"testing 7\" has been unmarked again!", undoHandlerTest.executeRedo(fileLinkTest));
		assertEquals("Nothing to redo!", undoHandlerTest.executeRedo(fileLinkTest));
	}
}
