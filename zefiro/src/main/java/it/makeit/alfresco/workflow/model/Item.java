package it.makeit.alfresco.workflow.model;

public class Item {
	private String id;
	private String name;
	private String title;
	private String description;
	private String mimeType;
	private String createdBy;
	private String createdAt;
	private String modifiedBy;
	private String modifiedAt;
	private double size;

	public String getId() {
		return id;
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

	public String getMimeType() {
		return mimeType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public String getModifiedAt() {
		return modifiedAt;
	}

	public double getSize() {
		return size;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public void setSize(double size) {
		this.size = size;
	}
}
