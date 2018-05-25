package it.makeit.alfresco.workflow;

public enum ResponseBodyPartEnum {
	ENTRY("entry"), ENTRIES("entries"), LIST("list"), ERROR("error");

	private String partName;

	ResponseBodyPartEnum(String partName) {
		this.partName = partName;
	}

	public String partName() {
		return partName;
	}
}
