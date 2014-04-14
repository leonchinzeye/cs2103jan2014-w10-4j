//@author A0097304E
import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class AddTest {
	DateAndTimeFormats dateFormatsTest = new DateAndTimeFormats();
	Undo undoHandlerTest = new Undo();
	Add addHandlerTest = new Add();
	FileLinker fileLinkTest = new FileLinker();
	DataUI dataUITest = new DataUI();
	Search searchHandlerTest = new Search();
	
	@Before
	public void populate() {
		fileLinkTest.reset();
	}
	
	@Test
	public void test() {
		String userInput = "additional read books";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You've entered an invalid add command :(", dataUITest.getFeedback());
	}
	
	@Test
	public void test2() {
		String userInput = "add hello world; 12pm to 2pm; 4pm to 6pm";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You seem to have entered more than you need to :( Please try again!", dataUITest.getFeedback());
	}
	
	@Test
	public void test3() {
		String userInput = "add hello world due by 24 april due by 26 april";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You seem to have entered more than you need to :( Please try again!", dataUITest.getFeedback());
	}
	
	@Test
	public void test4() {
		String userInput = "add Hello World; 12 April to 17 April to 19 April";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You didn't enter a timing for this event :( Please try again!", dataUITest.getFeedback());
	}
	
	@Test
	public void test5() {
		String userInput = "add Hello World due by , ,,";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You've entered an invalid date format :(", dataUITest.getFeedback());
	}
	
	@Test
	public void test6() {
		String userInput = "add meeting; to 14 april";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You've entered an invalid date format :(", dataUITest.getFeedback());
	}
	
	@Test
	public void test7() {
		String userInput = "add meeting; 31 April to 40 April";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You've entered an invalid date format :(", dataUITest.getFeedback());
	}
	
	@Test
	public void test8() {
		String userInput = "add meeting; 21 April to 16 April";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("Greaaat Scott! Are you a time traveller?", dataUITest.getFeedback());
	}
	
	@Test
	public void test9() {
		String userInput = "add meeting; 18 april to 16:00";
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("You've entered an invalid date format :(", dataUITest.getFeedback());
	}
	
	@Test
	public void test10() {
		String userInput = "add meeting with Australia; 2pm to 4pm";
		Calendar timeStart = Calendar.getInstance();
		timeStart.set(Calendar.HOUR_OF_DAY, 14);
		timeStart.set(Calendar.MINUTE, 0);
		timeStart.set(Calendar.SECOND, 0);
		timeStart.set(Calendar.MILLISECOND, 0);
		Calendar timeEnd = Calendar.getInstance();
		timeEnd.set(Calendar.HOUR_OF_DAY, 16);
		timeEnd.set(Calendar.MINUTE, 0);
		timeEnd.set(Calendar.SECOND, 0);
		timeEnd.set(Calendar.MILLISECOND, 0);
		addHandlerTest.executeAdd(userInput, fileLinkTest, dataUITest, undoHandlerTest, dateFormatsTest);
		assertEquals("\"meeting with Australia\" has been successfully added!", dataUITest.getFeedback());
		assertEquals(timeStart, fileLinkTest.getIncompleteEvents().get(0).getStartDay());
		assertEquals(timeEnd, fileLinkTest.getIncompleteEvents().get(0).getEndDay());
	}
}
