package application;

import com.sun.javafx.application.LauncherImpl;
import application.staff.StaffManagementService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;

public class MainClass extends Application {
	static boolean isRegistered;
	@Override
	public void init() throws Exception {
		StaffManagementService regService = new StaffManagementService();
		isRegistered = regService.isRegisterd();
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		if(!isRegistered) {
			runStageFXML("FXML/RegisterPage.fxml");
		}else if(isRegistered) {
			runStageFXML("FXML/LoginPage.fxml");
		}
	}
	public void runStageFXML(String fxml) throws Exception{
		Stage newStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		newStage.setScene(scene);
		newStage.setResizable(false);
		newStage.sizeToScene();			// to solve the extra margins caused by setResizable(false) method
		newStage.setTitle("LMS GECR");
		newStage.getIcons().add(new Image("/application/images/icon.png"));
		newStage.show();
		newStage.setOnCloseRequest(w->{
			Platform.exit();
			System.exit(0);
		});
	}
	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainClass.class, PreLoaderClass.class, args);
	}
	public void stop(Stage primaryStage) throws Exception {
		Platform.exit();
	}

}
