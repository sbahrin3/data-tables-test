package my.test.dt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="users")
public class User {
	
	@Id @Column(name="id", length=200)
	private String id;
	@Column(length=100)
	private String userName;
	@Column(length=100)
	private String firstName;
	@Column(length=100)
	private String lastName;

	
	public User() {
		setId(UIDGenerator.getUID());
	}
	
	public User(String userName, String firstName, String lastName) {
		setId(userName);
		setUserName(userName);
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
}
