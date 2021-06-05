package application.home;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;  
import javax.persistence.*;  
import org.hibernate.*;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;  



/**
 * 
 * @author pavan BTD
 *
 Note that controller classes uses hibernate package classes like session and transaction and not of javax since javax jpa was only used to avoid using hbm.xml mapping config file
 *
 *
 */


public class RegisterAndLoginController {
	
	
	
	

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
	private PasswordField loginRfid;
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
		}else if (staffService.getStaffByRfid(loginRfid.getText()) != null) {
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
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
