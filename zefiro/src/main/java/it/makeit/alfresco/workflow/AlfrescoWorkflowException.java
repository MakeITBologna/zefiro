package it.makeit.alfresco.workflow;

public class AlfrescoWorkflowException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String errorKey;
	public AlfrescoWorkflowException(int code, String errorKey) {
		super("HTTP status " +code +": " + errorKey );
		this.code = code;
		this.errorKey = errorKey;
		
	}
	public int getCode() {
		return code;
	}
	public String getErrorKey() {
		return errorKey;
	}
	
	
	
}
