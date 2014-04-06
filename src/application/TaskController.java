package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

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

/**
 * This class is the controller for the main UI. It sets the rules of how the user can interact with the program
 * and is a gateway for the user to manipulate the data shown on screen.
 */
//@author A0094534B
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
	private Text dayAndTime;
	@FXML
	private Text dateText;
	
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
	private TitledPane eventsHelpPane;
	
	@FXML
	private TableView<TaskDataUI> taskTableIncomplete;
	@FXML
	private TableView<EventDataUI> eventTableIncomplete;
	@FXML
	private TableView<TaskDataUI> taskTableComplete;
	@FXML
	private TableView<EventDataUI> eventTableComplete;
	@FXML
	private TableView<EventDataUI> helpTable;
	
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
	
	@FXML
	private TableColumn<EventDataUI, String> colHelpID;
	@FXML
	private TableColumn<EventDataUI, String> colHelpPriority;
	@FXML
	private TableColumn<EventDataUI, String> colHelpName;
	@FXML
	private TableColumn<EventDataUI, String> colHelpStartDate;
	@FXML
	private TableColumn<EventDataUI, String> colHelpStartTime;
	@FXML
	private TableColumn<EventDataUI, String> colHelpEndDate;
	@FXML
	private TableColumn<EventDataUI, String> colHelpEndTime;
	
	private final int TASK_INC = 1;
	private final int EVENT_INC = 2;
	private final int TASK_COM = 3;
	private final int EVENT_COM = 4;
	
	private String testingResponse;
	private UI ui = new UI();
	private Stack<String> history = new Stack<String>();
	private Stack<String> forward = new Stack<String>();
	private CommandHandler commandHandle;
	private static DataUI dataUI;
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
	private ObservableList<EventDataUI> helpEvents = FXCollections.observableArrayList();
	
	private ArrayList<String> themes = new ArrayList<String>();
	private String jedigreen = getClass().getResource("jedigreen.css").toExternalForm();
	private String sithred = getClass().getResource("sithred.css").toExternalForm();
	private String australia = getClass().getResource("australia.css").toExternalForm();
	private String italy = getClass().getResource("italy.css").toExternalForm();	
	private static final int JEDI_GREEN = 0;
	private static final int SITH_RED = 1;
	private static final int AUSTRALIA = 2;
	private static final int ITALY = 3;
	private static int themeIndex = 0;
	
	private static Timer timer;
	
	//@author A0094534B
	public TaskController() {
		commandHandle = new CommandHandler();
		notification = new TextField();
		command = new TextField();
		anchor = new AnchorPane();
		eventTableIncomplete = new TableView<EventDataUI>();
		taskTableIncomplete = new TableView<TaskDataUI>();
		eventTableComplete = new TableView<EventDataUI>();
		taskTableComplete = new TableView<TaskDataUI>();
		helpTable = new TableView<EventDataUI>();
		tab = new TabPane();
		validPane = new Pane();
		helpAnchor = new AnchorPane();
		testingResponse = "";
		themes.add(jedigreen);
		themes.add(sithred);
		themes.add(australia);
		themes.add(italy);
	}
	
	/**
	 * This method sets up all the columns with all the information it will display,
	 * starts the timer to update the clock as well as to refresh the UI to update it
	 * when a task or event is detected to be expired or ongoing.
	 */
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
		
		colHelpID.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("ID"));
		colHelpPriority.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("priority"));
		colHelpName.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("name"));
		colHelpStartDate.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("startDate"));
		colHelpStartTime.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("startTime"));
		colHelpEndDate.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("endDate"));
		colHelpEndTime.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("endTime"));
		
		dataUI = commandHandle.getDataUI();
		
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
		helpEvents.addAll(dataUI.getHelpEvents());
		eventsHelpPane.setText("Events [2]");
		helpTable.setItems(helpEvents);
		
		eventPaneIncomplete.setText(String.format("Events [%d]", incompleteEvents.size()));
		taskPaneIncomplete.setText(String.format("Tasks [%d]", incompleteTasks.size()));
	
		validPane.setStyle("-fx-background-color: green;");
		updateClock();
		highlightExpiredAndOngoingRows();
		
		Calendar tester = Calendar.getInstance();
		tester.add(Calendar.MINUTE, 1);
		tester.set(Calendar.SECOND, 0);
		tester.set(Calendar.MILLISECOND, 0);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						commandHandle.executeCmd("highlightexpiredandongoingrows", tableNo);
						updateClock();
						setUI(ui);
					}
				});
			}
		}, tester.getTime(), 60*1000);		
		
		incompleteEvents.removeAll(incompleteEvents);
		incompleteTasks.removeAll(incompleteTasks);
		completedEvents.removeAll(completedEvents);
		completedTasks.removeAll(completedTasks);
		
		currentTableShown();
	}

	private void updateClock() {
	  dayAndTime.setText(dataUI.getUIClock());
		dateText.setText(dataUI.getUIdate());
  }
	
	/**
	 * Uses a Callback to look through all the rows and sets its RowFactory to change the CSS
	 * in the case where it is detected to be expired or ongoing.
	 */
	private void highlightExpiredAndOngoingRows() {
	  final ObservableList<Integer> highlightOngoingEvents = FXCollections.observableArrayList();
		final ObservableList<Integer> highlightExpiredEvents = FXCollections.observableArrayList();
		final ObservableList<Integer> highlightExpiredTasks = FXCollections.observableArrayList();
		final ObservableList<Integer> highlightHelpExpired = FXCollections.observableArrayList();
		final ObservableList<Integer> highlightHelpOngoing = FXCollections.observableArrayList();
		
		for (int i = 0; i < helpEvents.size(); i++) {
			if (helpEvents.get(i).getIsOngoing()) {
				highlightHelpOngoing.add(i);
			} else if (helpEvents.get(i).getIsExpired()) {
				highlightHelpExpired.add(i);
			}
		}
		
		for (int i = 0; i < incompleteEvents.size(); i++) {
			if (incompleteEvents.get(i).getIsOngoing()) {
				highlightOngoingEvents.add(i);
			} else if (incompleteEvents.get(i).getIsExpired()) {
				highlightExpiredEvents.add(i);
			}
		}
		
		for (int i = 0; i < incompleteTasks.size(); i++) {
			if (incompleteTasks.get(i).getIsExpired()) {
				highlightExpiredTasks.add(i);
			}
		}
		
		eventTableIncomplete.setRowFactory(new Callback<TableView<EventDataUI>, TableRow<EventDataUI>>() {
			@Override
			public TableRow<EventDataUI> call(TableView<EventDataUI> tableView) {
				final TableRow<EventDataUI> row = new TableRow<EventDataUI>() {
					@Override
					protected void updateItem(EventDataUI event, boolean empty){
						super.updateItem(event, empty);
						if (highlightOngoingEvents.contains(getIndex())) {
							if (!getStyleClass().contains("highlightOngoing")) {
								getStyleClass().add("highlightOngoing");
							}
						} else if (highlightExpiredEvents.contains(getIndex())){
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
		
		taskTableIncomplete.setRowFactory(new Callback<TableView<TaskDataUI>, TableRow<TaskDataUI>>() {
			@Override
			public TableRow<TaskDataUI> call(TableView<TaskDataUI> tableView) {
				final TableRow<TaskDataUI> row = new TableRow<TaskDataUI>() {
					@Override
					protected void updateItem(TaskDataUI task, boolean empty){
						super.updateItem(task, empty);
						if (highlightExpiredTasks.contains(getIndex())){
							if (!getStyleClass().contains("highlightExpired")) {
								getStyleClass().add("highlightExpired");
							}
						} else {
							getStyleClass().removeAll(Collections.singleton("highlightExpired"));
						}
					}
				};
				return row;
			}
		});
		
		helpTable.setRowFactory(new Callback<TableView<EventDataUI>, TableRow<EventDataUI>>() {
			@Override
			public TableRow<EventDataUI> call(TableView<EventDataUI> tableView) {
				final TableRow<EventDataUI> row = new TableRow<EventDataUI>() {
					@Override
					protected void updateItem(EventDataUI event, boolean empty){
						super.updateItem(event, empty);
						if (highlightHelpOngoing.contains(getIndex())) {
							if (!getStyleClass().contains("highlightOngoing")) {
								getStyleClass().add("highlightOngoing");
							}
						} else if (highlightHelpExpired.contains(getIndex())){
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
  }
	
	/**
	 * The main method to take in input and send it to CommandHandler to execute. It also stores the
	 * input in a command history list. 
	 */
	@FXML
	public void parseInput() {
		String lastInput = "";
		
		taskTableIncomplete.getSelectionModel().select(0);
		lastInput = command.getText();
		commandHistoryStorage(lastInput);
		executeCmd(lastInput);
	}

	/**
	 * Sets the notification text field to return the response from execution of user input
	 * followed by refreshing the UI.
	 */
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
		highlightNewAdd(lastInput);
		updateCounter();
  }

	/**
	 * In the situation where a user adds a new task or event, this will expand its panel, if not done so already,
	 * and highlight the row in its table.
	 * @param lastInput
	 */
	private void highlightNewAdd(String lastInput) {
	  if (lastInput.contains("add")) {
			if (dataUI.getFileAdded() == TASK_INC) {
				taskPaneIncomplete.setExpanded(true);
				taskTableIncomplete.getSelectionModel().select(dataUI.getRowAdded());
			} else if (dataUI.getFileAdded() == EVENT_INC) {
				eventPaneIncomplete.setExpanded(true);
				eventTableIncomplete.getSelectionModel().select(dataUI.getRowAdded());
			}
		}
  }

	/**
	 * The method used to change themes based on user preference.
	 */
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
	
	/**
	 * This is mainly used to retain themes when switching from and to the mini UI.
	 */
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
	 * The instructions for the "view" command, used to display the "events" or "tasks" panels
	 * as well as to switch between tabs. It is recommended for the user to use the shortcuts however
	 * as it is much faster, and produces less bugs.
	 * @param lastInput
	 */
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
	 */
	private void updateCounter() {
	  if (tab.getSelectionModel().isSelected(0)) {
			eventPaneIncomplete.setText(String.format("Events [%d]", incompleteEvents.size()));
			taskPaneIncomplete.setText(String.format("Tasks [%d]", incompleteTasks.size()));
		} else if (tab.getSelectionModel().isSelected(1)) {
			eventPaneComplete.setText(String.format("Events [%d]", completedEvents.size()));
			taskPaneComplete.setText(String.format("Tasks [%d]", completedTasks.size()));
		}
  }
	
	/**
	 * Retrieves the information from DataUI to be shown in the tables.
	 * The highlightExpiredAndOngoingRows(), updateCounter(), updateClock(), and changeThemeByInt() methods
	 * are called since this method (setUI()) is one of the first methods to be executed
	 * when the program starts. Calling the aforementioned methods ensures that the information that is initially shown
	 * is up to date.
	 * @param ui
	 */
	public void setUI(UI ui) {
		this.ui = ui;
		
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
		
		highlightExpiredAndOngoingRows();
		updateCounter();
		updateClock();
		changeThemeByInt(themeIndex);
	}
	
	/**
	 * The default keyboard shortcuts that allow the user to switch between panels and tabs
	 * as well as to change the focus to the text field without using the mouse.
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
	 * Sets the view to default, i.e. Incomplete Events
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
	
	/**
	 * If the focus is currently not on the text bar, the user can hit the Enter key
	 * to start typing. This is similar to Ctrl+L in most web browsers.
	 */
	public void focusTextInput(KeyEvent key) {
		if (key.getCode().equals(KeyCode.ENTER)) {
			command.requestFocus();
			command.end();
		}
	}
	
	/**
	 * Scrolls the currently shown table up or down.
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
	 */
	@FXML
	public void changeTab(KeyEvent key) {
		if (key.isControlDown() && key.getCode().equals(KeyCode.TAB)) {						//1
			switchTabs();
		} else if (key.isControlDown() && key.getCode().equals(KeyCode.A)) {
			switchPanes();
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

	/**
	 * Switch between the current panel that is open (Events or Tasks) when Ctrl+A is input.
	 */
	private void switchPanes() {
	  if (incompleteTab.isSelected()) {
	  	if (eventPaneIncomplete.isExpanded()) {
	  		taskPaneIncomplete.setExpanded(true);
	  	} else {
	  		eventPaneIncomplete.setExpanded(true);
	  	}
	  } else if (completeTab.isSelected()) {
	  	if (eventPaneComplete.isExpanded()) {
	  		taskPaneComplete.setExpanded(true);
	  	} else {
	  		eventPaneComplete.setExpanded(true);
	  	}
	  }
  }

	/**
	 * Switch between the tabs (Incomplete or Completed) when Ctrl+Tab is input.
	 */
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
	
	/**
	 * Basic rules for the UI to follow when the Incomplete tab is selected.
	 */
	@FXML
	public void openIncompleteTab() {
		notification.setText("Read me!");
	  command.setPromptText("Feed me!");
	  updateCounter();
		notification.setDisable(false);
		command.setDisable(false);
		helpAnchor.setVisible(false);
	}
	
	/**
	 * Basic rules for the UI to follow when the Completed tab is selected.
	 */
	@FXML
	public void openCompleteTab() {
		notification.setText("Read me!");
	  command.setPromptText("Feed me!");
	  updateCounter();
		notification.setDisable(false);
		command.setDisable(false);
		helpAnchor.setVisible(false);
	}
	
	/**
	 * Basic rules for the UI to follow when the Help tab is opened.
	 */
	@FXML
	public void openHelpTab() {
		notification.setText("This will return responses to you based on your commands.");
		command.setPromptText("This is where you enter your commands.");
		command.setMouseTransparent(true);
		command.setFocusTraversable(false);
		helpAnchor.setVisible(true);
		dragPane.requestFocus();
	}
	
	/**
	 * The command text field, which is the main form of user input, will act differently when specific keys are pressed.
	 * These include "Undo", "Redo", and recalling previous commands.
	 */
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
		testingResponse = response;
		notification.setText(response);
		
		setUI(ui);
		updateCounter();
  }
	
	private void redoShortcut() {
		String response = "";
		
		dataUI = commandHandle.executeCmd("redo", tableNo);
		response = dataUI.getFeedback();
		testingResponse = response;
		notification.setText(response);
		
		setUI(ui);
		updateCounter();
  }

	/**
	 * This is the beginning to setting the highlighting style of a row or column, based on the user's command.
	 * It also informs the user whether a command is valid or not based on the validPane color.
	 */
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
	
	/**
	 * If the command text field is blank, and the user hits Enter, it is akin to refreshing the screen.
	 */
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

	/**
	 * This sets the notification text whenever a specific command is entered, to aid the user
	 * in typing in the command with the proper syntax in order to avoid errors.
	 */
	private void commandSetNotification(String input) {
	  if (input.matches("\\w|\\w.+|/x")) {
			validPane.setStyle("-fx-background-color: red;");
			switch(input) {
				case "a":
					notification.setText("add <Name> due by <End> OR add <Name>; <Start> to <End>");
					tab.getSelectionModel().select(incompleteTab);
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
	
	/**
	 * Sets the row highlighting style for the "del" and "mark" and "unmark" commands.
	 * Sets the column highlighting style for the "edit" command.
	 */
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
	
	/**
	 * Sets the column highlighting style for edit based on the field the user wishes to change.
	 */
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
	
	/**
	 * Stores the user's input in a list in order to be recalled later.
	 */
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

	/**
	 * The method used to recall commands from the history list.
	 */
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
	
	/**
	 * This is used to tell the logic which table is currently being shown in the UI.
	 * This is particularly important when executing commands that are not list specific.
	 * For instance, using "mark" instead of "markt" (for tasks) or "marke" (for events).
	 */
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
	
	/**
	 * Used for integration testing.
	 * @return
	 */
	public String getTestingResponse() {
		return testingResponse;
	}
	
	/**
	 * The following six methods sets the rules for the minimize and close buttons of the UI.
	 * An alternative to exiting the program is entering "/x".
	 * @param mouse
	 */
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
	
	public DataUI getDataUI() {
		return dataUI;
	}
	
	public void setDataUI(DataUI dataUI) {
		TaskController.dataUI = dataUI;
		setUI(ui);
	}
	
	public int getTheme() {
		return themeIndex;
	}
	
	public void setTheme(int theme) {
		TaskController.themeIndex = theme;
	}
}
