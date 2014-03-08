import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Edit {
	/**
	 * First, edit will see whether the keyword exists.
	 * Next, it will check whether it's a date.
	 * If yes, it will search for the date in the to do list.
	 * If no, it will see whether it's a digit.
	 * If yes, it will search for the digit in the task name or task date in the list.
	 * If no, it will search for the keyword in the task name in the list.
	 * If not found, it will return the appropriate message.
	 * @param cmdArray
	 * @param fileLink
	 * @return
	 * @author Omar Khalid
	 */
	
	private static Scanner scan = new Scanner(System.in);
	private static int choice;
	private static ArrayList <TaskCard> originalList; //this is the cloned to do list
	private static ArrayList <TaskCard> editList; //this is the list that will be printed out on the screen
	private static ArrayList <Integer> editIndex; //this is to store the indices of the TaskCards that match the keyword
	//as of right now, I'm still not sure what editIndex will do
	private static TaskCard editedTask;
	private static boolean found = false;
	private static boolean isDate = false;
	private static boolean isDigit = false;
	private static String keyword = "";
	private static Calendar dateQuery = GregorianCalendar.getInstance();
	private static SimpleDateFormat dateAndTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat dateString = new SimpleDateFormat("dd/MM");
	private static SimpleDateFormat timeString = new SimpleDateFormat("HH:mm");
	private static String response = "";
	
	public static String executeEdit (String[] cmdArray, FileLinker fileLink) {
		if (cmdArray.length == 1) {
			return "Invalid query! Please enter a keyword to search for.";
		} else {
			//in case there's more than one keyword
			for (int i = 1; i < cmdArray.length; i++) {
				keyword.concat(cmdArray[i]);
				keyword += " ";
			}
			keyword.replaceFirst("\\s+$", ""); //remove any trailing whitespace
			originalList = fileLink.editHandling();
			isDate = checkQueryForDate(); //check if keyword is a date
			if (isDate == true) {
				for (int i = 0; i < Storage.numberOfIncompleteTasks; i++) {
					if (dateQuery.equals(originalList.get(i).getStartDay()) || dateQuery.equals(originalList.get(i).getEndDay())) {
						editIndex.add(i);
						editList.add(originalList.get(i));
						if (editList.isEmpty()) {
							return "Keyword not found! Try a different keyword.";
						} else {
							System.out.println("Which would you like to edit?");
							for (int j = 0; j < editList.size(); j++) {
								System.out.println(j+1 + ". " + editList.get(j).getTaskString());
							}
							String input = scan.nextLine();
							if (input.toLowerCase().equals("exit")) {
								return null;
							} else if (checkForDigit(input) == false) {
								//invalid input, ask for input again
							} else if (checkForDigit(input) == true) {
								choice = Integer.parseInt(input);
								if (choice > editList.size()) {
									//invalid input, ask for input again
								} else {
									editedTask = editList.get(choice - 1);
									System.out.println("What would you like to edit?");
									System.out.println("1. Name\n" + "2. Start date\n" + "3. End date\n"
														+ "4. Start time\n" + "5. End time\n" + "6. Priority");
									int nextChoice = scan.nextInt();
									switch (nextChoice) {
										case 1:
											String newName = scan.nextLine();
											editedTask.setName(newName);
											break;
										case 2:
											//change date
											break;
										case 3:
											//change date
											break;
										case 4:
											//change time
											break;
										case 5:
											//change time
											break;
										case 6:
											int priority = scan.nextInt();
											editedTask.setPriority(priority);
											break;
										default:
											break;
									}
								}
							}
						}
						//send new task card and index back to FileLinker
						fileLink.editHandler(editedTask, editIndex.get(choice));
					}
				}
				for (int i = 0; i < Storage.numberOfCompletedTasks; i++) {
					
				}
			} else {
				isDigit = checkForDigit(keyword);
			}
			
			if (isDigit == true) {
				
			}
			
			if (found == true) {
				return null;
			} else {
				return null;
			}
		}
	}

	private static boolean checkQueryForDate() {
		//check if keyword is a date
		try {
			dateQuery.setTime(dateAndTime.parse(keyword));
			dateQuery.setTime(dateString.parse(keyword));
			dateQuery.setTime(timeString.parse(keyword));
		} catch (ParseException e){
			return false;
		}
		return true;
	}
	
	private static boolean checkForDigit(String query) {
		//check if keyword is a digit
		try {
			Integer.parseInt(query);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
