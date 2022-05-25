//Avraam Katsigras 321/2015087

package serverpackage;												

import java.io.Serializable;

@SuppressWarnings("serial") 
public class User implements Serializable{							//Class that represents the User
	private String name;											//User has a user name and a password
	private String pass;
	
	public User(String name, String pass){
		this.name = name;
		this.pass = pass;
	}
	
	public String getUname() {										//Getter for user name
		return name;
	}
	
	public String toString() {
		return name + " : " + pass;
	}
	
	@Override
	public boolean equals(Object o) {								//Override equals for convenience
		if(!(o instanceof User)) return false;
		User toauth = (User) o;
		return this.name.equals(toauth.name) && this.pass.equals(toauth.pass);
	}
}
