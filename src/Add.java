public class Add {
	static FileHandler incompleteTasks = new FileHandler();
	static FileHandler completeTasks = new FileHandler();
	
	public String executeAdd(String commandFull) {
		String[] commandArray = commandFull.split(" ", 2);
		if (commandArray[1].trim().length() <= 0) {
			return "Invalid argument";
		} else if (commandArray[0].equals("/add")){
			addTask(commandArray[1]);
		} else if (commandArray[0].equals("/addf")) {
			addFloatingTask(commandArray[1]);
		} else if (commandArray[0].equals("/adde")) {
			addEvent (commandArray[1]);
		} else if (commandArray[0].equals("/addr")) {
			addRepeatingEvent (commandArray[1]);
		}
		return "added";
	}

	private void addTask(String argument) {
		
	}

	public void addFloatingTask(String argument) {
		
	}

	private void addEvent(String argument) {
		
	}
	
	private void addRepeatingEvent(String argument) {
		
	}
}
