import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class SearchTest {
	
	DateAndTimeFormats dateFormatsTest = new DateAndTimeFormats();
	Undo undoHandlerTest = new Undo();
	Add addHandlerTest = new Add();
	Mark markHandlerTest = new Mark();
	FileLinker fileLinkTest = new FileLinker();
	DataUI dataUITest = new DataUI();
	Search searchHandlerTest = new Search();
	
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
	public void test() {
		String userInputSearch = "search today";
		searchHandlerTest.executeSearch(userInputSearch, fileLinkTest, 
				dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"today\"", dataUITest.getFeedback());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(1).getName()); 
	}
	
	@Test
	public void test2() {
		String userInputSearch = "search toaday";
		searchHandlerTest.executeSearch(userInputSearch, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"toaday\"", dataUITest.getFeedback());
		assertEquals(0, fileLinkTest.getIncompleteTasks().size());
		assertEquals(0, fileLinkTest.getIncompleteEvents().size());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(0, fileLinkTest.getCompletedEvents().size());
	}
	
	@Test
	public void test3() {
		String search = "search Mon";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"Mon\"", dataUITest.getFeedback());
		assertEquals(0, fileLinkTest.getIncompleteTasks().size());
		assertEquals(0, fileLinkTest.getIncompleteEvents().size());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(0, fileLinkTest.getCompletedEvents().size());	
	}
	
	@Test
	public void test4() {
		String search = "search Wednesday";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"Wednesday\"", dataUITest.getFeedback());
		assertEquals(0, fileLinkTest.getIncompleteTasks().size());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(0, fileLinkTest.getCompletedEvents().size());
	}
	
	@Test
	public void test5() {
		String search = "search 14 April";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"14 April\"", dataUITest.getFeedback());
		assertEquals(1, fileLinkTest.getIncompleteTasks().size());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(0, fileLinkTest.getCompletedEvents().size());	
	}
	
	@Test
	public void test6() {
		String search = "search expired";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"expired\"", dataUITest.getFeedback());
		assertEquals(0, fileLinkTest.getIncompleteTasks().size());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals("Freshman Camp", fileLinkTest.getCompletedEvents().get(0).getName());
	}
	
	@Test
	public void test7() {
		String search = "search tomorrow";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"tomorrow\"", dataUITest.getFeedback());
		assertEquals("CS2105 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals("CS2107 Assignment", fileLinkTest.getIncompleteTasks().get(1).getName());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(2).getName());
		assertEquals(1, fileLinkTest.getIncompleteEvents().size());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(0, fileLinkTest.getCompletedEvents().size());
	}
	
	@Test
	public void test8() {
		String search = "search HIGH";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"HIGH\"", dataUITest.getFeedback());
		assertEquals("CS2105 Assignment", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals(0, fileLinkTest.getIncompleteEvents().size());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(0, fileLinkTest.getCompletedEvents().size());
	}
	
	@Test
	public void test9() {
		String search = "search MED";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"MED\"", dataUITest.getFeedback());
		assertEquals(2, fileLinkTest.getIncompleteTasks().size());
		assertEquals("CS2101 Presentation", fileLinkTest.getIncompleteEvents().get(0).getName());
		assertEquals("Hello World", fileLinkTest.getIncompleteEvents().get(1).getName());
		assertEquals("Meeting with boss", fileLinkTest.getIncompleteEvents().get(2).getName());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(1, fileLinkTest.getCompletedEvents().size());
	}
	
	@Test
	public void test10() {
		String search = "search LOW";
		searchHandlerTest.executeSearch(search, fileLinkTest, dataUITest, dateFormatsTest);
		assertEquals("Displaying results for \"LOW\"", dataUITest.getFeedback());
		assertEquals("Read Lord of the Rings", fileLinkTest.getIncompleteTasks().get(0).getName());
		assertEquals(0, fileLinkTest.getIncompleteEvents().size());
		assertEquals(0, fileLinkTest.getCompletedTasks().size());
		assertEquals(0, fileLinkTest.getCompletedEvents().size());
	}
}


