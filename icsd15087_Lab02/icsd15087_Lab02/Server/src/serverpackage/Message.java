//Avraam Katsigras 321/2015087

package serverpackage;

import java.io.Serializable;

@SuppressWarnings("serial")										//This is the same for every side in order to avoid serialization problems
public class Message implements Serializable{					//The messages sent back and forth are of this type
	private String message;										//The message code ("CREATE", "MODIFY, "DELETE", "SUCCESS", "NOAUTH" etc)
	private Object content;										//The content. Can be a user, a list of notices, anything the other side will need
	private User user;											//The author of the message. For authentication purposes
	
	public Message(String message) {							//Constructor for basic requests
		this.message = message;									//We only need the message code
		content = null;
		user = null;
	}
	
	public Message(String message, Object content, User user) {	//Constructor for more complex requests
		this.message = message;									//Get the message, content and user
		this.content = content;
		this.user = user;
	}
	
	public String message() {									//Getters
		return message;
	}
	
	public Object content() {
		return content;
	}
	
	public User user() {
		return user;
	}
}
