package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
	
	private final int TASK_INC = 1;
	private final int EVENT_INC = 2;
	private final int TASK_COM = 3;
	private final int EVENT_COM = 4;
	
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
	private ObservableList<EventDataUI> completedEvents = FXCollections.observableArrayList();
	private ObservableList<TaskDataUI> completedTasks = FXCollections.observableArrayList();
	
	private String jedigreen = getClass().getResource("jedigreen.css").toExternalForm();
	private String sithred = getClass().getResource("sithred.css").toExternalForm();
	private String australia = getClass().getResource("australia.css").toExternalForm();
	private String italy = getClass().getResource("italy.css").toExternalForm();
	private ArrayList<String> themes = new ArrayList<String>();
	
	private static final int JEDI_GREEN = 0;
	private static final int SITH_RED = 1;
	private static final int AUSTRALIA = 2;
	private static final int ITALY = 3;
	
	public TaskControllerMin() {
		commandHandle = new CommandHandler();
		dataUI = new DataUI();
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
		
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
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
		dataUI = commandHandle.executeCmd(lastInput, tableNo);
		response = dataUI.getFeedback();
		notification.setText(response);
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
				int themeIndex = 0;
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
		completedEvents.removeAll(completedEvents);
		completedTasks.removeAll(completedTasks);
		
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
	}
	
	/**
	 * The default keyboard shortcuts that allow the user to switch between panels and tabs
	 * as well as to change the focus to the text field without using the mouse.
	 * @param key
	 * @author Omar Khalid
	 */
	@FXML
	public void keystrokes(KeyEvent key) {		
		if (key.getCode().equals(KeyCode.ESCAPE)) {
			commandHandle.executeCmd("", tableNo);
			backToMain();
		}
	}
	
	/**
	 * Sets the view to default.
	 * @author Omar Khalid
	 */
	private void backToMain() {
		setUI(ui);
	  notification.setText("Read me!");
	  command.clear();
	  command.setPromptText("Feed me!");
	  command.setMouseTransparent(false);
	  command.setFocusTraversable(true);
	  tableNo = EVENT_INC;
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
		returnLastInput(key);
	}
	
	public void setTableColumnStyle(KeyEvent key) {
		String input = command.getText();
		
		commandBlank(input);
		commandSetNotification(input);
		
		if (input.matches("\\w.+ \\d+") && !input.contains("add")) {
			commandGeneralSelectionStyle(input);
		}
		
		if (input.matches("edit \\d+ \\w.+: \\w.+|edite \\d+ \\w.+: \\w.+|editt \\d+ \\w.+: \\w.+|add \\w.+|add \\w.+; \\w.+|add \\w.+ due by \\w.+") ||
				input.matches("view Incomplete|view Completed|view Events|view Tasks|view Incomplete Events|view Incomplete Tasks|view Completed Events|view Completed Tasks")) {
			validPane.setStyle("-fx-background-color: green;");
		}
		
		currentTableShown();
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
					currentTableShown();
					break;
				case "d":
					notification.setText("del/t/e/tc/ec <ID>");
					break;
				case "e":
					notification.setText("edit/t/e <ID> <Field>: <Edited entry>");
					break;
				case "m":
					notification.setText("mark/t/e <ID>");
					break;
				case "unm":
					notification.setText("unmark/t/e <ID>");
					break;
				case "s":
					notification.setText("search <Query OR Priority OR Date>");
					break;
				case "v":
					if (tableNo == EVENT_INC) {
						notification.setText("view Completed/Tasks");
					} else if (tableNo == TASK_INC) {
						notification.setText("view Completed/Events");
					} else if (tableNo == EVENT_COM) {
						notification.setText("view Incomplete/Tasks");
					} else if (tableNo == TASK_COM) {
						notification.setText("view Incomplete/Events");
					}
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
	
	private void commandGeneralSelectionStyle(String input) {
	  String[] inputArray;
	  
	  inputArray = input.split(" ");
	  
	  switch(inputArray[0]) {
	  	case "del":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "dele":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "delt":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "delec":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "deltc":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "mark":
	  		validPane.setStyle("-fx-background-color: green;");
	  		currentTableShown();
	  		break;
	  	case "marke":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "markt":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "unmark":
	  		validPane.setStyle("-fx-background-color: green;");
	  		currentTableShown();
	  		break;
	  	case "unmarke":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "unmarkt":
	  		validPane.setStyle("-fx-background-color: green;");
	  		break;
	  	case "edit":
	  		currentTableShown();
	  		break;
	  	case "edite":
	  		break;
	  	case "editt":
	  		break;
	  	default:
	  		validPane.setStyle("-fx-background-color: red;");
	  		break;
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
		
		if (command.getText().matches("\\w.+ \\d+") && !command.getText().contains("add")) {
			commandGeneralSelectionStyle(command.getText());
		}
	}
	
	private void currentTableShown() {
	  if (incompleteTab.isSelected()) {
			if (eventPaneIncomplete.isExpanded()) {
				tableNo = EVENT_INC;
			} else if (taskPaneIncomplete.isExpanded()) {
				tableNo = TASK_INC;
			}
		} else if (completeTab.isSelected()) {
			if (eventPaneComplete.isExpanded()) {
				tableNo = EVENT_COM;
			} else if (taskPaneComplete.isExpanded()) {
				tableNo = TASK_COM;
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
	
	public ObservableList<EventDataUI> getComEvents() {
		return completedEvents;
	}
	
	public ObservableList<TaskDataUI> getIncTasks() {
		return incompleteTasks;
	}
	
	public ObservableList<TaskDataUI> getComTasks() {
		return completedTasks;
	}
	
	public void setIncEvents(ObservableList<EventDataUI> incEvents) {
		this.incompleteEvents = incEvents;
	}
	
	public void setComEvents(ObservableList<EventDataUI> comEvents) {
		this.completedEvents = comEvents;
	}
	
	public void setIncTasks(ObservableList<TaskDataUI> incTasks) {
		this.incompleteTasks = incTasks;
	}
	
	public void setComTasks(ObservableList<TaskDataUI> comTasks) {
		this.completedTasks = comTasks;
	}
}
