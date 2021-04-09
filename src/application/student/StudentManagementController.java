package application.student;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

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
    	studentTable.setTableMenuButtonVisible(true);
		populateStudentTable();
		studentTabPageStatus.setText("");
		
		//search event
		studentSearch.setOnKeyReleased(new EventHandler<KeyEvent>() {
	         @Override  
	         public void handle(KeyEvent keyEvent) {
	        	 studentTabPageStatus.setText("Searching.... Please wait!");
	        	 studentTable.getItems().setAll(new StudentManagementService().getObservableStudentList(new StudentManagementService().getSearchedStudentList(studentSearch.getText())));
	        	 studentTabPageStatus.setText("");
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
			StudentManagementService addStudentService = new StudentManagementService();
			
			if(addStudentService.addOrEditStudent(id, newStudentUsn.getText(), newStudentName.getText(), newStudentBranch.getText(), newStudentSem.getText(), newStudentPhone.getText(), newStudentEmail.getText()) ) {
				if (id.isEmpty()) {
					studentTabPageStatus.setText("New Student added Successfully");
					studentTabPageStatus.setTextFill(Paint.valueOf("GREEN"));
				}else {
					studentTabPageStatus.setText("Student updated Successfully");
					studentTabPageStatus.setTextFill(Paint.valueOf("GREEN"));
				}
			}else {
				studentTabPageStatus.setText("Student already registered");
				studentTabPageStatus.setTextFill(Paint.valueOf("RED"));
			}
			//after saving, show the saved data on table
			studentTable.getItems().setAll(new StudentManagementService().getObservableStudentList(new StudentManagementService().getSearchedStudentList(newStudentUsn.getText())));
		}
		
	}
	
	
	
	
	//method to edit student
	//just need to set id on status label and fill form with slected student details
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
			studentTabPageStatus.setText("Right click on a student to edit");
		}
	}
	
	
	
	
	
	//method to delete selected student
		public void deleteStudent(ActionEvent event) {
			Student deleteStudent = studentTable.getSelectionModel().getSelectedItem();
			if (deleteStudent != null) {
				new StudentManagementService().deleteStudent(deleteStudent);
				studentTabPageStatus.setText("Student deleted");
				populateStudentTable();
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
	}
	
	
	
	
	
	//method to populate table data
	private void populateStudentTable() {
		studentTable.getItems().setAll(new StudentManagementService().getObservableStudentList(new StudentManagementService().getFullStudentList()));
	}
	
	
	
	
	
	
	
	
	//method to get excel file and save it to database
	
	//method to search table was added as event in initialize method
	
	//method to export / print students displayed on table
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

}
