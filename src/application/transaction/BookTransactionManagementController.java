package application.transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import application.exp.imp.ExportService;
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
import javafx.scene.input.ScrollEvent;
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
	public Label tableCountStatus;
	
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

    	
    	//initial populate table
    	refreshTab();
    	
		
		//search event
		bookTransactionSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
	         @Override  
	         public void handle(KeyEvent keyEvent) {
	        	 if (!bookTransactionSearch.getText().trim().isEmpty()) {
	        		 bookTransactionTable.getItems().clear();
	        		 BookTransactionManagementService.lastPageIndex = 0;
	        		 BookTransactionManagementService.searchText = bookTransactionSearch.getText();
	        		 BookTransactionManagementService.searchType = "loose";
	        		 bookTransactionTabPageStatus.setText("Searching.... Please wait!");
	        		 populateBookTransactionTable();
		        	 bookTransactionTabPageStatus.setText("");
				}else {
					refreshTab();
				}
	         }
	     });		
		
		//a  jugaad way after hours of shitting
		bookTransactionTabPage.setOnMouseEntered(event->{
			if (!SettingsController.refreshed) {
				SettingsController.refreshed = true;
				refreshTab();
			}
		});
		
		bookTransactionTable.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
               populateBookTransactionTable();
            }
       });
		
		
	}
	
	
	//refresh action event
	public void refreshTab() {
		 bookTransactionSearch.clear();
		 bookTransactionTable.getItems().clear();
		 BookTransactionManagementService.lastPageIndex = 0;
		 BookTransactionManagementService.searchText = "";
		 BookTransactionManagementService.searchType = "";
		 populateBookTransactionTable();
	}
	
	
	
	

	
	public void populateBookTransactionTableForIssued() {
		bookTransactionTable.getItems().clear();
		BookTransactionManagementService.lastPageIndex = 0;
		BookTransactionManagementService.searchText = "Issued";
		BookTransactionManagementService.searchType = "tight";
		populateBookTransactionTable();
	}
	
	public void populateBookTransactionTableForReturned() {
		bookTransactionTable.getItems().clear();
		BookTransactionManagementService.lastPageIndex = 0;
		BookTransactionManagementService.searchText = "Returned";
		BookTransactionManagementService.searchType = "tight";
		populateBookTransactionTable();
	}
	
	public void populateBookTransactionTableForDamaged() {
		bookTransactionTable.getItems().clear();
		BookTransactionManagementService.lastPageIndex = 0;
		BookTransactionManagementService.searchText = "Damaged";
		BookTransactionManagementService.searchType = "tight";
		populateBookTransactionTable();
	}	
	
	public void populateBookTransactionTableForLost() {
		bookTransactionTable.getItems().clear();
		BookTransactionManagementService.lastPageIndex = 0;
		BookTransactionManagementService.searchText = "Lost";
		BookTransactionManagementService.searchType = "tight";
		populateBookTransactionTable();
	}
	
	
	public void populateBookTransactionTable() {
		 bookTransactionTabPageStatus.setText("Getting records.... Please wait!");
		 ObservableList<BookTransaction> list = new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getNextScrollBookTransactions());
		 bookTransactionTable.getItems().addAll(list);
		 tableCountStatus.setText("Showing " + bookTransactionTable.getItems().size());
		 if (!list.isEmpty()) {
			 bookTransactionTable.scrollTo(list.get(0));
		 }
		 bookTransactionTabPageStatus.setText("Showing records on query : " + BookTransactionManagementService.searchText);
	}
	
	
	
	
	
	
	
	
	
	//method to export / print bookTransactions displayed on table
    public void exportAsPDF(MouseEvent event) {
    	bookTransactionTabPageStatus.setText("Downloading PDF...... Please wait!");
    	int tableCountBefore = bookTransactionTable.getItems().size(); 
    	populateBookTransactionTable();
    	while (tableCountBefore < bookTransactionTable.getItems().size()) {
    		tableCountBefore = bookTransactionTable.getItems().size(); 
        	populateBookTransactionTable();			
		}
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
        if (ExportService.initPDFExport( (Stage) bookTransactionTable.getScene().getWindow(), printData)) {
			bookTransactionTabPageStatus.setText("PDF downloaded successfully.");
		}else {
			bookTransactionTabPageStatus.setText("Couldnt download the PDF!");
		}
    }
	
	
	
	
	
	
	
	
	
	
	

	

}
