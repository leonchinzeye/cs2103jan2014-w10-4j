package application;

import java.util.Stack;

import application.CommandHandler.COMMAND_TYPE;
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

public class TaskController {
	@FXML
	private AnchorPane anchor;
	@FXML
	private AnchorPane helpAnchor;
	@FXML
	private AnchorPane helpAnchor2;
	@FXML
	private ImageView closeButton;
	@FXML
	private ImageView minimizeButton;
	@FXML
	private TabPane tab;
	@FXML
	private Tab incompleteTab;
	@FXML
	private Tab completeTab;
	@FXML
	private Tab helpTab;
	@FXML
	private Pane validPane;
	
	@FXML
	private TextField notification;
	@FXML
	private TextField command;
	@FXML
	private Text eventCounter;
	@FXML
	private Text taskCounter;
	
	@FXML
	private Accordion incompleteAccordion;
	@FXML
	private Accordion completeAccordion;
	@FXML
	private TitledPane eventPaneIncomplete;
	@FXML
	private TitledPane taskPaneIncomplete;
	@FXML
	private TitledPane eventPaneComplete;
	@FXML
	private TitledPane taskPaneComplete;
	
	@FXML
	private TableView<TaskDataUI> taskTableIncomplete;
	@FXML
	private TableView<EventDataUI> eventTableIncomplete;
	@FXML
	private TableView<TaskDataUI> taskTableComplete;
	@FXML
	private TableView<EventDataUI> eventTableComplete;
	
	@FXML
	private TableColumn<TaskDataUI, String> colTaskIDIncomplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskPriorityIncomplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskNameIncomplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskEndDateIncomplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskEndTimeIncomplete;
	
	@FXML
	private TableColumn<EventDataUI, String> colEventIDIncomplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventPriorityIncomplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventNameIncomplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventStartDateIncomplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventStartTimeIncomplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventEndDateIncomplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventEndTimeIncomplete;
	
	@FXML
	private TableColumn<TaskDataUI, String> colTaskIDComplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskPriorityComplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskNameComplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskEndDateComplete;
	@FXML
	private TableColumn<TaskDataUI, String> colTaskEndTimeComplete;
	
	@FXML
	private TableColumn<EventDataUI, String> colEventIDComplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventPriorityComplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventNameComplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventStartDateComplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventStartTimeComplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventEndDateComplete;
	@FXML
	private TableColumn<EventDataUI, String> colEventEndTimeComplete;
	
	private UI ui = new UI();
	private Stack<String> history = new Stack<String>();
	private Stack<String> forward = new Stack<String>();
	private CommandHandler commandHandle;
	private DataUI dataUI;
	private String lastInput = null;
	private static double mouseDragOffsetX;
	private static double mouseDragOffsetY;
	Image closeButtonDefault = new Image("/closeButton.png");
	Image closeButtonHover = new Image("/closeButtonHover.png");
	Image minimizeButtonDefault = new Image("/minimizeButton.png");
	Image minimizeButtonHover = new Image("/minimizeButtonHover.png");
	
	private final ObservableList<EventDataUI> incompleteEvents = FXCollections.observableArrayList();
	private final ObservableList<TaskDataUI> incompleteTasks = FXCollections.observableArrayList();
	private final ObservableList<EventDataUI> completedEvents = FXCollections.observableArrayList();
	private final ObservableList<TaskDataUI> completedTasks = FXCollections.observableArrayList();
	
	public TaskController() {
		commandHandle = new CommandHandler();
		dataUI = new DataUI();
		eventCounter = new Text("Events: 0");
		taskCounter = new Text("Tasks: 0");
		validPane = new Pane();
		helpAnchor = new AnchorPane();
	}
	
