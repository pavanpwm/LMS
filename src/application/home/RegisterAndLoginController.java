package application.home;



import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.MainClass;
import application.mail.MailService;
import application.staff.Staff;
import application.staff.StaffManagementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
 



/**
 * 
 * @author pavan BTD
 *
 Note that controller classes uses hibernate package classes like session and transaction and not of javax since javax jpa was only used to avoid using hbm.xml mapping config file
 *
 
 RFID text field was used instead of an invisible key event listener bcoz, user may press keys before scanning their card
 *
 */


public class RegisterAndLoginController implements Initializable {
	
	
	
	


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
	
	@FXML
	private Pane loginPane;
	static String loginRfid = "";
//	@FXML
//	private PasswordField loginRfid;
	@FXML
	private TextField loginEmail;
	@FXML
	private PasswordField loginPassword;
	@FXML
	private Label loginStatus;
	
	
	
	
	MainClass stage = new MainClass();
	StaffManagementService staffService = new StaffManagementService();

	
	
	public void adminRegister(ActionEvent event) throws Exception {						// to handle exception you can wither use throws or try and  catch block
			if( !( regName.getText().isEmpty() || regCollege.getText().isEmpty() || regEmail.getText().isEmpty() || regPassword.getText().isEmpty() )  && staffService.registerStaff(regName.getText(), regCollege.getText(), regEmail.getText(), regPassword.getText(), "admin", regRfid.getText()) ) {
				stage.runStageFXML("FXML/LoginPage.fxml");
				((Node)(event.getSource())).getScene().getWindow().hide();				// note the sequence of these two lines i.e first open a new stage and close previous one
			}
	}
	
	
	
	public void loginRedirect(ActionEvent event) throws Exception {
		if(staffService.login(loginEmail.getText(), loginPassword.getText())) {
			stage.runStageFXML("FXML/HomePage.fxml");
			((Node)(event.getSource())).getScene().getWindow().hide();
		}else {
			loginStatus.setText("Incorrect login credentials!");
		}
	}

	
	
	public void forgotPassword(ActionEvent event) throws Exception {
		if (loginEmail.getText().isEmpty()) {
			loginStatus.setText("Please enter your email!");
		}else {
			Staff staff = staffService.getStaffByEmail(loginEmail.getText().trim());
			if (staff == null) {
				loginStatus.setText("Your email is not registerd with us!");
			}else {
				loginStatus.setText("Password has been sent to your mail.");
				// to avoid application not responding in windows
				new Thread(() -> {
					MailService.sendMail(staff.getEmail(), "Your password for LMS login", "Password : " +  staff.getPassword());
				}).start();
			}
		}
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginPane.setOnKeyPressed( keyEvent ->{
			if (keyEvent.getCode()!= KeyCode.ENTER) {
				loginRfid += keyEvent.getText();
			}else {
				staffService.getFulltStaffList().forEach( staff->{
					if (loginRfid.contains(staff.getRfid())) {
						staffService.loggedInStaff = staff;
						try {
							stage.runStageFXML("FXML/HomePage.fxml");
						} catch (Exception e) {}
						((Node)(keyEvent.getSource())).getScene().getWindow().hide();
					}else {
						loginStatus.setText("Incorrect login credentials!");
					}
				});
			}
		});
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
