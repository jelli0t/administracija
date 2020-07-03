/**
 * 
 */
package rs.neks.administration.util;

/**
 * @author jelles
 *
 */
public class Notification {

	private boolean affirmative;
	private String message;
	private String code;
	
	public Notification(boolean affirmative, String message, String code) {
		super();
		this.affirmative = affirmative;
		this.message = message;
		this.code = code;
	}

	public boolean isAffirmative() {
		return affirmative;
	}

	public void setAffirmative(boolean affirmative) {
		this.affirmative = affirmative;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
