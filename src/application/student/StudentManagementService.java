package application.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.dbutil.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentManagementService {

	
	
	public boolean addOrEditStudent(String id, String usn, String name, String branch, String sem, String mobile, String email) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	
        	//if id is not empty then update else save
        	if(id.isEmpty()) {
        		// check if the usn exists! then follow
        		Student getStudentbyUsn = getStudentByUsn(usn);
                if(getStudentbyUsn == null) {
                	//save new student
                    transaction = session.beginTransaction();
                    Student student = new Student(name, branch, sem, usn.toUpperCase(), email, mobile);
                    session.save(student);
                    transaction.commit();
                    return true;
                }else {
                	//update existing student   :  this is for importing data from excel
            		transaction = session.beginTransaction();
            		getStudentbyUsn.setName(name);
                    getStudentbyUsn.setBranch(branch);
                    getStudentbyUsn.setUsn(usn.toUpperCase().trim());
                    getStudentbyUsn.setEmail(email);
                    getStudentbyUsn.setSem(sem);
                    getStudentbyUsn.setMobile(mobile);
                    session.update(getStudentbyUsn);
                    transaction.commit();
                    return true;
                }
        	}else {
        		//update existing student
        		transaction = session.beginTransaction();
                Student student = new Student(Integer.parseInt(id), name, branch, sem, usn.toUpperCase(), email, mobile) ;
                session.update(student);
                transaction.commit();
                return true;
        	}
        }
	}
	
	
	

	public List<Student> getFullStudentList() {
		List<Student> studentList;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			studentList = session.createQuery("from Student s", Student.class).list();
        }
		return studentList;
	}
	
	
	
	
	public ObservableList<Student> getObservableStudentList(List<Student> studentListForTable) {
			ObservableList<Student> data;
	        data = FXCollections.observableArrayList();
	        Iterator<Student> studentListIterator = studentListForTable.iterator();
	        while (studentListIterator.hasNext()) {
	            Student eachStudent = (Student) studentListIterator.next();
	            data.add(eachStudent);
	        }
	        return data;
	}


	public List<Student> getSearchedStudentList(String searchText) {
		List<Student> fullStudentList = getFullStudentList();
		List<Student> searchedStudentList = new ArrayList<Student>();							// always use ArrayList to constucta list else returning a list might throw NPE
		for(int i=0; i<fullStudentList.size(); i++) {
			if(fullStudentList.get(i).toString().toLowerCase().contains(searchText.toLowerCase())) {
				searchedStudentList.add(fullStudentList.get(i));
			}
		}
		return searchedStudentList;
	}


	public void deleteStudent(Student deleteStudent) {
		// TODO Auto-generated method stub
		Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(deleteStudent);
            transaction.commit();
        }
	}
	
	
	
	
	// this method is for issue book
	public Student getStudentByUsn(String usn) {
		List<Student> studentList;
		 try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			 TypedQuery query = session.getNamedQuery("findStudentByUsn");
             query.setParameter("usn",usn.toUpperCase().trim());
             studentList = query.getResultList();
		 }
		 if (studentList.isEmpty()) {
			return null;
		}else {
			return studentList.get(0);
		}
	}
	
	
	
	
	
	
	
}
