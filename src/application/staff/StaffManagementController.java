package application.staff;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import application.home.SettingsController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class StaffManagementController implements Initializable{				// a simply amazing interface called Initializable with initialize method thats fired upon the FXML related to this controller is shown
	
	@FXML
	public Pane staffTabPage;
	
	@FXML
	public Label staffTabPageStatus;
	@FXML
	public TextField newStaffName;
	@FXML
	public TextField newStaffEmail;
	@FXML
	public TextField newStaffPassword;
	@FXML
	public TextField newStaffRfid;
	
	@FXML
	public TableView<Staff> staffTable;
	@FXML
	public TableColumn<Staff, Integer> staffColumnId;
	@FXML
	public TableColumn<Staff, String> staffColumnName;
	@FXML
	public TableColumn<Staff, String> staffColumnEmail;
	@FXML
	public TableColumn<Staff, String> staffColumnPassword;
	@FXML
	public TableColumn<Staff, String> staffColumnRfid;
	
	
	
	// this method is fired when the fxml related to this controller is triggerd
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		staffColumnId.setCellValueFactory(new PropertyValueFactory<Staff, Integer>("id"));
    	staffColumnName.setCellValueFactory(new PropertyValueFactory<Staff, String>("name"));
    	staffColumnEmail.setCellValueFactory(new PropertyValueFactory<Staff, String>("email"));
    	staffColumnPassword.setCellValueFactory(new PropertyValueFactory<Staff, String>("password"));
    	staffColumnRfid.setCellValueFactory(new PropertyValueFactory<Staff, String>("rfid"));
    	refreshTab();
		
		staffTabPage.setOnMouseEntered(event->{
			if (!SettingsController.refreshed) {
				SettingsController.refreshed = true;
				refreshTab();
			}
		});
		
	}
	
	public void refreshTab(){
		staffTabPageStatus.setText("");
		newStaffName.setText("");
		newStaffEmail.setText("");
		newStaffPassword.setText("");
		newStaffRfid.setText("");
		populateStaffTable();
	}

	
	//method to add new staff
	public void registerNewStaff(ActionEvent event) {
		if (newStaffName.getText().isEmpty() || newStaffEmail.getText().isEmpty() || newStaffPassword.getText().isEmpty() ) {
			staffTabPageStatus.setText("Please dont leave * marked fields empty!");
			staffTabPageStatus.setTextFill(Paint.valueOf("RED"));
		}else {
			StaffManagementService regService = new StaffManagementService();
			if( regService.registerStaff(newStaffName.getText(), StaffManagementService.loggedInStaff.getCollege(), newStaffEmail.getText(), newStaffPassword.getText(), "staff", newStaffRfid.getText()) ) {
				staffTabPageStatus.setText("New Satff added Successfully");
			}else {
				staffTabPageStatus.setText("Please use another Email!");
			}
			populateStaffTable();
		}
		
	}
	
	//method to delete staff
	public void deleteStaff(ActionEvent event) {
		Staff deleteStaff = staffTable.getSelectionModel().getSelectedItem();
		if (deleteStaff != null) {
			new StaffManagementService().deleteStaff(deleteStaff);
			staffTabPageStatus.setText("Staff deleted");
			populateStaffTable();
		}else {
			staffTabPageStatus.setText("Select a staff to delete");
		}
	}

	// method to populate table data
    public void populateStaffTable() {
    	staffTable.getItems().setAll(new StaffManagementService().getObservableStaffList(new StaffManagementService().getFulltStaffList()));
    }
	
	
}
