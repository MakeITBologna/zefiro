package it.makeit.zefiro;

/**
 * 
 * @author Alseny Cissé
 *
 */
public enum MimeType {
	PDF("application/pdf"), OCTET_STREAM("application/octet-stream"), JSON("application/json");
	private String type;
	private MimeType(String type) {
		this.type = type;
	}
	
	public String value() {
		return type;
	}
}
