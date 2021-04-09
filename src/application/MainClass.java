package application;
	
import application.staff.StaffManagementService;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class MainClass extends Application {
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		StaffManagementService regService = new StaffManagementService();
		boolean isRegistered = regService.isRegisterd();
		
		if(!isRegistered) {
			runStageFXML("FXML/RegisterPage.fxml");
		}else if(isRegistered) {
			runStageFXML("FXML/LoginPage.fxml");
		}	
	}
	
	
	public void runStageFXML(String fxml) throws Exception{
		Stage newStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxml));							// setLocation method rectifies Base location is undefined while including one fxml file in another
		Pane root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		newStage.setScene(scene);
		newStage.setResizable(false);
		newStage.sizeToScene();			// to solve the extra margins caused by setResizable(false) method
		newStage.setTitle("LMS GECR");
		newStage.getIcons().add(new Image("/application/images/icon.png"));
		newStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	
	
//	//for transparent stage
//	newStage.initStyle(StageStyle.TRANSPARENT);
//	root.setStyle(  "-fx-background-color: rgba(255, 255, 255, 0.1);"   );
//	scene.setFill(Color.TRANSPARENT);
	

	
	
	
	
	/**
	 * use below way to control the controller of one fxml from controller of another fxml

				   HomeController home = (HomeController)loader.getController();		// note that loader should that the fxml that you want to control
				   home.initialiseHomePage();
	 * 
	 */
	
	
	
	
}
