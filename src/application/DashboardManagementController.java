package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardManagementController implements Initializable{

	
	@FXML
	public Pane dashboardTabPage;

	@FXML
	public TableView<BookTransaction> mostFamousBooksTable;
	@FXML
	public TableColumn<BookTransaction, Integer> mostFamousBooksTableRank;
	@FXML
	public TableColumn<BookTransaction, String> mostFamousBooksTableName;
	
	@FXML
	public TableView<BookTransaction> todaysTransactionsTable;
	@FXML
	public TableColumn<BookTransaction, Integer> todaysTransactionsTableId;
	@FXML
	public TableColumn<BookTransaction, String> todaysTransactionsTableName;
	@FXML
	public TableColumn<BookTransaction, String> todaysTransactionsTableBookCode;
	@FXML
	public TableColumn<BookTransaction, String> todaysTransactionsTableStatus;
	
	@FXML
	public Label totalBooksAdded;
	@FXML
	public Label totalBooksAvailable;
	@FXML
	public Label totalBooksIssuedToday;
	@FXML
	public Label totalBooksReturnedToday;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	
	public void issueBook(ActionEvent event) throws IOException {
		Stage issueStage = new Stage();
		issueStage.initOwner(((Node)(event.getSource())).getScene().getWindow());
		issueStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("IssueBookTabPage.fxml"));							
		Pane root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		issueStage.setScene(scene);
		issueStage.setResizable(false);
		issueStage.setTitle("LMS GECR");
		issueStage.showAndWait();
	}
	
	
	
	
}
