package it.makeit.alfresco.workflow.model;

public class Variable {
	private String name;
	private Object value;
	private String type;
	private String scope;

	public String getName() {
		return name;
	}

	public Object getValue() {
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

	public void setValue(Object value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
