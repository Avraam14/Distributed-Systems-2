//Avraam Katsigras 321/2015087

package serverpackage;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")										//The notices that are written and displayed
public class Notice implements Serializable{					//Same as every class in this package; It's the same for each side
	private String content;										//The actual notice
	private User author;										//The author
	private LocalDate lastmod;									//Last modified date
	
	public Notice(String content, User author) {				//Constructor
		this.content = content;									//We get the content and the author
		this.author = author;
		lastmod = LocalDate.now();								//This is the last time this was modified
	}
	
	public String toString() {									//Display
		return '"' + content + '"' + " by: " + 
			author.getUname() + " Last Modified: " + lastmod;
	}

	public boolean checkDates(LocalDate from, LocalDate to) {	//Check if notice was modified between two dates
		return lastmod.isAfter(from) && lastmod.isBefore(to);
	}

	public boolean checkUser(User user) {						//Check if the user actually wrote this
		return this.author.equals(user);
	}
	
	@Override
	public boolean equals(Object o) {							//Override equals for convenience
		if(!(o instanceof Notice)) return false;
		Notice tocheck = (Notice) o;
		return this.content.equals(tocheck.content);
	}
}