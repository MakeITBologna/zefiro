package it.makeit.alfresco.webscriptsapi.model;

public class WorkflowDefinition {

	private String id;
	private String url;
	private String name;
	private String title;
	private String description;
	private int version;

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public int getVersion() {
		return version;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
