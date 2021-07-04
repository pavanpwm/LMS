package application.book;

import java.net.URL;
import java.util.ResourceBundle;
import application.home.SettingsController;
import javafx.collections.ObservableList;
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
import javafx.scene.input.ScrollEvent;
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
	public Label tableCountStatus;
	
	
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
		bookColumnId.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
		bookColumnCode.setCellValueFactory(new PropertyValueFactory<Book, String>("code"));
		bookColumnTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		bookColumnShelf.setCellValueFactory(new PropertyValueFactory<Book, String>("shelf"));
		bookColumnStatus.setCellValueFactory(new PropertyValueFactory<Book, String>("status")); 	
		
		
		//initial populate table
		refreshTab();
				
		//search book event handler
		bookSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
			         @Override  
			         public void handle(KeyEvent keyEvent) {
			        	 if (!bookSearch.getText().trim().isEmpty()) {
			        		 bookTable.getItems().clear();
			        		 BookManagementService.lastPageIndex = 0;
				        	 BookManagementService.searchText = bookSearch.getText();
				        	 bookTabPageStatus.setText("Searching.... Please wait!");
				        	 populateBookTable();
				        	 bookTabPageStatus.setText("");
						}else {
							refreshTab();
						}
			         }  
			});
		
		bookTabPage.setOnMouseEntered(event->{
			if (!SettingsController.refreshed) {
				SettingsController.refreshed = true;
				refreshTab();
			}
		});
		
		bookTable.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
               populateBookTable();
            }
        });

	}
	
	
	
	//refresh action event
		public void refreshTab() {
       	    bookTabPageStatus.setText("Getting data.... Please wait!");
       	    BookManagementService.searchText ="";
       	    BookManagementService.lastPageIndex = 0;
       	    bookTable.getItems().clear();
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
				BookManagementService.searchText ="";
	       	    BookManagementService.lastPageIndex = 0;
	       	    bookTable.getItems().clear();
				populateBookTable();
			}
			
		}
		
		
		
		
		//method to edit book
		//need to set id on status label and fill form with selected book details
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
				bookTabPageStatus.setText("Requested books cannot be edited.");
			}
		}
	
		
		
		//method to delete selected book
		public void deleteBook(ActionEvent event) {
			Book deleteBook = bookTable.getSelectionModel().getSelectedItem();
				if (deleteBook != null) {
					new BookManagementService().deleteBook(deleteBook);
					refreshTab();
					bookTabPageStatus.setText("Book deleted");
				}else {
					bookTabPageStatus.setText("Select a book to delete");
				}
		}



		public void populateBookTable() {
			 ObservableList<Book> list = new BookManagementService().getObservableBookList(new BookManagementService().getNextScrollBooks());
			 bookTable.getItems().addAll(list);
			 tableCountStatus.setText("Showing " + bookTable.getItems().size());
			 if (!list.isEmpty()) {
				 bookTable.scrollTo(list.get(0));
			 }
		}

	
		
		

}
