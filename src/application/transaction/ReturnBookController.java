package application.transaction;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.book.Book;
import application.book.BookManagementService;
import application.home.DashboardManagementController;
import application.home.SettingsController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

public class ReturnBookController implements Initializable{
	
	
	@FXML
	public Pane returnBookTabPage;
	
	@FXML
	public TextField returnBookUsn;
	@FXML
	public Label returnBookTabPageStatus;
	@FXML
	public TableView<BookTransaction> returnBookTable;
	@FXML
	public TableColumn<BookTransaction, Integer> returnBookColumnId;
	@FXML
	public TableColumn<BookTransaction, String> returnBookColumnBookCode;
	@FXML
	public TableColumn<BookTransaction, String> returnBookColumnBookTitle;
	
	@FXML
	public Label returnBookId;
	@FXML
	public Label returnBookName;
	@FXML
	public Label returnBookBranch;
	@FXML
	public Label returnBookSem;
	@FXML
	public Label returnBookBookTitle;
	@FXML
	public Label returnBookShelf;
	@FXML
	public Label returnBookIssueDate;
	
	@FXML
	public DatePicker returnBookReturnDate;
	@FXML
	public CheckBox returnBookDamaged;
	@FXML
	public CheckBox returnBookLost;
	@FXML
	public TextArea returnBookRemarks;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		returnBookColumnId.setCellValueFactory(new PropertyValueFactory<BookTransaction, Integer>("id"));
		returnBookColumnBookCode.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("code"));
		returnBookColumnBookTitle.setCellValueFactory(new PropertyValueFactory<BookTransaction, String>("title"));
		returnBookTable.getItems().clear();
		returnBookTabPageStatus.setText("");
		returnBookId.setText("");
		returnBookName.setText("");
		returnBookBranch.setText("");
		returnBookSem.setText("");
		returnBookBookTitle.setText("");
		returnBookShelf.setText("");
		returnBookIssueDate.setText("");
		
    	//search event
		returnBookUsn.setOnKeyReleased(new EventHandler<KeyEvent>() {
	         @Override  
	         public void handle(KeyEvent keyEvent) {
	        	  
	        	 ObservableList<BookTransaction> oList = new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getBookTransactionsByUsnAndIssued(returnBookUsn.getText()));                           
	        	 returnBookTable.getItems().setAll(oList);
	        	 if(oList.isEmpty()) {
	        		 returnBookTable.getItems().clear();
	        		 returnBookTabPageStatus.setText("No transactions found for this usn.");
	        	 }else {
		        	 returnBookTable.getItems().setAll(oList);
	        		 returnBookTabPageStatus.setText(oList.size() + " transactions found due..");

	        	 }
	         } 
	     });
		
		
		returnBookLost.setOnAction(event -> {
			if (returnBookLost.isSelected()) {
				returnBookRemarks.setDisable(false);
			}else {
				returnBookRemarks.setDisable(true);
			}
			returnBookDamaged.setSelected(false);
			
		});
		
		returnBookDamaged.setOnAction(event -> {
			if (returnBookDamaged.isSelected()) {
				returnBookRemarks.setDisable(false);
			}else {
				returnBookRemarks.setDisable(true);
			}
			returnBookLost.setSelected(false);
		});
		
		
		returnBookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	BookTransaction b = returnBookTable.getSelectionModel().getSelectedItem();
				b = new BookTransactionManagementService().getBookTransactionById(b.getId());
				returnBookId.setText(""+b.getId());
				returnBookName.setText(b.getName());
				returnBookBranch.setText(b.getBranch());
				returnBookSem.setText(b.getSem());
				returnBookBookTitle.setText(b.getTitle());
				returnBookIssueDate.setText(b.getIssueDate());
				
				returnBookReturnDate.setValue(LocalDate.now());
				returnBookRemarks.setText("");
				returnBookRemarks.setDisable(true);
				returnBookDamaged.setSelected(false);
				returnBookLost.setSelected(false);
				
				Book bb = new BookManagementService().getBookByBookCode(b.getCode());
				if (bb !=null) {
					returnBookShelf.setText(bb.getShelf());
				}else {
					returnBookShelf.setText("--");
				}
				
		    }
		});
		
		
		
		DashboardManagementController.issueOrReturnStage.setOnCloseRequest(new EventHandler<WindowEvent>() { 			//get iisueStage that was show() from DashboardManagementController class
	          public void handle(WindowEvent we) {
	              SettingsController.refreshed = false;
	          }
	      });
	
	}

	
	
	
	public void returnBookTransaction() {
		BookTransaction b = returnBookTable.getSelectionModel().getSelectedItem();
		if (b == null) {
			returnBookTabPageStatus.setText("Please Select a transaction first");
		}else if(( returnBookDamaged.isSelected() || returnBookLost.isSelected() )  && returnBookRemarks.getText().isEmpty() ){
			returnBookRemarks.requestFocus();
			returnBookTabPageStatus.setText("Please add remarks!!");
		}else {
			if (new BookTransactionManagementService().updateBookTransactionById(b.getId(),   returnBookReturnDate.getValue()==null?LocalDate.now().toString():returnBookReturnDate.getValue().toString(),     returnBookDamaged.isSelected(), returnBookLost.isSelected(), returnBookRemarks.getText())) {
				if (returnBookDamaged.isSelected()) {
					new BookManagementService().updateBookStatus("Damaged", b.getCode());
				}else if (returnBookLost.isSelected()) {
					new BookManagementService().updateBookStatus("Lost",b.getCode());
				}else {
					new BookManagementService().updateBookStatus("Available",b.getCode());
				}
				
				returnBookTabPageStatus.setText("Book - "+ b.getCode() + " Returned");
				returnBookTable.getItems().setAll(new BookTransactionManagementService().getObservableBookTransactionList(new BookTransactionManagementService().getBookTransactionsByUsnAndIssued(returnBookUsn.getText())));                           
			}else {
				returnBookTabPageStatus.setText("Sorry! An error occured.");
			}
		}

	}
 
	
	
}
