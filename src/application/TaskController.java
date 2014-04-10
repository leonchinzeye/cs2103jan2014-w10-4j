package application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
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
	private static final String COMMAND_DEFAULT_PROMPT = "Feed me!";
	private static final String COMMAND_HELP_PG_1 = "This is where you enter your commands.";	
	private static final String COMMAND_HELP_PG_2 = "Use the UP and DOWN arrow keys to scroll through your command history.";
	private static final String NOTIFICATION_DEFAULT_PROMPT = "Read me!";
	private static final String NOTIFICATION_HELP_PG_1 = "This will return responses to you based on your commands.";
	private static final String NOTIFICATION_HELP_PG_2 = "Listed above are the accepted commands and their proper formats.";
	private static final String NOTIFICATION_VIEW_COM_TASKS = "view Incomplete/Events";
	private static final String NOTIFICATION_VIEW_COM_EVENTS = "view Incomplete/Tasks";
	private static final String NOTIFICATION_VIEW_INC_TASKS = "view Completed/Events";
	private static final String NOTIFICATION_VIEW_INC_EVENTS = "view Completed/Tasks";
	private static final String NOTIFICATION_SEARCH = "search <Query OR Priority OR Date>";
	private static final String NOTIFICATION_UNMARK = "unmark/t/e <ID>";
	private static final String NOTIFICATION_MARK = "mark/t/e <ID>";
	private static final String NOTIFICATION_EDIT = "edit/t/e <ID> <Field>: <Edited entry>";
	private static final String NOTIFICATION_DEL = "del/t/e/tc/ec <ID>";
	private static final String NOTIFICATION_ADD = "add <Name> due by <End> OR add <Name>; <Start> to <End>";
	private static final String CMD_EDITT = "editt";
	private static final String CMD_EDITE = "edite";
	private static final String CMD_EDIT = "edit";
	private static final String CMD_UNMARKT = "unmarkt";
	private static final String CMD_UNMARKE = "unmarke";
	private static final String CMD_UNMARK = "unmark";
	private static final String CMD_MARKT = "markt";
	private static final String CMD_MARKE = "marke";
	private static final String CMD_MARK = "mark";
	private static final String CMD_DELTC = "deltc";
	private static final String CMD_DELEC = "delec";
	private static final String CMD_DELT = "delt";
	private static final String CMD_DELE = "dele";
	private static final String CMD_DEL = "del";
	private static final String CMD_REDO = "redo";
	private static final String CMD_UNDO = "undo";
	private static final String CMD_VIEW_COMPLETED_TASKS = "view completed tasks";
	private static final String CMD_VIEW_COMPLETED_EVENTS = "view completed events";
	private static final String CMD_VIEW_INCOMPLETE_TASKS = "view incomplete tasks";
	private static final String CMD_VIEW_INCOMPLETE_EVENTS = "view incomplete events";
	private static final String CMD_VIEW_TASKS = "view tasks";
	private static final String CMD_VIEW_EVENTS = "view events";
	private static final String CMD_VIEW_COMPLETED = "view completed";
	private static final String CMD_VIEW_INCOMPLETE = "view incomplete";
	private static final String CMD_HELP = "help";
	private static final String CMD_THEME = "theme";
	private static final String CMD_ADD = "add";
	private static final String COL_START = "start";
	private static final String COL_END = "end";
	private static final String COL_START_TIME = "startTime";
	private static final String COL_START_DATE = "startDate";
	private static final String COL_END_TIME = "endTime";
	private static final String COL_END_DATE = "endDate";
	private static final String COL_NAME = "name";
	private static final String COL_PRIORITY = "priority";
	private static final String COL_ID = "ID";
	private static final String REGEX_WITH_EXIT = "\\w|\\w.+|/x";
	private static final String REGEX_GENERAL = "\\w.+ \\d+";
	private static final String REGEX_EDIT = "edit \\d+ \\w.+|editt \\d+ \\w.+|edite \\d+ \\w.+";
	private static final String SCROLL_BAR_VERTICAL = ".scroll-bar:vertical";	
	private static final String THEME_MISSING_ARGUMENT_MESSAGE = "You forgot to enter a theme to change to!";
	private static final String THEME_ITALY = "Italy";
	private static final String THEME_AUSTRALIA = "Australia";
	private static final String THEME_SITH = "Sith";
	private static final String THEME_JEDI = "Jedi";
	private static final String HIGHLIGHT_EXPIRED_STYLE = "highlightExpired";
	private static final String HIGHLIGHT_ONGOING_STYLE = "highlightOngoing";
	private static final String HIGHLIGHT_EXPIRED_AND_ONGOING_ROWS_CMD = "highlightexpiredandongoingrows";
	private static final String TASKS_COUNTER = "Tasks [%d]";
	private static final String EVENTS_COUNTER = "Events [%d]";
	private static final String HELP_EVENTS_COUNTER = "Events [2]";
	private static final String END_COLON = "end:";
	private static final String END_CAPITAL_COLON = "End:";
	private static final String START_COLON = "start:";
	private static final String START_CAPITAL_COLON = "Start:";
	private static final String PRIORITY_COLON = "priority:";
	private static final String PRIORITY_CAPITAL_COLON = "Priority:";
	private static final String NAME_COLON = "name:";
	private static final String NAME_CAPITAL_COLON = "Name:";
	private static final String END_CAPITAL = "End";
	private static final String START_CAPITAL = "Start";
	private static final String PRIORITY_CAPITAL = "Priority";
	private static final String NAME_CAPITAL = "Name";
	private static final String CSS_ITALY = "italy.css";
	private static final String CSS_AUSTRALIA = "australia.css";
	private static final String CSS_SITHRED = "sithred.css";
	private static final String CSS_JEDIGREEN = "jedigreen.css";
	private static final String MINIMIZE_BUTTON_HOVER = "/minimizeButtonHover.png";
	private static final String MINIMIZE_BUTTON = "/minimizeButton.png";
	private static final String CLOSE_BUTTON_HOVER = "/closeButtonHover.png";
	private static final String CLOSE_BUTTON = "/closeButton.png";
	private static final double SCROLLBAR_DECREMENT = 0.1;
	private static final double SCROLLBAR_INCREMENT = 0.1;
	private static final double SCROLLBAR_MAX = 1.0;
	private static final double SCROLLBAR_MIN = 0.0;
	private static final int MILLISECONDS = 0;
	private static final int SECONDS = 0;
	private static final int MINUTES = 1;
	private static final int TASK_INC = 1;
	private static final int EVENT_INC = 2;
	private static final int TASK_COM = 3;
	private static final int EVENT_COM = 4;
	private static final int INCOMPLETE_TAB = 0;
	private static final int COMPLETE_TAB = 1;
	private static final int JEDI_GREEN = 0;
	private static final int SITH_RED = 1;
	private static final int AUSTRALIA = 2;
	private static final int ITALY = 3;
	
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
	
	private String testingResponse;
	private UI ui = new UI();
	private Stack<String> history = new Stack<String>();
	private Stack<String> forward = new Stack<String>();
	private CommandHandler commandHandle;
	private static DataUI dataUI;
	private String lastInput = null;
	private int tableNo = 0;
	private boolean isEnterKey = false;
	private static int themeIndex = 0;
	private static Timer timer;
	private static double mouseDragOffsetX;
	private static double mouseDragOffsetY;
	Image closeButtonDefault = new Image(CLOSE_BUTTON);
	Image closeButtonHover = new Image(CLOSE_BUTTON_HOVER);
	Image minimizeButtonDefault = new Image(MINIMIZE_BUTTON);
	Image minimizeButtonHover = new Image(MINIMIZE_BUTTON_HOVER);
	
	private ObservableList<EventDataUI> incompleteEvents = FXCollections.observableArrayList();
	private ObservableList<TaskDataUI> incompleteTasks = FXCollections.observableArrayList();
	private ObservableList<EventDataUI> completedEvents = FXCollections.observableArrayList();
	private ObservableList<TaskDataUI> completedTasks = FXCollections.observableArrayList();
	private ObservableList<EventDataUI> helpEvents = FXCollections.observableArrayList();
	
	private ArrayList<String> themes = new ArrayList<String>();
	private String jedigreen = getClass().getResource(CSS_JEDIGREEN).toExternalForm();
	private String sithred = getClass().getResource(CSS_SITHRED).toExternalForm();
	private String australia = getClass().getResource(CSS_AUSTRALIA).toExternalForm();
	private String italy = getClass().getResource(CSS_ITALY).toExternalForm();
	
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
		helpAnchor = new AnchorPane();
		testingResponse = ""; //for integration testing
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
			@SuppressWarnings("static-access")
      @Override
			public void handle(MouseEvent event) {
				ui.primaryS.setX(event.getScreenX() - mouseDragOffsetX);
				ui.primaryS.setY(event.getScreenY() - mouseDragOffsetY);
			}
		});
		
		colTaskIDIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_ID));
		colTaskPriorityIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_PRIORITY));
		colTaskNameIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_NAME));
		colTaskEndDateIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_END_DATE));
		colTaskEndTimeIncomplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_END_TIME));
		
		colEventIDIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_ID));
		colEventPriorityIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_PRIORITY));
		colEventNameIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_NAME));
		colEventStartDateIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_START_DATE));
		colEventStartTimeIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_START_TIME));
		colEventEndDateIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_END_DATE));
		colEventEndTimeIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_END_TIME));
		
		colTaskIDComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_ID));
		colTaskPriorityComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_PRIORITY));
		colTaskNameComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_NAME));
		colTaskEndDateComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_END_DATE));
		colTaskEndTimeComplete.setCellValueFactory(new PropertyValueFactory<TaskDataUI, String>(COL_END_TIME));
		
		colEventIDComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_ID));
		colEventPriorityComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_PRIORITY));
		colEventNameComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_NAME));
		colEventStartDateComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_START_DATE));
		colEventStartTimeComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_START_TIME));
		colEventEndDateComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_END_DATE));
		colEventEndTimeComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_END_TIME));
		
		colHelpID.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_ID));
		colHelpPriority.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_PRIORITY));
		colHelpName.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_NAME));
		colHelpStartDate.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_START_DATE));
		colHelpStartTime.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_START_TIME));
		colHelpEndDate.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_END_DATE));
		colHelpEndTime.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>(COL_END_TIME));
		
		dataUI = commandHandle.getDataUI();
		
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
		helpEvents.addAll(dataUI.getHelpEvents());
		eventsHelpPane.setText(HELP_EVENTS_COUNTER);
		helpTable.setItems(helpEvents);
		
		eventPaneIncomplete.setText(String.format(EVENTS_COUNTER, incompleteEvents.size()));
		taskPaneIncomplete.setText(String.format(TASKS_COUNTER, incompleteTasks.size()));

		updateClock();
		highlightExpiredAndOngoingRows();	
		startTimer();
		
		incompleteEvents.removeAll(incompleteEvents);
		incompleteTasks.removeAll(incompleteTasks);
		completedEvents.removeAll(completedEvents);
		completedTasks.removeAll(completedTasks);
		
		currentTableShown();
	}

	private void updateClock() {
	  dayAndTime.setText(dataUI.getUIClock());

	  
	  /*del after record video
	  Calendar now = new GregorianCalendar();
	  now.set(Calendar.DAY_OF_MONTH, 6);
		now.set(Calendar.MONTH, 4);
	  dataUI.setUIdate(now);
	  */
	  
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
							if (!getStyleClass().contains(HIGHLIGHT_ONGOING_STYLE)) {
								getStyleClass().add(HIGHLIGHT_ONGOING_STYLE);
							}
						} else if (highlightExpiredEvents.contains(getIndex())){
							if (!getStyleClass().contains(HIGHLIGHT_EXPIRED_STYLE)) {
								getStyleClass().add(HIGHLIGHT_EXPIRED_STYLE);
							}
						} else {
							getStyleClass().removeAll(Collections.singleton(HIGHLIGHT_ONGOING_STYLE));
							getStyleClass().removeAll(Collections.singleton(HIGHLIGHT_EXPIRED_STYLE));
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
							if (!getStyleClass().contains(HIGHLIGHT_EXPIRED_STYLE)) {
								getStyleClass().add(HIGHLIGHT_EXPIRED_STYLE);
							}
						} else {
							getStyleClass().removeAll(Collections.singleton(HIGHLIGHT_EXPIRED_STYLE));
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
							if (!getStyleClass().contains(HIGHLIGHT_ONGOING_STYLE)) {
								getStyleClass().add(HIGHLIGHT_ONGOING_STYLE);
							}
						} else if (highlightHelpExpired.contains(getIndex())){
							if (!getStyleClass().contains(HIGHLIGHT_EXPIRED_STYLE)) {
								getStyleClass().add(HIGHLIGHT_EXPIRED_STYLE);
							}
						} else {
							getStyleClass().removeAll(Collections.singleton(HIGHLIGHT_ONGOING_STYLE));
							getStyleClass().removeAll(Collections.singleton(HIGHLIGHT_EXPIRED_STYLE));
						}
					}
				};
				return row;
			}
		});		
  }
	
	private void startTimer() {
	  Calendar tester = Calendar.getInstance();
		tester.add(Calendar.MINUTE, MINUTES);
		tester.set(Calendar.SECOND, SECONDS);
		tester.set(Calendar.MILLISECOND, MILLISECONDS);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						commandHandle.executeCmd(HIGHLIGHT_EXPIRED_AND_ONGOING_ROWS_CMD, tableNo);
						updateClock();
						setUI(ui);
					}
				});
			}
		}, tester.getTime(), 60*1000);
  }
	
	/**
	 * The main method to take in input and send it to CommandHandler to execute. It also stores the
	 * input in a command history list. 
	 */
	@FXML
	public void parseInput() {
		String lastInput = "";
		
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
	  if (lastInput.contains(CMD_ADD)) {
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
		if (lastInput.contains(CMD_THEME)) {
			inputArray = lastInput.split(" ", 2);
			
			if (!inputArray[1].isEmpty()) {
				String chosenTheme = inputArray[1];
				anchor.getStylesheets().removeAll(themes);
				if (chosenTheme.equalsIgnoreCase(THEME_JEDI)) {
					themeIndex = JEDI_GREEN;
				} else if (chosenTheme.equalsIgnoreCase(THEME_SITH)) {
					themeIndex = SITH_RED;
				} else if (chosenTheme.equalsIgnoreCase(THEME_AUSTRALIA)) {
					themeIndex = AUSTRALIA;
				} else if (chosenTheme.equalsIgnoreCase(THEME_ITALY)) {
					themeIndex = ITALY;
				}
				anchor.getStylesheets().add(themes.get(themeIndex));
			} else {
				notification.setText(THEME_MISSING_ARGUMENT_MESSAGE);
			}
		}
  }
	
	/**
	 * This is mainly used to retain themes when switching from and to the mini UI.
	 */
	private void changeThemeByInt(int index) {
		anchor.getStylesheets().removeAll(themes);
		anchor.getStylesheets().add(themes.get(index));
	}

	/**
	 * The instructions for the "view" command, used to display the "events" or "tasks" panels
	 * as well as to switch between tabs. It is recommended for the user to use the shortcuts however
	 * as it is much faster, and produces less bugs.
	 * @param lastInput
	 */
	private void viewCmd(String lastInput) {
	  if (lastInput.equals(CMD_HELP)) {
			tab.getSelectionModel().select(helpTab);
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_INCOMPLETE)) {
			tab.getSelectionModel().select(incompleteTab);
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_COMPLETED)) {
			tab.getSelectionModel().select(completeTab);
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_EVENTS)) {
			if (incompleteTab.isSelected()) {
				eventPaneIncomplete.setExpanded(true);
			} else if (completeTab.isSelected()) {
				eventPaneComplete.setExpanded(true);
			}
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_TASKS)) {
			if (incompleteTab.isSelected()) {
				taskPaneIncomplete.setExpanded(true);
			} else if (completeTab.isSelected()) {
				taskPaneComplete.setExpanded(true);
			}
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_INCOMPLETE_EVENTS)) {
			tab.getSelectionModel().select(incompleteTab);
			eventPaneIncomplete.setExpanded(true);
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_INCOMPLETE_TASKS)) {
			tab.getSelectionModel().select(incompleteTab);
			taskPaneIncomplete.setExpanded(true);
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_COMPLETED_EVENTS)) {
			tab.getSelectionModel().select(completeTab);
			eventPaneComplete.setExpanded(true);
		} else if (lastInput.equalsIgnoreCase(CMD_VIEW_COMPLETED_TASKS)) {
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
			eventPaneIncomplete.setText(String.format(EVENTS_COUNTER, incompleteEvents.size()));
			taskPaneIncomplete.setText(String.format(TASKS_COUNTER, incompleteTasks.size()));
		} else if (tab.getSelectionModel().isSelected(1)) {
			eventPaneComplete.setText(String.format(EVENTS_COUNTER, completedEvents.size()));
			taskPaneComplete.setText(String.format(TASKS_COUNTER, completedTasks.size()));
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
	  notification.setText(NOTIFICATION_DEFAULT_PROMPT);
	  command.clear();
	  command.setPromptText(COMMAND_DEFAULT_PROMPT);
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
				ScrollBar bar = (ScrollBar) eventTableIncomplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() >= SCROLLBAR_MIN && bar.getValue() < SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() + SCROLLBAR_INCREMENT);
				}
			} else if (tableNo == TASK_INC) {
				ScrollBar bar = (ScrollBar) taskTableIncomplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() >= SCROLLBAR_MIN && bar.getValue() < SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() + SCROLLBAR_INCREMENT);
				}
			} else if (tableNo == EVENT_COM) {
				ScrollBar bar = (ScrollBar) eventTableComplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() >= SCROLLBAR_MIN && bar.getValue() < SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() + SCROLLBAR_INCREMENT);
				}
			} else if (tableNo == TASK_COM) {
				ScrollBar bar = (ScrollBar) taskTableComplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() >= SCROLLBAR_MIN && bar.getValue() < SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() + SCROLLBAR_INCREMENT);
				}
			}
		} else if (key.isControlDown() && key.getCode().equals(KeyCode.COMMA)) {
			if (tableNo == EVENT_INC) {
				ScrollBar bar = (ScrollBar) eventTableIncomplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() > SCROLLBAR_MIN && bar.getValue() <= SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() - SCROLLBAR_DECREMENT);
				}
			} else if (tableNo == TASK_INC) {
				ScrollBar bar = (ScrollBar) taskTableIncomplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() > SCROLLBAR_MIN && bar.getValue() <= SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() - SCROLLBAR_DECREMENT);
				}
			} else if (tableNo == EVENT_COM) {
				ScrollBar bar = (ScrollBar) eventTableComplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() > SCROLLBAR_MIN && bar.getValue() <= SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() - SCROLLBAR_DECREMENT);
				}
			} else if (tableNo == TASK_COM) {
				ScrollBar bar = (ScrollBar) taskTableComplete.lookup(SCROLL_BAR_VERTICAL);
				if (bar.getValue() > SCROLLBAR_MIN && bar.getValue() <= SCROLLBAR_MAX) {
					bar.setValue(bar.getValue() - SCROLLBAR_DECREMENT);
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
			notification.setText(NOTIFICATION_HELP_PG_2);
			command.setPromptText(COMMAND_HELP_PG_2);
		} else if (helpTab.isSelected() && key.getCode().equals(KeyCode.LEFT)) {	//3
			helpAnchor.setVisible(true);
			helpAnchor2.setVisible(false);
			notification.setText(NOTIFICATION_HELP_PG_1);
			command.setPromptText(COMMAND_HELP_PG_1);
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
	  if (!tab.getSelectionModel().isSelected(INCOMPLETE_TAB)) {
	  	tab.getSelectionModel().select(incompleteTab);
	  	helpAnchor.setVisible(false);
	  	helpAnchor2.setVisible(false);
	  } else if (!tab.getSelectionModel().isSelected(COMPLETE_TAB)){
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
		notification.setText(NOTIFICATION_DEFAULT_PROMPT);
	  command.setPromptText(COMMAND_DEFAULT_PROMPT);
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
		notification.setText(NOTIFICATION_DEFAULT_PROMPT);
	  command.setPromptText(COMMAND_DEFAULT_PROMPT);
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
		notification.setText(NOTIFICATION_HELP_PG_1);
		command.setPromptText(COMMAND_HELP_PG_1);
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
		
		dataUI = commandHandle.executeCmd(CMD_UNDO, tableNo);
		response = dataUI.getFeedback();
		testingResponse = response;
		notification.setText(response);
		
		setUI(ui);
		updateCounter();
  }
	
	private void redoShortcut() {
		String response = "";
		
		dataUI = commandHandle.executeCmd(CMD_REDO, tableNo);
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
		
		if (input.matches(REGEX_GENERAL) && !input.contains(CMD_ADD)) {
			commandGeneralSelectionStyle(input);
		} else if (input.matches(REGEX_EDIT)) {
			commandEditSelectionStyle(input);
		}
		
		currentTableShown();
	}
	
	/**
	 * If the command text field is blank, and the user hits Enter, it is akin to refreshing the screen.
	 */
	private void commandBlank(String input) {
	  if (input.matches("")) {
	  	notification.setText(NOTIFICATION_DEFAULT_PROMPT);
			eventTableIncomplete.getSelectionModel().clearSelection();
			taskTableIncomplete.getSelectionModel().clearSelection();
			eventTableComplete.getSelectionModel().clearSelection();
			taskTableComplete.getSelectionModel().clearSelection();
		}
  }

	/**
	 * This sets the notification text whenever a specific command is entered, to aid the user
	 * in typing in the command with the proper syntax in order to avoid errors.
	 */
	private void commandSetNotification(String input) {
	  if (input.matches(REGEX_WITH_EXIT)) {
			switch(input) {
				case "a":
					notification.setText(NOTIFICATION_ADD);
					tab.getSelectionModel().select(incompleteTab);
					currentTableShown();
					break;
				case "d":
					notification.setText(NOTIFICATION_DEL);
					break;
				case "e":
					notification.setText(NOTIFICATION_EDIT);
					break;
				case "m":
					notification.setText(NOTIFICATION_MARK);
					break;
				case "unm":
					notification.setText(NOTIFICATION_UNMARK);
					break;
				case "s":
					notification.setText(NOTIFICATION_SEARCH);
					break;
				case "v":
					if (tableNo == EVENT_INC) {
						notification.setText(NOTIFICATION_VIEW_INC_EVENTS);
					} else if (tableNo == TASK_INC) {
						notification.setText(NOTIFICATION_VIEW_INC_TASKS);
					} else if (tableNo == EVENT_COM) {
						notification.setText(NOTIFICATION_VIEW_COM_EVENTS);
					} else if (tableNo == TASK_COM) {
						notification.setText(NOTIFICATION_VIEW_COM_TASKS);
					}
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
	  	case CMD_DEL:
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
	  	case CMD_DELE:
	  		tab.getSelectionModel().select(incompleteTab);
	  		eventPaneIncomplete.setExpanded(true);
	  		eventTableIncomplete.scrollTo(rowID);
	  		eventTableIncomplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_DELT:
	  		tab.getSelectionModel().select(incompleteTab);
	  		taskPaneIncomplete.setExpanded(true);
	  		taskTableIncomplete.scrollTo(rowID);
	  		taskTableIncomplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_DELEC:
	  		tab.getSelectionModel().select(completeTab);
	  		eventPaneComplete.setExpanded(true);
	  		eventTableComplete.scrollTo(rowID);
	  		eventTableComplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_DELTC:
	  		tab.getSelectionModel().select(completeTab);
	  		taskPaneComplete.setExpanded(true);
	  		taskTableComplete.scrollTo(rowID);
	  		taskTableComplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_MARK:
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
	  	case CMD_MARKE:
	  		tab.getSelectionModel().select(incompleteTab);
	  		eventPaneIncomplete.setExpanded(true);
	  		eventTableIncomplete.scrollTo(rowID);
	  		eventTableIncomplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_MARKT:
	  		tab.getSelectionModel().select(incompleteTab);
	  		taskPaneIncomplete.setExpanded(true);
	  		taskTableIncomplete.scrollTo(rowID);
	  		taskTableIncomplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_UNMARK:
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
	  	case CMD_UNMARKE:
	  		tab.getSelectionModel().select(completeTab);
	  		eventPaneComplete.setExpanded(true);
	  		eventTableComplete.scrollTo(rowID);
	  		eventTableComplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_UNMARKT:
	  		tab.getSelectionModel().select(completeTab);
	  		taskPaneComplete.setExpanded(true);
	  		taskTableComplete.scrollTo(rowID);
	  		taskTableComplete.getSelectionModel().select(rowID);
	  		break;
	  	case CMD_EDIT:
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
	  	case CMD_EDITE:
	  		tab.getSelectionModel().select(incompleteTab);
	  		eventPaneIncomplete.setExpanded(true);
	  		eventTableIncomplete.scrollTo(rowID);
	  		eventTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
	  		eventTableIncomplete.getSelectionModel().select(rowID, colEventIDIncomplete);
	  		break;
	  	case CMD_EDITT:
	  		tab.getSelectionModel().select(incompleteTab);
	  		taskPaneIncomplete.setExpanded(true);
	  		taskTableIncomplete.scrollTo(rowID);
	  		taskTableIncomplete.getSelectionModel().setCellSelectionEnabled(true);
	  		taskTableIncomplete.getSelectionModel().select(rowID, colTaskIDIncomplete);
	  		break;
	  	default:
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
	  		case NAME_CAPITAL:
	  		case COL_NAME:
	  		case NAME_CAPITAL_COLON:
	  		case NAME_COLON:
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventNameIncomplete);
	  			break;
	  		case PRIORITY_CAPITAL:
	  		case COL_PRIORITY:
	  		case PRIORITY_CAPITAL_COLON:
	  		case PRIORITY_COLON:
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventPriorityIncomplete);
	  			break;
	  		case START_CAPITAL:
	  		case COL_START:
	  		case START_CAPITAL_COLON:
	  		case START_COLON:
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventStartDateIncomplete);
	  			eventTableIncomplete.getSelectionModel().select(rowID, colEventStartTimeIncomplete);
	  			break;
	  		case END_CAPITAL:
	  		case COL_END:
	  		case END_CAPITAL_COLON:
	  		case END_COLON:
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
	  		case NAME_CAPITAL:
	  		case COL_NAME:
	  		case NAME_CAPITAL_COLON:
	  		case NAME_COLON:
	  			taskTableIncomplete.getSelectionModel().select(rowID, colTaskNameIncomplete);
	  			break;
	  		case PRIORITY_CAPITAL:
	  		case COL_PRIORITY:
	  		case PRIORITY_CAPITAL_COLON:
	  		case PRIORITY_COLON:
	  			taskTableIncomplete.getSelectionModel().select(rowID, colTaskPriorityIncomplete);
	  			break;
	  		case END_CAPITAL:
	  		case COL_END:
	  		case END_CAPITAL_COLON:
	  		case END_COLON:
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

		if (command.getText().matches(REGEX_GENERAL) && !command.getText().contains(CMD_ADD)) {
			commandGeneralSelectionStyle(command.getText());
		} else if (command.getText().matches(REGEX_EDIT)) {
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
	
	@SuppressWarnings("static-access")
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
