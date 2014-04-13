//@author A0097304E
package application;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RefreshUI {
	
	/**
	 * this is the main method that is called to refresh the details on the UI.
	 * the clock shown on the UI is also refreshed at this point
	 * @return
	 */
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