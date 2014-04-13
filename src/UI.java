//@author A0094534B
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

public class UI extends Application {
	private static final String JEDIGREEN_CSS = "jedigreen.css";
	private static final String ITALY_CSS = "italy.css";
	private static final String AUSTRALIA_CSS = "australia.css";
	private static final String SITHRED_CSS = "sithred.css";
	private static final String TASKWORTHY_LOGO = "projectX.png";
	private static final String TASKWORTHY_MIN_FXML = "TaskWorthyMin.fxml";
	private static final String TASKWORTHY_UI_FXML = "TaskWorthyUI.fxml";
	private static final String TASK_WORTHY = "TaskWorthy";
	private Stage primaryStage;
	public static Stage primaryS;
	private Stage primaryStageSub;
	private boolean sceneMin = false;
	 
	@Override
	/**
	 * This method sets up the scene to be shown and adds the stylesheets to be used.
	 * An event handler is implemented to detect when the Ctrl+Enter is pressed 
	 * during which the scene will change to scene2, which is the minimized UI.
	 */
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(TASK_WORTHY);
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		try {
			FXMLLoader loader = new FXMLLoader(UI.class.getResource(TASKWORTHY_UI_FXML));
			FXMLLoader loaderMin = new FXMLLoader(UI.class.getResource(TASKWORTHY_MIN_FXML));
			
			Parent root = (Parent) loader.load();
			Parent rootMin = (Parent) loaderMin.load();
			Scene scene = new Scene(root,640,480);
			Scene scene2 = new Scene(rootMin, 640, 150);
			final Scene scene1Sub = scene;
			final Scene scene2Sub = scene2;
			
			primaryStage.getIcons().add(new Image(UI.class.getResourceAsStream(TASKWORTHY_LOGO)));
			//CSS file
			scene.getStylesheets().add(SITHRED_CSS);
			scene.getStylesheets().add(AUSTRALIA_CSS);
			scene.getStylesheets().add(ITALY_CSS);
			scene.getStylesheets().add(JEDIGREEN_CSS);
			
			final TaskController tc = loader.getController();
			final TaskControllerMin tcMin = loaderMin.getController();
			
			primaryStage.setScene(scene);
			primaryStageSub = primaryStage;
			tcMin.setUI(this);
			tc.setUI(this);
			
			primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					final KeyCombination minimize = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
					if (sceneMin == false && minimize.match(t)) {
						primaryStageSub.setScene(scene2Sub);
						sceneMin = true;
						tcMin.setTheme(tc.getTheme());
						tcMin.setDataUI(tc.getDataUI());
					} else if (sceneMin == true && minimize.match(t)) {
						primaryStageSub.setScene(scene1Sub);
						sceneMin = false;
						tc.setTheme(tcMin.getTheme());
						tc.setDataUI(tcMin.getDataUI());
					}
				}
			});
			
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		primaryS = primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
