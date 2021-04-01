package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



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
	
	
	public static Stage issueOrReturnStage;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		mostFamousBooksTableRank.setCellValueFactory(new PropertyValueFactory<BookTransaction, Integer>("id"));
		mostFamousBooksTableName.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("title"));
    	
    	
		todaysTransactionsTableId.setCellValueFactory(new PropertyValueFactory<BookTransaction, Integer>("id"));
		todaysTransactionsTableName.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("name"));
		todaysTransactionsTableBookCode.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("code"));
		todaysTransactionsTableStatus.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("status"));
		refreshTab();

		
		
		//a  jugaad way after hours of shitting
		dashboardTabPage.setOnMouseEntered(event->{
			if (SettingsController.tabNumber == 1) {
				SettingsController.tabNumber = 0;
				refreshTab();
			}
		});
		
				
	}
	
	
	public void refreshTab(){
		// get all transactions in a list and pass it to below methods
		List<BookTransaction> list = new BookTransactionManagementService() .getFullBookTransactionList();
		populateRankTable(list);
		populateTodaysTransacntionTable(list);
		drawChartAndSetCounts(list);
	}
	
	
	
	
	
	
	public void populateRankTable(List<BookTransaction> list){
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setId(0);
			for (int j = 0; j < list.size(); j++) {
				if (list.get(i).getCode().equalsIgnoreCase(list.get(j).getCode())) {
					list.get(i).setId(list.get(i).getId() + 1);
				}
			}
		}
		
		Comparator<BookTransaction> myList = Comparator.comparing(BookTransaction :: getId);
		Collections.sort(list, myList);
		Collections.reverse(list);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setId(i+1);
		}
		mostFamousBooksTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(list));
		
	}

	
	public void populateTodaysTransacntionTable(List<BookTransaction> list){
		todaysTransactionsTable.getItems()
		.setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( LocalDate.now().toString() , "search" )));
		todaysTransactionsTableId.setSortType(SortType.DESCENDING);
		todaysTransactionsTable.getSortOrder().setAll(todaysTransactionsTableId);
	}
	
	
	
	public void drawChartAndSetCounts(List<BookTransaction> list) {
		totalBooksAdded.setText("Total Books Added : " + new BookManagementService().getBooksAddedCount());
		totalBooksAvailable.setText("Books Available : " + new BookManagementService().getBooksAvailableCount());
		totalBooksIssuedToday.setText("Books Issued Today : " + getBooksIssuedTodayCount());
		totalBooksReturnedToday.setText("Books Returned Today : " + getBooksReturnedTodayCount());
		
		
		
		
		//  code  for chart
	}
	
	
	
	

	
	
	
	public void issueBook(ActionEvent event) throws IOException {
		
		issueOrReturnStage = new Stage();
		issueOrReturnStage.initOwner(((Node)(event.getSource())).getScene().getWindow());
		issueOrReturnStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("IssueBookTabPage.fxml"));							
		Pane root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		issueOrReturnStage.setScene(scene);
		issueOrReturnStage.setResizable(false);
		issueOrReturnStage.setTitle("LMS GECR");
		issueOrReturnStage.getIcons().add(new Image("/icon.png"));
		issueOrReturnStage.showAndWait();

	}
	
	
	public void returnBook(ActionEvent event) throws IOException {
		
		issueOrReturnStage = new Stage();
		issueOrReturnStage.initOwner(((Node)(event.getSource())).getScene().getWindow());
		issueOrReturnStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ReturnBookTabPage.fxml"));							
		Pane root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		issueOrReturnStage.setScene(scene);
		issueOrReturnStage.setResizable(false);
		issueOrReturnStage.setTitle("LMS GECR");
		issueOrReturnStage.getIcons().add(new Image("/icon.png"));
		issueOrReturnStage.showAndWait();		
		

	}
	
	
	public int getBooksIssuedTodayCount() {
		ObservableList<BookTransaction> list =  todaysTransactionsTable.getItems();
		int count =0;
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getStatus().equalsIgnoreCase("Issued")) {
				count++;
            }
		}
		return count;
	}
	
	public int getBooksReturnedTodayCount() {
		ObservableList<BookTransaction> list =  todaysTransactionsTable.getItems();
		int count =0;
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getStatus().equalsIgnoreCase("Returned")) {
				count++;
            }
		}
		return count;
	}
	
	
}
