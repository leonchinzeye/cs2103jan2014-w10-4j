package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;

public class UI extends Application {
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TaskWorthy");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		try {
			FXMLLoader loader = new FXMLLoader(UI.class.getResource("TaskWorthyUI.fxml"));
			
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root,640,430);
			
			//CSS file
			scene.getStylesheets().add("application/emerald.css");
			
			TaskController tc = loader.getController();
			tc.setUI(this);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage sendPrimary() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
