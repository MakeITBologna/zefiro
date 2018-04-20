package it.makeit.alfresco.workflow.model;

public class ProcessDefinition {
	private String id;
	private String key;
	private String name;
	private String category;
	private String deploymentId;
	private String title;
	private String description;
	private String startFormResourceKey;
	private Boolean graphicNotationDefined;
	private Integer version;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartFormResourceKey() {
		return startFormResourceKey;
	}

	public void setStartFormResourceKey(String startFormResourceKey) {
		this.startFormResourceKey = startFormResourceKey;
	}

	public Boolean getGraphicNotationDefined() {
		return graphicNotationDefined;
	}

	public void setGraphicNotationDefined(Boolean graphicNotationDefined) {
		this.graphicNotationDefined = graphicNotationDefined;
	}

}
