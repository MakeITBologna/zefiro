package it.makeit.alfresco.workflow.model;

import java.util.List;

public class FormModel {
	private String qualifiedName;
	private String dataType;
	private String name;
	private String title;
	private boolean required;
	private List<?> allowedValues;
	private String defaultValue;

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

	public List<?> getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(List<?> allowedValues) {
		this.allowedValues = allowedValues;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
