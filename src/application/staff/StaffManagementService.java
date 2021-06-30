package application.staff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.dbutil.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



/**
 * 
 * @author pavan BTD
 *
 Note that service classes uses hibernate package classes like session and transaction and not of javax since javax jpa was only used to avoid using hbm.xml mapping config file
 *
 *
 */



public class StaffManagementService {

	
	public static Staff loggedInStaff;
	
	
	//add method /////////////////////////////////////////////////////////////////////////
	
	public boolean registerStaff(String name, String college, String email, String password, String role,String rfid) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	Staff s = getStaffByEmail(email);
        	Staff s2 = getStaffByRfid(rfid);
            if(s == null && ( rfid.isEmpty() || s2 == null) ) {
                transaction = session.beginTransaction();
                Staff staff = new Staff(name, college, email, password, role, rfid);
                session.save(staff);
                transaction.commit();
                return true;
            }else {
            	return false;
            }
        }
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	//Retrieve methods ////////////////////////////////////////////////////////////////////////////////////

	

	public boolean isRegisterd() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	TypedQuery query = session.getNamedQuery("checkIfRegistered");
            query.setParameter("role","admin");   
            List<Staff> admin = query.getResultList();
            if(!admin.isEmpty()) {
            	return true;
            }else {
            	return false;
            }
        }
	}
	
	public boolean login(String email, String password) {
		 try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        	TypedQuery query = session.getNamedQuery("checkStaffLogin");
	            query.setParameter("email",email);   
	            query.setParameter("password", password);
	            List<Staff> staff = query.getResultList();
	            if(!staff.isEmpty()) {
	            	loggedInStaff = staff.get(0);
	            	return true;
	            }else {
	            	return false;
	            }
	     }
	}
	
	// to get password and mail it.
	public Staff getStaffByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			TypedQuery query = session.getNamedQuery("findStaffByEmail");
	        query.setParameter("email",email);   
	        List<Staff> staffList = query.getResultList();
	        if(!staffList.isEmpty()) {
	        	return staffList.get(0);
	        }
			return null;
		}
	}
	
	public Staff getStaffByRfid(String rfid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			TypedQuery query = session.getNamedQuery("findStaffByRfid");
	        query.setParameter("rfid",rfid);   
	        List<Staff> staffList = query.getResultList();
	        if(!staffList.isEmpty()) {
	        	loggedInStaff = staffList.get(0);
	        	return staffList.get(0);
	        }
			return null;
		}
	}
	
	
	public List<Staff> getFulltStaffList() {
		List<Staff> staffList;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			staffList = session.createQuery("from Staff s", Staff.class).list();
        }
		return staffList;
	}
	
	public List<Staff> getObservableStaffList(List<Staff> staffListForTable) {
			ObservableList<Staff> data = FXCollections.observableArrayList();
	        Iterator<Staff> staffListIterator = staffListForTable.iterator();
	        while (staffListIterator.hasNext()) {
	            Staff eachStaff = (Staff) staffListIterator.next();
	            data.add(eachStaff);
	        }
	        return data;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	
	
	//Delete method ////////////////////////////////////////////////////////////////////////////////////

	public void deleteStaff(Staff deleteStaff) {
		// TODO Auto-generated method stub
		Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(deleteStaff);
            transaction.commit();
        }
	}

	public List<Staff> getStaffListWithoutAdmin() {
		List<Staff> staffList;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			staffList = session.createQuery("from Staff s where s.role='staff'", Staff.class).list();
        }
		return staffList;
	}
	 
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	 
	 
	 
	 
	
	 
	 
	 
	
}
