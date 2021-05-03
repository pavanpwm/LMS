package application.home;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn.SortType;
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
	
	@FXML
	public BarChart<?, ?> barChart;
	@FXML
	public CategoryAxis x;
	@FXML
	public NumberAxis y;
	
	
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

		
		
		//a  jugaadu way after hours of shitting
		dashboardTabPage.setOnMouseEntered(event->{
			if (!SettingsController.refreshed) {
				SettingsController.refreshed = true;
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
		pupolateBarChart(list);
	}
	
	
	
	
	
	
	
	public void pupolateBarChart(List<BookTransaction> list) {
		barChart.getData().clear();
		//Configuring Series for XY chart   
	    XYChart.Series series = new XYChart.Series<>();
	    for (int i = 01; i < 13; i++) {
	    	String query = "";
	    	if (i<10) {
				query = "-0" + i + "-";
			}else {
				query = "-" + i + "-";
			}
	    	List<BookTransaction> tempList =  new BookTransactionManagementService().getSearchedBookTransactionList(Arrays.asList(new String[] {query}), "search");
		    series.getData().add(new XYChart.Data(  Month.of(i).toString().substring(0, 3)  , tempList.size() ));
		}
	    //Adding series to the barchart
	    barChart.getData().addAll(series);
	}
	
	
	
	
	public void populateRankTable(List<BookTransaction> list){
		
		List<BookTransaction> countedList = new ArrayList<BookTransaction>(); 
		List<String> bookTitles = new ArrayList<String>();
		
		
		// get repeated titles list
		for (int i = 0; i < list.size(); i++) {
			bookTitles.add(list.get(i).getTitle());
		}
		
		// get countedList
		for (int i = 0; i < bookTitles.size(); i++) {
			BookTransaction bt = new BookTransaction();
			bt.setTitle(bookTitles.get(i));
			bt.setId(Collections.frequency(bookTitles, bookTitles.get(i)));			// count set in place of id
			if (  !countedList.stream().map(BookTransaction::getTitle).filter(bookTitles.get(i)::equals).findFirst().isPresent() ) {   // I dont know ehy the heck  !contedList.contains(bt) didnt work at all
				countedList.add(bt);
			}
		}

		
		//arrange counted List in decreasing count
		Comparator<BookTransaction> myList = Comparator.comparing(BookTransaction :: getId);
		Collections.sort(countedList, myList);
		Collections.reverse(countedList);
		//set rank in place of id's
		for (int i = 0; i < countedList.size(); i++) {
			countedList.get(i).setId(i+1);
		}
		mostFamousBooksTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(countedList));
		
	}

	
	public void populateTodaysTransacntionTable(List<BookTransaction> list){
		
		todaysTransactionsTable.getItems()
		.setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( Arrays.asList(new String[] {LocalDate.now().toString()}) , "search" )));
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
