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

/**
 * This class is a shorter version of the main controller as this controller is for the minimized UI.
 * It does not contain as much information and it restricts the user from entering most commands.
 * The only accepted ones are "add", "addu", "undo", "redo", "theme", and "/x".
 * Most, if not all, of the methods below are direct duplicates of the methods in TaskController.java.
 */
//@author A0094534B
public class TaskControllerMin {
	private static final String MINIMIZE_BUTTON_HOVER = "/minimizeButtonHover.png";
	private static final String MINIMIZE_BUTTON = "/minimizeButton.png";
	private static final String CLOSE_BUTTON_HOVER = "/closeButtonHover.png";
	private static final String CLOSE_BUTTON = "/closeButton.png";
	private static final String NOTIFICATION_ADD = "add <Name> due by <End> OR add <Name>; <Start> to <End>";
	private static final String REGEX_WITH_EXIT = "\\w|\\w.+|/x";
	private static final String NOTIFICATION_DEFAULT_PROMPT = "Read me!";
	private static final String THEME_MISSING_ARGUMENT_MESSAGE = "You forgot to enter a theme to change to!";
	private static final String THEME_ITALY = "Italy";
	private static final String THEME_AUSTRALIA = "Australia";
	private static final String THEME_SITH = "Sith";
	private static final String THEME_JEDI = "Jedi";
	private static final String CMD_THEME = "theme";
	private static final String CMD_UNDO = "undo";
	private static final String CMD_REDO = "redo";
	private static final String CMD_EXIT = "/x";
	private static final String CMD_ADD = "add";
	private static final String CSS_ITALY = "italy.css";
	private static final String CSS_AUSTRALIA = "australia.css";
	private static final String CSS_SITHRED = "sithred.css";
	private static final String CSS_JEDIGREEN = "jedigreen.css";
	private static final String MINI_UI_INVALID_CMD_MESSAGE = "That can't be done here! Expand the window with Ctrl+Enter.";
	private static final int JEDI_GREEN = 0;
	private static final int SITH_RED = 1;
	private static final int AUSTRALIA = 2;
	private static final int ITALY = 3;
	private static int themeIndex = 0;
	
	@FXML
	private AnchorPane anchor;
	@FXML
	private Pane dragPane;
	@FXML
	private ImageView closeButton;
	@FXML
	private ImageView minimizeButton;
	
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
	Image closeButtonDefault = new Image(CLOSE_BUTTON);
	Image closeButtonHover = new Image(CLOSE_BUTTON_HOVER);
	Image minimizeButtonDefault = new Image(MINIMIZE_BUTTON);
	Image minimizeButtonHover = new Image(MINIMIZE_BUTTON_HOVER);
	
	private ObservableList<EventDataUI> incompleteEvents = FXCollections.observableArrayList();
	private ObservableList<TaskDataUI> incompleteTasks = FXCollections.observableArrayList();
	
	private String jedigreen = getClass().getResource(CSS_JEDIGREEN).toExternalForm();
	private String sithred = getClass().getResource(CSS_SITHRED).toExternalForm();
	private String australia = getClass().getResource(CSS_AUSTRALIA).toExternalForm();
	private String italy = getClass().getResource(CSS_ITALY).toExternalForm();
	private ArrayList<String> themes = new ArrayList<String>();
	
	public TaskControllerMin() {
		commandHandle = new CommandHandler();
		notification = new TextField();
		command = new TextField();
		anchor = new AnchorPane();
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
			@SuppressWarnings("static-access")
      @Override
			public void handle(MouseEvent event) {
				ui.primaryS.setX(event.getScreenX() - mouseDragOffsetX);
				ui.primaryS.setY(event.getScreenY() - mouseDragOffsetY);
			}
		});
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
	  if (lastInput.contains(CMD_ADD) || lastInput.equals(CMD_EXIT) || 
	  		lastInput.contains(CMD_UNDO) || lastInput.contains(CMD_REDO) || lastInput.contains(CMD_THEME)) {
	  	dataUI = commandHandle.executeCmd(lastInput, tableNo);
	  	response = dataUI.getFeedback();
	  	notification.setText(response);
	  } else {
	  	notification.setText(MINI_UI_INVALID_CMD_MESSAGE);
	  }
		command.clear();
		command.requestFocus();

		setUI(ui);
  }

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
	
	private void changeThemeByInt(int index) {
		anchor.getStylesheets().removeAll(themes);
		anchor.getStylesheets().add(themes.get(index));
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
		
		dataUI = commandHandle.executeCmd(CMD_UNDO, tableNo);
		response = dataUI.getFeedback();
		notification.setText(response);
		
		setUI(ui);
  }
	
	private void redoShortcut() {
		String response = "";
		
		dataUI = commandHandle.executeCmd(CMD_REDO, tableNo);
		response = dataUI.getFeedback();
		notification.setText(response);
		
		setUI(ui);
  }
	
	public void setTableColumnStyle(KeyEvent key) {
		String input = command.getText();
		
		commandBlank(input);
		commandSetNotification(input);
	}
	
	private void commandBlank(String input) {
	  if (input.matches("")) {
	  	notification.setText(NOTIFICATION_DEFAULT_PROMPT);
		}
  }

	private void commandSetNotification(String input) {
	  if (input.matches(REGEX_WITH_EXIT)) {
			switch(input) {
				case "a":
					notification.setText(NOTIFICATION_ADD);
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
	
	@SuppressWarnings("static-access")
  @FXML
	public void minimizeWindow (MouseEvent mouse) {
		ui.primaryS.setIconified(true);
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