	@FXML
	private void initialize() {
		anchor.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseDragOffsetX = event.getSceneX();
				mouseDragOffsetY = event.getSceneY();
			}
		});
		anchor.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ui.primaryS.setX(event.getScreenX() - mouseDragOffsetX);
				ui.primaryS.setY(event.getScreenY() - mouseDragOffsetY);
			}
		});
		
		colTaskIDIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("ID"));
		colTaskPriorityIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("priority"));
		colTaskNameIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("name"));
		colTaskEndDateIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("endDate"));
		colTaskEndTimeIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("endTime"));
		
		colEventIDIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("ID"));
		colEventPriorityIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("priority"));
		colEventNameIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("name"));
		colEventStartDateIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("startDate"));
		colEventStartTimeIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("startTime"));
		colEventEndDateIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("endDate"));
		colEventEndTimeIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("endTime"));
		
		colTaskIDComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("ID"));
		colTaskPriorityComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("priority"));
		colTaskNameComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("name"));
		colTaskEndDateComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("endDate"));
		colTaskEndTimeComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>("endTime"));
		
		colEventIDComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("ID"));
		colEventPriorityComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("priority"));
		colEventNameComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("name"));
		colEventStartDateComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("startDate"));
		colEventStartTimeComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("startTime"));
		colEventEndDateComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("endDate"));
		colEventEndTimeComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("endTime"));
		
		validPane.setStyle("-fx-background-color: green;");
		
		/*
		 * The lines below will set the task counter upon starting the program.
		 */
		dataUI = commandHandle.getDataUI();
		
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
		
		eventCounter.setText("Events: " + incompleteEvents.size());
		taskCounter.setText("Tasks: " + incompleteTasks.size());
		
		incompleteEvents.removeAll(incompleteEvents);
		incompleteTasks.removeAll(incompleteTasks);
		completedEvents.removeAll(completedEvents);
		completedTasks.removeAll(completedTasks);
	}
	
	/**
	 * The main method to take in input and send it to CommandHandler to execute
	 * @author Omar Khalid
	 */
	@FXML
	public void parseInput() {
		String response = "";
		String lastInput = "";
		
		taskTableIncomplete.getSelectionModel().select(0);
		lastInput = command.getText();
		history.add(lastInput); //stores the last entered line to command history to be accessed by the up/down buttons.
		
		if (lastInput.equals("help")) {
			tab.getSelectionModel().select(helpTab);
		} else if (lastInput.equalsIgnoreCase("view incomplete")) {
			tab.getSelectionModel().select(incompleteTab);
		} else if (lastInput.equalsIgnoreCase("view completed")) {
			tab.getSelectionModel().select(completeTab);
		} else if (lastInput.equalsIgnoreCase("view events")) {
			if (incompleteTab.isSelected()) {
				eventPaneIncomplete.setExpanded(true);
			} else if (completeTab.isSelected()) {
				eventPaneComplete.setExpanded(true);
			}
		} else if (lastInput.equalsIgnoreCase("view tasks")) {
			if (incompleteTab.isSelected()) {
				taskPaneIncomplete.setExpanded(true);
			} else if (completeTab.isSelected()) {
				taskPaneComplete.setExpanded(true);
			}
		} else if (lastInput.equalsIgnoreCase("view incomplete events")) {
			tab.getSelectionModel().select(incompleteTab);
			eventPaneIncomplete.setExpanded(true);
		} else if (lastInput.equalsIgnoreCase("view incomplete tasks")) {
			tab.getSelectionModel().select(incompleteTab);
			taskPaneIncomplete.setExpanded(true);
		} else if (lastInput.equalsIgnoreCase("view completed events")) {
			tab.getSelectionModel().select(completeTab);
			eventPaneComplete.setExpanded(true);
		} else if (lastInput.equalsIgnoreCase("view completed tasks")) {
			tab.getSelectionModel().select(completeTab);
			taskPaneComplete.setExpanded(true);
		}
		anchor.requestFocus();
			
		dataUI = commandHandle.executeCmd(lastInput);
		response = dataUI.getFeedback();
		notification.setText(response);
		command.clear(); //clears the input text field
		command.requestFocus();
		
		/*
		 * Retrieves the new information to be shown in the tables.
		 * Also updates the task counter accordingly.
		 */
		setUI(ui);
		updateCounter();
	}

	/**
	 * Updates the counter based on the tab currently shown.
	 * @author Omar Khalid
	 */
	private void updateCounter() {
	  if (tab.getSelectionModel().isSelected(0)) {
			eventCounter.setText("Events: " + incompleteEvents.size());
			taskCounter.setText("Tasks: " + incompleteTasks.size());
		} else if (tab.getSelectionModel().isSelected(1)) {
			eventCounter.setText("Events: " + completedEvents.size());
			taskCounter.setText("Tasks: " + completedTasks.size());
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
		eventTableIncomplete.setItems(incompleteEvents);
		taskTableIncomplete.setItems(incompleteTasks);
		
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
		eventTableComplete.setItems(completedEvents);
		taskTableComplete.setItems(completedTasks);
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
			commandHandle.executeCmd("");
			backToMain();
		}
		if (!helpTab.isSelected()) {
			focusTextInput(key);//check if the user wants to type something
		}
		scrollTable(key);
		changeTab(key);//shortcuts to change between tabs
		//changePanel(key);//shortcuts to change between panels
	}
	
	/**
	 * Sets the view to default.
	 * @author Omar Khalid
	 */
	private void backToMain() {
		eventTableIncomplete.getSelectionModel().clearSelection();
		taskTableIncomplete.getSelectionModel().clearSelection();
		eventTableComplete.getSelectionModel().clearSelection();
		taskTableComplete.getSelectionModel().clearSelection();
	  tab.getSelectionModel().select(0);
	  eventPaneIncomplete.setExpanded(true);
	  setUI(ui);
	  updateCounter();
	  helpAnchor.setVisible(false);
	  helpAnchor2.setVisible(false);
	  notification.setText("Read me!");
	  command.clear();
	  command.setPromptText("Feed me!");
	  command.setMouseTransparent(false);
	  command.setFocusTraversable(true);
  }
	
	public void focusTextInput(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)) {
			command.requestFocus();
			command.end();
		}
	}
	
	/**
	 * Scrolls the currently shown table up or down.
	 * @param key
	 * @author Omar Khalid
	 */
	public void scrollTable(KeyEvent key) {
		if (key.isControlDown() && key.getCode().equals(KeyCode.PERIOD)) {
			if (tab.getSelectionModel().isSelected(0)) {
				if (eventPaneIncomplete.isExpanded()) {
					ScrollBar bar = (ScrollBar) eventTableIncomplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() + 0.5);
				} else {
					ScrollBar bar = (ScrollBar) taskTableIncomplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() + 0.5);
					System.out.println(bar.getValue());
				}
			} else if (tab.getSelectionModel().isSelected(1)) {
				if (eventPaneComplete.isExpanded()) {
					ScrollBar bar = (ScrollBar) eventTableComplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() + 0.5);
				} else {
					ScrollBar bar = (ScrollBar) taskTableComplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() + 0.5);
				}
			}
		} else if (key.isControlDown() && key.getCode().equals(KeyCode.COMMA)) {
			if (tab.getSelectionModel().isSelected(0)) {
				if (eventPaneIncomplete.isExpanded()) {
					ScrollBar bar = (ScrollBar) eventTableIncomplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() - 0.5);
				} else {
					ScrollBar bar = (ScrollBar) taskTableIncomplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() - 0.5);
					System.out.println(bar.getValue());
				}
			} else if (tab.getSelectionModel().isSelected(1)) {
				if (eventPaneComplete.isExpanded()) {
					ScrollBar bar = (ScrollBar) eventTableComplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() - 0.5);
				} else {
					ScrollBar bar = (ScrollBar) taskTableComplete.lookup(".scroll-bar:vertical");
					bar.setValue(bar.getValue() - 0.5);
				}
			}
		}
	}
	
	/**
	 * Change the panel to be expanded with Ctrl+UP or Ctrl+DOWN.
	 * @param key
	 */
	/*@FXML
	public void changePanel(KeyEvent key) {
		if (key.isControlDown() && (key.getCode().equals(KeyCode.UP) || key.getCode().equals(KeyCode.DOWN))) {
			if (eventPaneIncomplete.isExpanded()) {
				taskPaneIncomplete.setExpanded(true);
				taskPaneComplete.setExpanded(true);
			} else if (taskPaneIncomplete.isExpanded()) {
				eventPaneIncomplete.setExpanded(true);
				eventPaneComplete.setExpanded(true);			
			}
		}
	}*/
	
	/**
	 * 1. Change the tab between only the Incomplete and Complete tabs with Ctrl+Tab.
	 * 2. Change to the Help page with Ctrl+H.
	 * 3. Change between Help pages with Left and Right arrow keys.
	 * @param key
	 * @author Omar Khalid
	 */
	@FXML
	public void changeTab(KeyEvent key) {
		if (key.isControlDown() && key.getCode().equals(KeyCode.TAB)) {						//1
			switchTabs();
		} else if (key.isControlDown() && key.getCode().equals(KeyCode.H)) {			//2
			tab.getSelectionModel().select(helpTab);
		} else if (helpTab.isSelected() && key.getCode().equals(KeyCode.RIGHT)) {	//3
			helpAnchor.setVisible(false);
			helpAnchor2.setVisible(true);
			notification.setText("Listed above are the accepted commands and their proper formats.");
			command.setPromptText("Use the UP and DOWN arrow keys to scroll through your command history.");
		} else if (helpTab.isSelected() && key.getCode().equals(KeyCode.LEFT)) {	//3
			helpAnchor.setVisible(true);
			helpAnchor2.setVisible(false);
			notification.setText("This will return responses to you based on your commands.");
			command.setPromptText("This is where you enter your commands.");
		}
	}

	private void switchTabs() {
	  command.setMouseTransparent(false);
	  command.setFocusTraversable(true);
	  if (!tab.getSelectionModel().isSelected(0)) {
	  	tab.getSelectionModel().select(incompleteTab);
	  	helpAnchor.setVisible(false);
	  	helpAnchor2.setVisible(false);
	  } else if (!tab.getSelectionModel().isSelected(1)){
	  	tab.getSelectionModel().select(completeTab);
	  	helpAnchor.setVisible(false);
	  	helpAnchor2.setVisible(false);
	  }
  }
	
	@FXML
	public void openIncompleteTab() {
		notification.setText("Read me!");
	  command.setPromptText("Feed me!");
		eventCounter.setText("Events: " + incompleteEvents.size());
		taskCounter.setText("Tasks: " + incompleteTasks.size());
		notification.setDisable(false);
		command.setDisable(false);
		helpAnchor.setVisible(false);
	}
	
	@FXML
	public void openCompleteTab() {
		notification.setText("Read me!");
	  command.setPromptText("Feed me!");
		eventCounter.setText("Events: " + completedEvents.size());
		taskCounter.setText("Tasks: " + completedTasks.size());
		notification.setDisable(false);
		command.setDisable(false);
		helpAnchor.setVisible(false);
	}
	
	@FXML
	public void openHelpTab() {
		eventCounter.setText("Events: 0");
		taskCounter.setText("Tasks: 0");
		notification.setText("This will return responses to you based on your commands.");
		command.setPromptText("This is where you enter your commands.");
		command.setMouseTransparent(true);
		command.setFocusTraversable(false);
		helpAnchor.setVisible(true);
	}
	
	@FXML
	public void commandTextFieldKeystrokes (KeyEvent key) {
		setTableColumnStyle(key);
		returnLastInput(key);
	}
	
	public void setTableColumnStyle(KeyEvent key) {
		String[] inputArray = null;
		String input = command.getText();
		int rowID = 0;
		
		if (input.matches("")) {
			eventTableIncomplete.getSelectionModel().clearSelection();
			taskTableIncomplete.getSelectionModel().clearSelection();
			eventTableComplete.getSelectionModel().clearSelection();
			taskTableComplete.getSelectionModel().clearSelection();
			notification.setText("Read me!");
		}
		
		if (input.matches("\\b.+|/x")) {
			switch(input) {
				case "add":
					notification.setText("add <Name> due by <End> OR add <Name>; <Start> to <End>");
					break;
				case "del":
					notification.setText("del<t/e/tc/ec> <Integer>");
					break;
				case "edit":
					notification.setText("edit<t/e> <Integer> <Field>: <Edited entry>");
					break;
				case "mark":
					notification.setText("mark<t/e> <Integer>");
					break;
				case "unmark":
					notification.setText("unmark<t/e> <Integer>");
					break;
				case "search":
					notification.setText("search <Query OR Priority OR Date>");
					break;
				case "view":
					notification.setText("view <Tab Name/Panel Name>");
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
					validPane.setStyle("-fx-background-color: red;");
					break;
			}
		}
		
		if (input.matches("\\w.+ \\d")) {
			eventTableIncomplete.getSelectionModel().setCellSelectionEnabled(false);
			taskTableIncomplete.getSelectionModel().setCellSelectionEnabled(false);
			
			inputArray = input.split(" ");
			rowID = Integer.parseInt(inputArray[1]) - 1;
			
			switch(inputArray[0]) {
				case "dele":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(incompleteTab);
					eventPaneIncomplete.setExpanded(true);
					eventTableIncomplete.scrollTo(rowID);
					eventTableIncomplete.getSelectionModel().select(rowID);
					break;
				case "delt":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(incompleteTab);
					taskPaneIncomplete.setExpanded(true);
					taskTableIncomplete.scrollTo(rowID);
					taskTableIncomplete.getSelectionModel().select(rowID);
					break;
				case "delec":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(completeTab);
					eventPaneComplete.setExpanded(true);
					eventTableComplete.scrollTo(rowID);
					eventTableComplete.getSelectionModel().select(rowID);
					break;
				case "deltc":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(completeTab);
					taskPaneComplete.setExpanded(true);
					taskTableComplete.scrollTo(rowID);
					taskTableComplete.getSelectionModel().select(rowID);
					break;
				case "marke":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(incompleteTab);
					eventPaneIncomplete.setExpanded(true);
					eventTableIncomplete.scrollTo(rowID);
					eventTableIncomplete.getSelectionModel().select(rowID);
					break;
				case "markt":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(incompleteTab);
					taskPaneIncomplete.setExpanded(true);
					taskTableIncomplete.scrollTo(rowID);
					taskTableIncomplete.getSelectionModel().select(rowID);
					break;
				case "unmarke":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(completeTab);
					eventPaneComplete.setExpanded(true);
					eventTableComplete.scrollTo(rowID);
					eventTableComplete.getSelectionModel().select(rowID);
					break;
				case "unmarkt":
					validPane.setStyle("-fx-background-color: green;");
					tab.getSelectionModel().select(completeTab);
					taskPaneComplete.setExpanded(true);
					taskTableComplete.scrollTo(rowID);
					taskTableComplete.getSelectionModel().select(rowID);
					break;
				case "edite":
					tab.getSelectionModel().select(incompleteTab);
					eventPaneIncomplete.setExpanded(true);
					eventTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
					eventTableIncomplete.scrollTo(rowID);
					eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
					break;
				case "editt":
					tab.getSelectionModel().select(incompleteTab);
					taskPaneIncomplete.setExpanded(true);
					taskTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
					taskTableIncomplete.scrollTo(rowID);
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskIDIncomplete);
					break;
				default:
					validPane.setStyle("-fx-background-color: red;");
					break;
			}
		} else if (input.matches("edite \\d \\w.+")) {
			inputArray = input.split(" ");
			rowID = Integer.parseInt(inputArray[1]) - 1;
			eventTableIncomplete.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			
			switch (inputArray[2]) {
				case "Name":
					eventTableIncomplete.getSelectionModel().clearSelection();
					eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
					eventTableIncomplete.getSelectionModel().select(rowID, colEventNameIncomplete);
					break;
				case "Priority":
					eventTableIncomplete.getSelectionModel().clearSelection();
					eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
					eventTableIncomplete.getSelectionModel().select(rowID, colEventPriorityIncomplete);
					break;
				case "Start":
					eventTableIncomplete.getSelectionModel().clearSelection();
					eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
					eventTableIncomplete.getSelectionModel().select(rowID, colEventStartDateIncomplete);
					eventTableIncomplete.getSelectionModel().select(rowID, colEventStartTimeIncomplete);
					break;
				case "End":
					eventTableIncomplete.getSelectionModel().clearSelection();
					eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
					eventTableIncomplete.getSelectionModel().select(rowID, colEventEndDateIncomplete);
					eventTableIncomplete.getSelectionModel().select(rowID, colEventEndTimeIncomplete);
					break;
				default:
					break;
			}
		} else if (input.matches("editt \\d \\w.+")) {
			inputArray = input.split(" ");
			rowID = Integer.parseInt(inputArray[1]) - 1;			
			taskTableIncomplete.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			
			switch (inputArray[2]) {
				case "Name":
					taskTableIncomplete.getSelectionModel().clearSelection();
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskIDIncomplete);
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskNameIncomplete);
					break;
				case "Priority":
					taskTableIncomplete.getSelectionModel().clearSelection();
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskIDIncomplete);
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskPriorityIncomplete);
					break;
				case "End":
					taskTableIncomplete.getSelectionModel().clearSelection();
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskIDIncomplete);
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskEndDateIncomplete);
					taskTableIncomplete.getSelectionModel().select(rowID, colTaskEndTimeIncomplete);
					break;
				default:
					break;
			}
		} 
		
		if (input.matches("edite \\d .+\\w: .+\\w|editt \\d .+\\w: .+\\w|add \\w.+; \\w.+|add \\w.+ due by \\w.+") ||
				input.matches("view Incomplete|view Complete|view Events|view Tasks")) {
			validPane.setStyle("-fx-background-color: green;");
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
}
