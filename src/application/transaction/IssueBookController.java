package application.transaction;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import application.book.Book;
import application.book.BookManagementService;
import application.home.DashboardManagementController;
import application.home.SettingsController;
import application.student.Student;
import application.student.StudentManagementService;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.WindowEvent;

public class IssueBookController implements Initializable{
	
	
	@FXML
	public Pane issueBookTabPage;
	
	@FXML
	public Label issueBookTabPageStatus;
	
	
	@FXML
	public TextField issueBookUsn;
	@FXML
	public TextField issueBookName;
	@FXML
	public TextField issueBookBranch;
	@FXML
	public TextField issueBookSem;
	@FXML
	public TextField issueBookPhone;
	@FXML
	public DatePicker issueBookIssueDate;
	@FXML
	public TextField issueBookEmail;
	@FXML
	public TextField issueBookBookCode;
	@FXML
	public TextField issueBookBookTitle;

	@FXML
	public TableView<Book> issueBookTable;
	@FXML
	public TableColumn<Book, String> issueBookColumnBookCode;
	@FXML
	public TableColumn<Book, String> issueBookColumnBookTitle;

	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		issueBookColumnBookCode.setCellValueFactory(new PropertyValueFactory<Book, String>("code"));
		issueBookColumnBookTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		issueBookTable.getItems().clear();
    	issueBookTabPageStatus.setText("");
    	
    	issueBookTabPageStatus.setTextFill(Paint.valueOf("RED"));
		
    	//search event
    	issueBookUsn.setOnKeyReleased(new EventHandler<KeyEvent>() {
	         @Override  
	         public void handle(KeyEvent keyEvent) {
	        	 //populate student details field
	        	 Student student = new StudentManagementService().getStudentByUsn(issueBookUsn.getText().trim());
	        	 if(student != null ) {
	        		 issueBookUsn.setText(student.getUsn());
	        		 issueBookName.setText(student.getName());
	        		 issueBookBranch.setText(student.getBranch());
	        		 issueBookSem.setText(student.getSem());
	        		 issueBookPhone.setText(student.getMobile());
	        		 issueBookIssueDate.setValue(LocalDate.now());
	        		 issueBookEmail.setText(student.getEmail());
	        		 issueBookTabPageStatus.setText("One student found for usn : " + issueBookUsn.getText());
	        		 issueBookBookCode.requestFocus();
	        	 }else {
	        		 issueBookName.setText("");
	        		 issueBookBranch.setText("");
	        		 issueBookSem.setText("");
	        		 issueBookPhone.setText("");
	        		 issueBookEmail.setText("");
	        		 issueBookTabPageStatus.setText("No student found for usn : " + issueBookUsn.getText());
	        	 }
	         } 
	     });			
    	
    	
    	issueBookBookCode.setOnKeyReleased(new EventHandler<KeyEvent>() {
	         @Override  
	         public void handle(KeyEvent keyEvent) {
	        	 Book book =   new BookManagementService().getBookByBookCode( issueBookBookCode.getText().trim());
	        	 boolean boolAlreadyAdded = bookAlreadyExistsInTable();
	        	 if(book != null && !boolAlreadyAdded) {
	        		 issueBookBookCode.setText(book.getCode());
	        		 issueBookBookTitle.setText(book.getTitle());
	        		 issueBookTabPageStatus.setText("One book found for book code : " + issueBookBookCode.getText());
	        		 try { wait(500);; }catch (Exception e) {}
	        		 updateIssueBookTable();
	        	 }else if(boolAlreadyAdded){
	        		 
	        	 }else{
	        		 issueBookTabPageStatus.setText("No Book found for book code : " + issueBookBookCode.getText());
	        	 }
	         }  
    	});
    	
    	DashboardManagementController.issueOrReturnStage.setOnCloseRequest(new EventHandler<WindowEvent>() { 			//get ixsueStage that was show() from DashboardManagementController class
	          public void handle(WindowEvent we) {
	              SettingsController.refreshed = false;
	          }
	      });
	}




	public void updateIssueBookTable() {
		if(bookAlreadyExistsInTable()) {
    		issueBookBookTitle.setText("");
		}else {
			if (issueBookBookCode.getText().isEmpty() ||issueBookBookTitle.getText().isEmpty() ) {
				issueBookTabPageStatus.setText("Please dont leave star (*) marked fields empty!.");
			}else {
				Book book = new Book();
	    		book.setCode(issueBookBookCode.getText().toUpperCase());
	    		book.setTitle(issueBookBookTitle.getText());
	    		issueBookTable.getItems().add(book);
	    		issueBookBookCode.setText("");
	    		issueBookBookTitle.setText("");
	    		issueBookTabPageStatus.setText("Book " + issueBookBookCode.getText() +  " added");
			}
			
    	}
	}
	
		
	public boolean bookAlreadyExistsInTable() {
		ObservableList<Book> list =  issueBookTable.getItems();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getCode().trim().equalsIgnoreCase(issueBookBookCode.getText().trim())) {
				issueBookTabPageStatus.setText("Book is already added");
				return true;
            }
		}
		return false;
	}
	
	
	
		
	public void issueBook() {
		if (issueBookUsn.getText().isEmpty() || issueBookName.getText().isEmpty() || issueBookBranch.getText().isEmpty() || issueBookSem.getText().isEmpty() || issueBookPhone.getText().isEmpty() || issueBookIssueDate.getValue() == null) {
			issueBookTabPageStatus.setText("Please dont leave star (*) marked fields empty!.");
		}else {
			ObservableList<Book> list =  issueBookTable.getItems();
			for (int i = 0; i < list.size(); i++) {
				Book eachBook =  list.get(i);
	            BookTransaction bookTransaction = new BookTransaction(issueBookUsn.getText().toUpperCase(), issueBookName.getText(), issueBookBranch.getText(), issueBookSem.getText(), eachBook.getCode().toUpperCase(), eachBook.getTitle(), issueBookPhone.getText(), issueBookEmail.getText(), issueBookIssueDate.getValue().toString(), "", "Issued", "");
	            new BookTransactionManagementService().addNewBookTransaction(bookTransaction);
	            //set status in Book  table
	            new BookManagementService().updateBookStatus("Issued", eachBook.getCode());
			}
			
			issueBookTable.getItems().clear();
	    	issueBookTabPageStatus.setText("Books successfully issued.");
		}
		
	}
	
	
	
	
	public void removeSelected() {
		if (issueBookTable.getSelectionModel().getSelectedItems().isEmpty()) {
			issueBookTabPageStatus.setText("Please select a book to remove");
		}else {
			issueBookTable.getItems().remove(issueBookTable.getSelectionModel().getSelectedItems().get(0));
	    	issueBookTabPageStatus.setText("Books Successfully issued.");
		}
		
	}
	
	public void removeAll() {
		issueBookTable.getItems().clear();
    	issueBookTabPageStatus.setText("Books Successfully issued.");
	}
	
	
	
	
	

	
	
	
}
