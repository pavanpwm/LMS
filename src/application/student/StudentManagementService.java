package application.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import application.dbutil.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentManagementService {
	
	public static int lastPageIndex = 0;
	public static int pageSize = 50;
	public static String searchText = "";

	
	//add and update methods /////////////////////////////////////////////////////////////////////////

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
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////


	
	//Retrieve methods ////////////////////////////////////////////////////////////////////////////////////

	
	
	public List<Student> getNextScrollStudents() {
		List<Student> studentList =  new ArrayList<Student>();
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			if (getStudentsAddedCount() >= lastPageIndex+1) {
				//paginating results
			    Query query;
			    if (!searchText.isEmpty()) {
			    	query = session.getNamedQuery("searchStudents");
			    	query.setParameter("search", searchText);
				}else {
					query = session.createQuery("from Student s order by s.id desc");
				} 
			    query.setFirstResult(lastPageIndex);
			    query.setMaxResults(pageSize);
			    lastPageIndex += pageSize;
			    studentList = query.list();
			}
        }
		return studentList;
	}
	
	
	public Long getStudentsAddedCount() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query query = session.createQuery("select count(s.id) from Student s");
		    Long count = (Long) query.uniqueResult();
			return  count;
        }
	}
	
	
	public ObservableList<Student> getObservableStudentList(List<Student> studentListForTable) {
			ObservableList<Student> data = FXCollections.observableArrayList();
	        Iterator<Student> studentListIterator = studentListForTable.iterator();
	        while (studentListIterator.hasNext()) {
	            Student eachStudent = (Student) studentListIterator.next();
	            data.add(eachStudent);
	        }
	        return data;
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


	///////////////////////////////////////////////////////////////////////////////////////////////////////


	//Delete method ////////////////////////////////////////////////////////////////////////////////////

	public void deleteStudent(Student deleteStudent) {
		// TODO Auto-generated method stub
		Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(deleteStudent);
            transaction.commit();
        }
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	
	
	
	
	
	
	
	
}
