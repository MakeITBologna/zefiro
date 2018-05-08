package it.makeit.alfresco.workflow.model;

public class FormModel {
	private String qualifiedName;
	private String dataType;
	private String name;
	private String title;
	private boolean required;

	public String getQualifiedName() {
		return qualifiedName;
	}

	public String getDataType() {
		return dataType;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public boolean isRequired() {
		return required;
	}

	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
