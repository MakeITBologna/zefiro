package it.makeit.alfresco.workflow.model;

public class Deployment {

	private String id;
	private String name;
	private String category;
	private String deployedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDeployedAt() {
		return deployedAt;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setDeployedAt(String deployedAt) {
		this.deployedAt = deployedAt;
	}
}
