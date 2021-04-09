package application.home;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import application.staff.StaffManagementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import javafx.scene.control.TextField;

public class SettingsController implements Initializable {
	
	
	@FXML
	public Label collegeName;	
	@FXML
	public Label loggedInStaffNameAndTime;
	@FXML
	public Label staffId;				// for db reference
	@FXML
	public TextField settingsPageName;
	@FXML
	public TextField settingsPageEmail;
	@FXML
	public TextField setingsPagePassword;
	@FXML
	public Label setingsPageStatus;
	
	@FXML
	public Tab dashboardTab;
	@FXML
	public Tab bookTransactionsTab;
	@FXML
	public Tab bookManagementTab;
	@FXML
	public Tab studentManagementTab;
	@FXML
	public Tab staffManagementTab;
	
	public static boolean refreshed = false;
	
	
	
	
	// below method generated due to implementation of Initializable interface
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if (StaffManagementService.loggedInStaff.getRole().equalsIgnoreCase("staff")) {
			staffManagementTab.setDisable(true);
		}
		
		initialiseHomePage();
		
		dashboardTab.setOnSelectionChanged(event->{
			if (dashboardTab.isSelected()) {
				refreshed = false;
	        }
		});
		bookTransactionsTab.setOnSelectionChanged(event->{
			if (bookTransactionsTab.isSelected()) {
				refreshed = false;
	        }
		});
		bookManagementTab.setOnSelectionChanged(event->{
			if (bookManagementTab.isSelected()) {
				refreshed = false;
	        }
		});
		studentManagementTab.setOnSelectionChanged(event->{
			if (studentManagementTab.isSelected()) {
				refreshed = false;
	        }
		});
		staffManagementTab.setOnSelectionChanged(event->{
			if (staffManagementTab.isSelected()) {
				refreshed = false;
	        }
		});
		
		
		
		
	}
	
	
	// method to show college name and staff name on homepage
	public void initialiseHomePage() {
		staffId.setText(""+StaffManagementService.loggedInStaff.getId());
		collegeName.setText(StaffManagementService.loggedInStaff.getCollege().toUpperCase());
		loggedInStaffNameAndTime.setText("Logged-in as  " + StaffManagementService.loggedInStaff.getName() + " at " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
	}
	
	
	// method to update personal settings of loggedin staff
	public void updatePersonalSettings(ActionEvent event) {
		if( !settingsPageName.getText().isEmpty() || !settingsPageEmail.getText().isEmpty() || !setingsPagePassword.getText().isEmpty() ){
			if(new SettingsPageService().updatePersonalDetailsService(Integer.parseInt(staffId.getText()), settingsPageName.getText(), settingsPageEmail.getText(), setingsPagePassword.getText()) ) {
				setingsPageStatus.setText("Update successful");
			}else {
				setingsPageStatus.setText("Something went wrong! Try again later..");
			}
		}else {
			setingsPageStatus.setText("Please enter something..");
		}
		
		initialiseHomePage();
		settingsPageName.clear();
		settingsPageEmail.clear();
		setingsPagePassword.clear();
	}


	
	
	
	
	
	
	
	
	
	
	

}
