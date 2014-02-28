import java.util.Scanner;

public class Clear {

	private static final int FIRST_ARGUMENT = 0;
	private static FileHandler fh;
	
	public Clear(FileHandler fh) {
		this.fh = fh;
	}

	public static void executeClear(String[] userInput) {
		boolean correctCmd;
		
		correctCmd = checkCmdInput(userInput[FIRST_ARGUMENT]);
		
		if(correctCmd == true) {
			
		} else {
			
		}
	}
	
	private static boolean checkCmdInput(String cmd) {
		String cmdLowerCase = cmd.toLowerCase();
		
		if(cmdLowerCase.equals("/clear")) {
			return true;
		} else {
			return false;
		}
	}

	private static void clearErrorHandling() {
		
	}
}
