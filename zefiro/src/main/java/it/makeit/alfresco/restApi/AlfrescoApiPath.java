package it.makeit.alfresco.restApi;

/**
 * @author Alba Quarto
 */
public enum AlfrescoApiPath {

	WORKFLOW("/alfresco/api/-default-/public/workflow/versions/1"), ALFRESCO(
			"/alfresco/api/-default-/public/alfresco/versions/1"), SERVICE("/alfresco/s/api");

	private String path;

	AlfrescoApiPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}
}
