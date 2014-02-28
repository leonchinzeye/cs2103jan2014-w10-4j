import java.util.Scanner;

public class Clear {

	private static final int FIRST_ARGUMENT = 0;
	private static FileHandler fh;
	
	/*
	 * Clear constructor
	 */
	public Clear(FileHandler fh) {
		this.fh = fh;
	}

	public static void executeClear(String[] userInput) {
		boolean correctCmd;
		
		correctCmd = checkCmdInput(userInput[FIRST_ARGUMENT]);
		
		if(correctCmd == true) {
			
		} else {
			clearErrorHandling();
		}
	}
	
	/*
	 * for checking if the first argument is exactly "/clear"
	 * user might have input "/clearCS2105" and forgotten the spacing
	 */
	private static boolean checkCmdInput(String cmd) {
		String cmdLowerCase = cmd.toLowerCase();
		
		if(cmdLowerCase.equals("/clearh") || 
				cmdLowerCase.equals("/clearall") || 
				cmdLowerCase.equals("/cleare") ||
				cmdLowerCase.equals("/cleart")) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * handle the error identified in checkCmdInput
	 */
	private static void clearErrorHandling() {
		
	}
	
	private static void clearHistory() {
		
	}
}
