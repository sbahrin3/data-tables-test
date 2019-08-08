package my.tester;

import com.github.javafaker.Faker;

import my.test.dt.Persistence;
import my.test.dt.entity.User;

public class TestFaker {
	
	public static void main(String[] args) throws Exception {
		
		Persistence db = Persistence.db();
		
		Faker faker = new Faker();
		
		for ( int i=0; i < 1000; i++ ) {
			String firstName = faker.name().firstName();
			String lastName = faker.name().lastName();
			String username = firstName.toLowerCase();
			System.out.println(username + ", " + firstName + ", " + lastName);
			
			User user = new User();
			user.setId("x" + i);
			user.setUserName("x" + i);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			
			db.save(user);
			
			
		}
		
		db.close();
		
	}

}
