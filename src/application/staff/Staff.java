package application.staff;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * @author pavan BTD
 *
 Note that entity or model class use javax annotations and not hibernate(we can use for eg. hibernate validations) unlike the controller class
 i.e we are use hibernate with JPA annotations to avoid using hibernate mapping xml -hbm.xml
 *
 *
 */




@NamedQueries(  
	    {  
	        @NamedQuery(  
	        name = "checkIfRegistered",  
	        query = "from Staff s where s.role = :role"  			// S in Staff should be capital
	        		),
	        @NamedQuery(  
	    	        name = "checkStaffLogin", 
	    	        query = "from Staff s where s.email = :email  and  s.password = :password"  			
	        		),
	        @NamedQuery(  
	    	        name = "findStaffByEmail", 
	    	        query = "from Staff s where s.email = :email"  			
	        		),
	        @NamedQuery(  
	    	        name = "findStaffByRfid", 
	    	        query = "from Staff s where s.rfid = :rfid"  			
	        		)
	        
	        
	    } 
	    
)  


@Entity
@Table(name = "staff")
public class Staff implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "college")
	private String college;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "rfid")
	private String rfid;

	public Staff() {
	}
	

	public Staff(String name, String college, String email, String password, String role, String rfid) {
		this.name = name;
		this.college = college;
		this.email = email;
		this.password = password;
		this.role = role;
		this.rfid = rfid;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	public String getRfid() {
		return rfid;
	}


	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	
	


}
