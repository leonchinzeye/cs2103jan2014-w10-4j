package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TaskController {
	@FXML
	private TabPane tab;
	@FXML
	private Tab incompleteTab;
	@FXML
	private Tab completeTab;
	@FXML
	private TableView<TaskCard> taskTableIncomplete;
	@FXML
	private TableView<TaskCard> eventTableIncomplete;
	@FXML
	private TableView<TaskCard> taskTableComplete;
	@FXML
	private TableView<TaskCard> eventTableComplete;
	
	@FXML
	private TableColumn<TaskCard, Integer> colTaskIDIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colTaskNameIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colTaskEndDateIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colTaskEndTimeIncomplete;
	
	@FXML
	private TableColumn<TaskCard, Integer> colEventIDIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colEventNameIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colEventStartDateIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colEventStartTimeIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colEventEndDateIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colEventEndTimeIncomplete;
	@FXML
	private TableColumn<TaskCard, String> colEventFrequencyIncomplete;
	
	@FXML
	private TableColumn<TaskCard, Integer> colTaskIDComplete;
	@FXML
	private TableColumn<TaskCard, String> colTaskNameComplete;
	@FXML
	private TableColumn<TaskCard, String> colTaskEndDateComplete;
	@FXML
	private TableColumn<TaskCard, String> colTaskEndTimeComplete;
	
	@FXML
	private TableColumn<TaskCard, Integer> colEventIDComplete;
	@FXML
	private TableColumn<TaskCard, String> colEventNameComplete;
	@FXML
	private TableColumn<TaskCard, String> colEventStartDateComplete;
	@FXML
	private TableColumn<TaskCard, String> colEventStartTimeComplete;
	@FXML
	private TableColumn<TaskCard, String> colEventEndDateComplete;
	@FXML
	private TableColumn<TaskCard, String> colEventEndTimeComplete;
	@FXML
	private TableColumn<TaskCard, String> colEventFrequencyComplete;
	
	@FXML
	private TextField notification;
	@FXML
	private TextField command;
	
	private UI ui = new UI();
	private CommandHandler commandHandle;
	private FileLinker fileLink;
	private DataUI dui;
	
	private final ObservableList<TaskCard> incompleteEvents = FXCollections.observableArrayList();
	private final ObservableList<TaskCard> incompleteTasks = FXCollections.observableArrayList();
	private final ObservableList<TaskCard> completedEvents = FXCollections.observableArrayList();
	private final ObservableList<TaskCard> completedTasks = FXCollections.observableArrayList();
	
	public TaskController() {
		commandHandle = new CommandHandler();
		dui = new DataUI();
		fileLink = new FileLinker();
	}
	
	@FXML
	private void initialize() {
		colTaskIDIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, Integer>("ID"));
		colTaskNameIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("name"));
		//colTaskEndDateIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("endDate"));
		//colTaskEndTimeIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("endTime"));
		colEventIDIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, Integer>("ID"));
		colEventNameIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("name"));
		//colEventStartDateIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("startDate"));
		//colEventStartTimeIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("startTime"));
		//colEventEndDateIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("endDate"));
		//colEventEndTimeIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("endTime"));
		colEventFrequencyIncomplete.setCellValueFactory(new PropertyValueFactory<TaskCard, String>("frequency"));
	}
	
	public void setUI(UI ui) {
		this.ui = ui;
		incompleteEvents.addAll(fileLink.getIncompleteEvents());
		incompleteTasks.addAll(fileLink.getIncompleteTasks());
		eventTableIncomplete.setItems(incompleteEvents);
		taskTableIncomplete.setItems(incompleteTasks);
	}
	
	@FXML
	public void parseInput() {
		String response = "";
		dui = commandHandle.executeCmd(command.getText());
		if (dui.equals(null)) {
			response = "Read me!";
		}
		response = dui.getFeedback();
		notification.setText(response);
		command.clear(); //clears the input text field
	}
}
