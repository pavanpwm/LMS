package application.home;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import application.dbutil.HibernateUtil;
import application.staff.StaffManagementService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsPageService {
	
	
	
	public boolean updatePersonalDetailsService(int id, String name, String email, String password) {
		
		Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            StaffManagementService.loggedInStaff.setId(id);
            if(!name.isEmpty()) {
            	StaffManagementService.loggedInStaff.setName(name);
            }else {
            }
            if(!email.isEmpty()) {
            	StaffManagementService.loggedInStaff.setEmail(email);
            }
            if(!password.isEmpty()) {
            	StaffManagementService.loggedInStaff.setPassword(password);
            }
            // save the staff objects
            session.update(StaffManagementService.loggedInStaff);					// use update and not save() method
            // commit transaction
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Transaction error");
            return false;
        }
	}
	
	
	
	public static void backupDb(Stage stage) throws Exception {
		DirectoryChooser dir_chooser = new DirectoryChooser();
		dir_chooser.setTitle("Select a folder to backup your databse");
        File loc = dir_chooser.showDialog(stage);
        Path to = Paths.get(loc.getAbsolutePath() + "/lmsdb.db");
        Path from = Paths.get("lmsdb.db");
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
	}
	
	
	public static void restoreDb(Stage stage) throws Exception {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText("Restore Database");
		alert.getDialogPane().setContentText("Are you sure you want to restore database? You may loose all your current data!!");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Select the database file to restore");
	        FileChooser.ExtensionFilter extFilter
	                = new FileChooser.ExtensionFilter("DB files (*.db)", "*.db");
	        fileChooser.getExtensionFilters().add(extFilter);
	        File loc = fileChooser.showOpenDialog(stage);
	        Path to = Paths.get(System.getProperty("user.dir") + "/lmsdb.db");
	        Path from = Paths.get(loc.getAbsolutePath());
	        HibernateUtil.shutdown();
	        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
	        Alert alert1 = new Alert(Alert.AlertType.INFORMATION,"");
			alert1.initModality(Modality.APPLICATION_MODAL);
			alert1.initOwner(stage);
			alert1.getDialogPane().setHeaderText("Database restored successfully");
			alert1.getDialogPane().setContentText("Please restart your application.");
			result = alert1.showAndWait();
			Platform.exit();
			System.exit(0);
		}
	}
	
	
	public static void clearTables(Stage stage, CheckBox transactions, CheckBox books, CheckBox students) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText("Clear tables");
		alert.getDialogPane().setContentText("Are you sure you want to clear selected tables?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			if (transactions.isSelected()) {
				try (Session session = HibernateUtil.getSessionFactory().openSession()) {
					Transaction transaction = session.beginTransaction();
					Query query = session.createQuery("delete from BookTransaction b");
				    query.executeUpdate();
				    transaction.commit();
		        }
			}
			if (books.isSelected()) {
				try (Session session = HibernateUtil.getSessionFactory().openSession()) {
					Transaction transaction = session.beginTransaction();
					Query query = session.createQuery("delete from Book b");
				    query.executeUpdate();
				    transaction.commit();
		        }
			}
			if (students.isSelected()) {
				try (Session session = HibernateUtil.getSessionFactory().openSession()) {
					Transaction transaction = session.beginTransaction();
					Query query = session.createQuery("delete from Student b");
				    query.executeUpdate();
				    transaction.commit();
		        }
			}
		}
	}



	public static void deleteDbAndRestart(Stage stage) throws IOException {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText("Clear tables");
		alert.getDialogPane().setContentText("Are you sure you want to RESET evreyting?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			HibernateUtil.shutdown();
			Files.delete(Paths.get("lmsdb.db"));
			Alert alert1 = new Alert(Alert.AlertType.INFORMATION,"");
			alert1.initModality(Modality.APPLICATION_MODAL);
			alert1.initOwner(stage);
			alert1.getDialogPane().setHeaderText("Reset successful.");
			alert1.getDialogPane().setContentText("Please restart your application.");
			result = alert1.showAndWait();
			Platform.exit();
			System.exit(0);
		}
		
	}
	
	
	

}
