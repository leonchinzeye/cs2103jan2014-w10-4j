package application;

import java.util.Stack;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TaskController {
	@FXML
	private TabPane tab;
	
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
	private TableColumn<EventDataUI, String> colEventFrequencyIncomplete;
	
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
	private TableColumn<EventDataUI, String> colEventFrequencyComplete;
	
	@FXML
	private TextField notification;
	@FXML
	private TextField command;
	
	private UI ui = new UI();
	private Stack<String> prevInputs = new Stack<String>();
	private Stack<String> nextInputs = new Stack<String>();
	private CommandHandler commandHandle;
	private FileLinker fileLink;
	private DataUI dataUI;
	
	private final ObservableList<EventDataUI> incompleteEvents = FXCollections.observableArrayList();
	private final ObservableList<TaskDataUI> incompleteTasks = FXCollections.observableArrayList();
	private final ObservableList<EventDataUI> completedEvents = FXCollections.observableArrayList();
	private final ObservableList<TaskDataUI> completedTasks = FXCollections.observableArrayList();
	
	public TaskController() {
		commandHandle = new CommandHandler();
		dataUI = new DataUI();
	}
	
	@FXML
	private void initialize() {
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
		colEventFrequencyIncomplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("frequency"));
		
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
		colEventFrequencyComplete.setCellValueFactory(new PropertyValueFactory<EventDataUI, String>("frequency"));
	}
	
	public void setUI(UI ui) {
		this.ui = ui;
		dataUI = commandHandle.getDataUI();
		
		incompleteAccordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			@Override public void changed(ObservableValue<? extends TitledPane> property, final TitledPane oldPane, final TitledPane newPane) {
				if (oldPane != null) oldPane.setCollapsible(true);
				if (newPane != null) Platform.runLater(new Runnable() { @Override public void run() {
					newPane.setCollapsible(false);
				}});
			}
		});
		
		completeAccordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			@Override public void changed(ObservableValue<? extends TitledPane> property, final TitledPane oldPane, final TitledPane newPane) {
				if (oldPane != null) oldPane.setCollapsible(true);
				if (newPane != null) Platform.runLater(new Runnable() { @Override public void run() {
					newPane.setCollapsible(false);
				}});
			}
		});
		
		incompleteEvents.addAll(dataUI.getIncompleteEvents());
		incompleteTasks.addAll(dataUI.getIncompleteTasks());
		eventTableIncomplete.setItems(incompleteEvents);
		taskTableIncomplete.setItems(incompleteTasks);
		
		completedEvents.addAll(dataUI.getCompleteEvents());
		completedTasks.addAll(dataUI.getCompleteTasks());
		eventTableComplete.setItems(completedEvents);
		taskTableComplete.setItems(completedTasks);
	}
	
	@FXML
	public void parseInput() {
		String response = "";
		String lastInput = "";
		lastInput = command.getText();
		prevInputs.add(lastInput);
		
		dataUI = commandHandle.executeCmd(lastInput);
		if (dataUI.equals(null)) {
			response = "Read me!";
		}
		response = dataUI.getFeedback();
		notification.setText(response);
		command.clear(); //clears the input text field
		incompleteEvents.removeAll(incompleteEvents);
		incompleteTasks.removeAll(incompleteTasks);
		completedEvents.removeAll(completedEvents);
		completedTasks.removeAll(completedTasks);
		setUI(ui);
	}
	
	@FXML
	public void anchorKeystrokes(KeyEvent key) {
		focusTextInput(key);
		changePanel(key);
	}
	
	@FXML
	public void focusTextInput(KeyEvent key) {
		if (key.getCode().equals(KeyCode.SLASH)) {
			command.requestFocus();
			command.end();
		}
	}
	
	@FXML
	public void changePanel(KeyEvent key) {
		if (key.isShiftDown() && key.getCode().equals(KeyCode.UP)) {
			if (eventPaneIncomplete.isExpanded()) {
				taskPaneIncomplete.setExpanded(true);
				taskPaneComplete.setExpanded(true);
			}
		} else if (key.isShiftDown() && key.getCode().equals(KeyCode.DOWN)) {
			if (taskPaneIncomplete.isExpanded()) {
				eventPaneIncomplete.setExpanded(true);
				eventPaneComplete.setExpanded(true);
			}
		}
	}
	
	@FXML
	public void changeTab(KeyEvent key) {
		if (key.isShiftDown() && (key.getCode().equals(KeyCode.RIGHT)) || (key.getCode().equals(KeyCode.LEFT))) {
			if (tab.getSelectionModel().isSelected(0)) {
				tab.getSelectionModel().selectNext();
			} else {
				tab.getSelectionModel().selectPrevious();
			}
		}
	}
	
	@FXML
	public void returnLastInput(KeyEvent key) { //BROKEN
		String lastInput = "";
		String nextInput = "";
		
		if(key.getCode().equals(KeyCode.UP)) {
			if (command.getText().equals(lastInput)) {
				nextInputs.add(command.getText());
			}
			if (!prevInputs.isEmpty()) {
				lastInput = prevInputs.pop();
				command.appendText(lastInput);
				command.end();
			}
		} else if(key.getCode().equals(KeyCode.DOWN)) {	
				if (nextInputs.isEmpty()) {
					command.clear();
				} else {
					nextInput = nextInputs.pop();
					command.appendText(nextInput);
					command.end();
					prevInputs.add(nextInput);
				}
			}
	}
}
