import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler {

	private static ArrayList<TaskCards> fileDetails;
	private static String fileName = null;
	private static int numberOfTasks = 0;
	
	public FileHandler(String fileName) {
		this.fileName = fileName;
	}
}
