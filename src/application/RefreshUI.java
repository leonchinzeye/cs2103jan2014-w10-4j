package application;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * this class allows the user to refresh the GUI
 * things it should do:
 *   - update the UI based
 *   - the GUI should call refresh every hour of the clock
 *   - refresh should be able to handle marking of events or tasks once
 *     past the deadline
 *   - when refreshing and marking events that have past their designated
 *  	 timeslots, for those repeating ones, it should know how to put it 
 *  	 to the next repeated time
 * @author leon
 *
 */
//@author A0097304E
public class RefreshUI {
	
	public static boolean executeRefresh(FileLinker fileLink, DataUI dataUI) {
		//should check for events that have passed the designated time (not done yet)		
		Calendar now = GregorianCalendar.getInstance();
		
		dataUI.configIncompleteTasks(fileLink);
		dataUI.configIncompleteEvents(fileLink);
		dataUI.configCompleteTasks(fileLink);
		dataUI.configCompletedEvents(fileLink);
		
		dataUI.setUIclock(now);
		dataUI.setUIdate(now);
		
		return true;
	}
}