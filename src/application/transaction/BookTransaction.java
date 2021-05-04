package application.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;



@NamedQueries(  
	    {  
	        @NamedQuery(  
	    	        name = "returnBook", 
	    	        query = "update BookTransaction b set b.returnDate = :returnDate, b.status = :status, b.remarks = :remarks  where b.id = :id"  			
	        		),
	        @NamedQuery(  
	    	        name = "getBookTransactionsByUsn", 
	    	        query = "from BookTransaction b where b.usn = :usn and b.status = :status"  			
	        		),
	        @NamedQuery(  
	    	        name = "findBookTransactionById", 
	    	        query = "from BookTransaction b where b.id = :id"  			
	        		),
	        @NamedQuery(  
	    	        name = "searchBookTransactions", 
	    	        query = "from BookTransaction b where upper(concat(b.usn, '', b.name, '', b.branch, '', b.sem, '', b.code, '', b.title, '', b.phone, '', b.email, '', b.issueDate, '', b.returnDate, '', b.status, '', b.remarks, '')) like upper(concat('%', :search, '%'))  order by b.id desc"  			
	        		),
	        @NamedQuery(  
	    	    	    name = "getBookTransactionsByStatus", 
	    	    	    query = "from BookTransaction b where b.status = :status  order by b.id desc"  			
	    	        )
	    } 
	    
) 




@Entity
@Table(name = "booktransaction")
public class BookTransaction implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "usn")
	private String usn;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "branch")
	private String branch;
	
	@Column(name = "sem")
	private String sem;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "issueDate")
	private String issueDate;
	
	@Column(name = "returnDate")
	private String returnDate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "remarks")
	private String remarks;


	public BookTransaction() {
		super();
	}
	
	public BookTransaction(String usn, String name, String branch, String sem, String code, String title, String phone,
			String email, String issuDate, String returnDate, String status, String remarks) {
		super();
		this.usn = usn;
		this.name = name;
		this.branch = branch;
		this.sem = sem;
		this.code = code;
		this.title = title;
		this.phone = phone;
		this.email = email;
		this.issueDate = issuDate;
		this.returnDate = returnDate;
		this.status = status;
		this.remarks = remarks;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getUsn() {
		return usn;
	}

	public void setUsn(String usn) {
		this.usn = usn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getSem() {
		return sem;
	}

	public void setSem(String sem) {
		this.sem = sem;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issuDate) {
		this.issueDate = issuDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	
	
}