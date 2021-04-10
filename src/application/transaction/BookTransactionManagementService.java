package application.transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

import application.dbutil.HibernateUtil;
import javafx.collections.ObservableList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookTransactionManagementService {
	
	
	
	public void addNewBookTransaction(BookTransaction bookTransaction) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(bookTransaction);
            transaction.commit();
        }
	}
	

	public List<BookTransaction> getFullBookTransactionList() {
		List<BookTransaction> bookTransactionList;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			bookTransactionList = session.createQuery("from BookTransaction b", BookTransaction.class).list();
        }
		return bookTransactionList;
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


	public List<BookTransaction> getSearchedBookTransactionList(List<String> searchTokenList, String queryType) {
		List<BookTransaction> fullBookTransactionList = getFullBookTransactionList();
		List<BookTransaction> searchedBookTransactionList = new ArrayList<BookTransaction>();		// always use ArrayList to constucta list else returning a list might throw NPE
		if (queryType.equalsIgnoreCase("search") && !searchTokenList.isEmpty()) {
			for (int i = 0; i < searchTokenList.size(); i++) {
				for(int j=0; j<fullBookTransactionList.size(); j++) {
					if(fullBookTransactionList.get(j).toString().toLowerCase().contains(searchTokenList.get(i).toLowerCase())) {
						searchedBookTransactionList.add(fullBookTransactionList.get(j));
					}
				}
				fullBookTransactionList.clear();
				fullBookTransactionList.addAll(searchedBookTransactionList);
				searchedBookTransactionList.clear();
			}
			return fullBookTransactionList;	// dont mind the name
		}else if (queryType.equalsIgnoreCase("issued")) {
			for(int i=0; i<fullBookTransactionList.size(); i++) {
				if(fullBookTransactionList.get(i).getStatus().toString().toLowerCase().contains("issued")) {
					searchedBookTransactionList.add(fullBookTransactionList.get(i));
				}
			}
			return searchedBookTransactionList;
		}else if (queryType.equalsIgnoreCase("returned")) {
			for(int i=0; i<fullBookTransactionList.size(); i++) {
				if(fullBookTransactionList.get(i).getStatus().toString().toLowerCase().contains("returned")) {
					searchedBookTransactionList.add(fullBookTransactionList.get(i));
				}
			}
			return searchedBookTransactionList;
		}else if (queryType.equalsIgnoreCase("lost")) {
			for(int i=0; i<fullBookTransactionList.size(); i++) {
				if(fullBookTransactionList.get(i).getStatus().toString().toLowerCase().contains("lost")) {
					searchedBookTransactionList.add(fullBookTransactionList.get(i));
				}
			}
			return searchedBookTransactionList;
		}else if (queryType.equalsIgnoreCase("damaged")) {
			for(int i=0; i<fullBookTransactionList.size(); i++) {
				if(fullBookTransactionList.get(i).getStatus().toString().toLowerCase().contains("damaged")) {
					searchedBookTransactionList.add(fullBookTransactionList.get(i));
				}
			}
			return searchedBookTransactionList;
		}
		
		return searchedBookTransactionList;
		
	}


	public List<BookTransaction> getBookTransactionsByUsnAndIssued(String usn) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			TypedQuery query = session.getNamedQuery("getBookTransactionsByUsn");
            query.setParameter("usn",usn.toUpperCase());
            query.setParameter("status","Issued");
            List<BookTransaction> bookTransactionList = query.getResultList();
            return bookTransactionList;
		}
	}


	public BookTransaction getBookTransactionById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			TypedQuery query = session.getNamedQuery("findBookTransactionById");
            query.setParameter("id", id);
            List<BookTransaction> bookTransactionList = query.getResultList();
            return bookTransactionList.get(0);
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

	
	
	

	
	
	



	
	
}
