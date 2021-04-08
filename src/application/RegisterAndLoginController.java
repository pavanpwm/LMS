package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;  
import javax.persistence.*;  
import org.hibernate.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
	private TextField regPassword;
	@FXML
	private TextField loginEmail;
	@FXML
	private TextField loginPassword;
	@FXML
	private Label loginStatus;
	
	
	
	MainClass stage = new MainClass();
	StaffManagementService regService = new StaffManagementService();

	
	
	public void adminRegister(ActionEvent event) throws Exception {						// to handle exception you can wither use throws or try and  catch block
		StaffManagementService regService = new StaffManagementService();
			if( !( regName.getText().isEmpty() || regCollege.getText().isEmpty() || regEmail.getText().isEmpty() || regPassword.getText().isEmpty() )  && regService.registerStaff(regName.getText(), regCollege.getText(), regEmail.getText(), regPassword.getText(), "admin") ) {
				stage.runStageFXML("LoginPage.fxml");
				((Node)(event.getSource())).getScene().getWindow().hide();				// note the sequence of these two lines i.e first open a new stage and close previous one
			}
	}
	
	
	
	public void loginRedirect(ActionEvent event) throws Exception {
		if(regService.login(loginEmail.getText(), loginPassword.getText())) {
			stage.runStageFXML("HomePage.fxml");
			((Node)(event.getSource())).getScene().getWindow().hide();
		}else {
			loginStatus.setText("Incorrect login credentials!");
		}
	}

	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
