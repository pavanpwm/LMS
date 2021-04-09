package application.transaction;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import application.export.ExportService;
import application.home.SettingsController;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
	        	 bookTransactionTabPageStatus.setText("Searching.... Please wait!");
	        	 bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList(bookTransactionSearch.getText(), "search")));
	        	 bookTransactionTabPageStatus.setText("Showing results for search : " +  bookTransactionSearch.getText());
	     		 bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	         }  
	     });		
		
		//a  jugaad way after hours of shitting
		bookTransactionTabPage.setOnMouseEntered(event->{
			if (!SettingsController.refreshed) {
				SettingsController.refreshed = true;
				refreshTab();
			}
		});
		
		
	}
	
	
	
	
	//refresh action event
	public void refreshTab() {
		bookTransactionTabPageStatus.setText("Getting records.... Please wait!");
		populateBookTransactionTableForShowAll();
		bookTransactionTabPageStatus.setText("");
		bookTransactionSearch.setText("");
	}
	
	
	
	
	
	//method to populate table data
	public void populateBookTransactionTableForShowAll() {
		bookTransactionTabPageStatus.setText("Getting records.... Please wait!");
		bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getFullBookTransactionList()));
		bookTransactionTabPageStatus.setText("Showing results for Show all" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
	}
	
	public void populateBookTransactionTableForIssued() {
		bookTransactionTabPageStatus.setText("Getting records.... Please wait!");
   	 	bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Issued" , "issued" )));
		bookTransactionTabPageStatus.setText("Showing results for books issued" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
		bookTransactionTable.getSortOrder().setAll(bookTransactionColumnId);
	}
	
	public void populateBookTransactionTableForReturned() {
		bookTransactionTabPageStatus.setText("Getting records.... Please wait!");
   	 	bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Returned" , "returned" )));
   	 	bookTransactionTabPageStatus.setText("Showing results for books returned" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
		bookTransactionTable.getSortOrder().setAll(bookTransactionColumnId);
	}
	
	public void populateBookTransactionTableForDamaged() {
		bookTransactionTabPageStatus.setText("Getting records.... Please wait!");
		bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Damaged" , "damaged" )));
		bookTransactionTabPageStatus.setText("Showing results for books damaged" );
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
		bookTransactionTable.getSortOrder().setAll(bookTransactionColumnId);
	}	
	
	public void populateBookTransactionTableForLost() {
		bookTransactionTabPageStatus.setText("Getting records.... Please wait!");
		bookTransactionTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getSearchedBookTransactionList( "Lost" , "lost" )));
		bookTransactionTabPageStatus.setText("Showing results for books lost");
		bookTransactionColumnId.setSortType(TableColumn.SortType.DESCENDING);
		bookTransactionTable.getSortOrder().setAll(bookTransactionColumnId);
	}
	
	
	
	
	
	
	
	//method to export / print bookTransactions displayed on table
	
	
	
	
    public void exportAsPDF(MouseEvent event) {
		bookTransactionTabPageStatus.setText("Downloading PDF...... Please wait!");
    	ObservableList<BookTransaction> list = bookTransactionTable.getItems();
        List<List> printData = new ArrayList<>();
        String[] headers = {"Index", "   USN   ", "    Name    ", " Phone ", "Branch", "Sem", " Book Code ", "           Title          ", "IssueDate", "ReturnDate", "Remarks"};
        printData.add(Arrays.asList(headers));
        for (BookTransaction info : list) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(info.getId()));
            row.add(info.getUsn());
            row.add(info.getName());
            row.add(info.getPhone());
            row.add(info.getBranch());
            row.add(info.getSem());
            row.add(info.getCode());
            row.add(info.getTitle());
            row.add(info.getIssueDate());
            row.add(info.getReturnDate());
            row.add(info.getRemarks());

            printData.add(row);
        }
        if (ExportService.initPDFExprot( (Stage) bookTransactionTable.getScene().getWindow(), printData)) {
			bookTransactionTabPageStatus.setText("PDF downloaded successfully.");
		}else {
			bookTransactionTabPageStatus.setText("Couldnt download the PDF!");
		}
        

    }
	
	
	
	
	
	
	
	
	
	
	

	

}
