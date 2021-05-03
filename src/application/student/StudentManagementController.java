package application.student;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import application.exp.imp.ImportService;
import application.home.SettingsController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentManagementController implements Initializable {
	
	
	@FXML
	private Pane studentTabPage;
	
	@FXML
	private TextField newStudentUsn;	
	@FXML
	private TextField newStudentName;
	@FXML
	private TextField newStudentBranch;
	@FXML
	private TextField newStudentSem;
	@FXML
	private TextField newStudentPhone;
	@FXML
	private TextField newStudentEmail;

	@FXML
	private Label studentTabPageStatus;
	
	
	@FXML
	private TextField studentSearch;
	@FXML
	private Label tableCountStatus;
	
	
	@FXML
	public TableView<Student> studentTable;
	@FXML
	public TableColumn<Student, Integer> studentColumnId;
	@FXML
	public TableColumn<Student, String> studentColumnName;
	@FXML
	public TableColumn<Student, String> studentColumnUsn;
	@FXML
	public TableColumn<Student, String> studentColumnBranch;
	@FXML
	public TableColumn<Student, String> studentColumnSem;
	@FXML
	public TableColumn<Student, String> studentColumnEmail;
	@FXML
	public TableColumn<Student, String> studentColumnPhone;
	
	
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		studentColumnId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
    	studentColumnName.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
    	studentColumnUsn.setCellValueFactory(new PropertyValueFactory<Student, String>("usn"));
    	studentColumnBranch.setCellValueFactory(new PropertyValueFactory<Student, String>("branch"));
    	studentColumnSem.setCellValueFactory(new PropertyValueFactory<Student, String>("sem"));
    	studentColumnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
    	studentColumnPhone.setCellValueFactory(new PropertyValueFactory<Student, String>("mobile")); 	


    	//initial populate table
    	refreshTab();
		
		//search event
		studentSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
	         @Override  
	         public void handle(KeyEvent keyEvent) {
	        	 if (!studentSearch.getText().trim().isEmpty()) {
	        		 studentTabPageStatus.setText("Searching.... Please wait!");
	        		 studentTable.getItems().clear();
	        		 StudentManagementService.lastPageIndex = 0;
	        		 StudentManagementService.searchText = studentSearch.getText();
		        	 populateStudentTable();
		        	 studentTabPageStatus.setText("");
				}else {
					refreshTab();
				}
	         }  
	     });
		
		studentTabPage.setOnMouseEntered(event->{
			if (!SettingsController.refreshed) {
				SettingsController.refreshed = true;
				refreshTab();
			}
		});

	}
	
	
	
	
	
	//refresh action event
	public void refreshTab() {
   	 	studentTabPageStatus.setText("Getting data.... Please wait!");
   	    studentTable.getItems().clear();
	    StudentManagementService.lastPageIndex = 0;
	    StudentManagementService.searchText = "";
		populateStudentTable();
		studentTabPageStatus.setText("");
		newStudentBranch.setText("");
		newStudentEmail.setText("");
		newStudentName.setText("");
		newStudentPhone.setText("");
		newStudentSem.setText("");
		newStudentUsn.setText("");
		studentSearch.setText("");
		newStudentUsn.setDisable(false);
	}





	
	
	
	
	
	//method to add new student
	public void addOrEditStudent(ActionEvent event) {
		
		String id;
		if (studentTabPageStatus.getText().contains("Editing")) {
			id = studentTabPageStatus.getText().replaceAll("Editing: ", "").trim();
		}else {
			id = "";
		}
				
		if ( newStudentName.getText().trim().isEmpty() ||  newStudentBranch.getText().trim().isEmpty() || newStudentEmail.getText().trim().isEmpty() || newStudentPhone.getText().trim().isEmpty() || newStudentSem.getText().trim().isEmpty()
		     || newStudentUsn.getText().isEmpty() ){
			
			studentTabPageStatus.setText("Please dont leave empty fields!");
			studentTabPageStatus.setTextFill(Paint.valueOf("RED"));
			
		}else {
			if(new StudentManagementService().addOrEditStudent(id, newStudentUsn.getText(), newStudentName.getText(), newStudentBranch.getText(), newStudentSem.getText(), newStudentPhone.getText(), newStudentEmail.getText()) ) {
				if (id.isEmpty()) {
					studentTabPageStatus.setText("New Student added Successfully");
				}else {
					studentTabPageStatus.setText("Student updated Successfully");
				}
			}
			//after saving, show the saved data on table
			studentTable.getItems().clear();
		    StudentManagementService.lastPageIndex = 0;
		    StudentManagementService.searchText = "";
			populateStudentTable();
		}
		
	}
	
	
	
	
	//method to edit student
	//just need to set id on status label and fill form with selected student details
	public void editStudent(ActionEvent event) {
		Student editStudent = studentTable.getSelectionModel().getSelectedItem();
		if (editStudent != null) {
			studentTabPageStatus.setText("Editing: "+ editStudent.getId());
			newStudentBranch.setText(editStudent.getBranch());
			newStudentEmail.setText(editStudent.getEmail());
			newStudentName.setText(editStudent.getName());
			newStudentPhone.setText(editStudent.getMobile());
			newStudentSem.setText(editStudent.getSem());
			newStudentUsn.setText(editStudent.getUsn());
			newStudentUsn.setDisable(true);
		}else {
			refreshTab();
		}
	}
	
	
	
	
	
	//method to delete selected student
	public void deleteStudent(ActionEvent event) {
		Student deleteStudent = studentTable.getSelectionModel().getSelectedItem();
		if (deleteStudent != null) {
			new StudentManagementService().deleteStudent(deleteStudent);
			refreshTab();
			studentTabPageStatus.setText("Student deleted");
		}else {
			studentTabPageStatus.setText("Select a Student to delete");
		}
	}	
		
		
	
	//method to delete all students visible in the table
	public void confirmDeleteAllStudents(ActionEvent event) throws IOException {
		Stage primaryStage = (Stage) studentTabPage.getScene().getWindow();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(primaryStage);
		alert.getDialogPane().setHeaderText("Delete All!!");
		alert.getDialogPane().setContentText("Are you sure you want to delete all the entries currently displayed on the table??");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			deleteAllStudents();
		}
	}
	
	
	public void deleteAllStudents() {
		StudentManagementService manager = new StudentManagementService();
		ObservableList<Student> studentListOnTable =  studentTable.getItems();
		studentListOnTable.forEach(student->{
    		Student deleteStudent = new Student(student.getId(), student.getName(), student.getBranch(), student.getSem(), student.getUsn(), student.getEmail(), student.getMobile());
    		manager.deleteStudent(deleteStudent);
    	});
		refreshTab();
		studentTabPageStatus.setText("Deleted displayed student entries");
	}
	
	
	
	
	
	//method to populate table data
	public void populateStudentTable() {
		ObservableList<Student> list = new StudentManagementService().getObservableStudentList(new StudentManagementService().getNextScrollStudents());
		studentTable.getItems().addAll(list);
		tableCountStatus.setText("Showing " + studentTable.getItems().size());
		if (!list.isEmpty()) {
			studentTable.scrollTo(list.get(0));
		}

		
	}
	
	
	
	
	
	
	
	
	//method to get excel file and save data to database
	public void importStudentDetails(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select EXCEL Files");
		//fileChooser.setInitialDirectory(new File("X:\\testdir\\two"));
		fileChooser.getExtensionFilters().addAll(
		        new ExtensionFilter("Excel Files", "*.xlsx"));
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog((Stage) studentTable.getScene().getWindow());
		List<Student> list = new ArrayList<Student>();
		if (selectedFiles != null) {
		    selectedFiles.forEach(file->{
		    	try {
					list.addAll(new ImportService().getStudentListFromExcel(file));
				} catch (IOException e) {
					studentTabPageStatus.setText("Something went wrong!!");
				}
		    });
		}
		
		if (!list.isEmpty()) {
			int size = list.size();
			for (int i = 0; i<size; i++) {
				new StudentManagementService().addOrEditStudent("", list.get(i).getUsn(), list.get(i).getName(), list.get(i).getBranch(), list.get(i).getSem(), list.get(i).getMobile(), list.get(i).getEmail());
				studentTabPageStatus.setText("Importing  " + i +"/" + size + "......Please wait");
			}
			refreshTab();
			studentTabPageStatus.setText("Import completed.");
		}else {
			studentTabPageStatus.setText("Something went wrong!");
		}
		
		
	}
	
	

		
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

}
