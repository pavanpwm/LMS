package application.home;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.dbutil.HibernateUtil;
import application.staff.StaffManagementService;

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

}
