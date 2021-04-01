package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookTransactionManagementController implements Initializable {
	
	
	@FXML
	public Pane bookTransactionTabPage;
	
	@FXML
	public Label bookTransactionTabPageStatus;
	
	@FXML
	public TextField bookTransactionSearch;
	
	@FXML
	public TableView<BookTransaction> bookTransactionTable;
	@FXML
	public TableColumn<BookTransaction, Integer> bookTransactionColumnId;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnName;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnUsn;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnBranch;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnSem;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnEmail;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnPhone;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnBookCode;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnBookTitle;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnStatus;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnIssueDate;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnReturnDate;
	@FXML
	public TableColumn<BookTransaction, String> bookTransactionColumnRemarks;
		
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		bookTransactionColumnId.setCellValueFactory(new PropertyValueFactory<BookTransaction, Integer>("id"));
    	bookTransactionColumnName.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("name"));
    	bookTransactionColumnUsn.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("usn"));
    	bookTransactionColumnBranch.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("branch"));
    	bookTransactionColumnSem.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("sem"));
    	bookTransactionColumnEmail.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("email"));
    	bookTransactionColumnPhone.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("phone"));
    	bookTransactionColumnBookCode.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("code"));	
    	bookTransactionColumnBookTitle.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("title"));	
    	bookTransactionColumnStatus.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("status"));	
    	bookTransactionColumnIssueDate.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("issueDate"));	
    	bookTransactionColumnReturnDate.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("returnDate"));	
    	bookTransactionColumnRemarks.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("remarks"));	
    	bookTransactionTable.setTableMenuButtonVisible(true);
    	populateBookTransactionTableForShowAll();
		bookTransactionTabPageStatus.setText("");
		
		//search event
		bookTransactionSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
	         @Override  
	         public void handle(KeyEvent keyEvent) {
	        	 bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList(bookTransactionSearch.getText(), "search")));
	        	 bookTransactionTabPageStatus.setText("Showin results for search : " +  bookTransactionSearch.getText());
	     		 bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	         }  
	     });		
		
		//a  jugaad way after hours of shitting
		bookTransactionTabPage.setOnMouseEntered(event->{
			if (SettingsController.tabNumber == 2) {
				SettingsController.tabNumber = 0;
				refreshTab();
			}
		});
		
		
	}
	
	
	
	
	//refresh action event
	public void refreshTab() {
		populateBookTransactionTableForShowAll();
		bookTransactionTabPageStatus.setText("");
		bookTransactionSearch.setText("");
	}
	
	
	
	
	
	//method to populate table data
	public void populateBookTransactionTableForShowAll() {
		bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getFullBookTransactionList()));
		bookTransactionTabPageStatus.setText("Showin results for Show all" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	}
	
	public void populateBookTransactionTableForIssued() {
   	 	bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Issued" , "issued" )));
		bookTransactionTabPageStatus.setText("Showin results for books issued" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	}
	
	public void populateBookTransactionTableForReturned() {
   	 	bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Returned" , "returned" )));
   	 	bookTransactionTabPageStatus.setText("Showin results for books returned" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	}
	
	public void populateBookTransactionTableForDamaged() {
		bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Damaged" , "damaged" )));
		bookTransactionTabPageStatus.setText("Showin results for books damaged" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	}	
	
	public void populateBookTransactionTableForLost() {
		bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Lost" , "lost" )));
		bookTransactionTabPageStatus.setText("Showin results for books lost");
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	}
	
	
	
	
	
	
	
	//method to export / print bookTransactions displayed on table
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

	

}
