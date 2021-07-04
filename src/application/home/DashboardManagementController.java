package application.home;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import application.book.Book;
import application.book.BookManagementService;
import application.transaction.BookTransaction;
import application.transaction.BookTransactionManagementService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;




public class DashboardManagementController implements Initializable{

	
	@FXML
	public Pane dashboardTabPage;


	@FXML
	public TableView<Book> mostFamousBooksTable;
	@FXML
	public TableColumn<Book, Integer> mostFamousBooksTableRank;
	@FXML
	public TableColumn<Book, String> mostFamousBooksTableName;
	
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
	
	@FXML
	public BarChart<?, ?> barChart;
	@FXML
	public CategoryAxis x;
	@FXML
	public NumberAxis y;
	
	
	public static Stage issueOrReturnStage;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		mostFamousBooksTableRank.setCellValueFactory(new PropertyValueFactory<Book, Integer>("rank"));
		mostFamousBooksTableName.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
    	
    	
		todaysTransactionsTableId.setCellValueFactory(new PropertyValueFactory<BookTransaction, Integer>("id"));
		todaysTransactionsTableName.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("name"));
		todaysTransactionsTableBookCode.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("code"));
		todaysTransactionsTableStatus.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("status"));
		refreshTab();

		
		
		//a  jugaadu way after hours of shitting
		dashboardTabPage.setOnMouseEntered(event->{
			if (!SettingsController.refreshed) {
				SettingsController.refreshed = true;
				refreshTab();
			}
		});
		
				
	}
	
	
	public void refreshTab(){
		populateRankTable();
		populateTodaysTransacntionTable();
		setCounts();
		pupolateBarChart();
	}
	public void populateRankTable(){
		mostFamousBooksTable.getItems().setAll(new BookManagementService().getObservableBookList(new BookManagementService().getRankedBook()));
	}
	public void populateTodaysTransacntionTable(){
		BookTransactionManagementService.searchText = LocalDate.now().toString();
		BookTransactionManagementService.lastPageIndex = 0;
		BookTransactionManagementService.searchType = "loose";
		todaysTransactionsTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getNextScrollBookTransactions()));
	}	
	public void setCounts() {
		totalBooksAdded.setText("Total Books Added : " + new BookManagementService().getBooksAddedCount());
		totalBooksAvailable.setText("Books Available : " + new BookManagementService().getBooksAvailableCount());
		totalBooksIssuedToday.setText("Books Issued Today : " + getBooksIssuedTodayCount());
		totalBooksReturnedToday.setText("Books Returned Today : " + getBooksReturnedTodayCount());
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
	public void pupolateBarChart() {
	    XYChart.Series series = new XYChart.Series<>();
	    for (int i = 1; i < 13; i++) {
	    	String query = "";
	    	if (i<10) {
				query = "-0" + i + "-";
			}else {
				query = "-" + i + "-";
			}
		    series.getData().add(new XYChart.Data(  Month.of(i).toString().substring(0, 3)  , new BookTransactionManagementService().getBookTransactionsCountForBarChart(query)));
		}
	    barChart.getData().setAll(series);
	}
	
	
	
	

	
	
	
	public void issueBook(ActionEvent event) throws IOException {
		
		issueOrReturnStage = new Stage();
		issueOrReturnStage.initOwner(((Node)(event.getSource())).getScene().getWindow());
		issueOrReturnStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/application/FXML/IssueBookTabPage.fxml"));							
		Pane root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		issueOrReturnStage.setScene(scene);
		issueOrReturnStage.setResizable(false);
		issueOrReturnStage.setTitle("LMS GECR");
		issueOrReturnStage.getIcons().add(new Image("/application/images/icon.png"));
		issueOrReturnStage.showAndWait();

	}
	
	
	public void returnBook(ActionEvent event) throws IOException {
		
		issueOrReturnStage = new Stage();
		issueOrReturnStage.initOwner(((Node)(event.getSource())).getScene().getWindow());
		issueOrReturnStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/application/FXML/ReturnBookTabPage.fxml"));							
		Pane root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		issueOrReturnStage.setScene(scene);
		issueOrReturnStage.setResizable(false);
		issueOrReturnStage.setTitle("LMS GECR");
		issueOrReturnStage.getIcons().add(new Image("/application/images/icon.png"));
		issueOrReturnStage.showAndWait();		
		

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
