package it.makeit.alfresco.workflow.model;

public class TaskVariable {
	private String name;
	private String value;
	private String type;
	private String scope;

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getType() {
		return type;
	}

	public String getScope() {
		return scope;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
