package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;

public class UI extends Application {
	private Stage primaryStage;
	private Stage secondaryStage;
	public static Stage primaryS;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("TaskWorthy");
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		try {
			FXMLLoader loader = new FXMLLoader(UI.class.getResource("TaskWorthyUI.fxml"));
			//FXMLLoader loader2 = new FXMLLoader(UI.class.getResource("TaskWorthyMin.fxml"));
			
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root,640,480);
			
			primaryStage.getIcons().add(new Image(UI.class.getResourceAsStream("/projectX.png")));
			//secondaryStage.getIcons().add(new Image(UI.class.getResourceAsStream("/projectX.png")));
			//CSS file
			scene.getStylesheets().add("application/jedigreen.css");
			//scene.getStylesheets().add("application/sithred.css");
			//scene.getStylesheets().add("application/australia.css");
			//scene.getStylesheets().add("application/italy.css");
			
			TaskController tc = loader.getController();
			tc.setUI(this);
			
			primaryStage.setScene(scene);
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
