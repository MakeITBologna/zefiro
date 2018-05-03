package it.makeit.alfresco.restApi;

public class AlfrescoUrlException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static final String NULL_URL = "exception.nullUrl";

	public static final String PATH_PARAM = "exception.pathParam";

	public static final String METHOD = "exception.method";

	public AlfrescoUrlException() {
	}

	public AlfrescoUrlException(String message) {
		super(message);
	}
}
