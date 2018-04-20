package it.makeit.zefiro.dao;

public class MessageBean {
	private String message;
	
	public MessageBean(String pStrMessage){
		this.message = pStrMessage;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
