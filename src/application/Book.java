package application;

import java.io.Serializable;

import javax.persistence.*;




@NamedQueries(  
	    {  
	        @NamedQuery(  
	        name = "checkIfBookExists",  
	        query = "from Book b where b.code = :code"  		
	        		),
	        @NamedQuery(  
	    	        name = "updateBook", 
	    	        query = "update Book b set b.title = :title, b.shelf = :shelf  where b.code = :code"  			
	        		),
	        @NamedQuery(  
	    	        name = "updateBookStatus", 
	    	        query = "update Book b set b.status = :status  where b.code = :code"  			
	        		)
	        
	        
	    } 
	    
) 




@Entity
@Table(name = "book")
public class Book implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "shelf")
	private String shelf;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "price")
	private String price;

	public Book() {
		super();
	}

	public Book(String code, String title, String shelf, String status, String price) {
		super();
		this.code = code;
		this.title = title;
		this.shelf = shelf;
		this.status = status;
		this.price = price;
	}

	public Book(int id, String code, String title, String shelf, String status, String price) {
		super();
		this.id = id;
		this.code = code;
		this.title = title;
		this.shelf = shelf;
		this.status = status;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return code + "___" + title + "___" + shelf + "___" + status;
	}
	
	
	

	
	
	
	

}
