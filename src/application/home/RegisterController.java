package application.home;

import application.MainClass;
import application.staff.StaffManagementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

	
	@FXML
	private TextField regName;
	@FXML
	private TextField regCollege;
	@FXML
	private TextField regEmail;
	@FXML
	private PasswordField regPassword;
	@FXML
	private PasswordField regRfid;
	
	
	MainClass stage = new MainClass();
	StaffManagementService staffService = new StaffManagementService();
	
	public void adminRegister(ActionEvent event) throws Exception {						// to handle exception you can wither use throws or try and  catch block
		if( !( regName.getText().isEmpty() || regCollege.getText().isEmpty() || regEmail.getText().isEmpty() || regPassword.getText().isEmpty() )  && staffService.registerStaff(regName.getText(), regCollege.getText(), regEmail.getText(), regPassword.getText(), "admin", regRfid.getText()) ) {
			stage.runStageFXML("FXML/LoginPage.fxml");
			((Node)(event.getSource())).getScene().getWindow().hide();				// note the sequence of these two lines i.e first open a new stage and close previous one
		}
}
	
}
