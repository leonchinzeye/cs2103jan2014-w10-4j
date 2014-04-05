package application;

import java.util.ArrayList;
import java.util.Stack;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class TaskControllerMin {
	@FXML
	private AnchorPane anchor;
	@FXML
	private Pane dragPane;
	@FXML
	private ImageView closeButton;
	@FXML
	private ImageView minimizeButton;
	@FXML
	private Pane validPane;
	
	@FXML
	private TextField notification;
	@FXML
	private TextField command;
	
	private UI ui = new UI();
	private Stack<String> history = new Stack<String>();
	private Stack<String> forward = new Stack<String>();
	private CommandHandler commandHandle;
	private DataUI dataUI;
	private String lastInput = null;
	private int tableNo = 0;
	private boolean isEnterKey = false;
	private static double mouseDragOffsetX;
	private static double mouseDragOffsetY;
	Image closeButtonDefault = new Image("/closeButton.png");
	Image closeButtonHover = new Image("/closeButtonHover.png");
	Image minimizeButtonDefault = new Image("/minimizeButton.png");
	Image minimizeButtonHover = new Image("/minimizeButtonHover.png");
	
	private ObservableList<EventDataUI> incompleteEvents = FXCollections.observableArrayList();
	private ObservableList<TaskDataUI> incompleteTasks = FXCollections.observableArrayList();
	
	private String jedigreen = getClass().getResource("jedigreen.css").toExternalForm();
	private String sithred = getClass().getResource("sithred.css").toExternalForm();
	private String australia = getClass().getResource("australia.css").toExternalForm();
	private String italy = getClass().getResource("italy.css").toExternalForm();
	private ArrayList<String> themes = new ArrayList<String>();
	
	private static final int JEDI_GREEN = 0;
	private static final int SITH_RED = 1;
	private static final int AUSTRALIA = 2;
	private static final int ITALY = 3;
	private static int themeIndex = 0;
	
	public TaskControllerMin() {
		commandHandle = new CommandHandler();
		notification = new TextField();
		command = new TextField();
		anchor = new AnchorPane();
		validPane = new Pane();
		themes.add(jedigreen);
		themes.add(sithred);
		themes.add(australia);
		themes.add(italy);
	}
	
	@FXML
	private void initialize() {
		dragPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseDragOffsetX = event.getSceneX();
				mouseDragOffsetY = event.getSceneY();
			}
		});
		dragPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ui.primaryS.setX(event.getScreenX() - mouseDragOffsetX);
				ui.primaryS.setY(event.getScreenY() - mouseDragOffsetY);
			}
		});
		
		validPane.setStyle("-fx-background-color: green;");
		
		/*
		 * The lines below will set the task counter upon starting the program.
		 */
		dataUI = commandHandle.getDataUI();
	}
	
	/**
	 * The main method to take in input and send it to CommandHandler to execute
	 * @author Omar Khalid
	 */
	@FXML
	public void parseInput() {
		String lastInput = "";
		
		lastInput = command.getText();
		commandHistoryStorage(lastInput);
		executeCmd(lastInput);
	}

	public void executeCmd(String lastInput) {
	  String response;
	  changeTheme(lastInput);
	  if (lastInput.contains("add") || lastInput.contains("undo") || lastInput.contains("redo") || lastInput.contains("theme")) {
	  	dataUI = commandHandle.executeCmd(lastInput, tableNo);
	  	response = dataUI.getFeedback();
	  	notification.setText(response);
	  } else {
	  	notification.setText("Please try something else!");
	  }
		command.clear(); //clears the input text field
		command.requestFocus();
		
		/*
		 * Retrieves the new information to be shown in the tables.
		 * Also updates the task counter accordingly.
		 */
		setUI(ui);
  }

	private void changeTheme(String lastInput) {
		String[] inputArray = null;
		if (lastInput.contains("theme")) {
			inputArray = lastInput.split(" ", 2);
			
			if (!inputArray[1].isEmpty()) {
				String chosenTheme = inputArray[1];
				anchor.getStylesheets().removeAll(themes);
				if (chosenTheme.equalsIgnoreCase("Jedi")) {
					themeIndex = JEDI_GREEN;
				} else if (chosenTheme.equalsIgnoreCase("Sith")) {
					themeIndex = SITH_RED;
				} else if (chosenTheme.equalsIgnoreCase("Australia")) {
					themeIndex = AUSTRALIA;
				} else if (chosenTheme.equalsIgnoreCase("Italy")) {
					themeIndex = ITALY;
				}
				anchor.getStylesheets().add(themes.get(themeIndex));
			} else {
				notification.setText("You forgot to enter a theme to change to!");
			}
		}
  }
	
	private void changeThemeByInt(int index) {
		anchor.getStylesheets().removeAll(themes);
		if (index == 0) {
			themeIndex = JEDI_GREEN;
		} else if (index == 1) {
			themeIndex = SITH_RED;
		} else if (index == 2) {
			themeIndex = AUSTRALIA;
		} else if (index == 3) {
			themeIndex = ITALY;
		}
		anchor.getStylesheets().add(themes.get(themeIndex));
	}
	
	/**
	 * Retrieves the information to be shown in the tables.
	 * @param ui
	 * @author Omar Khalid
	 */
	public void setUI(UI ui) {
		this.ui = ui;
		
		/*
		 * The four lines below clears the table so that new information can be shown.
		 */
		incompleteEvents.removeAll(incompleteEvents);
		incompleteTasks.removeAll(incompleteTasks);
		
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		
		changeThemeByInt(themeIndex);
	}
	
	public void focusTextInput(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)) {
			command.requestFocus();
			command.end();
		}
	}
	
	@FXML
	public void commandTextFieldKeystrokes (KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)) {
			isEnterKey = true;
		} else {
			isEnterKey = false;
		}
		if (!isEnterKey) {
			setTableColumnStyle(key);
		}
		if (key.isControlDown() && key.getCode().equals(KeyCode.Z)) {
			undoShortcut();
		}
		if (key.isControlDown() && key.getCode().equals(KeyCode.Y)) {
			redoShortcut();
		}
		returnLastInput(key);
	}
	
	private void undoShortcut() {
		String response = "";
		
		dataUI = commandHandle.executeCmd("undo", tableNo);
		response = dataUI.getFeedback();
		notification.setText(response);
		
		setUI(ui);
  }
	
	private void redoShortcut() {
		String response = "";
		
		dataUI = commandHandle.executeCmd("redo", tableNo);
		response = dataUI.getFeedback();
		notification.setText(response);
		
		setUI(ui);
  }
	
	public void setTableColumnStyle(KeyEvent key) {
		String input = command.getText();
		
		commandBlank(input);
		commandSetNotification(input);
		
		if (input.matches("|add \\w.+|add \\w.+; \\w.+|add \\w.+ due by \\w.+")) {
			validPane.setStyle("-fx-background-color: green;");
		}
	}
	
	private void commandBlank(String input) {
	  if (input.matches("")) {
	  	notification.setText("Read me!");
			validPane.setStyle("-fx-background-color: green;");
		}
  }

	private void commandSetNotification(String input) {
	  if (input.matches("\\w|\\w.+|/x")) {
			validPane.setStyle("-fx-background-color: red;");
			switch(input) {
				case "a":
					notification.setText("add <Name> due by <End> OR add <Name>; <Start> to <End>");
					break;
				case "undo":
					validPane.setStyle("-fx-background-color: green;");
					break;
				case "redo":
					validPane.setStyle("-fx-background-color: green;");
					break;
				case "/x":
					validPane.setStyle("-fx-background-color: green;");
					break;
				default:
					break;
			}
		}
  }
	
	private void commandHistoryStorage(String lastInput) {
	  if (!forward.empty()) {
			Stack<String> temp = new Stack<String>();
			while (!forward.empty()) {
				temp.add(forward.pop());
			}
			forward.add(lastInput);
			while (!temp.empty()) {
				forward.add(temp.pop());
			}
		} else {
			history.add(lastInput);
		}
	}

	public void returnLastInput(KeyEvent key) {		
		if(key.getCode().equals(KeyCode.UP)) {
			if(this.lastInput != null) {
				forward.push(this.lastInput);
			}
			
			if (!history.isEmpty()) {
				this.lastInput = history.pop();
				command.setText(this.lastInput);
				command.end();
			} else {
				this.lastInput = null;
				command.clear();
			}
		} else if(key.getCode().equals(KeyCode.DOWN)) {
			if(this.lastInput != null) {
				history.push(this.lastInput);
			}
			
			if (!forward.isEmpty()) {
				this.lastInput = forward.pop();
				command.setText(this.lastInput);
				command.end();
			} else {
				this.lastInput = null;
				command.clear();
			}
		}
	}
	
	@FXML
	public void closeEnter (MouseEvent mouse) {
		closeButton.setImage(closeButtonHover);
	}
	
	@FXML
	public void closeExit (MouseEvent mouse) {
		closeButton.setImage(closeButtonDefault);
	}
	
	@FXML
	public void closeWindow (MouseEvent mouse) {
		Platform.exit();
		System.exit(0);
	}
	
	@FXML
	public void minimizeEnter (MouseEvent mouse) {
		minimizeButton.setImage(minimizeButtonHover);
	}
	
	@FXML
	public void minimizeExit (MouseEvent mouse) {
		minimizeButton.setImage(minimizeButtonDefault);
	}
	
	@FXML
	public void minimizeWindow (MouseEvent mouse) {
		ui.primaryS.setIconified(true);
	}
	
	public ObservableList<EventDataUI> getIncEvents() {
		return incompleteEvents;
	}
	
	public ObservableList<TaskDataUI> getIncTasks() {
		return incompleteTasks;
	}
	
	public void setIncEvents(ObservableList<EventDataUI> incEvents) {
		this.incompleteEvents = incEvents;
	}
	
	public void setIncTasks(ObservableList<TaskDataUI> incTasks) {
		this.incompleteTasks = incTasks;
	}
	
	public DataUI getDataUI() {
		return dataUI;
	}
	
	public void setDataUI(DataUI dataUI) {
		this.dataUI = dataUI;
		setUI(ui);
	}
	
	public int getTheme() {
		return themeIndex;
	}
	
	public void setTheme(int theme) {
		TaskControllerMin.themeIndex = theme;
	}
}
