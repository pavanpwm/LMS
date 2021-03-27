package application;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class BookManagementController implements Initializable {
	
	
	
	@FXML
	public Pane bookTabPage;
	
	@FXML
	public TextField newBookCode;
	@FXML
	public TextArea newBookTitle;
	@FXML
	public TextField newBookShelf;
	@FXML
	public CheckBox bookRequestCheckBox;
	
	@FXML
	public Label bookTabPageStatus;
	
	
	@FXML
	public TextField bookSearch;
	@FXML
	public TableView<Book> bookTable;
	@FXML
	public TableColumn<Book, Integer> bookColumnId;
	@FXML
	public TableColumn<Book, String> bookColumnCode;
	@FXML
	public TableColumn<Book, String> bookColumnTitle;
	@FXML
	public TableColumn<Book, String> bookColumnShelf;
	@FXML
	public TableColumn<Book, String> bookColumnStatus;
	
	
	

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		bookColumnId.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
		bookColumnCode.setCellValueFactory(new PropertyValueFactory<Book, String>("code"));
		bookColumnTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		bookColumnShelf.setCellValueFactory(new PropertyValueFactory<Book, String>("shelf"));
		bookColumnStatus.setCellValueFactory(new PropertyValueFactory<Book, String>("status")); 	
		populateBookTable();
		bookTabPageStatus.setText("");
				
				//search book event
		bookSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			         @Override  
			         public void handle(KeyEvent keyEvent) {
			        	 bookTable.getItems().setAll(new BookManagementService().getObservableBookList(new BookManagementService().getSearchedBookList(bookSearch.getText())));
			         }  
			});

	}
	
	
	
	//refresh action event
		public void refreshTab() {
			populateBookTable();
			bookTabPageStatus.setText("");
			newBookCode.setText("");
			newBookTitle.setText("");
			newBookShelf.setText("");
			bookSearch.setText("");
			newBookCode.setDisable(false);
			newBookShelf.setDisable(false);
			bookRequestCheckBox.setSelected(false);
			
		}
		
		
		
		
		//method for books request checkBox
		public void ifBookRequest(ActionEvent event) {
			if (bookRequestCheckBox.isSelected()) {
				//clear text fields and disable em
				newBookCode.setText("");
				newBookShelf.setText("");
				bookTabPageStatus.setText("");
				newBookCode.setDisable(true);
				newBookShelf.setDisable(true);
			}else {
				newBookCode.setDisable(false);
				newBookShelf.setDisable(false);
			}
			
		}
		



		//method to add new book
		public void addOrEditBook(ActionEvent event) {
			
			String id;
			String status= "Available";
			if (bookTabPageStatus.getText().contains("Editing")) {
				id = bookTabPageStatus.getText().replaceAll("Editing: ", "").trim();
			}else {
				id = "";
			}
			
			if(bookRequestCheckBox.isSelected()) {
				status= "Requested";
			}
					
			if (!bookRequestCheckBox.isSelected() && ( newBookCode.getText().trim().isEmpty() || newBookShelf.getText().trim().isEmpty() ) ||  newBookTitle.getText().trim().isEmpty() ){
				
				bookTabPageStatus.setText("Please dont leave empty fields!");
				bookTabPageStatus.setTextFill(Paint.valueOf("RED"));
				
			}else {
				BookManagementService addBookService = new BookManagementService();
				
				if( addBookService.addOrEditBook(id, newBookCode.getText(), newBookTitle.getText(), newBookShelf.getText(), status) ) {
					if (id.isEmpty()) {
						bookTabPageStatus.setText("New book added successfully");
						bookTabPageStatus.setTextFill(Paint.valueOf("GREEN"));
					}else {
						bookTabPageStatus.setText("Book updated successfully");
						bookTabPageStatus.setTextFill(Paint.valueOf("GREEN"));
					}
				}else {
					bookTabPageStatus.setText("Book already exists");
					bookTabPageStatus.setTextFill(Paint.valueOf("RED"));
				}
				//after saving, show the saved data on table
				bookTable.getItems().setAll(new BookManagementService().getObservableBookList(new BookManagementService().getSearchedBookList(newBookCode.getText())));
			}
			
		}
		
		
		
		
		//method to edit book
		//just need to set id on status label and fill form with slected book details
		public void editBook(ActionEvent event) {
			Book editBook = bookTable.getSelectionModel().getSelectedItem();
			if (editBook != null && !editBook.getStatus().toLowerCase().contains("req")) {
				bookTabPageStatus.setText("Editing: "+ editBook.getId());
				newBookCode.setText(editBook.getCode());
				newBookTitle.setText(editBook.getTitle());
				newBookShelf.setText(editBook.getShelf());
				newBookCode.setDisable(true);
			}else {
				bookTabPageStatus.setText("Right click on a book to edit");
			}
			if(editBook.getStatus().toLowerCase().contains("req")) {
				refreshTab();
				bookTabPageStatus.setText("Requested books cannpt be edited.");
			}
		}
	
		
		
		//method to delete selected book
		public void deleteBook(ActionEvent event) {
			Book deleteBook = bookTable.getSelectionModel().getSelectedItem();
				if (deleteBook != null) {
					new BookManagementService().deleteBook(deleteBook);
					bookTabPageStatus.setText("Book deleted");
					populateBookTable();
				}else {
					bookTabPageStatus.setText("Select a book to delete");
				}
		}



		public void populateBookTable() {
			bookTable.getItems().setAll(new BookManagementService().getObservableBookList(new BookManagementService().getFullBookList()));
			
		}
	
		
	
		//  delete all books should be in settings page
	
	
	
		//method to export / print students displayed on table
		
		
		

}
