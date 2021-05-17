package application.transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import application.book.Book;
import application.dbutil.HibernateUtil;
import javafx.collections.ObservableList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookTransactionManagementService {
	
	public static int lastPageIndex = 0;
	public static int pageSize = 5;
	public static String searchText = "";
	public static String searchType = "";
	
	
	
	//add and update methods /////////////////////////////////////////////////////////////////////////

	public void addNewBookTransaction(BookTransaction bookTransaction) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(bookTransaction);
            transaction.commit();
        }
	}
	
	public boolean updateBookTransactionById(int id, String returnDate, boolean damaged, boolean lost, String remarks) {
        Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			TypedQuery query = session.getNamedQuery("returnBook");
			query.setParameter("id",id);
			query.setParameter("returnDate",returnDate);
			if (damaged) {
				query.setParameter("status","Damaged");
			}else if(lost) {
				query.setParameter("status","Lost");
			}else {
				query.setParameter("status","Returned");
			}
	        query.setParameter("remarks",remarks);
	        query.executeUpdate();
	        transaction.commit();
	        return true;
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	
	//Retrieve methods ////////////////////////////////////////////////////////////////////////////////////


	
	public List<BookTransaction> getNextScrollBookTransactions() {
		List<BookTransaction> bookTransactionList = new ArrayList<BookTransaction>();
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			if (getBookTransactionsAddedCount() >= lastPageIndex+1) {
				//paginating results
			    Query query;
			    if (!searchText.isEmpty() && searchType.equals("loose")) {
			    	query = session.getNamedQuery("searchBookTransactions");
			    	query.setParameter("search", searchText);
				}else if (searchType.equals("tight")) {
					query = session.getNamedQuery("getBookTransactionsByStatus");
				    query.setParameter("status", searchText);
				}else{
					query = session.createQuery("from BookTransaction b order by b.id desc");
				} 
			    query.setFirstResult(lastPageIndex);
			    query.setMaxResults(pageSize);
			    lastPageIndex += pageSize;
			    bookTransactionList = query.list();
			}
        }
		return bookTransactionList;
	}
	
	
	public int getBookTransactionsCountForBarChart(String month){
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query query = session.getNamedQuery("searchBookTransactions");
			query.setParameter("search", month);
		    int count =  query.list().size();
			return  count;
        }
	}
	

	
	public Long getBookTransactionsAddedCount() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query query = session.createQuery("select count(b.id) from BookTransaction b");
		    Long count = (Long) query.uniqueResult();
			return  count;
        }
	}

	public ObservableList<BookTransaction> getObservableBookTransactionList(List<BookTransaction> bookTransactionListForTable) {
			ObservableList<BookTransaction> data;
	        data = FXCollections.observableArrayList();
	        Iterator<BookTransaction> bookTransactionListIterator = bookTransactionListForTable.iterator();
	        while (bookTransactionListIterator.hasNext()) {
	            BookTransaction eachBookTransaction = (BookTransaction) bookTransactionListIterator.next();
	            data.add(eachBookTransaction);
	        }
	        return data;
	}

	
	//for return book
	public List<BookTransaction> getBookTransactionsByUsnAndIssued(String usn) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			TypedQuery query = session.getNamedQuery("getBookTransactionsByUsn");
            query.setParameter("usn",usn.toUpperCase());
            query.setParameter("status","Issued");
            List<BookTransaction> bookTransactionList = query.getResultList();
            return bookTransactionList;
		}
	}

	//for return book
	public BookTransaction getBookTransactionById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			TypedQuery query = session.getNamedQuery("findBookTransactionById");
            query.setParameter("id", id);
            List<BookTransaction> bookTransactionList = query.getResultList();
            return bookTransactionList.get(0);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////

	

	
	
	

	
	
	



	
	
}
