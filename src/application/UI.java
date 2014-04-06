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
	private boolean sceneMin = false;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TaskWorthy");
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		try {
			FXMLLoader loader = new FXMLLoader(UI.class.getResource("TaskWorthyUI.fxml"));
			FXMLLoader loaderMin = new FXMLLoader(UI.class.getResource("TaskWorthyMin.fxml"));
			
			Parent root = (Parent) loader.load();
			Parent rootMin = (Parent) loaderMin.load();
			Scene scene = new Scene(root,640,480);
			Scene scene2 = new Scene(rootMin, 640, 150);
			final Scene scene1Sub = scene;
			final Scene scene2Sub = scene2;
			
			primaryStage.getIcons().add(new Image(UI.class.getResourceAsStream("/projectX.png")));
			//CSS file
			scene.getStylesheets().add("application/sithred.css");
			scene.getStylesheets().add("application/australia.css");
			scene.getStylesheets().add("application/italy.css");
			scene.getStylesheets().add("application/jedigreen.css");
			
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
