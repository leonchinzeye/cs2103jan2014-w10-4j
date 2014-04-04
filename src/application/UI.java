package application;

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
	private Stage primaryStage;
	public static Stage primaryS;
	private Stage primaryStageSub;
	private boolean sceneMin = true;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TaskWorthy");
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		try {
			FXMLLoader loader = new FXMLLoader(UI.class.getResource("TaskWorthyMin.fxml"));
			FXMLLoader loader2 = new FXMLLoader(UI.class.getResource("TaskWorthyUI.fxml"));
			
			Parent root = (Parent) loader.load();
			Parent root2 = (Parent) loader2.load();
			Scene scene = new Scene(root,640,150);
			Scene scene2 = new Scene(root2, 640, 480);
			final Scene scene1Sub = scene;
			final Scene scene2Sub = scene2;
			
			primaryStage.getIcons().add(new Image(UI.class.getResourceAsStream("/projectX.png")));
			//CSS file
			scene.getStylesheets().add("application/sithred.css");
			scene.getStylesheets().add("application/australia.css");
			scene.getStylesheets().add("application/italy.css");
			scene.getStylesheets().add("application/jedigreen.css");
			
			final TaskController tc = loader.getController();
			final TaskController tc2 = loader2.getController();
			
			primaryStage.setScene(scene);
			primaryStageSub = primaryStage;
			primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					final KeyCombination minimize = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
					if (sceneMin == true && minimize.match(t)) {
						primaryStageSub.setScene(scene2Sub);
						primaryStageSub.centerOnScreen();
						sceneMin = false;
						tc2.setIncEvents(tc.getIncEvents());
						tc2.setIncTasks(tc.getIncTasks());
						tc2.setComEvents(tc.getComEvents());
						tc2.setComTasks(tc.getComTasks());
					} else if (sceneMin == false && minimize.match(t)) {
						primaryStageSub.setScene(scene1Sub);
						primaryStageSub.centerOnScreen();
						sceneMin = true;
						tc.setIncEvents(tc2.getIncEvents());
						tc.setIncTasks(tc2.getIncTasks());
						tc.setComEvents(tc2.getComEvents());
						tc.setComTasks(tc2.getComTasks());
					}
				}
			});
			primaryStage.show();
			if (sceneMin == true) {
				tc2.setUI(this);
			} else {
				tc.setUI(this);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		primaryS = primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
