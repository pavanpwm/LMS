package application;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PreLoaderClass extends Preloader {

    private Stage preloaderStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
    	Label title = new Label("Loading LMS! Please Wait..");
        title.setTextAlignment(TextAlignment.CENTER);
		VBox root = new VBox(title);
		root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 400, 100);
        preloaderStage = primaryStage;
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.setScene(scene);
        preloaderStage.show();
        
    }




    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        StateChangeNotification.Type type = info.getType();
        if (type == Type.BEFORE_START) {
        	preloaderStage.hide();
		}
    }
}
