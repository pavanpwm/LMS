package application.book;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import application.dbutil.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class BookManagementService {

	public static int lastPageIndex = 0;
	public static int pageSize = 2;
	public static String searchText = "";
	
	public boolean addOrEditBook(String id, String code, String title, String shelf, String status) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	//if id is not empty then update else save
        	if(id.isEmpty()) {
        		if(status.toLowerCase().contains("req")) {
        			//save new book
                    transaction = session.beginTransaction();
                    Book book = new Book(code.toUpperCase(), title, shelf, status, "");
                    session.save(book);
                    transaction.commit();
                    return true;
        		}else{
        			// check if the book code exists! then follow
            		TypedQuery query = session.getNamedQuery("checkIfBookExists");
                    query.setParameter("code",code.toUpperCase());
                    List<Book> bookList = query.getResultList();
                    if(bookList.isEmpty()) {
                    	//save new book
                        transaction = session.beginTransaction();
                        Book book = new Book(code.toUpperCase(), title, shelf, status, "");
                        session.save(book);
                        transaction.commit();
                        return true;
                    }else {
                    	return false;
                    }
        		}
        	}else {
        		//update existing book
        		transaction = session.beginTransaction();
        		TypedQuery query = session.getNamedQuery("updateBook");
        		query.setParameter("title",title);
        		query.setParameter("shelf",shelf);
                query.setParameter("code",code.toUpperCase());
                query.executeUpdate();
                transaction.commit();
                return true;
        	}
        }
	}
	
	
	

	public List<Book> getNextScrollBooks() {
		List<Book> bookList;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			
		    //paginating results
		    Query selectQuery;
		    if (!searchText.isEmpty()) {
		    	selectQuery = session.getNamedQuery("searchBooks");
		    	selectQuery.setParameter("search", searchText);
		    	System.out.println("above");
			}else {
				selectQuery = session.createQuery("from Book b order by b.id desc");
		    	System.out.println("below");

			} 
		    selectQuery.setFirstResult(lastPageIndex);
		    selectQuery.setMaxResults(pageSize);
		    lastPageIndex += pageSize;
		    bookList = selectQuery.list();
		    return bookList;
        }catch (Exception e) {
			// TODO: handle exception
        	System.out.println(e);
		}
		return null;
		
	}
	
	
	public Long getBooksAddedCount() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query countQuery = session.createQuery("select count(b.id) from Book b");
		    Long countResults = (Long) countQuery.uniqueResult();
			return  countResults;
        }
	}
	
	
	public Long getBooksAvailableCount() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query query = session.createQuery("select count(b.id) from Book b where b.status='Available'");
			Long count = (Long) query.iterate().next();
			return count;
        }
	}
	
	
	
	
	
	
	public ObservableList<Book> data;
	public List<Book> getObservableBookList(List<Book> bookListForTable) {
	        data = FXCollections.observableArrayList();
	        Iterator<Book> bookListIterator = bookListForTable.iterator();
	        while (bookListIterator.hasNext()) {
	            Book eachBook = (Book) bookListIterator.next();
	            data.add(eachBook);
	        }
	        return data;
	}


//	public List<Book> getSearchedBookList(String searchText) {
//		List<Book> fullBookList = getFullBookList();
//		List<Book> searchedBookList = new ArrayList<Book>();							// always use ArrayList to construct a list else returning a list might throw NPE
//		for(int i=0; i<fullBookList.size(); i++) {
//			if(fullBookList.get(i).toString().toLowerCase().contains(searchText.toLowerCase())) {
//				searchedBookList.add(fullBookList.get(i));
//			}
//		}
//		return searchedBookList;
//	}


	public void deleteBook(Book deleteBook) {
		// TODO Auto-generated method stub
		Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(deleteBook);
            transaction.commit();
        }
	}




	public Book getBookByBookCode(String code) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			TypedQuery query = session.getNamedQuery("checkIfBookExists");
            query.setParameter("code",code.toUpperCase());
            List<Book> bookList = query.getResultList();
            if (bookList.isEmpty()) {
				return null;
			}
            return bookList.get(0);
		}
	}
	
	
	public void  updateBookStatus(String status, String code) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        		Transaction transaction = session.beginTransaction();
        		TypedQuery uQuery = session.getNamedQuery("updateBookStatus");
        		uQuery.setParameter("code",code.toUpperCase());
        		uQuery.setParameter("status", status);
                uQuery.executeUpdate();
                transaction.commit();
		}
	}
	
	
	
	
	
}
