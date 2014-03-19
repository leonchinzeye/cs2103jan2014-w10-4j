package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class UI extends Application {
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TaskWorthy");
		
		try {
			FXMLLoader loader = new FXMLLoader(UI.class.getResource("TaskWorthyUI.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root,640,600);
			
			TaskController tc = loader.getController();
			tc.setUI(this);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
