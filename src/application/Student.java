package application;

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
	        name = "findStudentByUsn",  
	        query = "from Student s where s.usn = :usn"  		
	        		)
	    } 
	    
)  


@Entity
@Table(name = "student")
public class Student implements Serializable {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "branch")
	private String branch;

	@Column(name = "sem")
	private String sem;

	@Column(name = "usn", unique = true)
	private String usn;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile")
	private String mobile;
	
	

	public Student() {
		
	}
	
	



	public Student(int id, String name, String branch, String sem, String usn, String email, String mobile) {
		super();
		this.id = id;
		this.name = name;
		this.branch = branch;
		this.sem = sem;
		this.usn = usn;
		this.email = email;
		this.mobile = mobile;
	}




	public Student(String name, String branch, String sem, String usn, String email, String mobile) {
		super();
		this.name = name;
		this.branch = branch;
		this.sem = sem;
		this.usn = usn;
		this.email = email;
		this.mobile = mobile;
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



	public String getUsn() {
		return usn;
	}



	public void setUsn(String usn) {
		this.usn = usn;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	@Override
	public String toString() {
		return name + "________" + usn + "________" + branch + "________" + mobile + "________" + sem + "________" + email;
	}
	
	
	
	
	
	
	
	

	
	
	
	
	

}
