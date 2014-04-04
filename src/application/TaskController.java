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

public class TaskController {
	@FXML
	private AnchorPane anchor;
	@FXML
	private AnchorPane helpAnchor;
	@FXML
	private AnchorPane helpAnchor2;
	@FXML
	private Pane dragPane;
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
	
	private final int TASK_INC = 1;
	private final int EVENT_INC = 2;
	private final int TASK_COM = 3;
	private final int EVENT_COM = 4;
	
	private String testingResponse;
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
	
	public TaskController() {
		commandHandle = new CommandHandler();
		dataUI = new DataUI();
		notification = new TextField();
		command = new TextField();
		anchor = new AnchorPane();
		eventTableIncomplete = new TableView<EventDataUI>();
		taskTableIncomplete = new TableView<TaskDataUI>();
		eventTableComplete = new TableView<EventDataUI>();
		taskTableComplete = new TableView<TaskDataUI>();
		tab = new TabPane();
		eventCounter = new Text("Events: 0");
		taskCounter = new Text("Tasks: 0");
		validPane = new Pane();
		helpAnchor = new AnchorPane();
		testingResponse = "";
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
		
		final ObservableList<Integer> highlightOngoingRows = FXCollections.observableArrayList();
		final ObservableList<Integer> highlightExpiredRows = FXCollections.observableArrayList();
		
		for (int i = 0; i < incompleteEvents.size(); i++) {
			if (incompleteEvents.get(i).getIsOngoing()) {
				highlightOngoingRows.add(i);
			} else if (incompleteEvents.get(i).getIsExpired()) {
				highlightExpiredRows.add(i);
			}
		}
		
		eventTableIncomplete.setRowFactory(new Callback<TableView<EventDataUI>, TableRow<EventDataUI>>() {
			@Override
			public TableRow<EventDataUI> call(TableView<EventDataUI> tableView) {
				final TableRow<EventDataUI> row = new TableRow<EventDataUI>() {
					@Override
					protected void updateItem(EventDataUI event, boolean empty){
						super.updateItem(event, empty);
						if (highlightOngoingRows.contains(getIndex())) {
							if (!getStyleClass().contains("highlightOngoing")) {
								getStyleClass().add("highlightOngoing");
							}
						} else if (highlightExpiredRows.contains(getIndex())){
							if (!getStyleClass().contains("highlightExpired")) {
								getStyleClass().add("highlightExpired");
							}
						} else {
							getStyleClass().removeAll(Collections.singleton("highlightOngoing"));
							getStyleClass().removeAll(Collections.singleton("highlightExpired"));
						}
					}
				};
				return row;
			}
		});
		
		incompleteEvents.removeAll(incompleteEvents);
		incompleteTasks.removeAll(incompleteTasks);
		completedEvents.removeAll(completedEvents);
		completedTasks.removeAll(completedTasks);
		
		currentTableShown();
		System.out.println("Main UI initialized.");
	}
	
	/**
	 * The main method to take in input and send it to CommandHandler to execute
	 * @author Omar Khalid
	 */
	@FXML
	public void parseInput() {
		String lastInput = "";
		
		taskTableIncomplete.getSelectionModel().select(0);
		lastInput = command.getText();
		commandHistoryStorage(lastInput);
		executeCmd(lastInput);
	}

