package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


public class StageController extends Application {
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		StaffManagementService regService = new StaffManagementService();
		boolean isRegistered = regService.isRegisterd();
		
		if(!isRegistered) {
			runStageFXML("RegisterPage.fxml");
		}else if(isRegistered) {
			runStageFXML("LoginPage.fxml");
		}	
	}
	
	
	public void runStageFXML(String fxml) throws Exception{
		Stage newStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxml));							// setLocation method rectifies Base location is undefined while incliding one fxml file in another
		Pane root = loader.load();				//open stream and homecontroller implements initialisable were added so that we could controller ui from another class
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		newStage.setScene(scene);
		newStage.setResizable(false);
		newStage.setTitle("LMS GECR");
		newStage.show();  			// should be executed after setOnShow()
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * use below way to control the controller of one fxml from controller of another fxml

				   HomeController home = (HomeController)loader.getController();		// note that loader should that the fxml that you want to control
				   home.initialiseHomePage();
	 * 
	 */
	
	
	
	
}
