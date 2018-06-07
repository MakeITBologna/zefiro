package it.makeit.alfresco.webscriptsapi.model;

import java.util.Map;

public class TaskInstance {
	private String id;
	private String url;
	private String name;
	private String title;
	private String description;
	private String state;
	private String path;
	private boolean isPooled;
	private boolean isEditable;
	private boolean isReassignable;
	private boolean isClaimable;
	private boolean isReleasable;
	private String outcome;
	private PersonUser owner;
	private Map<String, Object> properties;
	private Map<String, Object> propertyLabels;
	private WorkflowInstance workflowInstance;

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

	public String getState() {
		return state;
	}

	public String getPath() {
		return path;
	}

	public boolean isPooled() {
		return isPooled;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public boolean isReassignable() {
		return isReassignable;
	}

	public boolean isClaimable() {
		return isClaimable;
	}

	public boolean isReleasable() {
		return isReleasable;
	}

	public String getOutcome() {
		return outcome;
	}

	public PersonUser getOwner() {
		return owner;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public Map<String, Object> getPropertyLabels() {
		return propertyLabels;
	}

	public WorkflowInstance getWorkflowInstance() {
		return workflowInstance;
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

	public void setState(String state) {
		this.state = state;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPooled(boolean isPooled) {
		this.isPooled = isPooled;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public void setReassignable(boolean isReassignable) {
		this.isReassignable = isReassignable;
	}

	public void setClaimable(boolean isClaimable) {
		this.isClaimable = isClaimable;
	}

	public void setReleasable(boolean isReleasable) {
		this.isReleasable = isReleasable;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public void setOwner(PersonUser owner) {
		this.owner = owner;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void setPropertyLabels(Map<String, Object> propertyLabels) {
		this.propertyLabels = propertyLabels;
	}

	public void setWorkflowInstance(WorkflowInstance workflowInstance) {
		this.workflowInstance = workflowInstance;
	}
}