	public void executeCmd(String lastInput) {
	  String response;
	  viewCmd(lastInput);
	  changeTheme(lastInput);
		dataUI = commandHandle.executeCmd(lastInput, tableNo);
		response = dataUI.getFeedback();
		testingResponse = response;
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

	private void viewCmd(String lastInput) {
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
	  
	  currentTableShown();
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
		
		FileLinker fileLink = new FileLinker();
		RefreshUI.executeRefresh(fileLink, dataUI);
		
		/*
		 * The four lines below clears the table so that new information can be shown.
		 */
		completedEvents.removeAll(completedEvents);
		completedTasks.removeAll(completedTasks);
		
		incompleteEvents.removeAll(incompleteEvents);
		incompleteTasks.removeAll(incompleteTasks);
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		
		eventTableIncomplete.setItems(incompleteEvents);
		taskTableIncomplete.setItems(incompleteTasks);
		
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
		eventTableComplete.setItems(completedEvents);
		taskTableComplete.setItems(completedTasks);
		
		System.out.println("Max Incomplete Tasks 2:");
		for (int i = 0; i < incompleteTasks.size(); i++) {
			System.out.println(incompleteTasks.get(i).getName());
		}
		System.out.println("\n");
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
		if (!helpTab.isSelected()) {
			focusTextInput(key);//check if the user wants to type something
		}
		changeTab(key);//shortcuts to change between tabs
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
	  tableNo = EVENT_INC;
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
	@FXML
	public void scrollTable(KeyEvent key) {
		if (key.isControlDown() && key.getCode().equals(KeyCode.PERIOD)) {
			if (tableNo == EVENT_INC) {
				ScrollBar bar = (ScrollBar) eventTableIncomplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() >= 0.0 && bar.getValue() < 1.0) {
					bar.setValue(bar.getValue() + 0.1);
				}
			} else if (tableNo == TASK_INC) {
				ScrollBar bar = (ScrollBar) taskTableIncomplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() >= 0.0 && bar.getValue() < 1.0) {
					bar.setValue(bar.getValue() + 0.1);
				}
			} else if (tableNo == EVENT_COM) {
				ScrollBar bar = (ScrollBar) eventTableComplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() >= 0.0 && bar.getValue() < 1.0) {
					bar.setValue(bar.getValue() + 0.1);
				}
			} else if (tableNo == TASK_COM) {
				ScrollBar bar = (ScrollBar) taskTableComplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() >= 0.0 && bar.getValue() < 1.0) {
					bar.setValue(bar.getValue() + 0.1);
				}
			}
		} else if (key.isControlDown() && key.getCode().equals(KeyCode.COMMA)) {
			if (tableNo == EVENT_INC) {
				ScrollBar bar = (ScrollBar) eventTableIncomplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() > 0.0 && bar.getValue() <= 1.0) {
					bar.setValue(bar.getValue() - 0.1);
				}
			} else if (tableNo == TASK_INC) {
				ScrollBar bar = (ScrollBar) taskTableIncomplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() > 0.0 && bar.getValue() <= 1.0) {
					bar.setValue(bar.getValue() - 0.1);
				}
			} else if (tableNo == EVENT_COM) {
				ScrollBar bar = (ScrollBar) eventTableComplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() > 0.0 && bar.getValue() <= 1.0) {
					bar.setValue(bar.getValue() - 0.1);
				}
			} else if (tableNo == TASK_COM) {
				ScrollBar bar = (ScrollBar) taskTableComplete.lookup(".scroll-bar:vertical");
				if (bar.getValue() > 0.0 && bar.getValue() <= 1.0) {
					bar.setValue(bar.getValue() - 0.1);
				}
			}
		}
	}
	
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
	  
	  currentTableShown();
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
		dragPane.requestFocus();
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
		} else if (input.matches("edit \\d+ \\w.+|editt \\d+ \\w.+|edite \\d+ \\w.+")) {
			commandEditSelectionStyle(input);
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
			eventTableIncomplete.getSelectionModel().clearSelection();
			taskTableIncomplete.getSelectionModel().clearSelection();
			eventTableComplete.getSelectionModel().clearSelection();
			taskTableComplete.getSelectionModel().clearSelection();
			validPane.setStyle("-fx-background-color: green;");
		}
  }

	private void commandSetNotification(String input) {
	  if (input.matches("\\w|\\w.+|/x")) {
			validPane.setStyle("-fx-background-color: red;");
			switch(input) {
				case "a":
					notification.setText("add <Name> due by <End> OR add <Name>; <Start> to <End>");
					tab.getSelectionModel().select(incompleteTab);
					eventPaneIncomplete.setExpanded(true);
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
	  int rowID;
	  eventTableIncomplete.getSelectionModel().setCellSelectionEnabled(false);
	  taskTableIncomplete.getSelectionModel().setCellSelectionEnabled(false);
	  
	  inputArray = input.split(" ");
	  rowID = Integer.parseInt(inputArray[1]) - 1;
	  
	  switch(inputArray[0]) {
	  	case "del":
	  		validPane.setStyle("-fx-background-color: green;");
	  		if (tableNo == EVENT_INC) {
	  			eventPaneIncomplete.setExpanded(true);
	  			eventTableIncomplete.scrollTo(rowID);
	  			eventTableIncomplete.getSelectionModel().select(rowID);
	  		} else if (tableNo == TASK_INC) {
	  			taskPaneIncomplete.setExpanded(true);
	  			taskTableIncomplete.scrollTo(rowID);
	  			taskTableIncomplete.getSelectionModel().select(rowID);
	  		} else if (tableNo == EVENT_COM) {
	  			eventPaneComplete.setExpanded(true);
	  			eventTableComplete.scrollTo(rowID);
	  			eventTableComplete.getSelectionModel().select(rowID);
	  		} else if (tableNo == TASK_COM) {
	  			taskPaneComplete.setExpanded(true);
	  			taskTableComplete.scrollTo(rowID);
	  			taskTableComplete.getSelectionModel().select(rowID);
	  		}
	  		break;
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
	  	case "mark":
	  		validPane.setStyle("-fx-background-color: green;");
	  		tab.getSelectionModel().select(incompleteTab);
	  		currentTableShown();
	  		if (tableNo == EVENT_INC) {
	  			eventTableIncomplete.scrollTo(rowID);
	  			eventTableIncomplete.getSelectionModel().select(rowID);
	  		} else if (tableNo == TASK_INC) {
	  			taskTableIncomplete.scrollTo(rowID);
	  			taskTableIncomplete.getSelectionModel().select(rowID);
	  		}
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
	  	case "unmark":
	  		validPane.setStyle("-fx-background-color: green;");
	  		tab.getSelectionModel().select(completeTab);
	  		currentTableShown();
	  		if (tableNo == EVENT_COM) {
	  			eventTableComplete.scrollTo(rowID);
	  			eventTableComplete.getSelectionModel().select(rowID);
	  		} else if (tableNo == TASK_COM) {
	  			taskTableComplete.scrollTo(rowID);
	  			taskTableComplete.getSelectionModel().select(rowID);
	  		}
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
	  	case "edit":
	  		tab.getSelectionModel().select(incompleteTab);
	  		currentTableShown();
	  		if (tableNo == EVENT_INC) {
	  			eventPaneIncomplete.setExpanded(true);
	  			eventTableIncomplete.scrollTo(rowID);
	  			eventTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
	  		} else if (tableNo == TASK_INC) {
	  			taskPaneIncomplete.setExpanded(true);
	  			taskTableIncomplete.scrollTo(rowID);
	  			taskTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
	  			taskTableIncomplete.getSelectionModel().select(rowID, colTaskIDIncomplete);
	  		}
	  		break;
	  	case "edite":
	  		tab.getSelectionModel().select(incompleteTab);
	  		eventPaneIncomplete.setExpanded(true);
	  		eventTableIncomplete.scrollTo(rowID);
	  		eventTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
	  		eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
	  		break;
	  	case "editt":
	  		tab.getSelectionModel().select(incompleteTab);
	  		taskPaneIncomplete.setExpanded(true);
	  		taskTableIncomplete.scrollTo(rowID);
	  		taskTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
	  		taskTableIncomplete.getSelectionModel().select(rowID, colTaskIDIncomplete);
	  		break;
	  	default:
	  		validPane.setStyle("-fx-background-color: red;");
	  		break;
	  }
  }
	
	private void commandEditSelectionStyle(String input) {
	  String[] inputArray;
	  int rowID;
	  inputArray = input.split(" ");
	  rowID = Integer.parseInt(inputArray[1]) - 1;
	  
	  if (tableNo == EVENT_INC) {
	  	eventTableIncomplete.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	  	eventTableIncomplete.getSelectionModel().clearAndSelect(rowID, colEventIDIncomplete);
	  	
	  	switch (inputArray[2]) {
	  		case "Name":
	  		case "name":
	  		case "Name:":
	  		case "name:":
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventNameIncomplete);
	  			break;
	  		case "Priority":
	  		case "priority":
	  		case "Priority:":
	  		case "priority:":
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventPriorityIncomplete);
	  			break;
	  		case "Start":
	  		case "start":
	  		case "Start:":
	  		case "start:":
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventStartDateIncomplete);
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventStartTimeIncomplete);
	  			break;
	  		case "End":
	  		case "end":
	  		case "End:":
	  		case "end:":
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventEndDateIncomplete);
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventEndTimeIncomplete);
	  			break;
	  		default:
	  			break;
	  	}
	  } else if (tableNo == TASK_INC) {
	  	taskTableIncomplete.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	  	taskTableIncomplete.getSelectionModel().clearAndSelect(rowID, colTaskIDIncomplete);
	  	
	  	switch (inputArray[2]) {
	  		case "Name":
	  		case "name":
	  		case "Name:":
	  		case "name:":
	  			taskTableIncomplete.getSelectionModel().select(rowID, colTaskNameIncomplete);
	  			break;
	  		case "Priority":
	  		case "priority":
	  		case "Priority:":
	  		case "priority:":
	  			taskTableIncomplete.getSelectionModel().select(rowID, colTaskPriorityIncomplete);
	  			break;
	  		case "End":
	  		case "end":
	  		case "End:":
	  		case "end:":
	  			taskTableIncomplete.getSelectionModel().select(rowID, colTaskEndDateIncomplete);
	  			taskTableIncomplete.getSelectionModel().select(rowID, colTaskEndTimeIncomplete);
	  			break;
	  		default:
	  			break;
	  	}
	  }
  }
	
	private void commandHistoryStorage(String lastInput) {
	  if(!(this.lastInput == null)) {
	  	history.add(this.lastInput);
	  	this.lastInput = null;
	  }
	  
	  if(!forward.empty()) {
	  	while(!forward.empty()) {
	  		history.add(forward.pop());
	  	}
	  }
	  
	  if(!lastInput.equals("")) {
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
		} else if (command.getText().matches("edit \\d+ \\w.+|editt \\d+ \\w.+|edite \\d+ \\w.+")) {
			commandEditSelectionStyle(command.getText());
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
	
	public String getTestingResponse() {
		return testingResponse;
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
}
