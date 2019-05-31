package it.makeit.zefiro.dao;

import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 *
 * @author MAKE IT
 */
public class DocumentBean implements BaseData {

	private String id;

	private String name;

	private String description;

	private String type;

	private String version;

	private java.sql.Timestamp created;

	private String createdBy;

	private Map<String, DocumentPropertyBean> properties;

	private String uploadedFileName;
	
	private String alfrescoDir;

	public DocumentBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(String pStrId) {
		id = pStrId;
	}

	public String getName() {
		return name;
	}

	public void setName(String pStrName) {
		name = pStrName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String pDescription) {
		description = pDescription;
	}

	public String getType() {
		return type;
	}

	public void setType(String pStrType) {
		type = pStrType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String pStrVersion) {
		version = pStrVersion;
	}

	public java.sql.Timestamp getCreated() {
		return created;
	}

	public String getUploadedFileName() {
		return this.uploadedFileName;
	}

	public void setUploadedFileName(String lString) {
		this.uploadedFileName = lString;
	}

	public void setCreated(java.sql.Timestamp pTimestampCreated) {
		created = pTimestampCreated;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String pStrCreatedBy) {
		createdBy = pStrCreatedBy;
	}

	public Map<String, DocumentPropertyBean> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, DocumentPropertyBean> pProperties) {
		if (pProperties == null) {
			throw new IllegalArgumentException();
		}
		properties = pProperties;
	}

	public DocumentPropertyBean getProperty(String pPropertyName) {
		return properties.get(pPropertyName);
	}

	public void setProperty(DocumentPropertyBean pProperty) {
		if (pProperty == null || pProperty.getQueryName() == null) {
			throw new IllegalArgumentException();
		}
		properties.put(pProperty.getQueryName(), pProperty);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getAlfrescoDir() {
		return alfrescoDir;
	}

	public void setAlfrescoDir(String alfrescoDir) {
		this.alfrescoDir = alfrescoDir;
	}
}
